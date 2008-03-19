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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-May-03    Geoff           VBM:2003042904 - Created; a test case for 
 *                              UnsignedInteger.
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link UnsignedInteger}.
 * <p>
 * At the moment we only test the pack method.
 */ 
public class UnsignedIntegerTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public UnsignedIntegerTestCase(String s) {
        super(s);
    }

    /**
     * See http://scholar.hw.ac.uk/site/computing/activity11.asp for 
     * a nice explanation of two's complement arithmetic.
     */ 
    public void testPack() {
        int unpackedInt;
        byte packedByte;
        
        /**
         * int:  0000 0000 =  0x00
         * byte: 0000 0000 =  0x00 
         */
        unpackedInt = 0x00;
        packedByte = 0x00;
        check(unpackedInt, packedByte);
        
        /**
         * int:  0000 0001 =  0x01
         * byte: 0000 0000 =  0x01 
         */
        unpackedInt = 0x01;
        packedByte = 0x01;
        check(unpackedInt, packedByte);
        
        /**
         * int:  0111 1111 =  0x7F
         * byte: 0111 1111 =  0x7F 
         */
        unpackedInt = 0x7F;
        packedByte = 0x7F;
        check(unpackedInt, packedByte);

        /**
         * int:  1000 0000 =  0x80
         *       0111 1111    (invert)
         *       1000 0000    (+1)
         * byte: 1000 0000 = -0x80 
         */
        unpackedInt = 0x80;
        packedByte = -0x80;
        check(unpackedInt, packedByte);
        
        /**
         * int:  1111 1111 =  0xFF
         *       0000 0000    (invert)
         *       0000 0001    (+1)
         * byte: 1000 0001 = -0x01 
         */
        unpackedInt = 0xFF;
        packedByte = -0x01;
        check(unpackedInt, packedByte);

    }
    
    private void check(int unpackedInt, byte packedByte) {
        byte actualPackedByte = UnsignedInteger.pack(unpackedInt);
        assertEquals(packedByte, actualPackedByte);
        
        int actualUnpackedInt = UnsignedInteger.unpack(packedByte);
        assertEquals(unpackedInt, actualUnpackedInt);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Jul-03	709/1	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/1	geoff	VBM:2003070209 clean up WBSAX test cases

 ===========================================================================
*/
