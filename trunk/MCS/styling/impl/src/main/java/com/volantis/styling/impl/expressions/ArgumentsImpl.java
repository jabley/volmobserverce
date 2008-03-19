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

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of arguments to a function call.
 */
public class ArgumentsImpl
        implements Arguments {

    /**
     * The list of argument values that may or may not need evaluating before
     * they are used.
     */
    private final List values;

    /**
     * Indicates whether the values contains any expressions that need
     * evaluating.
     */
    private final boolean containsExpression;

    /**
     * Initialise.
     *
     * @param arguments          The list of argument values that may or may
     *                           not need evaluating before they are used.
     * @param containsExpression Indicates whether the values contains any
     *                           expressions that need evaluating.
     */
    public ArgumentsImpl(List arguments, boolean containsExpression) {
        this.values = arguments;
        this.containsExpression = containsExpression;
    }

    // Javadoc inherited.
    public List evaluate(EvaluationContext context) {
        List evaluated;
        if (containsExpression) {
            evaluated = new ArrayList();
            for (int i = 0; i < values.size(); i++) {
                StyleValue value = (StyleValue) values.get(i);
                if (value instanceof StyleCompiledExpression) {
                    StyleCompiledExpression expression =
                            (StyleCompiledExpression) value;
                    value = expression.evaluate(context);
                }
                evaluated.add(value);
            }
        } else {
            evaluated = values;
        }

        return evaluated;
    }

    // Javadoc inherited.
    public void addDescription(StringBuffer description) {
        for (int i = 0; i < values.size(); i++) {
            description.append(values.get(i));
            if (i < values.size() - 1) {
                description.append(',');
            }
        }
    }
}
