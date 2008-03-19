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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.styling.properties.StyleProperty;

/**
 * An abstract implementation of {@link StyleValuePostProcessor} containing
 * shared functionality.
 */
public abstract class AbstractPostProcessor implements StyleValuePostProcessor {

    static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Parses a string as a specified property type, returning the style value
     * that it parses as. This may be a StyleInvalid if the string can not be
     * parsed.
     *
     * @param styleProperty The property to parse
     * @param textValue The text value of that property
     * @return The parsed {@link PropertyValue} represented by the string
     */
    protected StyleValue parseStyleValue(final StyleProperty styleProperty,
                                       String textValue) {
        // An empty string is no value.
        if (textValue.trim().equals("")) {
            return null;
        }

        StyleValue value;
        try {
            CSSParser parser =
                    CSSParserFactory.getDefaultInstance().createStrictParser();
            value = parser.parseStyleValue(styleProperty, textValue);
        } catch (IllegalStateException ise) {
            value = STYLE_VALUE_FACTORY.getInvalid(textValue);
        }

        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 09-Nov-05	10197/2	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 ===========================================================================
*/
