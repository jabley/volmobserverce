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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting;

import java.util.HashMap;
import java.util.Map;

/**
 * Type safe enumeration of the status codes passed to the {@link Report#stop}
 * methods.
 * 
 * @volantis-api-include-in PublicAPI
 */
public class Status {

    /**
     * Map the names of the created enums to thier instances. Note that the
     * position of the map is important. It must be initialized before the enum
     * instances are created
     */
    private static final Map enums = new HashMap();

    /**
     * Indicates successful termination of the report.
     */
    public static final Status SUCCESS = new Status("SUCCESS");

    /**
     * Indicates unsuccessful termination of the report.
     */
    public static final Status FAILURE = new Status("FAILURE");

    /**
     * Indicates aborted reporting.
     */
    public static final Status ABORT = new Status("ABORT");

    /**
     * Indicates an otherwise unspecified termination of the report.
     */
    public static final Status UNKNOWN = new Status("UNKNOWN");

    /**
     * The name of the enumeration entry
     */
    private final String myName;

    /**
     * Construct a type safe enum instance.
     *
     * @param name the name of the enumeration entry.
     */
    private Status(String name) {
        myName = name;
        if (enums.containsKey(name)) {
            throw new IllegalArgumentException(
                "Enum '" + name + "' already exists");
        }
        enums.put(name, this);
    }

    /**
     * Returns the name of the enumeration literal.
     *
     * @return the name of the enumeration literal as a string
     */
    public String toString() {
        return myName;
    }

    /**
     * Return the Status instance corresponding to the specified name.
     *
     * @param name the name of the instance to return.
     * @throws IllegalArgumentException if a literal does not exist with the
     *                                  specified name.
     */
    public static Status literal(String name) {
        Status result = null;
        if (enums.containsKey(name)) {
            result = (Status) enums.get(name);
        } else {
            throw new IllegalArgumentException(
                "Enum '" + name + "' does not exist");
        }
        return result;
    }

}
