/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.EventEffect;
import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.TestEvent;
import com.volantis.testtools.mock.TestSimpleExpectation;
import junit.framework.AssertionFailedError;

public class RepeatingExpectationTestCase
        extends ExpectationTestAbstract {

/*
    protected Expectation createExpectation() {
        return new RepeatingExpectation(0, 1, new TestSimpleExpectation("A"));
    }

    protected List createSatisfyingEvents() {
        List list = new ArrayList();
        list.add(new TestEvent("A"));
        return list;
    }
*/

    public void testOptional() {

        Expectation nested = new TestSimpleExpectation("A");
        RepeatingExpectation repeating = new RepeatingExpectation(0, 1, nested);

        // It should be partially satisfied without any events.
        repeating.verify();

        // It should be able to process the single event.
        repeating.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);

        // It should be completely satisfied now.
        assertTrue("Not completely satisfied",
                   repeating.isCompletelySatisfied(ExpectationState.CURRENT));
    }

    public void testOptionalPartialSatisfaction() {

        Expectation nested = new TestSimpleExpectation("A");
        RepeatingExpectation repeating = new RepeatingExpectation(0, 1, nested);

        // It should be partially satisfied without any events.
        repeating.verify();

        // See what effect processing the event will have.
        EventEffect effect = repeating.checkExpectations(
                ExpectationState.CURRENT, new TestEvent("B"), report);
        assertSame("Event does not satisfy expectation",
                   EventEffect.WOULD_SATISFY, effect);
    }

    public void testAtLeastOne() {

        Expectation nested = new TestSimpleExpectation("A");
        RepeatingExpectation repeating
                = new RepeatingExpectation(1, Integer.MAX_VALUE, nested);

        try {
            // It should not be partially satisfied without any events.
            repeating.verify();

            fail("Should not be verifiable");
        } catch (AssertionFailedError e) {
            // Worked
        }

        // Try and process a number of events.
        for (int i = 0; i < 5; i += 1) {
            // It should be able to process the single event.
            repeating.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);

            // Now it should be partially satisfied.
            repeating.verify();
        }

        // It should not be completely satisfied now.
        assertFalse("Completely satisfied",
                    repeating.isCompletelySatisfied(ExpectationState.CURRENT));
    }

    public void testFixedNumber() {

        Expectation nested = new TestSimpleExpectation("A");
        RepeatingExpectation repeating
                = new RepeatingExpectation(3, 3, nested);

        try {
            // It should not be partially satisfied without any events.
            repeating.verify();

            fail("Should not be verifiable");
        } catch (AssertionFailedError e) {
            // Worked
        }

        // Try and process the correct number of events.
        for (int i = 0; i < 3; i += 1) {
            // It should be able to process the single event.
            repeating.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);
        }

        // It should be completely satisfied now.
        assertTrue("Not completely satisfied",
                   repeating.isCompletelySatisfied(ExpectationState.CURRENT));
    }

    /**
     * Test that a repeating expectation can be partially satisfied.
     */
    public void testPartialSatisfaction() {

        Expectation nested = new TestSimpleExpectation("A");
        RepeatingExpectation repeating
                = new RepeatingExpectation(1, 2, nested);

        // Process and event to move it into a partially satisfied state.
        repeating.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);

        // Make sure that it is only partially satisfied.
        repeating.verify();
        assertFalse("Completely satisfied", repeating.isCompletelySatisfied(ExpectationState.CURRENT));

        // The unhandled event should cause the repeating expectation to be
        // satisfied but not consume the event.
        EventEffect effect = repeating.checkExpectations(
                ExpectationState.CURRENT, new TestEvent("B"), report);
        assertSame("Event does not satisfy expectation",
                   EventEffect.WOULD_SATISFY, effect);
    }

    /**
     * Test that a repeating expectation that is not partially satisfied fails
     * properly when given an event it cannot consume.
     */
    public void testPartialDissatisfaction() {

        Expectation nested = new TestSimpleExpectation("A");
        RepeatingExpectation repeating
                = new RepeatingExpectation(2, 3, nested);

        // Process and event to move it towards a partially satisfied state.
        repeating.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);

        // Make sure that it is not partially satisfied.
        assertFalse("Partially satisfied", repeating.isSatisfied(report));

        // The unhandled event should cause the repeating expectation to fail.
        EventEffect effect = repeating.checkExpectations(
                ExpectationState.CURRENT, new TestEvent("B"), report);

        assertSame("Event does not fail expectation",
                   EventEffect.WOULD_FAIL, effect);
    }

    /**
     * Tests what happens when a repeating expectation contains an optional
     * expectation and the event cannot be processed by that expectation.
     */
    public void testNestedOptionalPartialSatisfaction() {

        // Create an optional expectation.
        Expectation nested = new RepeatingExpectation(
                0, 1, new TestSimpleExpectation("A"));
        RepeatingExpectation repeating
                = new RepeatingExpectation(1, 2, nested);

        // Process and event to move it into a partially satisfied state.
        repeating.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);

        // Make sure that it is only partially satisfied.
        repeating.verify();
        assertFalse("Completely satisfied", repeating.isCompletelySatisfied(ExpectationState.CURRENT));

        // The unhandled event should cause the repeating expectation to be
        // satisfied but not consume the event.
        EventEffect effect = repeating.checkExpectations(
                ExpectationState.CURRENT, new TestEvent("B"), report);
        assertSame("Event does not satisfy expectation",
                   EventEffect.WOULD_SATISFY, effect);
    }

    /**
     * Test that a repeating expectation that contains an optional expectation
     * is always satisfied if the nested one is satisfied.
     */
    public void testNestedOptionalPartialAlwaysSatisfied() {

        // Create an optional expectation.
        Expectation nested = new RepeatingExpectation(
                0, 1, new TestSimpleExpectation("A"));
        RepeatingExpectation repeating
                = new RepeatingExpectation(20, 300, nested);

        // Process and event to move it towards a partially satisfied state.
        repeating.checkExpectations(ExpectationState.CURRENT, new TestEvent("A"), report);

        // Make sure that it is not partially satisfied.
        try {
            repeating.verify();
            fail("Partially satisfied");
        } catch (AssertionFailedError e) {
            // Worked.
        }

        // The unhandled event should cause the repeating expectation to be
        // satisfied but not consume the event.
        EventEffect effect = repeating.checkExpectations(
                ExpectationState.CURRENT, new TestEvent("B"), report);
        assertSame("Event does not satisfy expectation",
                   EventEffect.WOULD_SATISFY, effect);
    }

    /**
     * Tests what happens when a repeating expectation contains an optional
     * expectation and the event cannot be processed by that expectation.
     */
    public void testNestedOptionalPartialUnsatisfaction() {

        // Create an optional expectation.
        Expectation nested = new RepeatingExpectation(
                0, 1, new TestSimpleExpectation("A"));
        RepeatingExpectation repeating
                = new RepeatingExpectation(1, 2, nested);

        // Make sure that it is not partially satisfied.
        try {
            repeating.verify();
            fail("Partially satisfied");
        } catch (AssertionFailedError e) {
            // Worked.
        }

        // The unhandled event should cause the repeating expectation to be
        // satisfied but not consume the event.
        EventEffect effect = repeating.checkExpectations(
                ExpectationState.CURRENT, new TestEvent("B"), report);
        assertSame("Event does not satisfy expectation",
                   EventEffect.WOULD_SATISFY, effect);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
