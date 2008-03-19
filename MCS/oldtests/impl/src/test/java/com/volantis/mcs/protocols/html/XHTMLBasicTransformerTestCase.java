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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLBasicTransformerTestCase.java,v 1.9 2003/04/16 10:23:22 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 01-Nov-02    Byron           VBM:2002103106 - Created in order to to verify
 *                              transformation for nested div tags.
 * 05-Nov-02    Byron           VBM:2002103106 - Added more unit tests:
 *                              testGetChildrenCount() and another for
 *                              transformation
 * 08-Nov-02    Byron           VBM:2002110508 - Updated test case to check
 *                              for div tags that have form tags as parents
 * 22-Nov-02    Phil W-S        VBM:2002112006 - Fix retention of class and ID
 *                              attributes.
 * 27-Nov-02    Byron           VBM:2002112005 - Added testFindInnerElement(),
 *                              NullRemoverTreeVisitor class and updated test
 *                              cases. Renamed some methods to match renamed
 *                              methods in referred class (eg. testCount-
 *                              Children).
 * 05-Dec-02    Byron           VBM:2002112210 - Updated test cases with
 *                              appropriate changes to test this bug fix.
 * 06-Dec-02    Byron           VBM:2002112002 - Updated test cases with
 *                              appropriate changes to test this bug fix.
 *                              Modified testTransform with new mechanism of
 *                              run different permutations using Values class.
 * 17-Apr-03    Geoff           VBM:2003041505 - Commented out System.out
 *                              calls which clutter the JUnit console output.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.values.PropertyValues;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Test the transformation of the DOM tree for XHTMLBasic protocol
 */
public class XHTMLBasicTransformerTestCase extends AbstractDivRemoverTestAbstract {

    // Javadoc inherited.
    public AbstractDivRemover getDivRemover() {
        return (AbstractDivRemover) new XHTMLBasicTransformer().
                getDOMVisitor(createProtocol());
    }

