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

package com.volantis.styling.compiler;

import com.volantis.styling.expressions.StylingFunction;

/**
 * Resolve a function name to a function implementation.
 *
 * @mock.generate 
 */
public interface FunctionResolver {

    /**
     * Resolves the name to a {@link com.volantis.styling.expressions.StylingFunction}, or returns null if
     * no matching function could be found.
     *
     * @param name The name of the function.
     * @return The {@link com.volantis.styling.expressions.StylingFunction}, or null if
     *         no matching function could be found.
     */
    public StylingFunction resolve(String name);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
