/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * Base class for functions that:
 * <ul>
 * <li>take exactly one numeric argument or empty sequence
 * (for other types casting to double is attempted)</li>
 * <li>returns integer value for integer argument</li>
 * <li>returns double value for double argument</li>
 * <li>returns empty sequence if argument is an empty sequence</li>
 * </ul>
 */
public abstract class OneArgumentNumericFunction implements Function {
    
    // javadoc inherited    
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        FunctionArgumentsValidationHelper.checkArgumentsCount(getName(), 1, arguments.length);
        Value result;
        if (arguments[0] instanceof IntValue) {
            result =  context.getFactory().createIntValue(
                    getValue(((IntValue) arguments[0]).asJavaInt()));
        } else if (arguments[0] instanceof DoubleValue) {
            double doubleValue = ((DoubleValue) arguments[0]).asJavaDouble();
            if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
                result = arguments[0];
            } else {
                result = context.getFactory().createDoubleValue(getValue(doubleValue));
            }
        } else if (arguments[0].getSequence().getLength() == 0) {
            result = Sequence.EMPTY;
        } else {
            double doubleValue = PipelineExpressionHelper.stringToDouble(arguments[0].stringValue());
            result = context.getFactory().createDoubleValue(getValue(doubleValue));
        }
        return result;
    }
    
    /**
     * Returns value for double argument
     * 
     * @param arg the argument
     * @return function value
     */
    protected abstract double getValue(double arg);
    
    /**
     * Returns value for integer argument
     * 
     * @param arg the argument
     * @return function value
     */
    protected abstract int getValue(int arg);
    
    /**
     * Returns function name
     */
    protected abstract String getName();

}
