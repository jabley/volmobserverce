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
package com.volantis.xml.pipeline.sax.drivers.webservice;

import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLCatalog;

/**
 * Encapsulates the configuration information for the various web services
 * driver.
 *
 * @todo later this should probably be made available as part of the public API
 */
public class WSDriverConfiguration implements Configuration {

    /**
     * The catalog of wsdl entries for this WSDriverConfiguration.
     */
    private WSDLCatalog wsdlCatalog;

    /**
     * The namespace uri for response messsages.
     */
    private String responseNamespaceURI =
            "http://www.volantis.com/xmlns/marlin-web-service-response";

    /**
     * The prefix for response messages.
     */
    private String responseDefaultPrefixURI = "wsr";

    /**
     * Get the WSDLCatalog associated with this WSDriverConfiguration.
     * @return The WSDLCatalog for this WSDriverConfiguration.
     */
    public WSDLCatalog getWSDLCatalog() {
        return wsdlCatalog;
    }

    /**
     * Set the WSDLCatalog associated with the WSDriverConfiguration.
     * @param wsdlCatalog The WSDLCatalog for this WSDriverConfiguration.
     */
    public void setWsdlCatalog(WSDLCatalog wsdlCatalog) {
        this.wsdlCatalog = wsdlCatalog;
    }

    /**
     * Get the namespace URI for result elements produced by this
     * WSDriverConfiguration.
     * @return responseNamespaceURI
     */
    public String getResponseNamespaceURI() {
        return responseNamespaceURI;
    }

    /**
     * Set the namespace URI for result elements produced by this
     * WSDriverConfiguration.
     * @param responseNamespaceURI The result namespace URI.
     */
    public void setResponseNamespaceURI(String responseNamespaceURI) {
        this.responseNamespaceURI = responseNamespaceURI;
    }

    /**
     * Get the default prefix for result elements produced by this
     * WSDriver.
     * @return responseDefaultPrefixURI
     */
    public String getResponseDefaultPrefixURI() {
        return responseDefaultPrefixURI;
    }

    /**
     * Set the default prefix for result elements produced by this
     * WSDriver.
     * @param responseDefaultPrefixURI
     */
    public void setResponseDefaultPrefixURI(String responseDefaultPrefixURI) {
        this.responseDefaultPrefixURI = responseDefaultPrefixURI;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	285/1	doug	VBM:2003080402 Renamed XMLProcessConfiguration interface to Configuration

 29-Jun-03	98/7	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 19-Jun-03	98/5	allan	VBM:2003022822 WS Connector renamed to WS Driver

 18-Jun-03	98/1	allan	VBM:2003022822 Promote prelimary classes so that other can access them

 ===========================================================================
*/
