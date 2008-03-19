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

package com.volantis.mcs.css.impl.parser.functions;

import com.volantis.mcs.css.impl.parser.CSSParserMessages;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.css.impl.parser.ParserValidationException;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.values.StyleValueCaster;

import java.util.List;

public abstract class ValidatingFunctionParser
        extends PassThroughFunctionParser {

    private static final StyleValueCaster CASTER = new StyleValueCaster();

    protected void expectArguments(
            ParserContext context, String name, List arguments,
            int min, int max) {

        int length = arguments.size();
        if (length < min || length > max) {
            Object maxObject;
            if (max == Integer.MAX_VALUE) {
                maxObject = "any";
            } else {
                maxObject = new Integer(max);
            }
            context.addDiagnostic(
                    CSSParserMessages.WRONG_NUMBER_ARGUMENTS_RANGE,
                    new Object[]{
                        name,
                        new Integer(min),
                        maxObject,
                        new Integer(length)
                    });

            // Break out.
            throw new ParserValidationException();
        }
    }

    protected void expectsStringExpression(
            ParserContext context, String name, List arguments, int index) {

        expectsArgument(context, name, arguments, index, StyleValueType.STRING,
                true);
    }

    protected void expectsIdentifier(
            ParserContext context, String name, List arguments, int index) {

        expectsArgument(context, name, arguments, index, StyleValueType.IDENTIFIER, false);
    }

    protected StyleKeyword expectsKeyword(
            ParserContext context, String name, List arguments, int index,
            AllowableKeywords allowableKeywords) {

        // The argument is actually going to be an identifier 
        StyleIdentifier value = (StyleIdentifier) expectsArgument(
                context, name, arguments, index, StyleValueType.IDENTIFIER,
                false);

        String identifier = value.getName();
        StyleKeyword keyword =
                allowableKeywords.getKeyword(identifier);
        if (keyword == null) {
            context.addDiagnostic(CSSParserMessages.ARGUMENT_TYPE,
                                  new Object[] {
                                      name,
                                      identifier,
                                  });

            // Break out.
            throw new ParserValidationException();
        }

        return keyword;
    }

    /**
     * Ensure that the specified argument is an integer.
     * @param context      The context within which the validation is being
     *                     performed.
     * @param name         The name of the function.
     * @param arguments    The list of arguments.
     * @param index        The index of the argument being checked.
     * @return             The original value, or if it needed to be cast and
     * could be cast then the value to which it was cast.
     */
    protected StyleValue expectsIntegerExpression(
            ParserContext context, String name, List arguments, int index) {

        return expectsArgument(context, name, arguments, index,
                StyleValueType.INTEGER, true);
    }

    /**
     * Ensure that the specified argument is of the expected type.
     *
     * @param context      The context within which the validation is being
     *                     performed.
     * @param name         The name of the function.
     * @param arguments    The list of arguments.
     * @param index        The index of the argument being checked.
     * @param expectedType The expected type of the argument.
     * @param expression   True if the value can be an expression, false if it
     *                     must be a fixed type.
     * @return             The original value, or if it needed to be cast and
     * could be cast then the value to which it was cast.
     */
    private StyleValue expectsArgument(
            ParserContext context, String name, List arguments, int index,
            StyleValueType expectedType,
            boolean expression) {

        StyleValue value = (StyleValue) arguments.get(index);
        StyleValueType type = value.getStyleValueType();

        // Attempt to cast the value into one of the appropiate type.
        StyleValue cast = CASTER.cast(value, expectedType);
        if (cast != null) {
            value = cast;
        } else if (!expression || type != StyleValueType.FUNCTION_CALL) {

            // The value could not be cast so fail.
            context.addDiagnostic(CSSParserMessages.WRONG_ARGUMENT_TYPE,
                    new Object[]{
                        name,
                        new Integer(index),
                        expectedType.getType(),
                        type.getType()
                    });

            // Break out.
            throw new ParserValidationException();
        }

        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10612/1	pduffin	VBM:2005120504 Fixed counter parsing issue and some counter test cases

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 ===========================================================================
*/
