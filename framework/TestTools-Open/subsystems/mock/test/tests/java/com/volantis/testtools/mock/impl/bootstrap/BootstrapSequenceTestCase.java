/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.bootstrap;

import com.volantis.testtools.mock.EventMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.EventEffect;
import com.volantis.testtools.mock.expectations.Report;
import com.volantis.testtools.mock.impl.ExpectationSequence;
import com.volantis.testtools.mock.impl.ExpectationState;
import com.volantis.testtools.mock.impl.InternalExpectationMock;
import com.volantis.testtools.mock.impl.ReportImpl;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Tests the expectation sequence using the mock framework.
 */
public class BootstrapSequenceTestCase
        extends MockTestCaseAbstract {

    /**
     * Test that a sequence works correctly when an expectation is partially
     * satisfied and so cannot consume any more events but a following
     * expectation can consume the event.
     */
    public void testPartialSatisfaction() {


        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a sequence of shared expectations.
        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();

        final ExpectedValue EXPECTS_INSTANCE_OF_REPORT =
                mockFactory.expectsInstanceOf(Report.class);

        final InternalExpectationMock expectation1Mock =
                new InternalExpectationMock("expectation1Mock", expectations);

        final InternalExpectationMock expectation2Mock =
                new InternalExpectationMock("expectation2Mock", expectations);

        final EventMock eventMock =
                new EventMock("eventMock", expectations);

        Report report = new ReportImpl();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectation1Mock.expects.makeImmutable();
        expectation2Mock.expects.makeImmutable();

        // The first expectation cannot consume the event but would be
        // satisfied by it.
        expectation1Mock.fuzzy
                .checkExpectations(ExpectationState.CURRENT, eventMock,
                                   EXPECTS_INSTANCE_OF_REPORT)
                .returns(EventEffect.WOULD_SATISFY);

        // The second expectation would consume the event.
        expectation2Mock.fuzzy
                .checkExpectations(ExpectationState.CURRENT, eventMock,
                                   EXPECTS_INSTANCE_OF_REPORT)
                .returns(EventEffect.MATCHED_EXPECTATION);

        // The second expectation then expects to be asked to process the
        // event.
        //expectation2Mock.expects.processEvent(ExpectationState.CURRENT, eventMock, null);

        // After processing the second expectation will be asked to see
        // whether it is completely satisfied.
        expectation2Mock.expects
                .isCompletelySatisfied(ExpectationState.CURRENT)
                .returns(false);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ExpectationSequence sequence = new ExpectationSequence();
        sequence.add(expectation1Mock);
        sequence.add(expectation2Mock);

        sequence.checkExpectations(ExpectationState.CURRENT, eventMock, report);
        //sequence.processEvent(event2Mock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
