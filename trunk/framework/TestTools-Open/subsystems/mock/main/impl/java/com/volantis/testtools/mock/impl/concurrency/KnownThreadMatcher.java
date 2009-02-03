/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.concurrency;

import com.volantis.testtools.mock.concurrency.ThreadMatcher;

/**
 * Use this when the thread to be matched is known in advance.
 *
 * <p>These have the highest possible precedence so are always used first.</p>
 */
public class KnownThreadMatcher
        extends ThreadMatcherImpl {

    /**
     * The known thread.
     */
    private final Thread knownThread;

    /**
     * Initialise.
     *
     * @param description The description of the thread(s) this is intended
     *                    to match.
     * @param knownThread The thread that this matches.
     */
    public KnownThreadMatcher(String description, Thread knownThread) {
        super(description, Integer.MAX_VALUE);

        this.knownThread = knownThread;
    }

    /**
     * Initialise.
     *
     * <p>Binds to the current thread.</p>
     *
     * @param description The description of the thread(s) this is intended
     *                    to match.
     */
    public KnownThreadMatcher(String description) {
        super(description, Integer.MAX_VALUE);

        this.knownThread = Thread.currentThread();
    }

    // Javadoc inherited.
    public boolean matches(Thread thread) {
        return knownThread == thread;
    }


    // Javadoc inherited.
    public int compareTo(Object o) {
        if (!(o instanceof ThreadMatcher)) {
            throw new IllegalArgumentException(
                    "Can only compare against ThreadMatcher instances");
        }

        ThreadMatcher other = (ThreadMatcher) o;

        if (other instanceof KnownThreadMatcher) {
            KnownThreadMatcher otherKnown = (KnownThreadMatcher) other;
            if (knownThread == otherKnown.knownThread) {
                throw new IllegalStateException(
                        "Cannot have multiple matchers that target the " +
                                "same known thread");
            } else {
                return knownThread.getName()
                        .compareTo(otherKnown.knownThread.getName());
            }
        }

        return getPrecedence() - other.getPrecedence();
    }
}
