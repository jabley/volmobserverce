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
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.ContentKeywords;

import java.util.HashSet;
import java.util.Set;

/**
 * Parser for the content property.
 */
public class ContentParser
        extends AbstractKeywordOrListParser {

    /**
     * The converter for items in the list.
     */
    private static final ValueConverter ITEM_CONVERTER;

    static {
        Set supportedTypes = new HashSet();
        supportedTypes.add(StyleValueType.STRING);
        supportedTypes.add(StyleValueType.FUNCTION_CALL);
        supportedTypes.add(StyleValueType.URI);
        supportedTypes.add(StyleValueType.COMPONENT_URI);
        supportedTypes.add(StyleValueType.TRANSCODABLE_URI);
        supportedTypes.add(StyleValueType.KEYWORD);

        ITEM_CONVERTER = new SimpleValueConverter(supportedTypes,
                ContentKeywords.getDefaultInstance());
    }

    /**
     * Initialise.
     */
    public ContentParser() {
        super(StylePropertyDetails.CONTENT,
                ContentKeywords.getDefaultInstance(),
                ITEM_CONVERTER, false);
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
