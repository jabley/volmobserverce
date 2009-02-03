/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.concurrency;

/**
 * Use this when the thread to be matched has a well known name.
 *
 * <p>These have a precedence of 10000.</p>
 */
public class NamedThreadMatcher
        extends ThreadMatcherImpl {

    /**
     * The name to compare against the supplied thread's name.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param description The description of the thread(s) this is intended
     *                    to match.
     * @param name The name to compare against the supplied thread's name.
     */
    public NamedThreadMatcher(String description, String name) {
        super(description, 10000);

        this.name = name;
    }

    // Javadoc inherited.
    public boolean matches(Thread thread) {
        return name.equals(thread.getName());
    }
}
