/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.concurrency;

/**
 * Use this when nothing is known of the thread to be matched
 *
 * <p>These have a precedence of 0, and will only be used as a last resort.</p>
 */
public class AnyThreadMatcher
        extends ThreadMatcherImpl {

    /**
     * Initialise.
     *
     * @param description The description of the thread(s) this is intended
     *                    to match.
     */
    public AnyThreadMatcher(final String description) {
        super(description, 0);
    }

    // Javadoc inherited.
    public boolean matches(Thread thread) {
        return true;
    }
}
