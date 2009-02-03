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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * Base class for all standard XPath functions which are relared to
 * sequences.
 */
public abstract class BaseSequenceFunction implements Function {

    /**
     * Used to localize exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    BaseSequenceFunction.class);

    // javadoc inherited
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        if (arguments.length != 2) {
            throw new IllegalArgumentException(
                    EXCEPTION_LOCALIZER.format(
                            "invalid-num-of-args",
                            new Object[]{getName(), new Integer(2), 
                                    new Integer(arguments.length)}));
        }
        final Sequence firstSequence = arguments[0].getSequence();
        final Sequence secondSequence = arguments[1].getSequence();
        Sequence result = Sequence.EMPTY;
        Item[] items = getItems(firstSequence, secondSequence, context);
        if (items.length > 0) {
            result = context.getFactory().createSequence(items);
        }
        return result;
    }

    /**
     * Gets name of function.
     * 
     * @return name of function
     */
    protected abstract String getName();

    /**
     * Gets items array for given sequences.
     * 
     * @param firstSequence the first sequence to be checked
     * @param secondSequence the second sequence to be checked
     * @return item array depends on function type
     * @throws ExpressionException on any error while performing
     *                             sequences comparision
     */
    protected abstract Item[] getItems(Sequence firstSequence,
            Sequence secondSequence, ExpressionContext context)
            throws ExpressionException;

}
