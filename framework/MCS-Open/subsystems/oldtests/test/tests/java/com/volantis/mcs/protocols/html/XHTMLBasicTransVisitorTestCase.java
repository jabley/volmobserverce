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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLBasicTransVisitorTestCase.java,v 1.1.4.9 2003/04/04 15:20:32 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 16-Oct-02    Phil W-S        VBM:2002100201 - Added test for inverse
 *                              remapping of tables within forms within tables
 * 24-Dec-02    Phil W-S        VBM:2002122402 - Test extended style class
 *                              retention
 * 02-Jan-03    Phil W-S        VBM:2002122401 - Add testing of table sections
 * 07-Jan-03    Phil W-S        VBM:2003010712 - Added testing of the
 *                              artificial table optimization attribute.
 * 09-Jan-03    Phil W-S        VBM:2003010906 - Update to ensure that the test
 *                              protocol indicates that it supports multi-style
 *                              classes.
 * 15-Jan-03    Phil W-S        VBM:2002110402 - Rework: Updated to account for
 *                              single column table removal.
 * 21-Mar-03    Adrian          VBM:2003031701 - Added new test method
 *                              testWhitespaceHandling to integration test
 *                              TransVisitor methods normalize(..) and
 *                              getFirstNonWhitespaceChild(..). Also added new
 *                              TestDOMProtocol inner class which initializes a
 *                              DOMPool on construction as this is required by
 *                              the new test.
 * 04-Apr-03    Adrian          VBM:2003031701 - Made this a subclass of
 *                              TransVisitorTestAbstract and removed some
 *                              redundant code.
 * 03-Jun-03    Byron           VBM:2003042204 - Added doProcessingTest(..) and
 *                              debugOutput() as a result of refactoring.
 *                              Modified tests to call aforementioned methods.
 *                              Added testNormalization().
 * 04-Jun-03    Byron           VBM:2003042204 - Moved doProcessingTest,
 *                              debugOuput, toString methods to superclass.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.mcs.protocols.trans.TransVisitorTestAbstract;
import com.volantis.mcs.devices.InternalDevice;

/**
 * This is the unit test for the XHTMLBasicTransVisitor and TransVisitor
 * classes.
 */
public class XHTMLBasicTransVisitorTestCase extends TransVisitorTestAbstract {

