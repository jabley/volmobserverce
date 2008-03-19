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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/WMLDOMTransformerTestCase.java,v 1.1.2.7 2003/03/13 19:18:22 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Dec-02    Allan           VBM:2002112906 - Testcase for
 *                              WMLDOMTransformer. Current this testcase only
 *                              really excercises table fixing i.e. it is
 *                              by no means a complete testcase.
 * 15-Jan-03    Chris W         VBM:2002111508 - testTransformRemoveExtraneousPTags
 *                              added.
 * 27-Jan-03    Geoff           VBM:2003012302 - add tests to check that 
 *                              promotion of P tags does not remove things it
 *                              should not.
 * 28-Jan-03    Geoff           VBM:2003012227 - add test to check this VBM 
 *                              does not return - it was fixed by prev VBM.
 * 12-Mar-03    Phil W-S        VBM:2003031110 - Add testing of addParagraphs.
 * 26-May-03    Chris W         VBM:2003052205 - Added tests to check that
 *                              dividehints are converted into keeptogethers
 *                              correctly. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceMock;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test the transformation of the DOM tree for WML.
 */
public class WMLDOMTransformerTestCase extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    WMLDOMTransformer transformer;
    WMLRoot protocol;
//    MyMarinerPageContext context;
    Element element;
    private InternalDeviceMock deviceMock;
    private ProtocolConfiguration configuration;


    public WMLDOMTransformerTestCase(String name) {
        super(name);
    }


    protected void setUp() throws Exception {
        super.setUp();
        
        deviceMock = new InternalDeviceMock("deviceMock", expectations);

        deviceMock.fuzzy.getBooleanPolicyValue(mockFactory.expectsAny())
                .returns(false).any();

        initializeTransformer();

        element = domFactory.createElement();
    }

    private void initializeTransformer() {
        //        context = new MyMarinerPageContext();

        deviceMock.fuzzy.getPolicyValue(mockFactory.expectsAny())
                .returns(null).any();
        deviceMock.fuzzy.getIntegerPolicyValue(mockFactory.expectsAny())
                .fails(new NumberFormatException()).any();
        deviceMock.fuzzy.getBooleanPolicyValue(mockFactory.expectsAny())
                .returns(false).any();
        deviceMock.expects.getStyleSheetVersion().returns(null).any();
        deviceMock.expects.getName().returns("mock").any();

        deviceMock.expects.getProtocolConfiguration().returns(null);
        deviceMock.fuzzy.setProtocolConfiguration(
                mockFactory.expectsInstanceOf(ProtocolConfiguration.class))
                .does(new MethodAction() {

                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        configuration = (ProtocolConfiguration)
                                event.getArgument(Object.class);
                        return null;
                    }
                });

        ProtocolBuilder builder = new ProtocolBuilder();
        protocol = (WMLRoot) builder.build(
                new TestProtocolRegistry.TestWMLRootFactory(), deviceMock);
