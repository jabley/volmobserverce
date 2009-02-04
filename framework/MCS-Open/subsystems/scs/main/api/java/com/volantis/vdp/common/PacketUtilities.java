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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.vdp.common;

/**
 *
 */

public class PacketUtilities {


    /**
     * Creates the Integer value from the given data byte array.
     * @param data the int value as the byte array
     * @return converted value
     */
    public static int toInteger(byte[] data) {
        int retVal = 0;
        retVal += (data[0]&255)<<24;
        retVal += (data[1]&255)<<16;
        retVal += (data[2]&255)<<8;
        retVal += (data[3]&255);

        return retVal;
    }

    /**
     * Converts the given Integer to byte array.
     * @param value Integer value
     * @return converted value
     */
    public static byte[] integerToByteArray(int value) {

        byte[] retVal = new byte[4];

        retVal[0] = (byte) ((value>>24)&255);
        retVal[1] = (byte) ((value>>16)&255);
        retVal[2] = (byte) ((value>>8)&255);
        retVal[3] = (byte) (value&255);

        return retVal;
    }

}
