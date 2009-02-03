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

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of standard XPath op:except function.
 * See: <a href="http://www.w3.org/TR/xquery-operators/#func-except">
 * op:except</a>
 */
public class ExceptFunction extends BaseSequenceFunction {

    // javadoc inherited
    protected Item[] getItems(Sequence firstSequence,
            Sequence secondSequence, ExpressionContext context)
            throws ExpressionException {
        // if first sequence is empty then we can return empty array of
        // items
        if (firstSequence == Sequence.EMPTY) {
            return new Item[0];
        }
        // copy given sequence into new sequence with unique values
        firstSequence = PipelineExpressionHelper
                .getSequenceWithUniqueValues(firstSequence,
                        context.getFactory());
        // use list as positions of items are important
        // we have to iterate over all values in the first sequence
        // and add to result list (items) these values, which are not present
        // in the second sequence
        final List items = new ArrayList();
        final int length = firstSequence.getLength();
        for (int i = 1; i <= length; i++) {
            final Value value = firstSequence.getItem(i);
            // if value is not present in the second sequence then
            // we can add it to result list
            if (!PipelineExpressionHelper.isValueInSequence(secondSequence,
                    value)) {
                items.add(value);
            }
        }
        return (Item[]) items.toArray(new Item[0]);
    }

    // javadoc inherited
    protected String getName() {
        return "except";
    }

}
