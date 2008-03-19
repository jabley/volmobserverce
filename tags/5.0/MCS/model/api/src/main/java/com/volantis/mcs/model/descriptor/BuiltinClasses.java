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

package com.volantis.mcs.model.descriptor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BuiltinClasses {

    private static final Set PRIMITIVE_CLASSES;
    private static final Set IMMUTABLE_CLASSES;

    private static final Map PRIMITIVE_2_AUTOBOX;

    private static final Set PRIMITIVE_OR_IMMUTABLE;

    static {
        Set set;

        set = new HashSet();
        set.add(Boolean.TYPE);
        set.add(Character.TYPE);
        set.add(Double.TYPE);
        set.add(Float.TYPE);
        set.add(Integer.TYPE);
        set.add(Long.TYPE);
        set.add(Short.TYPE);
        PRIMITIVE_CLASSES = set;

        Map map = new HashMap();
        map.put(Boolean.TYPE, Boolean.class);
        map.put(Character.TYPE, Character.class);
        map.put(Double.TYPE, Double.class);
        map.put(Float.TYPE, Float.class);
        map.put(Integer.TYPE, Integer.class);
        map.put(Long.TYPE, Long.class);
        map.put(Short.TYPE, Short.class);
        PRIMITIVE_2_AUTOBOX = map;

        set = new HashSet();
        set.add(Boolean.class);
        set.add(Character.class);
        set.add(Double.class);
        set.add(Float.class);
        set.add(Integer.class);
        set.add(Long.class);
        set.add(Short.class);
        set.add(String.class);
        IMMUTABLE_CLASSES = set;

        PRIMITIVE_OR_IMMUTABLE = new HashSet();
        PRIMITIVE_OR_IMMUTABLE.addAll(PRIMITIVE_CLASSES);
        PRIMITIVE_OR_IMMUTABLE.addAll(IMMUTABLE_CLASSES);
    }

    public static boolean isPrimitiveClass(Class clazz) {
        return PRIMITIVE_CLASSES.contains(clazz);
    }

    public static boolean isImmutableClass(Class clazz) {
        return IMMUTABLE_CLASSES.contains(clazz);
    }

    public static Class primitive2AutoBoxType(Class primitiveClass) {
        final Class autoBox = (Class) PRIMITIVE_2_AUTOBOX.get(primitiveClass);
        if (autoBox == null) {
            throw new IllegalArgumentException(
                    "Unknown primitive " + primitiveClass);
        }
        return autoBox;
    }

    public static boolean isPrimitiveOrAutoBoxingClass(Class typeClass) {
        return PRIMITIVE_OR_IMMUTABLE.contains(typeClass);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 31-Oct-05	9961/12	pduffin	VBM:2005101811 Committing restructuring

 28-Oct-05	9886/2	adrianj	VBM:2005101811 New theme GUI

 26-Oct-05	9961/7	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/5	pduffin	VBM:2005101811 Fixed issue with diagnostics and improved user interface to allow opening of files

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