    /**
     * Test the transformation of the DOM tree for XHTMLBasic
     */
    public void testTransform() throws Exception {
        String data[][] = { {
            //------------------------------------------------------
            // Test the ut_nested.jsp page within this testcase.
            //------------------------------------------------------
            // Original 1
            //------------------------------------------------------
            "<html>" +
            "<head></head>" +
            "<body>" +
              "<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\">" +
                "<tr>" +
                  "<td>" +
                    "<table bgcolor=\"aqua\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                      "<tr>" +
                        "<td>Header</td>" +
                      "</tr>" +
                    "</table>" +
                  "</td>" +
                "</tr>" +
                "<tr>" +
                  "<td>" +
                    "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">" +
                      "<tr>" +
                        "<td>Main</td>" +
                      "</tr>" +
                    "</table>" +
                  "</td>" +
                "</tr>" +
              "</table>" +
              "<form>" +
                "<div>" +
                "</div>" +
                "</form>" +
                "<div>" +
                    "Text" +
                "</div>" +
              "</body>" +
            "</html>"
            ,
            //------------------------------------------------------
            // Expected
            //------------------------------------------------------
            "<html>" +
            "<head>" +
            "</head>" +
            "<body>" +
                "<div style='display: inline'>Header" + "<br/>" +
                "Main" + "<br/></div>" +
                "<form><div/></form>" +
                "<div>Text</div>" +
            "</body>" +
            "</html>"
            } , {
            "<html>" +
            "<head>" +
            "<title/>" +
            "</head>" +
            "<body>" +
              "<div foo=\"VF-0\">" +
                "<div>This is one div</div>" +
                "<div>This is the second div</div>" +
              "</div>" +
            "</body>" +
            "</html>"
            ,
            //------------------------------------------------------
            // Expected
            //------------------------------------------------------
            "<html>" +
            "<head>" +
            "<title/>" +
            "</head>" +
            "<body>" +
              "<div foo=\"VF-0\">" +
                "This is one div" +
                "<div>This is the second div</div>" +
              "</div>" +
            "</body>" +
            "</html>",
            } , {
            "<html>" +
              "<head>" +
              "</head>" +
                "<div>" +
                  "<div>" +
                    "<div>" +
                      "<div>" +
                        "A" +
                      "</div>" +
                      "<div>" +
                        "B" +
                      "</div>" +
                    "</div>" +
                    "<div>C</div>" +
                    "<div>D</div>" +
                  "</div>" +
                "</div>" +
                "<div>" +
                    "X<div>Y</div>Z" +
                "</div>" +
                "<div foo=\"kl\">" +
                  "Six" +
                "</div>" +
                "<div>" +
                  "<table>" +
                    "<tr>" +
                      "<td foo=\"mn\">Seven</td>" +
                      "<td foo=\"op\">Eight</td>" +
                    "</tr>" +
                  "</table>" +
                "</div>" +
            "</html>"
            ,
            //------------------------------------------------------
            // Expected
            //------------------------------------------------------
            "<html>" +
              "<head>" +
              "</head>" +
              "A" +
              "<div>" +
                "B" +
              "</div>" +
              "C" +
              "<div>" +
                "D" +
              "</div>" +
              "X" +
              "<div>" +
                "Y" +
              "</div>" +
              "Z" +
              "<div foo=\"kl\">" +
                "Six" +
              "</div>" +
              "<table>" +
                "<tr>" +
                  "<td foo=\"mn\">Seven</td>" +
                  "<td foo=\"op\">Eight</td>" +
                "</tr>" +
              "</table>" +
            "</html>"
        } };
        class Values {
            String original;
            String expected;
            String msg;
            Values(String original, String expected, String msg) {
                this.original = original;
                this.expected = expected;
                this.msg = msg;
            }
        }
        ArrayList tests = new ArrayList();
        for (int i = 0; i < data.length; i++) {
            tests.add(new Values(data[i][0],
                                 data[i][1],
                                 "DOM comparison failed - Test " + (i+1)));
        }
        Iterator iterator = tests.iterator();
        while (iterator.hasNext()) {
            Values values = (Values) iterator.next();
            DOMProtocol protocol = createProtocol();


            Document dom = getStrictStyledDOMHelper().parse(values.original);
            Document expected = getStrictStyledDOMHelper().parse(values.expected);
            String domAsString;
            String expectedAsString;
            DOMTransformer transformer = new XHTMLBasicTransformer();

            // Useful debug output

//        System.out.println("Original DOM:");
//        System.out.println(DOMUtilities.toString(dom, protocol.getCharacterEncoder()));

            dom = transformer.transform(protocol, dom);

            // Useful debug output

//        System.out.println("Transformed DOM:");
//        System.out.println(DOMUtilities.toString(dom, protocol.getCharacterEncoder()));

            domAsString = getStrictStyledDOMHelper().render(dom);
            expectedAsString = getStrictStyledDOMHelper().render(expected);

            assertEquals(values.msg, expectedAsString, domAsString);
        }
    }

    private DOMProtocol createProtocol() {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        ProtocolBuilder builder = new ProtocolBuilder();
        return (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestXHTMLBasicFactory(),
                internalDevice);
    }



    /**
     * Verify that when tables are flattened into divs, the styles of the
     * emulated elements are correctly captured on the emulating elements.
     */
    public void testFlattenTableCellWithStylesAndNamedUnstyledParent()
            throws IOException, SAXException, ParserConfigurationException {

        Styles expectedStyles = generateTestColorStyles();

        Element newParent = doFlattenTableCell(expectedStyles,
                false, true, false, true);

        assertNotNull(newParent.getStyles());
        Styles actualStyles = newParent.getStyles();
        checkFlattenedValues(expectedStyles, actualStyles, true);

        newParent = doFlattenTableCell(expectedStyles,
                true, true, false, true);
        assertNotNull(newParent.getStyles());
        actualStyles = newParent.getStyles();
        checkFlattenedValues(expectedStyles, actualStyles, true);
        Element tail = (Element)newParent.getTail();
        assertNotNull(tail);
        assertEquals("br", tail.getName());


        newParent = doFlattenTableCell(expectedStyles,
                true, false, false, true);

        Element element = ((Element) newParent.getHead());
        assertNotNull(element.getStyles());
        checkFlattenedValues(expectedStyles, actualStyles, false);
        tail = (Element) newParent.getTail();
        assertNotNull(tail);
        assertEquals("br", tail.getName());

        newParent = doFlattenTableCell(expectedStyles,
                false, false, false, true);

        element = ((Element) newParent.getHead());
        assertNotNull(element.getStyles());
    }

