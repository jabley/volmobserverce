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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.expressions;

import com.volantis.styling.compiler.FunctionResolver;
import com.volantis.styling.compiler.FunctionResolverBuilder;
import com.volantis.styling.expressions.StylingFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link FunctionResolverBuilder}.
 */
public class FunctionResolverBuilderImpl
        implements FunctionResolverBuilder {

    /**
     * The functions recognised by this resolver.
     */
    private Map functions = new HashMap();

    // Javadoc inherited
    public void addFunction(String functionName, StylingFunction function) {
        functions.put(functionName, function);
    }

    // Javadoc inherited.
    public FunctionResolver getResolver() {
        return new SimpleFunctionResolver(functions);
    }
}
