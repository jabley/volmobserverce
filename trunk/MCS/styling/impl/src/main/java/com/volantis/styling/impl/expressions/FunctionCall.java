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

import com.volantis.styling.expressions.StylingFunction;
import com.volantis.styling.expressions.EvaluationContext;
import com.volantis.mcs.themes.StyleValue;

import java.util.List;

/**
 * An expression that represents a function call.
 */
public class FunctionCall
        implements StylingExpression {

    private final String name;
    /**
     * The resolved function.
     */
    private final StylingFunction function;

    /**
     * The arguments.
     */
    private final Arguments arguments;

    public FunctionCall(String name, StylingFunction function,
                        Arguments arguments) {
        this.name = name;
        this.function = function;
        this.arguments = arguments;
    }

    // Javadoc inherited.
    public StyleValue evaluate(EvaluationContext context) {
        List evaluatedArguments = arguments.evaluate(context);
        return function.evaluate(context, name, evaluatedArguments);
    }

    // Javadoc inherited.
    public String getDescription() {
        StringBuffer description = new StringBuffer();
        description.append(name).append('(');
        arguments.addDescription(description);
        description.append(")");
        return description.toString();
    }
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
