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

package com.volantis.mcs.expression.functions.policy;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.functions.FunctionTable;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.pipeline.Namespace;

/**
 * The set of policy related functions.
 */
public class PolicyFunctions {

    private static final String BRANDING = "branding";
    private static final String BRANDING_URI =
            Namespace.literal(BRANDING).getURI();

    private static final ImmutableExpandedName GET_UNBRANDED_POLICY =
            new ImmutableExpandedName(BRANDING_URI, "getUnbrandedPolicy");
    private static final Function GET_UNBRANDED_POLICY_FUNCTION =
            new GetUnbrandedPolicyNameFunction();

    /**
     * The table of functions.
     */
    public static final FunctionTable FUNCTION_TABLE;

    static {
        ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
        FunctionTableBuilder builder = factory.createFunctionTableBuilder();
        builder.addDefaultPrefixMappings(BRANDING, BRANDING_URI);
        builder.addFunction(GET_UNBRANDED_POLICY, GET_UNBRANDED_POLICY_FUNCTION);
        FUNCTION_TABLE = builder.buildTable();
    }
}
