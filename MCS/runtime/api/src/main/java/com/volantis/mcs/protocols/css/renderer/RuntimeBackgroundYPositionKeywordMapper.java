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
 * $Header: /src/voyager/com/volantis/mcs/protocols/css/renderer/css2/RuntimeBackgroundYPositionKeywordMapper.java,v 1.1 2003/03/13 12:27:00 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Mar-03    Payal             VBM:2003030710 - Created.Maps keyword value
 *                                from its internal representation its
 *                                canonical external representation.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css.renderer;

import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.properties.BackgroundYPositionKeywords;


/**
 * Map a keyword value from its internal representation its
 * canonical external representation.
 */
public final class RuntimeBackgroundYPositionKeywordMapper
        extends DefaultKeywordMapper {

    /**
     * The singleton instance of RuntimeBackgroundYPositionKeywordMapper.
     */
    private static final KeywordMapper singleton;

    static {
        // Always initialise to prevent the synchronization
        // problem that could occur through lazy initialisation
        singleton = new RuntimeBackgroundYPositionKeywordMapper();
    }

    /**
     * Get the singleton instance of this
     * RuntimeBackgroundYPositionKeywordMapper.
     *
     * @return the RuntimeBackgroundYPositionKeywordMapper instance
     */
    public static KeywordMapper getSingleton() {
        return singleton;
    }

    /**
     * Construct a new RuntimeBackgroundYPositionKeywordMapper.
     */
    private RuntimeBackgroundYPositionKeywordMapper() {
        addMapping(BackgroundYPositionKeywords.TOP, "0%");
        addMapping(BackgroundYPositionKeywords.CENTER, "50%");
        addMapping(BackgroundYPositionKeywords.BOTTOM, "100%");
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
