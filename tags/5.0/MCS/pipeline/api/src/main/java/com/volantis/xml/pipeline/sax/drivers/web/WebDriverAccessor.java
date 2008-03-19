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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;

/**
 * Provides methods to access the web driver interaction objects from the
 * current pipeline context.
 * <p>Implementations of this interface must ensure that the methods are thread
 * safe as they may be called from multiple threads simultaneously.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface WebDriverAccessor {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Get the request from the current pipeline context.
     * <p>If no request currently exists then this factory can create one
     * from the environment specific information accessed through the
     * pipeline context.</p>
     * @param pipelineContext The context within which this method is being
     * invoked.
     * @return The WebDriverRequest, or null if it is not provided.
     */
    public WebDriverRequest getRequest(XMLPipelineContext pipelineContext);

    /**
     * Get the response used to contain the response from the web driver.
     * @param pipelineContext The context within which this method is being
     * invoked.
     * @param id The id that identifies the instance of the web driver, may be
     * null.
     * @return The response to use, or null if no response is required.
     */
    public WebDriverResponse getResponse(XMLPipelineContext pipelineContext,
                                         String id);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 17-Jul-03	215/2	steve	VBM:2003070806 Implement we driver

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
