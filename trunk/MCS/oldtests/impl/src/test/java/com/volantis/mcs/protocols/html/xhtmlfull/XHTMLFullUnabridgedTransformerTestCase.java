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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/xhtmlfull/XHTMLFullUnabridgedTransformerTestCase.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jan-03    Phil W-S        VBM:2002110402 - Created. New test case for the
 *                              XHTMLFull protocol-specific
 *                              UnabridgedTransformer.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.xhtmlfull;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.html.XHTMLFullConfiguration;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests for the XHTMLFullUnabridgedTransformer.
 */
public class XHTMLFullUnabridgedTransformerTestCase extends TestCaseAbstract {

    private StrictStyledDOMHelper helper;

    protected void setUp() throws Exception {
        super.setUp();

        helper = new StrictStyledDOMHelper(null);
    }

    public void testOptimization() throws Exception {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestXHTMLFullFactory(),
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
                      // Test promotion with multi-classes
                      "<div style=\"background-color:green\">" +
                        // Test lossy optimization with attribute preservation
                        "<table OPTIMIZE=\"always\" " +
                               "border=\"1\" " +
                               "width=\"100%\" " +
                               "style=\"font-family:sans-serif\">" +
                          "<tr>" +
                            "<td>Always With Border</td>" +
                          "</tr>" +
                        "</table>" +
                        // Test that normalization tables are always optimized
                        "<span style=\"text-align:center\">Span</span>" +
                      "</div>" +
                      // Test normalization tables are always optimized and
                      // check that the following table is retained because of
                      // optimization rules
                      "<table OPTIMIZE=\"little impact\" " +
                             "border=\"1\" " +
                             "width=\"100%\" " +
                             "style=\"font-family:sans-serif\">" +
                        "<tr>" +
                          "<td>Little Impact With Border</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                    "<td>Right Block</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      // Test that this table can be optimized away (i.e. that
                      // table ID checks don't see the span's ID)
                      "<table OPTIMIZE=\"little impact\" " +
                             "width=\"100%\">" +
                        "<tr>" +
                          "<td>" +
                            "<span id=\"spanid\">Span with Id</span>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                    "<td>" +
                      // Provide a table that is retained (by default)
                      "<table cellpadding=\"0\" " +
                             "cellspacing=\"0\" " +
                             "width=\"100%\">" +
                        "<tr>" +
                          "<td>" +
                            // Test that this table will be retained because
                            // a cell has an ID set
                            "<table OPTIMIZE=\"little impact\" " +
                                   "cellpadding=\"0\" " +
                                   "cellspacing=\"0\" " +
                                   "width=\"100%\">" +
                              "<tr>" +
                                "<td>Cell</td>" +
                                "<td id=\"tdId\">Cell with Id</td>" +
                                "<td>Cell</td>" +
                              "</tr>" +
                            "</table>" +
                          "</td>" +
                          "<td>" +
                            // Test that this table will be optimized away
                            // because its attributes match the containing
                            // table
                            "<table OPTIMIZE=\"little impact\" " +
                                   "style=\"vertical-align:top\" " +
                                   "cellpadding=\"0\" " +
                                   "cellspacing=\"0\" " +
                                   "width=\"100%\">" +
                              "<tr>" +
                                "<td>Cell</td>" +
                                "<td>Cell</td>" +
                                "<td>Cell</td>" +
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
                    "<td style=\"background-color:green; font-family:sans-serif\">Always With Border</td>" +
                    "<td rowspan=\"3\">Right Block</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td style=\"background-color:green\">" +
                      "<span style=\"text-align:center\">Span</span>" +
                    "</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      "<table border=\"1\" style=\"font-family:sans-serif\" width=\"100%\">" +
                        "<tr>" +
                          "<td>Little Impact With Border</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      "<span id=\"spanid\">Span with Id</span>" +
                    "</td>" +
                    "<td>" +
                      "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                        "<tr>" +
                          "<td>" +
                            "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                              "<tr>" +
                                "<td>Cell</td>" +
                                "<td id=\"tdId\">Cell with Id</td>" +
                                "<td>Cell</td>" +
                              "</tr>" +
                            "</table>" +
                          "</td>" +
                          "<td style=\"vertical-align:top\">Cell</td>" +
                          "<td style=\"vertical-align:top\">Cell</td>" +
                          "<td style=\"vertical-align:top\">Cell</td>" +
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
     * Convenience method which sets up and runs the transformation test.
     *
     * @param protocol  to be used in the test
     * @param original  markup to be transformed
     * @param expected  expected result of transformation
     * @throws Exception if there was a problem parsing the XML
     */
    protected void doTest(DOMProtocol protocol, String original,
            String expected) throws Exception {

        Document dom = helper.parse(original);
        Document expectedDom = helper.parse(expected);

        DOMTransformer transformer = getTransformer();

        transformer.transform(protocol, dom);

        String actualXML = helper.render(dom);
        String expectedXML = helper.render(expectedDom);

        assertEquals("Processing of <" + original + "> not as", expectedXML,
                actualXML);
    }

    /**
     * Supporting method allows this test case to be specialized.
     *
     * @return the transformer to be tested
     */
    protected DOMTransformer getTransformer() {
        return new XHTMLFullUnabridgedTransformer(new XHTMLFullConfiguration());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9223/5	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

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
