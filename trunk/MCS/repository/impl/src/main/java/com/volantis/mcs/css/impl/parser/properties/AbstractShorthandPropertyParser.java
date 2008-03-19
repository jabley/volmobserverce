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
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.styling.properties.StyleProperty;

/**
 * Base for many of the shorthand property parsers.
 */
public class AbstractShorthandPropertyParser
        extends AbstractPropertyParser {

    /**
     * The handlers for the different properties that constitute the shorthand.
     */
    protected final ShorthandValueHandler[] shorthandProperties;

    /**
     * Initialise.
     *
     * @param shorthandProperties The handlers for the different properties
     * that constitute the shorthand.
     */
    public AbstractShorthandPropertyParser(ShorthandValueHandler[] shorthandProperties) {
        this.shorthandProperties = shorthandProperties;
    }

    /**
     * Initialise.
     *
     * @param factory The factory for obtaining parsers and handlers for
     * properties.
     *
     * @param properties The properties that constitute the shorthand.
     */
    public AbstractShorthandPropertyParser(PropertyParserFactory factory,
                                           StyleProperty[] properties) {
        this.shorthandProperties = new ShorthandValueHandler[properties.length];
        for (int i = 0; i < properties.length; i++) {
            StyleProperty property = properties[i];
            shorthandProperties[i] = factory.getShorthandValueHandler(
                    property.getName());
        }
    }

    /**
     * Initialise.
     *
     * @param factory The factory for obtaining parsers and handlers for
     * properties.
     *
     * @param propertyNames The names of the properties that constitute the
     * shorthand.
     */
    public AbstractShorthandPropertyParser(PropertyParserFactory factory,
                                           String[] propertyNames) {
        this.shorthandProperties = new ShorthandValueHandler[propertyNames.length];
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i];
            shorthandProperties[i] = factory.getShorthandValueHandler(propertyName);
        }
    }

    // Javadoc inherited.
    protected void setPropertyValue(
            MutableStyleProperties properties, StyleValue value,
            Priority priority) {

        for (int i = 0; i < shorthandProperties.length; i++) {
            ShorthandValueHandler shorthandValueHandler = shorthandProperties[i];
            shorthandValueHandler.setPropertyValue(properties, value, priority);
        }
    }

    // Javadoc inherited.
    protected void parseImpl(
            ParserContext context,
            StyleValueIterator iterator) {

        boolean detectedError = false;
        int shorthandPropertyCount = shorthandProperties.length;

        MutableStyleProperties sp = context.getStyleProperties();
        StyleValue [] values = new StyleValue[shorthandPropertyCount];
        while (iterator.hasMore()) {

            // Remember how many values were remaining before trying the
            // different handlers.
            int before = iterator.remaining();

            for (int j = 0; j < shorthandPropertyCount; j++) {
                ShorthandValueHandler handler = shorthandProperties[j];

                // Try the different parsers in turn.
                StyleValue value = handler.convert(context, iterator);
                if (value != null) {
                    if (values[j] != null) {
                        context.addDiagnostic(
                                value,
                                CSSParserMessages.SHORTHAND_VALUE_ALREADY_SET,
                                new Object[]{
                                    context.getCurrentPropertyName(),
                                    handler.getShorthandReference()
                                });
                    } else {
                        values[j] = value;
                    }

                    // Break out of the loop as the value has been consumed.
                    break;
                }
            }

            // If no values were consumed then it must not be an allowable
            // value so report it, and break out.
            int after = iterator.remaining();
            if (after == before) {
                StyleValue error = iterator.value();
                context.addDiagnostic(
                        error,
                        CSSParserMessages.NOT_ALLOWABLE_VALUE,
                        new Object[]{
                            context.getCurrentPropertyName(),
                            error
                        });
                detectedError = true;
                break;
            }
        }

        // Do not set any values if an error was detected.
        if (!detectedError) {
            // If any of the properties were not set then default them.
            for (int i = 0; i < shorthandPropertyCount; i += 1) {
                ShorthandValueHandler shorthandValueHandler =
                        shorthandProperties[i];
                StyleValue value = values[i];
                if (value == null) {
                    value = shorthandValueHandler.getInitial();
                }

                // Some properties do not have an initial value, and cannot be
                // calculated yet (for example border color - if unset it
                // depends on the color of the element against which it ends up
                // being applied).
                if (value != null) {
                    shorthandValueHandler.setPropertyValue(sp, value,
                            context.getCurrentPriority());
                }
            }
        }
    }    
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
