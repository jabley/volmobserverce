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

import org.xml.sax.InputSource;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;

/**
 * An interface for classes that provide access to a WSDL document via an
 * org.xml.sax.InputSource.
 */
public interface WSDLResource {
    /**
     * Provide an InputSource for a WSDL document.
     *
     * @param context the context in which the input source is required
     * @return An org.xml.sax.InputSource.
     */
    public InputSource provideInputSource(XMLPipelineContext context)
            throws WSDLResourceException;
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

 23-Jun-03	111/1	adrian	VBM:2003061903 Implemented URIResource provideInputSource method

 19-Jun-03	98/4	allan	VBM:2003022822 WS Connector renamed to WS Driver

 18-Jun-03	98/1	allan	VBM:2003022822 Promote prelimary classes so that other can access them

 ===========================================================================
*/
