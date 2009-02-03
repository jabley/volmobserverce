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
 * Tests for {@link CountFunction}
 */
public class CountFunctionTestCase extends FunctionTestAbstract {
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called without
     * arguments
     */
    public void testNoArguments() {
        final Function function = new CountFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{});
            fail("Exception wasn't thrown when count was invoked with 0 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called
     * with two arguments
     */
    public void testTwoArguments() {
        final Function function = new CountFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{
                    factory.createStringValue("arg1"), 
                    factory.createStringValue("arg2")});
            fail("Exception wasn't thrown when count was invoked with 2 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if 0 is returned for empty sequence
     */
    public void testEmptySequence() throws ExpressionException {
        final Function function = new CountFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createSequence(new Item[]{})});
        assertTrue(result instanceof IntValue);
        assertEquals(0, ((IntValue) result).asJavaInt());
    }
    
    /**
     * Tests if 1 is returned for integer
     */
    public void testIntValue() throws ExpressionException {
        final Function function = new CountFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createIntValue(3)});
        assertTrue(result instanceof IntValue);
        assertEquals(1, ((IntValue) result).asJavaInt());
    }
    
    /**
     * Tests if 1 is returned for string
     */
    public void testStringValue() throws ExpressionException {
        final Function function = new CountFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("abc")});
        assertTrue(result instanceof IntValue);
        assertEquals(1, ((IntValue) result).asJavaInt());
    }
    
    /**
     * Tests if function works correctly for sequences
     */
    public void testSequence() throws ExpressionException {
        final Function function = new CountFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                        factory.createBooleanValue(true),
                        factory.createStringValue("abc"),
                        DoubleValue.NOT_A_NUMBER,
                        factory.createDurationValue(true, 2, 2, 3, 1, 0, 6, 100)
                })});
        assertTrue(result instanceof IntValue);
        assertEquals(4, ((IntValue) result).asJavaInt());
    }
}
