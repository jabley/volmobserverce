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

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.MCSTextBlinkKeywords;
import com.volantis.mcs.themes.properties.MCSTextLineThroughStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextOverlineStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextUnderlineStyleKeywords;

/**
 * Parser for the text-decoration shorthand property.
 */
public class TextDecorationParser
    extends AbstractShorthandPropertyParser {

    /**
     * Initialise.
     */
    public TextDecorationParser() {
        super(new ShorthandValueHandler[] {
            new ToggleShorthandValueHandler(
                    StylePropertyDetails.MCS_TEXT_BLINK,
                    "blink", MCSTextBlinkKeywords.BLINK,
                    "none", MCSTextBlinkKeywords.NONE),

            new ToggleShorthandValueHandler(
                    StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE,
                    "line-through", MCSTextLineThroughStyleKeywords.SOLID,
                    "none", MCSTextLineThroughStyleKeywords.NONE),

            new ToggleShorthandValueHandler(
                    StylePropertyDetails.MCS_TEXT_OVERLINE_STYLE,
                    "overline", MCSTextOverlineStyleKeywords.SOLID,
                    "none", MCSTextOverlineStyleKeywords.NONE),

            new ToggleShorthandValueHandler(
                    StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE,
                    "underline", MCSTextUnderlineStyleKeywords.SOLID,
                    "none", MCSTextUnderlineStyleKeywords.NONE),
        });
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