    /**
     * Verify that when tables are flattened into divs and the newParent
     * already has styles, the styles of the emulated elements are correctly
     * captured on the emulating elements.
     */
    public void testFlattenTableCellWithStylesAndNamedStyledParent() {

        Styles expectedStyles = generateTestColorStyles();

        // the table cell should be removed because doInlineDiv was true
        Element newParent = doFlattenTableCell(expectedStyles,
                false, true, true, true);
        assertNotNull(newParent.getStyles());
        Styles actualStyles = newParent.getStyles();
        checkFlattenedValues(expectedStyles, actualStyles, true);

        // the table cell should be removed because doInlineDiv was true
        newParent = doFlattenTableCell(expectedStyles, true, true,
                true, true);
        assertNotNull(newParent.getStyles());
        actualStyles = newParent.getStyles();
        checkFlattenedValues(expectedStyles, actualStyles, true);
        Element tail = (Element) newParent.getTail();
        assertNotNull(tail);
        assertEquals("br", tail.getName());

        // the table cell should be surrounded by a div because doInlineDiv was false
        newParent = doFlattenTableCell(expectedStyles, true, false,
                true, true);

        Element element = ((Element) newParent.getHead());
        assertNotNull(element.getStyles());
        actualStyles = newParent.getStyles();
        checkFlattenedValues(expectedStyles, actualStyles, false);
        tail = (Element) newParent.getTail();
        assertNotNull(tail);
        assertEquals("br", tail.getName());

        // the table cell should be surrounded by a div because doInlineDiv was false
        newParent = doFlattenTableCell(expectedStyles, false, false, true, true);

        element = ((Element) newParent.getHead());
        assertNotNull(element.getStyles());
        actualStyles = newParent.getStyles();
        checkFlattenedValues(expectedStyles, actualStyles, false);
    }

    /**
     * Verify that when tables are flattened into divs, the styles of the
     * emulated elements are correctly captured on the emulating elements.
     */
    public void testFlattenTableCellWithStylesAndUnnamedUnstyledParent()
            throws IOException, SAXException, ParserConfigurationException {

        Styles expectedStyles = generateTestColorStyles();

        Element newParent = doFlattenTableCell(expectedStyles,
                false, true, false, false);

        assertNull(newParent.getStyles());
        Element child = (Element)newParent.getHead();
        assertNotNull(child);
        assertNotNull(child.getStyles());
        Styles actualStyles = child.getStyles();
        checkFlattenedValues(expectedStyles, actualStyles, true);

        newParent = doFlattenTableCell(expectedStyles,
                true, true, false, false);
        assertNull(newParent.getStyles());
        child = (Element) newParent.getHead();
        assertNotNull(child);
        assertNotNull(child.getStyles());
        actualStyles = child.getStyles();
        checkFlattenedValues(expectedStyles, actualStyles, true);
        Element tail = (Element) newParent.getTail();
        assertNotNull(tail);
        assertEquals("br", tail.getName());

        newParent = doFlattenTableCell(expectedStyles,
                true, false, false, false);

        Element element = ((Element) newParent.getHead());
        assertNotNull(element.getStyles());
        checkFlattenedValues(expectedStyles, actualStyles, false);
        tail = (Element) newParent.getTail();
        assertNotNull(tail);
        assertEquals("br", tail.getName());

        newParent = doFlattenTableCell(expectedStyles,
                false, false, false, false);

        element = ((Element) newParent.getHead());
        assertNotNull(element.getStyles());
        checkFlattenedValues(expectedStyles, actualStyles, false);
    }

