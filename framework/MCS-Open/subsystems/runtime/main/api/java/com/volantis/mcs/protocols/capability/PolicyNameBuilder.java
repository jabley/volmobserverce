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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.capability;

/**
 * Utility class for building policy names.
 */
class PolicyNameBuilder {
    /**
     * Prefix for the to the css properties part of the device policy value
     */
    private static final String CSS_PROPERTIES = "x-css.properties";

    /**
     * Prefix for the to the element part of the device policy value
     */
    private static final String ELEMENT_ID = "x-element";

    /**
     * Policy value parameter for showing an item supports something
     */
    private static final String SUPPORTS = "supports";

    /**
     * Policy value parameter for showing an item is supported
     */
    private static final String SUPPORTED = "supported";

    /**
     * Infix for the CSS policy keywords
     */
    private static final String KEYWORD = "keyword";

    /**
     * Policy value parameter for showing an item supports style property keyword
     */
    private static final String SUPPORT = "support";

    static String elementSupported(String element) {
        return ELEMENT_ID + "." + element + "." + SUPPORTED;
    }

    static String elementSupportsStyleProperty(String element, String property) {
        return ELEMENT_ID + "." + element + "."
                + SUPPORTS + ".css." + property;
    }

    static String elementSupportsAttribute(String element, String attribute) {
        return ELEMENT_ID + "." + element + "."
                + SUPPORTS + ".attribute." + attribute;
    }

    static String styleKeywordSupport(String property, String keyword) {
        return CSS_PROPERTIES + "." + property + "."
                + KEYWORD + "." + keyword + "." + SUPPORT;
    }
}
