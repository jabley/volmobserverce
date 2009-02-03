/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

/**
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-ceiling">fn:ceiling</a>.
 */
public class CeilingFunction extends OneArgumentNumericFunction {
    
    /**
     * The name against which this function should be registered.
     */
    public static final String NAME = "ceiling";

    // javadoc inherited
    protected double getValue(double arg) {
        return Math.ceil(arg);
    }

    // javadoc inherited
    protected int getValue(int arg) {
        return arg;
    }
    
    // javadoc inherited
    protected String getName() {
        return NAME;
    }

}
