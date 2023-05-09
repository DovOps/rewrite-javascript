package org.openrewrite.javascript.internal.tsc;

import com.caoccao.javet.values.reference.V8ValueObject;
import org.openrewrite.javascript.internal.tsc.generated.TSCIndexKind;
import org.openrewrite.javascript.internal.tsc.generated.TSCSignatureKind;
import org.openrewrite.javascript.internal.tsc.generated.TSCSymbolFlag;
import org.openrewrite.javascript.internal.tsc.generated.TSCTypeFlag;

import javax.annotation.Nullable;
import java.util.List;

import static org.openrewrite.javascript.internal.tsc.TSCConversions.*;

public class TSCTypeChecker implements TSCV8Backed {

    // TODO: unmapped functions include all marked @internal and those that return "uncheckable" nodes

    public static TSCTypeChecker wrap(TSCProgramContext context, V8ValueObject objectV8) {
        return new TSCTypeChecker(context, objectV8);
    }

    private final TSCProgramContext programContext;
    private final V8ValueObject objectV8;

    private TSCTypeChecker(TSCProgramContext programContext, V8ValueObject objectV8) {
        this.programContext = programContext;
        this.objectV8 = objectV8;
    }

    public TSCType getTypeOfSymbolAtLocation(TSCSymbol symbol, TSCNode node) {
        return this.invokeMethodNonNull(
                "getTypeOfSymbolAtLocation",
                TYPE,
                symbol,
                node
        );
    }

    public TSCType getTypeOfSymbol(TSCSymbol symbol) {
        return this.invokeMethodNonNull(
                "getTypeOfSymbol",
                TYPE,
                symbol
        );
    }

    public TSCType getDeclaredTypeOfSymbol(TSCSymbol symbol) {
        return this.invokeMethodNonNull(
                "getDeclaredTypeOfSymbol",
                TYPE,
                symbol
        );
    }

    public List<TSCSymbol> getPropertiesOfType(TSCType type) {
        return this.invokeMethodNonNull(
                "getPropertiesOfType",
                SYMBOL_LIST,
                type
        );
    }

    public @Nullable TSCSymbol getPropertyOfType(TSCType type, String name) {
        return this.invokeMethodNullable(
                "getPropertyOfType",
                SYMBOL,
                type,
                name
        );
    }

    public @Nullable TSCSymbol getPrivateIdentifierPropertyOfType(TSCType leftType, String name, TSCNode location) {
        return this.invokeMethodNullable(
                "getPrivateIdentifierPropertyOfType",
                SYMBOL,
                leftType,
                name,
                location
        );
    }

    public @Nullable TSCIndexInfo getIndexInfoOfType(TSCType type, TSCIndexKind kind) {
        return this.invokeMethodNullable(
                "getIndexInfoOfType",
                INDEX_INFO,
                type,
                kind.code
        );
    }

    public List<TSCIndexInfo> getIndexInfosOfType(TSCType type) {
        return this.invokeMethodNonNull(
                "getIndexInfosOfType",
                INDEX_INFO_LIST,
                type
        );
    }

    public List<TSCIndexInfo> getIndexInfosOfIndexSymbol(TSCSymbol indexSymbol) {
        return this.invokeMethodNonNull(
                "getIndexInfosOfIndexSymbol",
                INDEX_INFO_LIST,
                indexSymbol
        );
    }

    public List<TSCSignature> getSignaturesOfType(TSCType type, TSCSignatureKind signatureKind) {
        return this.invokeMethodNonNull(
                "getSignaturesOfType",
                SIGNATURE_LIST,
                type,
                signatureKind.code
        );
    }

    public @Nullable TSCType getIndexTypeOfType(TSCType type, TSCIndexKind indexKind) {
        return this.invokeMethodNullable(
                "getIndexTypeOfType",
                TYPE,
                type,
                indexKind.code
        );
    }

    public @Nullable List<TSCType> getBaseTypes(TSCType interfaceType) {
        return this.invokeMethodNonNull(
                "getBaseTypes",
                TYPE_LIST,
                interfaceType
        );
    }

    public TSCType getBaseTypeOfLiteralType(TSCType type) {
        return this.invokeMethodNonNull(
                "getBaseTypeOfLiteralType",
                TYPE,
                type
        );
    }

    public TSCType getWidenedType(TSCType type) {
        return this.invokeMethodNonNull(
                "getWidenedType",
                TYPE,
                type
        );
    }

