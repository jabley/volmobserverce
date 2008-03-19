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

import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.styling.properties.StyleProperty;

public class ToggleShorthandValueHandler
    implements ShorthandValueHandler {

    private final StyleKeyword onKeyword;
    private final String offName;
    private final StyleKeyword offKeyword;
    private final StyleProperty property;
    protected final String name;

    public ToggleShorthandValueHandler(
            StyleProperty property,
            String onName, StyleKeyword onKeyword,
            String offName, StyleKeyword offKeyword) {

        this.property = property;
        this.name = onName;
        this.onKeyword = onKeyword;
        this.offName = offName;
        this.offKeyword = offKeyword;
    }

    // Javadoc inherited.
    public StyleValue convert(
            ParserContext context, StyleValueIterator iterator) {

        StyleValue value = iterator.value();
        if (value instanceof StyleIdentifier) {
            StyleIdentifier identifierValue = (StyleIdentifier) value;
            String identifier = identifierValue.getName();
            if (name.equals(identifier)) {
                value = onKeyword;
                iterator.consume();
                return value;
            } else if (offName.equals(identifier)) {
                value = offKeyword;
                iterator.consume();
                return value;
            }
        }

        return null;
    }

    // Javadoc inherited.
    public void setPropertyValue(
            MutableStyleProperties properties, StyleValue value,
            Priority priority) {

        PropertyValue propertyValue;
        if (value == null) {
            propertyValue = null;
        } else {
            propertyValue =
                ThemeFactory.getDefaultInstance().createPropertyValue(
                    property, value, priority);
        }

        properties.setPropertyValue(propertyValue);
    }

    // Javadoc inherited.
    public String getShorthandReference() {
        return name;
    }

    public StyleValue getInitial() {
        return property.getStandardDetails().getInitialValue();
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
