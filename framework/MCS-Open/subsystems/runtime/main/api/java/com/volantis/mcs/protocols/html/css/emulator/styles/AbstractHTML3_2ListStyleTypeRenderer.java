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

package com.volantis.mcs.protocols.html.css.emulator.styles;

import com.volantis.mcs.protocols.styles.AbstractPropertyRenderer;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.styling.values.MutablePropertyValues;

public class AbstractHTML3_2ListStyleTypeRenderer
        extends AbstractPropertyRenderer {

    protected final static DefaultKeywordMapper listStyleKeywordMapper;
    protected final static DefaultKeywordMapper orderedListStyleKeywordMapper;
    protected final static DefaultKeywordMapper unorderedListStyleKeywordMapper;

    static {
        listStyleKeywordMapper = new DefaultKeywordMapper();
        orderedListStyleKeywordMapper = new DefaultKeywordMapper();
        unorderedListStyleKeywordMapper = new DefaultKeywordMapper();

        addUnorderedMapping(ListStyleTypeKeywords.DISC, "disc");
        addUnorderedMapping(ListStyleTypeKeywords.CIRCLE, "circle");
        addUnorderedMapping(ListStyleTypeKeywords.SQUARE, "square");
        addOrderedMapping(ListStyleTypeKeywords.DECIMAL, "1");
        addOrderedMapping(ListStyleTypeKeywords.DECIMAL_LEADING_ZERO, "1");
        addOrderedMapping(ListStyleTypeKeywords.LOWER_ROMAN, "i");
        addOrderedMapping(ListStyleTypeKeywords.UPPER_ROMAN, "I");
        addOrderedMapping(ListStyleTypeKeywords.LOWER_ALPHA, "a");
        addOrderedMapping(ListStyleTypeKeywords.UPPER_ALPHA, "A");
    }

    private static void addOrderedMapping(
            StyleKeyword keyword, String external) {
        listStyleKeywordMapper.addMapping(keyword, external);
        orderedListStyleKeywordMapper.addMapping(keyword, external);
    }

    private static void addUnorderedMapping(
            StyleKeyword keyword, String external) {
        listStyleKeywordMapper.addMapping(keyword, external);
        unorderedListStyleKeywordMapper.addMapping(keyword, external);
    }

    private final KeywordMapper mapper;

    protected AbstractHTML3_2ListStyleTypeRenderer(KeywordMapper mapper) {
        this.mapper = mapper;
    }

    public String getAsString(MutablePropertyValues propertyValues) {
        StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.LIST_STYLE_TYPE);
        return mapper.getExternalString(value);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
