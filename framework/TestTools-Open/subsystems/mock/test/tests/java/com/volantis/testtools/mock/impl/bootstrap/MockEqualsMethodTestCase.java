/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.bootstrap;

import com.volantis.mock.samples.InterfaceWithEqualsMock;
import com.volantis.testtools.mock.MockObject;

/**
 * Test the mock {@link Object#equals(Object)} method works.
 */
public class MockEqualsMethodTestCase
        extends OptionalObjectMethodTestAbstract {

    private static final Object OBJECT = new Object();

    // Javadoc inherited.
    protected MockObject createUncheckingMock() {
        return new InterfaceWithEqualsMock("equalsMock", expectations);
    }

    // Javadoc inherited.
    protected void doTestUncheckingMock(final MockObject equalsMock) {
        assertFalse("Not equal to null", equalsMock.equals(null));
        assertFalse("Not equal to an object", equalsMock.equals(OBJECT));
        assertTrue("Equal to self", equalsMock.equals(equalsMock));
    }

    // Javadoc inherited.
    protected MockObject createCheckingMock() {
        final InterfaceWithEqualsMock equalsMock =
                new InterfaceWithEqualsMock("equalsMock", expectations);
        equalsMock.configuration.setEqualsShouldCheckExpectations(true);
        return equalsMock;
    }

    // Javadoc inherited.
    protected void addCheckingExpectations(final MockObject mock) {
        InterfaceWithEqualsMock equalsMock = (InterfaceWithEqualsMock) mock;
        equalsMock.expects._equals(OBJECT).returns(true);
        equalsMock.expects._equals(equalsMock).returns(true);
    }

    // Javadoc inherited.
    protected void doTestCheckingMock(final MockObject mock) {
        assertTrue("Not equal to an object", mock.equals(OBJECT));
        assertTrue("Equal to self", mock.equals(mock));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 12-Jul-05	8996/5	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 ===========================================================================
*/
