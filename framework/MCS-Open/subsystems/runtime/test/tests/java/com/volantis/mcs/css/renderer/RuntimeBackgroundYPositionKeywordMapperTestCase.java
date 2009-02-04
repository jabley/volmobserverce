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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/css/renderer/css2/RuntimeBackgroundYPositionKeywordMapperTestCase.java,v 1.2 2003/03/17 15:54:23 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Mar-02    Payal           VBM:2003030710 - Created. A unit test to test 
 *                              the mapping for constants in 
 *                              RuntimeBackgroundYPositionKeywordMapper.
 * 17-Mar-03    Payal           VBM:2003030710 - Added javadoc.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.protocols.css.renderer.RuntimeBackgroundYPositionKeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.properties.BackgroundYPositionKeywords;
import junit.framework.TestCase;

/**
 * This class unit test the RuntimeBackgroundYPositionKeywordMapperclass.
 * @todo later make these test cases reflect the target class hierarchy
 */ 
public class RuntimeBackgroundYPositionKeywordMapperTestCase extends TestCase {

    /**
     * Construct a new RuntimeBackgroundYPositionKeywordMapperTestCase.
     * @param name
     */
    public RuntimeBackgroundYPositionKeywordMapperTestCase(String name) {
        super(name);
    }

    /**
     * Test that the mapping for constant BACKGROUND_YPOSITION__TOP is set 
     * up correctly.
     */
    public void testMapperTop() {
        KeywordMapper keywordMapper =
                RuntimeBackgroundYPositionKeywordMapper.getSingleton();

        assertEquals("0%", keywordMapper.
                getExternalString(BackgroundYPositionKeywords.TOP));
    }

    /**
     * Test that the mapping for constant BACKGROUND_YPOSITION__CENTER is set 
     * up correctly.
     */
    public void testMapperCenter() {
        KeywordMapper keywordMapper =
                RuntimeBackgroundYPositionKeywordMapper.getSingleton();

        assertEquals("50%", keywordMapper.
                getExternalString(BackgroundYPositionKeywords.CENTER));
    }

    /**
     * Test that the mapping for constant BACKGROUND_YPOSITION__BOTTOM is set 
     * up correctly.
     */
    public void testMapperBottom() {
        KeywordMapper keywordMapper =
                RuntimeBackgroundYPositionKeywordMapper.getSingleton();

        assertEquals("100%", keywordMapper.
                getExternalString(BackgroundYPositionKeywords.BOTTOM));
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

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
