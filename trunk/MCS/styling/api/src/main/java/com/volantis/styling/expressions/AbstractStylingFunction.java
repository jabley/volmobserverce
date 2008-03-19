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

package com.volantis.styling.expressions;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.values.StyleValueCaster;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.List;

/**
 * Base class for all {@link StylingFunction}.
 */
public abstract class AbstractStylingFunction
        implements StylingFunction {

    /**
     * Used to retrieve localized exception messages.
     */
    protected static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    AbstractStylingFunction.class);

    /**
     * Object to use to cast values to appropriate type.
     */
    private static final StyleValueCaster CASTER = new StyleValueCaster();

    /**
     * Get the specified argument as an int.
     *
     * @param arguments The list of arguments.
     * @param index     The index of the argument.
     * @return The value as an integer.
     */
    protected int getArgumentAsInt(
            String functionName, List arguments, int index) {
        StyleValue value = (StyleValue) arguments.get(index);
        StyleValue cast = CASTER.cast(value, StyleValueType.INTEGER);
        if (cast == null) {
            unexpectedType(functionName, index, StyleValueType.INTEGER, value);
        }

        StyleInteger styleInteger = (StyleInteger) cast;
        return styleInteger.getInteger();
    }

    /**
     * Throw an exception indicating that the function expected something of
     * one type but got something else.
     *
     * @param functionName The name of the function being executed.
     * @param index        The index of the argument.
     * @param expectedType The expected type of the argument.
     * @param value        The actual value of the argument.
     */
    protected void unexpectedType(
            String functionName, int index,
            final StyleValueType expectedType,
            StyleValue value) {

        throw new IllegalStateException(EXCEPTION_LOCALIZER.format(
                "styling-function-argument-type", new Object[]{
                    functionName,
                    String.valueOf(index),
                    expectedType.getType(),
                    value,
                    value.getStyleValueType().getType()
                }));
    }

    /**
     * Get the specified argument as a keyword.
     *
     * @param arguments         The list of arguments.
     * @param index             The index of the argument.
     * @param allowableKeywords The set of allowable keywords.
     * @return The value as a keyword.
     */
    protected StyleKeyword getArgumentAsKeyword(
            String functionName, List arguments, int index,
            AllowableKeywords allowableKeywords) {

        StyleValue value = (StyleValue) arguments.get(index);
        StyleKeyword keyword = null;
        if (value instanceof StyleKeyword) {
            keyword = (StyleKeyword) value;
        } else {
            unexpectedType(functionName, index, StyleValueType.KEYWORD, value);
        }

        // Make sure that it is an allowable keyword.
        if (!allowableKeywords.isValidKeyword(keyword)) {
            throw new IllegalStateException("Keyword " + keyword +
                    " not allowed");
        }

        return keyword;
    }

    /**
     * Get the specified argument as a identifier.
     *
     * @param arguments The list of arguments.
     * @param index     The index of the argument.
     * @return The value as a identifier.
     */
    protected StyleIdentifier getArgumentAsIdentifier(
            String functionName, List arguments, int index) {
        StyleValue value = (StyleValue) arguments.get(index);
        StyleIdentifier identifier = null;
        if (value instanceof StyleIdentifier) {
            identifier = (StyleIdentifier) value;
        } else {
            unexpectedType(functionName, index, StyleValueType.IDENTIFIER,
                    value);
        }

        return identifier;
    }

    /**
     * Get the specified argument as a string.
     *
     * @param arguments The list of arguments.
     * @param index     The index of the argument.
     * @return The value as a string.
     */
    protected String getArgumentAsString(
            String functionName, List arguments, int index) {
        StyleValue value = (StyleValue) arguments.get(index);
        StyleString string = null;
        if (value instanceof StyleString) {
            string = (StyleString) value;
        } else {
            unexpectedType(functionName, index, StyleValueType.STRING,
                    value);
        }

        return string.getString();
    }
}
