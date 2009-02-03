/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.Event;
import com.volantis.testtools.mock.Expectation;

import java.util.Iterator;
import java.util.List;

public abstract class RepeatableExpectationTestAbstract
        extends ExpectationTestAbstract {

    /**
     * Create the expectation that will be tested.
     *
     * @return The expectation that will be tested.
     */
    protected abstract Expectation createExpectation();

    /**
     * Create the list of events that will satisfy the expectation returned
     * by {@link #createExpectation}.
     *
     * @return A list of events.
     */
    protected abstract List createSatisfyingEvents();

    public void testRepeated() {
        Expectation expectation = createExpectation();
        List events = createSatisfyingEvents();

        RepeatingExpectation repeating = new RepeatingExpectation(1, 3);
        repeating.addExpectation(expectation);

        // Iterate over the events and target them at the repeating method.
        for (Iterator iterator = events.iterator(); iterator.hasNext();) {
            Event event = (Event) iterator.next();
            repeating.checkExpectations(ExpectationState.CURRENT, event, report);
        }

        // Ensure that the expectations have been met.
        repeating.verify();

        // Now iterator over them again.
        for (Iterator iterator = events.iterator(); iterator.hasNext();) {
            Event event = (Event) iterator.next();
            repeating.checkExpectations(ExpectationState.CURRENT, event, report);
        }

        // Ensure that the expectations have been met.
        repeating.verify();

        // Now iterator over them again.
        for (Iterator iterator = events.iterator(); iterator.hasNext();) {
            Event event = (Event) iterator.next();
            repeating.checkExpectations(ExpectationState.CURRENT, event, report);
        }

        // Ensure that the expectations have been met.
        repeating.verify();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
