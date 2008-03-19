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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.context.MarinerRequestContext;
import org.xml.sax.Locator;

/**
 * This interface defines the methods that are necessary for an MCS content
 * handler. It is an internal interface and should not be exposed to clients
 * ({@link MarlinContentHandler} is the public api).
 */
public interface MCSInternalContentHandler extends MarlinContentHandler {

    /**
     * Get the current MarinerRequestContext.
     *
     * @return the current MarinerRequestContext
     */
    MarinerRequestContext getCurrentRequestContext();

    /**
     * Sets the initial MarinerRequestContext for this content handler.
     */
    void setInitialRequestContext(MarinerRequestContext requestContext);

    /**
     * Return the current document locator. May return null if none has been
     * set.
     *
     * @return Locator that should be used to locate the origin of SAX document
     *         events, or null if none has been set.
     */
    Locator getDocumentLocator();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 ===========================================================================
*/
