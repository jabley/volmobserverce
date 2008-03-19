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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes;

/**
 * The representation of a style syntax.
 */
public class StyleSyntax {

    /**
     * The name of the syntax.
     */
    private final String name;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param name of the syntax
     */
    StyleSyntax(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        // Syntaxes should not be case sensitive.
        this.name = name.toLowerCase();
    }

    /**
     * Get the name of the syntax.
     *
     * @return The name of the syntax.
     */
    public String getName() {
        return name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
