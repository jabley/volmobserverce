/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock;

import com.volantis.testtools.mock.impl.AbstractSingleExpectation;
import com.volantis.testtools.mock.expectations.Report;

public class TestSimpleExpectation
        extends AbstractSingleExpectation {

    private final String expectedValue;

    public TestSimpleExpectation(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    protected boolean checkExpectation(Event event) {

        if (!(event instanceof TestEvent)) {
            return false;
        }

        TestEvent testEvent = (TestEvent) event;
        String actualValue = testEvent.getValue();
        if (expectedValue.equals(actualValue)) {
            return true;
        } else {
            return false;
        }
    }

    public void debug(Report report) {
        report.append("TestEvent: " + expectedValue);
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
