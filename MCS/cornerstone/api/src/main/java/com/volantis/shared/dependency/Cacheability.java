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

package com.volantis.shared.dependency;

/**
 * An enumeration of the different levels of cacheability of a dependency.
 */
public class Cacheability {
    /**
     * Indicates that the dependency is cacheable.
     */
    public static final Cacheability CACHEABLE = new Cacheability("CACHEABLE");

    /**
     * Indicates that the dependency is not cacheable.
     */
    public static final Cacheability UNCACHEABLE = new Cacheability(
            "UNCACHEABLE");

    /**
     * The name of the enumeration, for debug only.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name  The name of the enumeration.
     */
    private Cacheability(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
