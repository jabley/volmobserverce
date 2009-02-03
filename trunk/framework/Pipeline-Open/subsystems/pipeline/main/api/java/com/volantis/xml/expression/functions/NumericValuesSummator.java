/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.atomic.numeric.NumericValue;
import com.volantis.xml.expression.sequence.Sequence;

import java.math.BigDecimal;

/**
 * Class responsible for calculating a sum of numeric values.
 */
public class NumericValuesSummator {
    
    /**
     * True if all added values are integers
     */
    private boolean allNumbersAreIntegers = true;
    
    /**
     * Result of adding finite values. After any infinite or not a number value
     * is added all finite values are ignored because they don't change
     * the result.
     */
    private BigDecimal sumOfFiniteValues = BigDecimal.valueOf(0);
    
    /**
     * Result of adding all {@value Double#NaN}, {@value Double#POSITIVE_INFINITY}
     * and {@value Double#NEGATIVE_INFINITY} values. Value 0 means that no
     * such value was added. 
     */
    private double sumOrInfiniteAndNotNumberValues = 0;
    
    public void addValue(Value value) throws ExpressionException {
        if (value instanceof IntValue) {
            addValue((IntValue) value);
        } else {
            if (value instanceof DoubleValue) {
                addValue((DoubleValue) value);
            } else if (value instanceof Sequence) {
                addAllValues(value.getSequence());
            } else {
                addValue(value.stringValue());
            }
        }
    }
    
    /**
     * Adds an integer value
     * 
     * @param value the value to be added
     */
    public void addValue(IntValue value) {
        if (sumOrInfiniteAndNotNumberValues == 0) {
            sumOfFiniteValues = sumOfFiniteValues.add(
                    BigDecimal.valueOf(value.asJavaInt()));
        }
    }
    
    /**
     * Adds a double value
     * 
     * @param value the value to be added
     */
    public void addValue(DoubleValue value) {
        addValue(value.asJavaDouble());
        allNumbersAreIntegers = false;
    }
    
    /**
     * Adds a value represented as string
     * 
     * @param value the value to be added
     * @throws if given value does not represent a number
     */
    public void addValue(StringValue value) throws ExpressionException {
        addValue(PipelineExpressionHelper.stringToDouble(value.stringValue()));
        allNumbersAreIntegers = false;
    }
    
    /**
     * Adds all values from given sequence
     * 
     * @param sequence sequence of values to be added
     */
    public void addAllValues(Sequence sequence) throws ExpressionException {
        for (int i = 1; i <= sequence.getLength(); i++) {
            addValue(sequence.getItem(i));
        }
    }
    
    /**
     * Returns the sum as double
     */
    public double getSumAsDouble() {
        return sumOrInfiniteAndNotNumberValues != 0 ? 
                sumOrInfiniteAndNotNumberValues : sumOfFiniteValues.doubleValue();
    }
    
    /**
     * Returns the sum as {@link DoubleValue}
     * 
     * @param context expression context used to create the value
     * @return the sum
     */
    public DoubleValue getSumAsDouble(ExpressionContext context) {
        return context.getFactory().createDoubleValue(getSumAsDouble());
    }
    
    /**
     * Returns the sum as numeric value, integer if all added values are
     * integers, double otherwise 
     * 
     * @param context expression context used to create the value
     * @return the sum
     */
    public NumericValue getSum(ExpressionContext context) {
        return allNumbersAreIntegers ?
                (NumericValue) context.getFactory().createIntValue(
                        sumOfFiniteValues.intValue()) 
                    : (NumericValue) getSumAsDouble(context);
    }
    
    /**
     * Adds double to the sum
     * 
     * @param value value to be added
     */
    private void addValue(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            sumOrInfiniteAndNotNumberValues = sumOrInfiniteAndNotNumberValues + value;
        } else {
            if (sumOrInfiniteAndNotNumberValues == 0) {
                sumOfFiniteValues = sumOfFiniteValues.add(
                        new BigDecimal(Double.toString(value)));
            }
        }
    }
}
