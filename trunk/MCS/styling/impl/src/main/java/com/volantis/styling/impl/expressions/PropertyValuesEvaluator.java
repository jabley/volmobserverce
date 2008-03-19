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
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.expressions.EvaluationContext;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Evaluates any compiled expressions within values stored in the styles.
 */
public class PropertyValuesEvaluator
        implements StylePropertyIteratee {

    private final EvaluationContext context;

    private MutablePropertyValues values;

    public PropertyValuesEvaluator(EvaluationContext context) {
        this.context = context;
    }

    public void evaluate(MutablePropertyValues values) {
        // Evaluate values associated with the styles.
        this.values = values;
        values.iterateStyleProperties(this);
    }

    // Javadoc inherited.
    public IterationAction next(StyleProperty property) {

        // If the value is a compiled expression then evaluate it and store
        // the result back into the properties.
        StyleValue value = values.getComputedValue(property);

        if (value instanceof StyleCompiledExpression) {
            StyleValue evaluatedValue;
            StyleCompiledExpression expression =
                    (StyleCompiledExpression) value;
            evaluatedValue = expression.evaluate(context);
            values.setComputedValue(property, evaluatedValue);
        }

        return IterationAction.CONTINUE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
