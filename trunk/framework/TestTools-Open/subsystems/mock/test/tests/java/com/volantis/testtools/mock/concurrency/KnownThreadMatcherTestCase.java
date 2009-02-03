/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.concurrency;

import com.volantis.testtools.mock.impl.concurrency.KnownThreadMatcher;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * Test cases for {@link KnownThreadMatcher}.
 */
public class KnownThreadMatcherTestCase
        extends MockTestCaseAbstract {

    /**
     * Ensure that it matches a specific thread.
     */
    public void testMatches() {

        Thread thread = new Thread();
        ThreadMatcher matcher =
                mockFactory.createKnownThreadMatcher("hello", thread);

        assertTrue(matcher.matches(thread));
        assertFalse(matcher.matches(Thread.currentThread()));
    }
}
