/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;


import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.expectations.Report;

/**
 * Base class for all those composite expectations that can only contain one
 * expectation.
 */
public abstract class AbstractSingleCompositeExpectation
        extends AbstractCompositeExpectationContainer {

    /**
     * The expectation contained.
     */
    private InternalExpectation expectation;

    // Javadoc inherited.
    public void addExpectation(Expectation expectation) {
        if (this.expectation != null) {
            throw new IllegalStateException("Can only contain one expectation");
        }

        // Add the expectation.
        this.expectation = (InternalExpectation) expectation;

        // Mark the added expectation as immutable.
        this.expectation.makeImmutable();
    }

    // Javadoc inherited.
    protected InternalExpectation getExpectation() {
        return expectation;
    }

    /**
     * Reset all the expectations apart from the specified one.
     *
     * @param alreadyReset The expectation that has already been reset.
     */
    protected void resetAllApartFrom(Expectation alreadyReset) {
        if (expectation != alreadyReset) {
            expectation.reset();
        }
    }

    // Javadoc inherited.
    public void reset() {
        super.reset();

        // Reset the nested expectation.
        expectation.reset();
    }

    // Javadoc inherited.
    public void debug(Report report) {
        before(report);
        expectation.debug(report);
        after(report);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/4	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 13-May-05	8208/1	pduffin	VBM:2005051208 Committing again after fixing issue with build

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
