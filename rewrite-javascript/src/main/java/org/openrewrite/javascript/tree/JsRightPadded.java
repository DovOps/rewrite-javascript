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

import lombok.Getter;

public class JsRightPadded {
    @Getter
    public enum Location {
        ALIAS_PROPERTY_NAME(JsSpace.Location.ALIAS_PROPERTY_NAME_PREFIX),
        ARRAY_LITERAL_ELEMENT_SUFFIX(JsSpace.Location.ARRAY_LITERAL_SUFFIX),
        BINDING_ELEMENT(JsSpace.Location.BINDING_ELEMENT_SUFFIX),
        BINDING_ELEMENT_PROPERTY_NAME(JsSpace.Location.BINDING_ELEMENT_PROPERTY_NAME_SUFFIX),
        EXPORT_ELEMENT_SUFFIX(JsSpace.Location.EXPORT_ELEMENT_SUFFIX),
        FUNCTION_TYPE_PARAMETER_SUFFIX(JsSpace.Location.FUNCTION_TYPE_SUFFIX),
        IMPORT_ELEMENT_SUFFIX(JsSpace.Location.IMPORT_ELEMENT_SUFFIX),
        IMPORT_NAME_SUFFIX(JsSpace.Location.IMPORT_NAME_SUFFIX),
        PROPERTY_ASSIGNMENT_NAME(JsSpace.Location.PROPERTY_ASSIGNMENT_NAME_SUFFIX),
        SCOPED_VARIABLE_DECLARATIONS_VARIABLE(JsSpace.Location.SCOPED_VARIABLE_DECLARATIONS_VARIABLE_SUFFIX),
        TAG(JsSpace.Location.TAG_SUFFIX),
        TUPLE_ELEMENT_SUFFIX(JsSpace.Location.TUPLE_ELEMENT_SUFFIX),
        UNION_TYPE(JsSpace.Location.UNION_TYPE_SUFFIX),
        INTERSECTION_TYPE(JsSpace.Location.INTERSECTION_TYPE_SUFFIX),
        JSNAMED_VARIABLE(JsSpace.Location.JSNAMED_VARIABLE_SUFFIX),
        NAMESPACE_DECLARATION_NAME(JsSpace.Location.NAMESPACE_DECLARATION_PREFIX),
        JSMETHOD_DECLARATION_PARAMETER(JsSpace.Location.JSMETHOD_DECLARATION_PARAMETERS),
        TYPE_LITERAL_MEMBERS(JsSpace.Location.TYPE_LITERAL_MEMBERS_SUFFIX),
        INDEXED_SIGNATURE_DECLARATION_PARAMETERS(JsSpace.Location.INDEXED_SIGNATURE_DECLARATION_PARAMETERS_SUFFIX),
        FOR_CONTROL_VAR(JsSpace.Location.FOR_INIT_SUFFIX),
        FOR_CONTROL_ITER(JsSpace.Location.FOR_ITER_SUFFIX),
        FOR_BODY(JsSpace.Location.FOR_BODY_SUFFIX),
        FUNCTION_TYPE_CONSTRUCTOR(JsSpace.Location.FUNCTION_TYPE_CONSTRUCTOR_SUFFIX),
        ARRAY_BINDING_PATTERN_ELEMENTS(JsSpace.Location.FOR_BODY_SUFFIX),
        EXPR_WITH_TYPE_ARG_PARAMETERS_SUFFIX(JsSpace.Location.EXPR_WITH_TYPE_ARG_PARAMETERS_SUFFIX),
        TEMPLATE_EXPRESSION_TAG(JsSpace.Location.TEMPLATE_EXPRESSION_TAG_SUFFIX),
        TEMPLATE_EXPRESSION_TYPE_ARG_PARAMETERS_SUFFIX(JsSpace.Location.TEMPLATE_EXPRESSION_TYPE_ARG_PARAMETERS_SUFFIX),
        TEMPLATE_EXPRESSION_TEMPLATE_SPAN(JsSpace.Location.TEMPLATE_EXPRESSION_TEMPLATE_SPAN_SUFFIX),
        CONDITIONAL_TYPE_CONDITION(JsSpace.Location.CONDITIONAL_TYPE_CONDITION_SUFFIX),
        IMPORT_TYPE_TYPEOF(JsSpace.Location.IMPORT_TYPE_TYPEOF_SUFFIX),
        IMPORT_TYPE_TYPE_ARGUMENTS(JsSpace.Location.IMPORT_TYPE_TYPE_ARGUMENTS_SUFFIX);

        private final JsSpace.Location afterLocation;

        Location(JsSpace.Location afterLocation) {
            this.afterLocation = afterLocation;
        }

    }
}
