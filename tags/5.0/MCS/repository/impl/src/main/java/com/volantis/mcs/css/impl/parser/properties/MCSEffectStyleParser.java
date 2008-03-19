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

import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.css.impl.parser.Separator;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.MCSEffectStyleKeywords;

import java.util.HashSet;
import java.util.Set;

/**
 * Parser for the mcs-effect-style property.
 */
public class MCSEffectStyleParser
    extends AbstractListParser {

    /**
     * The converter for items in the list.
     */
    private static final ValueConverter ITEM_CONVERTER;
    static {
        Set supportedTypes = new HashSet();
        supportedTypes.add(StyleValueType.STRING);
        supportedTypes.add(StyleValueType.KEYWORD);

        ITEM_CONVERTER = new SimpleValueConverter(supportedTypes,
                MCSEffectStyleKeywords.getDefaultInstance());
    }

    /**
     * Initialise.
     */
    public MCSEffectStyleParser(PropertyParserFactory factory) {
        super(StylePropertyDetails.MCS_EFFECT_STYLE, ITEM_CONVERTER, true);
    }
            
    // Javadoc inherited.
    protected void checkSeparator(
            ParserContext context, StyleValueIterator iterator) {
        iterator.separator(Separator.COMMA);
    }
}