    /**
     * Utility method which generates a Styles with the text
     * and background colour property values set to white and red respectively.
     *
     * @return a MutablePropertyValues with the text and background colour
     * values set to white and red respectively.
     */
    private Styles generateTestColorStyles() {
        return StylesBuilder.getCompleteStyles("background-color: red; color: white",
                true);
    }

    /**
     * Utility method to test the flattenTableCell method.
     *
     * @param styles            the styles to set on the table column. May be
     *                          null.
     * @param forceLineBreak    true if the line requires a break,
     *                          false otherwise
     * @param doInlineDiv       true if we should add the display inline
     *                          property value to the output Styles, false
     *                          otherwise
     * @param namedParent
     * @return the flattened table cell
     */
    private Element doFlattenTableCell(Styles styles, boolean forceLineBreak,
            boolean doInlineDiv, boolean styledParent, boolean namedParent) {

        DOMFactory domFactory = DOMFactory.getDefaultInstance();
        Element column = domFactory.createElement();
        Element newParent = domFactory.createElement();
        if (namedParent) {
            newParent.setName("div");
        }

        if (styles != null) {
            column.setStyles(styles);
            if (styledParent) {
                newParent.setStyles(styles);
            }
        }

        Element child = domFactory.createElement();
        child.setName("p");
        column.addHead(child);

        MyXHTMLBasicTransformer transformer = new MyXHTMLBasicTransformer();
        DOMProtocol protocol = createProtocol();
        transformer.initialize(protocol);

        transformer.proxyTransformTableCell(column, newParent, forceLineBreak,
                doInlineDiv);

        return newParent;
    }

    /**
     * Tests that the colour styles of both property values match.
     * @param expectedStyles  the expected style property values
     * @param actualStyles    the actual style property values
     * @param doInlineDiv if true, the DISPLAY property should be set, if false
     * it shouldn't be.
     */
    private void checkFlattenedValues(Styles expectedStyles,
            Styles actualStyles, boolean doInlineDiv) {

        PropertyValues expected = expectedStyles.getPropertyValues();
        PropertyValues actual = actualStyles.getPropertyValues();

        assertNotNull(actual);
        assertEquals(
                expected.getComputedValue(
                        StylePropertyDetails.BACKGROUND_COLOR),
                actual.getComputedValue(
                        StylePropertyDetails.BACKGROUND_COLOR));
        assertEquals(
                expected.getComputedValue(StylePropertyDetails.COLOR),
                actual.getComputedValue(StylePropertyDetails.COLOR));

        if (doInlineDiv) {
            StyleValue value = DisplayKeywords.INLINE;
            assertEquals(value,
                    actual.getComputedValue(StylePropertyDetails.DISPLAY));
        }
    }

