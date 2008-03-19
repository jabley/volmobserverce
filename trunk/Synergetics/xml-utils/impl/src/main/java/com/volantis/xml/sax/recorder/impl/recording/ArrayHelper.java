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

package com.volantis.xml.sax.recorder.impl.recording;



/**
 * Contains methods for manipulating arrays of various types.
 */
public class ArrayHelper {

    /**
     * Expand the array.
     *
     * @param array The array to expand.
     * @param newSize The new size of the array.
     *
     * @return The new array containing the contents of the old array.
     */
    public static char[] expandArray(char[] array, int newSize) {
        char[] newArray = new char[newSize];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    /**
     * Expand the array.
     *
     * @param array The array to expand.
     * @param newSize The new size of the array.
     *
     * @return The new array containing the contents of the old array.
     */
    public static int[] expandArray(int[] array, int newSize) {
        int[] newArray = new int[newSize];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    /**
     * Copy the array.
     *
     * @param array The array to copy.
     * @param count The number of cells to copy.
     *
     * @return The new array.
     */
    public static char[] copyArray(char[] array, int count) {
        char[] newArray = new char[count];
        System.arraycopy(array, 0, newArray, 0, count);
        return newArray;
    }

    /**
     * Copy the array.
     *
     * @param array The array to copy.
     * @param count The number of cells to copy.
     *
     * @return The new array.
     */
    public static int[] copyArray(int[] array, int count) {
        int[] newArray = new int[count];
        System.arraycopy(array, 0, newArray, 0, count);
        return newArray;
    }
}
