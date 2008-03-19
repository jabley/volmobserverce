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

/**
 * Function that takes a single argument that represents the name of a
 * device. The function then returns a string that identifies the relationship
 * between the device argument and the requesting device. The relationship are
 * defined in the {@link
 * com.volantis.mcs.context.MarinerRequestContext#getAncestorRelationship}
 * method.
 */
public class GetAncestorRelationshipFunction
        extends DeviceRelationshipFunction {

    /**
     * Name of this function
     */
    private static final String NAME = "getAncestorRelationship";

    // javadoc inherited
    protected Value createFunctionReturnValue(
            ExpressionFactory factory,
            DeviceAncestorRelationship relationship) {
        return factory.createStringValue(relationship.getRelationshipName());
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
