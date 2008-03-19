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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/css/renderer/css2/RuntimeKeywordMapperFactoryTestCase.java,v 1.2 2003/03/17 15:54:23 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Mar-03    Payal           VBM:2003030710 - Created to test the methods of 
 *                              RuntimeKeywordMapperFactory.
 * 17-Mar-03    Payal           VBM:2003030710 - Added javadoc.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.protocols.css.renderer.RuntimeBackgroundXPositionKeywordMapper;
import com.volantis.mcs.protocols.css.renderer.RuntimeBackgroundYPositionKeywordMapper;
import com.volantis.mcs.protocols.css.renderer.RuntimeKeywordMapperFactory;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import junit.framework.TestCase;

/**
 * This class unit test the RuntimeCSS2KeywordMapperFactoryclass.
 * @todo later make these test cases reflect the target class hierarchy
 */ 
public class RuntimeKeywordMapperFactoryTestCase extends TestCase {

    /**
     * Construct a new RuntimeKeywordMapperFactoryTestCase.
     * @param name
     */
    public RuntimeKeywordMapperFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Tests the KeywordMapper for the BackgroundXPosition property
     * is correct
     */
    public void testGetBackgroundXPositionKeywordMapper() {
        KeywordMapper mapper = RuntimeKeywordMapperFactory.getSingleton().
                getBackgroundXPositionKeywordMapper();

        assertEquals(mapper.getClass(), 
                     RuntimeBackgroundXPositionKeywordMapper.class);
    }

    /**
     * Tests the KeywordMapper for the BackgroundYPosition property
     * is correct
     */
    public void testGetBackgroundYPositionKeywordMapper() {
        KeywordMapper mapper = RuntimeKeywordMapperFactory.getSingleton().
                getBackgroundYPositionKeywordMapper();

        assertEquals(mapper.getClass(), 
                     RuntimeBackgroundYPositionKeywordMapper.class);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
