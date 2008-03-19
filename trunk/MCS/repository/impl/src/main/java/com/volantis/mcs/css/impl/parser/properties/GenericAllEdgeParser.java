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
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.styling.properties.StyleProperty;

/**
 * Base for all those parsers that set properties on all four edges.
 */
public class GenericAllEdgeParser
        extends AbstractPropertyParser
        implements ShorthandValueHandler {

    /**
     * The value converter for all the values for all edges.
     */
    private final ValueConverter valueConverter;

    /**
     * The array of properties, indexed by the {@link PropertyGroups#TOP} at
     * al.
     */
    private final StyleProperty[] properties;

    /**
     * The property for the top edge.
     */
    private final StyleProperty top;

    /**
     * The property for the left edge.
     */
    private final StyleProperty left;

    /**
     * The property for the bottom edge.
     */
    private final StyleProperty bottom;

    /**
     * The property for the right edge.
     */
    private final StyleProperty right;

    /**
     * Initialise.
     *
     * @param valueConverter The value converter.
     * @param properties     The properties for the edges in the order specified
     *                       in {@link PropertyGroups}.
     */
    public GenericAllEdgeParser(
            ValueConverter valueConverter, StyleProperty[] properties) {

        this.valueConverter = valueConverter;
        this.properties = properties;
        this.top = properties[PropertyGroups.TOP];
        this.left = properties[PropertyGroups.LEFT];
        this.bottom = properties[PropertyGroups.BOTTOM];
        this.right = properties[PropertyGroups.RIGHT];
    }

    // Javadoc inherited.
    public void setPropertyValue(
            MutableStyleProperties properties, StyleValue value,
            Priority priority) {
        
        for (int i = 0; i < this.properties.length; i++) {
            StyleProperty property = this.properties[i];
            if (value != null) {
                PropertyValue propertyValue =
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        property, value, priority);
                properties.setPropertyValue(propertyValue);
            }
        }
    }

    // Javadoc inherited.
    protected void parseImpl(
            ParserContext context, StyleValueIterator iterator) {

        int available = iterator.remaining();
        int remaining = available;
        if (available > 4) {
            remaining = 4;
        }

        StyleValue value;

        switch (remaining) {
            case 1:
                value = nextStyleValue(context, iterator);
                if (value != null) {
                    context.setPropertyValue(top, value);
                    context.setPropertyValue(bottom, value);
                    context.setPropertyValue(left, value);
                    context.setPropertyValue(right, value);
                }
                break;

            case 2:
                // Top & bottom get first
                value = nextStyleValue(context, iterator);
                if (value != null) {
                    context.setPropertyValue(top, value);
                    context.setPropertyValue(bottom, value);
                }

                // Left & right get second
                value = nextStyleValue(context, iterator);
                if (value != null) {
                    context.setPropertyValue(left, value);
                    context.setPropertyValue(right, value);
                }
                break;

            case 3:
                // Top gets first
                value = nextStyleValue(context, iterator);
                if (value != null) {
                    context.setPropertyValue(top, value);
                }

                // Left & right get second
                value = nextStyleValue(context, iterator);
                if (value != null) {
                    context.setPropertyValue(left, value);
                    context.setPropertyValue(right, value);
                }

                // Bottom gets third
                value = nextStyleValue(context, iterator);
                if (value != null) {
                    context.setPropertyValue(bottom, value);
                }
                break;

            case 4:
                // Top gets first
                value = nextStyleValue(context, iterator);
                if (value != null) {
                    context.setPropertyValue(top, value);
                }

                // Right gets second
                value = nextStyleValue(context, iterator);
                if (value != null) {
                    context.setPropertyValue(right, value);
                }

                // Bottom gets third
                value = nextStyleValue(context, iterator);
                if (value != null) {
                    context.setPropertyValue(bottom, value);
                }

                // Left gets fourth
                value = nextStyleValue(context, iterator);
                if (value != null) {
                    context.setPropertyValue(left, value);
                }
                break;
        }

        if (available > 4) {
            context.addDiagnostic(
                    iterator.value(),
                    CSSParserMessages.WRONG_NUMBER_VALUES_RANGE,
                    new Object[]{
                        context.getCurrentPropertyName(),
                        new Integer(1),
                        new Integer(4),
                        new Integer(available)
                    });
        }
    }

    /**
     * Get the value.
     *
     * @param context The context within which the parser is running.
     * @param iterator The iterator over the style value.
     *
     * @return The next value, or null if it was not valid.
     */
    private StyleValue nextStyleValue(
            ParserContext context, StyleValueIterator iterator) {

        StyleValue convertedValue = valueConverter.convert(context, iterator);
        if (convertedValue == null) {
            StyleValue error = iterator.value();
            context.addDiagnostic(
                    error,
                    CSSParserMessages.NOT_ALLOWABLE_VALUE,
                    new Object[]{
                        context.getCurrentPropertyName(),
                        error
                    });

            // Ignore this value and move onto the next one.
            iterator.consume();
        }

        return convertedValue;
    }

    // Javadoc inherited.
    public StyleValue getInitial() {
        return top.getStandardDetails().getInitialValue();
    }

    // Javadoc inherited.
    public StyleValue convert(
            ParserContext context, StyleValueIterator iterator) {
        return valueConverter.convert(context, iterator);
    }

    // Javadoc inherited.
    public String getShorthandReference() {
        return "todo";
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
