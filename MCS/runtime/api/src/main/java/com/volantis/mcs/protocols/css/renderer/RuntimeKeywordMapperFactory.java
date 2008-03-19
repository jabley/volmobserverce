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
 * $Header: /src/voyager/com/volantis/mcs/protocols/css/renderer/css2/RuntimeKeywordMapperFactory.java,v 1.1 2003/03/13 12:27:00 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Mar-03    Payal             VBM:2003030710 - Created.This class extends 
 *                                CSSKeywordMapperFactory
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css.renderer;

import com.volantis.mcs.css.mappers.CSSKeywordMapperFactory;
import com.volantis.mcs.runtime.css.emulator.mappers.DisplayKeywordMapper;
import com.volantis.mcs.runtime.css.emulator.mappers.MCSMarqueeDirectionKeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapperFactory;

/**
 * This class extends CSSKeywordMapperFactory.
 */
public class RuntimeKeywordMapperFactory
        extends CSSKeywordMapperFactory {

    /**
     * TThe singleton instance of RuntimeKeywordMapperFactory.
     */
    private static final KeywordMapperFactory singleton;

    static {
        // Always initialise to prevent the synchronization
        // problem that could occur through lazy initialisation
        singleton = new RuntimeKeywordMapperFactory();
    }

    /**
     * Construct a new RuntimeKeywordMapperFactory.
     */
    protected RuntimeKeywordMapperFactory() {
    }

    /**
     * Get the singleton instance of this  
     * RuntimeKeywordMapperFactory.
     * @return the RuntimeKeywordMapperFactory instance
     */
    public static KeywordMapperFactory getSingleton() {
        return singleton;
    }

    // javadoc inherited
    public KeywordMapper getBackgroundXPositionKeywordMapper() {
        return RuntimeBackgroundXPositionKeywordMapper.getSingleton();
    }

    // javadoc inherited
    public KeywordMapper getBackgroundYPositionKeywordMapper() {
        return RuntimeBackgroundYPositionKeywordMapper.getSingleton();
    }

    // javadoc inherited
    public KeywordMapper getMCSMarqueeDirectionKeywordMapper() {
        return MCSMarqueeDirectionKeywordMapper.getSingleton();
    }

    // javadoc inherited
    public KeywordMapper getDisplayKeywordMapper() {
        return DisplayKeywordMapper.getSingleton();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 17-Dec-03	2242/1	andy	VBM:2003121702 vbm2003121702

 30-Sep-03	1475/1	byron	VBM:2003092606 Move contents of accessors.xml package to jdom package

 ===========================================================================
*/
