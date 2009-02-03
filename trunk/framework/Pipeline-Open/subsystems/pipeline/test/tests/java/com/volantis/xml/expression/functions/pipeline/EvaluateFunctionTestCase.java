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
 * Test for {@link EvaluateFunction}.
 */
public class EvaluateFunctionTestCase extends FunctionTestAbstract {

    /**
     * Function.
     */
    private Function function = new EvaluateFunction();

    /**
     * Expression context.
     */
    private ExpressionContext context = ExpressionFactory.getDefaultInstance()
            .createExpressionContext(new SimpleEnvironmentInteractionTracker(),
                    new DefaultNamespacePrefixTracker());

    protected void setUp()
            throws Exception {
        super.setUp();

        context.getNamespacePrefixTracker().startPrefixMapping("pipeline",
                "http://www.volantis.com/xmlns/marlin-pipeline");
    }

    /**
     * Tests empty sequence as an argument.
     */
    public void testEmptySequenceArgument() throws Exception {
        try {
            function.invoke(context, new Value[] {Sequence.EMPTY});
            fail("Did not detect parse error");
        } catch (Exception e) {
            // Do nothing.
        }
    }

    /**
     * Tests string as an argument.
     */
    public void testStringArgument() throws Exception {
        Value result = function.invoke(context, new Value[] { factory
                .createStringValue("'abc'") }); 
        String resultString = result.stringValue().asJavaString();
        assertEquals("Expected value: abc but was: " + resultString,
                resultString, "abc");
    }

    /**
     * Tests string value as an argument.
     */
    public void testNestedArgument() throws Exception{
        Value result = function.invoke(context,
                new Value[] { factory.createStringValue("pipeline:evaluate('\"abc\"')") });
        String resultString = result.stringValue().asJavaString();
        assertEquals("Expected value: abc but was: " + resultString,
                resultString, "abc");
    }

    /**
     * Tests wrong number arguments.
     */
    public void testWrongNumberArguments() {
        try {
            function.invoke(context, new Value[] {
                    Sequence.EMPTY, Sequence.EMPTY });
            fail("Wrong number of arguments");
        } catch (Exception e) {
            // do nothing
        }
    }
}