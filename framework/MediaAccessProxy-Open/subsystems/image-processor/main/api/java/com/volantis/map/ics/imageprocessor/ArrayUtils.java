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
package com.volantis.map.ics.imageprocessor;

/**
 * Class containing helper methods
 */
public class ArrayUtils {

    /**
     * Stop this from being instanciated
     */
    private ArrayUtils() {
    }

    /**
     * Helper method to convert an array of integers into a string.
     *
     * @param data the array to convert
     * @return a string representation of the array.
     */
    public static String asString(int[] data) {
        StringBuffer sb = new StringBuffer();
        if (data == null) {
            sb.append("null");
        } else {
            sb.append("L").append(data.length).append("[");
            for (int i = 0; i < data.length; i++) {
                sb.append(data[i]);
                if (i != (data.length - 1)) {
                    sb.append(", ");
                }
            }

            sb.append("]");
        }
        return sb.toString();
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jan-05	289/1	matthew	VBM:2005012617 Fix problems with transcoding gif/png with transparency and refactor test case

 24-Jan-05	276/1	matthew	VBM:2004121009 Allow transcoding of indexed images that have a transparency

 19-Jan-05	270/1	matthew	VBM:2004121009 Allow correct handling of gif images with transparency

 ===========================================================================
*/
