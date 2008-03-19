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

package com.volantis.mcs.expression.functions.request;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.functions.FunctionTable;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.pipeline.Namespace;

/**
 * The set of request related functions.
 */
public class RequestFunctions {

    private static final String REQUEST = "request";
    private static final String REQUEST_URI =
            Namespace.literal(REQUEST).getURI();

    private static final ImmutableExpandedName GET_PARAMETER =
            new ImmutableExpandedName(REQUEST_URI, "getParameter");
    private static final Function GET_PARAMETER_FUNCTION =
            new GetParameterFunction();

    private static final ImmutableExpandedName GET_PARAMETERS =
            new ImmutableExpandedName(REQUEST_URI, "getParameters");
    private static final Function GET_PARAMETERS_FUNCTION =
            new GetParametersFunction();

    private static final ImmutableExpandedName GET_HEADER =
            new ImmutableExpandedName(REQUEST_URI, "getHeader");
    private static final Function GET_HEADER_FUNCTION =
            new GetHeaderFunction();

    private static final ImmutableExpandedName GET_HEADERS =
            new ImmutableExpandedName(REQUEST_URI, "getHeaders");
    private static final Function GET_HEADERS_FUNCTION =
            new GetHeadersFunction();

    /**
     * The table of functions.
     */
    public static final FunctionTable FUNCTION_TABLE;

    static {
        ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
        FunctionTableBuilder builder = factory.createFunctionTableBuilder();
        builder.addDefaultPrefixMappings(REQUEST, REQUEST_URI);
        builder.addFunction(GET_HEADER, GET_HEADER_FUNCTION);
        builder.addFunction(GET_HEADERS, GET_HEADERS_FUNCTION);
        builder.addFunction(GET_PARAMETER, GET_PARAMETER_FUNCTION);
        builder.addFunction(GET_PARAMETERS, GET_PARAMETERS_FUNCTION);
        FUNCTION_TABLE = builder.buildTable();
    }
}
