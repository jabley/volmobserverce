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

public class LengthUnit {

    private static final Map units = new HashMap();

    public static final LengthUnit MM = add("mm");
    public static final LengthUnit CM = add("cm");
    public static final LengthUnit PT = add("pt");
    public static final LengthUnit PC = add("pc");
    public static final LengthUnit EM = add("em");
    public static final LengthUnit EX = add("ex");
    public static final LengthUnit PX = add("px");
    public static final LengthUnit IN = add("in");

    private static LengthUnit add(String name) {
        // Length units should not be case sensitive.
        name = name.toLowerCase();
        if (units.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate units " + name);
        }

        LengthUnit unit = new LengthUnit(name);
        units.put(name, unit);
        return unit;
    }

    private final String name;

    public LengthUnit(String name) {
        // Length units should not be case sensitive.
        this.name = name.toLowerCase();
    }
    
    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    public static LengthUnit getUnitByName(String text) {
        // Length units should not be case sensitive.
        return (LengthUnit) units.get(text.toLowerCase());
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
