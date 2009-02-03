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
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPMessageEntities;

/**
 * This interface provides a method that allows the manipulation of request
 * and response headers and cookies. This interface is currently only used in the
 * AbstractPluggableHTTPManager to allow manipulation of HTTP headers after a
 * response is recieved. It allows changes to be made before a redirect is
 * followed or after a successful get/post is made.
 *
 * @see com.volantis.xml.pipeline.sax.drivers.web.AbstractPluggableHTTPManager
 * @see com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration
 */
public interface PluggableHeaderManipulator {

    /**
     * Implementations of this method are permitted to manipulate the header and
     * cookie parameters.
     *
     * @param responseStatusCode The status code of the HTTP response. -1 if it
     * is not known
     * @param responseHeaders The headers associated with the http response.
     * Null if not known.
     * @param requestHeaders The headers associated with the http request. null
     *  if not known
     */
    public void manipulateHeaders(int responseStatusCode,
                                  HTTPMessageEntities responseHeaders,
                                  HTTPMessageEntities requestHeaders);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 05-Sep-04	854/1	matthew	VBM:2004083107 need to see changes from another stream

 ===========================================================================
*/
