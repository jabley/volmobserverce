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

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.expressions.EvaluationContext;

/**
 * Represents an expression used within the styling engine.
 *
 * @mock.generate 
 */
public interface StylingExpression {

    /**
     * Evaluate this expression.
     *
     * @param context The context within which the expression is being
     * evaluated.
     *
     * @return The result of evaluating the expression.
     */
    public StyleValue evaluate(EvaluationContext context);

    /**
     * Return a string representation of this expression.
     *
     * @return String representation of this expression
     */
    public String getDescription();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
