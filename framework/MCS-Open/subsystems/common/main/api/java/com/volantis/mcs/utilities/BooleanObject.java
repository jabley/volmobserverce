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
 * $Header: /src/voyager/com/volantis/mcs/utilities/BooleanObject.java,v 1.1 2002/06/05 09:26:30 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-02    Allan           VBM:2002030615 - Created. An object that has
 *                              a value property that can be set to true or
 *                              false.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.utilities;

/**
 * An object that has a value property that can be set to true or false.
 */
public class BooleanObject {

    /**
     * The value of this BooleanObject.
     */
    private boolean value;
    
    /**
     * Get the value of value.
     * @return value of value.
     */
    public boolean getValue() {
        return value;
    }
    
    /**
     * Set the value of value.
     * @param v  Value to assign to value.
     */
    public void setValue(boolean  v) {
        this.value = v;
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
