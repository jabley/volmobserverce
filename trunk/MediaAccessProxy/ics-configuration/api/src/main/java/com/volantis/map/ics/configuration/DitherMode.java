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
package com.volantis.map.ics.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Type safe enumeration of the supported dithering modes
 */
public class DitherMode {

    /**
     * Map the names of the created enums to thier instances. Note that the
     * position of the map is important. It must be initialized before the enum
     * instances are created
     */
    private static final Map enums = new HashMap();

    /**
     * An NONE instance
     */
    public static final DitherMode NONE = new DitherMode("none");

    /**
     * An FLOYD instance
     */
    public static final DitherMode FLOYD = new DitherMode("floyd-steinberg");

    /**
     * An JARVIS instance
     */
    public static final DitherMode JARVIS = new DitherMode("jarvis");

    /**
     * An STUCKI instance
     */
    public static final DitherMode STUCKI = new DitherMode("stucki");

    /**
     * An PATTERNED instance
     */
    public static final DitherMode PATTERNED = new DitherMode("patterned");

    /**
     * An ordered dither
     */
    public static final DitherMode ORDERED = new DitherMode("ordered-dither");

    /**
     * The name of the enumeration entry
     */
    private final String myName;

    /**
     * Construct a type safe enum instance.
     *
     * @param name the name of the enumeration entry.
     */
    private DitherMode(String name) {
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
     * Return the DitherMode instance corresponding to the specified name.
     *
     * @param name the name of the instance to return.
     * @throws IllegalArgumentException if an enum does not exist for the
     *                                  specified name.
     */
    public static DitherMode literal(String name) {
        DitherMode result = null;
        if (enums.containsKey(name)) {
            result = (DitherMode) enums.get(name);
        } else {
            throw new IllegalArgumentException(
                "Enum '" + name + "' does not exist");
        }
        return result;
    }

    /**
     * deserialize the dither mode
     *
     * @param mode the string version of the mode
     * @return the mode
     */
    public static DitherMode deserialize(String mode) {
        DitherMode result = FLOYD;
        if (null != mode) {
            mode = mode.trim();
            mode = mode.toLowerCase();
            result = literal(mode);
        }
        return result;
    }

    /**
     * serialize the Dither mode
     *
     * @param mode the mode
     * @return the string representation of the dither mode
     */
    public static String serialize(DitherMode mode) {
        String result = FLOYD.myName;
        if (null != mode) {
            result = mode.myName;
        }
        return result;
    }

}
