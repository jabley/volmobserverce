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
 * $Header: /src/voyager/com/volantis/mcs/css/emulator/mappers/html32/HTMLVersion3_2KeywordMapperFactory.java,v 1.3 2003/03/13 12:27:00 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jun-02    Adrian          VBM:2002040808 - Created this factory to return
 *                              the correct keyword mappers for
 *                              HTMLVersion3_2Style.
 * 28-Jun-02    Paul            VBM:2002051302 - The css specific mappers have
 *                              moved from themes.mappers to css.mappers.
 * 13-Mar-03    Payal           VBM:2003030710 - Modified class to extend 
 *                              RuntimeCSS2KeywordMapperFactory instead of
 *                              CSS2KeywordMapperFactory.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.css.emulator.mappers.html32;

import com.volantis.mcs.protocols.css.renderer.RuntimeKeywordMapperFactory;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapperFactory;

public class HTMLVersion3_2KeywordMapperFactory
        extends RuntimeKeywordMapperFactory {


    private static KeywordMapperFactory singleton;

    static {
        // Always initialise to prevent the synchronization
        // problem that could occur through lazy initialisation
        singleton = new HTMLVersion3_2KeywordMapperFactory();
    }

    protected HTMLVersion3_2KeywordMapperFactory() {
    }

    public static KeywordMapperFactory getSingleton() {
        return singleton;
    }

    // javadoc inherited
    public KeywordMapper getBorderWidthUnitsKeywordMapper() {
        return BorderWidthUnitsKeywordMapper.getSingleton();
    }

    // javadoc inherited
    public KeywordMapper getFontSizeKeywordMapper() {
        return FontSizeKeywordMapper.getSingleton();
    }

    // javadoc inherited
    public KeywordMapper getListStyleTypeKeywordMapper() {
        return ListStyleTypeKeywordMapper.getSingleton();
    }

    // javadoc inherited
    public KeywordMapper getMarinerMMFlashAlignKeywordMapper() {
        return MarinerMMFlashAlignKeywordMapper.getSingleton();
    }

    // javadoc inherited
    public KeywordMapper getMarinerMMFlashXScaledAlignKeywordMapper() {
        return MarinerMMFlashXScaledAlignKeywordMapper.getSingleton();
    }

    // javadoc inherited
    public KeywordMapper getMarinerMMFlashYScaledAlignKeywordMapper() {
        return MarinerMMFlashYScaledAlignKeywordMapper.getSingleton();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/3	ianw	VBM:2004090605 New Build system

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
