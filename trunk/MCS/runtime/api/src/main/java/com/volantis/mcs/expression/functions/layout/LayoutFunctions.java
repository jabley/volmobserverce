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

package com.volantis.mcs.expression.functions.layout;

import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.functions.FunctionTable;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.pipeline.Namespace;

/**
 * The set of layout related functions.
 */
public class LayoutFunctions {

    private static final String LAYOUT = "layout";
    private static final String LAYOUT_URI =
            Namespace.literal(LAYOUT).getURI();


    private static final ImmutableExpandedName GET_PANE_INSTANCE =
            new ImmutableExpandedName(LAYOUT_URI, "getPaneInstance");
    private static final Function GET_PANE_INSTANCE_FUNCTION =
            new GetIteratedFormatInstanceFunction(FormatNamespace.PANE);

    private static final ImmutableExpandedName GET_REGION_INSTANCE =
            new ImmutableExpandedName(LAYOUT_URI, "getRegionInstance");
    private static final Function GET_REGION_INSTANCE_FUNCTION =
            new GetIteratedFormatInstanceFunction(FormatNamespace.REGION);

    /**
     * The table of functions.
     */
    public static final FunctionTable FUNCTION_TABLE;

    static {
        ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
        FunctionTableBuilder builder = factory.createFunctionTableBuilder();
        builder.addDefaultPrefixMappings(LAYOUT, LAYOUT_URI);

        // Note the factory is not passed to the Get[Format]InstanceFunction
        // classes as it is never used. Get[Format]InstanceFunction implements
        // Function so the expression context is passed to the invoke() method
        // when it is required. The factory is then retrieved directly from
        // the context.
        builder.addFunction(GET_PANE_INSTANCE, GET_PANE_INSTANCE_FUNCTION);
        builder.addFunction(GET_REGION_INSTANCE, GET_REGION_INSTANCE_FUNCTION);

        FUNCTION_TABLE = builder.buildTable();
    }
}
