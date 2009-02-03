/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.concurrency;

import com.volantis.testtools.mock.impl.concurrency.NamedThreadMatcher;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * Test cases for {@link NamedThreadMatcher}.
 */
public class NamedThreadMatcherTestCase
        extends MockTestCaseAbstract {

    /**
     * Ensure that it matches a specific thread.
     */
    public void testMatches() {

        Thread thread = new Thread("thread1");

        ThreadMatcher matcher =
                mockFactory.createNamedThreadMatcher("hello", "thread1");

        assertTrue(matcher.matches(thread));
        assertFalse(matcher.matches(Thread.currentThread()));
    }
}
