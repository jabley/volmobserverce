/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;


import com.volantis.testtools.mock.expectations.Report;
import com.volantis.testtools.mock.Event;
import com.volantis.testtools.mock.EventEffect;
import com.volantis.testtools.mock.Expectation;

import java.io.PrintWriter;

/**
 * An expectation that consists of a sequence of other expectations.
 */
public class ExpectationSequence
        extends AbstractCompositeExpectation {

    /**
     * The position of the next expectation to satisfy.
     */
    private int currentPosition;

    /**
     * Initialise this instance.
     */
    public ExpectationSequence() {
    }

    /**
     * Initialise this instance.
     */
    public ExpectationSequence(String description) {
        super("OrderedExpectations: " + description);
    }

    // Javadoc inherited
    public void reset() {
        super.reset();

        currentPosition = 0;
    }

    protected EventEffect checkNestedExpectations(
            ExpectationState state, Event event, Report report) {

        int position;
        if (state == ExpectationState.INITIAL) {
            position = 0;
        } else if (state == ExpectationState.CURRENT) {
            position = currentPosition;
        } else {
            throw new IllegalArgumentException("Unknown expectation state " + state);
        }

        // Try all the remaining unsatisfied expectations until we find one
        // that will fail, or consume the event.
        EventEffect effect = EventEffect.WOULD_SATISFY;
        int size = expectations.size();

        for (int i = position; i < size && effect == EventEffect.WOULD_SATISFY; i += 1) {
            // Check to see if the next unsatisfied expectation can consume the
            // event.
            InternalExpectation expectation =
                    (InternalExpectation) expectations.get(i);
            effect = expectation.checkExpectations(state, event, report);

            // If the event was matched by this expectation then update
            // the position to ensure that when the event is processed that
            // it will be processed by the correct expectation.
            if (effect == EventEffect.MATCHED_EXPECTATION) {

                if (state == ExpectationState.INITIAL) {
                    resetAllApartFrom(expectation);
                }

                currentPosition = i;

                // If consuming the event means that the expectation is now satisfied
                // move onto the next expectation.
                if (expectation.isCompletelySatisfied(ExpectationState.CURRENT)) {
                    currentPosition += 1;
                }
            }
        }

        return effect;
    }

    /**
     * The contents of an expectation sequence require wrapping if the
     * container is not null, not an instance of this and the size is greater
     * than 1.
     */
    protected boolean opaqueContainer(Expectation container) {
        return !(container instanceof ExpectationSequence)
                && expectations.size() > 1;
    }

    protected void before(Report report) {
        PrintWriter out = report.getPrintWriter();
        out.println("The following:");
        report.beginBlock();
    }

    protected void after(Report report) {
        report.endBlock();
    }

    protected boolean isCompletelySatisfiedImpl(ExpectationState state) {
        int position;
        if (state == ExpectationState.INITIAL) {
            position = 0;
        } else if (state == ExpectationState.CURRENT) {
            position = currentPosition;
        } else {
            throw new IllegalArgumentException("Unknown expectation state " + state);
        }

        return position == expectations.size();
    }

//    // Javadoc inherited.
//    protected boolean verifyImpl(Report report) {
//        super.verifyImpl(report);
//        verifyContainedExpectations(expectations, currentPosition,
//                                    currentPosition, report);
//    }
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

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
