/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import junit.framework.Assert;

import com.volantis.testtools.mock.EventEffect;
import com.volantis.testtools.mock.Event;
import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.impl.method.OccurrencesImpl;
import com.volantis.testtools.mock.method.Occurrences;
import com.volantis.testtools.mock.expectations.Report;

/**
 * An expectation that repeats another expectation.
 */
public class RepeatingExpectation
        extends AbstractSingleCompositeExpectation {

    /**
     * The number of times that the nested expectation has been satisfied.
     */
    private int currentCount;

    /**
     * The number of events that have been processed by this expectation.
     */
    private int currentEventsProcessed;

    private OccurrencesImpl occurences;

    /**
     * Initialise an instance of this.
     *
     * @param minimum The minimum number of times that the contained
     *                expectation must be satisfied, must be 0 or more and less than or equal
     *                to the maximum.
     * @param maximum The maximum number of times that the contained
     *                expectation must be satisfied, must be 0 or more and greater than or
     *                equal to the minimum.
     */
    public RepeatingExpectation(int minimum, int maximum,
                                Expectation expectation) {
        this(minimum, maximum);

        addExpectation(expectation);
    }

    public RepeatingExpectation(int minimum, int maximum) {
        this.occurences = new OccurrencesImpl(minimum, maximum);
    }

    public RepeatingExpectation() {
        this.occurences = new OccurrencesImpl();
    }

    public Occurrences getOccurences() {
        return occurences;
    }

    public void reset() {
        super.reset();

        currentCount = 0;
        currentEventsProcessed = 0;
    }

    /**
     * This is opaque if the minimum and maximum are not both 1.
     */
    protected boolean opaqueContainer(Expectation container) {
        return getMinimum() != 1 || getMaximum() != 1;
    }

    protected EventEffect checkNestedExpectations(
            ExpectationState state, Event event, Report report) {

        int eventsProcessed;
        int count;
        if (state == ExpectationState.INITIAL) {
            eventsProcessed = 0;
            count = 0;
        } else if (state == ExpectationState.CURRENT) {
            eventsProcessed = currentEventsProcessed;
            count = currentCount;
        } else {
            throw new IllegalArgumentException("Unknown state " + state);
        }

        // Get the nested expectation.
        InternalExpectation expectation = getExpectation();

        // The effect that the event will have.
        EventEffect effect;

        // Assess the effect on the nested expectation.
        EventEffect nestedEffect = expectation.checkExpectations(
                state, event, report);

        if (nestedEffect == EventEffect.WOULD_FAIL) {
            SatisfactionLevel satisfactionLevel;
            if (state == ExpectationState.CURRENT
                    && (satisfactionLevel = expectation.checkSatisfactionLevel(
                            NullReport.INSTANCE)) != SatisfactionLevel.UNSATISFIED) {

                // Check to see whether we should do another iteration. If the
                // nested expectation is partially satisfied and this
                // event cannot be processed then

                //  As the
                // nested expectation is satisfied then we could have completed
                // one more iteration than we think so increment the count.
                effect = checkNextIteration(event, count, report, satisfactionLevel);

            } else if (eventsProcessed == 0 && getMinimum() == 0) {
                // The nested expectations have not matched any events and is
                // optional so will be satisfied by this event.
                Assert.assertEquals("Count not zero", 0, count);
                effect = EventEffect.WOULD_SATISFY;
            } else {
                effect = EventEffect.WOULD_FAIL;
            }
        } else if (nestedEffect == EventEffect.MATCHED_EXPECTATION) {

            // Carry on.
            effect = nestedEffect;

        } else if (nestedEffect == EventEffect.WOULD_SATISFY) {
            // This should probably behave just as in the situation when the
            // nested expectation fails because it is completely satisfied.
            //throw new UnsupportedOperationException("5");

            // Check to see whether we should do another iteration.
            effect = checkNextIteration(event, count, report,
                                        SatisfactionLevel.PARTIAL);

        } else {
            throw new IllegalArgumentException("Unknown event effect "
                    + nestedEffect);
        }

        if (effect == EventEffect.MATCHED_EXPECTATION) {

            // If this has been called in the initial state then reset the
            // state.
            if (state == ExpectationState.INITIAL) {
                resetAllApartFrom(expectation);
            }

            currentEventsProcessed += 1;

            if (expectation.isCompletelySatisfied(ExpectationState.CURRENT)) {
                currentCount += 1;
            }
        }

        return effect;
    }

    protected void before(Report report) {
        int min = getMinimum();
        int max = getMaximum();

        String occurences;
        if (min == 0 && max == 1) {
            occurences = "At most one of";
        } else if (min == 0 && max == Integer.MAX_VALUE) {
            occurences = "Any number of";
        } else if (min == 1 && max == Integer.MAX_VALUE) {
            occurences = "At least one of";
        } else if (min == 1 && max == 1) {
            occurences = "One of";
        } else {
            occurences = "Between " + min + " and " + max + " of";
        }

        report.append(occurences + ":");
        report.getPrintWriter().println();
        report.beginBlock();
    }

    protected void after(Report report) {
        report.endBlock();
    }

    /**
     * Checks the effect of the event on the nested expectation when it is in
     * its initial state in order to determine whether it is appropriate to
     * start another loop.
     *
     * @param satisfactionLevel
     * @param event The event to assess.
     * @return The assessment.
     */
    private EventEffect checkNextIteration(
            Event event, int count, Report report,
            SatisfactionLevel satisfactionLevel) {

        // Get the nested expectation.
        InternalExpectation expectation = getExpectation();

        EventEffect effect;

        // The expectation fails because it is completely satisfied,
        // and this is not being done in the initial state. As we know
        // that this expectation is not completely satisfied (otherwise
        // the code should not have reached here) we should see whether
        // we should start another iteration in the loop by checking
        // the effect of the event in the initial state.
        EventEffect initialEffect = expectation.checkExpectations(
                ExpectationState.INITIAL, event, report);

        if (initialEffect == EventEffect.WOULD_FAIL) {
            // The nested expectation would fail if it processed the event.

            if (satisfactionLevel == SatisfactionLevel.PARTIAL) {
                // The nested expectation is partially satisfied and the next
                // iteration cannot process this event so we must treat the
                // nested expectation as if it was completely satisfied. This
                // means that the number of iterations that have been completed
                // needs increasing by one.
                count += 1;
            }
            
            // The event will cause the nested one to fail in the
            // initial state so we need to check whether the event will
            // partially satisfy this.
            if (count >= getMinimum()) {
                effect = EventEffect.WOULD_SATISFY;
            } else {
                effect = EventEffect.WOULD_FAIL;
            }

        } else if (initialEffect == EventEffect.MATCHED_EXPECTATION) {

            effect = initialEffect;

        } else if (initialEffect == EventEffect.WOULD_SATISFY) {

            // The event satisfies the nested on in its initial state
            // so it would satisfy it again and again for as many times
            // as necessary to satisfy this expectation.
            effect = EventEffect.WOULD_SATISFY;

        } else {
            throw new IllegalArgumentException("Unknown event effect "
                    + initialEffect);
        }
        return effect;
    }

    /**
     * A repeated count is only satisfied if has processed the expectation
     * the specified number of times.
     */
    protected boolean isCompletelySatisfiedImpl(ExpectationState state) {
        int count;
        if (state == ExpectationState.INITIAL) {
            count = 0;
        } else if (state == ExpectationState.CURRENT) {
            count = currentCount;
        } else {
            throw new IllegalArgumentException("Unknown expectation state " + state);
        }

        return count == occurences.getMaximum();
    }

    private int getMinimum() {
        return occurences.getMinimum();
    }

    private int getMaximum() {
        return occurences.getMaximum();
    }

    public String toString() {
        return String.valueOf(getExpectation()) + " {" + getMinimum() + ", "
                + getMaximum() + "}";
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

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
