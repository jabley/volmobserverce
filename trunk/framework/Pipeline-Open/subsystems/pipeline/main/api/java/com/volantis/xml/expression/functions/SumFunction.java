/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.sequence.Sequence;

import java.math.BigDecimal;

/**
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-sum">fn:sum</a>.
 */
public class SumFunction implements Function {
    
    /**
     * The name against which this function should be registered.
     */
    public static final String NAME = "sum";
    
    // javadoc inherited
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        FunctionArgumentsValidationHelper.checkArgumentsCount(NAME, 1, 2, arguments.length);
        Sequence sequence = arguments[0].getSequence();
        Value result;
        if (sequence.getLength() == 0) {
            result = arguments.length == 2 ? 
                    arguments[1] : context.getFactory().createIntValue(0); 
        } else {
            NumericValuesSummator summator = new NumericValuesSummator();
            summator.addAllValues(sequence);
            return summator.getSum(context);
        }
        return result;
    }
    
    

}
