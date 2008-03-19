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
package com.volantis.mcs.http.servlet;

import com.volantis.mcs.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * A default implementation for the HttpServletFactory.
 */
public class DefaultHttpServletFactory extends HttpServletFactory {

    // javadoc inherited.
    public HttpHeaders getHTTPHeaders(final HttpServletRequest request) {
        return new HttpHeaders() {
            // javadoc inherited.
            public String getHeader(String name) {
                return request.getHeader(name);
            }

            // javadoc inherited.
            public Enumeration getHeaders(String name) {
                return request.getHeaders(name);
            }

            // javadoc inherited.
            public Enumeration getHeaderNames() {
                return request.getHeaderNames();
            }
        };
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 ===========================================================================
*/
