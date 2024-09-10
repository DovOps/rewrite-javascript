import {Markers, Tree, TreeVisitor, UUID} from "../core";

export abstract class J implements Tree {
    accept<R extends Tree, P>(v: TreeVisitor<R, P>, p: P): R | null {
        return null;
    }

    isAcceptable<P>(v: TreeVisitor<Tree, P>, p: P): boolean {
        return false;
    }

    abstract get id(): UUID;

    abstract get markers(): Markers;

    abstract withId(id: UUID): Tree;

    abstract withMarkers(markers: Markers): Tree;
}

export interface JsonKey extends Tree {
}

export interface JsonValue extends Tree {
}

export class Space {
}

export class Comment {
}

export class JRightPadded<T> {
    constructor(element: T) {
        this._element = element;
    }

    private readonly _element: T;

    get element(): T {
        return this._element;
    }

    static getElements<T>(padded: JRightPadded<T>[]) {
        return [];
    }

    static withElements<T>(padded: JRightPadded<T>[], elements: T[]) {
        return [];
    }

    static withElement<T>(padded: JRightPadded<T> | null, element: T | null): JRightPadded<T> {
        return padded;
    }
}