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
 * Tests for {@link FloorFunction}
 */
public class FloorFunctionTestCase extends FunctionTestAbstract {
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called without
     * arguments
     */
    public void testNoArguments() {
        final Function function = new FloorFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{});
            fail("Exception wasn't thrown when floor was invoked with 0 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called
     * with two arguments
     */
    public void testTwoArguments() {
        final Function function = new FloorFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{
                    factory.createDoubleValue(10.0), 
                    factory.createDoubleValue(20.0)});
            fail("Exception wasn't thrown when floor was invoked with 2 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if empty sequence is returned for empty sequence
     */
    public void testEmptySequence() throws ExpressionException {
        final Function function = new FloorFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createSequence(new Item[]{})});
        assertTrue(result instanceof Sequence);
        assertEquals(0, result.getSequence().getLength());
    }
    
    /**
     * Tests if function works correctly when argument is positive
     * and has fraction less than 0.5
     */
    public void testPositiveFractionLessThanHalf() throws ExpressionException {
        final Function function = new FloorFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createDoubleValue(10.2)});
        assertTrue(result instanceof DoubleValue);
        assertTrue(10.0 == ((DoubleValue) result).asJavaDouble());
    }
    
    /**
     * Tests if function works correctly when argument is positive
     * and has fraction greater than 0.5
     */
    public void testPositiveFractionGreaterThanHalf() throws ExpressionException {
        final Function function = new FloorFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createDoubleValue(10.7)});
        assertTrue(result instanceof DoubleValue);
        assertTrue(10.0 == ((DoubleValue) result).asJavaDouble());
    }
    
    /**
     * Tests if function works correctly when argument is negative
     * and has fraction less than 0.5
     */
    public void testNegativeFractionLessThanHalf() throws ExpressionException {
        final Function function = new FloorFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createDoubleValue(-10.2)});
        assertTrue(result instanceof DoubleValue);
        assertTrue(-11.0 == ((DoubleValue) result).asJavaDouble());
    }
    
    /**
     * Tests if function works correctly when argument is negative
     * and has fraction greater than 0.5
     */
    public void testNegativeFractionGreaterThanHalf() throws ExpressionException {
        final Function function = new FloorFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createDoubleValue(-10.7)});
        assertTrue(result instanceof DoubleValue);
        assertTrue(-11.0 == ((DoubleValue) result).asJavaDouble());
    }
    
    /**
     * Tests if function works correctly for 0.0
     */
    public void testZero() throws ExpressionException {
        final Function function = new FloorFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createDoubleValue(0)});
        assertTrue(result instanceof DoubleValue);
        assertTrue(0.0 == ((DoubleValue) result).asJavaDouble());
    }
    
    /**
     * Tests if function works correctly for integers
     */
    public void testInt() throws ExpressionException {
        final Function function = new FloorFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createIntValue(4)});
        assertTrue(result instanceof IntValue);
        assertEquals(4, ((IntValue) result).asJavaInt());
    }
    
    /**
     * Tests if function works correctly for not a number
     */
    public void testNotANumber() throws ExpressionException {
        final Function function = new FloorFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                DoubleValue.NOT_A_NUMBER});
        assertTrue(result instanceof DoubleValue);
        assertTrue(Double.isNaN(((DoubleValue) result).asJavaDouble()));
    }
    
    /**
     * Tests if function works correctly for positive infinity
     */
    public void testPositiveInfinity() throws ExpressionException {
        final Function function = new FloorFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                DoubleValue.POSITIVE_INFINITY});
        assertTrue(result instanceof DoubleValue);
        assertTrue(Double.POSITIVE_INFINITY == (((DoubleValue) result).asJavaDouble()));
    }
    
    /**
     * Tests if function works correctly for negative infinity
     */
    public void testNegativeInfinity() throws ExpressionException {
        final Function function = new FloorFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                DoubleValue.NEGATIVE_INFINITY});
        assertTrue(result instanceof DoubleValue);
        assertTrue(Double.NEGATIVE_INFINITY == (((DoubleValue) result).asJavaDouble()));
    }
}
