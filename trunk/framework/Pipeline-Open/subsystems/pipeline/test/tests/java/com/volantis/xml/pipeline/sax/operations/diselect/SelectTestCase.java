/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.operations.diselect;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;

/**
 * Test class for the select rule (and when and otherwise)
 */
public class SelectTestCase extends PipelineTestAbstract {

    /**
     * Tests that a select is skipped if its expr evaluates to false
     */
    public void testSelectWithFalseExpression() throws Exception {
        doTest(
                new TestPipelineFactory(),
                "select-with-false-expression.input.xml",
                "select-with-false-expression.expected.xml");
    }

    /**
     * Tests that a select is included if its expr evaluates to true
     */
    public void testSelectWithTrueExpression() throws Exception {
        doTest(
                new TestPipelineFactory(),
                "select-with-true-expression.input.xml",
                "select-with-true-expression.expected.xml");
    }

    /**
     * Tests that a select is included if has no expr attribute
     */
    public void testSelectWithNoExpression() throws Exception {
        doTest(
                new TestPipelineFactory(),
                "select-with-no-expression.input.xml",
                "select-with-no-expression.expected.xml");
    }

    /**
     * Tests that a when in a select with a false expr is skipped
     */
    public void testWhenFalseExpression() throws Exception {
        doTest(
                new TestPipelineFactory(),
                "when-false.input.xml",
                "when-false.expected.xml");
    }

    /**
     * Tests that only the first 'true' when is included for matchfirst
     */
    public void testMultipleMatchfirst() throws Exception {
        doTest(
                new TestPipelineFactory(),
                "when-multiple-matchfirst.input.xml",
                "when-multiple-matchfirst.expected.xml");
    }

    /**
     * Tests that every 'true' when is included in matchevery
     */
    public void testMultipleMatchevery() throws Exception {
        doTest(
                new TestPipelineFactory(),
                "when-multiple-matchevery.input.xml",
                "when-multiple-matchevery.expected.xml");
    }

    /**
     * Tests that otherwise is included when there is no previous match
     */
    public void testOtherwiseNoPreviousMatch() throws Exception {
        doTest(
                new TestPipelineFactory(),
                "otherwise-no-previousmatch.input.xml",
                "otherwise-no-previousmatch.expected.xml");
    }

    /**
     * Tests that otherwise is skipped when there is a previous match
     */
    public void testOtherwisePreviousMatch() throws Exception {
        doTest(
                new TestPipelineFactory(),
                "otherwise-previousmatch.input.xml",
                "otherwise-previousmatch.expected.xml");
    }

    /**
     * Tests that selects can be nested
     */
    public void testNestedSelects() throws Exception {
        doTest(
                new TestPipelineFactory(),
                "nested-selects.input.xml",
                "nested-selects.expected.xml");
    }
}