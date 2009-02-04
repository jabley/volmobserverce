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
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.styling.expressions.EvaluationContext;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Iterates over a list evaluating any values that have been compiled and
 * constructing a new list.
 */
public class ListExpression
        implements StylingExpression {

    /**
     * The object to use to create new values.
     */
    private static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    /**
     * The list of compiled values.
     */
    private final List compiledValues;

    /**
     * Initialise.
     *
     * @param compiledValues The list of compiled values.
     */
    public ListExpression(List compiledValues) {
        this.compiledValues = compiledValues;
    }

    // Javadoc inherited.
    public StyleValue evaluate(EvaluationContext context) {
        List evaluatedValues = new ArrayList();
        for (int i = 0; i < compiledValues.size(); i++) {
            StyleValue value = (StyleValue) compiledValues.get(i);
            StyleValue evaluatedValue;
            if (value instanceof StyleCompiledExpression) {
                StyleCompiledExpression expression =
                        (StyleCompiledExpression) value;
                evaluatedValue = expression.evaluate(context);
            } else {
                evaluatedValue = value;
            }
            if (evaluatedValue != null) {
                evaluatedValues.add(evaluatedValue);
            }
        }

        return STYLE_VALUE_FACTORY.getList(evaluatedValues);
    }

    // Javadoc inherited.
    public String getDescription() {
        StringBuffer description = new StringBuffer();
        for(Iterator i = compiledValues.iterator(); i.hasNext(); ) {
            StyleValue value = (StyleValue) i.next();
            description.append(value.getStandardCSS()).append(' ');
        }
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
