package com.volantis.styling.impl.engine.matchers.composites;

/**
 * Typesafe enumeration of operators for composite selectors.
 */
public class Operator {
    private String operator;

    public static final Operator ADJACENT = new Operator("+");
    public static final Operator CHILD = new Operator(">");
    public static final Operator DESCENDANT = new Operator(" ");
    public static final Operator SIBLING = new Operator("~");

    private Operator(String operator) {
    }

    public String toString() {
        return operator;
    }
}