    // javadoc inherited from superclass
    protected DOMProtocol createDOMProtocol(InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestXHTMLBasicFactory(),
                internalDevice);
        return protocol;
    }

    // javadoc inherited from superclass
    protected TransFactory getProtocolSpecificFactory() {
        return new XHTMLBasicTransFactory(new XHTMLBasicConfiguration());
    }

    public TransVisitor getTransVisitor() {
        return null;
    }

    public DOMProtocol getProtocol() {
        return null;
    }

    /**
     * Declared here so subclasses may re-use this string as input.
     */
    final protected String TEST_BASIC_INTERFACE =
        "<table>" +
          "<tr>" +
            "<td>TD 1</td>" +
            "<td>" +
              "<table>" +
                "<tr>" +
                  "<td>TD 2</td>" +
                  "<td>" +
                    "<p>TD 3 P1</p>" +
                    "<table><tr><td>TD 4</td></tr></table>" +
                    "<strong>TD 3 STRONG1</strong>" +
                  "</td>" +
                "</tr>" +
              "</table>" +
            "</td>" +
          "</tr>" +
          "<tr>" +
            "<td>TD 5</td>" +
            "<td>" +
              "<div id=\"fun\" style='background-color: red'>" +
                "<p>TD 6 P1</p>" +
                "<p>TD 6 P2</p>" +
                "<table><tr><td>TD 7</td></tr></table>" +
                "<table><tr><td>TD 8</td></tr></table>" +
                "<p>TD 6 P3</p>" +
                "<table style='text-align: center'><tr><td>TD 9</td></tr></table>" +
                "<p>TD 6 P4</p>" +
                "<p>TD 6 P5</p>" +
              "</div>" +
            "</td>" +
            "<td>" +
              "<form id=\"fun\" style='vertical-align: top'>" +
                "<p>TD 10 P1</p>" +
                "<p>TD 10 P2</p>" +
                "<table><tr><td>TD 11</td></tr></table>" +
                "<table><tr><td>TD 12</td></tr></table>" +
                "<p>TD 10 P3</p>" +
                "<table style='text-align: center'><tr><td>TD 13</td></tr></table>" +
                "<p>TD 10 P4</p>" +
                "<p>TD 10 P5</p>" +
              "</form>" +
            "</td>" +
          "</tr>" +
        "</table>";

    /**
     * This method tests the major functionality:
     * <ol>
     * <li>preprocess</li>
     * <li>process and</li>
     * <li>reset</li>
     * </ol>
     * All other methods within this class are exercised by the example, which
     * contains:
     * <ol>
     * <li>styles that need to be preserved</li>
     * <li>a column with two tables in separate cells</li>
     * <li>a row with a single table in one of the cells</li>
     * <li>simple nested tables</li>
     * <li>a table within a form within a table where the form's table
     *     cannot be preserved</li>
     * <li>a cell that contains nested tables with non-table and
     *     table siblings</li>
     * </ol>
     * There are a few minor areas not covered here, including:
     * <ol>
     * <li>nested tables that should be retained (this protocol doesn't do
     *     this)</li>
     * <li>multiple tables in a row/column where LCM must be calculated</li>
     * <li>cells that already have a row or col span</li>
     * </ol>
     *
     * @todo add tests for the above deficiencies in this one
     */
    public void testBasicInterface() throws Exception {
        String expected =
            "<table>" +
              "<tr>" +
                "<td rowspan=\"3\">TD 1</td>" +
                "<td rowspan=\"3\">TD 2</td>" +
                "<td>" +
                  "<p>TD 3 P1</p>" +
                "</td>" +
              "</tr>" +
              "<tr>" +
                "<td>TD 4</td>" +
              "</tr>" +
              "<tr>" +
                "<td>" +
                  "<strong>TD 3 STRONG1</strong>" +
                "</td>" +
              "</tr>" +
              "<tr>" +
                "<td rowspan=\"6\">TD 5</td>" +
                "<td style='background-color: red' colspan=\"2\">" +
                  "<p>TD 6 P1</p>" +
                  "<p>TD 6 P2</p>" +
                "</td>" +
                "<td rowspan=\"6\">" +
                  "<form style='vertical-align: top' id=\"fun\">" +
                    "<div>" +
                      "<p>TD 10 P1</p>" +
                      "<p>TD 10 P2</p>" +
                    "</div>" +
                    "<div>TD 11</div>" +
                    "<div>TD 12</div>" +
                    "<div>" +
                      "<p>TD 10 P3</p>" +
                    "</div>" +
                    "<div style='text-align: center'>" +
                      "TD 13" +
                    "</div>" +
                    "<div>" +
                      "<p>TD 10 P4</p>" +
                      "<p>TD 10 P5</p>" +
                    "</div>" +
                  "</form>" +
                "</td>" +
              "</tr>" +
              "<tr>" +
                "<td style='background-color: red' colspan=\"2\">TD 7</td>" +
              "</tr>" +
              "<tr>" +
                "<td style='background-color: red' colspan=\"2\">TD 8</td>" +
              "</tr>" +
              "<tr>" +
                "<td style='background-color: red' colspan=\"2\">" +
                  "<p>TD 6 P3</p>" +
                "</td>" +
              "</tr>" +
              "<tr>" +
                "<td style='background-color: red; text-align: center' " +
                "colspan=\"2\">TD 9</td>" +
              "</tr>" +
              "<tr>" +
                "<td style='background-color: red' colspan=\"2\">" +
                  "<p>TD 6 P4</p>" +
                  "<p>TD 6 P5</p>" +
                "</td>" +
              "</tr>" +
            "</table>";

        doProcessingTest(TEST_BASIC_INTERFACE, expected, true);
    }

    /**
     * Declared here so subclasses may re-use this string as input.
     */
    final protected String TEST_INVERSE_REMAP_INPUT =
        "<html>" +
          "<table>" +
            "<tr>" +
              "<td>TD 1</td>" +
            "</tr>" +
            "<tr>" +
              "<td>" +
                "<table>" +
                  "<tr>" +
                    "<td>" +
                      "<form>" +
                        "<table>" +
                          "<tr>" +
                            "<td>Fake Form</td>" +
                            "<td>Input</td>" +
                          "</tr>" +
                        "</table>" +
                        "<table>" +
                          "<tr>" +
                            "<td>Fake Form</td>" +
                            "<td>Input 2</td>" +
                          "</tr>" +
                        "</table>" +
                        "<p>Bottom of form</p>" +
                      "</form>" +
                    "</td>" +
                  "</tr>" +
                "</table>" +
              "</td>" +
            "</tr>" +
          "</table>" +
        "</html>";
    /**
     * This test specifically checks to see if the inverse remapping of a
     * form is correctly handled where the form contains two tables and
     * is contained within a hierarchy of single column tables.
     */
    public void testInverseRemap() throws Exception {
        String expected =
            "<html>" +
              "<div>TD 1</div>" +
              "<div>" +
                "<div>" +
                  "<form>" +
                    "<table>" +
                      "<tr>" +
                        "<td>Fake Form</td>" +
                        "<td>Input</td>" +
                      "</tr>" +
                    "</table>" +
                    "<table>" +
                      "<tr>" +
                        "<td>Fake Form</td>" +
                        "<td>Input 2</td>" +
                      "</tr>" +
                    "</table>" +
                    "<div>" +
                      "<p>Bottom of form</p>" +
                    "</div>" +
                  "</form>" +
                "</div>" +
              "</div>" +
            "</html>";

        doProcessingTest(TEST_INVERSE_REMAP_INPUT, expected, false);

    }

    /**
     * Declared here so subclasses may re-use this string as input.
     */
    protected final String TEST_STYLE_CLASS_RETENTION =
        "<html>" +
          "<table>" +
            "<tr>" +
              "<td>TD 1</td>" +
              "<td>TD 2</td>" +
            "</tr>" +
            "<tr>" +
              "<td class=\"originalCell\">" +
                "<table>" +
                  "<tr>" +
                    "<td class=\"nestedCell\">TD 3</td>" +
                    "<td>TD 4</td>" +
                  "</tr>" +
                "</table>" +
              "</td>" +
              "<td class=\"originalCell\">" +
                "<table class=\"originalTable\">" +
                  "<tr>" +
                    "<td class=\"nestedCell\">TD 5</td>" +
                    "<td>TD 6</td>" +
                  "</tr>" +
                "</table>" +
              "</td>" +
              "<td class=\"originalCell\">" +
                "<table class=\"originalTable\">" +
                  "<tr class=\"originalRow\">" +
                    "<td class=\"nestedCell\">TD 7</td>" +
                    "<td>TD 8</td>" +
                  "</tr>" +
                "</table>" +
              "</td>" +
            "</tr>" +
          "</table>" +
        "</html>";

    /**
     * Declared here so subclasses may re-use this string as input.
     */
    protected final String TEST_TABLE_SECTION_HANDLING =
        "<html>" +
          "<table>" +
            "<thead>" +
              "<tr>" +
                "<th>Heading</th>" +
                "<th>Heading</th>" +
              "</tr>" +
            "</thead>" +
            "<tfoot>" +
              "<tr>" +
                "<th>Footer</th>" +
                "<th>Footer</th>" +
              "</tr>" +
            "</tfoot>" +
            "<tr>" +
              "<td>Body</td>" +
              "<td>Body</td>" +
            "</tr>" +
            "<tr>" +
              "<td>" +
                "<table>" +
                  "<thead>" +
                    "<tr>" +
                      "<th>Subheading</th>" +
                      "<th>Subheading</th>" +
                    "</tr>" +
                  "</thead>" +
                  "<tfoot>" +
                    "<tr>" +
                      "<th>Subfooter</th>" +
                      "<th>Subfooter</th>" +
                    "</tr>" +
                  "</tfoot>" +
                  "<tbody>" +
                    "<tr>" +
                      "<td>Subbody</td>" +
                      "<td>Subbody</td>" +
                    "</tr>" +
                  "</tbody>" +
                "</table>" +
              "</td>" +
              "<td>Next to table</td>" +
            "</tr>" +
          "</table>" +
        "</html>";

    /**
     * This test specifically checks handling of thead, tfoot and tbody
     * elements. Note that these elements are not strictly part of XHTMLBasic
     * but handling is in the base algorithm anyway.
     */
    public void testTableSectionHandling() throws Exception {
        String expected =
            "<html>" +
              "<table>" +
                "<thead>" +
                  "<tr>" +
                    "<th colspan=\"2\">Heading</th>" +
                    "<th>Heading</th>" +
                  "</tr>" +
                "</thead>" +
                "<tfoot>" +
                  "<tr>" +
                    "<th colspan=\"2\">Footer</th>" +
                    "<th>Footer</th>" +
                  "</tr>" +
                "</tfoot>" +
                "<tr>" +
                  "<td colspan=\"2\">Body</td>" +
                  "<td>Body</td>" +
                "</tr>" +
                "<tr>" +
                  "<th>Subheading</th>" +
                  "<th>Subheading</th>" +
                  "<td rowspan=\"3\">Next to table</td>" +
                "</tr>" +
                "<tr>" +
                  "<td>Subbody</td>" +
                  "<td>Subbody</td>" +
                "</tr>" +
                "<tr>" +
                  "<th>Subfooter</th>" +
                  "<th>Subfooter</th>" +
                "</tr>" +
              "</table>" +
            "</html>";

        doProcessingTest(TEST_TABLE_SECTION_HANDLING, expected, false);
    }

    /**
     * Declared here so subclasses may re-use this string as input.
     */
    protected final String TEST_TABLE_OPTIMIZATION_ATTRIBUTE_HANDLING =
        "<html>" +
          "<table OPTIMIZE=\"never\">" +
            "<tr>" +
              "<td>Loose Beer</td>" +
              "<td>" +
                "<table>" +
                  "<tr>" +
                    "<td>Free Cars</td>" +
                    "<td>Fast Women</td>" +
                  "</tr>" +
                "</table>" +
                "<table OPTIMIZE=\"little impact\">" +
                  "<tr>" +
                    "<td>Dumb Bunnies</td>" +
                    "<td>Stupid Asses</td>" +
                  "</tr>" +
                "</table>" +
              "</td>" +
            "</tr>" +
          "</table>" +
        "</html>";

    /**
     * This test specifically checks handling of the 'OPTIMIZE' attribute on
  !      * table elements.
     */
    public void testTableOptimizationAttributeHandling() throws Exception {

        // For XHTMLBasic (and any other protocol which does not support nested
        // tables), we always ignore OPTIMISE attributes and hard optimise all
        // tables.
        String expected =
                "<html>" +
                    "<table>" +
                        "<tr>" +
                            "<td rowspan=\"2\">Loose Beer</td>" +
                            "<td>Free Cars</td>" +
                            "<td>Fast Women</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<td>Dumb Bunnies</td>" +
                            "<td>Stupid Asses</td>" +
                        "</tr>" +
                    "</table>" +
                "</html>";

        doProcessingTest(TEST_TABLE_OPTIMIZATION_ATTRIBUTE_HANDLING,
                         expected,
                         false);
    }

    /**
     * Declared here so subclasses may re-use this string as input.
     */
    final protected String TEST_NO_MULTI_ATTRIBUTE_CLASS_SUPPORT =
        "<html>" +
          "<table class=\"loose\">" +
            "<tr>" +
              "<td>Loose Beer</td>" +
              "<td class=\"subtable\">" +
                "<table class=\"fastnfree\">" +
                  "<tr>" +
                    "<td class=\"cars\">Free Cars</td>" +
                    "<td>Fast Women</td>" +
                  "</tr>" +
                "</table>" +
                "<table>" +
                  "<tr class=\"dumb\">" +
                    "<td>Dumb Bunnies</td>" +
                    "<td class=\"ass\">Stupid Asses</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>Subtle</td>" +
                    "<td class=\"ubiquitous\">Persuasion</td>" +
                  "</tr>" +
                "</table>" +
              "</td>" +
            "</tr>" +
          "</table>" +
        "</html>";

    /**
     * Declared here so subclasses may re-use this string as input.
     */
    protected final String TEST_WHITESPACE_HANDLING =
        "<html>" +
          "<table>" +
            "<tr>" +
              "<td>Loose Beer</td>" +
              // ======================================================
              // The space following the td below is important!
              // This is testing that normalisation removes whitespace
              // Text nodes instead of inserting them as children of
              // Cells in the new parent table.
              // ======================================================
              "<td> " +
                "<table>" +
                  "<tr>" +
                    "<td>Free Cars</td>" +
                    "<td>Fast Women</td>" +
                  "</tr>" +
                "</table>" +
                "<table>" +
                  "<tr>" +
                    "<td>Dumb Bunnies</td>" +
                    "<td>Stupid Asses</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>Subtle</td>" +
                    "<td>Persuasion</td>" +
                  "</tr>" +
                "</table>" +
              "</td>" +
            "</tr>" +
          "</table>" +
        "</html>";

    /**
     * This integration test tests the methods
     * {@link TransVisitor}.getFirstNonWhitespaceChild(Element parent) and
     * {@link TransVisitor}.normalize(Element table, TransCell cell)
     *
     * The former is designed to ensure that the later does not include Text
     * Nodes containing only whitespace when normalising table elements and
     * their siblings into parent table cells.  Where a table cell in the
     * source is followed by whitespace it becomes a text node with whitespace.
     * After normalisation we need to ensure that we do not have a table
     * cell created containing that whitespace node.
     */
    public void testWhitespaceHandling() throws Exception {
        String expected =
                "<html>" +
                  "<table>" +
                    "<tr>" +
                      "<td rowspan=\"3\">Loose Beer</td>" +
                      "<td>Free Cars</td>" +
                      "<td>Fast Women</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td>Dumb Bunnies</td>" +
                      "<td>Stupid Asses</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td>Subtle</td>" +
                      "<td>Persuasion</td>" +
                    "</tr>" +
                  "</table>" +
                "</html>";

        doProcessingTest(TEST_WHITESPACE_HANDLING, expected, false);
    }

    /**
     * Declared here so subclasses may re-use this string as input.
     */
    final protected String TEST_NORMALIZE_INPUT =
        "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
          "<head>" +
            "<title></title>" +
          "</head>" +
          "<body>" +
            "<table width=\"100%\">" +
              "<tr>" +
                "<td>" +
                  "<table>" +
                    "<tr>" +
                      "<td>" +
                        "<table>" +
                          "<tr>" +
                            "<td>" +
                              "<table>" +
                                "<tr>" +
                                  "<td>Main Pane</td>" +
                                "</tr>" +
                              "</table>" +
                            "</td>" +
                          "</tr>" +
                          "<tr>" +
                            "<td>" +
                              "<table>" +
                                "<tr>" +
                                  "<td>" +
                                    "<table>" +
                                      "<tr>" +
                                        "<td>C</td>" +
                                      "</tr>" +
                                    "</table>" +
                                  "</td>" +
                                  "<td>" +
                                    "<table>" +
                                      "<tr>" +
                                        "<td>B</td>" +
                                      "</tr>" +
                                    "</table>" +
                                  "</td>" +
                                "</tr>" +
                                "<tr>" +
                                  "<td>" +
                                    "<table>" +
                                      "<tr>" +
                                        "<td>D</td>" +
                                      "</tr>" +
                                    "</table>" +
                                  "</td>" +
                                  "<td>" +
                                    "<table>" +
                                      "<tr>" +
                                        "<td>" +
                                          "<table>" +
                                            "<tr>" +
                                              "<td>DA</td>" +
                                            "</tr>" +
                                          "</table>" +
                                        "</td>" +
                                        "<td>" +
                                          "<table>" +
                                            "<tr>" +
                                              "<td>DB</td>" +
                                            "</tr>" +
                                          "</table>" +
                                        "</td>" +
                                      "</tr>" +
                                      "<tr>" +
                                        "<td>" +
                                          "<table>" +
                                            "<tr>" +
                                              "<td>DC</td>" +
                                            "</tr>" +
                                          "</table>" +
                                        "</td>" +
                                        "<td>" +
                                          "<table>" +
                                            "<tr>" +
                                              "<td>" +
                                                "<table>" +
                                                  "<tr>" +
                                                    "<td>DDA</td>" +
                                                  "</tr>" +
                                                "</table>" +
                                              "</td>" +
                                              "<td>" +
                                                "<table>" +
                                                  "<tr>" +
                                                    "<td>DDB</td>" +
                                                  "</tr>" +
                                                "</table>" +
                                              "</td>" +
                                            "</tr>" +
                                            "<tr>" +
                                              "<td>" +
                                                "<table>" +
                                                  "<tr>" +
                                                    "<td>DDC</td>" +
                                                  "</tr>" +
                                                "</table>" +
                                              "</td>" +
                                              "<td></td>" +
                                            "</tr>" +
                                          "</table>" +
                                        "</td>" +
                                      "</tr>" +
                                    "</table>" +
                                  "</td>" +
                                "</tr>" +
                              "</table>" +
                            "</td>" +
                          "</tr>" +
                        "</table>" +
                      "</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td>" +
                        "<form action=\"test\" method=\"get\">" +
                          "<div>" +
                            "<input name=\"vform\" type=\"hidden\" value=\"s0\"></input>" +
                            // This table tag has a div tag as a parent
                            // and a form tag as a grandparent. Therfore
                            // the normalization should not happen here.
                            "<table width=\"100%\">" +
                              "<tr>" +
                                "<td>" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td>A</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                                "<td align=\"right\">" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td align=\"right\">" +
                                        "<input name=\"a\" type=\"checkbox\" value=\"1\"></input>" +
                                      "</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                                "<td>" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td>B</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                                "<td align=\"right\">" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td align=\"right\">" +
                                        "<input name=\"b\" type=\"checkbox\" value=\"1\"></input>" +
                                      "</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                              "</tr>" +
                              "<tr>" +
                                "<td>" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td>C</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                                "<td>" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td>" +
                                        "<input name=\"c\" type=\"checkbox\" value=\"1\"></input>" +
                                      "</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                                "<td>" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td>D</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                                "<td align=\"right\">" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td align=\"right\">" +
                                        "<input name=\"b\" type=\"checkbox\" value=\"1\"></input>" +
                                      "</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                              "</tr>" +
                              "<tr>" +
                                "<td>" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td>Submit</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                                "<td>" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td>" +
                                        "<input title=\"Submit\" type=\"submit\" value=\"submit\"></input>" +
                                      "</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                                "<td></td>" +
                                "<td></td>" +
                              "</tr>" +
                            "</table>" +
                          "</div>" +
                        "</form>" +
                      "</td>" +
                    "</tr>" +
                  "</table>" +
                "</td>" +
              "</tr>" +
            "</table>" +
          "</body>" +
        "</html>";

    /**
     * Test the normalization for this family of protocols. More specifically,
     * target the case where a table tag has a div tag as a parent and a form
     * tag as a grandparent - normalization should therefore not happen.
     */
    public void testNormalize() throws Exception {
        String expected =
            "<html>" +
               "<head>" +
                   "<title/>" +
               "</head>" +
               "<body>" +
                   "<div>" +
                       "<div>Main Pane</div>" +
                       "<table>" +
                           "<tr>" +
                               "<td>C</td>" +
                               "<td colspan=\"3\">B</td>" +
                           "</tr>" +
                           "<tr>" +
                               "<td rowspan=\"3\">D</td>" +
                               "<td>DA</td>" +
                               "<td colspan=\"2\">DB</td>" +
                           "</tr>" +
                           "<tr>" +
                               "<td rowspan=\"2\">DC</td>" +
                               "<td>DDA</td>" +
                               "<td>DDB</td>" +
                           "</tr>" +
                           "<tr>" +
                               "<td>DDC</td>" +
                               "<td/>" +
                           "</tr>" +
                       "</table>" +
                       "<div>" +
                           "<form action=\"test\" method=\"get\">" +
                               "<div>" +
                                   "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
                               "</div>" +
                               "<table width=\"100%\">" +
                                   "<tr>" +
                                       "<td>A</td>" +
                                       "<td align=\"right\">" +
                                           "<input name=\"a\" type=\"checkbox\" value=\"1\"/>" +
                                       "</td>" +
                                       "<td>B</td>" +
                                       "<td align=\"right\">" +
                                           "<input name=\"b\" type=\"checkbox\" value=\"1\"/>" +
                                       "</td>" +
                                   "</tr>" +
                                   "<tr>" +
                                       "<td>C</td>" +
                                       "<td>" +
                                           "<input name=\"c\" type=\"checkbox\" value=\"1\"/>" +
                                       "</td>" +
                                       "<td>D</td>" +
                                       "<td align=\"right\">" +
                                           "<input name=\"b\" type=\"checkbox\" value=\"1\"/>" +
                                       "</td>" +
                                   "</tr>" +
                                   "<tr>" +
                                       "<td>Submit</td>" +
                                       "<td>" +
                                           "<input title=\"Submit\" type=\"submit\" value=\"submit\"/>" +
                                       "</td>" +
                                       "<td/>" +
                                       "<td/>" +
                                   "</tr>" +
                               "</table>" +
                          "</form>" +
                      "</div>" +
                   "</div>" +
               "</body>" +
            "</html>";
        doProcessingTest(TEST_NORMALIZE_INPUT, expected, false);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9223/5	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 21-Jun-05	8856/2	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 30-Jun-04	4781/3	adrianj	VBM:2002111405 Created SMS test case and added check for null/empty mime types in protocols

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 ===========================================================================
*/
