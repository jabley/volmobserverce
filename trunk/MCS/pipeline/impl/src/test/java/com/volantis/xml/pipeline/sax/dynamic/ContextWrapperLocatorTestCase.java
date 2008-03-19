/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.dynamic;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineContextMock;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextWrapperLocator;
import mock.org.xml.sax.LocatorMock;

import java.net.URL;
import java.net.MalformedURLException;

/**
 * JUnit test class used to test ContextWrapperLocator.  It uses the delegate
 * pattern, calling the equivalent Locator interface methods on the current
 * Locator of a wrapped XMLPipelineContext
 */
public class ContextWrapperLocatorTestCase extends TestCaseAbstract {

    private static final String A_B_STRING = "file:/a/b.xml";
    private static final URL A_B_URL;
    static {
        try {
            A_B_URL = new URL(A_B_STRING);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String B_C_STRING = "file:/b/c.xml";

    private XMLPipelineContextMock contextMock;
    private LocatorMock locatorMock;
    
    protected void setUp() throws Exception {
        super.setUp();

        locatorMock = new LocatorMock("locatorMock", expectations);

        contextMock = new XMLPipelineContextMock(
                "contextMock", expectations);
    }

    /**
     * Test the method getSystemId.
     */
    public void testGetSystemId() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getCurrentBaseURI()
                .returns(A_B_URL).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContextWrapperLocator locator = new ContextWrapperLocator(contextMock);

        assertEquals(A_B_STRING, locator.getSystemId());
    }

    /**
     * Test the method getPublicId when system id matches the base URL.
     */
    public void testGetPublicIdSystemIdMatches() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getCurrentLocator().returns(locatorMock).any();
        contextMock.expects.getCurrentBaseURI()
                .returns(A_B_URL).any();
        locatorMock.expects.getSystemId().returns(A_B_STRING).any();
        locatorMock.expects.getPublicId().returns("xyz").any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContextWrapperLocator locator = new ContextWrapperLocator(contextMock);

        assertEquals("xyz", locator.getPublicId());
    }

    /**
     * Test the method getPublicId when system id does not match the base URL.
     */
    public void testGetPublicIdSystemIdDoesNotMatch() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getCurrentLocator().returns(locatorMock).any();
        contextMock.expects.getCurrentBaseURI()
                .returns(A_B_URL).any();
        locatorMock.expects.getSystemId().returns(B_C_STRING).any();
        locatorMock.expects.getPublicId().returns("xyz").any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContextWrapperLocator locator = new ContextWrapperLocator(contextMock);

        assertEquals(null, locator.getPublicId());
    }

    /**
     * Test the method getColumnNumber when system id matches the base URL.
     */
    public void testGetColumnNumberSystemIdMatches() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getCurrentLocator().returns(locatorMock).any();
        contextMock.expects.getCurrentBaseURI()
                .returns(A_B_URL).any();
        locatorMock.expects.getSystemId().returns(A_B_STRING).any();
        locatorMock.expects.getColumnNumber().returns(22).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContextWrapperLocator locator = new ContextWrapperLocator(contextMock);

        assertEquals(22, locator.getColumnNumber());
    }

    /**
     * Test the method getColumnNumber when system id does not match the base URL.
     */
    public void testGetColumnNumberSystemIdDoesNotMatch() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getCurrentLocator().returns(locatorMock).any();
        contextMock.expects.getCurrentBaseURI()
                .returns(A_B_URL).any();
        locatorMock.expects.getSystemId().returns(B_C_STRING).any();
        locatorMock.expects.getColumnNumber().returns(22).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContextWrapperLocator locator = new ContextWrapperLocator(contextMock);

        assertEquals(-1, locator.getColumnNumber());
    }

    /**
     * Test the method getLineNumber when system id matches the base URL.
     */
    public void testGetLineNumberSystemIdMatches() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getCurrentLocator().returns(locatorMock).any();
        contextMock.expects.getCurrentBaseURI()
                .returns(A_B_URL).any();
        locatorMock.expects.getSystemId().returns(A_B_STRING).any();
        locatorMock.expects.getLineNumber().returns(2).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContextWrapperLocator locator = new ContextWrapperLocator(contextMock);

        assertEquals(2, locator.getLineNumber());
    }

    /**
     * Test the method getLineNumber when system id does not match the base URL.
     */
    public void testGetLineNumberSystemIdDoesNotMatch() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getCurrentLocator().returns(locatorMock).any();
        contextMock.expects.getCurrentBaseURI()
                .returns(A_B_URL).any();
        locatorMock.expects.getSystemId().returns(B_C_STRING).any();
        locatorMock.expects.getLineNumber().returns(2).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContextWrapperLocator locator = new ContextWrapperLocator(contextMock);

        assertEquals(-1, locator.getLineNumber());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jan-04	527/3	adrian	VBM:2004011903 Added Copyright statements to new classes

 20-Jan-04	527/1	adrian	VBM:2004011903 Added ContextAnnotationProcess and supporting classes

 ===========================================================================
*/
