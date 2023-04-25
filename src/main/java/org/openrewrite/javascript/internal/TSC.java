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
package org.openrewrite.javascript.internal;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interception.logging.JavetStandardConsoleInterceptor;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.callback.JavetCallbackContext;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueInteger;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueFunction;
import com.caoccao.javet.values.reference.V8ValueMap;
import com.caoccao.javet.values.reference.V8ValueObject;
import org.openrewrite.IOUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public interface TSC {

    static String getJSEntryProgramText() {
        try (InputStream is = TSC.class.getClassLoader().getResourceAsStream("index.js")) {
            if (is == null) throw new IllegalStateException("entry JS resource does not exist");
            return IOUtils.readFully(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class Runtime implements Closeable {
        public final V8Runtime v8Runtime;
        public V8ValueFunction tsParse;
        private final JavetStandardConsoleInterceptor javetStandardConsoleInterceptor;

        public static Runtime init() {
            try {
                V8Runtime v8Runtime = V8Host.getV8Instance().createV8Runtime();
                JavetStandardConsoleInterceptor javetStandardConsoleInterceptor = new JavetStandardConsoleInterceptor(v8Runtime);
                javetStandardConsoleInterceptor.register(v8Runtime.getGlobalObject());
                return new Runtime(v8Runtime, javetStandardConsoleInterceptor);
            } catch (JavetException e) {
                throw new RuntimeException(e);
            }
        }

        public Runtime(V8Runtime v8Runtime, JavetStandardConsoleInterceptor javetStandardConsoleInterceptor) {
            this.v8Runtime = v8Runtime;
            this.javetStandardConsoleInterceptor = javetStandardConsoleInterceptor;
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
                this.tsParse.setWeak();
            } catch (JavetException e) {
                throw new RuntimeException(e);
            }
        }

        public <T> T parseSourceText(String sourceText, Function<TSC.Node, T> callback) {
            importTS();
            try {
                try (V8Value tmp = tsParse.call(null, sourceText)) {
                    if (!(tmp instanceof V8ValueObject)) {
                        throw new RuntimeException();
                    }

                    V8ValueObject obj = (V8ValueObject) tmp;
                    TSC.Context context;
                    TSC.Node node;

                    try (V8Value metaV8 = obj.get("meta")) {
                        if (!(metaV8 instanceof V8ValueObject)) {
                            throw new RuntimeException("expected `meta` to be an Object");
                        }
                        context = TSC.Context.fromJS(metaV8);
                        System.out.println(context.syntaxKindCode("InterfaceDeclaration"));
                    }

                    try (V8Value nodeV8 = obj.get("sourceFile")) {
                        node = context.tscNode(nodeV8);
                        return callback.apply(node);
                    }
                }
            } catch (JavetException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {
            if (!v8Runtime.isClosed()) {
                v8Runtime.await();
                v8Runtime.lowMemoryNotification();

                try {
                    javetStandardConsoleInterceptor.unregister(v8Runtime.getGlobalObject());
                } catch (JavetException e) {
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

    interface ContextCallback {
        V8Value apply(V8Value... args);
    }

    class Context {
        private static final Method CONTEXT_CALLBACK_APPLY_METHOD;

        static {
            try {
                CONTEXT_CALLBACK_APPLY_METHOD = ContextCallback.class.getDeclaredMethod("apply", V8Value[].class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        private final V8Runtime runtime;
        private final WeakHashMap<V8Value, Node> cache = new WeakHashMap<>();

        public static Context fromJS(V8Value metaV8) {
            Context context = new Context(metaV8.getV8Runtime());
            if (metaV8 instanceof V8ValueObject) {
                try (V8Value syntaxKinds = ((V8ValueObject) metaV8).get("syntaxKinds")) {
                    if (syntaxKinds instanceof V8ValueMap) {
                        ((V8ValueMap) syntaxKinds).forEach((V8Value keyV8, V8Value valueV8) -> {
                            if (keyV8 instanceof V8ValueString && valueV8 instanceof V8ValueInteger) {
                                int code = ((V8ValueInteger) valueV8).getValue();
                                String name = ((V8ValueString) keyV8).getValue();
                                context.syntaxKindsByCode.put(code, name);
                                context.syntaxKindsByName.put(name, code);
                            }
                        });
                    }
                } catch (JavetException e) {
                    throw new RuntimeException(e);
                }
            }
            return context;
        }

        private final Map<String, Integer> syntaxKindsByName = new HashMap<>();
        private final Map<Integer, String> syntaxKindsByCode = new HashMap<>();

        private Context(V8Runtime runtime) {

            this.runtime = runtime;
        }

        public Node tscNode(V8Value v8) {
            return this.cache.computeIfAbsent(v8, (arg) -> {
                if (!(arg instanceof V8ValueObject)) {
                    throw new IllegalArgumentException("can only wrap a V8 object as a Node");
                }
                System.out.println("*** creating new Node instance");
                return new Node(this, (V8ValueObject) arg);
            });
        }

        public V8ValueFunction asJSFunction(ContextCallback func) {
            JavetCallbackContext callbackContext = new JavetCallbackContext(func, CONTEXT_CALLBACK_APPLY_METHOD);
            try {
                return this.runtime.createV8ValueFunction(callbackContext);
            } catch (JavetException e) {
                throw new RuntimeException(e);
            }
        }

        public V8ValueFunction asJSFunction(Function<? super V8Value, ? extends V8Value> func) {
            ContextCallback callback = (V8Value[] args) -> func.apply(args[0]);
            return asJSFunction(callback);
        }

        public V8ValueFunction asJSFunction(Consumer<? super V8Value> func) {
            ContextCallback callback = (V8Value[] args) -> {
                func.accept(args[0]);
                return runtime.createV8ValueUndefined();
            };
            return asJSFunction(callback);
        }

        public int syntaxKindCode(String name) {
            return this.syntaxKindsByName.get(name);
        }

        public String syntaxKindName(int code) {
            return this.syntaxKindsByCode.get(code);
        }
    }

    class Node {
        private final Context context;
        private final V8ValueObject object;

        public Node(Context context, V8ValueObject object) {
            this.context = context;
            this.object = object;
        }

        public int syntaxKindCode() {
            try {
                return object.getInteger("kind");
            } catch (JavetException e) {
                throw new RuntimeException(e);
            }
        }

        public String syntaxKindName() {
            return context.syntaxKindName(this.syntaxKindCode());
        }

        public int getChildCount() {
            try {
                return this.object.invokeInteger("getChildCount");
            } catch (JavetException e) {
                throw new RuntimeException(e);
            }
        }

        public String getText() {
            try {
                return this.object.invokeString("getText");
            } catch (JavetException e) {
                throw new RuntimeException(e);
            }
        }

        public void forEachChild(Consumer<Node> callback) {
            Consumer<V8Value> v8Callback = v8Value -> callback.accept(context.tscNode(v8Value));
            try {
                this.object.invoke("forEachChild", context.asJSFunction(v8Callback));
            } catch (JavetException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
