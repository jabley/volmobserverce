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
 * 18-May-03    Geoff           VBM:2003042904 - Created; represents a 
 *                              multi-byte integer in a WBSAX event stream.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents a multi-byte integer in a WBSAX event stream.
 * <p>
 * <b>NOTE: this only goes up to a maximum of 28 bits, rather than the 32 bits 
 * that the specification mandates for "mb_u_int32" multi-byte integers.</b>
 * <p>
 * This is because a Java 32 bit int uses the top bit to represent the sign for
 * negative numbers (two's complement binary storage). This leaves you with 31 
 * bits between 0 and {@link Integer#MAX_VALUE}. 
 * <p>
 * I don't anticipate needing all 32 bits at this stage, so this will do for 
 * now. A more general implementation might use the following to implement
 * a full 32 bit multi-byte integer:
 * <ol>
 *   <li>Store multi-byte integers as longs. This may be slow.
 *   <li>Store multi-byte integers in ints, where the negative range is used
 *      to represent large positive numbers. This is tricky as you have to 
 *      remember that these ints are not "ordinary" ints and you have to 
 *      convert them back and forwards to longs to apply normal aritmetic and
 *      comparison operators to them (or do everything in binary).
 * </ol>
 * @todo extend to 5 byte / 31 bit processing rather than 4 byte / 28 bit.
 */ 
public class MultiByteInteger extends UnsignedInteger {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd ${YEAR}.";

    /**
     * The smallest value greater than the maximum we can store in 4 bytes.
     */ 
    private static int BIT_28 = 1 << (7 * 4);

    /**
     * The smallest value greater than the maximum we can store in 3 bytes.
     */ 
    private static int BIT_22 = 1 << (7 * 3);

    /**
     * The smallest value greater than the maximum we can store in 2 bytes.
     */ 
    private static int BIT_15 = 1 << (7 * 2);

    /**
     * The smallest value greater than the maximum we can store in 1 byte.
     */ 
    private static int BIT_8  = 1 << (7 * 1);
    
    
    /**
     * The encoded format of the multi-byte integer. 
     */ 
    byte [] bytes;

    /**
     * Constructs an instance of this class without setting it's value.
     * <p>
     * This is protected, as it is for subclasses. Those subclasses must call
     * {@link #setInteger} to set the value.
     */ 
    protected MultiByteInteger() {
        // Can't encode int's bigger than 28 bits. If we want to, 
        // we'd need to use long or always remember that the the value
        // of the int is stored in 2's complement form. 
        setMaximum(BIT_28 - 1);
    }

    /**
     * Constructs an instance of this class to represent the integer value
     * supplied.
     *  
     * @param value the integer value to represent, must be 0 or positive, and
     *      cannot be greater than or equal to 28 bits.
     */ 
    public MultiByteInteger(int value) {
        this();
        setInteger(value);
    }

    /**
     * Returns the value of this multi byte integer, encoded in mb_u_int32
     * format.
     * 
     * @return the value of this multi byte integer, encoded in mb_u_int32
     * format.
     * 
     * @todo this may be faster as a writeTo(OutputStream) method (no
     * need to create a byte[]).
     */ 
    public byte[] getBytes() {
        if (bytes == null) {
            byte[] buffer;
            int maxBytes;
            int value = getInteger();
            if (value < BIT_8) {
                maxBytes = 0;
            } else if (value < BIT_15) {
                maxBytes = 1;
            } else if (value < BIT_22) {
                maxBytes = 2;
            } else if (value < BIT_28) {
                maxBytes = 3;
            } else {
                // Should never get this.
                throw new IllegalStateException();
            }
            buffer = new byte[maxBytes + 1];
            if (maxBytes >= 1) {
                buffer[maxBytes] = pack(value & 0x7F);
                value = value >> 7;
                for (int i = maxBytes - 1; i >= 1; i--) {
                    buffer[i] = pack((value & 0x7F) | 0x80);
                    value = value >> 7;
                }
                buffer[0] = pack(value | 0x80);
            } else {
                buffer[0] = pack(value);
            }

            this.bytes = buffer;
        }
        return bytes;
    }
    

//    Alternative version of above
//    // Here I have unwound the normal loop that we would use. 
//    if (value < BIT_8) {
//        buffer = new int[1];
//        buffer[0] = value;
//    } else if (value < BIT_15) {
//        buffer = new int[2];
//        buffer[1] = (value & 0x7F);
//        value = value >> 7;
//        buffer[0] = value | 0x80;
//    } else if (value < BIT_22) {
//        buffer = new int[3];
//        buffer[2] = (value & 0x7F);
//        value = value >> 7;
//        buffer[1] = (value & 0x7F) | 0x80;
//        value = value >> 7;
//        buffer[0] = value | 0x80;
//    } else if (value < BIT_28) {
//        buffer = new int[4];
//        buffer[3] = (value & 0x7F);
//        value = value >> 7;
//        buffer[2] = (value & 0x7F) | 0x80;
//        value = value >> 7;
//        buffer[1] = (value & 0x7F) | 0x80;
//        value = value >> 7;
//        buffer[0] = value | 0x80;
//    } else {
//    }


//    // NOTE: some possibly useful example code for creating from the encoded
//    // representation. This would need to be used in a Factory object rather
//    // than here anyway.
//    public MultiByteInteger(int[] buffer) {
//        int length = buffer.length;
//        if (length <= 0 || length > 4) {
//            throw new IllegalArgumentException(
//                    "Multibyte byte array " + new ArrayObject(buffer) + 
//                    " has invalid length: " + buffer.length);
//        }
//        
//        this.bytes = buffer;
//        // Decode the (up to) 4 bytes into 28 bits, by removing 1 bit 
//        // continuation from each byte and concatenating the rest together.
//        // We start with the most significant byte and shift left.
//        int value = 0;
//        int eightBits = 0; 
//        for (int i = 0; i < length; i++) {
//            eightBits = buffer[i];
//            value += (eightBits & 0x7F);
//            if ((eightBits & 0x80) != 0x80) {
//                break;
//            } else {
//                value = value << 7;
//            }
//        }
//        if ((eightBits & 0x80) == 0x80) {
//            throw new IllegalArgumentException(
//                    "Multibyte byte array is not terminated: " + 
//                        new ArrayObject(buffer));
//        }
//        if ((value & 0xFFFFFFFF) != value) {
//            // We only support up to 32 bit integers
//            throw new IllegalArgumentException("Multibyte value " + value + 
//                    " invalid");
//        }
//        this.value = value;
//    }

    
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
