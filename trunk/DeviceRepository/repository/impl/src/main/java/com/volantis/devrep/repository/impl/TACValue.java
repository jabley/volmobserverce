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
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.impl;

/**
 * Wrapper object for a long TAC value.
 */
public class TACValue {
    /**
     * The value of the TAC
     */
    private long tac;

    /**
     * Initialises a TACValue object with a TAC represented by a primitive
     * long value.
     *
     * @param value The new instance's TAC
     */
    public TACValue(long value) {
        tac = value;
    }

    /**
     * Returns the TAC as a primitive long value.
     *
     * @return the value of the TAC
     */
    public long getLongTAC() {
        return tac;
    }

    // Javadoc inherited
    public boolean equals(Object obj) {
        if (obj instanceof TACValue) {
            return tac == ((TACValue)obj).getLongTAC();
	    }
    	return false;
    }

    // Javadoc inherited
    public int hashCode() {
        return (int)(tac ^ (tac >> 32));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Aug-04	5121/1	adrianj	VBM:2004080203 Implementation of public API methods for lookup by TAC

 ===========================================================================
*/
