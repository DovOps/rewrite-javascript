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
package org.openrewrite.javascript;


import org.intellij.lang.annotations.Language;
import org.openrewrite.ExecutionContext;
import org.openrewrite.ParseExceptionResult;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Space;
import org.openrewrite.javascript.tree.JS;
import org.openrewrite.test.SourceSpec;
import org.openrewrite.test.SourceSpecs;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public final class Assertions {

    private Assertions() {
    }

    public static SourceSpecs javaScript(@Language("typescript") @Nullable String before) {
        return javaScript(before, s -> {
        });
    }

    public static SourceSpecs javaScript(@Language("typescript") @Nullable String before, Consumer<SourceSpec<JS.CompilationUnit>> spec) {
        return javaScript0(before, null, spec);
    }

    public static SourceSpecs javaScript(@Language("typescript") @Nullable String before, @Language("typescript") String after) {
        return javaScript(before, after, s -> {
        });
    }

    public static SourceSpecs javaScript(@Language("typescript") @Nullable String before, @Language("typescript") String after,
                                         Consumer<SourceSpec<JS.CompilationUnit>> spec) {
        return javaScript0(before, after, spec);
    }

    private static SourceSpec<JS.CompilationUnit> javaScript0(@Language("typescript") @Nullable String before, @Language("typescript") @Nullable String after, Consumer<SourceSpec<JS.CompilationUnit>> spec) {
        SourceSpec<JS.CompilationUnit> js = new SourceSpec<>(
                JS.CompilationUnit.class,
                null,
                JavaScriptParser.builder(),
                before,
                SourceSpec.ValidateSource.noop,
                Assertions::customizeExecutionContext
        );
        if (after != null) {
            js = js.after(s -> after);
        }
        acceptSpec(spec, js);
        return js;
    }

    static void customizeExecutionContext(ExecutionContext ctx) {
    }

    private static void acceptSpec(Consumer<SourceSpec<JS.CompilationUnit>> spec, SourceSpec<JS.CompilationUnit> javaScript) {
        Consumer<JS.CompilationUnit> userSuppliedAfterRecipe = javaScript.getAfterRecipe();
        javaScript.afterRecipe(userSuppliedAfterRecipe::accept);
        isFullyParsed().andThen(spec).accept(javaScript);
    }

    public static Consumer<SourceSpec<JS.CompilationUnit>> isFullyParsed() {
        return spec -> spec.afterRecipe(cu -> {
            new JavaScriptIsoVisitor<Integer>() {
                @Override
                public Space visitSpace(Space space, Space.Location loc, Integer integer) {
                    assertThat(space.getWhitespace().trim()).isEmpty();
                    return super.visitSpace(space, loc, integer);
                }
            }.visit(cu, 0);

            new JavaScriptVisitor<Integer>() {
                @Override
                public @Nullable J preVisit(J tree, Integer integer) {
                    if (tree instanceof J.Unknown) {
                        ((J.Unknown) tree).getSource().getMarkers().findFirst(ParseExceptionResult.class)
                                .ifPresent(result -> assertThat(result.getMessage()).isEqualTo(""));
                    }
                    return super.preVisit(tree, integer);
                }
            }.visit(cu, 0);
        });
    }
}
