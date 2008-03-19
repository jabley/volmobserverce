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

import com.volantis.shared.net.url.URLContentManager;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.url.PipelineURLContentManager;
import org.xml.sax.InputSource;

import java.io.IOException;

/**
 * A WSDLResource based on a URI.
 */
public class URIResource implements WSDLResource {
    /**
     * The uri to the resource in the classuri.
     */
    private String uri;

    /**
     * Construct a URIResource with no properties set.
     */
    public URIResource() {
        this(null);
    }

    /**
     * Construct a new URIBasedResource with a given uri.
     * @param uri The uri.
     */
    public URIResource(String uri) {
        setURI(uri);
    }

    /**
     * Get the uri.
     * @return the uri.
     */
    public String getURI() {
        return uri;
    }

    /**
     * Set the uri.
     * @param uri
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    // javadoc inherited
    public InputSource provideInputSource(XMLPipelineContext context)
            throws WSDLResourceException {

        URLContentManager manager = PipelineURLContentManager.retrieve(context);

        InputSource result;
        try {
            result = WSDriverUtils.createURIInputSource(uri, manager, context);
        } catch (IOException e) {
            throw new WSDLResourceException(e);
        }

        return result;
    }

    // javadoc inherited
    public boolean equals(Object o) {
        boolean result = false;

        if (this == o) {
            result = true;
        } else {
            if (o instanceof URIResource) {

                final URIResource uriResource = (URIResource)o;

                if (uri != null ? uri.equals(uriResource.uri) :
                        uriResource.uri == null) {
                    result = true;
                }
            }
        }

        return result;
    }

    // javadoc inherited
    public int hashCode() {
        return (uri != null ? uri.hashCode() : 0);
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

 24-Jun-03	124/1	adrian	VBM:2003061902 Implemented provideInputSource method for ServletRequestResource

 23-Jun-03	111/3	adrian	VBM:2003061903 set SystemID on InputSource created in URIResource

 23-Jun-03	111/1	adrian	VBM:2003061903 Implemented URIResource provideInputSource method

 19-Jun-03	98/4	allan	VBM:2003022822 WS Connector renamed to WS Driver

 18-Jun-03	98/1	allan	VBM:2003022822 Promote prelimary classes so that other can access them

 ===========================================================================
*/
