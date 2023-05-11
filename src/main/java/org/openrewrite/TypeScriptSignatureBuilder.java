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
package org.openrewrite;

import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaTypeSignatureBuilder;
import org.openrewrite.javascript.internal.tsc.TSCNode;
import org.openrewrite.javascript.internal.tsc.TSCSymbol;
import org.openrewrite.javascript.internal.tsc.TSCType;
import org.openrewrite.javascript.internal.tsc.generated.TSCSyntaxKind;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@Incubating(since = "0.0")
public class TypeScriptSignatureBuilder implements JavaTypeSignatureBuilder {

    @Nullable
    Set<String> typeVariableNameStack;

    @Override
    public String signature(@Nullable Object object) {
        if (object == null) {
            return "{undefined}";
        }

        assert object instanceof TSCNode;
        TSCNode node = (TSCNode) object;
        switch (node.syntaxKind()) {
            case ClassDeclaration:
            case EnumDeclaration:
            case InterfaceDeclaration:
                return node.hasProperty("typeParameters") && !node.getNodeListProperty("typeParameters").isEmpty() ? parameterizedSignature(node) : classSignature(node);
            case ConstructSignature:
            case MethodDeclaration:
            case MethodSignature:
                return methodSignature(node);
            case TypeParameter:
                return genericSignature(node);
            default:
                return mapNode(node);
        }
    }

    @Override
    public String arraySignature(Object object) {
        return null;
    }

    @Override
    public String classSignature(Object object) {
        assert object instanceof TSCNode;
        TSCNode node = (TSCNode) object;
        assert (node.syntaxKind() == TSCSyntaxKind.ClassDeclaration ||
                node.syntaxKind() == TSCSyntaxKind.InterfaceDeclaration ||
                node.syntaxKind() == TSCSyntaxKind.EnumDeclaration);

        StringBuilder signature = new StringBuilder();
        mapFqn(node, signature);
        return signature.toString();
    }

    @Override
    public String genericSignature(Object object) {
        assert object instanceof TSCNode;
        TSCNode node = (TSCNode) object;

        if (typeVariableNameStack == null) {
            typeVariableNameStack = new HashSet<>();
        }

        String name = node.getNodeProperty("name").getText();
        if (!typeVariableNameStack.add(name)) {
            return "Generic{" + name + "}";
        }

        StringBuilder s = new StringBuilder("Generic{").append(name);
        StringJoiner boundSigs = new StringJoiner(" & ");

        if (node.hasProperty("constraint")) {
            TSCNode constraint = node.getNodeProperty("constraint");
            if (constraint.syntaxKind() == TSCSyntaxKind.IntersectionType) {
                for (TSCNode type : constraint.getNodeListProperty("types")) {
                    boundSigs.add(signature(type));
                }
            } else {
                boundSigs.add(signature(constraint));
            }
        }

        String boundSigStr = boundSigs.toString();
        if (!boundSigStr.isEmpty()) {
            s.append(" extends ").append(boundSigStr);
        }

        typeVariableNameStack.remove(name);
        return s.append("}").toString();
    }

    public String methodSignature(Object object) {
        assert object instanceof TSCNode;
        TSCNode node = (TSCNode) object;
        assert (node.syntaxKind() == TSCSyntaxKind.ConstructSignature ||
                node.syntaxKind() == TSCSyntaxKind.MethodDeclaration ||
                node.syntaxKind() == TSCSyntaxKind.MethodSignature);

        StringBuilder signature = new StringBuilder();
        boolean isConstructor = node.syntaxKind() == TSCSyntaxKind.ConstructSignature;

        String name = isConstructor ? "TODO:" : classSignature(node.getNodeProperty("parent"));
        signature.append(name);
        if (isConstructor) {
            throw new IllegalStateException("implement me.");
        } else {
            String returnSignature;
            if (node.hasProperty("type")) {
                returnSignature = signature(node.getNodeProperty("type"));
            } else {
                returnSignature = "void";
            }
            signature.append("{name=")
                    .append(node.getNodeProperty("name").getText())
                    .append(",return=")
                    .append(returnSignature);
        }
        return signature.append(",parameters=")
                .append(methodArgumentSignature(node))
                .append("}").toString();
    }

    @Override
    public String parameterizedSignature(Object object) {
        assert object instanceof TSCNode;
        TSCNode node = (TSCNode) object;
        StringBuilder s = new StringBuilder(classSignature(node));
        StringJoiner joiner = new StringJoiner(", ", "<", ">");
        for (TSCNode param : node.getNodeListProperty("typeParameters")) {
            joiner.add(signature(param));
        }

        s.append(joiner);
        return s.toString();
    }

    @Override
    public String primitiveSignature(Object object) {
        return null;
    }

    private String methodArgumentSignature(TSCNode node) {
        StringJoiner genericArgumentTypes = new StringJoiner(",", "[", "]");
        List<TSCNode> parameters = node.getOptionalNodeListProperty("parameters");
        if (parameters != null && !parameters.isEmpty()) {
            parameters.forEach(p -> genericArgumentTypes.add(signature(p)));
        }
        return genericArgumentTypes.toString();
    }

    // TEMP ... using `getSourceFile` does not work for all cases.
    public static String mapFqn(TSCNode node) {
        String dirty = node.getSourceFile().getPath().replace("/", ".");
        String clean = dirty.startsWith(".") ? dirty.substring(1) : dirty;
        return clean + "." + (node.hasProperty("name") ? node.getNodeProperty("name").getText() : "");
    }

    // FIME: resolve parents to SF, create cleaned FQN.
    public static void mapFqn(TSCNode node, StringBuilder sb) {
        TSCNode parent = node.getParent();
        if (parent == null) {
            return;
        }

        if (parent.syntaxKind() == TSCSyntaxKind.SourceFile) {
            String dirty = node.getSourceFile().getPath().replace("/", ".");
            String clean = dirty.startsWith(".") ? dirty.substring(1) : dirty;
            sb.insert(0, clean + ".");
        } else if (parent.syntaxKind() == TSCSyntaxKind.ClassDeclaration) {
            throw new IllegalStateException("implement me. $...");
        } else {
            // methodName()
            throw new IllegalStateException("implement me ...");
        }

        sb.append(node.getNodeProperty("name").getText());
    }

    public String mapNode(TSCNode node) {
        switch (node.syntaxKind()) {
            case Parameter:
                TSCNode typeNode = node.getNodeProperty("type");
                TSCNode declaration = getDeclaration(typeNode.getTypeChecker().getTypeFromTypeNode(typeNode).getSymbolForType().getDeclarations());
                if (declaration != null) {
                    return signature(declaration);
                }
                break;
            case Identifier:
            case TypeReference: {
                TSCType type = node.getTypeChecker().getTypeFromTypeNode(node);
                TSCSymbol symbol = type.getTypeChecker().getTypeFromTypeNode(node).getOptionalSymbolProperty("symbol");
                if (symbol != null && symbol.getDeclarations() != null && symbol.getDeclarations().size() == 1) {
                    return signature(symbol.getDeclarations().get(0));
                }
                break;
            }
        }
        return "{undefined}";
    }

    @Nullable
    private TSCNode getDeclaration(@Nullable List<TSCNode> declarations) {
        if (declarations == null || declarations.isEmpty()) {
            return null;
        }

        if (declarations.size() == 1) {
            return declarations.get(0);
        } else {
            // FIXME: Add support for merged declarations.
            return null;
        }
    }
}
