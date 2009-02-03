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

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;


/**
 * This function returns the positions of its search argument within its given
 * sequence. This is equivalent to the XPath 2.0 function
 * <a href="http://www.w3.org/TR/2005/CR-xpath-functions-20051103/#func-index-of">index-of</a>.
 *
 * <p>
 * Examples:
 * </p>
 * <dl>
 * <dt>index-of((15, 40, 25, 40, 10), 40)</dt>
 * <dd>returns sequence (2, 4)</dd>
 *
 * <dt>index-of(("a", "dog", "and", "a", "duck"), "a")</dt>
 *
 * <dd>returns sequence (1, 4)</dd>
 *
 * <dt>index-of((15, 40, 25, 40, 10), 18)</dt>
 *
 * <dd>returns empty sequence ()</dd>
 *
 * </dl>
 */
public class IndexOfFunction implements Function {

    /**
     * Used for localizing exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.
                    createExceptionLocalizer(IndexOfFunction.class);

    // javadoc inherited
    public Value invoke(ExpressionContext context, Value[] args)
            throws ExpressionException {
        if ((args.length != 2)) {
            throw new ExpressionException(
                    EXCEPTION_LOCALIZER.format(
                            "invalid-num-of-args",
                            new Object[]{new Integer(args.length),
                                    new Integer(2)}));
        }

        Value result = Sequence.EMPTY;

        if (args[0] != Sequence.EMPTY && args[1] != Sequence.EMPTY) {
            final Sequence inputSequence = args[0].getSequence();
            final String searchStr = args[1].stringValue().asJavaString();
            final ExpressionFactory factory = context.getFactory();

            // Allocate enough space for the return sequence as we know the
            // maximum number of possible matches.
            final Item [] items = new Item[inputSequence.getLength()];
            int numMatches = 0;
            for (int i = 1; i <= inputSequence.getLength(); i++) {
                Item item = inputSequence.getItem(i);
                if (searchStr.equals(item.stringValue().asJavaString())) {

                    // save a match
                    items[numMatches++] = factory.createIntValue(i);
                }
            }
            if (numMatches > 0) {
                // Create the sequence of match indices, if any
                final Item[] matched = new Item[numMatches];
                System.arraycopy(items, 0, matched, 0, numMatches);
                result = factory.createSequence(matched);
            }
        }

        return result;
    }
}
