/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.method.ExpectedArguments;
import com.volantis.testtools.mock.method.MethodIdentifier;
import com.volantis.testtools.mock.example.FooMock;
import com.volantis.testtools.mock.impl.method.ExpectedMethodInvocationImpl;
import com.volantis.testtools.mock.impl.method.MethodInvocationEvent;

import java.util.ArrayList;
import java.util.List;

public class ExpectedMethodInvocationTestCase
        extends RepeatableExpectationTestAbstract {

    private FooMock fooMock;

    protected void setUp() throws Exception {
        super.setUp();

        fooMock = new FooMock("fooMock");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected Expectation createExpectation() {
        Object[] arguments = new Object[]{
            "input"
        };

        MethodIdentifier identifier =
                FooMock._getMethodIdentifier("setBar(java.lang.String)");
        return new ExpectedMethodInvocationImpl(fooMock, identifier,
                new ExpectedArguments(identifier, arguments, true), null);
    }

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
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
