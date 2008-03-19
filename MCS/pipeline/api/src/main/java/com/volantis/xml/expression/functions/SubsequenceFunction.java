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

package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-subsequence">fn:subsequence</a>
 */
public class SubsequenceFunction
        implements Function {

    // Javadoc inherited.
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {

        if (arguments.length < 2 || arguments.length > 3) {
            throw new IllegalArgumentException(
                    "subsequence() takes two or three arguments");
        }

        // First argument is a sequence.
        Sequence sequence = arguments[0].getSequence();

        // Second argument is a number.
        int length = sequence.getLength();
        int start = getIntRound(arguments[1]);
        int end;
        if (arguments.length == 3) {
            end = start + getIntRound(arguments[2]);
        } else {
            end = length;
        }

        // If the start is before the first item then make it the first item.
        if (start < 1) {
            start = 1;
        }

        // If the end is after the last item then make it the last item.
        if (end > length + 1) {
            end = length + 1;
        }

        Sequence subsequence;

        // The result sequence will be empty if:
        // 1) The input sequence is empty.
        // 2) The start is after the end of the sequence.
        // 3) The end is before or the same as the start.
        Item[] items;
        if (start == 1 && end == length + 1) {
            // The subsequence is the whole of the input so just return
            // that.
            subsequence = sequence;;
        } else if (start > length || end <= start) {
            subsequence = Sequence.EMPTY;
        } else {
            items = new Item[end - start];
            int index = 0;
            for (int i = start; i < end; i += 1) {
                items[index] = sequence.getItem(i);
                index += 1;
            }
            ExpressionFactory factory = context.getFactory();
            subsequence = factory.createSequence(items);
        }

        return subsequence;
    }

    /**
     * Get the value as an int by rounding according to {@link Math#round}.
     *
     * <p>If the value cannot be converted to an int then it will fail.</p>
     *
     * @param value The value to get as an integer.
     * @return The value as an int.
     */
    private int getIntRound(Value value) {
        if (value instanceof DoubleValue) {
            DoubleValue doubleValue = (DoubleValue) value;
            return (int) Math.round(doubleValue.asJavaDouble());
        } else if (value instanceof IntValue) {
            IntValue intValue = (IntValue) value;
            return intValue.asJavaInt();
        } else {
            throw new IllegalArgumentException(
                    "'" + value + "' is not a number");
        }
    }
}
