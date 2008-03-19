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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/xhtmlfull/XHTMLFullTransVisitorTestCase.java,v 1.1.2.2 2003/04/04 15:20:32 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Created. Tests the
 *                              XHTMLFullTransVisitor.
 * 04-Apr-03    Adrian          VBM:2003031701 - Made this a subclass of 
 *                              XHTMLBasicTransVisitorTestCase and removed some
 *                              redundant code. 
 * 04-Jun-03    Byron           VBM:2003042204 - Implemented various test
 *                              cases.
 * 04-Jun-03    Byron           VBM:2003042204 - Idented testcases. Removed
 *                              createTransVisitor.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.xhtmlfull;

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.Utils;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.html.XHTMLBasicTransVisitorTestCase;
import com.volantis.mcs.protocols.html.XHTMLFullConfiguration;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.mcs.devices.InternalDevice;

/**
 * Tests the XHTMLFullTransVisitor. Note that this does not include testing
 * the usage of the visitor, but merely the specialist behaviour.
 */
public class XHTMLFullTransVisitorTestCase extends 
        XHTMLBasicTransVisitorTestCase {

    // javadoc inherited from superclass
    protected DOMProtocol createDOMProtocol(InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        return (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestXHTMLFullFactory(),
                internalDevice);
    }

    // javadoc inherited from superclass
    protected TransFactory getProtocolSpecificFactory() {
        return new XHTMLFullTransFactory(new XHTMLFullConfiguration());
    }

    public void testGetPromotePreserveStyleAttributes() throws Exception {
        // Don't expect to find class as this is handled separately
        String[] expected =
            {"border", "cellpadding", "cellspacing", "dir", "frame", "id",
             "lang",
             "onclick", "ondblclick", "onkeydown", "onkeypress", "onkeyup",
             "onmousedown", "onmousemove", "onmouseout", "onmouseover",
             "onmouseup",
             "rules", "style", "summary", "title", "width", "xml:lang"};

        TransVisitor visitor = getProtocolSpecificFactory().getVisitor(protocol);
        String result = Utils.findMismatches(expected,
            ((XHTMLFullTransVisitor)visitor).getPromotePreserveStyleAttributes());

        assertTrue("Preserve style attribute mismatches: " + result,
                   (result == null));
    }
    
    /**
     * todo Implement this test!
     * This has been stubbed out for the moment as this will currently fail
     * with the XHTMLBasicTransVisitorTestCase expected results.  this is
     * because XHTMLBasicTransVisitor optimizes away nested tables whereas
     * we do not here.
     * 
     * When implemented this test should demonstrate that this output is the
     * same as the input but with whitespace removed from tables siblings.
     */
    public void testBasicInterface() throws Exception {
    }



    /**
     * When implemented this test should demonstrate that this output is the
     * same as the input but with whitespace removed from tables siblings.
     */
    public void testStyleClassRetention() throws Exception {
        String expected =
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
        doProcessingTest(TEST_STYLE_CLASS_RETENTION,
                         expected, false);
    }

    public void testWhitespaceHandling() throws Exception {
        String expected =
            "<html>" +
              "<table>" +
                "<tr>" +
                  "<td rowspan=\"2\">Loose Beer</td>" +
                  "<td>" +
                    "<table>" +
                      "<tr>" +
                        "<td>Free Cars</td>" +
                        "<td>Fast Women</td>" +
                      "</tr>" +
                    "</table>" +
                  "</td>" +
                "</tr>" +
                "<tr>" +
                  "<td>" +
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
        doProcessingTest(TEST_WHITESPACE_HANDLING,
                         expected, false);
    }

    public void testTableOptimizationAttributeHandling() throws Exception {
        String expected =
            "<html>" +
              "<table>" +
                "<tr>" +
                  "<td rowspan=\"2\">Loose Beer</td>" +
                  "<td colspan=\"2\">" +
                    "<table>" +
                      "<tr>" +
                        "<td>Free Cars</td>" +
                        "<td>Fast Women</td>" +
                      "</tr>" +
                    "</table>" +
                  "</td>" +
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

    public void testTableSectionHandling() throws Exception {
        String expected =
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
        doProcessingTest(TEST_TABLE_SECTION_HANDLING,
                         expected, false);
    }

    public void testInverseRemap() throws Exception {
        String expected =
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
                                "<td>" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td>Fake Form</td>" +
                                      "<td>Input</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                              "</tr>" +
                              "<tr>" +
                                "<td>" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td>Fake Form</td>" +
                                      "<td>Input 2</td>" +
                                    "</tr>" +
                                  "</table>" +
                                "</td>" +
                              "</tr>" +
                              "<tr>" +
                                "<td>" +
                                  "<p>Bottom of form</p>" +
                                "</td>" +
                              "</tr>" +
                            "</table>" +
                          "</form>" +
                        "</td>" +
                      "</tr>" +
                    "</table>" +
                  "</td>" +
                "</tr>" +
              "</table>" +
            "</html>";
        doProcessingTest(TEST_INVERSE_REMAP_INPUT,
                         expected, false);
    }

    public void testNormalize() throws Exception {
        String expected =
            "<html>" +
              "<head>" +
                "<title/>" +
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
                                                  "<td/>" +
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
                                "<input name=\"vform\" type=\"hidden\" value=\"s0\"/>" +
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
                                              "<input name=\"a\" type=\"checkbox\" value=\"1\"/>" +
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
                                              "<input name=\"b\" type=\"checkbox\" value=\"1\"/>" +
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
                                              "<input name=\"c\" type=\"checkbox\" value=\"1\"/>" +
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
                                              "<input name=\"b\" type=\"checkbox\" value=\"1\"/>" +
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
                                              "<input title=\"Submit\" type=\"submit\" value=\"submit\"/>" +
                                            "</td>" +
                                          "</tr>" +
                                        "</table>" +
                                      "</td>" +
                                      "<td/>" +
                                      "<td/>" +
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

 22-Aug-05	9223/2	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 ===========================================================================
*/
