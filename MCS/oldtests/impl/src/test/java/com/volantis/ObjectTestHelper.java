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
 * $Header: /src/voyager/testsuite/unit/com/volantis/ObjectTestHelper.java,v 1.2 2003/03/11 08:54:28 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Mar-03    Allan           VBM:2003021802 - Created. A test helper for 
 *                              Object level tests. 
 * 10-Mar-03    Allan           VBM:2003021801 - Modify testHashCodesNonEqual()
 *                              to increment index after updating 
 *                              previousHashCodes so that previousHashCodes 
 *                              contain the correct entries at the expected 
 *                              indices. 
 * ----------------------------------------------------------------------------
 */
package com.volantis;

import java.util.List;

/**
 * A helper class for testing Object methods that are overriden such as 
 * hashCode().
 */ 
public class ObjectTestHelper {
    /**
     * Test that a Collection of Objects all generate different hashcodes.
     * @param objects The Collection of Objects to test.
     * @return true if all the Objects in the collection have different 
     * hashcodes; false otherwise.
     */ 
    public static boolean testHashCodesNonEqual(List objects) {
        boolean hashCodesEqual = false;
        int previousHashCodes [] = new int[objects.size()];
        
        int index = 0;

        while(index<objects.size() && !hashCodesEqual) {
            int currentHashCode = objects.get(index).hashCode();
            for(int i=0; i<index && !hashCodesEqual; i++) {
                hashCodesEqual = currentHashCode == previousHashCodes[i];

            }

            previousHashCodes[index] = currentHashCode;
            
            index++;
            
        }

        return !hashCodesEqual;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
