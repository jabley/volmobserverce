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
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.MutableStyleProperties;

/**
 * The base for all property parsers.
 */
public abstract class AbstractPropertyParser
    implements PropertyParser {

    /**
     * The object to use to create style value instances.
     */
    protected static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    // Javadoc inherited.
    public void parse(ParserContext context, StyleValueIterator iterator) {

        StyleValue value = (StyleValue) iterator.value();
        if (value instanceof StyleInherit) {
            setPropertyValue(context.getStyleProperties(), value,
                    context.getCurrentPriority());

            iterator.consume();

            int remaining = iterator.remaining();
            if (remaining > 1) {
                context.addDiagnostic(
                        CSSParserMessages.INHERIT_SPECIFIED_ALONE,
                        new Object[]{
                            context.getCurrentPropertyName(),
                            new Integer(remaining)
                        });
            }

            return;
        }

        parseImpl(context, iterator);
    }

    /**
     * Set the value.
     *
     * <p>This may set the value on one or many properties but all of them
     * use the same value.</p>
     *
     * @param properties The set of properties to update.
     * @param value The value to set.
     * @param priority
     */
    protected abstract void setPropertyValue(
            MutableStyleProperties properties, StyleValue value,
            Priority priority);

    /**
     * Method to actually perform the property specific parsing.
     *
     * @param context The context.
     * @param iterator The iterator being parsed.
     */
    protected abstract void parseImpl(
            ParserContext context, StyleValueIterator iterator);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