    public TSCType getReturnTypeOfSignature(TSCSignature signature) {
        return this.invokeMethodNonNull(
                "getReturnTypeOfSignature",
                TYPE,
                signature
        );
    }

    public TSCType getNullableType(TSCType type, TSCTypeFlag... typeFlags) {
        return this.invokeMethodNonNull(
                "getNullableType",
                TYPE,
                type,
                TSCTypeFlag.union(typeFlags)
        );
    }

    public TSCType getNonNullableType(TSCType type) {
        return this.invokeMethodNonNull(
                "getNonNullableType",
                TYPE,
                type
        );
    }

    public List<TSCType> getTypeArguments(TSCType type) {
        return this.invokeMethodNonNull(
                "getTypeArguments",
                TYPE_LIST,
                type
        );
    }

    public List<TSCSymbol> getSymbolsInScope(TSCNode location, TSCSymbolFlag... meaning) {
        return this.invokeMethodNonNull(
                "getSymbolsInScope",
                SYMBOL_LIST,
                location,
                TSCSymbolFlag.union(meaning)
        );
    }

    public @Nullable TSCSymbol getSymbolAtLocation(TSCNode node) {
        return this.invokeMethodNullable(
                "getSymbolAtLocation",
                SYMBOL,
                node
        );
    }

    public List<TSCSymbol> getSymbolsOfParameterPropertyDeclaration(TSCNode parameterDeclaration, String parameterName) {
        return this.invokeMethodNonNull(
                "getSymbolsOfParameterPropertyDeclaration",
                SYMBOL_LIST,
                parameterDeclaration,
                parameterName
        );
    }

    //
    //  TODO everything from `getShorthandAssignmentValueSymbol` on is unmapped, except the functions below
    //

    public TSCType getTypeAtLocation(TSCNode node) {
        return this.invokeMethodNonNull(
                "getTypeAtLocation",
                TYPE,
                node
        );
    }

    public String signatureToString(TSCSignature signature) {
        // TODO leaves out optional parameters
        return this.invokeMethodNonNull(
                "signatureToString",
                STRING,
                signature
        );
    }

    public String typeToString(TSCType type) {
        // TODO leaves out optional parameters
        return this.invokeMethodNonNull(
                "typeToString",
                STRING,
                type
        );
    }

    public String symbolToString(TSCSymbol symbol) {
        // TODO leaves out optional parameters
        return this.invokeMethodNonNull(
                "symbolToString",
                STRING,
                symbol
        );
    }

    public String getFullyQualifiedName(TSCSymbol symbol) {
        return this.invokeMethodNonNull(
                "getFullyQualifiedName",
                STRING,
                symbol
        );
    }

    public TSCType getAnyType() {
        return this.invokeMethodNonNull("getAnyType", TYPE);
    }

    public TSCType getStringType() {
        return this.invokeMethodNonNull("getStringType", TYPE);
    }

    public TSCType getStringLiteralType(String value) {
        return this.invokeMethodNonNull("getStringLiteralType", TYPE, value);
    }

    public TSCType getNumberType() {
        return this.invokeMethodNonNull("getNumberType", TYPE);
    }

    public TSCType getNumberLiteralType(Number value) {
        return this.invokeMethodNonNull("getNumberLiteralType", TYPE, value);
    }

    public TSCType getBigIntType() {
        return this.invokeMethodNonNull("getBigIntType", TYPE);
    }

    public TSCType getBooleanType() {
        return this.invokeMethodNonNull("getBooleanType", TYPE);
    }

    public TSCType getFalseType() {
        return this.invokeMethodNonNull("getFalseType", TYPE);
    }

    public TSCType getTrueType() {
        return this.invokeMethodNonNull("getTrueType", TYPE);
    }

    public TSCType getVoidType() {
        return this.invokeMethodNonNull("getVoidType", TYPE);
    }

    public TSCType getUndefinedType() {
        return this.invokeMethodNonNull("getUndefinedType", TYPE);
    }

    public TSCType getNullType() {
        return this.invokeMethodNonNull("getNullType", TYPE);
    }

    public TSCType getESSymbolType() {
        return this.invokeMethodNonNull("getESSymbolType", TYPE);
    }

    public TSCType getNeverType() {
        return this.invokeMethodNonNull("getNeverType", TYPE);
    }

    @Override
    public TSCProgramContext getProgramContext() {
        return programContext;
    }

    @Override
    public V8ValueObject getBackingV8Object() {
        return objectV8;
    }

    @Override
    public String debugDescription() {
        return "(type checker)";
    }
}
