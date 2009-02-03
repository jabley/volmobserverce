/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.EventEffect;
import com.volantis.testtools.mock.Event;
import com.volantis.testtools.mock.expectations.Report;

/**
 * The base of expectations that only match one event and which cannot be
 * partially satisfied.
 *
 * <p>This is not suitable for expectations that can be partially
 * satisfied, e.g. if the expectation is optional.</p>
 */
public abstract class AbstractSingleExpectation
        extends AbstractExpectation {

    /**
     * Initialise and mark it as immutable.
     */
    public AbstractSingleExpectation() {
        makeImmutable();
    }

    /**
     * Consume the event updating any necessary state and performing
     * appropriate actions.
     *
     * @param event The event to consume.
     */
    protected abstract boolean checkExpectation(Event event);

    // Javadoc inherited.
    protected EventEffect checkExpectationsImpl(ExpectationState state,
                                           Event event,
                                           Report report) {

        // A single expectation can only match or fail, it can never just
        // be satisfied. Delegate to a method that checks the behaviour.
        boolean matched = checkExpectation(event);
        if (!matched) {
            debug(report);
            return EventEffect.WOULD_FAIL;
        }

        // If this is in the current state then reset before updating the
        // state. To be honest this is not strictly necessary as the code that
        // immediately follows undoes the effect of this anyway. However, it
        // fits into the pattern of the other expectations.
        if (state == ExpectationState.INITIAL) {
            reset();
        }

        // After consuming an event this expectation is completely satisfied.
        markAsCompletelySatisfied();

        // The event matched the expectation.
        return EventEffect.MATCHED_EXPECTATION;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 14-Jun-05	7997/4	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 10-May-04	4164/4	pduffin	VBM:2004050404 Fixed some problems with HTML orientation separator

 07-May-04	4164/2	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
