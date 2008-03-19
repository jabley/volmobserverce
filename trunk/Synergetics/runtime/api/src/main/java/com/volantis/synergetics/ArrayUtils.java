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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics;

/**
 * Utility class for arrays.
 */
public final class ArrayUtils {

    /**
     * An empty array for when there are no children.
     */
    public static final Object[] EMPTY_ARRAY = new Object[0];

    /**
     * Private constructor so this class cannot be instantiate outside.
     */
    private ArrayUtils() {
    }

    /**
     * Overload toString() to provide some simple additional formatting if
     * required.
     *
     * This is kind of like the opposite of a StringTokenizer. It takes an
     * array and returns String of the toString() value of each array Object
     * separated by a given separator.
     *
     * @param array       the array
     * @param separator   the separator
     * @param quoteValues if true each value in the array will be enclosed in
     *                    "" characters in the returned value.
     * @return the String of the given array
     */
    public static String toString(Object[] array, String separator,
                                  boolean quoteValues) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(separator);
            }
            if (quoteValues) {
                buffer.append("\"");
            }
            buffer.append(array[i].toString());
            if (quoteValues) {
                buffer.append("\"");
            }
        }
        return buffer.toString();
    }

    public static int[] add(int[] a, char[] b) {
        int[] result = new int[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        for (int i = 0; i < b.length; i++) {
            result[a.length + i] = b[i];
        }
        // equivalent to 
        // System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static int[] add(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static int[] join(int[] a, int b) {
        int[] result = new int[a.length + 1];
        System.arraycopy(a, 0, result, 0, a.length);
        result[a.length] = b;
        return result;
    }

    public static int[] grow(int[] array, int growBy) {
        int[] result = new int[array.length + growBy];
        System.arraycopy(array, 0, result, 0, array.length);
        return result;
    }

    public static String toString(int[] array) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append(array[i]);
        }
        buffer.append("]");
        return buffer.toString();
    }

    public static String toString(byte[] array) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append(array[i]);
        }
        buffer.append("]");
        return buffer.toString();
    }

    public static String toString(char[] array) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append(array[i]);
        }
        buffer.append("]");
        return buffer.toString();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	491/1	allan	VBM:2005062308 Move ArrayUtils into Synergetics and add a new toString

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/1	allan	VBM:2004070608 Device search

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
