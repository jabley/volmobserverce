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
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.properties.MCSMMFlashXScaledAlignKeywords;
import com.volantis.mcs.themes.properties.MCSMMFlashYScaledAlignKeywords;

import java.util.HashSet;
import java.util.Set;

/**
 * Parser for the mcs-mmflash-scaled-align property.
 */
public class MCSMMFlashScaledAlignParser
        extends AbstractSinglePropertyParser {

    /**
     * The converter for the pair of values.
     */
    private static final PairValueConverter CONVERTER;
    static {
        StyleValue initialValue;
        AllowableKeywords allowableKeywords;
        Set supportedTypes;

        supportedTypes = new HashSet();
        supportedTypes.add(StyleValueType.KEYWORD);

        // Set up the X component.
        initialValue = MCSMMFlashXScaledAlignKeywords.CENTER;

        allowableKeywords = MCSMMFlashXScaledAlignKeywords.getDefaultInstance();

        PairComponentValueHandler xHandler =
                new SimplePairComponentValueHandler(
                        supportedTypes, allowableKeywords, initialValue);

        // Set up the Y component.
        initialValue = MCSMMFlashYScaledAlignKeywords.CENTER;

        allowableKeywords = MCSMMFlashYScaledAlignKeywords.getDefaultInstance();

        PairComponentValueHandler yHandler =
                new SimplePairComponentValueHandler(
                        supportedTypes, allowableKeywords, initialValue);

        CONVERTER = new PairValueConverter(xHandler, yHandler);
    }

    /**
     * Initialise.
     */
    public MCSMMFlashScaledAlignParser() {
        super(StylePropertyDetails.MCS_MMFLASH_SCALED_ALIGN, 2);
    }

    // Javadoc inherited.
    public StyleValue convert(
            ParserContext context, StyleValueIterator iterator) {
        return CONVERTER.convert(context, iterator);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/1	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
