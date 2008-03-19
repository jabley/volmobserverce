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
 * $Header: /src/voyager/com/volantis/mcs/css/emulator/mappers/waptv5/WapTV5_WMLVersion1_3KeywordMapperFactory.java,v 1.5 2003/03/13 12:27:00 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Jul-02    Ian             VBM:2002072603 - Created.
 * 08-Aug-02    Ian             VBM:2002080603 - Added whiteSpaceKeywordMapper
 * 16-Sep-02    Ian             VBM:2002091006 - Added 
 *                              MarinerFocusKeywordMapper.
 * 13-Mar-03    Payal           VBM:2003030710 - Modified class to extend 
 *                              RuntimeCSS2KeywordMapperFactory instead of
 *                              CSS2KeywordMapperFactory.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.css.emulator.mappers.waptv5;

import com.volantis.mcs.protocols.css.renderer.RuntimeKeywordMapperFactory;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapperFactory;

public class WapTV5_WMLVersion1_3KeywordMapperFactory
        extends RuntimeKeywordMapperFactory {

    private static KeywordMapperFactory singleton;

    static {
        // Always initialise to prevent the synchronization
        // problem that could occur through lazy initialisation
        singleton = new WapTV5_WMLVersion1_3KeywordMapperFactory();
    }

    /**
     * Get the singleton instance of this
     * RuntimeKeywordMapperFactory.
     *
     * @return the RuntimeKeywordMapperFactory instance
     */
    public static KeywordMapperFactory getSingleton() {
        return singleton;
    }

    protected WapTV5_WMLVersion1_3KeywordMapperFactory() {
    }

    // javadoc inherited
    public KeywordMapper getFontSizeKeywordMapper() {
        return FontSizeKeywordMapper.getSingleton();
    }

    // javadoc inherited
    public KeywordMapper getMarinerFocusKeywordMapper() {
        return MarinerFocusKeywordMapper.getSingleton();
    }

    // javadoc inherited
    public KeywordMapper getTextAlignKeywordMapper() {
        return TextAlignKeywordMapper.getSingleton();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/3	ianw	VBM:2004090605 New Build system

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
