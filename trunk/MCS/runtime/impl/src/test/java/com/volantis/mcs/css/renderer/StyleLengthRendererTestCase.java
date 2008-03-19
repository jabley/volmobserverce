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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/css/renderer/StyleLengthRendererTestCase.java,v 1.2 2003/03/19 09:52:58 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Mar-03    Byron           VBM:2003031105 - Created to test the render
 *                              method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.protocols.css.renderer.RuntimeCSSStyleSheetRenderer;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import junit.framework.TestCase;

import java.io.StringWriter;

/**
 * This class tests the StyleLengthRenderer class.
 */
public class StyleLengthRendererTestCase extends TestCase {

    private StyleLengthRenderer renderer;
    private static final StyleValueFactory STYLE_VALUE_FACTORY = StyleValueFactory.getDefaultInstance();

    /**
     * The constructor for this test case
     */
    public StyleLengthRendererTestCase(String name) {
        super(name);
    }


    protected void setUp() throws Exception {
        renderer = new StyleLengthRenderer();
    }

    protected void tearDown() throws Exception {
        renderer = null;
    }

    /**
     * Test the renderer.
     */
    public void testRender() throws Exception {
        doRendererTest(null, "");
        doBatchRenderTest(1.0);
        doBatchRenderTest(1.1);
        doBatchRenderTest(1.12);
        doBatchRenderTest(3.4);
        doBatchRenderTest(3.5); // Test rounding
        doBatchRenderTest(3.6);
        doBatchRenderTest(1.123);
        doBatchRenderTest(123.123);

        doBatchRenderTest(-0.10);
        doBatchRenderTest(-0.123);
        doBatchRenderTest(-1.123);
        doBatchRenderTest(-3.4);
        doBatchRenderTest(-3.5); // Test rounding
        doBatchRenderTest(-3.6);
        doBatchRenderTest(-123.123);

        doBatchRenderTest(22/7);

        doBatchZeroTest(0.0);
        doBatchZeroTest(-0.0);
    }

    /**
     * This method will test that the all the values defined as constants in
     * StyleLength are checked. Note that if the order is changed, or new items
     * are changed, this test case will fail as it should.
     *
     * @param value the value that will be used to check the outputted
     *              StyleLength
     */
    private void doBatchRenderTest(double value) throws Exception {
        assertEquals("All items are tested. Update this test case.",
                     8, LengthUnit.getCount());

        String strValue;
        int intValue = (int) Math.round(value);
        if (intValue == value) {
            strValue = Integer.toString(intValue);
        } else {
            strValue = Double.toString(value);
        }
        long longValue = Math.round(value);

        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.MM), strValue + "mm");
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.CM), strValue + "cm");
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.PT), strValue + "pt");
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.PC), strValue + "pc");
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.EM), strValue + "em");
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.EX), strValue + "ex");
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.PX), longValue + "px");
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.IN), strValue + "in");

    }

    /**
     * This method will test that the all the values defined as constants in
     * StyleLength are checked. Note that if the order is changed, or new items
     * are changed, this test case will fail as it should.
     *
     * @param value the value that will be used to check the outputted
     *              StyleLength
     */
    private void doBatchZeroTest(double value)
            throws Exception {

        assertEquals("All items are tested. Update this test case.",
                     8, LengthUnit.getCount());

        String strValue;
        int intValue = (int) Math.round(value);
        if (intValue == value) {
            strValue = Integer.toString(intValue);
        } else {
            strValue = Double.toString(value);
        }

        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.MM), strValue);
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.CM), strValue);
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.PT), strValue);
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.PC), strValue);
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.EM), strValue);
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.EX), strValue);
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.PX), strValue);
        doRendererTest(STYLE_VALUE_FACTORY.getLength(
            null, value, LengthUnit.IN), strValue);

    }

    /**
     * Tests the value rendered for a StyleLength is correct.
     */
    private void doRendererTest(StyleLength value, String expectedResult)
            throws Exception {
        StyleSheetRenderer styleSheetRenderer;
        StringWriter writer;
        RendererContext context;

        styleSheetRenderer = new RuntimeCSSStyleSheetRenderer();
        writer = new StringWriter();
        context = new RendererContext(writer, styleSheetRenderer);
        renderer.render(value, context);
        assertEquals(expectedResult, context.getWriter().toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10585/2	pduffin	VBM:2005112407 Fixed pair rendering issue, valign="baseline" and also fixed string rendering as well

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
