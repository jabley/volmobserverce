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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.service;

import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.expression.functions.FunctionTable;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.namespace.ImmutableExpandedName;

/**
 * The set of service related functions.
 */
public class ServiceFunctions {

    /**
     * Constant for service identifier
     */
    private static final String SERVICE = "service";

    /**
     * String representation of the Service Namespace
     */
    private static final String SERVICE_URI =
            Namespace.literal(SERVICE).getURI();

    /**
     * QName for the getCharacteristic function
     */
    private static final ImmutableExpandedName GET_CHARACTERISTIC =
            new ImmutableExpandedName(SERVICE_URI, "getCharacteristic");

    /**
     * Function instance
     */
    private static final Function GET_CHARACTERISTIC_FUNCTION =
            new GetCharacteristicFunction();

    /**
     * The table of functions.
     */
    public static final FunctionTable FUNCTION_TABLE;

    static {
        ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
        FunctionTableBuilder builder = factory.createFunctionTableBuilder();
        builder.addDefaultPrefixMappings(SERVICE, SERVICE_URI);
        builder.addFunction(GET_CHARACTERISTIC,
                            GET_CHARACTERISTIC_FUNCTION);
        FUNCTION_TABLE = builder.buildTable();
    }
}
