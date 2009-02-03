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
import com.volantis.xml.expression.sequence.Sequence;

/**
 * Tests for {@link AvgFunction}
 */
public class AvgFunctionTestCase extends FunctionTestAbstract {
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called without
     * arguments
     */
    public void testNoArguments() {
        final Function function = new AvgFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{});
            fail("Exception wasn't thrown when avg was invoked with 0 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if empty sequence is returned for empty sequence
     */
    public void testEmptySequence() throws ExpressionException {
        final Function function = new AvgFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createSequence(new Item[]{})});
        assertTrue(result instanceof Sequence);
        assertEquals(0, result.getSequence().getLength());
    }
    
    /**
     * Tests avg on sequence of integers and doubles
     */
    public void testSequenceOfNumbers() throws ExpressionException {
        final Function function = new AvgFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createIntValue(6),
                      factory.createIntValue(3),
                      factory.createDoubleValue(4.2)
                })});
        assertTrue(result instanceof DoubleValue);
        assertTrue(4.4 == ((DoubleValue) result).asJavaDouble());
    }
    
    /**
     * Tests avg on sequence containing not a number
     */
    public void testNotANumber() throws ExpressionException {
        final Function function = new AvgFunction();
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
     * Tests avg on sequence containing infinity
     */
    public void testInfinity() throws ExpressionException {
        final Function function = new AvgFunction();
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
     * Tests avg on sequence containing infinity
     */
    public void testPositiveAndNegativeInfinity() throws ExpressionException {
        final Function function = new AvgFunction();
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
