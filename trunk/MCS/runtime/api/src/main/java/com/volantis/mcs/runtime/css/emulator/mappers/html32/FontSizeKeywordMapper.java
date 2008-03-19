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
 * $Header: /src/voyager/com/volantis/mcs/css/emulator/mappers/html32/FontSizeKeywordMapper.java,v 1.3 2002/05/14 15:37:47 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Apr-02    Adrian          VBM:2002040808 - Created this keyword mapper to
 *                              render font size values appropriate to the
 *                              HTMLVersion3_2 protocol.
 * 10-May-02    Adrian          VBM:2002040808 - Call superclass constructor
 *                              with MAX + 1
 * 13-May-02    Adrian          VBM:2002040808 - assign static instantiation to
 *                              class member variable instead of local var in
 *                              static.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.css.emulator.mappers.html32;

import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.properties.FontSizeKeywords;


/**
 * Map a keyword value from its internal representation its
 * canonical external representation.
 */
public class FontSizeKeywordMapper
        extends DefaultKeywordMapper {

    private static KeywordMapper singleton;

    static {
        // Always initialise to prevent the synchronization
        // problem that could occur through lazy initialisation
        singleton = new FontSizeKeywordMapper();
    }

    public static KeywordMapper getSingleton() {
        return singleton;
    }

    protected FontSizeKeywordMapper() {
        addMapping(FontSizeKeywords.XX_SMALL, "1");
        addMapping(FontSizeKeywords.X_SMALL, "1");
        addMapping(FontSizeKeywords.SMALL, "2");
        addMapping(FontSizeKeywords.MEDIUM, "3");
        addMapping(FontSizeKeywords.LARGE, "4");
        addMapping(FontSizeKeywords.X_LARGE, "5");
        addMapping(FontSizeKeywords.XX_LARGE, "7");
        addMapping(FontSizeKeywords.LARGER, "larger");
        addMapping(FontSizeKeywords.SMALLER, "smaller");
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
