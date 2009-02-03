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
import com.volantis.xml.expression.sequence.Sequence;

/**
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-string-length">fn:string-length</a>.
 */
public class StringLengthFunction implements Function {
    
    /**
     * The name against which this function should be registered.
     */
    public static final String NAME = "string-length";
    
    // javadoc inherited    
    public Value invoke(ExpressionContext exprContext, Value[] values)
            throws ExpressionException {
        FunctionArgumentsValidationHelper.checkArgumentsCount(NAME, 1, values.length);
        Sequence sequence = values[0].getSequence();
        int result = sequence.getLength() > 0 ?
                values[0].stringValue().asJavaString().length() : 0;
        return exprContext.getFactory().createIntValue(result);
    }
}
