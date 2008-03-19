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

package com.volantis.mcs.xdime.widgets;


import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xml.schema.model.Namespace;
import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * Defines element types for XDIME2 elements from widget/response namespace
 */
public class ResponseElements {

    /**
     * The namespace containing all these elements.
     */
    public static final Namespace NAMESPACE =
            new Namespace(XDIMESchemata.RESPONSE_NAMESPACE);

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

    public static final ElementType RESPONSE = getElement("response");
    public static final ElementType BODY = getElement("body");
    public static final ElementType HEAD = getElement("head");
    public static final ElementType LINK = getElement("link");
    public static final ElementType CAROUSEL = getElement("carousel");
    public static final ElementType CLOCK = getElement("clock");
    public static final ElementType TIMER = getElement("timer");
    public static final ElementType TICKER_TAPE = getElement("ticker-tape");
    public static final ElementType PROGRESS = getElement("progress");
    public static final ElementType FOLDING_ITEM = getElement("folding-item");
    public static final ElementType VALIDATION = getElement("validation");
    public static final ElementType FIELD = getElement("field");
    public static final ElementType MESSAGE = getElement("message");
    public static final ElementType AUTOCOMPLETE = getElement("autocomplete");
    public static final ElementType TAB = getElement("tab");
    public static final ElementType DATE_PICKER = getElement("date-picker");
    public static final ElementType MAP = getElement("map");
    public static final ElementType PRESENTER = getElement("presenter");
    public static final ElementType DECK = getElement("deck");
    public static final ElementType TABLE_BODY = getElement("tbody");
    public static final ElementType ERROR = getElement("error");
}
