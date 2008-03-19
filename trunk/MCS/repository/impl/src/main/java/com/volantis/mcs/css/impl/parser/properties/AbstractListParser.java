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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.css.impl.parser.CSSParserMessages;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.properties.StyleProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Base for those parsers that deal with a list of values.
 */
public abstract class AbstractListParser
        extends AbstractSinglePropertyParser
        implements ShorthandValueHandler {

    /**
     * The converter for an individual value in the list.
     */
    private final ValueConverter converter;

    /**
     * Indicates whether the list is allowed to contain duplicate values.
     */
    private final boolean unique;

    /**
     * Initialise.
     *
     * @param property  The property to set.
     * @param converter The converter for an individual item in the list.
     * @param unique    Indicator of whether the list can contain duplicate
     *                  values, false if it can, true if it cannot.
     */
    protected AbstractListParser(
            StyleProperty property, ValueConverter converter, boolean unique) {
        super(property, Integer.MAX_VALUE);
        this.converter = converter;
        this.unique = unique;
    }

    // Javadoc inherited.
    public StyleValue convert(
            ParserContext context, StyleValueIterator iterator) {

        // The list of values being constructed.
        List values = new ArrayList();

        // Iterate over the values.
        while (iterator.hasMore()) {

            // todo Do some sort of uniqueness check.

            // Attempt to convert the value.
            StyleValue value = converter.convert(context, iterator);
            if (value == null) {
                // The value was invalid so log an error, consume it and
                // carry on.
                StyleValue error = iterator.value();
                context.addDiagnostic(
                        error,
                        CSSParserMessages.NOT_ALLOWABLE_VALUE,
                        new Object[]{
                            context.getCurrentPropertyName(),
                            error
                        });

                iterator.consume();
            } else {
                // Add the value to the list.
                values.add(value);
            }

            // If there are more then check the separator.
            if (iterator.hasMore()) {
                checkSeparator(context, iterator);
            }
        }

        // Create the list.
        return STYLE_VALUE_FACTORY.getList(values, unique);
    }

    /**
     * Check the separator.
     * <p/>
     * <p>Lists use a number of different separators, i.e. no separator apart
     * from white space and a /.</p>
     * <p/>
     * <p>By default this uses no separators.</p>
     *
     * @param context  The context within which the parsing is being done.
     * @param iterator The iterator over values (and separators).
     */
    protected void checkSeparator(
            ParserContext context, StyleValueIterator iterator) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