    /**
     * Verify that the flattenTable method removes the table tag and does not
     * generate a replacement div tag when there is no need.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testFlattenTableWithoutStyles() throws Exception {

        String input =  "<table>" +
                            "<tr><td><p>cell1</p></td></tr>" +
                            "<tr>" +
                                "<td>" +
                                    "<table>" +
                                        "<tr><td>cell2></td></tr>" +
                                    "</table>" +
                                "</td>" +
                            "</tr>" +
                        "</table>";
        String expected =   "<p>cell1</p>" +
                            "<br/>" +
                            "<table>" +
                                "<tr><td>cell2></td></tr>" +
                            "</table>";

        Document dom = getStrictStyledDOMHelper().parse(input);
        Element table = dom.getRootElement();

        Element flattenedTable = doFlattenTable(table, expected);
        assertNull(flattenedTable.getName());
    }

    /**
     * Verify that the flattenTable method copies any style information on the
     * original table element to the replacement div tag.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testFlattenTableWithStyles() throws Exception {

        String input =  "<table>" +
                            "<tr><td><p>cell1</p></td></tr>" +
                            "<tr><td>" +
                                "<table><tr><td>cell2></td></tr></table>" +
                            "</td></tr>" +
                        "</table>";

        String expected2 =  "<div style='background-color: red; color: white; display: inline'>" +
                                "<p>cell1</p>" +
                                "<br/>" +
                                "<table><tr><td>cell2></td></tr></table>" +
                            "</div>";

        Document dom = getStrictStyledDOMHelper().parse(input);
        Element table = dom.getRootElement();

        Styles tableStyles = generateTestColorStyles();
        table.setStyles(tableStyles);
        table.setAttribute("styleClass", "testClass");

        Element flattenedTable = doFlattenTable(table, expected2);
        assertTrue("div".equals(flattenedTable.getName()));
    }

    /**
     * Utility method to test the flattenTable method.
     *
     * @param input     the table element to flatten
     * @param expected  the expected markup after flattening the table
     * @return Element  the flattened table
     * @throws Exception if there was a problem running the test
     */
    private Element doFlattenTable(Element input, String expected)
            throws Exception {
        Styles originalStyles = input.getStyles();
        MyXHTMLBasicTransformer transformer = new MyXHTMLBasicTransformer();
        DOMProtocol protocol = createProtocol();
        transformer.initialize(protocol);

        Element flattenedTable = transformer.proxyFlattenTable(input, 2, 1);
        assertNotNull("The flattened table should not be null",
                flattenedTable);

        if (originalStyles != null) {
            assertNotNull("The flattened table styles should not be null",
                    flattenedTable.getStyles());
        } else {
            assertNull("The flattened table styles should be null",
                    flattenedTable.getStyles());
        }

        // The styles are not quite the same.

//        assertEquals("The styles on the flattened table should be identical" +
//                " to those on the original table", originalStyles,
//                flattenedTable.getStyles());
        assertEquals(expected, getStrictStyledDOMHelper().render(flattenedTable));

        return flattenedTable;
    }

    public void testDivRemovalWithStyles()  throws Exception {

        // Characterization Test of how it works currently. No judgement is
        // made as to whether the current behaviour is correct, and it is
        // expected that this test case will require modifying over time until
        // the proper behaviour is specified (if possible - the code seems to
        // contain a lot of unexplained heuristics currently).
        //
        // See http://en.wikipedia.org/wiki/Characterization_Test
        String input =
                "<body>" +
                  "<div style=\"border-spacing: 0 0\">" +
                    "<div style=\"border-spacing: 0\">" +
                      "<form action=\"rateplan.jsp\" method=\"post\">" +
                        "<div>" +
                          "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                          "<input name=\"expiredSessionUrl\" type=\"hidden\" value=\"expired.jsp\"/>" +
                        "</div>" +
                        "<div style=\"background-color: #f60; color: #fff; border-spacing: 0 0; font-weight: bold; width: 100%\">" +
                          "<div>rateplan change</div>" +
                        "</div>" +
                        "<div style=\"color: #777; border-spacing: 0 0; width: 100%\">Your msisdn <b style=\"border-spacing: 0\">506984444</b></div>" +
                        "<div style=\"color: #777; border-spacing: 0 0; width: 100%\">" +
                          "<hr style=\"border-spacing: 0\"/>" +
                        "</div>" +
                        "<div style=\"border-spacing: 0 0\"/>" +
                        "<div style=\"color: #777; border-spacing: 0 0; width: 100%\">costs</div>" +
                        "<div style=\"color: #777; border-spacing: 0 0; width: 100%\">" +
                          "<span style=\"border-spacing: 0\">base??</span>" +
                          "<b style=\"border-spacing: 0\">10.0 zl</b>" +
                        "</div>" +
                        "<table style=\"border-spacing: 0 0\">" +
                          "<tr>" +
                            "<td style=\"width: auto\">" +
                               "<input name=\"cancelI\" type=\"submit\" value=\"cancel\" style=\"border-spacing: 0\"/>" +
                            "</td>" +
                          "</tr>" +
                        "</table>" +
                      "</form>" +
                    "</div>" +
                  "</div>" +
                "</body>";

        String expected =
                "<body>" +
                  "<div>" +
                    "<form action=\"rateplan.jsp\" method=\"post\" style='display: inline'>" +
                      "<div>" +
                        "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                        "<input name=\"expiredSessionUrl\" type=\"hidden\" value=\"expired.jsp\"/>" +
                      "</div>" +
                      "<div style='background-color: #f60; color: #fff; font-weight: bold; width: 100%'>rateplan change</div>" +
                      "<div style='color: #777; width: 100%'>Your msisdn <b>506984444</b></div>" +
                      "<div style='color: #777; width: 100%'><hr/></div>" +
                      "<div/>" +
                      "<div style='color: #777; width: 100%'>costs</div>" +
                      "<div style='color: #777; width: 100%'>" +
                        "<span>base??</span><b>10.0 zl</b>" +
                       "</div>" +
                      "<input name=\"cancelI\" type=\"submit\" value=\"cancel\"/>" +
                      "<br/>" +
                    "</form>" +
                  "</div>" +
                "</body>";

        doVisitorTest(input, expected);
    }

