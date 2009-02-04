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
 * 18-May-03    Geoff           VBM:2003042904 - Created; an unsigned integer 
 *                              which is restricted to representing values 
 *                              which may be stored in single unsigned byte.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * An unsigned integer which is restricted to representing values which may be 
 * stored in single unsigned byte (i.e. 8 bits, up to 0xFF).
 */ 
public class SingleByteInteger extends UnsignedInteger {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Construct an instance of this class with no value.
     * <p>
     * Protected as this may only be called by subclasses.
     */ 
    protected SingleByteInteger() {
        setMaximum(0xFF);
    }
    
    /**
     * Construct an instance of this class with the value provided.
     * <p>
     * Protected as this may only be called by subclasses.
     */ 
    protected SingleByteInteger(int value) {
        this();
        super.setInteger(value);
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
