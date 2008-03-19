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
 * $Header: /src/voyager/com/volantis/mcs/runtime/ServletRequestHeaders.java,v 1.3 2003/03/11 12:54:34 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-03    Byron           VBM:2003021204 - Created
 * 14-Feb-03    Byron           VBM:2003021204 - Added private helper method
 *                              indexOfNextQuote()
 * 11-Mar-03    Phil W-S        VBM:2003031102 - Fix parseAcceptMimeTypes to
 *                              strip leading and trailing whitespace (as well
 *                              as the parameters) from all supplied values.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.http.HTTPHeadersHelper;
import com.volantis.mcs.http.HttpHeaders;

/**
 * This wraps a <code>ServletRequest</code> and exposes the mime types and
 * profile header name.
 * @todo later this class should be renamed to RuntimeServletRequestHeaders or moved to another package.
 */
public class ServletRequestHeaders extends RequestHeaders {

    /**
     * Wrap the servlet request headers in the <code>HttpHeaders</code> instance.
     */
    private HttpHeaders headers;

    /**
     * Construct this object with the HttpHeaders instance.
     *
     * @param headers the HttpHeaders that wraps the servlet request headers.
     */
    public ServletRequestHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    /**
     * Get the headers for the servlet request headers.
     * @return the http headers.
     */
    public HttpHeaders getHttpHeaders() {
        return headers;
    }


    // javadoc inherited
    public String[] getAcceptMimeTypes() {
        return HTTPHeadersHelper.getAcceptMimeTypes(headers);
    }

    // javadoc inherited.
    public String getHeader(String name) {
        return headers.getHeader(name);
    }
}
