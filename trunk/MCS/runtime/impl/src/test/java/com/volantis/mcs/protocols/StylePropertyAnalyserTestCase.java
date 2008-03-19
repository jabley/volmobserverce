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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.themes.StylePropertyDetails;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 */
public class StylePropertyAnalyserTestCase extends StylePropertyAnalyserTestCaseAbstract {

    public void testNoImportantProperty() {
        assertFalse(getAnalyser().hasVisuallyImportantProperty(
            Collections.EMPTY_SET, null));
    }

    public void testIncorrectProperty() {
        try {
            getAnalyser().hasVisuallyImportantProperty(
                Collections.singletonList(StylePropertyDetails.BACKGROUND_REPEAT),
                null);
            fail("Unknown style property, exception expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testBackgroundColorAnalyser() {
        final List properties =
            Collections.singletonList(StylePropertyDetails.BACKGROUND_COLOR);

        // initial value is transparent
        Element element = getRootElement("<div/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit transparent
        element = getRootElement("<div style=\"background-color:transparent\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // normal value
        element = getRootElement("<div style=\"background-color:red\"/>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // same as parent value
        element = (Element) getRootElement(
            "<div style=\"background-color:red\">" +
                "<div style=\"background-color:red\"/>" +
            "</div>").getHead();
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // same as parent value, but parent has background image
        element = (Element) getRootElement(
            "<div style=\"background-color:red; background-image:url(\'marble.png\') \">" +
                "<div style=\"background-color:red\"/>" +
            "</div>").getHead();
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));
    }

    public void testBackgroundImageAnalyser() {
        final List properties =
            Collections.singletonList(StylePropertyDetails.BACKGROUND_IMAGE);

        // initial value is none
        Element element = getRootElement("<div/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit none
        element = getRootElement("<div style=\"background-image:none\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // normal value
        element = getRootElement(
            "<div style=\"background-image:url('marble.png')\"/>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));
    }

    public void testBorderWidthAnalysers() {
        testBorderWidth("bottom");
        testBorderWidth("left");
        testBorderWidth("right");
        testBorderWidth("top");
    }

    private void testBorderWidth(final String side) {
        final List properties = Collections.singletonList(
            StylePropertyDetails.getDefinitions().getStyleProperty(
                "border-" + side + "-width"));

        // initial value is medium, but with none style
        Element element = getRootElement("<div/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit medium
        element = getRootElement("<div style=\"border-" + side +
            "-width:medium\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit medium with style
        element = getRootElement("<div style=\"border-" + side +
            "-width:medium; border-" + side + "-style:solid\"/>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // 0px value
        element = getRootElement("<div style=\"border-" + side +
            "-width:0px; border-" + side + "-style:solid\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // 0cm value
        element = getRootElement("<div style=\"border-" + side +
            "-width:0cm; border-" + side + "-style:solid\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
    }

    public void testHeightAnalyser() {
        final List properties =
            Collections.singletonList(StylePropertyDetails.HEIGHT);

        // initial value is auto
        Element element = getRootElement("<div/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit auto
        element = getRootElement("<div style=\"height:auto\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // normal value
        element = getRootElement(
            "<div style=\"height:50%\"/>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // 100% value
        element = getRootElement(
            "<div style=\"height:100%\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
    }

    public void testMarginAnalysers() {
        testMarginOrPadding("margin-bottom");
        testMarginOrPadding("margin-left");
        testMarginOrPadding("margin-right");
        testMarginOrPadding("margin-top");

        // margin cannot be applied to table cells
        final List properties = new LinkedList();
        properties.add(StylePropertyDetails.MARGIN_BOTTOM);
        properties.add(StylePropertyDetails.MARGIN_LEFT);
        properties.add(StylePropertyDetails.MARGIN_RIGHT);
        properties.add(StylePropertyDetails.MARGIN_TOP);
        Element element = getRootElement(
            "<td style=\"margin-bottom:10cm; display:table-cell\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
        element = getRootElement(
            "<td style=\"margin-left:10cm; display:table-cell\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
        element = getRootElement(
            "<td style=\"margin-right:10cm; display:table-cell\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
        element = getRootElement(
            "<td style=\"margin-top:10cm; display:table-cell\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
    }

    public void testPaddingAnalysers() {
        testMarginOrPadding("padding-bottom");
        testMarginOrPadding("padding-left");
        testMarginOrPadding("padding-right");
        testMarginOrPadding("padding-top");
    }

    private void testMarginOrPadding(final String property) {
        final List properties = Collections.singletonList(
            StylePropertyDetails.getDefinitions().getStyleProperty(property));

        // initial value is 0
        Element element = getRootElement("<div/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit 0px
        element = getRootElement("<div style=\"" + property + ":0px\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit 0cm
        element = getRootElement("<div style=\"" + property + ":0cm\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // 10 px value
        element = getRootElement("<div style=\"" + property + ":10px\"/>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // 10cm value
        element = getRootElement("<div style=\"" + property + ":10cm\"/>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // cannot be applied if display style is table-row-group,
        //   table-header-group, table-footer-group, table-row,
        //   table-column-group, table-column
        element = getRootElement(
            "<tbody style=\"" + property + ":10cm; display:table-row-group\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
        element = getRootElement(
            "<thead style=\"" + property + ":10cm; display:table-header-group\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
        element = getRootElement(
            "<tfoot style=\"" + property + ":10cm; display:table-footer-group\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
        element = getRootElement(
            "<tr style=\"" + property + ":10cm; display:table-row\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
        element = getRootElement(
            "<colgroup style=\"" + property + ":10cm; display:table-column-group\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
        element = getRootElement(
            "<col style=\"" + property + ":10cm; display:table-column\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
    }

    public void testTextAlign() {
        final List properties =
            Collections.singletonList(StylePropertyDetails.TEXT_ALIGN);

        // initial value is left, but no text
        Element element = getRootElement("<div/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit left, no text
        element = getRootElement("<div style=\"text-align:left\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // div with text
        element = getRootElement("<div style=\"text-align:left\">text</div>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // parent has the same value
        element = (Element) getRootElement(
            "<div style=\"text-align:left\">" +
                "<div style=\"text-align:left\">text</div>" +
            "</div>").getHead();
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // parent has different value
        element = (Element) getRootElement(
            "<div style=\"text-align:center\">" +
                "<div style=\"text-align:left\">text</div>" +
            "</div>").getHead();
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // parent has different value child is not text but has the same property
        element = (Element) getRootElement(
            "<div style=\"text-align:center\">" +
                "<div style=\"text-align:left\"><div style=\"text-align:left\">text</div></div>" +
            "</div>").getHead();
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        element = (Element) getRootElement(
            "<div style=\"text-align:center\">" +
                "<div style=\"text-align:left\"><span>text</span></div>" +
            "</div>").getHead();
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        element = (Element) getRootElement(
            "<div style=\"text-align:center\">" +
                "<div style=\"text-align:left\">text</div>" +
            "</div>").getHead();
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // cannot be applied to table rows
        element = getRootElement(
            "<tr style=\"text-align:left; display:table-row\">text</tr>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // parent has different value child is not text but has the same property
        element = (Element) getRootElement(
            "<div style=\"text-align:center\">" +
                "<div style=\"text-align:left\"><div style=\"text-align:right\">text</div></div>" +
            "</div>").getHead();
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

    }

    public void testVerticalAlign() {
        final List properties =
            Collections.singletonList(StylePropertyDetails.VERTICAL_ALIGN);

        // initial value is baseline
        Element element = getRootElement("<div/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // initial value with display value 'inline'
        element = getRootElement("<div style=\"display:inline\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit baseline
        element = getRootElement("<div style=\"vertical-align:baseline\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit baseline with inline display style
        element = getRootElement("<div style=\"vertical-align:baseline; display:inline\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // different value
        element = getRootElement("<div style=\"vertical-align:super; display:inline\"/>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // can only be applied to inline, inline-table, run-in and table-cell
        element = getRootElement(
            "<tr style=\"vertical-align:super; display:table-row\">text</tr>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
    }

    public void testWhitespace() {
        final List properties =
            Collections.singletonList(StylePropertyDetails.WHITE_SPACE);

        // initial value is normal, no text
        Element element = getRootElement("<div/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit normal, no text
        element = getRootElement("<div style=\"white-space:normal\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // normal with text
        element = getRootElement("<div style=\"white-space:normal\">text</div>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // non-default value with text
        element = getRootElement("<div style=\"white-space:nowrap\">text</div>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // parent has the same value
        element = (Element) getRootElement(
            "<div style=\"white-space:normal\">" +
                "<div style=\"white-space:normal\">text</div>" +
            "</div>").getHead();
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // parent has different value
        element = (Element) getRootElement(
            "<div style=\"white-space:nowrap\">" +
                "<div style=\"white-space:normal\">text</div>" +
            "</div>").getHead();
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));
    }

    public void testWidth() {
        final List properties =
            Collections.singletonList(StylePropertyDetails.WIDTH);

        // initial value is auto
        Element element = getRootElement("<div/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // explicit auto
        element = getRootElement("<div style=\"width:auto\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // auto with display style other than 'block'
        element = getRootElement("<div style=\"width:auto; display:table\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // normal value
        element = getRootElement(
            "<div style=\"width:50%\"/>");
        assertTrue(getAnalyser().hasVisuallyImportantProperty(properties, element));

        // 100% value
        element = getRootElement(
            "<div style=\"width:100%\"/>");
        assertFalse(getAnalyser().hasVisuallyImportantProperty(properties, element));
    }

}
