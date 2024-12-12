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

public class JsSpace {
    public enum Location {
        ALIAS_PREFIX,
        ALIAS_PROPERTY_NAME_PREFIX,
        ARRAY_LITERAL_ELEMENTS,
        ARRAY_LITERAL_PREFIX,
        ARRAY_LITERAL_SUFFIX,
        ARROW_FUNCTION_PREFIX,
        AWAIT_PREFIX,
        BINARY_PREFIX,
        BINDING_ELEMENTS,
        BINDING_INITIALIZER_PREFIX,
        BINDING_ELEMENT_PREFIX,
        BINDING_ELEMENT_PROPERTY_NAME_SUFFIX,
        BINDING_ELEMENT_SUFFIX,
        DEFAULT_TYPE_PREFIX,
        DELETE_PREFIX,
        EXPORT_ELEMENTS,
        EXPORT_ELEMENT_SUFFIX,
        EXPORT_FROM_PREFIX,
        EXPORT_INITIALIZER_PREFIX,
        EXPORT_PREFIX,
        FUNCTION_TYPE_ARROW_PREFIX,
        FUNCTION_TYPE_PARAMETERS,
        FUNCTION_TYPE_PREFIX,
        FUNCTION_TYPE_SUFFIX,
        IMPORT_ELEMENTS,
        IMPORT_ELEMENT_SUFFIX,
        IMPORT_FROM_PREFIX,
        IMPORT_INITIALIZER_PREFIX,
        IMPORT_NAME_SUFFIX,
        IMPORT_PREFIX,
        JS_IMPORT_IMPORT_TYPE_PREFIX,
        OBJECT_BINDING_DECLARATIONS_PREFIX,
        OBJECT_BINDING_PREFIX,
        PROPERTY_ASSIGNMENT_NAME_SUFFIX,
        PROPERTY_ASSIGNMENT_PREFIX,
        SCOPED_VARIABLE_DECLARATIONS_PREFIX,
        SCOPED_VARIABLE_DECLARATIONS_SCOPE_PREFIX,
        SCOPED_VARIABLE_DECLARATIONS_VARIABLE_SUFFIX,
        TAG_SUFFIX,
        TEMPLATE_EXPRESSION_PREFIX,
        TEMPLATE_EXPRESSION_SPAN_PREFIX,
        TEMPLATE_EXPRESSION_SPAN_SUFFIX,
        TUPLE_ELEMENT,
        TUPLE_ELEMENT_SUFFIX,
        TUPLE_PREFIX,
        TYPEOF_PREFIX,
        TYPE_DECLARATION_NAME_PREFIX,
        TYPE_DECLARATION_INITIALIZER_PREFIX,
        TYPE_DECLARATION_PREFIX,
        TYPE_OPERATOR_PREFIX,
        UNION_PREFIX,
        UNION_TYPE_SUFFIX,
        INTERSECTION_PREFIX,
        INTERSECTION_TYPE_SUFFIX,
        UNKNOWN_PREFIX,
        UNKNOWN_SOURCE_PREFIX,
        VARIABLE_DECLARATION_PREFIX,
        VOID_PREFIX,
        YIELD_PREFIX,
        TYPE_INFO_PREFIX,
        TYPE_REFERENCE_PREFIX,
        JSVARIABLE_DECLARATIONS_PREFIX,
        JSVARIABLE_PREFIX,
        JSVARIABLE_INITIALIZER,
        JSNAMED_VARIABLE_SUFFIX,
        JSMETHOD_DECLARATION_PARAMETERS,
        JSMETHOD_DECLARATION_PREFIX,
        NAMESPACE_DECLARATION_PREFIX,
        NAMESPACE_DECLARATION_KEYWORD_PREFIX,
        FUNCTION_DECLARATION_PREFIX,
        JS_IMPORT_SPECIFIER_PREFIX,
        JS_IMPORT_SPECIFIER_IMPORT_TYPE_PREFIX,
        TYPE_LITERAL_PREFIX,
        TYPE_LITERAL_MEMBERS_PREFIX,
        TYPE_LITERAL_MEMBERS_SUFFIX,
        INDEXED_SIGNATURE_DECLARATION_PREFIX,
        INDEXED_SIGNATURE_DECLARATION_PARAMETERS_PREFIX,
        INDEXED_SIGNATURE_DECLARATION_PARAMETERS_SUFFIX,
        INDEXED_SIGNATURE_DECLARATION_TYPE_EXPRESSION_PREFIX,
        FOR_OF_LOOP_PREFIX,
        FOR_LOOP_CONTROL_PREFIX,
        FOR_OF_AWAIT_PREFIX,
        FOR_BODY_SUFFIX,
        FOR_INIT_SUFFIX,
        FOR_ITER_SUFFIX,
        FOR_IN_LOOP_PREFIX,
        TYPE_QUERY_PREFIX,
        FUNCTION_TYPE_CONSTRUCTOR_SUFFIX,
        ARRAY_BINDING_PATTERN_PREFIX,
        ARRAY_BINDING_PATTERN_ELEMENTS_PREFIX,
        ARRAY_BINDING_PATTERN_ELEMENTS_SUFFIX,
        EXPR_WITH_TYPE_ARG_PREFIX,
        EXPR_WITH_TYPE_ARG_PARAMETERS,
        EXPR_WITH_TYPE_ARG_PARAMETERS_SUFFIX,
        TEMPLATE_EXPRESSION_TAG_SUFFIX,
        TEMPLATE_EXPRESSION_TYPE_ARG_PARAMETERS,
        TEMPLATE_EXPRESSION_TYPE_ARG_PARAMETERS_SUFFIX,
        TEMPLATE_EXPRESSION_TEMPLATE_SPAN_SUFFIX,
        TAGGED_TEMPLATE_EXPRESSION_PREFIX,
        CONDITIONAL_TYPE_PREFIX,
        CONDITIONAL_TYPE_CONDITION,
        CONDITIONAL_TYPE_CONDITION_SUFFIX,
        INFER_TYPE_PREFIX,
        INFER_TYPE_PARAMETER_PREFIX,
        TYPE_PREDICATE_PREFIX,
        TYPE_PREDICATE_ASSERTS_PREFIX,
        TYPE_PREDICATE_EXPRESSION_PREFIX,
        LITERAL_TYPE_PREFIX,
        SATISFIES_EXPRESSION_PREFIX,
        SATISFIES_EXPRESSION_TYPE_PREFIX,
        IMPORT_TYPE_PREFIX,
        IMPORT_TYPE_TYPEOF_SUFFIX,
        IMPORT_TYPE_QUALIFIER_PREFIX,
        IMPORT_TYPE_TYPE_ARGUMENTS,
        IMPORT_TYPE_TYPE_ARGUMENTS_SUFFIX,
        EXPORT_DECLARATION_PREFIX,
        EXPORT_DECLARATION_TYPE_ONLY_PREFIX,
        EXPORT_DECLARATION_MODULE_SPECIFIER_PREFIX,
        EXPORT_SPECIFIER_PREFIX,
        EXPORT_SPECIFIER_TYPE_ONLY_PREFIX,
        NAMED_EXPORTS_PREFIX,
        NAMED_EXPORTS_ELEMENTS_PREFIX,
        NAMED_EXPORTS_ELEMENTS_SUFFIX,
        EXPORT_ASSIGNMENT_PREFIX,
        EXPORT_ASSIGNMENT_EXPORT_EQUALS_PREFIX,
        FUNCTION_DECLARATION_NAME_PREFIX,
        FUNCTION_DECLARATION_ASTERISK_TOKEN_PREFIX,
        INDEXED_ACCESS_TYPE_PREFIX,
        INDEXED_ACCESS_TYPE_INDEX_TYPE_SUFFIX,
        INDEXED_ACCESS_TYPE_INDEX_TYPE_PREFIX,
        INDEXED_ACCESS_TYPE_INDEX_TYPE_ELEMENT_SUFFIX,
        ASSIGNMENT_OPERATION_PREFIX,
        ASSIGNMENT_OPERATION_OPERATOR_PREFIX,
        ASSIGNMENT_OPERATION_OPERATOR,
        MAPPED_TYPE_PREFIX,
        MAPPED_TYPE_PREFIX_TOKEN_PREFIX,
        MAPPED_TYPE_VALUE_TYPE,
        MAPPED_TYPE_VALUE_TYPE_SUFFIX,
        MAPPED_TYPE_SUFFIX_TOKEN_PREFIX,
        MAPPED_TYPE_KEYS_REMAPPING_PREFIX,
        MAPPED_TYPE_KEYS_REMAPPING_TYPE_PARAMETER_SUFFIX,
        MAPPED_TYPE_MAPPED_TYPE_PARAMETER_PREFIX,
        MAPPED_TYPE_MAPPED_TYPE_PARAMETER_ITERATE_PREFIX,
        MAPPED_TYPE_KEYS_REMAPPING_NAME_TYPE_SUFFIX,
        MAPPED_TYPE_READONLY_PREFIX,
        MAPPED_TYPE_QUESTION_TOKEN_PREFIX,
        TYPE_TREE_EXPRESSION_PREFIX,
        LAMBDA_ARROW_PREFIX,
        JS_YIELD_DELEGATED_PREFIX;
    }
}
