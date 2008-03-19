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
package com.volantis.mcs.themes;

import com.volantis.styling.properties.StyleProperty;

/**
 * The representation of the style shorthand.
 */
public class StyleShorthand {

    /**
     * The name of the shorthand.
     */
    private final String name;

    /**
     * The standard set of properties that make up the shorthand.
     */
    private final StyleProperty[] standardProperties;

    /**
     * Initialise.
     *
     * @param name               The name of the shorthand.
     * @param standardProperties The properties.
     */
    StyleShorthand(String name, StyleProperty[] standardProperties) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (standardProperties == null) {
            throw new IllegalArgumentException(
                    "standardProperties cannot be null");
        }
        // Shorthands should not be case sensitive.
        this.name = name.toLowerCase();
        this.standardProperties = standardProperties;
    }

    /**
     * Get the name of the shorthand.
     *
     * @return The name of the shorthand.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the standard properties of the shorthand.
     *
     * @return The standard properties of the shorthand.
     */
    public StyleProperty[] getStandardProperties() {
        return standardProperties;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10829/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 ===========================================================================
*/
