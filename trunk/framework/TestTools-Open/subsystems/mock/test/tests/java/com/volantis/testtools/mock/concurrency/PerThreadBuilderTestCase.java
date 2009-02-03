/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.concurrency;

import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * Test cases for {@link PerThreadExpectationBuilder}.
 */
public class PerThreadBuilderTestCase
        extends MockTestCaseAbstract {

    /**
     * Ensure that if two known thread matchers that match the same thread are
     * added to a {@link PerThreadExpectationBuilder} that it fails.
     */
    public void testDetectKnownThreadClash() throws Exception {

        PerThreadExpectationBuilder builder =
                mockFactory.createPerThreadBuilder();
        ExpectationBuilder builder1 = mockFactory.createOrderedBuilder();
        ExpectationBuilder builder2 = mockFactory.createUnorderedBuilder();

        ThreadMatcher matcher1 =
                mockFactory.createKnownThreadMatcher("matcher 1");
        ThreadMatcher matcher2 =
                mockFactory.createKnownThreadMatcher("matcher 2");

        builder.addThreadSpecificBuilder(matcher1, builder1);
        try {
            builder.addThreadSpecificBuilder(matcher2, builder2);
            fail("Did not detect two known thread matchers that match the " +
                    "same thread");
        } catch (IllegalStateException expected) {
            assertEquals("Cannot have multiple matchers that target the same " +
                    "known thread", expected.getMessage());
        }
    }
}
