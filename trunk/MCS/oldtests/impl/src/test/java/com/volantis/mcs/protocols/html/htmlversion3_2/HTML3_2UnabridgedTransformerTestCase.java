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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/htmlversion3_2/HTML3_2UnabridgedTransformerTestCase.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-03    Phil W-S        VBM:2002110402 - Created. Tests associated
 *                              class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.htmlversion3_2;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.TestDOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests for the XHTMLFullUnabridgedTransformer.
 */
public class HTML3_2UnabridgedTransformerTestCase extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    private static ProtocolBuilder builder = new ProtocolBuilder();

    private StrictStyledDOMHelper helper;
    private InternalDevice internalDevice;

    protected void setUp() throws Exception {
        super.setUp();

        helper = new StrictStyledDOMHelper(null);

        internalDevice = InternalDeviceTestHelper.createTestDevice();
    }

    /**
     * Test optimisation when nested tables are enabled.
     *
     * @throws Exception
     */
    public void testOptimizationNestedEnabled() throws Exception {

        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestHTMLVersion3_2Factory(),
                internalDevice);

        String original =
            "<html>" +
              "<body>" +
                "<table>" +
                  "<tr>" +
                    "<td>Heading across here</td>" +
                    "<td>Heading across here</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      "<div align=\"right\">" +
                        // Test lossy optimization with attribute preservation
                        "<table OPTIMIZE=\"always\" " +
                               "border=\"1\" " +
                               "width=\"100%\">" +
                          "<tr>" +
                            "<td>Always With Border and BG from div</td>" +
                          "</tr>" +
                        "</table>" +
                        // Test that normalization tables are always optimized
                        "<span>Span</span>" +
                      "</div>" +
                      // Test normalization tables are always optimized and
                      // check that the following table is retained because of
                      // optimization rules
                      "<table OPTIMIZE=\"little impact\" " +
                             "border=\"1\" " +
                             "width=\"100%\" " +
                             "bgcolor=\"green\">" +
                        "<tr>" +
                          "<td>Little Impact With Border and BG</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                    "<td>Right Block</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      "<table OPTIMIZE=\"little impact\" " +
                             "width=\"100%\">" +
                        "<tr>" +
                          "<td>" +
                            "<span>Span</span>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                    "<td>" +
                      // Provide a table that is retained (by default)
                      "<table cellpadding=\"0\" " +
                             "cellspacing=\"0\" " +
                             "width=\"100%\" " +
                             "bgcolor=\"cyan\">" +
                        "<tr>" +
                          "<td>" +
                            // Test that this table will be optimized away
                            // because its attributes match the containing
                            // table
                            "<table OPTIMIZE=\"little impact\" " +
                                   "cellpadding=\"0\" " +
                                   "cellspacing=\"0\" " +
                                   "width=\"100%\" " +
                                   "bgcolor=\"cyan\">" +
                              "<tr>" +
                                "<td bgcolor=\"red\">Cell</td>" +
                                "<td>Cell</td>" +
                                "<td bgcolor=\"magenta\">Cell</td>" +
                              "</tr>" +
                            "</table>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +
                "</table>" +
              "</body>" +
            "</html>";
        String expected =
            "<html>" +
              "<body>" +
                "<table>" +
                  "<tr>" +
                    "<td>Heading across here</td>" +
                    "<td>Heading across here</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td align=\"right\">Always With Border and BG from div</td>" +
                    "<td rowspan=\"3\">Right Block</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td align=\"right\"><span>Span</span></td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      "<table bgcolor=\"green\" border=\"1\" width=\"100%\">" +
                        "<tr>" +
                          "<td>Little Impact With Border and BG</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td><span>Span</span></td>" +
                    "<td>" +
                      "<table bgcolor=\"cyan\" cellpadding=\"0\" " +
                             "cellspacing=\"0\" width=\"100%\">" +
                        "<tr>" +
                          "<td bgcolor=\"red\">Cell</td>" +
                          "<td bgcolor=\"cyan\">Cell</td>" +
                          "<td bgcolor=\"magenta\">Cell</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +
                "</table>" +
              "</body>" +
            "</html>";

        doTest(protocol, original, expected);
    }

    /**
     * Test that when nested tables are not supported, the redundant table
     * removal code only remaps single column tables which have no attributes
     * defined.
     *
     * @throws Exception
     */
    public void testRedundantTableRemovalNestedDisabled() throws Exception {
        // Use TestDOMProtocol so we can vary the nested table support easily.
        TestDOMProtocol protocol = (TestDOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        protocol.setSupportsNestedTables(false);

        String original =
            "<html>" +
              "<body>" +
                "<table border=\"1\">" +
                  "<tr>" +
                    "<td>" +
                      "<table>" +
                        "<tr>" +
                          "<td>Single Column</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +
                "</table>" +
              "</body>" +
            "</html>";
        String expected =
            "<html>" +
              "<body>" +
                "<table border=\"1\">" +
                  "<tr>" +
                    "<td>Single Column</td>" +
                  "</tr>" +
                "</table>" +
              "</body>" +
            "</html>";

        doTest(protocol, original, expected);
    }

    // NOTE: these two "nested disabled" tests actually come from the old
    // HTMLPalmWCA transformer testcase. HTMLPalmWCA is a subclass of HTML3.2
    // which fixes supports nested tables to false. Otherwise the transformer
    // was identical so I removed it.

    /**
     * Test table optimisation when nested tables are disabled.
     *
     * @throws Exception
     */
    public void testOptimizationNestedDisabled() throws Exception {
        // Use TestDOMProtocol so we can vary the nested table support easily.
        TestDOMProtocol protocol = (TestDOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        protocol.setSupportsNestedTables(false);

        // All these tables are optimised away because nested tables are
        // disabled.
        String original =
            "<html>" +
              "<body>" +
                "<table>" +
                  "<tr>" +
                    "<td>Heading across here 1</td>" +
                    "<td>Heading across here 2</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      "<div align=\"right\">" +
                        "<table " +
                               "border=\"1\" " +
                               "width=\"100%\">" +
                          "<tr>" +
                            "<td>Always With Border and BG from div</td>" +
                          "</tr>" +
                        "</table>" +
                        "<span>Span</span>" +
                      "</div>" +
                      "<table " +
                             "border=\"1\" " +
                             "width=\"100%\" " +
                             "bgcolor=\"green\">" +
                        "<tr>" +
                          "<td>Little Impact With Border and BG</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                    "<td>Right Block</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      "<table " +
                             "width=\"100%\">" +
                        "<tr>" +
                          "<td>" +
                            "<span>Span</span>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                    "<td>" +
                      "<table cellpadding=\"0\" " +
                             "cellspacing=\"0\" " +
                             "width=\"100%\" " +
                             "bgcolor=\"cyan\">" +
                        "<tr>" +
                          "<td>" +
                            "<table " +
                                   "cellpadding=\"0\" " +
                                   "cellspacing=\"0\" " +
                                   "width=\"100%\" " +
                                   "bgcolor=\"cyan\">" +
                              "<tr>" +
                                "<td bgcolor=\"red\">Cell</td>" +
                                "<td>Cell</td>" +
                                "<td bgcolor=\"magenta\">Cell</td>" +
                              "</tr>" +
                            "</table>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +
                "</table>" +
              "</body>" +
            "</html>";

        String expected =
            "<html>" +
              "<body>" +
                "<table>" +
                  "<tr>" +
                    "<td>Heading across here 1</td>" +
                    "<td colspan=\"3\">Heading across here 2</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td align=\"right\">Always With Border and BG from div</td>" +
                    "<td colspan=\"3\" rowspan=\"3\">Right Block</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td align=\"right\"><span>Span</span></td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td bgcolor=\"green\">Little Impact With Border and BG</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td><span>Span</span></td>" +
                    "<td bgcolor=\"red\">Cell</td>" +
                    "<td bgcolor=\"cyan\">Cell</td>" +
                    "<td bgcolor=\"magenta\">Cell</td>" +
                  "</tr>" +
                "</table>" +
              "</body>" +
            "</html>";

        doTest(protocol, original, expected);
    }

    /**
     * Test that inverse remap does retain and process all (deeply) nested
     * tables correctly when nested tables are disabled.
     *
     * @throws Exception
     */
    public void testInverseRemapTableRetentionNestedDisabled() throws Exception {
        // Use TestDOMProtocol so we can vary the nested table support easily.
        TestDOMProtocol protocol = (TestDOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        protocol.setSupportsNestedTables(false);
        String original =
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
        String expected =
                "<html>" +
                  "<head>" +
                    "<title/>" +
                  "</head>" +
                  "<body>" +
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
                          "<table width=\"100%\">" +
                            "<tr>" +
                              "<td>A</td>" +
                              "<td align=\"right\"><input name=\"a\" type=\"checkbox\" value=\"1\"/></td>" +
                              "<td>B</td>" +
                              "<td align=\"right\"><input name=\"b\" type=\"checkbox\" value=\"1\"/></td>" +
                            "</tr>" +
                            "<tr>" +
                              "<td>C</td>" +
                              "<td><input name=\"c\" type=\"checkbox\" value=\"1\"/></td>" +
                              "<td>D</td>" +
                              "<td align=\"right\"><input name=\"b\" type=\"checkbox\" value=\"1\"/></td>" +
                            "</tr>" +
                            "<tr>" +
                              "<td>Submit</td>" +
                              "<td><input title=\"Submit\" type=\"submit\" value=\"submit\"/></td>" +
                              "<td/>" +
                              "<td/>" +
                            "</tr>" +
                          "</table>" +
                        "</div>" +
                      "</form>" +
                    "</div>" +
                  "</body>" +
                "</html>";

        doTest(protocol, original, expected);
    }

    protected void doTest(DOMProtocol protocol,
                          String original,
                          String expected) throws Exception {

        Document dom = helper.parse(original);
        Document expectedDom = helper.parse(expected);
        String domAsString;
        String expectedAsString;
        DOMTransformer transformer = new HTML3_2UnabridgedTransformer(
                protocol.getProtocolConfiguration());

        transformer.transform(protocol, dom);

        domAsString = helper.render(dom);
        expectedAsString = helper.render(expectedDom);

        assertEquals("Processing of <" + original + "> not as",
                     expectedAsString,
                     domAsString);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
