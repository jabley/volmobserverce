/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.concurrency;

import com.volantis.testtools.mock.concurrency.ThreadMatcher;

/**
 * Base implementation of {@link ThreadMatcher}.
 */
public abstract class ThreadMatcherImpl
        implements ThreadMatcher {

    /**
     * A description of what this is attempting to match.
     */
    private final String description;

    /**
     * The precedence.
     */
    private final int precedence;

    /**
     * Initialise.
     *
     * @param description The description of the thread(s) this is intended
     *                    to match.
     * @param precedence The precedence.
     */
    protected ThreadMatcherImpl(String description, int precedence) {
        this.description = description;
        this.precedence = precedence;
    }

    // Javadoc inherited.
    public String getDescription() {
        return description;
    }

    // Javadoc inherited.
    public int getPrecedence() {
        return precedence;
    }

    // Javadoc inherited.
    public int compareTo(Object o) {
        if (!(o instanceof ThreadMatcher)) {
            throw new IllegalArgumentException(
                    "Can only compare against ThreadMatcher instances");
        }

        ThreadMatcher other = (ThreadMatcher) o;
        return getPrecedence() - other.getPrecedence();
    }
}
