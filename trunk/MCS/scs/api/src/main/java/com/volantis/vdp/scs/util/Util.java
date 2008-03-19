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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.vdp.scs.util;

/**
 * Util class contains a helpful methods.
 */
public class Util {

    /**
     * Checks if given parameter is digit.
     * @param number parameter that can be digit
     * @return true if number is digit or false if not
     */
    public static boolean isDigit(String number) {
        boolean retVal = false;
        char zero = '0';

        int i = 0;
        while( i < number.length() ) {
            if( (number.charAt(i) - zero) < 0 ||
                    (number.charAt(i) - zero) > 9 ) {
                    break;
            }
            i++;
        }
        if( i >0 && i == number.length() ) retVal = true;

        return retVal;
    }
}
