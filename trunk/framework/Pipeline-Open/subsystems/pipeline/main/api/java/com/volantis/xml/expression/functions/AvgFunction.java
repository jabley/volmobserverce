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
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-avg">fn:avg</a>.
 */
public class AvgFunction implements Function {
    
    /**
     * The name against which this function should be registered.
     */
    public static final String NAME = "avg";
    
    /**
     * Maximum number of digits in fraction part
     */
    private static final int MAX_PRECISION = 20;

    // javadoc inherited
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        FunctionArgumentsValidationHelper.checkArgumentsCount(NAME, 1, arguments.length);
        Sequence sequence = arguments[0].getSequence();
        Value result;
        if (sequence.getLength() == 0) {
            result = Sequence.EMPTY; 
        } else {
            NumericValuesSummator summator = new NumericValuesSummator();
            summator.addAllValues(sequence);
            double sum = summator.getSumAsDouble();
            return context.getFactory().createDoubleValue(
                    Double.isNaN(sum) || Double.isInfinite(sum) ? sum :
                        new BigDecimal(Double.toString(sum)).divide(BigDecimal.valueOf(sequence.getLength()), 
                                MAX_PRECISION, BigDecimal.ROUND_HALF_UP).doubleValue()
            );
        }
        return result;
    }

}
