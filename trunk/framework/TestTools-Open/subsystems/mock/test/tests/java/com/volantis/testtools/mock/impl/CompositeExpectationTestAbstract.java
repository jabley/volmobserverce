/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.CompositeExpectation;
import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.TestSimpleExpectation;

public abstract class CompositeExpectationTestAbstract
        extends RepeatableExpectationTestAbstract {

    protected abstract CompositeExpectation createCompositeExpectation();

    protected Expectation createExpectation() {
        return createCompositeExpectation();
    }

    public void testImmutable() {
        CompositeExpectation expectation = createCompositeExpectation();
        expectation.addExpectation(new TestSimpleExpectation("A"));

        ((InternalExpectation) expectation).makeImmutable();

        try {
            expectation.addExpectation(new TestSimpleExpectation("B"));

            fail("Did not detect attempt to change immutable expectation");
        } catch (IllegalStateException e) {
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
