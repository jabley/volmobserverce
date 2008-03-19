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

package com.volantis.mcs.themes;



/**
 * An enumeration of the different modes in which the CSS Parser can operate.
 */
public class CSSParserMode {

    /**
     * The CSS Parser is strict and will fail if it detects any problems.
     */
    public static final CSSParserMode STRICT = new CSSParserMode("STRICT");

    /**
     * The CSS Parser is lax and will log any problems as a warning but will
     * not fail.
     */
    public static final CSSParserMode LAX = new CSSParserMode("LAX");

    /**
     * The description of the enumeration.
     */
    private final String description;

    /**
     * Initialise.
     *
     * @param description The description of the enumeration.
     */
    private CSSParserMode(String description) {
        this.description = description;
    }

    // Javadoc inherited.
    public String toString() {
        return description;
    }

}
