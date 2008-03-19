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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import java.io.IOException;

import mock.javax.servlet.http.HttpServletResponseMock;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for EmptyBodyResponseWrapper class
 */
public class EmptyBodyResponseWrapperTestCase extends TestCaseAbstract {
    
    private HttpServletResponseMock responseMock; 
    private EmptyBodyResponseWrapper testee; 
 

    protected void setUp() throws Exception {
        // Let the base class do its preparation 
        super.setUp();        
        // Create mock of the underlying response
        responseMock = new HttpServletResponseMock("response", expectations);
        // Create the test subject
        testee = new EmptyBodyResponseWrapper(responseMock);                
    }
    
    /**
     * Test that header are correctly passed to the undlerlying response
     */
    public void testHeaders() {
        // Test setting of headers
        String headerName = "Header-Name";
        String headerValue = "Header value";        
        // Headers are expected to be passed to the underlying response
        responseMock.expects.addHeader(headerName, headerValue);
        testee.addHeader(headerName, headerValue);
    }
    
    /**
     * Test data written to the output stream do not reach the response,
     * but the content length is computed properly 
     */
    public void testWritingToOutputStream() throws IOException {
        // Test writing the output stream using all three available methods
        // No data is expected to reach the underlying response,
        // so no expectation set on the mock
        testee.getOutputStream().write('a');
        testee.getOutputStream().write(new byte[] { 'b', 'c', 'd', 'e', 'f', 'g'});
        testee.getOutputStream().write(new byte[] { 'h', 'i', 'j', 'k'}, 1, 3);
        testee.getOutputStream().flush();
        // but the length should be correct
        responseMock.expects.setContentLength(10);
        testee.updateContentLength();
    }
    
    /**
     * Test data written to the print writer do not reach the response,
     * but the content length is computed properly 
     */
    public void testWritingToPrintWriter() throws IOException {
        // Test writing the print writer
        testee.getWriter().print("12345");
        testee.getWriter().flush();
        // No data is expected to reach the underlying response,
        // so no expectation set on the mock
        // but the length should be correct
        responseMock.expects.setContentLength(5);
        testee.updateContentLength();        
    }    

    protected void tearDown() throws Exception {
        // Clear our objects        
        testee = null;
        responseMock = null;
        // Let the base class do its cleanup
        super.tearDown();
    }
}
