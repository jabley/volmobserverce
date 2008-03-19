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

import com.volantis.mcs.css.impl.parser.Separator;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleTime;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.properties.MCSMarqueeSpeedKeywords;
import com.volantis.mcs.themes.values.TimeUnit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Parser for the mcs-marquee-speed property.
 */
public class MarqueeSpeedParser extends AbstractSinglePropertyParser {

    /**
     * Converter to handle {@link com.volantis.mcs.themes.StyleLength}.
     */
    private static final PairComponentValueHandler LENGTH_CONVERTER;

    /**
     * Converter to handle {@link StyleTime}.
     */
    private static final PairComponentValueHandler TIME_CONVERTER;

    /**
     * Empty {@link AllowableKeywords} instance.
     */
    private static final AllowableKeywords NO_ALLOWABLE_KEYWORDS =
            new AllowableKeywords(new ArrayList());

    /**
     * Set of keywords which are allowed for the length and time components
     * respectively.
     */
    private static final HashSet LENGTH_SUPPORT = new HashSet(1);
    private static final HashSet TIME_SUPPORT = new HashSet(1);

    /**
     * Initial value for the {@link StyleTime} component of the marquee speed.
     */
    private static final StyleTime INITIAL_TIME =
        StyleValueFactory.getDefaultInstance().getTime(null, 1, TimeUnit.S);

    /**
     * {@link PairValueConverter} which will be used to convert fraction values
     * for the mcs-marquee-speed property, and will be composed from the two
     * {@link PairComponentValueHandler} instances.
     */
    private static final PairValueConverter FRACTION_CONVERTER;

    /**
     * Set which indicates that keywords are supported by this property.
     */
    private static Set SUPPORTED_TYPES_KEYWORD = new HashSet();

    static {
        LENGTH_SUPPORT.add(StyleValueType.LENGTH);
        TIME_SUPPORT.add(StyleValueType.TIME);
        LENGTH_CONVERTER = new SimplePairComponentValueHandler(LENGTH_SUPPORT,
                NO_ALLOWABLE_KEYWORDS, null);
        TIME_CONVERTER = new SimplePairComponentValueHandler(TIME_SUPPORT,
                NO_ALLOWABLE_KEYWORDS, INITIAL_TIME);
        FRACTION_CONVERTER = new PairValueConverter(LENGTH_CONVERTER,
                TIME_CONVERTER, Separator.SLASH, StyleValueType.FRACTION);
        SUPPORTED_TYPES_KEYWORD.add(StyleValueType.KEYWORD);
    }

    /**
     * Initialise.
     */
    public MarqueeSpeedParser() {
        // This property cannot have more than two values, so use that as the
        // expected value count.
        super(StylePropertyDetails.MCS_MARQUEE_SPEED, 2);
    }

    // Javadoc inherited.
    public StyleValue convert(
            ParserContext context, StyleValueIterator iterator) {

        // Handle a simple keyword value.
        StyleValue value = context.convertAndConsume(
                SUPPORTED_TYPES_KEYWORD,
                MCSMarqueeSpeedKeywords.getDefaultInstance(),
                iterator);
        if (value == null) {
            // If it's not a keyword, then try as a fraction.
            value = FRACTION_CONVERTER.convert(context, iterator);
        }
        return value;
    }
}
