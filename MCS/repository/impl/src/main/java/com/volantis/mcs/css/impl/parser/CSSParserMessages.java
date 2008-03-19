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

package com.volantis.mcs.css.impl.parser;

/**
 */
public interface CSSParserMessages {

    /**
     * Unknown function ''{0}''
     * <p/>
     * 0 - function name.
     */
    public static final String UNKNOWN_FUNCTION = "" +
            "";

    /**
     * Unknown unit ''{0}''
     * <p/>
     * 0 - unit name.
     */
    public static final String UNKNOWN_UNIT = "css.parser.unknown.unit";

    /**
     * Wrong number of arguments to function ''{0}'', expected ''{1}'', received ''{2}''
     * <p/>
     * 0 - function name.
     * 1 - expected number of arguments.
     * 2 - received number of arguments.
     */
    public static final String WRONG_NUMBER_ARGUMENTS = "css.parser.wrong.number.arguments";

    /**
     * Wrong number of arguments to function ''{0}'', between ''{1}'', and ''{2}''
     * <p/>
     * 0 - function name.
     * 1 - expected minimum number of arguments.
     * 2 - expected maximum number of arguments.
     * 3 - received number of arguments.
     */
    public static final String WRONG_NUMBER_ARGUMENTS_RANGE = "css.parser.wrong.number.arguments.range";

    /**
     * {0}: Wrong number of values, expected between ''{1}'' and
     * ''{2}'', received ''{3}''
     * <p/>
     * 0 - property name.
     * 1 - expected minimum number of values.
     * 2 - expected maximum number of values.
     * 3 - received number of values.
     */
    public static final String WRONG_NUMBER_VALUES_RANGE = "css.parser.wrong.number.values.range";

    /**
     * {0}: Expected argument {1} to be a ''{2}'' but was a ''{3}''
     * <p/>
     * 0 - function name.
     * 1 - argument index.
     * 2 - expected type.
     * 3 - actual type.
     */
    public static final String WRONG_ARGUMENT_TYPE = "css.parser.wrong.argument.type";

    /**
     * {0}: Too many values, ignoring the last {1} values.
     * <p/>
     * 0 - property name.
     * 1 - number of additional values.
     */
    public static final String TOO_MANY_VALUES = "css.parser.too.many.values";

    /**
     * Incorrect argument type to function ''{0}'', argument ''{1}'' expected ''{2}'' , received ''{3}''
     * <p/>
     * 0 - function name.
     * 1 - argument index (1 based).
     * 2 - expected type of argument.
     * 3 - received type of argument.
     */
    public static final String ARGUMENT_TYPE = "css.parser.argument.type";

    /**
     * rgb() function expected arguments to be either all numbers or all percentages
     */
    public static final String RGB_EXPECTED_ARGS_ALL_SAME_TYPE = "css.parser.rgb.expected.all.same";

    /**
     * {0}: Inherit must be specified alone not with ''{1}'' other values
     * <p/>
     * 0 - property name.
     * 1 - number of other values.
     */
    public static final String INHERIT_SPECIFIED_ALONE = "css.parser.inherit.specified.alone";

    /**
     * {0}: Shorthand property has already set ''{1]' properties.
     * <p/>
     * 0 - property name.
     * 1 - name of properties that have been set.
     */
    public static final String SHORTHAND_VALUE_ALREADY_SET = "css.parser.shorthand.property.already.set";

    /**
     * {0}: Value ''{1}'' is not an allowable value for this property.
     * <p/>
     * 0 - property name.
     * 1 - value.
     */
    public static final String NOT_ALLOWABLE_VALUE = "css.parser.not.allowable.value";

    /**
     * Unknown property ''{0}''
     * <p/>
     * 0 - property name.
     */
    public static final String UNKNOWN_PROPERTY = "css.parser.property.unknown";

    /**
     * {0}: Expected an integer but found a number {1}, casting down.
     * <p/>
     * 0 - property name.
     * 1 - number.
     */
    public static final String EXPECTED_INTEGER_FOUND_NUMBER = "css.parser.expected.integer.found.number";

    /**
     * {0}: Expected a ''{1}'' separator but found ''{2}''.
     *
     * 0 - property name.
     * 1 - expected seperator
     * 2 - actual token
     */
    public static final String EXPECTED_SEPARATOR = "css.parser.expected.separator";

    /**
     * {0}: Expected a value but found separator ''{1}''.
     *
     * 0 - property name.
     * 1 - unexpected separator
     */
    public static final String UNEXPECTED_SEPARATOR = "css.parser.unexpected.separator";

    /**
     * {0}: Missing <'font-family'>
     *
     * 0 - property name.
     */
    public static final String MISSING_FONT_FAMILY = "css.parser.missing.font-family";

    /**
     * Unknown pseudo class ''{0}''.
     *
     * 0 - unknown pseudo class
     */
    public static final String UNKNOWN_PSEUDO_CLASS = "css.parser.unknown.pseudo.class";

    /**
     * Unknown pseudo element ''{0}''.
     *
     * 0 - unknown pseudo element
     */
    public static final String UNKNOWN_PSEUDO_ELEMENT = "css.parser.unknown.pseudo.element";

    /**
     * Syntax error ''{0}''.
     *
     * 0 - message from syntax exception (ParseException or TokenMgrError)
     */
    public static final String SYNTAX_ERROR = "css.parser.syntax.error";

    /**
     * Expected ''{0}'' found ''{1}''.
     *
     * 0 - expected token.
     * 1 - actual token.
     */
    public static final String EXPECTED_TOKEN = "css.parser.expected.token";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Oct-05	9732/1	adrianj	VBM:2005100509 Fixes to Eclipse GUI

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
