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

import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.styling.properties.StyleProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for the counter parsers.
 */
public abstract class AbstractCounterParser extends AbstractKeywordOrListParser {

    /**
     * The converter for handling the identifier part of the counter.
     */
    private static final PairComponentValueHandler COUNTER_IDENTIFIER_CONVERTER;

    static {
        Set supportedTypes = new HashSet();
        supportedTypes.add(StyleValueType.IDENTIFIER);

        COUNTER_IDENTIFIER_CONVERTER = new SimplePairComponentValueHandler(
                supportedTypes, null, null);
    }

    /**
     * Create a converter that will handle converting a counter definition
     * into a single value.
     *
     * @param initialValue The initial value that will be used if the integer
     *                     value has not been specified. This will depend on
     *                     whether it is a counter increment or reset.
     * @return A value converter that will handle the
     */
    protected static ValueConverter createCounterDefinitionConverter(
            final int initialValue) {

        PairComponentValueHandler integerConverter;
        Set supportedTypes = new HashSet();
        supportedTypes.add(StyleValueType.INTEGER);
        integerConverter = new SimplePairComponentValueHandler(
                supportedTypes, null,
                STYLE_VALUE_FACTORY.getInteger(null, initialValue));

        return new PairValueConverter(COUNTER_IDENTIFIER_CONVERTER,
                                      integerConverter);
    }


    /**
     * Initialise.
     *
     * @param property  The property that should be set by this parser.
     * @param allowableKeywords    The mapper to use to convert keywords.
     * @param converter The converter to use to convert counter definition
     */
    public AbstractCounterParser(
            StyleProperty property, AllowableKeywords allowableKeywords,
            ValueConverter converter) {
        super(property, allowableKeywords, converter, false);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
