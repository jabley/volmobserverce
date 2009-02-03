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
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.sequence.Item;

/**
 * Tests for {@link SubstringFunction}
 */
public class SubstringFunctionTestCase extends FunctionTestAbstract {
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called without
     * arguments
     */
    public void testNoArguments() {
        final Function function = new SubstringFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{});
            fail("Exception wasn't thrown when substring was invoked with 0 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called
     * with one argument
     */
    public void testOneArgument() {
        final Function function = new SubstringFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{factory.createStringValue("abc")});
            fail("Exception wasn't thrown when substring was invoked with 1 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if {@link ExpressionException} is thrown when function is called
     * with four arguments
     */
    public void testFourArgument() {
        final Function function = new SubstringFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{
                    factory.createStringValue("arg1"),
                    factory.createStringValue("arg2"),
                    factory.createStringValue("arg3"),
                    factory.createStringValue("arg4")});
            fail("Exception wasn't thrown when substring was invoked with 4 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if function works with two arguments
     */
    public void testTwoArguments() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("123456"), factory.createIntValue(3)});
        assertTrue(result instanceof StringValue);
        assertEquals("3456", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if function works correctly when substring start is negative
     */
    public void testTwoArgumentsStartNegative() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("123456"), factory.createIntValue(-3)});
        assertTrue(result instanceof StringValue);
        assertEquals("123456", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if function works correctly when substring start is greater
     * than original string's length
     */
    public void testTwoArgumentsStartGreaterThanLength() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("123456"), factory.createIntValue(10)});
        assertTrue(result instanceof StringValue);
        assertEquals("", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if empty string is returned for empty sequence
     */
    public void testEmptySequence() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createSequence(new Item[]{}), 
                factory.createIntValue(1)});
        assertTrue(result instanceof StringValue);
        assertEquals("", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if function works with three arguments
     */
    public void testThreeArguments() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("123456"), 
                factory.createIntValue(3), factory.createIntValue(2)});
        assertTrue(result instanceof StringValue);
        assertEquals("34", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if number passed to function is casted to string
     */
    public void testSubstringOfNumber() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createIntValue(123456), 
                factory.createIntValue(3), factory.createIntValue(2)});
        assertTrue(result instanceof StringValue);
        assertEquals("34", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if substring start and length are correctly rounded when
     * specified as doubles
     */
    public void testBoundariesAsDoubles() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("123456"), 
                factory.createDoubleValue(3.5), factory.createDoubleValue(2.2)});
        assertTrue(result instanceof StringValue);
        assertEquals("45", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if exception is not thrown and correct string is returned 
     * when specified substring length is greater than length of original
     * string
     */
    public void testLengthGreaterThanStringLength() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("123456"), 
                factory.createIntValue(2), factory.createIntValue(100)});
        assertTrue(result instanceof StringValue);
        assertEquals("23456", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if correct string is returned when specified substring 
     * length is a positive infinity.
     */
    public void testLengthPositiveInfinity() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("123456"), 
                factory.createIntValue(2), DoubleValue.POSITIVE_INFINITY});
        assertTrue(result instanceof StringValue);
        assertEquals("23456", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if empty string is returned when specified substring 
     * start is not a number
     */
    public void testStartIsNotANumber() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("123456"), 
                DoubleValue.NOT_A_NUMBER, factory.createDoubleValue(2)});
        assertTrue(result instanceof StringValue);
        assertEquals("", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if substring is created when specified substring 
     * start is a string that can be converted to number
     */
    public void testStartAsString() throws ExpressionException {
        final Function function = new SubstringFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createStringValue("123456"), 
                factory.createStringValue("2"), factory.createDoubleValue(2.2)});
        assertTrue(result instanceof StringValue);
        assertEquals("23", ((StringValue) result).asJavaString());
    }
    
    /**
     * Tests if {@link ExpressionException} is thrown when specified substring 
     * start is a string that can't be converted to number
     */
    public void testStartAsNotNumericString() {
        final Function function = new SubstringFunction();
        try {
            function.invoke(expressionContextMock, 
                    new Value[]{factory.createStringValue("123456"), 
                    factory.createStringValue("abc"), factory.createDoubleValue(2.2)});
            fail("substring should fail when start position is a string");
        } catch (ExpressionException e) {
            //ignore, expected situation
        }
    }
    
    /**
     * Tests if {@link ExpressionException} is thrown when specified substring 
     * length is a string that can't be converted to number
     */
    public void testLengthAsNotNumericString() {
        final Function function = new SubstringFunction();
        try {
            function.invoke(expressionContextMock, 
                    new Value[]{factory.createStringValue("123456"), 
                    factory.createDoubleValue(2.2), factory.createStringValue("abc"), });
            fail("substring should fail when length is a string");
        } catch (ExpressionException e) {
            //ignore, expected situation
        }
    }
}