//        protocol.setMarinerPageContext(context);
        transformer = new WMLDOMTransformer(protocol, true);
    }

    protected void tearDown() throws Exception {
        element = null;
        protocol = null;
//        context = null;
        transformer = null;
    }

    protected Element createTableCell(String content,
                                      boolean useParagraph,
                                      boolean useNestedParagraph,
                                      int childCount) {

        if(content==null) {
            content = "cell";
        }

        Element td = domFactory.createElement();
        td.setName("td");
        for(int i=0; i<childCount; i++) {
            Element parent = td;
            if(useParagraph) {
                Element p = domFactory.createElement();
                p.setName("BLOCK");
                parent.addTail(p);
                parent = p;
            }
            if(useNestedParagraph) {
                Element p = domFactory.createElement();
                p.setName("BLOCK");
                parent.addTail(p);
                parent = p;
            }
            Text text = domFactory.createText();
            text.append(content);
            parent.addTail(text);
        }

        return td;
    }

    protected Element createRow(Element cells[]) {
        Element tr = domFactory.createElement();
        tr.setName("tr");
        for(int i=0; i<cells.length; i++) {
            tr.addTail(cells[i]);
        }

        return tr;
    }

    protected Element createTable(Element rows []) {
        Element table = domFactory.createElement();
        table.setName("table");
        for(int i=0; i<rows.length; i++) {
            table.addTail(rows[i]);
        }

        return table;
    }

    protected Element createTable(int rows, int[] columns) {
        Element table = domFactory.createElement();
        table.setName("table");
        int maxColumns = 0;
        int col = 0;
        for(int row = 0; row < rows; row++) {
            Element tr = domFactory.createElement();
            tr.setName("tr");
            table.addTail(tr);
            col = 0;
            for(; col < columns[row]; col++) {
                Element td = domFactory.createElement();
                td.setName("td");
                Element p = domFactory.createElement();
                p.setName("BLOCK");
                td.addTail(p);
                Text text = domFactory.createText();
                text.append("r");
                text.append(new Integer(row).toString());
                text.append(", c");
                text.append(new Integer(col).toString());
                p.addTail(text);
                tr.addTail(td);
            }
            if(col > maxColumns) {
                maxColumns = col;
            }
        }
        return table;
    }

    /**
     * Test that fixTable in transformElement() works with other
     * transformations.
     */
    public void testTransformElementFixTableFromParagraph() throws Exception {
        Element p = domFactory.createElement();
        p.setName(WMLConstants.BLOCH_ELEMENT);
        int[] columns = {1, 1};
        Element table = createTable(2, columns);
        p.addTail(table);

        Document doc = domFactory.createDocument();
        doc.addNode(p);        
        transformer.transformElement(p);
        String expected =
                "<BLOCK>r0, c0</BLOCK>" +
                "<BLOCK>r1, c0</BLOCK>";
        assertEquals(expected, DOMUtilities.toString(doc));
    }

    /**
     * Test that fixTable in transformElement() works with other
     * transformations.
     */
    public void testTransformElementFromWml() throws Exception {
        Element wml = domFactory.createElement();
        wml.setName("wml");
        Element card = domFactory.createElement();
        card.setName("card");
        wml.addTail(card);

        Element td []= new Element[1];
        td[0] = createTableCell("cell", false, false, 1);
        Element tr [] = new Element[1];
        tr[0] = createRow(td);
        Element table = createTable(tr);
        card.addTail(table);

        Document doc = domFactory.createDocument();
        doc.addNode(wml);

        transformer.transformElement(wml);

        String expected =
                "<wml>" +
                    "<card>" +
                        "<BLOCK>cell</BLOCK>" +
                    "</card>" +
                "</wml>";
        assertEquals(expected, DOMUtilities.toString(doc));
    }

    /**
     * Test that fixTable in transformElement() works with other
     * transformations. In this case a 1 column table containing a paragraph
     * in one row and a 3 column table in another whose rows are paragraphed
     * should be transformed to remove the outer table but keep the inner
     * table and move the paragraphs in the rows out to be a single paragraph
     * surrounding the inner table.
     */
    public void testTransformElementNestedTablesFromWml1() throws Exception {
        Element wml = domFactory.createElement();
        wml.setName("wml");
        Element card = domFactory.createElement();
        card.setName("card");
        wml.addTail(card);

        Element td [] = new Element[1];
        td[0] = createTableCell("cell", false, false, 1);
        Element tr [] = new Element[2];
        tr[0] = createRow(td);
        td = new Element[1];
        int cols [] = {2, 3, 1};
        td[0] = domFactory.createElement("td");
        td[0].addTail(createTable(3, cols));
        tr[1] = createRow(td);
        Element table = createTable(tr);
        card.addTail(table);

        Document doc = domFactory.createDocument();
        doc.addNode(wml);
        transformer.transformElement(wml);

        String expected =
                "<wml>" +
                "<card>" +
                "<BLOCK>cell</BLOCK>" +
                "<BLOCK>" +
                "<table columns=\"3\">" +
                "<tr>" +
                "<td>r0, c0</td>" +
                "<td>r0, c1</td>" +
                "</tr>" +
                "<tr>" +
                "<td>" +
                "r1, c0" +
                "</td>" +
                "<td>" +
                "r1, c1" +
                "</td>" +
                "<td>" +
                "r1, c2" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>r2, c0</td>" +
                "</tr>" +
                "</table>" +
                "</BLOCK>" +
                "</card>" +
                "</wml>";

        assertEquals(expected, DOMUtilities.toString(doc));
    }

    /**
     * Test that fixTable in transformElement() works with other
     * transformations. In this case a 1 column table containing a paragraph
     * in one row and a 1 column table in another whose rows are paragraphed
     * should be transformed to remove both tables.
     */
    public void testTransformElementNestedTablesFromWml2() throws Exception {
        Element wml = domFactory.createElement();
        wml.setName("wml");
        Element card = domFactory.createElement();
        card.setName("card");
        wml.addTail(card);

        Element td [] = new Element[1];
        td[0] = createTableCell("cell", false, false, 1);
        Element tr [] = new Element[2];
        tr[0] = createRow(td);
        td = new Element[1];
        int cols [] = {1, 1};
        td[0] = domFactory.createElement("td");
        td[0].addTail(createTable(2, cols));
        tr[1] = createRow(td);
        Element table = createTable(tr);
        card.addTail(table);

        Document doc = domFactory.createDocument();
        doc.addNode(wml);
        transformer.transformElement(wml);

        String expected =
                "<wml>" +
                "<card>" +
                "<BLOCK>cell</BLOCK>" +
                "<BLOCK>r0, c0</BLOCK>" +
                "<BLOCK>r1, c0</BLOCK>" +
                "</card>" +
                "</wml>";

        assertEquals(expected, DOMUtilities.toString(doc));
    }

    /**
     * Test that fixTable in transformElement() works with other
     * transformations. In this case a 1 column table containing a paragraph
     * in one row and a 1 column table in another whose rows are paragraphed
     * should be transformed to remove both tables. This is the same as
     * testTransformElementNestedTablesFromWml2 except the outer table
     * first row contains three nested paragraphs.
     */
    public void testTransformElementNestedTablesFromWml3() throws Exception {
        Element wml = domFactory.createElement();
        wml.setName("wml");
        Element card = domFactory.createElement();
        card.setName("card");
        wml.addTail(card);

        Element td [] = new Element[1];
        td[0] = createTableCell("cell", true, true, 3);
        Element tr [] = new Element[2];
        tr[0] = createRow(td);
        td = new Element[1];
        int cols [] = {1, 1};
        td[0] = domFactory.createElement("td");
        td[0].addTail(createTable(2, cols));
        tr[1] = createRow(td);
        Element table = createTable(tr);
        card.addTail(table);

        Document doc = domFactory.createDocument();
        doc.addNode(wml);
        transformer.transformElement(wml);

        String expected =
                "<wml>" +
                "<card>" +
                "<BLOCK>cell</BLOCK>" +
                "<BLOCK>cell</BLOCK>" +
                "<BLOCK>cell</BLOCK>" +
                "<BLOCK>r0, c0</BLOCK>" +
                "<BLOCK>r1, c0</BLOCK>" +
                "</card>" +
                "</wml>";
        assertEquals(expected, DOMUtilities.toString(doc));
    }

    /**
     * Test that fixTable in transformElement() works with other
     * transformations. In this case a 2 column table containing a paragraph
     * in one row and a 1 column table in another whose rows are paragraphed
     * should be transformed to remove the inner table.
     */
    public void testTransformElementNestedTablesFromWml4() throws Exception {
        Element wml = domFactory.createElement();
        wml.setName("wml");
        Element card = domFactory.createElement();
        card.setName("card");
        wml.addTail(card);

        Element td [] = new Element[2];
        td[0] = createTableCell("cell", false, false, 1);
        td[1] = createTableCell("cell", true, false, 2);
        Element tr [] = new Element[2];
        tr[0] = createRow(td);
        td = new Element[1];
        int cols [] = {1, 1};
        td[0] = domFactory.createElement("td");
        td[0].addTail(createTable(2, cols));
        tr[1] = createRow(td);
        Element table = createTable(tr);
        card.addTail(table);

        Document doc = domFactory.createDocument();
        doc.addNode(wml);
        transformer.transformElement(wml);

        String expected =
                "<wml>" +
                "<card>" +
                "<BLOCK>" +
                "<table columns=\"2\">" +
                "<tr>" +
                "<td>cell</td>" +
                "<td>cell<br/>cell</td>" +
                "</tr>" +
                "<tr>" +
                "<td>r0, c0<br/>r1, c0</td>" +
                "</tr>" +
                "</table>" +
                "</BLOCK>" +
                "</card>" +
                "</wml>";
        assertEquals(expected, DOMUtilities.toString(doc));
    }

    /**
     * Test that a paragraph containing a 2 column table with no
     * nesting and no column attribute does not change apart from the
     * addition of the correct column attribute.
     */
    public void testFixGridTable2ColumnTable() throws Exception {
        Element p = domFactory.createElement();
        p.setName("BLOCK");
        int[] columns = {2, 2};
        Element table = createTable(2, columns);
        p.addTail(table);

        // the expected table should contain the column attribute
        table.setAttribute("columns", "2");
        Document doc = domFactory.createDocument();
        doc.addNode(p);
        String expected = DOMUtilities.toString(doc);

        // the table to fix should not...
        table.removeAttribute("columns");

        transformer.fixTable(table);

        assertEquals(expected, DOMUtilities.toString(doc));
    }

     /**
     * Test that a paragraph containing a 1 column table with that has
     * a columns attribute value of 2 has the table removed.
     */
    public void testFixGridTable1ColumnTable() throws Exception {
        Element p = domFactory.createElement();
        p.setName("BLOCK");
        int[] columns = {1, 1};
        Element table = createTable(2, columns);
        p.addTail(table);

        Document doc = domFactory.createDocument();
        doc.addNode(p);

        transformer.fixTable(table);

         String expected =
                 "<BLOCK>r0, c0</BLOCK>" +
                 "<BLOCK>r1, c0</BLOCK>";
         assertEquals(expected, DOMUtilities.toString(doc));
     }

    /**
     * Test that a paragraph containing a 3 column table with an incorrect
     * column attribute value has the correct column attribute after processing,
     * but is otherwise unchanged.
     */
    public void testFixGridTableBadColumnAttTable() throws Exception {
        Element p = domFactory.createElement();
        p.setName("BLOCK");
        int[] columns = {2, 3};
        Element table = createTable(2, columns);
        p.addTail(table);

        // the expected table should contain the correct value for 
        // the column attribute
        table.setAttribute("columns", "3");
        Document doc = domFactory.createDocument();
        doc.addNode(p);
        String expected = DOMUtilities.toString(doc);

        // add the duff column attribute value in the table to be fixed
        table.setAttribute("columns", "200");

        transformer.fixTable(table);

        assertEquals(expected, DOMUtilities.toString(doc));
    }

    /**
     * Test case for VBM:2002111508
     * com.volantis.mcs.dom.ElementTestCase exercises the promotion of nodes
     * far more thoroughly.
     * @throws Exception
     */
    public void testTransformRemoveExtraneousPTags() throws Exception
    {
        String original =
          "<wml>"+
            "<card newcontext=\"true\">"+
              "<BLOCK>"+
                "<BLOCK>"+
                  "<BLOCK> "+ // The space after the <BLOCK> tag is intentional. It's the
                          // sole reason for this test.
                    "<BLOCK>formPane2</BLOCK>"+
                    "<do label=\"SubmitIt\" name=\"submitbutton1\" type=\"accept\">"+
                      "<go href=\"tt_xfactionsubmit.jsp\" method=\"get\">" +
                         "<postfield name=\"vform\" value=\"s0\"/>"+
                         "<postfield name=\"submitbutton1\" value=\"SubmitIt\"/>"+
                      "</go>"+
                    "</do>"+
                    "</BLOCK>"+
                  "</BLOCK>"+
                "</BLOCK>"+
             "</card>"+
          "</wml>";
          
        String expected =
          "<wml>"+
            "<card newcontext=\"true\">"+
               "<p>" +
                 "formPane2"+
                 "<br/>"+
                 "<do label=\"SubmitIt\" name=\"submitbutton1\" type=\"accept\">"+
                    "<go href=\"tt_xfactionsubmit.jsp\" method=\"get\">" +
                       "<postfield name=\"vform\" value=\"s0\"/>"+
                       "<postfield name=\"submitbutton1\" value=\"SubmitIt\"/>"+
                    "</go>"+
                 "</do>"+
               "</p>"+               
             "</card>"+
          "</wml>";
        
        checkTransformation(original, expected, true);
    }

    /**
     * Ensure that when a DOM is transformed, any paragraphs which contain
     * non P elements are kept, even if their text consists of all whitespace.
     * <p>
     * This test exposed the problems outlined in VBM:2003012302. 
     */ 
    public void testTransformKeepParagraphsWithContainedElements() 
            throws Exception {
        String original =
          "<wml>" +
            "<card>"+
              "<BLOCK>" +
                "<BLOCK>" +
                  "<kept-tag/>" +
                "</BLOCK>" +
              "</BLOCK>" +
            "</card>"+
          "</wml>";

        String expected =
          "<wml>" +
            "<card>"+
              "<p>" +
                "<kept-tag/>" +
              "</p>" +
            "</card>"+
          "</wml>";
        
        checkTransformation(original, expected, true);
    }

    /**
     * Ensure that when a DOM is transformed, any paragraphs which contain
     * non-whitespace text are kept.
     */ 
    public void testTransformKeepParagraphsWithContainedText() 
            throws Exception {
        String original =
          "<wml>" +
            "<card>"+
              "<BLOCK>" +
                "<BLOCK>" +
                  "kept text" +
                "</BLOCK>" +
              "</BLOCK>" +
            "</card>"+
          "</wml>";

        String expected =
          "<wml>" +
            "<card>"+
              "<p>" +
                "kept text" +
              "</p>" +
            "</card>"+
          "</wml>";
        
        checkTransformation(original, expected, true);
    }

    /**
     * Ensure that when a DOM is transformed, any paragraphs which contain
     * only whitespace provided by the user is kept.
     */ 
    public void testTransformKeepParagraphsWithUserWhitespace() 
            throws Exception {
        String original =
          "<wml>" +
            "<card>"+
              "<BLOCK>" +
                "<BLOCK>" +
                  " " +
                "</BLOCK>" +
              "</BLOCK>" +
            "</card>"+
          "</wml>";

        String expected =
          "<wml>" +
            "<card>"+
              "<p>" +
                " " +
              "</p>" +
            "</card>"+
          "</wml>";
        
        checkTransformation(original, expected, true);
    }

    /**
     * Ensure that when a DOM is transformed, any paragraphs which are created
     * during promotion and contain only whitespace are trimmed.
     * 
     * This test exposed the problems outlined in VBM:2003012227.
     */ 
    public void testTransformTrimParagraphsResultingFromPromotion() 
            throws Exception {
        String original =
          "<wml>" +
            "<card>"+
              "<BLOCK>" +
                " " +  // this ends up in P we created, by itself - trim!
                "<BLOCK>" +
                  "some content" + 
                "</BLOCK>" +
                " " +  // this ends up in P we created, by itself - trim!
              "</BLOCK>" +
            "</card>"+
          "</wml>";

        String expected =
          "<wml>" +
            "<card>"+
              "<p>" +
                "some content" +
              "</p>" +
            "</card>"+
          "</wml>";
        
        checkTransformation(original, expected, true);
    }

    /**
     * Ensure that when a DOM is transformed, elements that should be emulated
     * are replaced with the correct values (defined in
     * populateEmulateEmphasisTags).
     *
     * @throws java.io.IOException thrown if DOM cannot be converted to a String
     */
    public void testTransformElementWithEmulatedElements() throws IOException {

        deviceMock = new InternalDeviceMock("deviceMock", expectations);

        populateEmulateEmphasisTags();

        initializeTransformer();
        Document orig = domFactory.createDocument();
        Element wml = generateTestWML();
        orig.addNode(wml);

        transformer.transformElement(wml);

        String expected =
                "<wml>" +
                    "<card title=\"the test title\">" +
                        "<BLOCK>" +
                            "<div>TITLEthe test titleTITLE</div>" +
                        "</BLOCK>" +
                        "<BLOCK align=\"center\">" +
                            "<div>**<a href=\"wtai://wp/mc;20405050606\">20405050606</a>**</div>" +
                            "<div>**<anchor href=\"wtai://wp/mc;20405050606\">20405050606</anchor>**</div>" +
                            "<div>BIGThis is big text.BIG</div>" +
                            "<div>BOLDThis is b text.BOLD</div>" +
                            "<div>EMThis is em text.EM</div>" +
                            "<div>ITALICThis is i text.ITALIC</div>" +
                            "<div>SMALLThis is small text.SMALL</div>" +
                            "<div>STRONGThis is strong text.STRONG</div>" +
                            "<div>_This is u text._</div>" +
                        "</BLOCK>" +
                    "</card>" +
                "</wml>";

        assertEquals(expected, DOMUtilities.toString(orig));

        // And again.
        deviceMock = new InternalDeviceMock("deviceMock", expectations);

        // test that prefixes on their own are correctly rendered
        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_BOLD_TAG, true,
                "BOLD", "BOLD", null);

        // test that an altTag on its own is correctly rendered
        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_EMPHASIZE_TAG, true,
                null, null, "div");

        // test that link highlighting without an alternate tag works correctly
        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_LINK_HIGHLIGHTING,
                true, "**", "**", null);

        // special case - test that card titles with prefixes but no altTag
        // are correctly rendered
        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_CARD_TITLE, true,
                "TITLE", "TITLE", null);

        populateEmulateEmphasisTags();

        initializeTransformer();

        orig = domFactory.createDocument();
        wml = generateTestWML();
        orig.addNode(wml);

        transformer.transformElement(wml);

        expected =
                "<wml>" +
                    "<card title=\"the test title\">" +
                        "<BLOCK>TITLEthe test titleTITLE</BLOCK>" +
                        "<BLOCK align=\"center\">" +
                            "**<a href=\"wtai://wp/mc;20405050606\">20405050606</a>**" +
                            "**<anchor href=\"wtai://wp/mc;20405050606\">20405050606</anchor>**" +
                            "<div>BIGThis is big text.BIG</div>" +
                            "BOLDThis is b text.BOLD" +
                            "<div>This is em text.</div>" +
                            "<div>ITALICThis is i text.ITALIC</div>" +
                            "<div>SMALLThis is small text.SMALL</div>" +
                            "<div>STRONGThis is strong text.STRONG</div>" +
                            "<div>_This is u text._</div>" +
                        "</BLOCK>" +
                    "</card>" +
                "</wml>";

        assertEquals(expected, DOMUtilities.toString(orig));

        // And again.
        deviceMock = new InternalDeviceMock("deviceMock", expectations);

        // test that prefixes on their own are correctly rendered
        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_BOLD_TAG, true,
                "BOLD", "BOLD", null);

        // test that an altTag on its own is correctly rendered
        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_EMPHASIZE_TAG, true,
                null, null, "div");

        // test that link highlighting without an alternate tag works correctly
        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_LINK_HIGHLIGHTING,
                true, "**", "**", null);

        // special case - test that card titles with an altTag but no prefixes
        // is correctly rendered
        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_CARD_TITLE, true,
                null, null, "div");

        populateEmulateEmphasisTags();

        initializeTransformer();

        orig = domFactory.createDocument();
        wml = generateTestWML();
        orig.addNode(wml);

        transformer.transformElement(wml);

        expected =
                "<wml>" +
                    "<card title=\"the test title\">" +
                        "<BLOCK>" +
                            "<div>the test title</div>" +
                        "</BLOCK>" +
                        "<BLOCK align=\"center\">" +
                            "**<a href=\"wtai://wp/mc;20405050606\">20405050606</a>**" +
                            "**<anchor href=\"wtai://wp/mc;20405050606\">20405050606</anchor>**" +
                            "<div>BIGThis is big text.BIG</div>" +
                            "BOLDThis is b text.BOLD" +
                            "<div>This is em text.</div>" +
                            "<div>ITALICThis is i text.ITALIC</div>" +
                            "<div>SMALLThis is small text.SMALL</div>" +
                            "<div>STRONGThis is strong text.STRONG</div>" +
                            "<div>_This is u text._</div>" +
                        "</BLOCK>" +
                    "</card>" +
                "</wml>";

        assertEquals(expected, DOMUtilities.toString(orig));
    }

    /**
     * Populates the test framework with emulation values for all the elements
     * that are allowed to be emulated.
     */
    private void populateEmulateEmphasisTags() {

        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_CARD_TITLE, true,
                "TITLE", "TITLE", "div");

        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_LINK_HIGHLIGHTING,
                true, "**", "**", "div");

        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_BIG_TAG, true, "BIG",
                "BIG", "div");

        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_BOLD_TAG, true,
                "BOLD", "BOLD", "div");

        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_EMPHASIZE_TAG, true,
                "EM", "EM", "div");

        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_ITALIC_TAG, true,
                "ITALIC", "ITALIC", "div");

        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_SMALL_TAG, true,
                "SMALL", "SMALL", "div");

        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_STRONG_TAG, true,
                "STRONG", "STRONG", "div");

        expectEmphasisTag(DevicePolicyConstants.EMULATE_WML_UNDERLINE_TAG, true,
                "_", "_", "div");
    }

    private void expectEmphasisTag(
            String policy, boolean enable, String prefix, String suffix,
            String altTag) {
        deviceMock.expects.getBooleanPolicyValue(policy + ".enable")
                .returns( enable).any();
        deviceMock.expects.getPolicyValue(policy + ".prefix")
                .returns(prefix).any();
        deviceMock.expects.getPolicyValue(policy + ".suffix")
                .returns(suffix).any();
        deviceMock.expects.getPolicyValue(policy + ".altTag")
                .returns(altTag).any();
    }

    /**
     * Utility method which generates the element structure for use in
     * #testTransformElementWithEmulatedElements.
     *
     * @return the root element for use in tests
     */
    private Element generateTestWML() {
        Element wml = domFactory.createElement();
        wml.setName("wml");
        Element card = domFactory.createElement();
        card.setName("card");
        card.setAttribute("title", "the test title");

        Element p = domFactory.createElement();
        p.setName("BLOCK");
        p.setAttribute("align", "center");
        card.addTail(p);

        Element a = domFactory.createElement();
        a.setName("a");
        a.setAttribute("href", "wtai://wp/mc;20405050606");
        Text t = domFactory.createText();
        t.append("20405050606");
        a.addHead(t);
        p.addTail(a);

        Element anchor = domFactory.createElement();
        anchor.setName("anchor");
        anchor.setAttribute("href", "wtai://wp/mc;20405050606");
        anchor.addHead(t);
        p.addTail(anchor);

        String[] tags = {"big", "b", "em", "i", "small", "strong", "u"};

        for (int i = 0; i < tags.length; i++) {
            Element tag = domFactory.createElement();
            tag.setName(tags[i]);
            Text text = domFactory.createText();
            text.append("This is " + tags[i] + " text.");
            tag.addHead(text);
            p.addTail(tag);
        }

        wml.addHead(card);
        return wml;
    }

    /**
     * Test that dividehints are converted in keeptogethers correctly 
     * @throws Exception
     */
    public void testConvertDivideHintIntoKeepTogether() throws Exception {
        // NOTE: we have renamed dissection elements to be legal.
        // TODO: update test case to use normal XML input now?
        /* Create following DOM. We cannot use checkTransformation() due to
         * the special DISSECTABLE-CONTENTS and DIVIDE-HINT nodes.
         * <wml>
         *   <card>
         *     <DISSECTABLE-CONTENTS>
         *       <DIVIDE-HINT/>
         *       <BLOCK>some content</BLOCK>
         *     </DISSECTABLE-CONTENTS>
         *   </card>
         * <wml>
         */

        Document document = domFactory.createDocument();
        Element wml = domFactory.createElement();
        Element card = domFactory.createElement();
        Element dissectable = domFactory.createElement();
        Element dividehint = domFactory.createElement();
        Element p = domFactory.createElement();
        Text text = domFactory.createText();
        
        wml.setName("wml");
        card.setName("card");
        dissectable.setName(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
        dividehint.setName(DissectionConstants.DIVIDE_HINT_ELEMENT);
        p.setName("BLOCK");
        text.append("some content");
        
        document.addNode(wml);
        wml.addTail(card);
        card.addTail(dissectable);        
        dissectable.addTail(dividehint);
        dissectable.addTail(p);
        p.addTail(text);
        
        String expected =
            "<wml>" +
              "<card>"+
                "<"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
                  "<"+DissectionConstants.KEEPTOGETHER_ELEMENT+">" +
                    "<p>" +
                      "some content" +
                    "</p>" +
                  "</"+DissectionConstants.KEEPTOGETHER_ELEMENT+">" +
                "</"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
              "</card>"+
            "</wml>";
                            
        // Transform dom from original so we can check against expected
        Document transformed = transformer.transform(protocol, document);
        String domAsString = DOMUtilities.toString(transformed);
        assertEquals("Conversion dividehint to keeptogether failed",
                     expected, domAsString);
    }

    /**
     * Test that dividehints are converted in keeptogethers correctly 
     * @throws Exception
     */
    public void testConvertDivideHintIntoKeepTogetherZeroLength()
                throws Exception {
        // NOTE: we have renamed dissection elements to be legal.
        // TODO: update test case to use normal XML input now?
        /* Create following DOM. We cannot use checkTransformation() due to
         * the special DISSECTABLE-CONTENTS and DIVIDE-HINT nodes.
         * <wml>
         *   <card>
         *     <BLOCK>
         *       <DISSECTABLE-CONTENTS>
         *         <DIVIDE-HINT/><!--There is a zero length piece of text-->
         *                         <!--here. This is how MCS creates its DOM-->
         *         <BLOCK>some content</BLOCK>
         *       </DISSECTABLE-CONTENTS>
         *     </BLOCK>
         *   </card>
         * <wml>
         */

        Document document = domFactory.createDocument();
        Element wml = domFactory.createElement();
        Element card = domFactory.createElement();
        Element p1 = domFactory.createElement();
        Element dissectable = domFactory.createElement();
        Element dividehint = domFactory.createElement();
        Text zero = domFactory.createText();
        Element p2 = domFactory.createElement();
        Text text = domFactory.createText();
        
        wml.setName("wml");
        card.setName("card");
        p1.setName("BLOCK");
        dissectable.setName(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
        dividehint.setName(DissectionConstants.DIVIDE_HINT_ELEMENT);
        zero.append("");
        p2.setName("BLOCK");
        text.append("some content");
        
        document.addNode(wml);
        wml.addTail(card);
        card.addTail(p1);
        p1.addTail(dissectable);        
        dissectable.addTail(dividehint);
        dissectable.addTail(zero);
        dissectable.addTail(p2);
        p2.addTail(text);
        
        String expected =
            "<wml>" +
              "<card>"+
                "<"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
                  "<"+DissectionConstants.KEEPTOGETHER_ELEMENT+">" +
                    "<p>" +
                      "some content" + 
                    "</p>" +
                  "</"+DissectionConstants.KEEPTOGETHER_ELEMENT+">" +
                "</"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
              "</card>"+
            "</wml>";
                            
        // Transform dom from original so we can check against expected
        Document transformed = transformer.transform(protocol, document);
        String domAsString = DOMUtilities.toString(transformed);
        assertEquals("Conversion dividehint to keeptogether failed",
                     expected, domAsString);
    }

    /**
     * Test that dividehints are converted in keeptogethers correctly 
     * @throws Exception
     */
    public void testConvertDivideHintIntoKeepTogetherNoNextElement()
                throws Exception {
        // NOTE: we have renamed dissection elements to be legal.
        // TODO: update test case to use normal XML input now?
        /* Create following DOM. We cannot use checkTransformation() due to
         * the special DISSECTABLE-CONTENTS and DIVIDE-HINT nodes.
         * <wml>
         *   <card>
         *     <BLOCK>
         *       <DISSECTABLE-CONTENTS>
         *         <BLOCK>some content</BLOCK>
         *         <DIVIDE-HINT/><!--There is a zero length piece of text-->
         *                         <!--here. This is how MCS creates its DOM-->                          
         *       </DISSECTABLE-CONTENTS>
         *     </BLOCK>
         *   </card>
         * <wml>
         * As there isn't a node following the DIVIDE-HINT we omit it.
         */

        Document document = domFactory.createDocument();
        Element wml = domFactory.createElement();
        Element card = domFactory.createElement();
        Element p1 = domFactory.createElement();
        Element dissectable = domFactory.createElement();
        Element p2 = domFactory.createElement();
        Text text = domFactory.createText();
        Element dividehint = domFactory.createElement();
        Text zero = domFactory.createText();
        
        wml.setName("wml");
        card.setName("card");
        p1.setName("BLOCK");
        dissectable.setName(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
        p2.setName("BLOCK");
        text.append("some content");
        dividehint.setName(DissectionConstants.DIVIDE_HINT_ELEMENT);
        zero.append("");
        
        document.addNode(wml);
        wml.addTail(card);
        card.addTail(p1);
        p1.addTail(dissectable);        
        dissectable.addTail(p2);
        p2.addTail(text);        
        dissectable.addTail(dividehint);
        dissectable.addTail(zero);        
        
        String expected =
            "<wml>" +
              "<card>"+
                "<"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
                  "<p>" +
                     "some content" + 
                  "</p>" +                  
                "</"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
              "</card>"+
            "</wml>";
                            
        // Transform dom from original so we can check against expected
        Document transformed = transformer.transform(protocol, document);
        String domAsString = DOMUtilities.toString(transformed);
        assertEquals("Conversion dividehint to keeptogether failed",
                     expected, domAsString);
    }

    /**
     * Test that dividehints are converted in keeptogethers correctly 
     * @throws Exception
     */
    public void testConvertDivideHintIntoKeepTogetherMultipleDivideHints()
                throws Exception {
        // NOTE: we have renamed dissection elements to be legal.
        // TODO: update test case to use normal XML input now?
        /* Create following DOM. We cannot use checkTransformation() due to
         * the special DISSECTABLE-CONTENTS and DIVIDE-HINT nodes.
         * <wml>
         *   <card>
         *     <BLOCK>
         *       <DISSECTABLE-CONTENTS>
         *         <DIVIDE-HINT/><!--There is a zero length piece of text-->
         *                         <!--here. This is how MCS creates its DOM-->       
         *         <DIVIDE-HINT/><!--There is a zero length piece of text-->
         *                         <!--here. This is how MCS creates its DOM-->
         *         <BLOCK>some content</BLOCK>
         *       </DISSECTABLE-CONTENTS>
         *     </BLOCK>
         *   </card>
         * <wml>
         */

        Document document = domFactory.createDocument();
        Element wml = domFactory.createElement();
        Element card = domFactory.createElement();
        Element p1 = domFactory.createElement();
        Element dissectable = domFactory.createElement();
        Element dividehint1 = domFactory.createElement();
        Text zero1 = domFactory.createText();
        Element dividehint2 = domFactory.createElement();
        Text zero2 = domFactory.createText();
        Element p2 = domFactory.createElement();
        Text text = domFactory.createText();
        
        wml.setName("wml");
        card.setName("card");
        p1.setName("BLOCK");
        dissectable.setName(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
        dividehint1.setName(DissectionConstants.DIVIDE_HINT_ELEMENT);
        zero1.append("");
        dividehint2.setName(DissectionConstants.DIVIDE_HINT_ELEMENT);
        zero2.append("");
        p2.setName("BLOCK");
        text.append("some content");
        
        document.addNode(wml);
        wml.addTail(card);
        card.addTail(p1);
        p1.addTail(dissectable);        
        dissectable.addTail(dividehint1);
        dissectable.addTail(zero1);        
        dissectable.addTail(dividehint2);
        dissectable.addTail(zero2);        
        dissectable.addTail(p2);
        p2.addTail(text);        
        
        String expected =
            "<wml>" +
              "<card>"+
                "<"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
                  "<"+DissectionConstants.KEEPTOGETHER_ELEMENT+">" +
                    "<p>" +
                      "some content" + 
                    "</p>" +
                  "</"+DissectionConstants.KEEPTOGETHER_ELEMENT+">" +
                "</"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
              "</card>"+
            "</wml>";
                            
        // Transform dom from original so we can check against expected
        Document transformed = transformer.transform(protocol, document);
        String domAsString = DOMUtilities.toString(transformed);
        assertEquals("Conversion dividehint to keeptogether failed",
                     expected, domAsString);
    }

    /**
     * Test the promotion of the keeptogether special element
     * @throws Exception
     */
    public void testPromoteKeepTogether() throws Exception {
        // NOTE: we have renamed dissection elements to be legal.
        // TODO: update test case to use normal XML input now?
        /* Create following DOM. We cannot use checkTransformation() due to
         * the special DISSECTABLE-CONTENTS and 'KEEP TOGETHER' nodes.
         * <wml>
         *   <card>
         *     <BLOCK>
         *       <DISSECTABLE-CONTENTS>
         *         <BLOCK>
         *           <'KEEP TOGETHER'>
         *             <BLOCK>
         *               <'KEEP TOGETHER'>
         *                 <BLOCK>some content</BLOCK>
         *               </'KEEP TOGETHER'>
         *             </BLOCK>
         *           </'KEEP TOGETHER'>
         *         </BLOCK>
         *       </DISSECTABLE-CONTENTS>
         *     </BLOCK>
         *   </card>
         * <wml>
         */

        Document document = domFactory.createDocument();
        Element wml = domFactory.createElement();
        Element card = domFactory.createElement();
        Element p1 = domFactory.createElement();
        Element dissectable = domFactory.createElement();
        Element p2 = domFactory.createElement();
        Element keeptogether1 = domFactory.createElement();
        Element p3 = domFactory.createElement();
        Element keeptogether2 = domFactory.createElement();
        Element p4 = domFactory.createElement();
        Text text = domFactory.createText();
        
        wml.setName("wml");
        card.setName("card");
        p1.setName("BLOCK");
        dissectable.setName(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
        p2.setName("BLOCK");
        keeptogether1.setName(DissectionConstants.KEEPTOGETHER_ELEMENT);
        p3.setName("BLOCK");
        keeptogether2.setName(DissectionConstants.DIVIDE_HINT_ELEMENT);
        p4.setName("BLOCK");
        text.append("some content");
        
        document.addNode(wml);
        wml.addTail(card);
        card.addTail(p1);
        p1.addTail(dissectable);
        dissectable.addTail(p2);
        p2.addTail(keeptogether1);
        keeptogether1.addTail(p3);        
        p3.addTail(keeptogether2);
        keeptogether2.addTail(p4);        
        p4.addTail(text);        
        
        String expected =
            "<wml>" +
              "<card>"+
                "<"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
                  "<"+DissectionConstants.KEEPTOGETHER_ELEMENT+">" +
                    "<"+DissectionConstants.KEEPTOGETHER_ELEMENT+">" +
                      "<p>" +
                        "some content" + 
                      "</p>" +
                     "</"+DissectionConstants.KEEPTOGETHER_ELEMENT+">" +
                  "</"+DissectionConstants.KEEPTOGETHER_ELEMENT+">" +
                "</"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
              "</card>"+
            "</wml>";
                            
        // Transform dom from original so we can check against expected
        Document transformed = transformer.transform(protocol, document);
        String domAsString = DOMUtilities.toString(transformed);
        assertEquals("Promotion of keeptogether failed",
                     expected, domAsString);        
    }
    /**
     * Test the promotion of the keeptogether special element
     * @throws Exception
     */
    public void testTransformShardLinkGroup() throws Exception {
        // NOTE: we have renamed dissection elements to be legal.
        // TODO: update test case to use normal XML input now?
        /* Create following DOM. We cannot use checkTransformation() due to
         * the special DISSECTABLE-CONTENTS and SHARD-LINK-GROUP nodes.
         * <wml>
         *   <card>
         *     <BLOCK>
         *       <DISSECTABLE-CONTENTS>
         *         <BLOCK>some content</BLOCK>
         *       </DISSECTABLE-CONTENTS>
         *       <SHARD-LINK-GROUP>
         *         <BLOCK mode="wrap">
         *           <SHARD-LINK>
         *             <a href="url1">link1</a>
         *           </SHARD-LINK>
         *           <SHARD-LINK-CONDITIONAL>
         *             <br/>
         *           </SHARD-LINK-CONDITIONAL>
         *           <SHARD-LINK>
         *             <a href="url2">link2</a>
         *           </SHARD-LINK>
         *         </BLOCK>
         *       </SHARD-LINK-GROUP>
         *     </BLOCK>
         *   </card>
         * <wml>
         */

        Document document = domFactory.createDocument();
        Element wml = domFactory.createElement();
        Element card = domFactory.createElement();
        Element p1 = domFactory.createElement();
        Element dissectable = domFactory.createElement();
        Element p2 = domFactory.createElement();
        Text text = domFactory.createText();
        Element shardLinkGroup = domFactory.createElement();
        Element p3 = domFactory.createElement();
        Element shardLink1 = domFactory.createElement();
        Element a1 = domFactory.createElement();
        Text link1 = domFactory.createText();
        Element shardLinkConditional = domFactory.createElement();
        Element br = domFactory.createElement();
        Element shardLink2 = domFactory.createElement();
        Element a2 = domFactory.createElement();
        Text link2 = domFactory.createText();
                
        wml.setName("wml");
        card.setName("card");
        p1.setName("BLOCK");
        dissectable.setName(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
        p2.setName("BLOCK");
        text.append("some content");
        shardLinkGroup.setName(DissectionConstants.SHARD_LINK_GROUP_ELEMENT);
        p3.setName("BLOCK");
        p3.setAttribute("mode", "wrap");                
        shardLink1.setName(DissectionConstants.SHARD_LINK_ELEMENT);
        a1.setName("a");
        a1.setAttribute("href", "url1");
        a1.addTail(link1);
        link1.append("link1");
        shardLinkConditional.setName(
            DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT);
        br.setName("br");                             
        shardLink2.setName(DissectionConstants.SHARD_LINK_ELEMENT);
        a2.setName("a");
        a2.setAttribute("href", "url2");
        a2.addTail(link2);
        link2.append("link2");
      
        document.addNode(wml);
        wml.addTail(card);
        card.addTail(p1);
        p1.addTail(dissectable);
        dissectable.addTail(p2);
        p2.addTail(text);
        p1.addTail(shardLinkGroup);
        shardLinkGroup.addTail(p3);        
        p3.addTail(shardLink1);
        shardLink1.addTail(a1);
        p3.addTail(shardLinkConditional);
        shardLinkConditional.addTail(br);
        p3.addTail(shardLink2);
        shardLink2.addTail(a2);
        
        String expected =
            "<wml>" +
              "<card>"+
                "<"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
                  "<p>" +
                    "some content" + 
                  "</p>" +
                "</"+DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT+">" +
                "<"+DissectionConstants.SHARD_LINK_GROUP_ELEMENT+">" +
                  "<p mode=\"wrap\">" +
                    "<"+DissectionConstants.SHARD_LINK_ELEMENT+">" +
                      "<a href=\"url1\">link1</a>" +
                    "</"+DissectionConstants.SHARD_LINK_ELEMENT+">" +
                    "<"+DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT+">" +
                      "<br/>" +
                    "</"+DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT+">" +
                    "<"+DissectionConstants.SHARD_LINK_ELEMENT+">" +
                      "<a href=\"url2\">link2</a>" +
                    "</"+DissectionConstants.SHARD_LINK_ELEMENT+">" +
                  "</p>" +
                "</"+DissectionConstants.SHARD_LINK_GROUP_ELEMENT+">" + 
              "</card>"+
            "</wml>";
                            
        // Transform dom from original so we can check against expected
        Document transformed = transformer.transform(protocol, document);
        String domAsString = DOMUtilities.toString(transformed);
        assertEquals("Shard Link Group not on same level as dissecting pane",
                     expected, domAsString);        
    }

    public void testAddBlocks() throws Exception {
        Element card = domFactory.createElement();
        Element p = domFactory.createElement();
        Element timer = domFactory.createElement();
        Element onevent = domFactory.createElement();
        Element pre = domFactory.createElement();
        Element _do = domFactory.createElement();
        Element table = domFactory.createElement();
        boolean result;
        Node child;
        int count = 0;
        String[] expected = {"BLOCK", "timer", "onevent", "do", "BLOCK"};
        List actual = new ArrayList();

        card.setName("card");
        p.setName("BLOCK");
        timer.setName("timer");
        onevent.setName("onevent");
        pre.setName("pre");
        _do.setName("do");
        table.setName("table");

        card.addTail(p);
        card.addTail(timer);
        card.addTail(onevent);
        card.addTail(_do);
        card.addTail(pre);
        card.addTail(table);

        result = transformer.addBlocks(card);

        assertTrue("card should contain paragraphs",
                   result);

        child = card.getHead();
        while (child != null) {
            count++;

            if (child instanceof Element) {
                actual.add(((Element)child).getName());
            }

            child = child.getNext();
        }

        assertEquals("number of card children not as",
                     expected.length,
                     count);

        for (int i = 0;
             i < count;
             i++) {
            assertEquals("card element[" + i + "] not as",
                         expected[i],
                         actual.get(i));
        }
    }

    /**
     * Ensure that when a DOM is transformed, any mutiple column tables that
     * contain form elements are removed (leaving the table content inline)
     * to ensure that the output is valid according to the DTD.
     * <p>
     * Possibly not the cleverest solution, but it prevents the page breaking.
     */
    public void testTransformFormsInMultiColumnTables()
            throws Exception {

        String original =
          "<wml>" +
            "<card>"+
              "<BLOCK>" +
                "<table columns='2'>" +
                  "<tr>" +
                    "<td>" +
                      "<do/>" +
                    "</td>" +
                    "<td>" +
                      "td2" +
                    "</td>" +
                  "</tr>" +
                  "<tr>" +
                    "<td>" +
                      "td3" +
                    "</td>" +
                    "<td>" +
                      "td4" +
                    "</td>" +
                  "</tr>" +
                "</table>" +
              "</BLOCK>" +
            "</card>"+
          "</wml>";

        String expected =
                "<wml>" +
                  "<card>"+
                    "<p>" +
                      "<do/>" +
                      "td2" +
                      "<br/>" +
                      "td3" +
                      "td4" +
                    "</p>" +
                  "</card>"+
                "</wml>";

        checkTransformation(original, expected, true);
    }

    /**
     * Test that style text-align=center and white-space=nowrap on a device
     * that does not honour the align correctly will be transformed correctly,
     * so that the align is honoured.
     *
     * @throws Exception
     */
    public void testBlockWithAlignCentreAndModeNowrapNotHonoured()
            throws Exception {

        String original =
                "<wml>" +
                    "<card>"+
                        "<BLOCK style='white-space:nowrap; text-align:center'>" +
                            "A bit of text" +
                        "</BLOCK>" +
                    "</card>"+
                "</wml>";

        String expected =
            "<wml>" +
              "<card>" +
                "<p align=\"center\">" +
                      "A bit of text" +
                    "</p>" +
              "</card>" +
            "</wml>";

        checkTransformation(original, expected, false);
    }

    /**
     * Test that style text-align=left and white-space=nowrap on a device
     * that does not honour the align correctly will not be altered.
     *
     * @throws Exception
     */
    public void testBlockWithAlignLeftAndModeNowrapNotHonoured()
            throws Exception {

        String original =
                "<wml>" +
                    "<card>"+
                        "<BLOCK style='white-space:nowrap; text-align:left'>" +
                            "A bit of text" +
                        "</BLOCK>" +
                    "</card>"+
                "</wml>";

        String expected =
            "<wml>" +
              "<card>" +
                "<p mode=\"nowrap\">" +
                      "A bit of text" +
                    "</p>" +
              "</card>" +
            "</wml>";

        checkTransformation(original, expected, false);
    }

    /**
     * Test that style text-align=center and white-space=nowrap on a device
     * that does honour the align correctly will not be altered.
     *
     * @throws Exception
     */
    public void testBlockWithAlignCentreAndModeNowrapHonoured()
            throws Exception {

        String original =
                "<wml>" +
                    "<card>"+
                        "<BLOCK style='white-space:nowrap; text-align:center'>" +
                            "A bit of text" +
                        "</BLOCK>" +
                    "</card>"+
                "</wml>";

        String expected =
            "<wml>" +
              "<card>" +
                "<p align=\"center\" mode=\"nowrap\">" +
                      "A bit of text" +
                    "</p>" +
              "</card>" +
            "</wml>";

        checkTransformation(original, expected, true);
    }

    /**
     * Test to see that blocks are added into simple documents when required.
     *
     * @throws Exception
     */
    public void testBlockAddition() throws Exception {

        String original =
                "<wml>" +
                    "<card>"+
                        "simple" +
                    "</card>"+
                "</wml>";

        String expected =
                "<wml>" +
                    "<card>"+
                        "<p>" +
                            "simple" +
                        "</p>" +
                    "</card>"+
                "</wml>";

        checkTransformation(original, expected, true);
    }

    /**
     * Test to see that blocks are translated into paragraphs in dissected
     * documents.
     * <p>
     * In particular, we used to forget to render mode and align on blocks
     * inside dissectable contents, and we used to add an extra useless block
     * into the card when we had only dissectable contents.
     *
     * @throws Exception
     */
    public void testBlockAdditionDissection() throws Exception {

        String original =
                "<wml>" +
                    "<card>"+
                        "<DISSECTABLE-CONTENTS>" +
                            "implicit block" +
                            "<BLOCK style='white-space:nowrap; text-align:right'>" +
                                "explicit block" +
                            "</BLOCK>" +
                            "implicit block" +
                        "</DISSECTABLE-CONTENTS>" +
                    "</card>"+
                "</wml>";

        String expected =
                "<wml>" +
                    "<card>"+
                        "<DISSECTABLE-CONTENTS>" +
                            "<p>" +
                                "implicit block" +
                            "</p>" +
                            "<p mode='nowrap' align='right'>" +
                                "explicit block" +
                            "</p>" +
                            "<p mode='wrap'>" +
                                "implicit block" +
                            "</p>" +
                        "</DISSECTABLE-CONTENTS>" +
                    "</card>"+
                "</wml>";

        checkTransformation(original, expected, true);
    }

    public void testBlockWhiteSpace() throws Exception {
        String original =
                "<wml>" +
                  "<card>"+
                    "<BLOCK style='white-space: normal'>" +
                      "normal" +
                    "</BLOCK>" +
                    "<BLOCK style='white-space: pre'>" +
                      "pre" +
                    "</BLOCK>" +
                    "<BLOCK style='white-space: nowrap'>" +
                      "nowrap" +
                    "</BLOCK>" +
                  "</card>"+
                "</wml>";
        String expected =
                "<wml>" +
                  "<card>"+
                    "<p>" +
                      "normal" +
                    "</p>" +
                    "<p mode='nowrap'>" +
                      "pre" +
                    "<br/>" +
                      "nowrap" +
                    "</p>" +
                  "</card>"+
                "</wml>";

        checkTransformation(original, expected, true);
    }

    public void testBlockTextAlign() throws Exception {
        String original =
                "<wml>" +
                  "<card>"+
                    "<BLOCK style='text-align:left'>" +
                      "left" +
                    "</BLOCK>" +
                    "<BLOCK style='text-align:center'>" +
                      "center" +
                    "</BLOCK>" +
                    "<BLOCK style='text-align:right'>" +
                      "right" +
                    "</BLOCK>" +
                  "</card>"+
                "</wml>";
        String expected =
                "<wml>" +
                  "<card>"+
                    "<p>" +
                      "left" +
                    "</p>" +
                    "<p align='center'>" +
                      "center" +
                    "</p>" +
                    "<p align='right'>" +
                      "right" +
                    "</p>" +
                  "</card>"+
                "</wml>";


        checkTransformation(original, expected, true);
    }

    public void testBlockAll() throws Exception {
        String original =
                "<wml>" +
                  "<card>"+
                    "<BLOCK>" +
                      "(left)/(wrap)" +
                    "</BLOCK>" +
                    "<BLOCK style='text-align:left; white-space: normal'>" +
                      "left/wrap" +
                    "</BLOCK>" +
                    "<BLOCK style='text-align:right; white-space: normal'>" +
                      "right/wrap" +
                    "</BLOCK>" +
                    "<BLOCK style='text-align:right; white-space: nowrap'>" +
                      "right/nowrap" +
                    "</BLOCK>" +
                    "<BLOCK style='text-align:right; white-space: nowrap'>" +
                      "right/nowrap" +
                    "</BLOCK>" +
                  "</card>"+
                "</wml>";
        String expected =
                "<wml>" +
                  "<card>"+
                    "<p>" +
                      "(left)/(wrap)" +
                    "<br/>" +
                      "left/wrap" +
                    "</p>" +
                    "<p align='right'>" +
                      "right/wrap" +
                    "</p>" +
                    "<p align='right' mode='nowrap'>" +
                      "right/nowrap" +
                    "<br/>" +
                      "right/nowrap" +
                    "</p>" +
                  "</card>"+
                "</wml>";


        checkTransformation(original, expected, true);
    }

    /**
     * Tests processing of text node inside a table. Such nodes
     * are produced from XDIME containing table caption, until
     * it gets fixed by VBM 2006030111
     */
    public void testTableWithInnerText() throws Exception { 

        String original =
            "<wml>" +
              "<card>"+
                "<BLOCK>" +
                  "<table>" +
                    "This was once a caption" +
                    "<tr>" +
                      "<td>This is a cell contents</td>" +
                    "</tr>" +
                  "</table>" +
                "</BLOCK>" +
              "</card>"+
            "</wml>";

          String expected =
              "<wml>" +
                "<card>"+
                  "<p>" +
                    "This was once a caption<br/>" +
                    "This is a cell contents" +
                  "</p>" +
                "</card>"+
              "</wml>";

          checkTransformation(original, expected, true);
    }
    
    private void checkTransformation(String original, String expected,
                                     boolean honourAlign)
            throws Exception {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        ProtocolBuilder builder = new ProtocolBuilder();
        WMLRoot protocol = (WMLRoot) builder.build(
                new TestProtocolRegistry.TestWMLRootFactory(),
                internalDevice);
        MyMarinerPageContext context = new MyMarinerPageContext();
        protocol.setMarinerPageContext(context);
        WMLDOMTransformer transformer = new WMLDOMTransformer(
                protocol, honourAlign);
        XMLReader reader = DOMUtilities.getReader();

        Document originalDom = StyledDOMTester.createStyledDom(original);
//        Document originalDom = DOMUtilities.read(reader, original);
        Document expectedDom = DOMUtilities.read(reader, expected);

        // Transform dom from original so we can check against expected
        Document dom = transformer.transform(protocol, originalDom);

        String domAsString = DOMUtilities.toString(dom);
        String expectedDomAsString = DOMUtilities.toString(expectedDom);

        assertEquals(expectedDomAsString, domAsString);
    }
    
    class MyMarinerPageContext extends TestMarinerPageContext {

        public void resetDevicePolicyValue(String tagKey, String suffix,
                                           String newValue) {

            super.setDevicePolicyValue(tagKey + suffix, newValue);
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10779/1	geoff	VBM:2005121202 MCS35: WML vertical whitespace fix does not handle mode settings (take 2)

 29-Nov-05	10505/5	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (8)

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10328/1	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 21-Nov-05	10330/10	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 21-Nov-05	10330/8	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 17-Nov-05	10330/5	pabbott	VBM:2005110907 Honour align with mode=nospace

 17-Nov-05	10356/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 17-Nov-05	10333/3	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 21-Nov-05	10330/10	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 21-Nov-05	10330/8	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 17-Nov-05	10330/5	pabbott	VBM:2005110907 Honour align with mode=nospace

 17-Nov-05	10333/3	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 29-Sep-05	9600/3	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/1	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 04-May-05	7982/1	emma	VBM:2005041321 Merge from 3.3.0 - Re-enabling wml tag emulation support

 04-May-05	7980/1	emma	VBM:2005041321 Merge from 3.2.3 - Re-enabling wml tag emulation support

 16-Mar-05	7372/4	emma	VBM:2005031008 Modifications after review

 16-Mar-05	7372/2	emma	VBM:2005031008 Make cols attribute optional on the XDIME table element

 14-Jan-05	6346/3	geoff	VBM:2004110112 Certain form layouts generate invalid WML

 14-Jan-05	6346/1	geoff	VBM:2004110112 Certain form layouts generate invalid WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 13-Jun-03	399/1	mat	VBM:2003060503 Handle new special dissection tags

 12-Jun-03	394/1	mat	VBM:2003060503 Handle new special dissection tags

 ===========================================================================
*/
