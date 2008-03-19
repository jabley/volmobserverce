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

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.shared.time.Period;

/**
 * This interface provides the methods to perform and support HTTP get and post
 * operations
 */
public interface PluggableHTTPManager {

    /**
     * Initializes the <code>PluggableHTTPManager</code>
     *
     * @param configuration the WebDriverConfiguration.
     * @param timeout       the timeout to apply to the connections made by the
     *                      HTTP manager, measured in milliseconds. A negative
     *                      or zero value means that the configuration's
     */
    public void initialize(WebDriverConfiguration configuration, Period timeout);

    /**
     * Sends the requested url and replies with a HTTP status code.
     *
     * @param requestDetails
     *         encapsulates the details of the request
     * @param xmlPipelineContext the XML pipeline context.
     * @return The HTTP status code that resulted from the request
     */
    public int sendRequest(RequestDetails requestDetails,
                           XMLPipelineContext xmlPipelineContext)
            throws HTTPException;

    /**
     * Returns the underlying {@link HTTPCache}
     * @return the HTTPCache or null if caching is not supported
     */
    public HTTPCache getHTTPCache();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 29-Nov-04	6302/3	byron	VBM:2004112609 DSB has an issue with user-agent being replaced under stress

 29-Nov-04	6302/1	byron	VBM:2004112609 DSB has an issue with user-agent being replaced under stress

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 07-Sep-04	858/1	doug	VBM:2004090610 Added preprocessing of response capability

 01-Sep-04	740/1	doug	VBM:2004052801 Fixed problem with REMOVE_PREFIX URLPrefixRewriteOperation

 12-Jul-04	751/4	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 ===========================================================================
*/