    public void testDivRemovalNoStyles()  throws Exception {
        // Characterization Test of how it works currently. No judgement is
        // made as to whether the current behaviour is correct, and it is
        // expected that this test case will require modifying over time until
        // the proper behaviour is specified (if possible - the code seems to
        // contain a lot of unexplained heuristics currently).
        //
        // See http://en.wikipedia.org/wiki/Characterization_Test
        String input =
                "<body>" +
                  "<form action=\"rateplan.jsp\" method=\"post\">" +
                    "<div>" +
                      "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                      "<input name=\"expiredSessionUrl\" type=\"hidden\" value=\"expired.jsp\"/>" +
                    "</div>" +
                    "<div>" +
                      "<div>rateplan change</div>" +
                    "</div>" +
                    "<div>Your msisdn <b>506984444</b></div>" +
                    "<div>" +
                      "<hr/>" +
                    "</div>" +
                    "<div/>" +
                    "<div>costs</div>" +
                    "<div>" +
                      "<span>base??</span><b>10.0 zl</b>" +
                    "</div>" +
                    "<table>" +
                      "<tr>" +
                        "<td>" +
                           "<input name=\"cancelI\" type=\"submit\" value=\"cancel\"/>" +
                        "</td>" +
                      "</tr>" +
                    "</table>" +
                  "</form>" +
                "</body>";

        String expected =
                "<body>" +
                  "<form action=\"rateplan.jsp\" method=\"post\" style='display: inline'>" +
                    "<div>" +
                      "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                      "<input name=\"expiredSessionUrl\" type=\"hidden\" value=\"expired.jsp\"/>" +
                    "</div>" +
                    "<div>rateplan change</div>" +
                    "<div>Your msisdn <b>506984444</b></div>" +
                    "<div><hr/></div>" +
                    "<div/>" +
                    "<div>costs</div>" +
                    "<div>" +
                      "<span>base??</span><b>10.0 zl</b>" +
                     "</div>" +
                    "<input name=\"cancelI\" type=\"submit\" value=\"cancel\"/>" +
                    "<br/>" +
                  "</form>" +
                "</body>";

        doVisitorTest(input, expected);
    }

