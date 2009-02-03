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
import com.volantis.xml.expression.atomic.temporal.DateTimeValue;
import com.volantis.xml.expression.atomic.temporal.DateValue;
import com.volantis.xml.expression.atomic.temporal.TimeValue;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * Tests for {@link MinFunction}
 */
public class MinFunctionTestCase extends FunctionTestAbstract {
    
    /**
     * Tests if {@link ExpressionException} is thrown when min is called without
     * arguments
     */
    public void testNoArguments() {
        final Function function = new MinFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{});
            fail("Exception wasn't thrown when min was invoked with 0 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if {@link ExpressionException} is thrown when min is called with
     * two arguments
     */
    public void testTwoArguments() {
        final Function function = new MinFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{
                    factory.createSequence(new Item[]{}), 
                    factory.createDoubleValue(20.0)});
            fail("Exception wasn't thrown when min was invoked with 2 arguments");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests if empty sequence is returned for empty sequence
     */
    public void testEmptySequence() throws ExpressionException {
        final Function function = new MinFunction();
        Value result = function.invoke(expressionContextMock, 
                new Value[]{factory.createSequence(new Item[]{})});
        assertTrue(result instanceof Sequence);
        assertEquals(0, result.getSequence().getLength());
    }
    
    /**
     * Tests min on sequence of integers and doubles
     */
    public void testSequenceOfNumbers() throws ExpressionException {
        final Function function = new MinFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createIntValue(6),
                      factory.createIntValue(3),
                      factory.createDoubleValue(4.3)
                })});
        assertTrue(result instanceof IntValue);
        assertEquals(3, ((IntValue) result).asJavaInt());
    }
    
    /**
     * Tests on sequence of strings. Values should be compared using
     * Unicode code point collation (http://www.w3.org/2005/xpath-functions/collation/codepoint).
     */
    public void testSequenceOfStrings() throws ExpressionException {
        final Function function = new MinFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createStringValue("qwe"),
                      factory.createStringValue("rty"),
                      factory.createStringValue("asd")
                })});
        assertTrue(result instanceof StringValue);
        assertEquals("asd", result.stringValue().asJavaString());
    }
    
    /**
     * Tests on sequence of strings and numbers. {@link ExpressionException}
     * should be thrown.
     */
    public void testSequenceOfStringsAndNumbers() throws ExpressionException {
        final Function function = new MinFunction();
        try {
            function.invoke(expressionContextMock, new Value[]{
                    factory.createSequence(new Item[]{
                            factory.createIntValue(6),
                            factory.createIntValue(3),
                            factory.createStringValue("qwe"),
                            factory.createStringValue("rty"),
                            factory.createStringValue("asd")
                      })});
            fail("Exception wasn't thrown when min was invoked with sequence of strings and numbers");
        } catch (ExpressionException e) {
            //do nothing, expected situation
        }
    }
    
    /**
     * Tests on sequence of dates. Earliest date should be returned.
     */
    public void testSequenceOfDates() throws ExpressionException {
        final Function function = new MinFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createDateValue("2008-03-11"),
                      factory.createDateValue("2006-04-16"),
                      factory.createDateValue("2009-01-02")
                })});
        assertTrue(result instanceof DateValue);
        assertEquals(factory.createDateValue("2006-04-16"), result);
    }
    
    /**
     * Tests on sequence of datetimes. Earliest datetime should be returned.
     */
    public void testSequenceOfDatetimes() throws ExpressionException {
        final Function function = new MinFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createDateTimeValue("2008-03-11T07:56:16+00:00"),
                      factory.createDateTimeValue("2006-04-16T12:30:16+00:00"),
                      factory.createDateTimeValue("2006-04-16T06:56:16+00:00"),
                      factory.createDateTimeValue("2009-01-02T02:56:16+00:00")
                })});
        assertTrue(result instanceof DateTimeValue);
        assertEquals(factory.createDateTimeValue("2006-04-16T06:56:16+00:00"), result);
    }
    
    /**
     * Tests on sequence of times. Earliest time should be returned.
     */
    public void testSequenceOfTimeValues() throws ExpressionException {
        final Function function = new MinFunction();
        Value result = function.invoke(expressionContextMock, new Value[]{
                factory.createSequence(new Item[]{
                      factory.createTimeValue("07:56:16+00:00"),
                      factory.createTimeValue("12:30:16+00:00"),
                      factory.createTimeValue("02:55:21+00:00"),
                      factory.createTimeValue("02:56:16+00:00")
                })});
        assertTrue(result instanceof TimeValue);
        assertEquals(factory.createTimeValue("02:55:21+00:00"), result);
    }
}
