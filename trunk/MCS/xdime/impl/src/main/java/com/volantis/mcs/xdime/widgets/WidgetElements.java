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
 * Defines element types for XDIME2 elements from widget namespace
 */
public class WidgetElements {

    /**
     * The namespace containing all these elements.
     */
    public static final Namespace NAMESPACE =
            new Namespace(XDIMESchemata.WIDGETS_NAMESPACE, "widget");

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

    public static final ElementType REFRESH = getElement("refresh");

    public static final ElementType CAROUSEL = getElement("carousel");

    public static final ElementType POPUP = getElement("popup");

    public static final ElementType DISMISS = getElement("dismiss");

    public static final ElementType TICKER_TAPE = getElement("ticker-tape");

    public static final ElementType PROGRESS = getElement("progress");
    
    public static final ElementType FOLDING_ITEM = getElement("folding-item");

    public static final ElementType SUMMARY = getElement("summary");

    public static final ElementType DETAIL = getElement("detail");

    public static final ElementType LOAD = getElement("load");

    public static final ElementType FETCH = getElement("fetch");    

    public static final ElementType WIZARD = getElement("wizard");

    public static final ElementType LAUNCH = getElement("launch");

    public static final ElementType FIELD_EXPANDER = getElement("field-expander");

    public static final ElementType VALIDATE = getElement("validate");

    public static final ElementType MESSAGE = getElement("message");

    public static final ElementType MULTIPLE_VALIDATOR = getElement("multiple-validator");
    
    public static final ElementType FIELD = getElement("field");

    public static final ElementType AUTOCOMPLETE = getElement("autocomplete");
    
    public static final ElementType TABS = getElement("tabs");
    
    public static final ElementType TAB = getElement("tab");
    
    public static final ElementType LOG = getElement("log");
    
    public static final ElementType NEXT = getElement("next");
    
    public static final ElementType PREVIOUS = getElement("previous");
    
    public static final ElementType PLAY = getElement("play");
    
    public static final ElementType STOP = getElement("stop");
    
    public static final ElementType PAUSE = getElement("pause");
    
    public static final ElementType BUTTON = getElement("button");
    
    public static final ElementType DISPLAY = getElement("display");
    
    public static final ElementType INPUT = getElement("input");
    
    public static final ElementType HANDLER = getElement("handler");
    
    public static final ElementType SCRIPT = getElement("script");

    public static final ElementType MAP = getElement("map");
    
    public static final ElementType MAP_VIEW = getElement("map-view");
    
    public static final ElementType MAP_LOCATION_MARKER = getElement("map-location-marker");
    
    public static final ElementType MAP_LOCATION_MARKERS = getElement("map-location-markers");
    
    public static final ElementType PROPERTY = getElement("property");
    
    public static final ElementType DIGITAL_CLOCK = getElement("digital-clock");
    
    public static final ElementType CLOCK_CONTENT = getElement("clock-content");
    
    public static final ElementType STOPWATCH = getElement("stopwatch");
    
    public static final ElementType TIMER = getElement("timer");
    
    public static final ElementType DATE_PICKER = getElement("date-picker");
    
    public static final ElementType PREVIOUS_MONTH = getElement("previous-month");
    
    public static final ElementType NEXT_MONTH = getElement("next-month");
    
    public static final ElementType MONTH_DISPLAY = getElement("month-display");
    
    public static final ElementType YEAR_DISPLAY = getElement("year-display");
    
    public static final ElementType NEXT_YEAR = getElement("next-year");
    
    public static final ElementType PREVIOUS_YEAR = getElement("previous-year");
    
    public static final ElementType CALENDAR_DISPLAY = getElement("calendar-display");
    
    public static final ElementType SET_TODAY = getElement("set-today");

    public static final ElementType BLOCK = getElement("block");
    
    public static final ElementType BLOCK_CONTENT = getElement("block-content");
    
    public static final ElementType SELECT = getElement("select");

    public static final ElementType OPTION = getElement("option");
    
    public static final ElementType DECK = getElement("deck");
    
    public static final ElementType DECK_PAGE = getElement("deck-page");

    public static final ElementType TABLE = getElement("table");

    public static final ElementType TBODY = getElement("tbody");
}
