/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.bootstrap;

import com.volantis.mock.samples.InterfaceWithHashCodeMock;
import com.volantis.testtools.mock.MockObject;

/**
 * Test the mock {@link Object#hashCode()} method works.
 */
public class MockHashCodeMethodTestCase
        extends OptionalObjectMethodTestAbstract {
    private static final int HASH_CODE = 10;

    // Javadoc inherited.
    protected MockObject createUncheckingMock() {
        return new InterfaceWithHashCodeMock("hashCodeMock", expectations);
    }

    // Javadoc inherited.
    protected void doTestUncheckingMock(MockObject mock) {
        int systemIdentityHashCode = System.identityHashCode(mock);
        assertEquals("Hash code", systemIdentityHashCode,
                     mock.hashCode());
    }

    // Javadoc inherited.
    protected MockObject createCheckingMock() {
        final InterfaceWithHashCodeMock hashCodeMock =
                new InterfaceWithHashCodeMock("hashCodeMock", expectations);
        hashCodeMock.configuration.setHashCodeShouldCheckExpectations(true);
        return hashCodeMock;
    }

    // Javadoc inherited.
    protected void addCheckingExpectations(MockObject mock) {
        InterfaceWithHashCodeMock hashCodeMock =
                (InterfaceWithHashCodeMock) mock;
        hashCodeMock.expects._hashCode().returns(HASH_CODE).any();
    }

    // Javadoc inherited.
    protected void doTestCheckingMock(MockObject mock) {
        assertEquals("Hash code", HASH_CODE, mock.hashCode());
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
