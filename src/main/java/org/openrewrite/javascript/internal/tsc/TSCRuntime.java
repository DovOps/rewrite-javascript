/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.javascript.internal.tsc;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interception.logging.JavetStandardConsoleInterceptor;
import com.caoccao.javet.interop.JavetBridge;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.utils.JavetResourceUtils;
import com.caoccao.javet.values.reference.V8ValueArray;
import com.caoccao.javet.values.reference.V8ValueFunction;
import com.caoccao.javet.values.reference.V8ValueMap;
import com.caoccao.javet.values.reference.V8ValueObject;
import org.intellij.lang.annotations.Language;
import org.openrewrite.IOUtils;
import org.openrewrite.internal.lang.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class TSCRuntime implements Closeable {
    /**
     * Manually enable this when tracking down reference-counting issues.
     * <br/>
     * It causes tests to fail if references are not recycled, and will
     * attribute dangling references to the call site that created them.
     */
    private final static boolean USE_WRAPPED_V8_RUNTIME = false;

    public final V8Runtime v8Runtime;

    @Nullable
    public V8ValueFunction tsParse = null;

    private final JavetStandardConsoleInterceptor javetStandardConsoleInterceptor;

    public static TSCRuntime init() {
        try {
            V8Runtime v8Runtime = USE_WRAPPED_V8_RUNTIME ? JavetBridge.makeWrappedV8Runtime() : V8Host.getV8Instance().createV8Runtime();
            JavetStandardConsoleInterceptor javetStandardConsoleInterceptor = new JavetStandardConsoleInterceptor(v8Runtime);
            javetStandardConsoleInterceptor.register(v8Runtime.getGlobalObject());
            return new TSCRuntime(v8Runtime, javetStandardConsoleInterceptor);
        } catch (JavetException e) {
            throw new RuntimeException(e);
        }
    }

    public TSCRuntime(V8Runtime v8Runtime, JavetStandardConsoleInterceptor javetStandardConsoleInterceptor) {
        this.v8Runtime = v8Runtime;
        this.javetStandardConsoleInterceptor = javetStandardConsoleInterceptor;
    }

    private static String getJSEntryProgramText() {
        try (InputStream is = TSCRuntime.class.getClassLoader().getResourceAsStream("index.js")) {
            if (is == null) throw new IllegalStateException("entry JS resource does not exist");
            return IOUtils.readFully(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importTS() {
        if (tsParse != null) {
            return;
        }
        try {
            v8Runtime.getExecutor("const require = () => undefined;").executeVoid();
            v8Runtime.getExecutor("const module = {exports: {}};").executeVoid();
            v8Runtime.getExecutor(getJSEntryProgramText()).executeVoid();
            this.tsParse = v8Runtime.getExecutor("module.exports.default").execute();
        } catch (JavetException e) {
            throw new RuntimeException(e);
        }
    }

    /** Only intended for testing and debugging. */
    public void parseSingleSource(@Language("typescript") String sourceText, BiConsumer<TSCNode, TSCSourceFileContext> callback) {
        Map<String, String> sources = new HashMap<>();
        sources.put("example.ts", sourceText);
        parseSourceTexts(sources, callback);
    }

    public void parseSourceTexts(Map<String, String> sourceTexts, BiConsumer<TSCNode, TSCSourceFileContext> callback) {
        importTS();
        assert tsParse != null;
        try (V8ValueMap sourceTextsV8 = v8Runtime.createV8ValueMap()) {
            for (Map.Entry<String, String> entry : sourceTexts.entrySet()) {
                sourceTextsV8.set(entry.getKey(), entry.getValue());
            }
            try (
                    V8ValueObject parseResultV8 = tsParse.call(null, sourceTextsV8);
                    TSCProgramContext programContext = TSCProgramContext.fromJS(parseResultV8);
                    V8ValueArray sourceFilesV8 = parseResultV8.get("sourceFiles")
            ) {
                sourceFilesV8.forEach((sourceFileV8) -> {
                    String sourceText = ((V8ValueObject) sourceFileV8).invokeString("getText");
                    try (TSCSourceFileContext sourceFileContext = new TSCSourceFileContext(programContext, sourceText)) {
                        TSCNode node = programContext.tscNode((V8ValueObject) sourceFileV8);
                        callback.accept(node, sourceFileContext);
                    }
                });
            }
        } catch (JavetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        JavetResourceUtils.safeClose(this.tsParse);

        if (!v8Runtime.isClosed()) {
            v8Runtime.await();
            v8Runtime.lowMemoryNotification();

            try {
                javetStandardConsoleInterceptor.unregister(v8Runtime.getGlobalObject());
            } catch (JavetException ignored) {
            }
            v8Runtime.await();
            v8Runtime.lowMemoryNotification();
        }

        try {
            v8Runtime.close();
        } catch (JavetException e) {
            throw new RuntimeException(e);
        }
    }
}