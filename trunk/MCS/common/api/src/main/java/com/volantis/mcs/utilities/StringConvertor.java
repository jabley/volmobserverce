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
 * $Header: /src/voyager/com/volantis/mcs/utilities/StringConvertor.java,v 1.5 2003/01/17 09:32:16 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Feb-02    Allan           VBM:2002022007 - Created. Provide fast 
 *                              conversion from numbers to strings.
 * 23-Aug-02    Steve           VBM:2002082304 - Added lowerCase, upperCase
 *                              and capitalise string conversion methods.
 * 06-Jan-02    Steve           VBM:2002082304 - Removed the above methods.
 * 16-Jan-03    Steve           VBM:2002121208 - Modified valueOf to handle 
 *                              negative numbers.
 *                              Also decreased the size of the number storage
 *                              to 4 characters. 100 is a bit over the top to
 *                              hold a 4 digit number.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.utilities;

/**
 * Provide a mechanism to efficiently convert a non-String (just a range of  
 * ints currently) to a String.
 */ 
public class StringConvertor {

    /**
     * The default array size for integer Strings.
     */
    public static final int DEFAULT_INT_ARRAY_SIZE = 2000;

    /**
     * The array of integer Strings.
     */
    private static String integers[];

    static {
        // Create and populate the integer array
        integers = new String[DEFAULT_INT_ARRAY_SIZE];
        
        for(int i=0; i<integers.length; i++) {
            integers[i] = String.valueOf(i).intern();
        }
    }

    /**
     * Return the String value of an int.
     * @param value the integer to convert into a String
     * @return the String representation of i in base 10
     */
     public static String valueOf(int value ) {
         int absValue = ( value < 0 ) ? -value : value;
         if( absValue < integers.length) {
             if( value >= 0 ){
                 return integers[absValue];
             } else {
                 return "-" + integers[absValue];
             }
         } else {
             return String.valueOf( value );
          }
      }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
