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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;


/**
 * This function provides XPATH 2.0 tokenize functionality. Note that this
 * implementation only provides the two-argument version of the XPATH one; the
 * first argument being the string to tokenize, and the second the pattern.
 */
public class TokenizeFunction implements Function {

    /**
     * Used for localizing exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.
                    createExceptionLocalizer(TokenizeFunction.class);

    /**
     * The message for an invalid 'pattern' argument.
     */
    private static final String INVALID_PATTERN_MSG =
            EXCEPTION_LOCALIZER.format("invalid-parameter-value",
                    new Object[]{"pattern", ""});

    /**
     * A binary delimiter character, used during processing so that the correct
     * generation of empty string tokens is ensured.
     */
    private static final char BINARY_DELIM = 0;


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
            final String strToTokenize =
                    args[0].stringValue().asJavaString();
            final String pattern =
                    args[1].stringValue().asJavaString();

            if (pattern.length() == 0) {
                throw new ExpressionException(INVALID_PATTERN_MSG);
            }

            if (strToTokenize.length() == 0) {
                // If there is no input string, return the empty sequence.
                result = Sequence.EMPTY;
            } else {
                final ExpressionFactory factory = context.getFactory();

                // If the input matches the pattern then the result is a
                // sequence with one empty string.
                final boolean matchesPattern =
                        strToTokenize.matches('^' + pattern + '$');
                if (matchesPattern) {
                    result = factory.createSequence(
                            new Item[]{factory.createStringValue("")});
                } else {

                    // Surround the input string with the binary delimiter and
                    // split it.
                    final String[] tokens =
                            (BINARY_DELIM + strToTokenize + BINARY_DELIM).
                                    split(pattern);

                    // Remove the dummy delimiter.
                    tokens[0] = tokens[0].substring(1);
                    final String lastToken = tokens[tokens.length - 1];
                    tokens[tokens.length - 1] = lastToken.
                            substring(0, lastToken.length() - 1);

                    // Create the items for the result sequence
                    final Item[] items = new Item[tokens.length];
                    for (int i = 0; i < tokens.length; i++) {
                        items[i] = factory.createStringValue(tokens[i]);
                    }

                    result = factory.createSequence(items);
                }
            }
        }

        return result;
    }
}
