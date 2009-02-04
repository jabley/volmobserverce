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
package com.volantis.mcs.xdime;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.marlin.sax.AbstractContentHandlerFactory;
import com.volantis.mcs.marlin.sax.MCSInternalContentHandler;

/**
 * A factory which creates {@link XDIMEContentHandler} instances.
 */
public class XDIMEContentHandlerFactory
        implements AbstractContentHandlerFactory {

    /**
     * String array of the namespaces that should be handled by the
     * {@link XDIMEContentHandler}.
     */
    private static final String[] supportedNamespaces = new String[]{
        XDIMESchemata.XDIME_CP_SIMPLE_INITIALISATION_NAMESPACE,
        XDIMESchemata.XDIME_CP_INTERIM_SIMPLE_INITIALISATION_NAMESPACE,
        XDIMESchemata.XDIME2_SIMPLE_INITIALISATION_NAMESPACE,
        XDIMESchemata.XFORMS_NAMESPACE,
        XDIMESchemata.XHTML2_NAMESPACE,
        XDIMESchemata.XDIME2_MCS_NAMESPACE,
        XDIMESchemata.WIDGETS_NAMESPACE,
        XDIMESchemata.RESPONSE_NAMESPACE,
        XDIMESchemata.XML_EVENTS_NAMESPACE,
        XDIMESchemata.TICKER_NAMESPACE,
        XDIMESchemata.TICKER_RESPONSE_NAMESPACE,
        XDIMESchemata.GALLERY_NAMESPACE
    };

    // Javadoc inherited.
    public MCSInternalContentHandler createContentHandler(
            MarinerRequestContext requestContext) {

        XDIMEContentHandler handler = new XDIMEContentHandler();
        handler.setInitialRequestContext(requestContext);
        return handler;
    }

    // Javadoc inherited.
    public String[] getHandledNamespaces() {
        return supportedNamespaces;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 ===========================================================================
*/
