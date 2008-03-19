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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.device;

import com.volantis.mcs.context.DeviceAncestorRelationship;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.BooleanValue;

/**
 * Function that takes a single argument that represents the name of a
 * device. If the requesting device is an "instanceOf" the specified device
 * true is returned, otherwise false is returned.
 */
public class IsInstanceOfFunction extends DeviceRelationshipFunction {

    /**
     * Name of this function
     */
    private static final String NAME = "isInstanceOf";

    // javadoc inherited
    protected Value createFunctionReturnValue(
            ExpressionFactory factory,
            DeviceAncestorRelationship relationship) {
        // return true iff the relationship is that of "ancestor" or "device"
        return (relationship == DeviceAncestorRelationship.ANCESTOR ||
                relationship == DeviceAncestorRelationship.DEVICE)
                ? BooleanValue.TRUE : BooleanValue.FALSE;
    }

    // javadoc inherited
    protected String getFunctionName() {
        return NAME;
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
