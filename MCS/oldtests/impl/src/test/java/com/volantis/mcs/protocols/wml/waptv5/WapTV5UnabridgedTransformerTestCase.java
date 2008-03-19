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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/waptv5/WapTV5UnabridgedTransformerTestCase.java,v 1.2 2003/01/15 12:42:10 philws Exp $
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
package com.volantis.mcs.protocols.wml.waptv5;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.wml.WMLRootConfiguration;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests WapTV5UnabridgedTransformer.
 */
public class WapTV5UnabridgedTransformerTestCase extends TestCaseAbstract {

    private StrictStyledDOMHelper helper;

    protected void setUp() throws Exception {
        super.setUp();

        helper = new StrictStyledDOMHelper(null);
    }

    public void testNothingWAPTVIsFracked() {
        
    }

    public void notestOptimizationWithMultiClasses() throws Exception {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(), null);
        String original =
            "<wml>" +
              "<card>" +
                // Leave a duff column count attribute here (even though the
                // protocol won't actually generate this sort of thing) to
                // verify that the transformer will ignore this value
                "<table columns=\"200\">" +
                  "<tr>" +
                    "<td>Heading across here</td>" +
                    "<td>Heading across here</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      // Test non-promotion (and therefore table retention)
                      "<strong>" +
                        "<table OPTIMIZE=\"always\">" +
                          "<tr>" +
                            "<td>Always in Paragraph</td>" +
                          "</tr>" +
                        "</table>" +
                      "</strong>" +
                      // Test normalization tables are always optimized and
                      // check that the following table is retained because of
                      // optimization rules
                      "<table OPTIMIZE=\"little impact\" " +
                             "rowgap=\"5\">" +
                        "<tr>" +
                          "<td>Little Impact With Rowgap</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                    "<td>Right Block</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      // Test that this table can be optimized away (i.e. that
                      // table ID checks don't see the p's ID)
                      "<table OPTIMIZE=\"little impact\">" +
                        "<tr>" +
                          "<td>" +
                            "<p id=\"pid\">P with Id</p>" +
                          "</td>" +
                          "<td>Second Column</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                    "<td>" +
                      // Provide a table that is retained (by default)
                      "<table colgap=\"0\" " +
                             "rowgap=\"0\">" +
                        "<tr>" +
                          "<td>" +
                            // Test that this table will be retained because
                            // a cell has an ID set
                            "<table OPTIMIZE=\"little impact\" " +
                                   "colgap=\"0\" " +
                                   "rowgap=\"0\">" +
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
                                   "style=\"background-color:green\" " +
                                   "colgap=\"0\" " +
                                   "rowgap=\"0\">" +
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
              "</card>" +
            "</wml>";
        String expected =
            "<wml>" +
              "<card>" +
                "<table columns=\"3\">" +
                  "<tr>" +
                    "<td colspan=\"2\">Heading across here</td>" +
                    "<td>Heading across here</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td colspan=\"2\">" +
                      "<strong>" +
                        // Table retained because promotion is not allowed
                        "<table columns=\"1\">" +
                          "<tr>" +
                            "<td>Always in Paragraph</td>" +
                          "</tr>" +
                        "</table>" +
                      "</strong>" +
                    "</td>" +
                    "<td rowspan=\"2\">Right Block</td>" +
                  "</tr>" +
                  "<tr>" +
                    // The normalization table has been integrated into the
                    // main table
                    "<td colspan=\"2\">" +
                      // This table is retained because of the rowgap being
                      // different from the containing table's rowgap
                      "<table columns=\"1\" rowgap=\"5\">" +
                        "<tr>" +
                          "<td>Little Impact With Rowgap</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +
                  "<tr>" +
                    // This table has been optimized away (showning that
                    // table ID checks don't see the p's ID)
                    "<td>" +
                      "<p id=\"pid\">P with Id</p>" +
                    "</td>" +
                    "<td>Second Column</td>" +
                    "<td>" +
                      // This table is retained (by default - no 'OPTIMIZE')
                      "<table colgap=\"0\" columns=\"4\" rowgap=\"0\">" +
                        "<tr>" +
                          "<td>" +
                            // This table has been retained because
                            // a cell has an ID set
                            "<table colgap=\"0\" columns=\"3\" rowgap=\"0\">" +
                              "<tr>" +
                                "<td>Cell</td>" +
                                "<td id=\"tdId\">Cell with Id</td>" +
                                "<td>Cell</td>" +
                              "</tr>" +
                            "</table>" +
                          "</td>" +
                          // This table was optimized away because its
                          // attributes match the containing table
                          "<td style=\"background-color:green\">Cell</td>" +
                          "<td style=\"background-color:green\">Cell</td>" +
                          "<td style=\"background-color:green\">Cell</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +
                "</table>" +
              "</card>" +
            "</wml>";

        doTest(protocol, original, expected);
    }

    protected void doTest(DOMProtocol protocol, String original,
            String expected) throws Exception {

        Document dom = helper.parse(original);
        Document expectedDom = helper.parse(expected);

        DOMTransformer transformer = getTransformer();

        transformer.transform(protocol, dom);

        String domAsString = helper.render(dom);
        String expectedAsString = helper.render(expectedDom);

        assertEquals("Processing of <" + original + "> not as",
                     expectedAsString, domAsString);
    }

    /**
     * Supporting method allows this test case to be specialized.
     *
     * @return the transformer to be tested
     */
    protected DOMTransformer getTransformer() {
        return new WapTV5UnabridgedTransformer(
                new WMLRootConfiguration());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9522/1	ibush	VBM:2005091502 no_save on images

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9223/1	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 16-Mar-05	7372/2	emma	VBM:2005031008 Make cols attribute optional on the XDIME table element

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
