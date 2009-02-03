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
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-substring">fn:substring</a>.
 */
public class SubstringFunction implements Function {
    /**
     * The name against which this function should be registered.
     */
    public static final String NAME = "substring";
    
    // javadoc inherited    
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        FunctionArgumentsValidationHelper.checkArgumentsCount(NAME, 2, 3, arguments.length);
        Value result;
        Sequence sequence = arguments[0].getSequence();
        if (sequence.getLength() == 0) {
            result = context.getFactory().createStringValue("");
        } else {
            result = context.getFactory().createStringValue(
                    getSubstring(arguments[0], arguments[1], 
                            arguments.length == 2 ? 
                                    DoubleValue.POSITIVE_INFINITY : arguments[2]));
        }
        return result;
    }
    
    private String getSubstring(Value value, Value startingLoc, Value length) throws ExpressionException {
        String substring;
        if (isNaN(startingLoc) || isNaN(length) || 
                isPositiveInfinity(startingLoc) || 
                isNegativeInfinity(startingLoc) ||
                isNegativeInfinity(length)) {
            substring = "";
        } else {
            String str = value.stringValue().asJavaString();
            long start = toLong(startingLoc) - 1;
            long end = isPositiveInfinity(length) ? 
                    Long.MAX_VALUE : sum(start, toLong(length));
            if (start > end || start > str.length() || end < 0) {
                substring = "";
            } else {
                start = Math.max(start, 0);
                end = Math.min(end, str.length());
                substring = str.substring((int) start, (int) end);
            }
        }
        return substring;
    }
    
    private boolean isNaN(Value value) {
        return (value instanceof DoubleValue) && 
            Double.isNaN(((DoubleValue)value).asJavaDouble());  
    }
    
    private boolean isNegativeInfinity(Value value) {
        return (value instanceof DoubleValue) && 
            Double.NEGATIVE_INFINITY == ((DoubleValue)value).asJavaDouble();  
    }
    
    private boolean isPositiveInfinity(Value value) {
        return (value instanceof DoubleValue) && 
            Double.POSITIVE_INFINITY == ((DoubleValue)value).asJavaDouble();  
    }
    
    private long toLong(Value val) throws ExpressionException {
        if (val instanceof IntValue) {
            return ((IntValue) val).asJavaInt();
        } else {
            long result;
            if (val instanceof DoubleValue){
                result = Math.round(((DoubleValue)val).asJavaDouble());
            } else {
                String string = val.stringValue().asJavaString();
                try {
                    double value = Double.parseDouble(string);
                    result = Math.round(value);
                } catch (NumberFormatException e){
                    throw new ExpressionException(e);
                }
            }
            return result;
        }
    }
    
    private long sum(long a, long b) {
        long sum = a + b;
        if (a > 0 && b > 0 && sum < 0) {
            return Long.MAX_VALUE;
        } else if (a < 0 && b < 0 && sum > 0) {
            return Long.MIN_VALUE;
        } else {
            return sum;
        }
    }
}
