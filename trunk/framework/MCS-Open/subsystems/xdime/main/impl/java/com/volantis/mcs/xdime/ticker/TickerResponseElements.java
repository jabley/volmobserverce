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

package com.volantis.mcs.xdime.ticker;


import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xml.schema.model.Namespace;
import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * Defines element types for XDIME2 elements from ticker/response namespace
 */
public class TickerResponseElements {

    /**
     * The namespace containing all these elements.
     */
    public static final Namespace NAMESPACE =
            new Namespace(XDIMESchemata.TICKER_RESPONSE_NAMESPACE);

    /**
     * Get the element type for the specified local name in the current
     * namespace.
     *
     * @param localName The local name of the element.
     * @return The element type.
     */
    private static ElementType getElement(String localName) {
        return NAMESPACE.addElement(localName);
    }

    /*
     * Add entry for each XDIME2 element needed by your widget, in the following
     * form: public static final ElementType ELEMENT_NAME =
     * getElement("element_name");
     */

    public static final ElementType FEED_POLLER = getElement("feed-poller");
    public static final ElementType ADD_ITEM = getElement("add-item");
    public static final ElementType REMOVE_ITEM = getElement("remove-item");
    public static final ElementType TITLE = getElement("title");
    public static final ElementType ICON = getElement("icon");
    public static final ElementType DESCRIPTION = getElement("description");
    public static final ElementType PLAIN_DESCRIPTION = getElement("plain-description");
    public static final ElementType SET_URL = getElement("set-url");
    public static final ElementType SET_SKIP_TIMES = getElement("set-skip-times");
    public static final ElementType SKIP_TIME = getElement("skip-time");
    public static final ElementType SET_POLLING_INTERVAL = getElement("set-polling-interval");
}
