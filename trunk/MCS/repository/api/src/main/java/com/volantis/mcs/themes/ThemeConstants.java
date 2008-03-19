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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes;

/**
 * A class containing some useful constant values for themes.
 */
public final class ThemeConstants {
    /**
     * Private constructor to prevent instantiation
     */
    private ThemeConstants() {
    }

    /**
     * The universal selector.
     */
    public static final String UNIVERSAL_SELECTOR = "*";

    /**
     * List of allowable type names.
     */
    public static final String[] TYPE_LIST = {
        "a",
        "abbr",
        "address",
        "audio",
        "b",
        "big",
        "blockquote",
        "body",
        "br",
        "canvas",
        "caption",
        "chart",
        "cite",
        "code",
        "dd",
        "dfn",
        "div",
        "dl",
        "dt",
        "dynvis",
        "em",
        "group",
        "h1",
        "h2",
        "h3",
        "h4",
        "h5",
        "h6",
        "head",
        "hr",
        "html",
        "i",
        "img",
        "kbd",
        "label",
        "li",
        "logo",
        "menu",
        "menuitem",
        "menuitemgroup",
        "meta",
        "mmflash",
        "montage",
        "nl",
        "noscript",
        "object",
        "ol",
        "p",
        "pane",
        "param",
        "phonenumber",
        "pre",
        "quicktime",
        "quote",
        "realaudio",
        "realvideo",
        "region",
        "samp",
        "small",
        "span",
        "strong",
        "sub",
        "sup",
        "table",
        "tbody",
        "td",
        "tfoot",
        "th",
        "thead",
        "title",
        "tr",
        "tt",
        "u",
        "ul",
        "var",
        "winaudio",
        "winvideo",

        // Form elements.
        "xfaction",
        "xfboolean",
        "xfcontent",
        "xfform",
        "xfmuselect",
        "xfoptgroup",
        "xfoption",
        "xfsiselect",
        "xftextinput",
        "xfupload",
        "model",
        "submission",
        "instance",
        "input",
        "item",
        "label",
        "value",
        "secret",
        "select",
        "select1",
        "submit",
        "textarea",

        // Widget elements.
        "carousel",
        "detail",
        "dismiss",
        "field-expander",
        "folding-item",
        "launch",
        "load",
        "message",
        "popup",
        "progress",
        "refresh",
        "summary",
        "ticker-tape",
        "validate",
        "WidgetLoad",
        "WidgetRefresh",
        "wizard",
        "autocomplete",
        "tab",
        "tabs",
        "date-picker",
        "next-month",
        "next-year",
        "previous-year",
        "previous-month",
        "set-today",
        "month-display",
        "year-display",
        "calendar-display",
        "next",
        "previous",
        "play",
        "stop",
        "pause",
        "property",
        "button",
        "input",
        
        // Ticker elements
        "feed",
        "update-status",
        "items-count",
        "channels-count",
        "item-display",
        "item-channel",
        "item-title",
        "item-icon",
        "item-description",

        // Ticker response elements
        "title",
        "icon",
        "description",
        
        // Gallery elements
        "item-display",
        "item-number",
        "items-count",
        "start-item-number",
        "end-item-number",
        "page-number",
        "pages-count",
        "gallery",
        "slideshow",
    };

    public static final String[] DEPRECATED_TYPES = {
        "winaudio",
        "winvideo"
    };
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Nov-05	10195/1	adrianj	VBM:2005101803 Display deprecated elements in selector wizard

 07-Nov-05	10179/2	adrianj	VBM:2005101803 Show deprecated elements in selector group wizard

 07-Nov-05	10175/1	adrianj	VBM:2005110437 Validation in incremental builder

 07-Nov-05	10150/3	adrianj	VBM:2005110437 Validation in incremental builder

 04-Nov-05	10145/1	adrianj	VBM:2005101804 Add XDIME2 elements to selector wizard

 04-Nov-05	10129/1	adrianj	VBM:2005101804 Added XDIME CP elements for selector wizard

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
