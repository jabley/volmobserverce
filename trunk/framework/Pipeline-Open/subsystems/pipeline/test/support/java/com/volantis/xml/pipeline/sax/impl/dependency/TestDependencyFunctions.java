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

package com.volantis.xml.pipeline.sax.impl.dependency;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.functions.FunctionTable;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.namespace.ImmutableExpandedName;

/**
 * A collection of {@link com.volantis.xml.expression.Function} for use within
 * dependency testing.
 */
public class TestDependencyFunctions {

    private static final String DEPENDENCY_PREFIX = "dependency";
    private static final ImmutableExpandedName FUNCTION_NAME =
            new ImmutableExpandedName(DependencyTestRuleConfigurator.NAMESPACE,
                    "dependency");
    private static final DependencyFunction FUNCTION =
            new DependencyFunction();

    public static final FunctionTable FUNCTION_TABLE;

    static {
        ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
        FunctionTableBuilder builder = factory.createFunctionTableBuilder();
        builder.addDefaultPrefixMappings(
                DependencyTestRuleConfigurator.NAMESPACE, DEPENDENCY_PREFIX);
        builder.addFunction(FUNCTION_NAME, FUNCTION);
        FUNCTION_TABLE = builder.buildTable();
    }
}
