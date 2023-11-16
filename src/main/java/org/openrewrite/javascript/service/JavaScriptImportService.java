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
package org.openrewrite.javascript.service;

import org.openrewrite.Cursor;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Tree;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.service.ImportService;
import org.openrewrite.java.tree.J;

public class JavaScriptImportService extends ImportService {
    @Override
    public <J2 extends J> JavaVisitor<ExecutionContext> shortenAllFullyQualifiedTypeReferences() {
        //noinspection DataFlowIssue
        return shortenFullyQualifiedTypeReferencesIn(null);
    }

    @Override
    public <J2 extends J> JavaVisitor<ExecutionContext> shortenFullyQualifiedTypeReferencesIn(J2 subtree) {
        return new JavaVisitor<ExecutionContext>() {
            @Override
            public J visit(@Nullable Tree tree, ExecutionContext ctx, Cursor parent) {
                return (J) tree;
            }

            @Override
            public J visit(@Nullable Tree tree, ExecutionContext ctx) {
                return (J) tree;
            }
        };
    }
}
