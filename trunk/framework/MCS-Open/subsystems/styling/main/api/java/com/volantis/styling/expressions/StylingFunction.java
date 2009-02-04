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

package com.volantis.styling.expressions;

import com.volantis.mcs.themes.StyleFunctionCall;
import com.volantis.mcs.themes.StyleValue;

import java.util.List;

/**
 * A function that can be invoked during the processing of the styles.
 *
 * @mock.generate 
 */
public interface StylingFunction {

    /**
     * Called when the function is referenced from inside a
     * {@link StyleFunctionCall}.
     *
     * @param context   The evaluation context within which the function is
     *                  being invoked.
     * @param name      The name of the function being invoked.
     * @param arguments The arguments for the function being invoked.
     * @return The value of the function.
     */
    public StyleValue evaluate(
            EvaluationContext context, String name, List arguments);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
