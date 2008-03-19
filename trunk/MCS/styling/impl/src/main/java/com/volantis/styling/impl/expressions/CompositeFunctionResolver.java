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
import com.volantis.styling.expressions.StylingFunction;

import java.util.List;

/**
 * A resolver composed of other resolvers.
 */
public class CompositeFunctionResolver
        implements FunctionResolver {

    /**
     * The resolvers.
     */
    private final FunctionResolver[] resolvers;

    /**
     * Initialise.
     *
     * @param resolvers The list of resolvers.
     */
    public CompositeFunctionResolver(List resolvers) {
        // Copy the list into the array.
        this.resolvers = new FunctionResolver[resolvers.size()];
        resolvers.toArray(this.resolvers);
    }

    // Javadoc inherited.
    public StylingFunction resolve(String name) {
        StylingFunction function = null;
        for (int i = 0; i < resolvers.length; i++) {
            FunctionResolver resolver = resolvers[i];
            function = resolver.resolve(name);
            if (function != null) {
                return function;
            }
        }

        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
