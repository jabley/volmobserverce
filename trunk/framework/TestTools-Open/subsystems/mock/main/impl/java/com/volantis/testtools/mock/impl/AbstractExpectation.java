/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import junit.framework.Assert;

import com.volantis.testtools.mock.EventEffect;
import com.volantis.testtools.mock.Event;
import com.volantis.testtools.mock.expectations.Report;
import com.volantis.testtools.mock.method.MethodCall;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Base class for all the expectations.
 */
public abstract class AbstractExpectation
        implements InternalExpectation {

    /**
     * If true then this expectation is immutable.
     */
    private boolean immutable;

    /**
     * Indicates whether this expectation is completely satisfied.
     *
     * <p>True means that it is and false means that we do not know.
     */
    private boolean currentCompletelySatisfied;

    /**
     * Checks to see whether the event matches an expectation.
     *
     * <p>Checks to see whether the event matches an expectation. If it did
     * not, then it throws an exception describing what went wrong.</p>
     *
     * @param event The event to process.
     */
    protected void checkExpectations(MethodCall event) {

        // Assess the effect of the event.
        StringWriter out = new StringWriter();
        Report report = new ReportImpl(out);

        EventEffect effect = checkExpectations(ExpectationState.CURRENT, event, report);
        if (effect == EventEffect.WOULD_FAIL
                || effect == EventEffect.WOULD_SATISFY) {

            String reason = out.getBuffer().toString();
            StringBuffer message = new StringBuffer();
            message.append("Event ").append(event).append(" was not expected");

            // The following does nothing at the moment as the buffer does not
            // actually get anything put into it.
            if (reason.length() > 0) {
                message.append("\nExpected - ").append(reason);

                StringWriter writer = new StringWriter();
                PrintWriter printer = new PrintWriter(writer);
                new Throwable().printStackTrace(printer);

                message.append("\nAt\n");
                message.append(writer.getBuffer().toString());
            }
            Assert.fail(this + " - " + message.toString());
        }
    }

    // Javadoc inherited.
    public final EventEffect checkExpectations(
            ExpectationState state, Event event, Report report) {

        if (state == null) {
            throw new IllegalArgumentException("state cannot be null");
        }
        if (event == null) {
            throw new IllegalArgumentException("event cannot be null");
        }
        if (report == null) {
            throw new IllegalArgumentException("report cannot be null");
        }

        // If the expectation is completely satisfied in this state then the
        // event would cause it to fail.
        if (isCompletelySatisfied(state)) {
            return EventEffect.WOULD_FAIL;
        }

        // Invoke the expectation type specific implementation.
        return checkExpectationsImpl(state, event, report);
    }

    /**
     * Checks to see whether the event matches an expectation.
     *
     * @param state The state of the expectation when checking the event.
     * @param event The event to check.
     * @param report The report to describe the
     * @return an EventEffect
     */
    protected abstract EventEffect checkExpectationsImpl(
            ExpectationState state, Event event, Report report);

    // Javadoc inherited.
    public final boolean isCompletelySatisfied(ExpectationState state) {
        // If this has already been completely satisfied in the current state
        // then return immediately.
        if (state == ExpectationState.CURRENT) {

            // If this has already been completely satisfied then return
            // immediately.
            if (currentCompletelySatisfied) {
                return true;
            }

            // Check with the derived class, if it is completely satisfied and this
            // expectation is immutable then record it. If this expectation is not
            // immutable then additional expectations could be added that would
            // change its satisfaction.
            boolean completelySatisfied = isCompletelySatisfiedImpl(state);
            if (completelySatisfied && immutable) {
                currentCompletelySatisfied = completelySatisfied;
            }

            return completelySatisfied;

        } else {
            return isCompletelySatisfiedImpl(state);
        }
    }

    /**
     * Check to see whether this has been completely satisfied.
     * @param state The state within which the check should be made.
     * @return True if it is completely satisfied and false otherwise.
     */
    protected boolean isCompletelySatisfiedImpl(ExpectationState state) {
        return false;
    }

    /**
     * This expectation is completely satisfied in the current state.
     */
    protected void markAsCompletelySatisfied() {
        currentCompletelySatisfied = true;
    }

    // Javadoc inherited.
    public boolean isSatisfied(Report report) {
        SatisfactionLevel satisfaction = checkSatisfactionLevel(report);
        return (satisfaction != SatisfactionLevel.UNSATISFIED);
    }

    // Javadoc inherited.
    public SatisfactionLevel checkSatisfactionLevel(Report report) {
        if (isCompletelySatisfied(ExpectationState.CURRENT)) {
            return SatisfactionLevel.COMPLETE;
        }

        EventEffect eventEffect = checkExpectations(
                ExpectationState.CURRENT, VerifyEvent.INSTANCE, report);
        return (eventEffect == EventEffect.WOULD_SATISFY)
                ? SatisfactionLevel.PARTIAL : SatisfactionLevel.UNSATISFIED;
    }

    // Javadoc inherited.
    public void makeImmutable() {
        this.immutable = true;
    }

    /**
     * Ensures that this expectation is mutable.
     *
     * <p>If this expectation is not mutable then it will throw an
     * {@link IllegalStateException}.</p>
     *
     * @throws IllegalStateException If it is not mutable.
     */
    protected void ensureMutable() {
        if (immutable) {
            throw new IllegalStateException(
                    "Expectation " + this + " is immutable");
        }
    }

    // Javadoc inherited.
    public void reset() {
        currentCompletelySatisfied = false;
    }

    // Javadoc inherited.
    public final void verify() {
        if (isCompletelySatisfied(ExpectationState.CURRENT)) {
            return;
        }

        StringWriter writer = new StringWriter(100);
        Report report = new ReportImpl(writer);
        report.append("Verify failed, a number of expectations remain" +
                      " unsatisfied\n");
        report.beginBlock();
        boolean satisfied = verifyImpl(report);
        report.endBlock();

        if (!satisfied) {
            Assert.fail(writer.getBuffer().toString());
        }
    }

    /**
     * Verify that the expectations have been satisfied.
     *
     * @param report The report that should be updated with information about
     * any unsatisfied expectations.
     *
     * @return True if all the expectations have been satisfied, false
     * otherwise.
     */
    protected boolean verifyImpl(Report report) {
        return isSatisfied(report);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/2	pduffin	VBM:2004051207 Integrated separators into menu rendering

 10-May-04	4164/3	pduffin	VBM:2004050404 Fixed some problems with HTML orientation separator

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
