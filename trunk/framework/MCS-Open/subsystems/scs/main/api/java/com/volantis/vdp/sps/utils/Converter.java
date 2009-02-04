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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.vdp.sps.utils;

/**
 * Utiliti class
 * User: rstroz01
 * Date: 2006-01-04
 * Time: 11:11:25
 */
public class Converter {
    /**
     * String represents all hexadecimal digits
     */
    private static String HEX_DIGITS = "0123456789abcdef";

    /**
     * Method convert String representation of hexadecimal number to integer
     *
     * @param hex - String representation of hexadecimal number
     * @return
     * @throws NumberFormatException
     */
    public static int hex2int(final String hex) throws NumberFormatException {
        int value = 0;
        String hexValue = hex.toLowerCase();
        for (int i = (hexValue.length() - 1), j = 0; i >= 0; i--) {
            try {
                value += HEX_DIGITS.indexOf(hexValue.charAt(i)) *
                        (int) Math.pow(16, j);
                j++;
            } catch (IndexOutOfBoundsException ex) {
                throw new NumberFormatException();
            }
        }
        return value;
    }
}
