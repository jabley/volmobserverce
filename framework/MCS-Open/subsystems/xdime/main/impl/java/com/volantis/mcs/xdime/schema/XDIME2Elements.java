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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.schema;

import com.volantis.mcs.xdime.events.XMLEventsElements;
import com.volantis.mcs.xdime.gallery.GalleryElements;
import com.volantis.mcs.xdime.mcs.MCSElements;
import com.volantis.mcs.xdime.ticker.TickerElements;
import com.volantis.mcs.xdime.ticker.TickerResponseElements;
import com.volantis.mcs.xdime.widgets.ResponseElements;
import com.volantis.mcs.xdime.widgets.WidgetElements;
import com.volantis.mcs.xdime.xforms.XFormElements;
import com.volantis.mcs.xdime.xhtml2.XHTML2Elements;
import com.volantis.mcs.xml.schema.model.SchemaNamespacesBuilder;
import com.volantis.mcs.xml.schema.model.SchemaNamespaces;

public class XDIME2Elements
        extends SchemaNamespacesBuilder {

    /**
     * The default instance.
     */
    private static final SchemaNamespaces DEFAULT_INSTANCE;
    static {
        SchemaNamespacesBuilder namespaces = new SchemaNamespacesBuilder();
        namespaces.addNamespace(MCSElements.NAMESPACE);
        namespaces.addNamespace(ResponseElements.NAMESPACE);
        namespaces.addNamespace(SIElements.NAMESPACE);
        namespaces.addNamespace(XDIMECPSIElements.NAMESPACE);
        namespaces.addNamespace(XDIMECPInterimSIElements.NAMESPACE);
        namespaces.addNamespace(WidgetElements.NAMESPACE);
        namespaces.addNamespace(XFormElements.NAMESPACE);
        namespaces.addNamespace(XHTML2Elements.NAMESPACE);
        namespaces.addNamespace(XMLEventsElements.NAMESPACE);
        namespaces.addNamespace(TickerElements.NAMESPACE);
        namespaces.addNamespace(TickerResponseElements.NAMESPACE);
        namespaces.addNamespace(GalleryElements.NAMESPACE);
        DEFAULT_INSTANCE = namespaces;
    }

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static SchemaNamespaces getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public XDIME2Elements() {
        addNamespace(MCSElements.NAMESPACE);
        addNamespace(ResponseElements.NAMESPACE);
        addNamespace(SIElements.NAMESPACE);
        addNamespace(XDIMECPSIElements.NAMESPACE);
        addNamespace(XDIMECPInterimSIElements.NAMESPACE);
        addNamespace(WidgetElements.NAMESPACE);
        addNamespace(XFormElements.NAMESPACE);
        addNamespace(XHTML2Elements.NAMESPACE);
        addNamespace(XMLEventsElements.NAMESPACE);
        addNamespace(TickerElements.NAMESPACE);
        addNamespace(TickerResponseElements.NAMESPACE);
        addNamespace(GalleryElements.NAMESPACE);
    }
}
