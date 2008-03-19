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

package com.volantis.mcs.protocols.html.htmlimode;

import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.properties.TextAlignKeywords;

/**
 * Extend XHTMLBasic one but make it ignore left as that is the default.
 */
public class HTMLiModeDivHorizontalAlignKeywordMapper
        extends DefaultKeywordMapper {

    private static final KeywordMapper DEFAULT_INSTANCE =
            new HTMLiModeDivHorizontalAlignKeywordMapper();

    public static KeywordMapper getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private HTMLiModeDivHorizontalAlignKeywordMapper() {
        addStandardMapping(TextAlignKeywords.CENTER);
        addStandardMapping(TextAlignKeywords.JUSTIFY);
        addStandardMapping(TextAlignKeywords.RIGHT);
        addMapping(TextAlignKeywords.LEFT, null);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
