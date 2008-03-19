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
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.styling.properties.StyleProperty;

import java.util.Set;

/**
 * Parser for a large number of different properties.
 * <p/>
 * <p>The behaviour of this is driven by data associated with a property,
 * namely the property definition and the keyword allowableKeywords.</p>
 */
public class ParameterizedPropertyParser
        extends AbstractSinglePropertyParser {

    /**
     * The set of supported types.
     */
    private final Set supportedTypes;

    /**
     * The allowableKeywords for keywords.
     */
    private final AllowableKeywords allowableKeywords;

    /**
     * Initialise.
     *
     * @param property The property to parse.
     */
    public ParameterizedPropertyParser(StyleProperty property) {
        super(property, 1);

        this.allowableKeywords = property.getStandardDetails().getAllowableKeywords();
        supportedTypes = property.getStandardDetails().getSupportedTypes();

        if (supportedTypes.contains(StyleValueType.PAIR) ||
                supportedTypes.contains(StyleValueType.LIST) ||
                supportedTypes.contains(StyleValueType.FRACTION)) {
            throw new IllegalStateException(
                    "Cannot support pairs, lists or fractions");
        }
    }

    // Javadoc inherited.
    public StyleValue convert(
            ParserContext context,
            StyleValueIterator iterator) {

        return context.convertAndConsume(supportedTypes, allowableKeywords, iterator);
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
