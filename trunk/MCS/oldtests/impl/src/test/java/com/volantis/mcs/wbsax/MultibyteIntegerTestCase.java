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
 *                              MultiByteInteger.
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link MultiByteInteger}.
 */ 
public class MultibyteIntegerTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public MultibyteIntegerTestCase(String s) {
        super(s);
    }

    // Test some expected results from the spec.
    
    public void testCreateA0() {
        checkCreate(0xA0, new int[] {0x81, 0x20});
    }

    public void testCreate60() {
        checkCreate(0x60, new int[] {0x60});
    }
    
    // Bounds checking.
    
    public void testUnderflow() {
        checkCreateFail(-1, new int[] {-1});
    }
    
    public void testMin() {
        checkCreate(0x0, new int[] {0x0});
    }

    public void testMax() {
        checkCreate(0x0FFFFFFF, new int[] {0xFF, 0xFF, 0xFF, 0x7F});
    }

    public void testOverflow() {
        checkCreateFail(0x1FFFFFFF, new int[] {0xFF, 0xFF, 0xFF, 0x8F});
    }
    
    private void checkCreate(int value, int[] unpackedBytes) {
        MultiByteInteger valueInteger = new MultiByteInteger(value);
        assertEquals(value, valueInteger.getInteger());
        byte[] packedActualBytes = valueInteger.getBytes();
        int[] unpackedActualBytes = unpack(packedActualBytes);
        assertEquals(unpackedBytes, unpackedActualBytes);
        
//        MultiByteInteger byteInteger = new MultiByteInteger(bytes);
//        assertEquals(value, byteInteger.getInteger());
//        assertEquals(bytes, byteInteger.getBytes());
    }

    private int[] unpack(byte[] bytes) {
        int[] ints = new int[bytes.length];
        for (int i=0; i < bytes.length; i++) {
            ints[i] = MultiByteInteger.unpack(bytes[i]); 
        }
        return ints;
    }

    private void checkCreateFail(int value, int[] bytes) {
        try {
            new MultiByteInteger(value);
            fail("value should be illegal: " + value);
        } catch (IllegalArgumentException e) {
            // Expected, continue.
        }
//        try {
//            new MultiByteInteger(bytes);
//            fail("bytes should be illegal: " + new ArrayObject(bytes));
//        } catch (IllegalArgumentException e) {
//            // Expected, continue.
//        }
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
