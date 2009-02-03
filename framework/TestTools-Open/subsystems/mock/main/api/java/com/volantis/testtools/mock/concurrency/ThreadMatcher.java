/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.concurrency;

/**
 * Provides ability for mock tests to determine how a thread involved in the
 * test can be identified.
 *
 * <p>Multi threaded mock tests need to be able to define a set of expectations
 * for each thread within the test before those threads are created. If the
 * thread is created by the test that is easy but in many cases the threads are
 * created behind the scenes by one or more of the classes being tested and so
 * it is not always possible to get hold of a reference to the thread and in
 * many cases allowing access would harm the design. Therefore, a number of
 * different ways of identifying the thread are needed, some general and some
 * specific to a test.</p>
 */
public interface ThreadMatcher
        extends Comparable {

    /**
     * The description of the thread.
     *
     * @return The description of the thread(s) that this is intending to
     *         match.
     */
    String getDescription();

    /**
     * The precedence of this matcher.
     *
     * <p>Matchers are checked in order of precedence from highest to
     * lowest.</p>
     *
     * @return The precedence.
     */
    int getPrecedence();

    /**
     * Determines if the thread satisfies the constraints of this matcher.
     *
     * @param thread The thread to check.
     * @return True if it does, false otherwise.
     */
    boolean matches(Thread thread);
}
