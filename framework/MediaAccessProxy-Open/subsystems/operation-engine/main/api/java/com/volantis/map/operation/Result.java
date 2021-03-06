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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.operation;

import java.util.HashMap;
import java.util.Map;

/**
 * Type safe enumeration of operation results.
 *
 */
public class Result {

    /**
     * Map the names of the created enums to thier instances. Note that the
     * position of the map is important. It must be initialized before the enum
     * instances are created
     */
    private static final Map enums = new HashMap();

    /**
     * An SUCCESS result
     */
    public static final Result SUCCESS = new Result("SUCCESS");

    /**
     * An UNSUPPORTED result
     */
    public static final Result UNSUPPORTED = new Result("UNSUPPORTED");
    
    /**
     * The name of the enumeration entry
     */
    private final String myName;

    /**
     * Construct a type safe enum instance.
     *
     * @param name the name of the enumeration entry.
     */
    private Result(String name) {
        myName = name;
        if (enums.containsKey(name)) {
            throw new IllegalArgumentException(
                "Enum '" + name + "' already exists");
        }
        enums.put(name, this);
    }

    /**
     * Return the name of the enumeration entry
     */
    public String toString() {
        return myName;
    }

    /**
     * Return the Result instance corresponding to the specified name.
     *
     * @param name the name of the instance to return.
     * @throws IllegalArgumentException if an enum does not exist for the
     *                                  specified name.
     */
    public static Result literal(String name) {
        Result result = null;
        if (enums.containsKey(name)) {
            result = (Result) enums.get(name);
        } else {
            throw new IllegalArgumentException(
                "Enum '" + name + "' does not exist");
        }
        return result;
    }

}
