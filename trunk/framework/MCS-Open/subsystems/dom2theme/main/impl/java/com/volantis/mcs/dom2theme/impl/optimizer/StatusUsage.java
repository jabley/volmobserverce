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

package com.volantis.mcs.dom2theme.impl.optimizer;

/**
 * Indicates what usage will be made of the status returned by
 * {@link PropertyClearerChecker}.
 */
public class StatusUsage {

    /**
     * The status will be used for optimizing of an individual property.
     */
    public static final StatusUsage INDIVIDUAL = new StatusUsage("INDIVIDUAL");

    /**
     * The status will be used for optimizing of a shorthand.
     */
    public static final StatusUsage SHORTHAND = new StatusUsage("SHORTHAND");

    /**
     * The name of the enumeration.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name of the enumeration.
     */
    private StatusUsage(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
