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
package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.css.impl.parser.CSSParserMessages;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.styling.properties.StyleProperty;

/**
 * Parser for the mcs-border-radius shorthand property.
 */

public class MCSBorderRadiusParser extends AbstractShorthandPropertyParser {

    public MCSBorderRadiusParser(PropertyParserFactory factory) {
        super(factory, new StyleProperty[] {
                StylePropertyDetails.MCS_BORDER_TOP_LEFT_RADIUS,
                StylePropertyDetails.MCS_BORDER_TOP_RIGHT_RADIUS,
                StylePropertyDetails.MCS_BORDER_BOTTOM_RIGHT_RADIUS,
                StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS});
    }

    // Javadoc inherited.
    protected void parseImpl(ParserContext context, StyleValueIterator iterator) {

        boolean detectedError = false;
        int shorthandPropertyCount = shorthandProperties.length;

        MutableStyleProperties sp = context.getStyleProperties();
        StyleValue[] values = new StyleValue[shorthandPropertyCount];

        while (iterator.hasMore()) {
            // Remember how many values were remaining before trying the
            // different handlers.
            int before = iterator.remaining();

            ShorthandValueHandler handler = shorthandProperties[0];
            StyleValue value = handler.convert(context, iterator);
            
            for (int j = 0; j < shorthandPropertyCount; j++) {
                values[j] = value;
            }
            // If no values were consumed then it must not be an allowable
            // value so report it, and break out.
            int after = iterator.remaining();

            if (after == before) {
                StyleValue error = iterator.value();
                context.addDiagnostic(error,
                        CSSParserMessages.NOT_ALLOWABLE_VALUE, new Object[] {
                                context.getCurrentPropertyName(), error });
                detectedError = true;
                break;
            }
        }

        // Do not set any values if an error was detected.
        if (!detectedError) {
            // If any of the properties were not set then default them.
            for (int i = 0; i < shorthandPropertyCount; i += 1) {
                ShorthandValueHandler shorthandValueHandler = shorthandProperties[i];
                StyleValue value = values[i];
                if (value == null) {
                    value = shorthandValueHandler.getInitial();
                }

                shorthandValueHandler.setPropertyValue(sp, value, context
                        .getCurrentPriority());
            }
        }
    }

    public String getShorthandReference() {
        // TODO Auto-generated method stub
        return null;
    }

    public StyleValue getInitial() {
        // TODO Auto-generated method stub
        return null;
    }

    public StyleValue convert(ParserContext context, StyleValueIterator iterator) {
        // TODO Auto-generated method stub
        return null;
    }

    // Javadoc inherited.
    protected void setPropertyValue(MutableStyleProperties properties,
            StyleValue value, Priority priority) {

        for (int i = 0; i < shorthandProperties.length; i++) {
            ShorthandValueHandler shorthandValueHandler = shorthandProperties[i];
            shorthandValueHandler.setPropertyValue(properties, value, priority);
        }
    }

}
