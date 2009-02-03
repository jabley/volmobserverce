/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.bootstrap;

import com.volantis.mock.samples.InterfaceWithToStringMock;
import com.volantis.testtools.mock.MockObject;

/**
 * Test the mock {@link Object#toString()} method works.
 */
public class MockToStringMethodTestCase
        extends OptionalObjectMethodTestAbstract {
    private static final String TO_STRING = "fred";

    // Javadoc inherited.
    protected MockObject createUncheckingMock() {
        return new InterfaceWithToStringMock("toStringMock", expectations);
    }

    // Javadoc inherited.
    protected void doTestUncheckingMock(MockObject mock) {
        assertEquals(mock._getIdentifier(), mock.toString());
    }

    // Javadoc inherited.
    protected MockObject createCheckingMock() {
        final InterfaceWithToStringMock toStringMock =
                new InterfaceWithToStringMock("toStringMock",
                                              expectations);
        toStringMock.configuration.setToStringShouldCheckExpectations(true);
        return toStringMock;
    }

    // Javadoc inherited.
    protected void addCheckingExpectations(MockObject mock) {
        InterfaceWithToStringMock toStringMock =
                (InterfaceWithToStringMock) mock;
        toStringMock.expects._toString().returns(TO_STRING);
    }

    // Javadoc inherited.
    protected void doTestCheckingMock(MockObject mock) {
        assertEquals("toString", TO_STRING, mock.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 ===========================================================================
*/
