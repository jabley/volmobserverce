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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import java.io.StringWriter;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.mappers.CSSPropertyNameMapper;
import com.volantis.mcs.themes.properties.MCSMarqueeStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMenuImageStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMenuItemActiveAreaKeywords;
import com.volantis.mcs.themes.properties.MCSMenuItemIteratorAllocationKeywords;
import com.volantis.mcs.themes.properties.MCSMenuItemOrderKeywords;
import com.volantis.mcs.themes.properties.MCSMenuItemOrientationKeywords;
import com.volantis.mcs.themes.properties.MCSMenuStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMenuTextStyleKeywords;
import com.volantis.mcs.utilities.ReusableStringWriter;
import com.volantis.styling.properties.StyleProperty;

/**
 * Test the simple property renderer.
 */
public class SimplePropertyRendererTestCase
        extends PropertyRendererTestAbstract {

    /**
     * Test the rendering of the menu style.
     */
    public void testRenderingMenuStyle() throws Exception {
        doTest(StylePropertyDetails.MCS_MENU_STYLE,
                MCSMenuStyleKeywords.DYNAMIC,
                "dynamic;");

        context = new RendererContext(new StringWriter(), styleSheetRenderer);
        doTest(StylePropertyDetails.MCS_MENU_STYLE,
                MCSMenuStyleKeywords.STATIC,
                "static;");
    }

    /**
     * Test the rendering of the menu item active area style.
     */
    public void testRenderingMenuItemActiveArea() throws Exception {
        doTest(StylePropertyDetails.MCS_MENU_ITEM_ACTIVE_AREA,
                MCSMenuItemActiveAreaKeywords.BOTH,
                "both;");

        context = new RendererContext(new StringWriter(), styleSheetRenderer);
        doTest(StylePropertyDetails.MCS_MENU_ITEM_ACTIVE_AREA,
                MCSMenuItemActiveAreaKeywords.IMAGE_ONLY,
                "image-only;");

        context = new RendererContext(new StringWriter(), styleSheetRenderer);
        doTest(StylePropertyDetails.MCS_MENU_ITEM_ACTIVE_AREA,
                MCSMenuItemActiveAreaKeywords.TEXT_ONLY,
                "text-only;");
    }

    /**
     * Test the rendering of the menu item orientation style.
     */
    public void testRenderingMenuItemOrientation() throws Exception {
        doTest(StylePropertyDetails.MCS_MENU_ITEM_ORIENTATION,
                MCSMenuItemOrientationKeywords.HORIZONTAL,
                "horizontal;");

        context = new RendererContext(new StringWriter(), styleSheetRenderer);
        doTest(StylePropertyDetails.MCS_MENU_ITEM_ORIENTATION,
                MCSMenuItemOrientationKeywords.VERTICAL,
                "vertical;");
    }

    /**
     * Test the rendering of the menu item order style.
     */
    public void testRenderingMenuItemOrder() throws Exception {
        doTest(StylePropertyDetails.MCS_MENU_ITEM_ORDER,
                MCSMenuItemOrderKeywords.IMAGE_FIRST,
                "image-first;");

        context = new RendererContext(new StringWriter(), styleSheetRenderer);
        doTest(StylePropertyDetails.MCS_MENU_ITEM_ORDER,
                MCSMenuItemOrderKeywords.TEXT_FIRST,
                "text-first;");
    }

    /**
     * Test the rendering of the menu item iterator style.
     */
    public void testRenderingMenuItemIteratorAllocation() throws Exception {
        doTest(StylePropertyDetails.MCS_MENU_ITEM_ITERATOR_ALLOCATION,
                MCSMenuItemIteratorAllocationKeywords.AUTOMATIC,
                "automatic;");

        context = new RendererContext(new StringWriter(), styleSheetRenderer);
        doTest(StylePropertyDetails.MCS_MENU_ITEM_ITERATOR_ALLOCATION,
                MCSMenuItemIteratorAllocationKeywords.NONE,
                "none;");
    }

    /**
     * Test the rendering of the menu text style.
     */
    public void testRenderingMenuTextStyle() throws Exception {
        doTest(StylePropertyDetails.MCS_MENU_TEXT_STYLE,
                MCSMenuTextStyleKeywords.NONE,
                "none;");

        context = new RendererContext(new StringWriter(), styleSheetRenderer);
        doTest(StylePropertyDetails.MCS_MENU_TEXT_STYLE,
                MCSMenuTextStyleKeywords.PLAIN,
                "plain;");
    }

    /**
     * Test the rendering of the menu image style.
     */
    public void testRenderingMenuImageStyle() throws Exception {
        doTest(StylePropertyDetails.MCS_MENU_IMAGE_STYLE,
                MCSMenuImageStyleKeywords.NONE,
                "none;");

        context = new RendererContext(new StringWriter(), styleSheetRenderer);
        doTest(StylePropertyDetails.MCS_MENU_IMAGE_STYLE,
                MCSMenuImageStyleKeywords.PLAIN,
                "plain;");

        context = new RendererContext(new StringWriter(), styleSheetRenderer);
        doTest(StylePropertyDetails.MCS_MENU_IMAGE_STYLE,
                MCSMenuImageStyleKeywords.ROLLOVER,
                "rollover;");
    }

    /**
     * Verify that a property, which has a mapping defined from its internal
     * MCS name to the external CSS name, is rendered using the correct
     * external representation.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testRenderPropertyWithExternalMapping() throws Exception {

        StyleProperty property = StylePropertyDetails.MCS_MARQUEE_STYLE;
        StyleValue value = MCSMarqueeStyleKeywords.SCROLL;

        SimplePropertyRenderer renderer = new SimplePropertyRenderer(
                property);

        properties.setStyleValue(property,  value);

        renderer.render(properties, context);

        String expectedName = CSSPropertyNameMapper.getDefaultInstance().
                getExternalString(property);
        final String expected = expectedName +":" + value.getStandardCSS() +";";
        final String result = ((ReusableStringWriter) context.getWriter())
                .getBuffer().toString();
        assertEquals("Rendered value is incorrect: "  + expected, expected,
                result);
    }

    /**
     * Test the rendering using the style property detail and enumeration type.
     */
    private void doTest(StyleProperty stylePropertyDetails,
                        StyleKeyword styleKeyword,
                        String expected)
            throws Exception {
        SimplePropertyRenderer renderer = new SimplePropertyRenderer(
                stylePropertyDetails);

        properties.setStyleValue(stylePropertyDetails, styleKeyword);

        renderer.render(properties, context);

        expected = stylePropertyDetails.getName() + ":" + expected;
        final String result = ((ReusableStringWriter) context.getWriter())
                .getBuffer().toString();
        assertEquals("Rendered value is incorrect: "  + expected, expected,
                result);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 28-Apr-04	3937/1	byron	VBM:2004032308 Enhance Menu Support: Theme Changes: Update renderers and parsers

 ===========================================================================
*/
