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
import com.volantis.mcs.css.impl.parser.CSSParserMessages;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.styling.properties.StyleProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * Base for those parsers that support either a keyword, or a list.
 */
public class AbstractKeywordOrListParser
        extends AbstractListParser {

    /**
     * Supports keyword.
     */
    private static Set SUPPORTED_TYPES_KEYWORD = new HashSet();
    static {
        SUPPORTED_TYPES_KEYWORD = new HashSet();
        SUPPORTED_TYPES_KEYWORD.add(StyleValueType.KEYWORD);
    }

    /**
     * The allowableKeywords to use for the none keyword.
     */
    private final AllowableKeywords allowableKeywords;

    /**
     * Initialise.
     *
     * @param property  The property to set.
     * @param allowableKeywords    The keyword allowableKeywords.
     * @param converter The converter for an individual item in the list.
     * @param unique    Indicator of whether the list can contain duplicate
     */
    public AbstractKeywordOrListParser(
            StyleProperty property, AllowableKeywords allowableKeywords,
            ValueConverter converter, boolean unique) {
        super(property, converter, unique);
        this.allowableKeywords = allowableKeywords;
    }

    // Javadoc inherited.
    protected void parseImpl(
            ParserContext context, StyleValueIterator iterator) {

        StyleValue value = context.convertAndConsume(
                SUPPORTED_TYPES_KEYWORD, allowableKeywords, iterator);
        if (value == null) {
            super.parseImpl(context, iterator);
        } else {
            // Make sure that all of the values have been parsed (given that
            // we think we've finished). This masked a bug in vbm 2006071817.
            int remaining = iterator.remaining();
            if (remaining > 0) {
                context.addDiagnostic(CSSParserMessages.TOO_MANY_VALUES,
                        new Object[]{
                            context.getCurrentPropertyName(),
                            new Integer(expectedValueCount),
                            new Integer(remaining)
                        });
            }

            setPropertyValue(context.getStyleProperties(), value,
                    context.getCurrentPriority());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 ===========================================================================
*/
