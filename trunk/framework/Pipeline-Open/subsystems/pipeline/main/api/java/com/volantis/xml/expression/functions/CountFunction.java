/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;

/**
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-count">fn:count</a>.
 */
public class CountFunction implements Function {
    
    /**
     * The name against which this function should be registered.
     */
    public static final String NAME = "count";
    
    // javadoc inherited    
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        FunctionArgumentsValidationHelper.checkArgumentsCount(NAME, 1, arguments.length);
        return context.getFactory().createIntValue(arguments[0].getSequence().getLength());
    }

}
