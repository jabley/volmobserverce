/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.CompareResult;

/**
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-max">fn:max</a>.
 */
public class MaxFunction extends FindExtremeValueFunction {
    /**
     * The name against which this function should be registered.
     */
    public static final String NAME = "max";
    
    // javadoc inherited
    protected CompareResult getExtremeValueComparisonResult() {
        return CompareResult.GREATER_THAN;
    }
    
    // javadoc inherited    
    protected String getName() {
        return NAME;
    }    

}