    public void testDivsNotEligibleForRemovalDueToColor()  throws Exception {
        String input =
                "<body>" +
                  "<form action=\"rateplan.jsp\" method=\"post\">" +
                    "<div>" +
                      "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                      "<input name=\"expiredSessionUrl\" type=\"hidden\" value=\"expired.jsp\"/>" +
                      // We want this div - div21
                      "<div style=\"background-color:#f60;\">" +
                        "<div>rateplan change</div>" +
                      "</div>" +
                      // We want this div as well - div22
                      "<div style=\"color: #777;\">Your msisdn <b>506984444</b></div>" +
                    "</div>" +
                  "</form>" +
                "</body>";

        Document dom = getStrictStyledDOMHelper().parse(input);

        Element body = dom.getRootElement();
        Element form = getChild(body);

        // Get the first-level div, first index. There is only one.
        Element div11 = getChild(form);

        // Get the second-level div, first index. This is the one that contains
        // the other div with the "rateplan change" Text element.
        Element div21 = (Element) getChild(div11).getNext().getNext();

        // Get the second-level div, second index. This is the one that
        // contains the 'Your msisdn' Text element with the bold Element
        // containing the Text '506984444'
        Element div22 = (Element) div21.getNext();

        MyXHTMLBasicTransformer transformer =  new MyXHTMLBasicTransformer();
        transformer.initialize(createProtocol());

        assertFalse("background-color is important visual style",
                transformer.treeVisitor.isDivEligibleForRemoval(div21));
        assertFalse("Can't remove this <div> due to important visual style " +
                " background-color:#f60",
                transformer.treeVisitor.canRemoveDivElement(div21, false));

        assertFalse("color is important visual style",
                transformer.treeVisitor.isDivEligibleForRemoval(div22));

        assertFalse("Can't remove this <div> since it gives important block " +
                "structure to the content.",
                transformer.treeVisitor.canRemoveDivElement(div22, false));
    }

    /**
     * Return the child of the specified element. May be null.
     *
     * @param element
     * @return the child, or null if there isn't one.
     */
    private Element getChild(Element element) {
        return (Element) element.getHead();
    }

    public void testDivsAreNotRemovedDueToColor()  throws Exception {

        // This is the same input fragment as per
        // testDivsNotEligibleForRemovalDueToColor. Re-used here to capture the
        // output of the transform.
        String input =
                "<body>" +
                  "<form action=\"rateplan.jsp\" method=\"post\">" +
                    "<div>" +
                      "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                      "<input name=\"expiredSessionUrl\" type=\"hidden\" value=\"expired.jsp\"/>" +
                      "<div style=\"background-color:#f60;\">" +
                        "<div>rateplan change</div>" +
                      "</div>" +
                      "<div style=\"color: #777;\">Your msisdn <b>506984444</b></div>" +
                    "</div>" +
                  "</form>" +
                "</body>";

        String expected =
                "<body>" +
                  "<form action=\"rateplan.jsp\" method=\"post\" style='display: inline'>" +
                    "<div>" +
                      "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                      "<input name=\"expiredSessionUrl\" type=\"hidden\" value=\"expired.jsp\"/>" +
                      "<div style='background-color: #f60'>rateplan change</div>" +
                      "<div style='color: #777'>Your msisdn <b>506984444</b></div>" +
                    "</div>" +
                  "</form>" +
                "</body>";

        doVisitorTest(input, expected);
    }

    public void testDivsAreNotEligibleForRemovalDueToBlockiness()  throws Exception {
        String input =
                "<body>" +
                  "<form action=\"rateplan.jsp\" method=\"post\">" +
                    "<div>" +
                      "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                      "<input name=\"expiredSessionUrl\" type=\"hidden\" value=\"expired.jsp\"/>" +
                      "<div style=\"background-color:#f60;\">" +
                        "<div>rateplan change</div>" +
                      "</div>" +
                      "<div>Your msisdn <b>506984444</b></div>" +
                      "<div>Lots of text here, which is effectively a 'paragraph'</div>"   +
                    "</div>" +
                  "</form>" +
                "</body>";

        Document dom = getStrictStyledDOMHelper().parse(input);

        Element body = dom.getRootElement();
        Element form = getChild(body);
        Element div11 = getChild(form);
        Element div22 = (Element) getChild(div11).getNext().getNext().getNext();
        Element div23 = (Element) div22.getNext();

        MyXHTMLBasicTransformer transformer = new MyXHTMLBasicTransformer();
        transformer.initialize(createProtocol());

        final Element inputControl = ((Element) div22.getPrevious()
                .getPrevious());
        assertEquals("Check we have got the right element from the DOM",
                "input", inputControl.getName());
        assertEquals("Check that it is the second input control",
                "expiredSessionUrl",
                inputControl.getAttributeValue("name"));

        assertTrue("There are no styles preventing this " +
                "Element from being removed.",
                transformer.treeVisitor.isDivEligibleForRemoval(div22));
        assertTrue("There are no styles preventing this " +
                "Element from being removed.",
                transformer.treeVisitor.isDivEligibleForRemoval(div23));

        assertTrue(transformer.treeVisitor.canRemoveDivElement(div22, false));
        div22.setName(null);
        assertFalse(transformer.treeVisitor.canRemoveDivElement(div23, false));
    }

