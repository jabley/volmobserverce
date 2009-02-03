/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.Event;
import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.method.MethodIdentifier;
import com.volantis.testtools.mock.method.ExpectedArguments;
import com.volantis.testtools.mock.expectations.Report;
import com.volantis.testtools.mock.impl.method.MethodInvocationEvent;
import com.volantis.testtools.mock.impl.method.ExpectedMethodInvocationImpl;
import com.volantis.testtools.mock.example.FooMock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ReportVerbosityTestCase extends RepeatableExpectationTestAbstract {

    private FooMock fooMock;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        fooMock = new FooMock("fooMock");
    }

    // javadoc inherited
    protected Expectation createExpectation() {
        Object[] arguments = new Object[]{
                "input"
        };

        MethodIdentifier identifier =
                FooMock._getMethodIdentifier("setBar(java.lang.String)");
        return new ExpectedMethodInvocationImpl(fooMock, identifier,
                new ExpectedArguments(identifier, arguments, true), null);
    }

    // javadoc inherited
    protected List createSatisfyingEvents() {
        List list = new ArrayList();
        Object[] arguments = new Object[]{
                "input"
        };
        MethodIdentifier identifier =
                FooMock._getMethodIdentifier("setBar(java.lang.String)");
        list.add(new MethodInvocationEvent(fooMock, identifier, arguments));
        return list;
    }

    public void testVerifiedExpecationReturnsNoReportContent()  throws Exception {
        Expectation expectation = createExpectation();
        List events = createSatisfyingEvents();

        ReportingRepeatingExpectation repeating =
                new ReportingRepeatingExpectation(1, 3);

        repeating.addExpectation(expectation);

        // Iterate over the events and target them at the repeating method.
        for (Iterator iterator = events.iterator(); iterator.hasNext();) {
            Event event = (Event) iterator.next();
            repeating.checkExpectations(ExpectationState.CURRENT, event,
                    NullReport.INSTANCE);
        }

        repeating.doVerify(report);

        assertEquals("", out.toString());
    }

    public void testUnverifiedExpectationReturnsReportContent()  throws Exception {
        Expectation expectation = createExpectation();

        ReportingRepeatingExpectation repeating =
                new ReportingRepeatingExpectation(1, 3);
        repeating.addExpectation(expectation);

        repeating.doVerify(report);

        assertEquals("Between 1 and 3 of:\n" +
                "    Call to {fooMock}.Foo#setBar(input)\n" +
                "[failed verification]\n", out.toString());
    }

    static class ReportingRepeatingExpectation extends RepeatingExpectation {

        public ReportingRepeatingExpectation(int min, int max) {
            super(min, max);
        }

        public boolean doVerify(Report report) {
            return verifyImpl(report);
        }
    }
}
