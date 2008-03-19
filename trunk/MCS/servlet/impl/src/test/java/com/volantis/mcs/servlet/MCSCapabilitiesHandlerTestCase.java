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
package com.volantis.mcs.servlet;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.stubs.HttpServletRequestStub;
import com.volantis.testtools.stubs.HttpServletResponseStub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Tests for MCSCapabilitiesHandler.
 */
public class MCSCapabilitiesHandlerTestCase extends TestCaseAbstract {
    /**
     * Gets an HttpServletRequest with the specified value for the x-mcs-query
     * header.
     *
     * @param mcsQueryValue The value of the x-mcs-query header
     * @return An HttpServletRequest
     */
    private HttpServletRequest getRequest(final String mcsQueryValue) {
        HttpServletRequestStub request = new HttpServletRequestStub() {
            // Javadoc inherited
            public String getHeader(String headerName) {
                if ("x-mcs-query".equals(headerName)) {
                    return mcsQueryValue;
                } else {
                    return null;
                }
            }

            // Javadoc inherited
            public Enumeration getHeaders(String headerName) {
                Vector v = new Vector();
                if (getHeader(headerName) != null) {
                    v.add(getHeader(headerName));
                }
                return v.elements();
            }
        };
        return request;
    }

    /**
     * Test the checking of a request to see if it's for MCS capabilities.
     */
    public void testIsMCSCapabilitiesRequest() {
        // Test with no x-mcs-query header set.
        MCSCapabilitiesHandler handler =
                new MCSCapabilitiesHandler(getRequest(null),
                        new HttpServletResponseTest());
        assertFalse(handler.isMCSCapabilitiesRequest());

        // Test with an irrelevant x-mcs-query header set.
        handler = new MCSCapabilitiesHandler(getRequest("isThePopeCatholic"),
                new HttpServletResponseTest());
        assertFalse(handler.isMCSCapabilitiesRequest());

        // Test with the x-mcs-query header set to capabilities.
        handler = new MCSCapabilitiesHandler(getRequest("capabilities"),
                new HttpServletResponseTest());
        assertTrue(handler.isMCSCapabilitiesRequest());
    }

    /**
     * Test the processing of a request for MCS capabilities.
     */
    public void testProcessMCSCapabilitiesRequest() {
        HttpServletResponseTest response = new HttpServletResponseTest();
        MCSCapabilitiesHandler handler =
                new MCSCapabilitiesHandler(getRequest("capabilities"),
                        response);
        handler.processMCSCapabilitiesRequest();

        assertEquals("Status should be set to No Content",
                HttpServletResponse.SC_NO_CONTENT, response.getStatus());
        assertEquals("x-mcs-capabilities header should be set correctly",
                "MCS, xdime, xdime cp",
                response.getHeaders().get("x-mcs-capabilities"));
    }

    /**
     * A simple HttpServletResponse implementation that stores the status and
     * headers.
     */
    private class HttpServletResponseTest extends HttpServletResponseStub {
        private int status;
        private Map headers = new HashMap();

        // Javadoc inherited
        public void setStatus(int i) {
            status = i;
        }

        // Javadoc inherited
        public void setHeader(String s, String s1) {
            headers.put(s, s1);
        }

        public Map getHeaders() {
            return headers;
        }

        public int getStatus() {
            return status;
        }
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-05	9616/1	adrianj	VBM:2005092610 Allow querying of MCS capabilities

 ===========================================================================
*/
