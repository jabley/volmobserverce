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
 * Implementation of standard XPath op:union function.
 * See: <a href="http://www.w3.org/TR/xquery-operators/#func-union">
 * op:union</a>
 */
public class UnionFunction extends BaseSequenceFunction {

    // javadoc inherited
    public Item[] getItems(Sequence firstSequence,
            Sequence secondSequence, ExpressionContext context)
            throws ExpressionException {
        // if both sequences are empty then we can return empty array of
        // items
        if (firstSequence == Sequence.EMPTY &&
                secondSequence == Sequence.EMPTY) {
            return new Item[0];
        }
        // copy given sequences into new sequences with unique values
        if (firstSequence != Sequence.EMPTY) {
            firstSequence = PipelineExpressionHelper
                    .getSequenceWithUniqueValues(firstSequence,
                            context.getFactory());
        }
        if (secondSequence != Sequence.EMPTY) {
            secondSequence = PipelineExpressionHelper
                    .getSequenceWithUniqueValues(secondSequence,
                            context.getFactory());
        }
        // use list as positions of items are important
        // first add all values from the first sequence
        final List items = new ArrayList();
        int length = firstSequence.getLength();
        for (int i = 1; i <= length; i++) {
            items.add(firstSequence.getItem(i));
        }
        length = secondSequence.getLength();
        // then add also values from second sequence if
        // not already added from the first one
        for (int i = 1; i <= length; i++) {
            final Value value = secondSequence.getItem(i);
            // if value not exists already in array then add it
            if (!PipelineExpressionHelper.isValueInSequence(firstSequence,
                    value)) {
                items.add(value);
            }
        }
        return (Item[]) items.toArray(new Item[0]);
    }

    // javadoc inherited
    protected String getName() {
        return "union";
    }
    
}
