/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.concurrency;

import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.expectations.ExpectationBuilderInternal;
import com.volantis.testtools.mock.expectations.Expectations;
import com.volantis.testtools.mock.method.Occurrences;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface PerThreadExpectationBuilder
        extends ExpectationBuilderInternal {

    /**
     * Add a builder for the {@link ThreadMatcher}.
     *
     * @param matcher The matcher whose builder should be added.
     * @param builder The builder for the {@link ThreadMatcher}.
     */
    void addThreadSpecificBuilder(
            ThreadMatcher matcher, ExpectationBuilder builder);

    /**
     * Add expectations to the builder used for the thread identified by
     * {@link ThreadMatcher}.
     *
     * <p>A builder must already have been added for the same
     * {@link ThreadMatcher} as is provided to this method.</p>
     *
     * @param matcher      The matcher to whose builder the expectations should
     *                     be added.
     * @param expectations The expectations to add.
     * @return The object to control the number of occurences of the
     *         expectations.
     * @throws IllegalStateException If no builder could be found for the
     *                               supplied matcher.
     */
    Occurrences add(ThreadMatcher matcher, Expectations expectations);
}
