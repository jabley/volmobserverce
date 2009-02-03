/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.operations.diselect;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Function;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;

/**
 * Test class for the if rule
 */
public class IfRuleTestCase extends PipelineTestAbstract {

    /**
     * Tests that the contents is shown for a true expression
     */
    public void testIfTrue() throws Exception {
        doTest(new TestPipelineFactory(),
                "if-true-input.xml",
                "if-true-expected.xml");
    }

    /**
     * Tests that the contents is skipped for a true expression
     */
    public void testIfFalse() throws Exception {
        doTest(new TestPipelineFactory(),
                "if-false-input.xml",
                "if-false-expected.xml");
    }

    /**
     * test the behaviour when the expression evals to an empty sequence
     */
    public void testIfEmpty() throws Exception {
	doTest(new TestPipelineFactory(),
               "if-empty-input.xml",
               "if-empty-expected.xml");
    }

    // javadoc inherited
    protected void registerExpressionFunctions(ExpressionContext context) {
        //add a new function capable of returning an empty sequence.
        context.registerFunction(
            new ImmutableExpandedName("", "empty-sequence"),
            new Function() {
                public Value invoke(ExpressionContext context, Value[] args) {
                    return Sequence.EMPTY;
                }
            });

    }

}
