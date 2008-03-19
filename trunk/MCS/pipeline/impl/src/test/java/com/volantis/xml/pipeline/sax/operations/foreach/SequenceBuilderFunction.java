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

package com.volantis.xml.pipeline.sax.operations.foreach;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.expression.sequence.Item;

/**
 * Return an sequence containing items from the input values.
 */
public class SequenceBuilderFunction
        implements Function {

    // Javadoc inherited.
    public Value invoke(ExpressionContext expressionContext, Value[] values)
            throws ExpressionException {

        // Iterate over the values counting how many items there are in the
        // sequences.
        int itemCount = 0;
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                Value value = values[i];
                Sequence sequence = value.getSequence();
                itemCount += sequence.getLength();
            }
        }

        Item [] items;
        if (itemCount == 0) {
            // No items so create an empty sequence.
            items = null;
        } else {

            // Construct and populate an array of items from the values.
            items = new Item[itemCount];
            int index = 0;
            for (int i = 0; i < values.length; i++) {
                Value value = values[i];
                Sequence sequence = value.getSequence();
                for (int s = 1; s <= sequence.getLength(); s += 1) {
                    items[index] = sequence.getItem(s);
                    index += 1;
                }
            }
        }

        // Create a sequence to return.
        ExpressionFactory factory = expressionContext.getFactory();
        return factory.createSequence(items);
    }
}
