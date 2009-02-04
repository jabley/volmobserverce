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
 * $Header: /src/voyager/com/volantis/mcs/css/emulator/mappers/waptv5/TextAlignKeywordMapper.java,v 1.2 2002/07/29 16:01:28 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Jul-02    Ian             VBM:2002072603 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.css.emulator.mappers.waptv5;

import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.properties.TextAlignKeywords;


/**
 * Map a keyword value from its internal representation its
 * canonical external representation.
 */
public class TextAlignKeywordMapper
        extends DefaultKeywordMapper {

    private static KeywordMapper singleton;

    static {
        // Always initialise to prevent the synchronization
        // problem that could occur through lazy initialisation
        singleton = new TextAlignKeywordMapper();
    }

    public static KeywordMapper getSingleton() {
        return singleton;
    }

    protected TextAlignKeywordMapper() {
        addMapping(TextAlignKeywords.LEFT, "l");
        addMapping(TextAlignKeywords.RIGHT, "r");
        addMapping(TextAlignKeywords.CENTER, "c");
        addMapping(TextAlignKeywords.JUSTIFY, "j");
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

 05-Jan-04	2326/2	geoff	VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors

 ===========================================================================
*/