    public void testDivsAreNotRemovedDueToBlockiness()  throws Exception {
        String input =
                "<body>" +
                    "<div>" +
                        "<div>Some <b>bold text</b></div>" +
                        "<div>Some more text</div>" +
                    "</div>" +
                "</body>";
        String expected =
                "<body>" +
                    "<div>Some <b>bold text</b><div>Some more text</div></div>" +
                "</body>";

        doVisitorTest(input, expected);
    }

    /**
     *
     * @param input
     * @param expected
     */
    private void doVisitorTest(String input, String expected) {
        Document dom = getStrictStyledDOMHelper().parse(input);

        MyXHTMLBasicTransformer transformer = new MyXHTMLBasicTransformer();
        transformer.initialize(createProtocol());

        transformer.treeVisitor.visit(dom);

        assertEquals("", expected, getStrictStyledDOMHelper().render(dom));
    }

}

/**
 * Inner class provides access to protected method countChildren by
 * providing another method that accesses the protected method itself
 */
class MyXHTMLBasicTransformer extends XHTMLBasicTransformer {

    protected MyTreeVisitor treeVisitor = null;

    /**
     * Permit outer class to access TreeVisitor's protected methods via
     * proxy calls.
     */
    class MyTreeVisitor extends TreeVisitor {
        public MyTreeVisitor (DOMProtocol protocol) {
            super(protocol);
        }
    }

    public void initialize( DOMProtocol protocol ) {
        if (treeVisitor == null) {
            treeVisitor = new MyTreeVisitor(protocol);
        }
    }

    /**
     * Proxy call to the transformTableCell method in the parent class' tree Visitor
     *
     * @see XHTMLBasicTransformer.TreeVisitor#transformTableCell
     */
    public void proxyTransformTableCell(Element column, Element newParent,
                                        boolean forceLineBreak,
                                        boolean doInlineDiv) {
        treeVisitor.transformTableCell(column, newParent, forceLineBreak,
                doInlineDiv);
    }

    /**
     * Proxy call to the flattenTable method in the parent class' tree visitor
     * @param tableElement  the element representing the table
     * @param totalRows     the total number of rows in this table
     * @param totalColumns  the total number of columns in this table
     * @return Element      which represents the flattened table
     * @see XHTMLBasicTransformer.TreeVisitor#flattenTable
     */
    public Element proxyFlattenTable(Element tableElement, int totalRows,
                                  int totalColumns) {
        return treeVisitor.flattenTable(tableElement, totalRows, totalColumns);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9223/4	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 22-Aug-05	9223/3	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/2	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 22-Jul-05	8859/2	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 13-May-05	8233/1	emma	VBM:2005042706 Merge from 3.3.0 - multiple style classes generated when device didn't support it

 13-May-05	8192/1	emma	VBM:2005042706 Merge from 3.2.3 - multiple style classes generated when device didn't support it

 31-Mar-05	7527/3	geoff	VBM:2005032304 XHTML - No style is applied as style classes are written without a space.

 31-Mar-05	7527/1	geoff	VBM:2005032304 XHTML - No style is applied as style classes are written without a space.

 31-Mar-05	7523/1	geoff	VBM:2005032304 XHTML - No style is applied as style classes are written without a space.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 26-Aug-04	5323/21	pcameron	VBM:2004082015 Fixed XHTMLBasicTransformer's tree visitor inner class's findOnlyChild

 26-Aug-04	5287/8	pcameron	VBM:2004082015 Fixed XHTMLBasicTransformer's tree visitor inner class's findOnlyChild

 26-Aug-04	5287/6	pcameron	VBM:2004082015 Fixed XHTMLBasicTransformer's tree visitor inner class's findOnlyChild

 ===========================================================================
*/
