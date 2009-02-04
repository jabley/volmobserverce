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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/css/renderer/RendererContextTestCase.java,v 1.3 2003/03/19 09:52:58 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Mar-03    Payal           VBM:2003030710 - Created.A unit test to check 
 *                              values rendered for a StylePair.
 * 17-Mar-03    Payal           VBM:2003030710 - Added javadoc.
 * 19-Mar-03    Byron           VBM:2003031105 - Updated 3 testcases to check
 *                              for correct px values.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.protocols.css.renderer.RuntimeBackgroundXPositionKeywordMapper;
import com.volantis.mcs.protocols.css.renderer.RuntimeBackgroundYPositionKeywordMapper;
import com.volantis.mcs.protocols.css.renderer.RuntimeCSSStyleSheetRenderer;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BackgroundXPositionKeywords;
import com.volantis.mcs.themes.properties.BackgroundYPositionKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import junit.framework.TestCase;

import java.io.StringWriter;

/**
 * This class unit test the RendererContextClass.
 * @todo later make these test cases reflect the target class hierarchy
 */ 
public class RendererContextTestCase extends TestCase {

    /**
     * The object to use to create style values.
     */
    private StyleValueFactory styleValueFactory =
            StyleValueFactory.getDefaultInstance();

    /**
     * Construct a new RendererContextTestCase.
     * @param name
     */
    public RendererContextTestCase(String name) {
        super(name);
    }

    /**
     * Tests the values rendered for a StylePair is correct.
     */
    public void stylePairTest(StyleValue first, StyleValue second,
                              String expectedResult) throws Exception {
        StyleSheetRenderer styleSheetRenderer =
                new RuntimeCSSStyleSheetRenderer();
        StringWriter writer = new StringWriter();
        RendererContext context =
                new RendererContext(writer, styleSheetRenderer);
        context.setFirstKeywordMapper(
                RuntimeBackgroundXPositionKeywordMapper.getSingleton());
        context.setSecondKeywordMapper(
                RuntimeBackgroundYPositionKeywordMapper.getSingleton());

        StylePair pair = styleValueFactory.getPair(first, second);
        context.renderValue(pair);
        assertEquals(expectedResult, context.getWriter().toString());

    }

    /**
     * Tests the values rendered for left and bottom value is correct.
     */
    public void testRenderLeftBottomValue() throws Exception {
        StyleKeyword styleValueXPosition = BackgroundXPositionKeywords.LEFT;
        StyleKeyword styleValueYPosition = BackgroundYPositionKeywords.BOTTOM;
        stylePairTest(styleValueXPosition, styleValueYPosition, "0% 100%");
    }

    /**
     * Tests the values rendered for  a number and Top value is correct.
     */
    public void testRenderNumberTopValue() throws Exception {
        StyleLength styleValueXPosition = styleValueFactory.getLength(null, 20.0, LengthUnit.PX);

        StyleKeyword styleValueYPosition = BackgroundYPositionKeywords.TOP;
        stylePairTest(styleValueXPosition, styleValueYPosition, "20px 0%");
    }

    /**
     * Tests the values rendered for Center and number value is correct.
     */
    public void testRenderCenterNumberValue() throws Exception {
        StyleKeyword styleValueXPosition = BackgroundXPositionKeywords.CENTER;

        StyleLength styleValueYPosition = styleValueFactory.getLength(null, 32.1, LengthUnit.EM);
        stylePairTest(styleValueXPosition, styleValueYPosition, "50% 32.1em");
    }

    /**
     * Tests the values rendered for Right and number value is correct.
     */
    public void testRenderRightNumberValue() throws Exception {
        StyleKeyword styleValueXPosition = BackgroundXPositionKeywords.RIGHT;

        StyleLength styleValueYPosition = styleValueFactory.getLength(null, 18.1, LengthUnit.MM);
        stylePairTest(styleValueXPosition, styleValueYPosition, "100% 18.1mm");
    }

    /**
     * Tests the values rendered for number and Bottom value is correct.
     */
    public void testRenderNumberBottomValue() throws Exception {
        StyleLength styleValueXPosition = styleValueFactory.getLength(null, 12, LengthUnit.PX);

        StyleKeyword styleValueYPosition = BackgroundYPositionKeywords.BOTTOM;
        stylePairTest(styleValueXPosition, styleValueYPosition, "12px 100%");
    }

    /**
     * Tests the values rendered for number and Center value is correct.
     */
    public void testRenderNumberCenterValue() throws Exception {
        StyleLength styleValueXPosition = styleValueFactory.getLength(null, 3.1, LengthUnit.PT);

        StyleKeyword styleValueYPosition = BackgroundYPositionKeywords.CENTER;
        stylePairTest(styleValueXPosition, styleValueYPosition, "3.1pt 50%");
    }

    /**
     * Tests the values rendered for number and number value is correct.
     */
    public void testRenderStyleLengthPairValue() throws Exception {
        StyleLength styleValueXPosition = styleValueFactory.getLength(null, 2, LengthUnit.PX);

        StyleLength styleValueYPosition = styleValueFactory.getLength(null, 3.1, LengthUnit.EM);
        stylePairTest(styleValueXPosition, styleValueYPosition, "2px 3.1em");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
