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
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-normalize-space">fn:normalize-space</a>.
 */
public class NormalizeSpaceFunction implements Function {
    
    /**
     * The name against which this function should be registered.
     */
    public static final String NAME = "normalize-space";
    
    // javadoc inherited    
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        FunctionArgumentsValidationHelper.checkArgumentsCount(NAME, 1, arguments.length);
        Value result;
        if (arguments[0].getSequence().getLength() == 0) {
            result = context.getFactory().createStringValue("");
        } else {
            result = context.getFactory().createStringValue(
                    arguments[0].stringValue().asJavaString().trim());
        }
        return result;
    }

}
