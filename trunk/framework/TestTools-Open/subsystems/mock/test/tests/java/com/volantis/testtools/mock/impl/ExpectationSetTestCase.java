/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.CompositeExpectation;
import com.volantis.testtools.mock.EventEffect;
import com.volantis.testtools.mock.TestEvent;
import com.volantis.testtools.mock.TestSimpleExpectation;
import junit.framework.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;

public class ExpectationSetTestCase
        extends CompositeExpectationTestAbstract {

    private ExpectationSet set;

    protected void setUp() throws Exception {
        super.setUp();

        set = new ExpectationSet();
        set.addExpectation(new TestSimpleExpectation("A"));
        set.addExpectation(new TestSimpleExpectation("B"));
        set.addExpectation(new TestSimpleExpectation("C"));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected CompositeExpectation createCompositeExpectation() {
        return set;
    }

    protected List createSatisfyingEvents() {
        List list = new ArrayList();
        list.add(new TestEvent("A"));
        list.add(new TestEvent("B"));
        list.add(new TestEvent("C"));
        return list;
    }

    public void testCompleteSet() {
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("B"), report);
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("C"), report);

        set.verify();
    }

    public void testPartialSet() {
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("B"), report);

        try {
            set.verify();
            fail("Unsatisfied expectations not detected");
        } catch (AssertionFailedError e) {
            // Worked
        }
    }

    public void testDifferentOrderSet() {
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("B"), report);
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("C"), report);

        set.verify();
    }

    public void testInvalidSet() {
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);

        try {
            set.checkExpectations(ExpectationState.CURRENT, new TestEvent("D"), report);
            fail("Mismatching event not detected");
        } catch (AssertionFailedError e) {
            // Worked
        }
    }

    public void testAssessInitialState() {
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("B"), report);
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);
        set.checkExpectations(ExpectationState.CURRENT, new TestEvent("C"), report);

        EventEffect effect
                = set.checkExpectations(ExpectationState.INITIAL,
                                   new TestEvent("A"), report);
        assertSame("Unexpected event effect assessment",
                   EventEffect.MATCHED_EXPECTATION, effect);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
