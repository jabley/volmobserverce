/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.sequence.Item;

/**
 * Tests for {@link SumFunction}
 */
public class SumFunctionTestCase extends FunctionTestAbstract {

    /**
     * Tests if {@link ExpressionException} is thrown when function is called without
     * arguments
     */
    public void testNoArguments() {
        final Function function = new SumFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{});
            fail("Exception wasn't thrown when sum was invoked with 0 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if for empty sequence sum is 0
     */
    public void testEmptySequence() throws ExpressionException {
        final Function function = new SumFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createSequence(new Item[]{})});
        assertTrue(result instanceof IntValue);
        assertEquals(0, ((IntValue) result).asJavaInt());
    }
    
    /**
     * Tests sum on sequence of numbers
     */
    public void testSequenceOfNumbers() throws ExpressionException {
        final Function function = new SumFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createIntValue(6),
                      factory.createIntValue(3),
                      factory.createDoubleValue(4.2)
                })});
        assertTrue(result instanceof DoubleValue);
        assertTrue(13.2 == ((DoubleValue) result).asJavaDouble());
    }
    
    /**
     * Tests if integer is returned when argument is a sequence of integers
     */
    public void testSequenceOfIntegers() throws ExpressionException {
        final Function function = new SumFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createIntValue(6),
                      factory.createIntValue(3),
                      factory.createIntValue(4)
                })});
        assertTrue(result instanceof IntValue);
        assertEquals(13, ((IntValue) result).asJavaInt());
    }
    
    /**
     * Tests if no inexactness was introduced from binary representation of numbers
     */
    public void testResultIsExact() throws ExpressionException {
        final Function function = new SumFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createDoubleValue(0.01),
                      factory.createDoubleValue(0.01),
                      factory.createDoubleValue(0.01),
                      factory.createDoubleValue(0.01),
                      factory.createDoubleValue(0.01),
                      factory.createDoubleValue(0.01),
                })});
        assertTrue(result instanceof DoubleValue);
        assertTrue(0.06 == ((DoubleValue) result).asJavaDouble());
    }
    
    /**
     * Tests sum on sequence containing not a number
     */
    public void testNotANumber() throws ExpressionException {
        final Function function = new SumFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createIntValue(6),
                      DoubleValue.NOT_A_NUMBER,
                      factory.createIntValue(3),
                      factory.createDoubleValue(4.2)
                })});
        assertTrue(result instanceof DoubleValue);
        assertTrue(Double.isNaN(((DoubleValue) result).asJavaDouble()));
    }
    
    /**
     * Tests sum on sequence containing infinity
     */
    public void testInfinity() throws ExpressionException {
        final Function function = new SumFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createIntValue(6),
                      DoubleValue.POSITIVE_INFINITY,
                      factory.createIntValue(3),
                      factory.createDoubleValue(4.2)
                })});
        assertTrue(result instanceof DoubleValue);
        assertTrue(Double.POSITIVE_INFINITY == ((DoubleValue) result).asJavaDouble());
    }
    
    /**
     * Tests sum on sequence containing infinity
     */
    public void testPositiveAndNegativeInfinity() throws ExpressionException {
        final Function function = new SumFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createIntValue(6),
                      DoubleValue.POSITIVE_INFINITY,
                      factory.createIntValue(3),
                      DoubleValue.NEGATIVE_INFINITY,
                      factory.createDoubleValue(4.2)
                })});
        assertTrue(result instanceof DoubleValue);
        assertTrue(Double.isNaN(((DoubleValue) result).asJavaDouble()));
    }
}
