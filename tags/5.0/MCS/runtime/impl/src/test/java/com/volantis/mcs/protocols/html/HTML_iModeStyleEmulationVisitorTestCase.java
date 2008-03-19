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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.protocols.trans.StyleEmulationVisitor;
import com.volantis.mcs.protocols.trans.StyleEmulationVisitorTestAbstract;
import org.xml.sax.XMLReader;

/**
 * Test the HTML_iModeStyleEmulationVisitor.
 */
public class HTML_iModeStyleEmulationVisitorTestCase
        extends StyleEmulationVisitorTestAbstract {

    // Javadoc inherited.
    protected ProtocolConfigurationImpl getProtocolConfiguration() {
        return new HTML_iModeConfiguration();
    }

    protected String getParagraphElement() {
        return "p";
    }
    
    // javadoc inherited
    protected String getExpectedAtomicity() {
        String expected =
            "<b>" +
                "Block text before" +
                "<a href=\"...\">" +
                    "Bold link text 1" +
                    "<ANTI-B>" +
                        "Normal link Text" +
                    "</ANTI-B>" +
                    "Bold link text 2" +
                "</a>" +
                "Bold text after" +
            "</b>";
        return expected;
    }

    // javadoc inherited
    protected String getExpectedAtomicityNested() {
        String expected =
            "<b>" +
                "<i>" +
                    "Block text before" +
                    "<a href=\"...\">" +
                        "Bold link text 1" +
                        "<ANTI-B>" +
                            "Normal link Text" +
                        "</ANTI-B>" +
                        "Bold link text 2" +
                    "</a>" +
                    "Bold text after" +
                "</i>" +
            "</b>";
        return expected;
    }

    // javadoc inherited
    protected String getExpectedPushMatchingParentElementDown() {
        return
            "<body>" +
                "<font>textA</font>" +
                "<a>" +
                    "<font>" +
                        "txt-1" +
                        "<ANTI-B>" +
                            "txt-3" +
                        "</ANTI-B>" +
                        "txt-2" +
                    "</font>" +
                "</a>" +
                "<font>textE</font>" +
            "</body>";
    }

    // javadoc inherited
    protected String getExpectedPushMatchingParentElementDownSimple() {
        return
            "<font>" +
                "textA" +
             "</font>" +
             "<a>" +
                "<font>" +
                    "txt-1" +
                    "<ANTI-B>" +
                        "txt-3" +
                    "</ANTI-B>" +
                    "txt-2" +
                "</font>" +
            "</a>" +
            "<font>" +
                "textE" +
            "</font>";
    }

    // javadoc inherited.
    protected String getExpectedDivisibleStyleElements() {
        return  "<b>" +
                    "<sup>" +
                        "<ANTI-B>" +
                            "[not bold]" +
                        "</ANTI-B>" +
                        "[bold]" +
                    "</sup>" +
                    "<p>" +
                        "[bold-paragraph]" +
                    "</p>" +
                "</b>";
    }

    // javadoc inherited.
    protected String getExpectedDivisibleStyleElementsNull() {
        return  "<b>" +
                    "<ANTI-B>" +
                        "[not bold]" +
                    "</ANTI-B>" +
                    "[should-be-bold][bold]" +
                "</b>";
    }

    /**
     * Transform a ordered list by pushing a style element down into it.
     * <p>
     * Similar to testing a table.
     *
     * @throws Exception
     */
    public void testTransformList() throws Exception {
        String input =
            "<b>" +
                "Text Before" +
                    "<ol>" +
                        "<li>" +
                            "List Item 1" +
                        "</li>" +
                        "<li>" +
                            "List Item 2" +
                        "</li>" +
                    "</ol>" +
                "Text After" +
            "</b>";

        String expected = input;

        doTest(input, expected);
    }

    /**
     * Test that a document containing redundant markup (i.e. markup that is
     * not supported by the device and therefore will have no visual impact)
     * is left unchanged by the style emulation transformation.
     */
    public void testTransformWithRedundantMarkup() throws Exception {
        String input =
          "<body>" +
            "<i>" +
              "Some " +
              "<span>" +
                "Unbold " +
              "</span>" +
              "text" +
            "</i>" +
            "normal" +
          "</body>";

        String expected = input;
        doTest(input, expected);
    }

    /**
     * Test a simple transformation with redundant markup which should be removed.
     */

    /**
     * Test that if a document contains both:
     * <ul>
     * <li>redundant markup (i.e. markup that is not supported by the device
     * and therefore will have no visual impact)</li>
     * AND
     * <li>markup that is not supported by the protocol, but can be used to
     * emulate CSS if this is specifically allowed in the device repository</li>
     * </ul>
     * that both are left unchanged by the style emulation transformer.
     * <p/>
     * (If the device had explicitly specified that an emulation element
     * (e.g. u) was supported by the device, then the correct containment rules
     * would be applied to that element. That is not covered in this test.)
     */
    public void testTransformWithRedundantMarkupAntiBold() throws Exception {
        String input =
            "<body>" +
            "<span>" +
              "Some " +
              "<i>" +
                "Italic " +
              "</i>" +
              "text " +
            "</span>" +
            "<u>" +
                "underline " +
                "<span>" +
                  "not" +
                "</span>" +
              "</u>" +
            "</body>";

        String expected = input;
        doTest(input, expected);
    }

    /**
     * More complex
     */
    public void testTransformWithRedundantMarkupAntiBoldAndAntiItalics()
            throws Exception {
        String input =
            "<body>" +
              "<i>" +
                "<span>" +
                  "Some " +
                  "<i>" +
                    "Italic " +
                  "</i>" +
                  "<span>" +
                    "not italics " +
                  "</span>" +
                "</span>" +
              "</i>" +
              "more not italic" +
            "</body>";

        String expected = input;
        doTest(input, expected);
    }

    /**
     * Test a simple transformation with anti-bold elements.
     */
    public void testTransformSimpleInversion() throws Exception {
        String input =
            "<b>" +
              "This is some" +
              "<span>" +
                "text" +
              "</span>" +
              "which is mainly, but not all, bold." +
            "</b>";

        String expected = input;
        doTest(input, expected);
    }

    /**
     * Test a simple transformation with anti-bold elements.
     */
    public void testTransformSimpleNestedInversion() throws Exception {
        String input =
            "<b>" +
              "bold-1" +
              "<span>" +
                "<p>normal-1</p>" +
              "</span>" +
              "bold-2" +
            "</b>";

        String expected = input;
        doTest(input, expected);
    }

    /**
     * Test a nested inversion transformation with anti-bold and anti-italic elements.
     */
    public void testTransformNestedInversion() throws Exception {
        String input =
            "<b>" +
              "This is some" +
              "<i>" +
                "nested" +
                "<span>" +
                  "text" +
                "</span>" +
                "with and" +
                "<span>" +
                  "without" +
                "</span>" +
                "italics" +
              "</i>" +
              "which is mainly, but not all, bold." +
           "</b>";

        String expected = input;
        doTest(input, expected);
    }

    /**
     * Test transformation using containment inversion.
     */
    public void testTransformContainmentInversion() throws Exception {
        String input =
             "<b>" +
               "<table>" +
                 "<tr>" +
                   "<td>" +
                     "Bold" +
                     "<span>" +
                        "Normal" +
                     "</span>" +
                     "Bold" +
                   "</td>" +
                   "<td>" +
                     "All Bold" +
                   "</td>" +
                 "</tr>" +
              "</table>" +
             "</b>";

        String expected = input;
        doTest(input, expected);
    }
    /**
     * Test transformation using simple containment.
     */
    public void testTransformSimpleContainment() throws Exception {
        String input =
             "<b>" +
               "<table>" +
                 "<tr>" +
                   "<td>" +
                     "Bold" +
                   "</td>" +
                   "<td>" +
                     "All Bold" +
                   "</td>" +
                 "</tr>" +
              "</table>" +
             "</b>";

        String expected = input;
        doTest(input, expected);
    }


    /**
     * Multi element push down
     * @throws Exception
     */
    public void testTransformMultiElementContainment() throws Exception {
        String input =
           "<body>" +
             "<b>" +
               "textA" +
               "<table>" +
                 "<tr>" +
                   "<td>" +
                     "Bold" +
                   "</td>" +
                   "<td>" +
                     "All Bold" +
                   "</td>" +
                 "</tr>" +
              "</table>" +
              "textB" +
            "</b>" +
           "</body>";

        String expected = input;
        doTest(input, expected);
    }

    /**
     * Test transformation containment inversion.
     */
    public void testTransformNestedRedundantElements() throws Exception {
        String input =
            "<p>" +
              "<b>" +
                "<b>" +
                  "<span>" +
                    "<span>" +
                      "Alpha" +
                    "</span>" +
                  "</span>" +
                "</b>" +
              "</b>" +
            "</p>";

        String expected = input;
        doTest(input, expected);
    }

    // javadoc inherited.
    public void testTransformAtomicityKeepTogether() throws Exception {
        String input =
            "<b>" +
              "Block text before" +
              "<KEEP-TOGETHER>" +
                "Bold link text" +
                "<span>" +
                  "Normal link Text" +
                "</span>" +
                "Bold link text" +
              "</KEEP-TOGETHER>" +
              "Bold text after" +
            "</b>";

        String expected =
            "<b>" +
                "Block text before" +
                "<KEEP-TOGETHER>" +
                    "Bold link text" +
                    "<span>" +
                        "Normal link Text" +
                    "</span>" +
                    "Bold link text" +
                "</KEEP-TOGETHER>" +
                "Bold text after" +
            "</b>";
        doTest(input, expected);
    }


    // javadoc inherited
    public void testPushMatchingParentElementDown() throws Exception {
        // HTMLiMode doesn't support b and i tags so replace them with
        // font and dir tags respectively.
        StyleEmulationVisitor visitor = createStyleVisitor();
        String input =
                "<font>" +
                    "<body>" +
                        "textA" +
                        "<a>" +
                            "txt-1" +
                            "<ANTI-B>txt-3</ANTI-B>" +
                            "txt-2" +
                        "</a>" +
                        "textE" +
                    "</body>" +
                "</font>";

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);

        Element italics = (Element) dom.getRootElement().getHead();
        Element atomicElement = (Element)italics.getHead().getNext();
        pushCounterpartElementDown(visitor, atomicElement, "font");

        String actual = DOMUtilities.toString(dom, encoder);

        String expected = getExpectedPushMatchingParentElementDown();
        verifyDOMMatches(null, expected, actual);
    }

    // javadoc inherited
    public void testPushMatchingParentElementDownSimple() throws Exception {
        // HTMLiMode doesn't support the b tag so replace it with
        // the font tag.
        StyleEmulationVisitor visitor = createStyleVisitor();
        String input =
                "<font>" +
                    "textA" +
                    "<a>" +
                        "txt-1" +
                        "<ANTI-B>txt-3</ANTI-B>" +
                        "txt-2" +
                    "</a>" +
                    "textE" +
                "</font>";

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);

        Element parent = dom.getRootElement();
        Element atomicElement = (Element)parent.getHead().getNext();
        pushCounterpartElementDown(visitor, atomicElement, "font");

        String actual = DOMUtilities.toString(dom, encoder);

        String expected = getExpectedPushMatchingParentElementDownSimple();

        verifyDOMMatches(null, expected, actual);
    }

    /**
     * Test the pushing down of a stylistic element to all children.
     */
    public void testPushElementDownToAllChildren() throws Exception {
        String input;
        String expected;
        input = "<p></p>";
        expected = "<p/>";
        doTestPushElementDownToAllChildren(input, expected, "font");

        input = "<p>text</p>";
        expected = "<p><font>text</font></p>";
        doTestPushElementDownToAllChildren(input, expected, "font");

        input = "<p>text<p>text2</p>text3</p>";
        expected =
                "<p>" +
                    "<font>text</font>" +
                    "<p>" +
                        "<font>text2</font>" +
                    "</p>" +
                    "<font>text3</font>" +
                "</p>";
        doTestPushElementDownToAllChildren(input, expected, "font");

        input =
            "<body>" +
                "<ol>" +
                    "<li>" +
                        "Bold" +
                        "<ANTI-B>" +
                            "Normal" +
                        "</ANTI-B>" +
                        "Bold" +
                    "</li>" +
                "</ol>" +
            "</body>";
        expected =
                "<body>" +
                    "<ol>" +
                        "<li>" +
                            "<font>" +
                                "Bold" +
                            "</font>" +
                            "<ANTI-B>" +
                                "Normal" +
                            "</ANTI-B>" +
                            "<font>" +
                                "Bold" +
                            "</font>" +
                        "</li>" +
                    "</ol>" +
                "</body>";
        doTestPushElementDownToAllChildren(input, expected, "font");
    }

    // javadoc inherited
    protected String getNestedFont() {
        return
                "<p>" +
                    "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" " +
                            "width=\"100%\">" +
                        "<tr>" +
                            "<td align=\"center\" bgcolor=\"#c6d3de\">" +
                                "<b>" +
                                    "<a href=\"/wps/portal/!ut/p/.scr/Login\">" +
                                        "<font color=\"#3366cc\" size=\"3\">" +
                                            "<img alt=\"[Log in]\" border=\"0\" height=\"20\" src=\"/wps/themes/xdime/images/nav_login.gif\" width=\"20\"/>" +
                                        "</font>" +
                                    "</a>" +
                                    "<a href=\"/wps/portal/!ut/p/.scr/ForgotPassword\">" +
                                        "<font color=\"#000000\" size=\"3\">" +
                                            "<img alt=\"[I forgot my password]\" border=\"0\" height=\"20\" src=\"/wps/themes/xdime/images/nav_forgot_password.gif\" width=\"23\"/>" +
                                        "</font>" +
                                    "</a>" +
                                "</b>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</p>";
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10333/5	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 16-Nov-05	10333/3	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 09-Aug-05	9211/2	pabbott	VBM:2005080902 End to End CSS emulation test

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 31-Dec-04	6433/2	matthew	VBM:2004120805 fix null pointer exception in pushCounterpartElementDown

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Oct-04	5877/5	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 27-Oct-04	5877/3	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 26-Oct-04	5877/1	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 ===========================================================================
*/
