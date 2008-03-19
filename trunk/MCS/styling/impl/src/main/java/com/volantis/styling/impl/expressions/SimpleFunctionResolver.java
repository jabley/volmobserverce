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
package com.volantis.styling.impl.expressions;

import com.volantis.styling.compiler.FunctionResolver;
import com.volantis.styling.expressions.StylingFunction;

import java.util.Map;

/**
 * A simple map-based implementation of {@link FunctionResolver}.
 */
public class SimpleFunctionResolver
        implements FunctionResolver {
    
    /**
     * The functions recognised by this resolver.
     */
    private final Map functions;

    public SimpleFunctionResolver(Map functions) {
        this.functions = functions;
    }

    // Javadoc inherited
    public StylingFunction resolve(String name) {
        return (StylingFunction) functions.get(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9635/1	adrianj	VBM:2005092817 Counter functions for CSS

 ===========================================================================
*/
