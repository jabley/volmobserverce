/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.sequence.Item;

/**
 * Tests for {@link StringLengthFunction}
 */
public class StringLengthFunctionTestCase extends FunctionTestAbstract {

    /**
     * Tests if {@link ExpressionException} is thrown when function is called without
     * arguments
     */
    public void testNoArguments() {
        final Function function = new StringLengthFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{});
            fail("Exception wasn't thrown when string-length was invoked with 0 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called
     * with two arguments
     */
    public void testTwoArguments() {
        final Function function = new StringLengthFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{
                    factory.createStringValue("arg1"), 
                    factory.createStringValue("arg2")});
            fail("Exception wasn't thrown when string-length was invoked with 2 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if function works correctly for string
     */
    public void testString() throws ExpressionException {
        final Function function = new StringLengthFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("qwerty")});
        assertTrue(result instanceof IntValue);
        assertEquals(6, ((IntValue) result).asJavaInt());
    }
    
    /**
     * Tests if function returns 0 for empty sequence
     */
    public void testEmptySequence() throws ExpressionException {
        final Function function = new StringLengthFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createSequence(new Item[]{})});
        assertTrue(result instanceof IntValue);
        assertEquals(0, ((IntValue) result).asJavaInt());
    }
}
