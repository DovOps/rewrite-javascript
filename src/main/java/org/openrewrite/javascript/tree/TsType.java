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
package org.openrewrite.javascript.tree;

import org.openrewrite.Incubating;
import org.openrewrite.java.tree.JavaType;

@Incubating(since = "0.0")
public class TsType {
    public static final JavaType.ShallowClass ANY = JavaType.ShallowClass.build("any");
    public static final JavaType.ShallowClass NUMBER = JavaType.ShallowClass.build("number");
    public static final JavaType.ShallowClass UNDEFINED = JavaType.ShallowClass.build("undefined");
    public static final JavaType.ShallowClass UNKNOWN = JavaType.ShallowClass.build("unknown");

    public static final JavaType.ShallowClass ANONYMOUS = JavaType.ShallowClass.build("type.analysis.Anonymous");
    public static final JavaType.ShallowClass MERGED_INTERFACE = JavaType.ShallowClass.build("type.analysis.MergedInterface");
    public static final JavaType.ShallowClass MISSING_SYMBOL = JavaType.ShallowClass.build("type.analysis.MissingSymbol");
    public static final JavaType.ShallowClass PRIMITIVE_UNION = JavaType.ShallowClass.build("type.analysis.PrimitiveUnion");
    public static final JavaType.ShallowClass UNION = JavaType.ShallowClass.build("type.analysis.Union");
}
