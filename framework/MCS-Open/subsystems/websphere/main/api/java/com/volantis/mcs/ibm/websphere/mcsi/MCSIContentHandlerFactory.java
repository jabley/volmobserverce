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
package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.marlin.sax.AbstractContentHandlerFactory;
import com.volantis.mcs.marlin.sax.MCSInternalContentHandler;
import com.volantis.mcs.ibm.websphere.Websphere5Interface;

/**
 * A factory which creates {@link MCSIContentHandler} instances.
 */
public class MCSIContentHandlerFactory implements AbstractContentHandlerFactory {

    /**
     * String array of the namespaces that should be handled by the
     * {@link MCSIContentHandler}.
     */
    private static final String[] handledNamespaces =
            new String[]{Websphere5Interface.MCSI_URI};

    // Javadoc inherited.
    public MCSInternalContentHandler createContentHandler(
            MarinerRequestContext requestContext) {

        MCSIContentHandler handler = new MCSIContentHandler();
        handler.setInitialRequestContext(requestContext);
        return handler;
    }

    // Javadoc inherited.
    public String[] getHandledNamespaces() {
        return handledNamespaces;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 ===========================================================================
*/
