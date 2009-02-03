/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.sequence.Item;

/**
 * Tests for {@link NormalizeSpaceFunction}
 */
public class NormalizeSpaceFunctionTestCase extends FunctionTestAbstract {
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called without
     * arguments
     */
    public void testNoArguments() {
        final Function function = new NormalizeSpaceFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{});
            fail("Exception wasn't thrown when normalize-space was invoked with 0 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called
     * with two arguments
     */
    public void testTwoArguments() {
        final Function function = new NormalizeSpaceFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{
                    factory.createStringValue("arg1"), 
                    factory.createStringValue("arg2")});
            fail("Exception wasn't thrown when normalize-space was invoked with 2 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if empty string is returned for empty sequence
     */
    public void testEmptySequence() throws ExpressionException {
        final Function function = new NormalizeSpaceFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createSequence(new Item[]{})});
        assertTrue(result instanceof StringValue);
        assertEquals("", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if function works correctly for strings without spaces
     */
    public void testStringWithoutSpaces() throws ExpressionException {
        final Function function = new NormalizeSpaceFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("abc")});
        assertTrue(result instanceof StringValue);
        assertEquals("abc", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if function works correctly for strings with spaces
     */
    public void testSpacesOnBothSides() throws ExpressionException {
        final Function function = new NormalizeSpaceFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("  abc   ")});
        assertTrue(result instanceof StringValue);
        assertEquals("abc", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if function works correctly for strings with spaces on left side
     */
    public void testSpacesOnLeftSide() throws ExpressionException {
        final Function function = new NormalizeSpaceFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue(" abc")});
        assertTrue(result instanceof StringValue);
        assertEquals("abc", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if function works correctly for strings with spaces on right side
     */
    public void testSpacesOnRightSide() throws ExpressionException {
        final Function function = new NormalizeSpaceFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("abc  ")});
        assertTrue(result instanceof StringValue);
        assertEquals("abc", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if function works correctly for dates
     */
    public void testDateValue() throws ExpressionException {
        final Function function = new NormalizeSpaceFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createDateValue("2008-08-08")});
        assertTrue(result instanceof StringValue);
        assertTrue(((StringValue) result).asJavaString().startsWith("2008-08-08"));
    }
}
