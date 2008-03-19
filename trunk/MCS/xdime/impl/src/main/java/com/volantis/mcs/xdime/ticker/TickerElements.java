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
 * Defines element types for XDIME2 elements from ticker namespace
 */
public class TickerElements {

    /**
     * The namespace containing all these elements.
     */
    public static final Namespace NAMESPACE =
            new Namespace(XDIMESchemata.TICKER_NAMESPACE, "ticker");

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
    public static final ElementType FEED = getElement("feed");
    public static final ElementType UPDATE_STATUS = getElement("update-status");
    public static final ElementType ITEMS_COUNT = getElement("items-count");
    public static final ElementType CHANNELS_COUNT = getElement("channels-count");
    public static final ElementType ITEM_DISPLAY = getElement("item-display");
    public static final ElementType ITEM_CHANNEL = getElement("item-channel");
    public static final ElementType ITEM_TITLE = getElement("item-title");
    public static final ElementType ITEM_ICON = getElement("item-icon");
    public static final ElementType ITEM_DESCRIPTION = getElement("item-description");
    public static final ElementType ITEM_PLAIN_DESCRIPTION = getElement("item-plain-description");
}
