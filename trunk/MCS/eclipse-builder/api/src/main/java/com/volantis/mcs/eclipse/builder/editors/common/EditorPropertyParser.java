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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.eclipse.builder.editors.themes.StylePropertyMetadata;
import com.volantis.mcs.eclipse.builder.editors.themes.StyleValuePostProcessor;
import com.volantis.styling.properties.StyleProperty;

import java.util.Iterator;

/**
 * Class which uses the {@link CSSParser} framework to parse a text value for
 * a particular property into a {@link StyleValue}, applying any necessary
 * post processors. 
 */
public class EditorPropertyParser {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Parses a string as a specified property type, returning the style value
     * that it parses as. This may be a StyleInvalid if the string can not be
     * parsed.
     *
     * @param styleProperty The property to parse
     * @param textValue The text value of that property
     * @param important True if the property is important
     * @return The parsed {@link com.volantis.mcs.themes.StyleValue}
     * represented by the string
     */
    public PropertyValue parsePropertyValue(final StyleProperty styleProperty,
            String textValue, boolean important) {

        // An empty string is no value.
        if (textValue.trim().equals("")) {
            return null;
        }

        StyleValue value;
        try {
            CSSParser parser = CSSParserFactory.getDefaultInstance().
                    createStrictParser();
            value = parser.parseStyleValue(styleProperty, textValue);
        } catch (IllegalStateException ise) {
            value = STYLE_VALUE_FACTORY.getInvalid(textValue);
        }

        if (value != null) {
            java.util.List postProcessors = StylePropertyMetadata.
                    getPostProcessors(styleProperty);
            if (postProcessors != null) {
                Iterator it = postProcessors.iterator();
                while (it.hasNext()) {
                    StyleValuePostProcessor postProcessor =
                            (StyleValuePostProcessor) it.next();
                    value = postProcessor.postProcess(value);
                }
            }
        }

        return value == null ? null :
            ThemeFactory.getDefaultInstance().createPropertyValue(
                styleProperty, value,
                important ? Priority.IMPORTANT : Priority.NORMAL);
    }
}
