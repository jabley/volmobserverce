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
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.styling.properties.StyleProperty;

/**
 * Base for those property parsers that handle setting a single property.
 */
public abstract class AbstractSinglePropertyParser
        extends AbstractPropertyParser
        implements ShorthandValueHandler {

    private final StyleProperty property;
    protected final int expectedValueCount;

    public AbstractSinglePropertyParser(
            StyleProperty property,
            int expectedValueCount) {
        this.property = property;
        this.expectedValueCount = expectedValueCount;
    }

    // Javadoc inherited.
    public void setPropertyValue(
            MutableStyleProperties properties, StyleValue value,
            Priority priority) {

        PropertyValue propertyValue;
        if (value == null) {
            propertyValue = null;
        } else {
            propertyValue = ThemeFactory.getDefaultInstance().
                createPropertyValue(property, value, priority);
        }

        properties.setPropertyValue(propertyValue);
//        properties.setStyleValue(property, value);
    }

    // Javadoc inherited.
    public StyleValue getInitial() {
        return property.getStandardDetails().getInitialValue();
    }

    // Javadoc inherited.
    public String getShorthandReference() {
        return "<'" + property.getName() + "'>";
    }

    // Javadoc inherited.
    protected void parseImpl(
            ParserContext context, StyleValueIterator iterator) {

        StyleValue value;

        int total = iterator.remaining();

        value = convert(context, iterator);
        if (value == null) {

            // Value is invalid.
            StyleValue error = iterator.value();
            context.addDiagnostic(
                    error,
                    CSSParserMessages.NOT_ALLOWABLE_VALUE,
                    new Object[]{
                        context.getCurrentPropertyName(),
                        error.getStandardCSS()
                    });

        } else {
            int remaining = iterator.remaining();
            if (remaining > 0) {
                context.addDiagnostic(iterator.value(),
                        CSSParserMessages.TOO_MANY_VALUES,
                        new Object[]{
                            context.getCurrentPropertyName(),
                            new Integer(expectedValueCount),
                            new Integer(total)
                        });
            } else {
                context.setPropertyValue(property, value);
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

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
