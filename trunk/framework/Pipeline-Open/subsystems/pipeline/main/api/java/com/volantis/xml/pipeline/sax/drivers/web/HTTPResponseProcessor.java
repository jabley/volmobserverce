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
package com.volantis.xml.pipeline.sax.drivers.web;

import java.io.InputStream;

/**
 * Interface for processing a response from an HTTP request
 */
public interface HTTPResponseProcessor {
    /**
     * Processes an http response
     *
     * @param redirectURL
     *                   the url that was redirected to if a redirect occurred.
     *                   Null if a redirect did not occur
     * @param responseStream
     *                   an InputStream that can be used to access the response
     *                   body.
     * @param statusCode the status of the response.
     * @param contentType
     *                   the content type of the response.
     * @param contentEncoding
     *                   the content encoding of the response.
     * @throws HTTPException if an error occurs.
     */
    public void processHTTPResponse(String redirectURL,
                                    InputStream responseStream,
                                    int statusCode,
                                    String contentType,
                                    String contentEncoding)
            throws HTTPException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 23-Sep-04	890/1	byron	VBM:2004092003 DSB:HTTPS Requests not working

 23-Sep-04	888/1	byron	VBM:2004092003 DSB:HTTPS Requests not working

 12-Jul-04	751/3	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 ===========================================================================
*/
