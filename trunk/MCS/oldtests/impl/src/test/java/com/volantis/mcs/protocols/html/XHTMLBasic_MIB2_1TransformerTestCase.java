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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLBasic_MIB2_1TransformerTestCase.java,v 1.3 2003/01/27 16:36:59 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Dec-02    Byron           VBM:2002121126 - Created
 * 24-Jan-03    Byron           VBM:2003012404 - Split trasform test case into
 *                              separate methods. Updated test cases to cater
 *                              this bug fix.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.trans.NullRemovingDOMTransformer;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the MIB2_1 transformer. In particular specialized transformations
 * specialized for the MIB2_1 (other transformations should be tested by the
 * XTHMLBasicTransformer).
 */
public class XHTMLBasic_MIB2_1TransformerTestCase extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();
    private StrictStyledDOMHelper helper;

    protected void setUp() throws Exception {
        super.setUp();

        helper = new StrictStyledDOMHelper(null);
    }

    /**
     * Test the run.2002091901.jsp page within this testcase.
     * @throws Exception
     */
    public void testTransformComplexNesting()
        throws Exception {
        String input =
              "<html>" +
              "<head>" +
                "<title>G_img_315_v01_tln_uk</title>" +
              "</head>" +
              "<body>" +
                "<table>" +
                  "<tr>" +
                    "<td align=\"center\">" +
                      "<table>" +
                        "<tr>" +
                          "<td align=\"center\" style=\"background-color:green\">" +
                            "<a style=\"color:red\" href=\"http://www.3.com\">" +
                              "<img alt=\"\" style=\"vertical-align:bottom\" height=\"21\" src=\"/titlebar.jpg\" width=\"171\"></img>" +
                            "</a>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      "<div style=\"background-color:white; color:blue\">" +
                        "<table>" +
                          "<tr>" +
                            "<td valign=\"top\" style=\"background-color:green\" >" +
                               "<img alt=\"\" style=\"vertical-align:bottom\" height=\"91\" src=\"/large.jpg\" width=\"76\"/>" +
                            "</td>" +
                            "<td align=\"center\" valign=\"top\">" +
                              "<table>" +
                                "<tr>" +
                                  "<td>" +
                                    "<div style=\"background-color:white; color:blue\">" +
                                      "<table>" +
                                        "<tr>" +
                                          "<td style=\"font-weight:bold\">" +
                                            "Headline here" +
                                          "</td>" +
                                        "</tr>" +
                                      "</table>" +
                                    "</div>" +
                                    "<div style=\"background-color:white; color:blue\">" +
                                      "<table>" +  // 1 row, 2 columns, 1st column has image
                                        "<tr>" +
                                          "<td style=\"background-color:green\">" +
                                              "<img alt=\"\" style=\"vertical-align:bottom\" height=\"13\" src=\"/audio.jpg\" width=\"13\"/>" +
                                          "</td>" +
                                          "<td style=\"background-color:green\">" +
                                              "<a style=\"color:red\" href=\"http://www.1.com\">Video</a>" +
                                          "</td>" +
                                        "</tr>" +
                                      "</table>" +
                                    "</div>" +
                                    "<div style=\"background-color:white; color:blue\">" +
                                      "<table>" + // 1 row, 2 columns, 1st column has image
                                        "<tr>" +
                                          "<td style=\"background-color:green\">" +
                                              "<img alt=\"\" style=\"vertical-align:bottom\" height=\"13\" src=\"/audio.jpg\" width=\"13\"/>" +
                                          "</td>" +
                                          "<td style=\"background-color:green\">" +
                                              "<a style=\"color:red\" href=\"http://www.2.com\">Audio</a>" +
                                          "</td>" +
                                        "</tr>" +
                                      "</table>" +
                                    "</div>" +
                                    "<div style=\"background-color:white; color:blue\">" +
                                      "<table>" + // 1 row, 2 columns, 1st column has image
                                        "<tr>" +
                                          "<td style=\"background-color:green\">" +
                                              "<img alt=\"\" style=\"vertical-align:bottom\" height=\"14\" src=\"/image.jpg\" width=\"15\"/>" +
                                          "</td>" +
                                          "<td style=\"background-color:green\">" +
                                              "<a style=\"color:red\" href=\"http://www.3.com\">Image</a>" +
                                          "</td>" +
                                        "</tr>" +
                                      "</table>" +
                                    "</div>" +
                                  "</td>" +
                                "</tr>" +
                              "</table>" +
                            "</td>" +
                          "</tr>" +
                        "</table>" +
                      "</div>" +
                    "</td>" +
                  "</tr>" +
                "</table>" +
              "</body>" +
            "</html>";
        //------------------------------------------------------
        // Expected
        //------------------------------------------------------
        String expected =
            "<html>" +
              "<head>" +
                "<title>G_img_315_v01_tln_uk</title>" +
              "</head>" +
              "<body>" +
               "<div>" +
                "<div style=\"background-color:green\" >" +
                  "<a style=\"color:red\" href=\"http://www.3.com\">" +
                    "<img alt=\"\" style=\"vertical-align:bottom\" height=\"21\" src=\"/titlebar.jpg\" width=\"171\"></img>" +
                  "</a>" +
                "</div>" +
                "<div style=\"background-color:white; color:blue\">" +
                  "<table>" +
                    "<tr>" +
                      "<td style=\"background-color:green\" valign=\"top\">" +
                          "<img alt=\"\" style=\"vertical-align:bottom\" height=\"91\" src=\"/large.jpg\" width=\"76\"></img>" +
                      "</td>" +
                      "<td align=\"center\" valign=\"top\">" +
                          "<div style=\"background-color:white; color:blue\" >" +
                            "<div style=\"font-weight:bold\">Headline here</div>" +
                          "</div>" +
                          "<div style=\"background-color:white; color:blue\">" +
                            "<img alt=\"\" style=\"vertical-align:bottom; background-color:green; display:inline\" height=\"13\" src=\"/audio.jpg\" width=\"13\"></img>" +
                            "<a style=\"color:red; background-color:green; display:inline\" href=\"http://www.1.com\">Video</a>" +
                           "<br/>" +
                          "</div>" +
                          "<div style=\"background-color:white; color:blue\">" +
                            "<img alt=\"\" style=\"vertical-align:bottom; background-color:green; display:inline\" height=\"13\" src=\"/audio.jpg\" width=\"13\"></img>" +
                            "<a style=\"color:red; background-color:green; display:inline\" href=\"http://www.2.com\">Audio</a>" +
                            "<br/>" +
                          "</div>" +
                          "<div style=\"background-color:white; color:blue\">" +
                            "<img alt=\"\" style=\"vertical-align:bottom; background-color:green; display:inline\" height=\"14\" src=\"/image.jpg\" width=\"15\"></img>" +
                            "<a style=\"color:red; background-color:green; display:inline\" href=\"http://www.3.com\">Image</a>" +
                            "<br/>" +
                          "</div>" +
                      "</td>" +
                    "</tr>" +
                  "</table>" +
                "</div>" +
                "</div>" +
              "</body>" +
            "</html>";
        doTransformTest(input, expected);
    }

    /**
     * Test the ut_nested.jsp page within this testcase. Original 1.
     * @throws Exception
     */
    public void testTransformComplexTextSplitterAndForms()
        throws Exception {

        String input =
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
            "</html>";
        //------------------------------------------------------
        // Expected
        //------------------------------------------------------
        String expected =
            "<html>" +
            "<head>" +
            "</head>" +
            "<body>" +
                "<div>" +
                    "<div>Header</div>" +
                    "<div>Main</div>" +
                "</div>" +
                "<form><div/></form>" +
                "<div>Text</div>" +
            "</body>" +
            "</html>";

        doTransformTest(input, expected);
    }

    /**
     * Test the removal of br (modification)
     * @throws Exception
     */
    public void testTransformRemovalofBrTag()
            throws Exception {
        String input =
            "<html>" +
              "<head>" +
                "<title>Title</title>" +
              "</head>" +
              "<body>" +
                "<table>" +  // 2 x 1 table
                  "<tr>" +   // 1st row
                    "<td align=\"center\">" +
                      "<table>" +  // 1 x 1 table
                        "<tr>" +
                          "<td align=\"center\" style=\"background-color:green\">" +
                            "<a style=\"color:red\" href=\"#\">LINK" +
                            "</a>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +
                  "<tr>" +   // 2nd row
                    "<td>" +
                      "<div style=\"background-color:yellow\">" +
                        "Text" +
                      "</div>" +
                    "</td>" +
                  "</tr>" +
                "</table>" +
              "</body>" +
            "</html>";
        //------------------------------------------------------
        // Expected
        //------------------------------------------------------
        String expected =
            "<html>" +
              "<head>" +
                "<title>Title</title>" +
              "</head>" +
              "<body>" +
                "<div>" +
                  "<div style=\"background-color:green\" >" +
                    "<a style=\"color:red\" href=\"#\">LINK" +
                    "</a>" +
                  "</div>" +
                  "<div style=\"background-color:yellow\">" +
                    "Text" +
                  "</div>" +
                "</div>" +
              "</body>" +
            "</html>";
        doTransformTest(input, expected);
    }

    /**
     * Test the run.2002091901.jsp (Updated version).
     * @throws Exception
     */
    public void testTransformComplexWithTextAfterTd()
            throws Exception {
        String input =
            "<html>" +
              "<head>" +
                "<title>G_img_315_v01_tln_uk</title>" +
              "</head>" +

              "<body>" +
                "<table>" +
                  "<tr>" +
                    "<td align=\"center\">" +
                      "<table>" +
                        "<tr>" +
                          "<td align=\"center\" style=\"background-color:green\">" +
                            "<a style=\"color:red\" href=\"#\">" +
                              "<img alt=\"\" style=\"vertical-align:top\" height=\"21\" src=\"titlebar.jpg\" width=\"171\" ></img>" +
                            "</a> " +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</td>" +
                  "</tr>" +

                  "<tr>" +
                    "<td>" +
                      "<table>" +
                        "<tr>" +
                          "<td style=\"text-align:center\" valign=\"top\"><img alt=\"\" style=\"vertical-align:top\" height=\"91\" src=\"large.jpg\" width=\"76\" ></img></td>" +

                          "<td align=\"center\" valign=\"top\">" +
                            "<table>" +
                              "<tr>" +
                                "<td>" +
                                  "<table>" +
                                    "<tr>" +
                                      "<td style=\"text-align:center; vertical-align:top\">Headline here</td>" +
                                    "</tr>" +
                                  "</table>" +

                                  "<table>" +
                                    "<tr>" +

                                      "<td style=\"text-align:center; color:green\">" +
                                        "<img alt=\"\" style=\"vertical-align:top\" height=\"13\" src=\"audio.jpg\" width=\"13\"></img> " +
                                      "</td>" +
                                      "<td style=\"text-align:center; color:green\">" +
                                        "<a style=\"color:red\" href=\"#\">Video</a>" +
                                      "</td>" +
                                    "</tr>" +
                                  "</table>" +

                                  "<table>" +
                                    "<tr>" +
                  "TEXT" +  // This text caused a class cast exception. See VBM:2002121126
                                      "<td style=\"text-align:center; color:green\">" +
                                        "<img alt=\"\" style=\"vertical-align:top\" height=\"13\" src=\"audio.jpg\" width=\"13\"></img> " +
                                      "</td>" +
                                      "<td style=\"text-align:center; color:green\">" +
                                        "<a style=\"color:red\" href=\"#\">Audio</a>" +
                                      "</td>" +
                                    "</tr>" +
                                  "</table>" +

                                  "<table>" +
                                    "<tr>" +
                                      "<td style=\"text-align:center; color:green\">" +
                                        "<img alt=\"\" style=\"vertical-align:top\" height=\"14\" src=\"image.jpg\" width=\"15\" ></img> " +
                                      "</td>" +
                                      "<td style=\"text-align:center; color:green\">" +
                                        "<a style=\"color:red\" href=\"#\">Image</a>" +
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
                "</table>" +
              "</body>" +
            "</html>";
        
        String expected =
            "<html>" +
            "<head>" +
              "<title>G_img_315_v01_tln_uk</title>" +
            "</head>" +
            "<body>" +
            "<div>" +
              "<div style=\"background-color:green\">" +
                "<a style=\"color:red\" href=\"#\">" +
                  "<img alt=\"\" style=\"vertical-align:top\" height=\"21\" src=\"titlebar.jpg\" width=\"171\"/>" +
                "</a> " +
              "</div>" +
              "<table>" +
                "<tr>" +
                  "<td style=\"text-align:center\" valign=\"top\">" +
                    "<img alt=\"\" style=\"vertical-align:top\" height=\"91\" src=\"large.jpg\" width=\"76\"/>" +
                  "</td>" +
                  "<td align=\"center\" valign=\"top\">" +
                    "<div style=\"text-align:center; vertical-align:top\">" +
                      "Headline here" +
                    "</div>" +
                    "<img alt=\"\" style=\"vertical-align:top; text-align:center; color:green; display:inline\" height=\"13\" src=\"audio.jpg\" width=\"13\"/> " +
                    "<a style=\"color:red; text-align:center; display:inline\" href=\"#\">Video</a>" +
                    "<br/>" +
                    "<img alt=\"\" style=\"vertical-align:top; text-align:center; color:green; display:inline\" height=\"13\" src=\"audio.jpg\" width=\"13\"/> " +
                    "<a style=\"color:red; text-align:center; display:inline \" href=\"#\">Audio</a>" +
                    "<br/>" +
                    "<img alt=\"\" style=\"vertical-align:top; text-align:center; color:green; display:inline\" height=\"14\" src=\"image.jpg\" width=\"15\"/> " +
                    "<a style=\"color:red; text-align:center; display:inline\" href=\"#\">Image</a>" +
                    "<br/>" +
                  "</td>" +
                "</tr>" +
              "</table>" +
            "</div>" +
            "</body>" +
            "</html>";
        doTransformTest(input, expected);
    }

    /**
     * Supporting method for testing the transformation test case.
     *
     * @param  original            the original html dom input string
     * @param  expected            the expected result
     * @throws Exception           if anything goes wrong
     */
    private void doTransformTest(String original, String expected)
            throws Exception  {

        DOMProtocol protocol = createProtocol();

        Document originalDOM = helper.parse(original);
        Document expectedDOM = helper.parse(expected);

        XHTMLBasic_MIB2_1Transformer transformer =
                new XHTMLBasic_MIB2_1Transformer();

        Document transformedDOM = transformer.transform(protocol, originalDOM);

        // In actual operation this transformer would form a compound
        // transformer with the NullRemovingDOMTransformer.
        NullRemovingDOMTransformer nullRemover = new NullRemovingDOMTransformer();
        nullRemover.transform(null, transformedDOM);

        final String expectedXML = helper.render(expectedDOM);
        final String actualXML = helper.render(transformedDOM);

        boolean failed = true;
        try {
            assertXMLEquals("DOM comparison failed", expectedXML, actualXML);
            failed = false;
        }
        finally {
            if (failed) {
                System.out.println("Expected: " + expectedXML);
                System.out.println("Actual  : " + actualXML);
            }
        }

    }

    private DOMProtocol createProtocol() {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        ProtocolBuilder builder = new ProtocolBuilder();
        return (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestXHTMLBasic_MIB2_1Factory(),
                internalDevice);
    }

    /**
     * @todo later must be done sometime?
     * @throws Exception
     */
    public void notestGetFirstChildElement() throws Exception{
    }

    /**
     * @todo later must be done sometime?
     * @throws Exception
     */
    public void notestTransformTableElement() throws Exception{
    }

    /**
     *
     * @throws Exception
     *
     * todo simplify this so that instead of subclassing and creating a
     * todo transformer that it just creates the visitor directly and tests
     * todo that. Much simpler.
     */
    public void testMatches() throws Exception{
        Node node = null;
        MyXHTMLBasicMIB2_1Transformer transformer =
                new MyXHTMLBasicMIB2_1Transformer();
        DOMProtocol protocol = createProtocol();

        transformer.initialize(protocol);

        assertTrue(transformer.matches(node, null) == false);
        Element element = domFactory.createElement();
        element.setName("div");
        assertTrue(transformer.matches(element, "div"));

        element.setName("");
        assertTrue(transformer.matches(element, ""));

        element.setName(null);
        assertTrue(transformer.matches(element, "div") == false);
        assertTrue(transformer.matches(element, "") == false);
        try {
            assertTrue(transformer.matches(element, null));
            fail ("Should throw null pointer exception.");
        } catch (NullPointerException e) {
        }

        // Mis-matching names and case-sensitivity.
        element.setName("Name mismatch test");
        assertTrue(transformer.matches(element, "div") == false);
        assertTrue(transformer.matches(element, "name mismatch test") == false);
        assertTrue(transformer.matches(element, "Name mismatch test"));
    }

    /**
     * @todo later must be done sometime?
     * @throws Exception
     */
    public void notestFindImageNode() throws Exception {
    }

    /**
     * @todo later must be done sometime?
     * @throws Exception
     */
    public void notestEligibleForTransformation() throws Exception{
    }

}

/**
 * Package class provides access to protected methods in
 * <code>MIB2_1TreeVisitor</code> which is an inner class of
 * <code>XHTMLBasicMIB2_1Transformer</code>.
 */
class MyXHTMLBasicMIB2_1Transformer extends XHTMLBasic_MIB2_1Transformer {

    protected MyTreeVisitor treeVisitor = null;
    
    /**
     * Permit outer class to access TreeVisitor's protected methods via
     * proxy calls.
     */
    class MyTreeVisitor extends MIB2_1TreeVisitor {
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
     * Proxy for matches() in superclass
     * @see XHTMLBasic_MIB2_1Transformer.MIB2_1TreeVisitor#matches
     */
    protected boolean matches(Node node, String name) {
        return treeVisitor.matches(node, name);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9184/6	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 22-Aug-05	9223/4	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/1	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
