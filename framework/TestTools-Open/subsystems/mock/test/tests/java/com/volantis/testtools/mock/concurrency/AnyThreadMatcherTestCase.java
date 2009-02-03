/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.concurrency;

import com.volantis.testtools.mock.impl.concurrency.AnyThreadMatcher;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * Test cases for {@link AnyThreadMatcher}.
 */
public class AnyThreadMatcherTestCase
        extends MockTestCaseAbstract {

    /**
     * Ensure that it matches a specific thread.
     */
    public void testMatches() {

        Thread thread = new Thread();
        ThreadMatcher matcher = mockFactory.createAnyThreadMatcher("hello");

        assertTrue(matcher.matches(thread));
        assertTrue(matcher.matches(Thread.currentThread()));
    }
}
