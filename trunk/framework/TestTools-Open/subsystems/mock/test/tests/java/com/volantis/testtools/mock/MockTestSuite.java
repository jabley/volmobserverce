/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock;

import junit.framework.TestSuite;

import com.volantis.testtools.mock.impl.ExpectationSequenceTestCase;
import com.volantis.testtools.mock.impl.ExpectationSetTestCase;
import com.volantis.testtools.mock.impl.ExpectedMethodInvocationTestCase;
import com.volantis.testtools.mock.impl.RepeatingExpectationTestCase;

public class MockTestSuite
        extends TestSuite {

    public MockTestSuite(String name) {
        super(name);

        addTestSuite(ExpectationSequenceTestCase.class);
        addTestSuite(ExpectationSetTestCase.class);
        addTestSuite(RepeatingExpectationTestCase.class);
        addTestSuite(ExpectedMethodInvocationTestCase.class);
    }

    public static junit.framework.Test suite() {
        return new MockTestSuite("MockTestSuite");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
