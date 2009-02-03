/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import junit.framework.AssertionFailedError;

import com.volantis.testtools.mock.CompositeExpectation;
import com.volantis.testtools.mock.TestEvent;
import com.volantis.testtools.mock.TestSimpleExpectation;

import java.util.ArrayList;
import java.util.List;

public class ExpectationSequenceTestCase
        extends CompositeExpectationTestAbstract {

    private ExpectationSequence sequence;

    protected void setUp() throws Exception {
        super.setUp();

        sequence = new ExpectationSequence();
        sequence.addExpectation(new TestSimpleExpectation("A"));
        sequence.addExpectation(new TestSimpleExpectation("B"));
        sequence.addExpectation(new TestSimpleExpectation("C"));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected CompositeExpectation createCompositeExpectation() {
        return sequence;
    }

    protected List createSatisfyingEvents() {
        List list = new ArrayList();
        list.add(new TestEvent("A"));
        list.add(new TestEvent("B"));
        list.add(new TestEvent("C"));
        return list;
    }

    public void testCompleteSequence() {
        sequence.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);
        sequence.checkExpectations(ExpectationState.CURRENT, new TestEvent("B"), report);
        sequence.checkExpectations(ExpectationState.CURRENT, new TestEvent("C"), report);

        sequence.verify();
    }

    public void testPartialSequence() {
        sequence.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);
        sequence.checkExpectations(ExpectationState.CURRENT, new TestEvent("B"), report);

        try {
            sequence.verify();
            fail("Unsatisfied expectations not detected");
        } catch (AssertionFailedError e) {
            // Worked
        }
    }

    public void testInvalidSequence() {
        sequence.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);

        try {
            sequence.checkExpectations(ExpectationState.CURRENT, new TestEvent("C"), report);
            fail("Mismatching event not detected");
        } catch (AssertionFailedError e) {
            // Worked
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
