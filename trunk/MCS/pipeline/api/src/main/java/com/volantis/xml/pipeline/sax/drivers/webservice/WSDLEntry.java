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

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.shared.net.url.URLContentManager;
import org.xml.sax.InputSource;

import java.util.ArrayList;

/**
 * A WSDLEntry contains the uri for a WSDL document and alternative sources for
 * this document if any. These alternative sources are provided by
 * WSDLResource objects. The order that these WSDLResource objects are added to
 * the WSDLEntry are the order in which they will be tried. If none of the
 * alternative sources provide the WSDL document (or if there are no
 * alternatives) then the uri property of the WSDLEntry is used.
 */
public class WSDLEntry {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(WSDLEntry.class);

    /**
     * The uri of this WSDLEntry.
     */
    private String uri;

    /**
     * The List of alternative sources for the WSDL document. These alternatives
     * are stored as WSDLResource objects. The order that they are in this list
     * are the order in which they will be tried. Since a WSDLEntry may have no
     * associated WSDLResources, this list is lazilly initialized.
     */
    private ArrayList wsdlResources;

    /**
     * Creates a new WSDLEntry with no properties set.
     */
    public WSDLEntry() {
        this(null);
    }

    /**
     * Creates a new WSDLEntry with a given uri.
     * @param uri The uri for this WSDLEntry.
     */
    public WSDLEntry(String uri) {
        this.uri = uri;
    }

    /**
     * Get the uri for this WSDLEntry.
     * @return the uri.
     */
    public String getURI() {
        return uri;
    }

    /**
     * Set the uri for this WSDLEntry.
     * @param uri The uri.
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * Add a WSDLResource to the list of alternative sources for the WSDL doc.
     * @param wsdlResource the WSDLResource to add.
     */
    public void addWSDLResource(WSDLResource wsdlResource) {
        if (wsdlResources == null) {
            wsdlResources = new ArrayList(4);
        }
        wsdlResources.add(wsdlResource);
    }

    /**
     * Remove a specified WSDLResource from the list of alternative sources
     * for the WSDL doc.
     * @param wsdlResource the WSDLResource to remove.
     */
    public void removeWSDLResouce(WSDLResource wsdlResource) {
        if (wsdlResources != null) {
            wsdlResources.remove(wsdlResource);
        }
    }

    /**
     * Provide an alternative InputSource for the WSDL document associated with
     * this WSDLEntry. This method tries all the available alternatives but not
     * the uri. If there is no available alternative InputSource then null is
     * returned.
     * @return An alternative org.xml.sax.InputSource that specifies the
     * WSDL doc.
     */
    public InputSource provideAlternativeInputSource(
            XMLPipelineContext context,
            URLContentManager manager) {
        InputSource inputSource = null;

        if (wsdlResources != null) {
            for (int i = 0;
                 i < wsdlResources.size() && inputSource == null;
                 i++) {
                WSDLResource wsdlResource = (WSDLResource)wsdlResources.get(i);

                try {
                    inputSource = wsdlResource.provideInputSource(context);
                } catch (WSDLResourceException wsdl) {
                    logger.warn("wsdl-resource-input-source-failure",
                                wsdlResource,
                                wsdl);
                }
            }
        }

        return inputSource;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 24-Jun-03	124/1	adrian	VBM:2003061902 Implemented provideInputSource method for ServletRequestResource

 23-Jun-03	111/1	adrian	VBM:2003061903 Implemented URIResource provideInputSource method

 19-Jun-03	98/4	allan	VBM:2003022822 WS Connector renamed to WS Driver

 18-Jun-03	98/1	allan	VBM:2003022822 Promote prelimary classes so that other can access them

 ===========================================================================
*/
