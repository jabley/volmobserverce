/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;


import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.expectations.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all composite expectations.
 */
public abstract class AbstractCompositeExpectation
        extends AbstractCompositeExpectationContainer {

    protected final String description;

    /**
     * The list of expectations.
     */
    protected List expectations;

    /**
     * Initialise.
     */
    public AbstractCompositeExpectation() {
        this(null);
    }

    public AbstractCompositeExpectation(String description) {
        this.description = description;
        expectations = new ArrayList();
    }

    // Javadoc inherited.
    public void addExpectation(Expectation expectation) {

        // Make sure this is mutable before adding the expectation.
        ensureMutable();

        // Add the expectation.
        expectations.add(expectation);

        // Mark the added expectation as immutable.
        ((InternalExpectation) expectation).makeImmutable();
    }

    /**
     * Reset the state of this and all nested expectations.
     *
     * @param alreadyReset The expectation that has already been reset as a
     * result of processing an event in the initial state. This will be
     * null if all nested expectations should be reset.
     */
    protected void resetAllApartFrom(Expectation alreadyReset) {
        int size = expectations.size();
        for (int i = 0; i < size; i += 1) {
            InternalExpectation expectation =
                    (InternalExpectation) expectations.get(i);
            if (expectation != alreadyReset) {
                expectation.reset();
            }
        }
    }

    // Javadoc inherited.
    public void reset() {
        super.reset();

        resetAllApartFrom(null);
    }

    // Javadoc inherited.
    public void debug(Report report) {
        before(report);
        int size = expectations.size();
        for (int i = 0; i < size; i += 1) {
            Expectation expectation = (Expectation) expectations.get(i);
            ((AbstractExpectation) expectation).debug(report);
        }
        after(report);
    }

//    /**
//     * Verifies that the nested expectations are satisfied.
//     *
//     * @param expectations The list of expectations to check.
//     * @param alreadySatisfied The number of expectations that have already
//     * been satisfied.
//     * @param start The index of the first expectation to check.
//     */
//    protected boolean verifyContainedExpectations(
//            List expectations, int alreadySatisfied, int start, Report report) {
//
//        // If there are any expectations left unsatisfied then check to see
//        // whether they are actually required or not.
//        int size = expectations.size();
//        int remaining = size - alreadySatisfied;
//        if (remaining == 0) {
//            return true;
//        }
//
//        //List unsatisfied = null;
//        int unsatisfied = 0;
//        report.beginBlock();
//        for (int i = start; i < size; i += 1) {
//            InternalExpectation expectation =
//                    (InternalExpectation) expectations.get(i);
//
//            if (!expectation.isSatisfied(report)) {
//                unsatisfied += 1;
//            }
//        }
//        report.endBlock();
//
//        return unsatisfied == 0;
//    }

    // Javadoc inherited.
    public String toString() {
        if (description == null) {
            return super.toString();
        } else {
            return description;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/4	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/2	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
