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

package com.volantis.mcs.protocols.html.xhtmlbasic;

import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;

public class XHTMLBasicTableCellVerticalAlignKeywordMapper
        extends DefaultKeywordMapper {

    private static final KeywordMapper DEFAULT_INSTANCE =
            new XHTMLBasicTableCellVerticalAlignKeywordMapper();

    public static KeywordMapper getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private XHTMLBasicTableCellVerticalAlignKeywordMapper() {
        addStandardMapping(VerticalAlignKeywords.TOP);
        addStandardMapping(VerticalAlignKeywords.MIDDLE);
        addStandardMapping(VerticalAlignKeywords.BOTTOM);
        //addStandardMapping(VerticalAlignKeywords.BASELINE);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10585/1	pduffin	VBM:2005112407 Fixed pair rendering issue, valign="baseline" and also fixed string rendering as well

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
