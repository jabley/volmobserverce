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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.expression.functions.device;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.functions.FunctionTable;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.pipeline.Namespace;

/**
 * The set of device related functions.
 */
public class DeviceFunctions {

    private static final String DEVICE = "device";
    private static final String DEVICE_URI =
            Namespace.literal(DEVICE).getURI();

    private static final ImmutableExpandedName GET_POLICY_VALUE =
            new ImmutableExpandedName(DEVICE_URI, "getPolicyValue");
    private static final Function GET_POLICY_VALUE_FUNCTION =
            new GetPolicyValueFunction();

    private static final ImmutableExpandedName GET_DEVICE_NAME =
            new ImmutableExpandedName(DEVICE_URI, "getDeviceName");
    private static final Function GET_DEVICE_NAME_FUNCTION =
            new GetDeviceNameFunction();

    private static final ImmutableExpandedName GET_ANCESTOR_RELATIONSHIP =
            new ImmutableExpandedName(DEVICE_URI, "getAncestorRelationship");
    private static final Function
            GET_ANCESTOR_RELATIONSHIP_FUNCTION =
            new GetAncestorRelationshipFunction();

    private static final ImmutableExpandedName IS_INSTANCE_OF =
            new ImmutableExpandedName(DEVICE_URI, "isInstanceOf");
    private static final Function IS_INSTANCE_OF_FUNCTION =
            new IsInstanceOfFunction();

    /**
     * The table of functions.
     */
    public static final FunctionTable FUNCTION_TABLE;

    static {
        ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
        FunctionTableBuilder builder = factory.createFunctionTableBuilder();
        builder.addDefaultPrefixMappings(DEVICE, DEVICE_URI);
        builder.addFunction(GET_ANCESTOR_RELATIONSHIP,
                GET_ANCESTOR_RELATIONSHIP_FUNCTION);
        builder.addFunction(GET_DEVICE_NAME, GET_DEVICE_NAME_FUNCTION);
        builder.addFunction(GET_POLICY_VALUE, GET_POLICY_VALUE_FUNCTION);
        builder.addFunction(IS_INSTANCE_OF, IS_INSTANCE_OF_FUNCTION);
        FUNCTION_TABLE = builder.buildTable();
    }
}
