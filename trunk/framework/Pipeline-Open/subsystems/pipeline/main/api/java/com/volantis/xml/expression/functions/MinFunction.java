/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.CompareResult;

/**
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-min">fn:min</a>.
 */
public class MinFunction extends FindExtremeValueFunction {
    
    /**
     * The name against which this function should be registered.
     */
    public static final String NAME = "min";

    // javadoc inherited
    protected CompareResult getExtremeValueComparisonResult() {
        return CompareResult.LESS_THAN;
    }

    // javadoc inherited    
    protected String getName() {
        return NAME;
    }
    
}
