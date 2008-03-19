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
 * $Header: /src/voyager/com/volantis/mcs/gui/policyobject/PolicyObjectChooser.java,v 1.1 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-May-03    Adrian          VBM:2003051901 - Created this class to provide 
 *                              a mechanism to use booleans declared in method 
 *                              scope within anonymous inner classes in that 
 *                              method. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics;

/**
 * This simple class allows us to use booleans declared in method scope within
 * an anonymous inner class in that method.
 *
 * @deprecated Yeuch
 */
public class BooleanWrapper {

    /**
     * Volantis copyright object.
     */
    private static String mark =
        "(c) Volantis Systems Ltd 2003. ";

    /**
     * The boolean value.
     */
    private boolean value;

    /**
     * Construct a new BooleanWrapper.
     *
     * @param initial The initial value for this BooleanWrapper
     */
    public BooleanWrapper(boolean initial) {
        value = initial;
    }

    /**
     * Set the value of this BooleanWrapper
     *
     * @param value The value for this BooleanWrapper
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * Get the value of this BooleanWrapper
     *
     * @return the value of this BooleanWrapper.
     */
    public boolean getValue() {
        return value;
    }
}
