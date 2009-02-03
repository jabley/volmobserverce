/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.Event;
import com.volantis.testtools.mock.EventEffect;
import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.expectations.Report;

import java.io.PrintWriter;

/**
 * An expectation that consists of a set of other expectations.
 *
 * <p>All the contained expectations must be satisfied but the order of
 * satisfaction does not count.</p>
 */
public class ExpectationSet
        extends AbstractCompositeExpectation {

    /**
     * A count of all the expectations that have been satisfied.
     */
    private int currentSatisfiedCount;

    public ExpectationSet() {
    }

    public ExpectationSet(String description) {
        super("UnorderedExpectations: " + description);
    }

    // Javadoc inherited
    public void reset() {
        super.reset();

        currentSatisfiedCount = 0;
    }

    // Javadoc inherited
    protected boolean opaqueContainer(Expectation container) {
        return !(container instanceof ExpectationSet)
                && expectations.size() > 1;
    }

    // Javadoc inherited
    protected void before(Report report) {
        PrintWriter out = report.getPrintWriter();
        out.println("One of the following:");
        report.beginBlock();
    }

    protected EventEffect checkNestedExpectations(
            ExpectationState state, Event event, Report report) {

        // Iterate over all the expectations, ignoring those that have already
        // been satisfied to see what effect it will have on it.
        boolean allOptional = true;
        int size = expectations.size();
        for (int i = 0; i < size; i += 1) {
            InternalExpectation expectation =
                    (InternalExpectation) expectations.get(i);
            if (expectation.isCompletelySatisfied(state)) {
                continue;
            }

            EventEffect effect = expectation.checkExpectations(state, event, report);
            if (effect == EventEffect.MATCHED_EXPECTATION) {

                // If this is in the initial state then reset the count of the
                // number of satisfied nested expectations.
                if (state == ExpectationState.INITIAL) {
                    resetAllApartFrom(expectation);
                }

                // If consuming the event means that the expectation is now
                // satisfied update the number of satisfied expectations.
                if (expectation.isCompletelySatisfied(ExpectationState.CURRENT)) {
                    currentSatisfiedCount += 1;
                }

                return effect;

            } else if (effect != EventEffect.WOULD_SATISFY) {
                allOptional = false;
            }
        }

        // None of the expectations could consume the event so if they are all
        // optional then this is optional otherwise this will fail.
        if (allOptional) {
            return EventEffect.WOULD_SATISFY;
        } else {
            return EventEffect.WOULD_FAIL;
        }
    }

    protected void after(Report report) {
        report.endBlock();
    }

    // Javadoc inherited.
    protected boolean isCompletelySatisfiedImpl(ExpectationState state) {
        int satisfiedCount;
        if (state == ExpectationState.INITIAL) {
            satisfiedCount = 0;
        } else if (state == ExpectationState.CURRENT) {
            satisfiedCount = currentSatisfiedCount;
        } else {
            throw new IllegalArgumentException(
                    "Unknown expectation state " + state);
        }

        return satisfiedCount == expectations.size();
    }

//    // Javadoc inherited.
//    protected boolean verifyImpl(Report report) {
//        super.verifyImpl(report);
//
//        return verifyContainedExpectations(expectations, currentSatisfiedCount,
//                                           0, report);
//    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/4	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 10-May-04	4164/3	pduffin	VBM:2004050404 Fixed some problems with HTML orientation separator

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 06-Apr-04	3703/3	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
