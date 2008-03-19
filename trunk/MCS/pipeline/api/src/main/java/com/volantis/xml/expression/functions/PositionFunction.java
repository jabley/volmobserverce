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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.PositionScope;
import com.volantis.xml.expression.InternalExpressionContext;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * The position within the current position context is returned by this
 * function. This is equivalent to the XPath
 * <a href="http://www.w3.org/TR/xpath#function-position">position</a>
 * function.
 */
public class PositionFunction implements Function {
    /**
     * Used to localize exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    PositionFunction.class);

    // javadoc inherited
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        if (arguments.length != 0) {
            throw new IllegalArgumentException(
                    EXCEPTION_LOCALIZER.format(
                            "invalid-num-of-args",
                            new Object[]{new Integer(arguments.length),
                                         new Integer(0)}));
        }

        // This should never return a null value
        PositionScope pc = ((InternalExpressionContext)context).
                getPositionScope();

        return context.getFactory().createIntValue(pc.get());
    }
}
