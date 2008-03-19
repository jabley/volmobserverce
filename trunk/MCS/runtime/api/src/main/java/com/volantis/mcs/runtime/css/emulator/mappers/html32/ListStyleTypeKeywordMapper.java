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
 * $Header: /src/voyager/com/volantis/mcs/css/emulator/mappers/html32/ListStyleTypeKeywordMapper.java,v 1.1 2002/05/14 09:03:31 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-May-02    Adrian          VBM:2002040808 - Created this keyword mapper to
 *                              render listStyleType values appropriate to the
 *                              HTMLVersion3_2 protocol.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.css.emulator.mappers.html32;

import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;


/**
 * Map a keyword value from its internal representation its
 * canonical external representation.
 */
public class ListStyleTypeKeywordMapper
        extends DefaultKeywordMapper {

    private static KeywordMapper singleton;

    static {
        // Always initialise to prevent the synchronization
        // problem that could occur through lazy initialisation
        singleton = new ListStyleTypeKeywordMapper();
    }

    public static KeywordMapper getSingleton() {
        return singleton;
    }

    protected ListStyleTypeKeywordMapper() {
        addMapping(ListStyleTypeKeywords.NONE, "none");
        addMapping(ListStyleTypeKeywords.DISC, "disc");
        addMapping(ListStyleTypeKeywords.CIRCLE, "circle");
        addMapping(ListStyleTypeKeywords.SQUARE, "square");
        addMapping(ListStyleTypeKeywords.DECIMAL, "1");
        addMapping(ListStyleTypeKeywords.DECIMAL_LEADING_ZERO, "1");
        addMapping(ListStyleTypeKeywords.LOWER_ROMAN, "i");
        addMapping(ListStyleTypeKeywords.UPPER_ROMAN, "I");
        addMapping(ListStyleTypeKeywords.LOWER_GREEK, "none");
        addMapping(ListStyleTypeKeywords.LOWER_ALPHA, "a");
        addMapping(ListStyleTypeKeywords.LOWER_LATIN, "none");
        addMapping(ListStyleTypeKeywords.UPPER_ALPHA, "A");
        addMapping(ListStyleTypeKeywords.UPPER_LATIN, "none");
        addMapping(ListStyleTypeKeywords.HEBREW, "none");
        addMapping(ListStyleTypeKeywords.ARMENIAN, "none");
        addMapping(ListStyleTypeKeywords.GEORGIAN, "none");
        addMapping(ListStyleTypeKeywords.CJK_IDEOGRAPHIC, "none");
        addMapping(ListStyleTypeKeywords.HIRAGANA, "none");
        addMapping(ListStyleTypeKeywords.KATAKANA, "none");
        addMapping(ListStyleTypeKeywords.HIRAGANA_IROHA, "none");
        addMapping(ListStyleTypeKeywords.KATAKANA_IROHA, "none");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/1	ianw	VBM:2004090605 New Build system

 ===========================================================================
*/
