/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.org.xml.sax;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.org.xml.sax.EntityResolverMock;
import mock.org.xml.sax.LocatorMock;
import mock.org.xml.sax.ErrorHandlerMock;

/**
 * Tests for extended SQL related mock objects.
 */
public class SAXTestCase
        extends MockTestCaseAbstract {

    /**
     * Tests that all the mock objects can be initialised correctly.
     */
    public void testInitialisation() {
        new EntityResolverMock("entityResolver", expectations);
        new LocatorMock("locator", expectations);
        new ErrorHandlerMock("errorHandlerMock", expectations);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 ===========================================================================
*/
