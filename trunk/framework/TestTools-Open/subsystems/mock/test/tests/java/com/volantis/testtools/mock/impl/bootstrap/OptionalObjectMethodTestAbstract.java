/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.bootstrap;

import com.volantis.testtools.mock.MockObject;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import com.volantis.testtools.mock.test.MockTestHelper;

/**
 * Base class for testing all object methods that are optional.
 */
public abstract class OptionalObjectMethodTestAbstract
        extends MockTestCaseAbstract {

    /**
     * Create a mock object that does not check expectations on the method
     * being tested.
     *
     * @return A mock object.
     */
    protected abstract MockObject createUncheckingMock();

    /**
     * Create a mock object that does check expectations on the method being
     * tested.
     *
     * @return A mock object.
     */
    protected abstract MockObject createCheckingMock();

    /**
     * Test that the mock method does not by default check any expectations
     */
    public void testDoesNotCheckExpectationsByDefault() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final MockObject equalsMock = createUncheckingMock();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        doTestUncheckingMock(equalsMock);
    }

    protected abstract void doTestUncheckingMock(MockObject mock);

    /**
     * Test that the mock method does check expectations if it is explicitly
     * configured to do so.
     */
    public void testDoesCheckExpectationsWhenAsked() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final MockObject mock = createCheckingMock();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        addCheckingExpectations(mock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        doTestCheckingMock(mock);
    }

    protected abstract void doTestCheckingMock(MockObject mock);

    protected abstract void addCheckingExpectations(MockObject mock);

    /**
     * Test that the mock method does not check expectations within the
     * framework even when it has been asked to do so but does check both
     * before and after.
     */
    public void testDoesNotCheckExpectationsWithinFrameworkEvenWhenAsked() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final MockObject mock = createCheckingMock();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Test before entering the framework.
        addCheckingExpectations(mock);
        doTestCheckingMock(mock);

        // Test within the framework.
        try {
            MockTestHelper.insideFramework();
            doTestUncheckingMock(mock);
        } finally {
            MockTestHelper.outsideFramework();
        }

        // Test after exiting the framework.
        addCheckingExpectations(mock);
        doTestCheckingMock(mock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 ===========================================================================
*/
