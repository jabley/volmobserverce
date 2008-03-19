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

package com.volantis.mcs.themes.values;

import java.util.Map;
import java.util.HashMap;

public class AngleUnit {

    private static final Map units = new HashMap();

    public static final AngleUnit DEG = add("deg");
    public static final AngleUnit GRAD = add("grad");
    public static final AngleUnit RAD = add("rad");

    private static AngleUnit add(String name) {
        // Units should not be case sensitive.
        name = name.toLowerCase();
        if (units.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate units " + name);
        }

        AngleUnit unit = new AngleUnit(name);
        units.put(name, unit);
        return unit;
    }

    private final String name;

    public AngleUnit(String name) {
        this.name = name.toLowerCase();
    }

    public String toString() {
        return name;
    }

    public static AngleUnit getUnitByName(String text) {
        return (AngleUnit) units.get(text.toLowerCase());
    }

    public static int getCount() {
        return units.size();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
