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

package com.volantis.mcs.policies.impl;

/**
 * Provides helpful methods for implementing equals and hashCode methods.
 */
public class EqualsHashCodeHelper {

    public static boolean equals(boolean b1, boolean b2) {
        boolean equals = (b1 == b2);
        return equals;
    }

    public static boolean equals(int i1, int i2) {
        boolean equals = (i1 == i2);
        return equals;
    }

    public static boolean equals(Object o1, Object o2) {
        boolean equals = (o1 == null ? o2 == null : o1.equals(o2));
        return equals;
    }

    public static int hashCode(int result, boolean b) {
        return 37 * result + (b ? 1 : 0);
    }

    public static int hashCode(int result, int i) {
        return 37 * result + i;
    }

    public static int hashCode(int result, Object o) {
        return 37 * result + (o == null ? 0 : o.hashCode());
    }

    public static int getValue(Integer value) {
        return value == null ? 0 : value.intValue();
    }

    public static boolean getValueFromOptionalBoolean(Boolean value) {
        return value == null ? false : value.booleanValue();
    }

    public static Integer getInteger(int value) {
        return new Integer(value);
    }

    public static Boolean getBoolean(boolean value) {
        return value ? Boolean.TRUE : Boolean.FALSE;
    }
}
