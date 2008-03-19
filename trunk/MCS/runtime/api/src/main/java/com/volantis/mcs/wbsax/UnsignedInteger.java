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
 * 18-May-03    Geoff           VBM:2003042904 - Created; an integer which may 
 *                              only contain positive values.
 * 19-May-03    Geoff           VBM:2003042904 - Document and clean up the 
 *                              pack/unpack methods.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * An integer which may only contain positive values. The maximum value 
 * stored is {@link Integer#MAX_VALUE} (i.e. 31 bits!).
 */ 
public abstract class UnsignedInteger {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The maximum value we can store.
     */ 
    private int maximum = Integer.MAX_VALUE;

    /**
     * Actual value we are storing, -1 means none defined yet.
     */ 
    private int integer = -1;

    /**
     * Construct an instance of this class.
     * <p>
     * This is protected as this default constructor only makes sense when 
     * called by subclasses.
     */ 
    protected UnsignedInteger() {
    }
    
    /**
     * Set the maximum value that this unsigned integer may store.
     *  
     * @param maximum the max value we may store.
     */ 
    protected void setMaximum(int maximum) {
        if (integer >= 0) {
            throw new IllegalStateException("Attempt to set maximum " + 
                    maximum + " after value has been set.");
        }
        if (maximum < 0) {
            throw new IllegalStateException("Attempt to set negative " + 
                    "maximum " + maximum );
        }
        if (maximum > this.maximum) {
            // Prevent subclasses breaking their parents contracts.
            throw new IllegalStateException("Attempt to override existing " + 
                    "maximum " + this.maximum + " with lesser maximum " + 
                    maximum);
        }
        this.maximum = maximum;
    }

    /**
     * Set the value we are storing. It must be less than the current 
     * allowable maximum value.
     * 
     * @param integer the value to store.
     */ 
    protected void setInteger(int integer) {
        if (integer < 0) {
            throw new IllegalArgumentException("Value " + integer + 
                    " cannot be negative");
        }
        if (integer > maximum) {
            throw new IllegalArgumentException("Value " + integer + 
                    " cannot be larger than " + maximum);
        }
        this.integer = integer;
    }

    /**
     * Return the value we are storing. Will be less than the allowable
     * maximum value.
     * @return the integer value
     */ 
    public int getInteger() {
        if (integer < 0) {
            throw new IllegalStateException(
                    "Attempt to get value when none has been provided.");
        }
        return integer;
    }

    // Inherit javadoc.
    public String toString() {
        return "[uint:" + integer + "]"; 
    }

    // Inherit javadoc.
    public int hashCode() {
	return integer;
    }

    // Inherit javadoc.
    public boolean equals(Object obj) {
	if (obj instanceof UnsignedInteger) {
	    return integer == ((UnsignedInteger)obj).integer;
	}
	return false;
    }
     
    /**
     * Pack the values 0-255 from an "unsigned" integer into a byte; values
     * greater than 0x7F will be stored as negative according to the rules of
     * two's complement arithmetic.
     *  
     * @param uint an "unsigned" integer in the range 0x00-0xFF.
     * @return the "packed" byte in the range -0x80-0x7F.
     */ 
    protected static final byte pack(int uint) {
        return (byte) uint;
    }
    
    /**
     * Unpack a byte into an "unsigned" integer; negative values in 
     * the byte will be converted into values greater than 0x7F according to 
     * the rules of two's complement arithmetic.
     * 
     * @param b the "packed" byte in the range -0x80-0x7F
     * @return an "unsigned" integer in the range 0x00-0xFF.
     */ 
    protected static final int unpack(byte b) {
        return (b & 0xFF);
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 ===========================================================================
*/
