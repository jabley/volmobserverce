/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.expectations.Report;
import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.Event;
import com.volantis.testtools.mock.EventEffect;

/**
 * An expectation of some interaction that will be performed on a mock object.
 *
 * <p>Every expectation has two states associated with it, its initial and its
 * current state. The initial state is the state that it is in when it is first
 * created, or immediately after the {@link #reset} method is called. The
 * current state is the normal state that it is in when it is processing
 * events.</p>
 *
 * <p>The initial state (and the {@link #reset} method) is only used when an
 * expectation is repeated, at all other times it has no effect. For an
 * expectation to be repeatable it needs to provide a way for the effect of an
 * event to be assessed in both the current and initial states and for the
 * state to be reset back to the initial state.</p>
 *
 * <p>As an example lets assume that a sequence (S) of two events A and B is
 * expected to be repeated (R) one or two times, where S is an expectation
 * sequence wrapped inside a repeating expectation. Initially both S and R are
 * unsatisfied, but after receiving events A and B the first time S is now
 * completely satisfied and R is partially satisfied.</p>
 *
 * <p>When R receives an additional event it will query S and find that it
 * will fail if it processes the event so it will then check to see what the
 * effect would be on S to process the event when it is in its initial state.
 * If the event is A then S will be able to consume it so R will reset S back
 * to its initial state and ask it to process the event. If any other event was
 * received by R then it would mark itself as satisfied and let some other
 * expectation process the event. It can do this because S has been satisfied
 * once which matches R's expected range.</p>
 *
 * @mock.generate
 */
public interface InternalExpectation
        extends Expectation {

    /**
     * Process the event.
     *
     * <dl>
     *   <dt>{@link com.volantis.testtools.mock.EventEffect#WOULD_FAIL}</dt>
     *   <dd>
     *     <p>This indicates that the expectation failed to process the event.
     * </p>
     *   </dd>
     *
     *   <dt>{@link com.volantis.testtools.mock.EventEffect#MATCHED_EXPECTATION}</dt>
     *   <dd>
     *     <p>This indicates that the expectation matched the event and
     * updated its state. This may result in the expectation's satisfaction
     * level improving.</p>
     *   </dd>
     *
     *   <dt>{@link com.volantis.testtools.mock.EventEffect#WOULD_SATISFY}</dt>
     *   <dd>
     *     <p>This indicates that this expectation would be satisfied by the
     * event but would not consume it.</p>
     *   </dd>
     * </dl>
     *
     * @param event The event to process.
     */
    EventEffect checkExpectations(ExpectationState state, Event event,
                                  Report report);

    /**
     * Makes the expectation immutable.
     *
     * <p>Once this method has been called it is no longer possible to change
     * an expectation, e.g. by adding another expectation to a composite. Any
     * attempt to do so will result in an exception being thrown.</p>
     */
    void makeImmutable();

    /**
     * Checks to see whether this expectation has been completely satisfied.
     *
     * <p>An expectation is completely satisfied if it is no longer capable of
     * consuming events. In that case attempting to make it process an event
     * will always result in failure.</p>
     *
     * @return True if it has and false otherwise.
     */
    boolean isCompletelySatisfied(ExpectationState state);

    /**
     * Checks to see whether this expectation has been satisfied.
     *
     * <p>An expectation is satisfied if it is either completely satisfied in
     * the current state, or it returns {@link com.volantis.testtools.mock.EventEffect#WOULD_SATISFY}
     * for an event that does not match any of its partially satisfied
     * expectations.</p>
     *
     * @return True if it has and false otherwise.
     */
    boolean isSatisfied(Report report);

    SatisfactionLevel checkSatisfactionLevel(Report report);

    /**
     * Reset the expectation back to its initial state.
     */
    void reset();

    /**
     * Verify that this expectation has been satisfied, either completely or
     * partially.
     */
    void verify();

    void debug(Report report);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
