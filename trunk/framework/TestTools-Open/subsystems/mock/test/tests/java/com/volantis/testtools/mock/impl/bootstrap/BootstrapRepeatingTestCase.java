/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.bootstrap;

import com.volantis.testtools.mock.EventMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.expectations.Report;
import com.volantis.testtools.mock.EventEffect;
import com.volantis.testtools.mock.impl.ExpectationState;
import com.volantis.testtools.mock.impl.InternalExpectationMock;
import com.volantis.testtools.mock.impl.RepeatingExpectation;
import com.volantis.testtools.mock.impl.ReportImpl;
import com.volantis.testtools.mock.impl.SatisfactionLevel;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Tests the expectation sequence using the mock framework.
 */
public class BootstrapRepeatingTestCase
        extends MockTestCaseAbstract {

    /**
     * Test that a repeating expectation works correctly when it is partially
     * satisfied but a nested expectation cannot consume any more events.
     */
    public void testPartialSatisfaction() {


        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a sequence of shared expectations.
        ExpectationBuilder expects = mockFactory.createOrderedBuilder();

        final ExpectedValue EXPECTS_INSTANCE_OF_REPORT =
                mockFactory.expectsInstanceOf(Report.class);

        final InternalExpectationMock expectationMock =
                new InternalExpectationMock("expectationMock", expects);

//        final InternalExpectationMock expectation2Mock =
//                new InternalExpectationMock("expectation2Mock", expects);

        final EventMock eventMock =
                new EventMock("eventMock", expects);

        Report report = new ReportImpl();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectationMock.expects.makeImmutable();

        expectationMock.expects.checkExpectations(ExpectationState.CURRENT,
                                                  eventMock, report)
                .returns(EventEffect.MATCHED_EXPECTATION);

        expectationMock.expects.isCompletelySatisfied(ExpectationState.CURRENT)
                .returns(false);

        expectationMock.expects.checkExpectations(ExpectationState.CURRENT,
                                                  eventMock, report)
                .returns(EventEffect.WOULD_FAIL);

        expectationMock.fuzzy.checkSatisfactionLevel(EXPECTS_INSTANCE_OF_REPORT)
                .returns(SatisfactionLevel.PARTIAL);

        expectationMock.expects.checkExpectations(ExpectationState.INITIAL,
                                                  eventMock, report)
                .returns(EventEffect.WOULD_FAIL);

//        expectation1Mock.expects.makeImmutable();
//        expectation2Mock.expects.makeImmutable();

        // The first expectation cannot consume the event but would be
        // satisfied by it.
//        expectation1Mock.expects
//                .checkExpectations(ExpectationState.CURRENT, eventMock,
//                                   EXPECTS_INSTANCE_OF_REPORT)
//                .returns(EventEffect.WOULD_SATISFY);

        // The second expectation would consume the event.
//        expectation2Mock.expects
//                .checkExpectations(ExpectationState.CURRENT, eventMock,
//                                   EXPECTS_INSTANCE_OF_REPORT)
//                .returns(EventEffect.MATCHED_EXPECTATION);

        // The second expectation then expects to be asked to process the
        // event.
        //expectation2Mock.expects.processEvent(ExpectationState.CURRENT, eventMock, null);

        // After processing the second expectation will be asked to see
        // whether it is completely satisfied.
//        expectation2Mock.expects
//                .isCompletelySatisfied(ExpectationState.CURRENT)
//                .returns(false);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        RepeatingExpectation expectation = new RepeatingExpectation(
                1, Integer.MAX_VALUE, expectationMock);
        EventEffect effect;

        // Try one, this should cause the repeating expectation to be partially
        // satisfied.
        effect = expectation.checkExpectations(ExpectationState.CURRENT,
                                               eventMock, report);
        assertEquals(EventEffect.MATCHED_EXPECTATION, effect);

        // Try it again but this time the nested expectation fails.
        effect = expectation.checkExpectations(ExpectationState.CURRENT,
                                               eventMock, report);
        assertEquals(EventEffect.WOULD_SATISFY, effect);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
