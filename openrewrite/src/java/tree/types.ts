import {LstType} from "../../core";

export interface JavaType {
}

export namespace JavaType {
    export abstract class FullyQualified implements JavaType {
    }

    @LstType("org.openrewrite.java.tree.JavaType$Class")
    export class Class extends FullyQualified {
    }

    @LstType("org.openrewrite.java.tree.JavaType$MultiCatch")
    export class Union implements JavaType {
        private _types: JavaType[] | null = null;

        get types(): JavaType[] {
            return this._types!;
        }

        unsafeSet(types: JavaType[]): this {
            this._types = types;
            return this;
        }
    }

    @LstType("org.openrewrite.java.tree.JavaType$ShallowClass")
    export class ShallowClass extends FullyQualified {
    }

    @LstType("org.openrewrite.java.tree.JavaType$Parameterized")
    export class Parameterized extends FullyQualified {
    }

    @LstType("org.openrewrite.java.tree.JavaType$Primitive")
    export class Primitive implements JavaType {
        public constructor(private readonly _kind: PrimitiveKind) {
        }

        static of(kind: PrimitiveKind): Primitive {
            return new Primitive(kind);
        }

        get kind(): PrimitiveKind {
            return this._kind;
        }
    }

    export const enum PrimitiveKind {
        Boolean,
        Byte,
        Char,
        Double,
        Float,
        Int,
        Long,
        Short,
        Void,
        String,
        None,
        Null,
    }

    @LstType("org.openrewrite.java.tree.JavaType$Variable")
    export class Variable implements JavaType {
        type: JavaType = null!;

        withType(type: JavaType): Variable {
            return null!;
        }
    }

    @LstType("org.openrewrite.java.tree.JavaType$Method")
    export class Method implements JavaType {
        returnType: JavaType = null!;
    }

    @LstType("org.openrewrite.java.tree.JavaType$Unknown")
    export class Unknown implements JavaType {
        static INSTANCE = new Unknown();
    }
}
