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

import com.volantis.mcs.themes.CustomStyleValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.styling.expressions.EvaluationContext;

/**
 * A StyleValue instance that wraps a compiled expression.
 * <p/>
 * <p>This is only ever used internally within the styling subsystem.</p>
 */
public final class StyleCompiledExpression extends CustomStyleValue
        implements StylingExpression {

    /**
     * The compiled expression.
     */
    private final StylingExpression expression;

    /**
     * Initialise.
     *
     * @param expression The expression.
     */
    public StyleCompiledExpression(StylingExpression expression) {
        this.expression = expression;
    }

    // Javadoc inherited.
    public StyleValue evaluate(EvaluationContext context) {
        // Delegate to the contained expression.
        return expression.evaluate(context);
    }

    public String getStandardCSS() {
        return expression.getDescription();    
    }

    // Javadoc inherited.
    public String getDescription() {
        return getStandardCSS();
    }

    // Javadoc inherited.
    public StyleValueType getStyleValueType() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public void visit(StyleValueVisitor visitor, Object object) {
        visitor.visit(this, object);
    }

    // Javadoc inherited.
    public int getStandardCost() {
        throw new UnsupportedOperationException();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
