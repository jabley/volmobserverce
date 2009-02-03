/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2008. All rights reserved
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions.pipeline;

import com.volantis.shared.environment.SimpleEnvironmentInteractionTracker;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.temporal.DateTimeValue;
import com.volantis.xml.expression.functions.FunctionTestAbstract;
import com.volantis.xml.expression.functions.pipeline.DurationToSecondsFunction;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.namespace.DefaultNamespacePrefixTracker;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Test for {@link DurationToSecondsFunction}. 
 */
public class DurationToSecondsFunctionTestCase extends FunctionTestAbstract {

    /**
     * Function.
     */
    private Function function = new DurationToSecondsFunction();
    
    /**
     * Expression context.
     */
    private ExpressionContext context = ExpressionFactory.getDefaultInstance()
            .createExpressionContext(new SimpleEnvironmentInteractionTracker(),
                    new DefaultNamespacePrefixTracker());    
    
    // javadoc inherited
    public void setUp() throws Exception {
        super.setUp();
        context.setProperty(DateTimeValue.class, factory
                .createDateTimeValue(Calendar.getInstance()), false);
        context.setProperty(TimeZone.class, TimeZone.getDefault(), false);
    }
    
    /**
     * Tests empty sequence as an argument.
     */
    public void testEmptySequenceArgument() throws Exception {
        Value result =
            function.invoke(context, new Value[] {Sequence.EMPTY});   
        assertEquals("Expected empty sequence", result, Sequence.EMPTY);
    }
    
    /**
     * Tests duration value as an argument.
     */
    public void testDurationArgument() throws Exception {
        Value result = function.invoke(context, new Value[] { factory
                .createDurationValue(true, 0, 0, 1, 1, 1, 10, 100) }); 
        String resultString = result.stringValue().asJavaString();
        assertEquals("Expected value: 90070 but was: " + resultString,
                resultString, "90070");
    }
    
    /**
     * Tests string value as an argument.
     */
    public void testStringArgument() throws Exception{
        Value result = function.invoke(context,
                new Value[] { factory.createStringValue("P0Y0M0DT1H15M0.0S") });   
        String resultString = result.stringValue().asJavaString();
        assertEquals("Expected value: 4500 but was: " + resultString, resultString,
                "4500");
    }
    
    /**
     * Tests invalid value as an argument.
     */
    public void testWrongArgument() {
        try {
            Value result = function.invoke(context, new Value[] {
                    factory.createDoubleValue(1500.0) });
            fail("Wrong argument value");
        } catch (Exception e) {
            // do nothing
        }         
    }
    
    /**
     * Tests wrong number arguments.
     */
    public void testWrongNumberArguments() {
        try {
            Value result = function.invoke(context, new Value[] {
                    Sequence.EMPTY, Sequence.EMPTY });
            fail("Wrong number of arguments");
        } catch (Exception e) {
            // do nothing
        }
    }
}
