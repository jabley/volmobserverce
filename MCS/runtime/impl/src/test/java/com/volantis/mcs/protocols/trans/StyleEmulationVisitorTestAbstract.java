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
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManagerBuilder;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Test the style visitor.
 */
public abstract class StyleEmulationVisitorTestAbstract
        extends TestCaseAbstract {

    public static final String NULL_ELEMENT = "NULL";

    /**
     * Setting this to null will result in no encoding
     */
    protected CharacterEncoder encoder = null;

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();
    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    /**
     * Specialized visitor to rename elements to their 'illegal' names.
     */
    public static class StyleRenamerTreeVisitor extends RecursingDOMVisitor {


        public void visit(Element element) {
            if (NULL_ELEMENT.equals(element.getName())) {
                element.setName(null);
            }
            element.forEachChild(this);
        }
    }

    /**
     * A Visitation DOMVisitor used to collate the visitation count for
     * each node in the DOM Tree.
     */
    private static class VisitationCheckerDOMVisitor
            extends RecursingDOMVisitor {

        public List notVisited = new ArrayList();

        // javadoc inherited
        public void visit(Element element) {
            updateLists(element);
            element.forEachChild(this);
        }

        // javadoc inherited
        public void visit(Text text) {
            updateLists(text);
        }

        /**
         * Update the lists with the visitation count.
         * @param node the node to update.
         */
        private void updateLists(Node node) {
            if (node.getObject() == null) {
                notVisited.add(node);
            }
        }

        /**
         * Getter for the list of nodes that weren't visited.
         * @return the list of nodes that weren't visited.
         */
        public List getNotVisited() {
            return notVisited;
        }
    }

    /**
     * Create a style visitor.
     *
     * @return the newly created style visitor.
     */
    protected StyleEmulationVisitor createStyleVisitor() {
        ProtocolConfigurationImpl configuration = getProtocolConfiguration();
        final HashMap policies = new HashMap();
        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice("testDevice", policies, null));
        DeviceCapabilityManagerBuilder builder =
                new DeviceCapabilityManagerBuilder(device);
        configuration.initialize(device, builder);
        return new StyleEmulationVisitor(domFactory,
                configuration.getStyleEmulationElementConfiguration(),
                new StyleEmulationElementTracker());
    }

    /**
     * Create the protocol configuration which should be used in this test.
     *
     * @return ProtocolConfiguration
     */
    protected abstract ProtocolConfigurationImpl getProtocolConfiguration();

    protected abstract String getParagraphElement();

    /**
     * Test the recursive nature of pushing a matching parent down.
     */
    public void testPushMatchingParentElementDownSimple() throws Exception {
        StyleEmulationVisitor visitor = createStyleVisitor();
        String input =
                "<b>" +
                    "textA" +
                    "<a>" +
                        "txt-1" +
                        "<ANTI-B>txt-3</ANTI-B>" +
                        "txt-2" +
                    "</a>" +
                    "textE" +
                "</b>";

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);

        Element parent = dom.getRootElement();

        // the 'a' element.
        Element indivisibleElement = (Element)parent.getHead().getNext();
        visitor.pushCounterpartElementDown(indivisibleElement, "b");

        String actual = DOMUtilities.toString(dom, encoder);

        String expected = getExpectedPushMatchingParentElementDownSimple();

        verifyDOMMatches(null, expected, actual);
    }

    /**
     * Create expected results for
     * {@link #testPushMatchingParentElementDownSimple}.
     */
    protected abstract String getExpectedPushMatchingParentElementDownSimple();

    /**
     * Test the recursive nature of pushing a matching parent down.
     */
    public void testPushMatchingParentElementDown() throws Exception {
        StyleEmulationVisitor visitor = createStyleVisitor();
        String input =
                "<b>" +
                    "<i>" +
                        "textA" +
                        "<a>" +
                            "txt-1" +
                            "<ANTI-B>txt-3</ANTI-B>" +
                            "txt-2" +
                        "</a>" +
                        "textE" +
                    "</i>" +
                "</b>";

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);

        Element italics = (Element)dom.getRootElement().getHead();
        Element atomicElement = (Element)italics.getHead().getNext();
        visitor.pushCounterpartElementDown(atomicElement, "b");

        String actual = DOMUtilities.toString(dom, encoder);

        String expected = getExpectedPushMatchingParentElementDown();
        verifyDOMMatches(null, expected, actual);
    }

    /**
     * Create expected results for
     * {@link #testPushMatchingParentElementDown}.
     */
    protected abstract String getExpectedPushMatchingParentElementDown();

    /**
     * Test a simple transformation with redundant markup which should be removed.
     */
    public void testTransformWithRedundantMarkup() throws Exception {
        String p = getParagraphElement();
        String input =
          "<" + p + ">" +
            "<i>" +
              "Some " +
              "<ANTI-B>" +
                "Unbold " +
              "</ANTI-B>" +
              "text" +
            "</i>" +
            "normal" +
          "</" + p + ">";

        String expected =
                "<" + p + ">" +
                    "<i>Some Unbold text</i>" +
                    "normal" +
                "</" + p + ">";
        doTest(input, expected);
    }

    /**
     * Test a simple transformation with redundant markup which should be removed.
     */
    public void testTransformWithRedundantMarkupAntiBold() throws Exception {
        String p = getParagraphElement();
        String input =
          "<" + p + ">" +
            "<ANTI-B>" +
              "Some " +
              "<i>" +
                "Italic " +
              "</i>" +
              "text " +
            "</ANTI-B>" +
            "<u>" +
              "underline " +
              "<ANTI-U>" +
                "not" +
              "</ANTI-U>" +
            "</u>" +
          "</" + p + ">";

        String expected =
                "<" + p + ">" +
                    "Some " +
                    "<i>" +
                        "Italic " +
                    "</i>" +
                    "text " +
                    "<u>" +
                        "underline " +
                    "</u>" +
                    "not" +
                "</" + p + ">";
        doTest(input, expected);
    }

    /**
     * More complex
     */
    public void testTransformWithRedundantMarkupAntiBoldAndAntiItalics()
            throws Exception {
        String p = getParagraphElement();

        String input =
            "<" + p + ">" +
              "<i>" +
                "<ANTI-B>" +
                  "Some " +
                  "<i>" +
                    "Italic " +
                  "</i>" +
                  "<ANTI-I>" +
                    "not italics " +
                  "</ANTI-I>" +
                "</ANTI-B>" +
              "</i>" +
              "more not italic" +
            "</" + p + ">";

        String expected =
                "<" + p + ">" +
                  "<i>Some Italic </i>" +
                  "not italics more not italic" +
                "</" + p + ">";
        doTest(input, expected);
    }

    /**
     * Test a simple transformation with anti-bold elements.
     */
    public void testTransformSimpleInversion() throws Exception {
        String input =
            "<b>" +
              "This is some" +
              "<ANTI-B>" +
                "text" +
              "</ANTI-B>" +
              "which is mainly, but not all, bold." +
            "</b>";

       String expected =
            "<b>" +
              "This is some" +
            "</b>" +
            "text" +
            "<b>" +
              "which is mainly, but not all, bold." +
            "</b>";

        doTest(input, expected);
    }

    /**
     * Test a simple transformation with anti-bold elements.
     */
    public void testTransformSimpleNestedInversion() throws Exception {
        String input =
            "<b>" +
              "bold-1" +
              "<ANTI-B>" +
                "<p>normal-1</p>" +
              "</ANTI-B>" +
              "bold-2" +
            "</b>";

       String expected =
            "<b>" +
              "bold-1" +
            "</b>" +
            "<p>normal-1</p>" +
            "<b>" +
              "bold-2" +
            "</b>";

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
                "<ANTI-B>" +
                  "text" +
                "</ANTI-B>" +
                "with and" +
                "<ANTI-I>" +
                  "without" +
                "</ANTI-I>" +
                "italics" +
              "</i>" +
              "which is mainly, but not all, bold." +
           "</b>";

        String expected =
            "<b>" +
              "This is some" +
              "<i>nested</i>" +
            "</b>" +
            "<i>text</i>" +
            "<b>" +
              "<i>with and</i>" +
              "without" +
              "<i>italics</i>" +
              "which is mainly, but not all, bold." +
            "</b>";
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
                     "<ANTI-B>" +
                        "Normal" +
                     "</ANTI-B>" +
                     "Bold" +
                   "</td>" +
                   "<td>" +
                     "All Bold" +
                   "</td>" +
                 "</tr>" +
              "</table>" +
             "</b>";

        String expected =
            "<table>" +
              "<tr>" +
                "<td>" +
                  "<b>" +
                    "Bold" +
                  "</b>" +
                  "Normal" +
                  "<b>" +
                    "Bold" +
                  "</b>" +
                "</td>" +
                "<td>" +
                  "<b>" +
                    "All Bold" +
                  "</b>" +
                "</td>" +
              "</tr>" +
            "</table>";

        doTest(input, expected);
    }
    /**
     * Test transformation using simple containment.
     */
    public void testTransformSimpleContainment() throws Exception {
        // In WML <b> may contain a table
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

        String expected = getExpectedTransformSimpleContainment();
        doTest(input, expected);
    }

    protected String getExpectedTransformSimpleContainment() {
        // In WML <b> may contain a table
        String expected =
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
        return expected;
    }


    /**
     * Multi element push down
     * @throws Exception
     */
    public void testTransformMultiElementContainment() throws Exception {
        String p = getParagraphElement();
        String input =
           "<" + p + ">" +
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
              "<" + p + ">text</" + p + ">" +
            "</b>" +
           "</" + p + ">";

        String expected =
            "<" + p + ">" +
              "<b>" +
                "textA" +
              "</b>" +
              "<table>" +
                  "<tr>" +
                    "<td>" +
                      "<b>" +
                        "Bold" +
                      "</b>" +
                    "</td>" +
                    "<td>" +
                      "<b>" +
                        "All Bold" +
                      "</b>" +
                    "</td>" +
                  "</tr>" +
              "</table>" +
              "<b>" +
                "textB" +
              "</b>" +
              "<" + p + ">" +
                "<b>" +
                  "text" +
                "</b>" +
              "</" + p + ">" +
            "</" + p + ">";
        doTest(input, expected);
    }


    /**
     * Test transformation containment inversion.
     */
    public void testTransformNestedRedundantElements() throws Exception {
        String p = getParagraphElement();
        String input =
            "<" + p + ">" +
              "<b>" +
                "<b>" +
                  "<ANTI-B>" +
                    "<ANTI-B>" +
                      "Alpha" +
                    "</ANTI-B>" +
                  "</ANTI-B>" +
                "</b>" +
              "</b>" +
            "</" + p + ">";

        String expected =
                "<" + p + ">" +
                    "Alpha" +
                "</" + p + ">";

        doTest(input, expected);
    }

    /**
     * Test the atomicity
     * @todo all nodes are not visited.
     */
    public void testTransformAtomicity() throws Exception {
        String input =
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

        String expected = getExpectedAtomicity();
        doTest(input, expected);
    }

    /**
     * Create expected results for {@link #testTransformAtomicity}.
     */
    protected abstract String getExpectedAtomicity();

    /**
     * Test nested atomicity.
     * @todo all nodes are not visited.
     */
    public void testTransformAtomicityNested() throws Exception {
        String input =
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

        String expected = getExpectedAtomicityNested();
        doTest(input, expected);
    }

    /**
     * Create expected results for {@link #testTransformAtomicityNested}.
     */
    protected abstract String getExpectedAtomicityNested();

    /**
     * Test the atomicity with KEEP_TOGETHER
     * @todo all nodes are not visited.
     */
    public void testTransformAtomicityKeepTogether() throws Exception {
        String input =
            "<b>" +
              "Block text before" +
              "<KEEP-TOGETHER>" +
                "Bold link text" +
                "<ANTI-B>" +
                  "Normal link Text" +
                "</ANTI-B>" +
                "Bold link text" +
              "</KEEP-TOGETHER>" +
              "Bold text after" +
            "</b>";

        String expected =
            "<b>" +
              "Block text before" +
            "</b>" +
            "<" + DissectionConstants.KEEPTOGETHER_ELEMENT + ">" +
              "<b>" +
                "Bold link text" +
              "</b>" +
              "Normal link Text" +
              "<b>" +
                "Bold link text" +
              "</b>" +
            "</" + DissectionConstants.KEEPTOGETHER_ELEMENT + ">" +
            "<b>" +
              "Bold text after" +
            "</b>";

        doTest(input, expected);
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


        doTest(input, getExpectedTransformList());
    }

    /**
     * Create expected results for {@link #testTransformAtomicity}.
     */
    protected String getExpectedTransformList() {
        String expected =
            "<b>" +
              "Text Before" +
            "</b>" +
            "<ol>" +
                "<li>" +
                    "List Item 1" +
                "</li>" +
                "<li>" +
                    "List Item 2" +
                "</li>" +
            "</ol>" +
            "<b>" +
              "Text After" +
            "</b>";
        return expected;
    }


    /**
     * Helper method for doing the test.
     * @param input    the input DOM.
     * @param expected the expected DOM.
     */
    protected void doTest(
                          String input,
                          String expected)
            throws Exception {

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);
        new StyleRenamerTreeVisitor().visit(dom.getRootElement());

        StyleEmulationVisitor visitor = createStyleVisitor();
        visitor.transform(dom);

        String actual = DOMUtilities.toString(dom, encoder);
        assertEquals("Transformed result should match: " +
                "\nEXPECTED: " + expected +
                "\nACTUAL  : " + actual + "\n",
                expected,
                actual);

        // Visit all the nodes and examine how many time each node has been
        // processed.
        VisitationCheckerDOMVisitor visitationChecker =
                new VisitationCheckerDOMVisitor();

        dom.forEachChild(visitationChecker);

      //  System.out.println((DOMUtilities.toString(dom.getContentRoot(),
     //           protocol)));

        // TODO: we have had bugs where nodes were not visited, so fix this
        // This is commented out since there are test cases that have nodes
        // that aren't visited. This is OK since these nodes are created to
        // the left of the current element (which has already been traversed)
        // and therefore there is no need to visit them again. Additional
        // code will have to be added to explicitly set these nodes as visited
        // which will be redundant during runtime.
//        assertEquals("All nodes should've been visited: " +
//                visitationChecker.getNotVisited(),
//                0, visitationChecker.getNotVisited().size());

    }

    /**
     * Helper method that formats the failure nicely so that it is easy
     * to see the difference in the dom.
     * @param msg the message to display.
     * @param expected the expected result.
     * @param actual the actual result.
     */
    protected void verifyDOMMatches(String msg, String expected, String actual) {
        if (msg == null) {
            msg = "Transformed result should match: ";
        }
        assertEquals(msg +
                "\nEXPECTED: " + expected +
                "\nACTUAL  : " + actual + "\n",
                expected,
                actual);
    }

    /**
     * Helper method for testing
     */
    protected void pushCounterpartElementDown(StyleEmulationVisitor visitor,
                                              Element startElement,
                                              String elementName) {
        visitor.pushCounterpartElementDown(startElement, elementName);
    }

    /**
     * Helper method for testing
     */
    protected void pushStyleElementsDown(StyleEmulationVisitor visitor,
                                        Element startElement) {
        visitor.pushStyleElementsDown(startElement);
    }

    /**
     * Test the pushing down of a stylistic element to all children.
     */
    public void testPushElementDownToAllChildren() throws Exception {
        String p = getParagraphElement();
        String input;
        String expected;
        input = "<" + p + "></" + p + ">";
        expected = "<" + p + "/>";
        doTestPushElementDownToAllChildren(input, expected, "b");

        input = "<" + p + ">text</" + p + ">";
        expected = "<" + p + "><b>text</b></" + p + ">";
        doTestPushElementDownToAllChildren(input, expected, "b");

        input = "<" + p + ">text<" + p + ">text2</" + p + ">text3</" + p + ">";
        expected =
                "<" + p + ">" +
                    "<b>text</b>" +
                    "<" + p + ">" +
                        "<b>text2</b>" +
                    "</" + p + ">" +
                    "<b>text3</b>" +
                "</" + p + ">";
        doTestPushElementDownToAllChildren(input, expected, "b");

        input =
            "<body>" +
                "<table>" +
                    "<tr>" +
                        "<td>" +
                            "Bold" +
                                "<ANTI-B>" +
                                    "Normal" +
                                "</ANTI-B>" +
                                "Bold" +
                        "</td>" +
                        "<td>" +
                            "All Bold" +
                        "</td>" +
                    "</tr>" +
                "</table>" +
            "</body>";
        expected =
                "<body>" +
                    "<table>" +
                        "<tr>" +
                            "<td>" +
                                "<b>" +
                                    "Bold" +
                                    "<ANTI-B>" +
                                        "Normal" +
                                    "</ANTI-B>" +
                                    "Bold" +
                                "</b>" +
                            "</td>" +
                            "<td>" +
                                "<b>" +
                                    "All Bold" +
                                "</b>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</body>";
        doTestPushElementDownToAllChildren(input, expected, "b");
    }

    /**
     * Helper method for removing style element test.
     */
    protected void doTestPushElementDownToAllChildren(String input,
                                                      String expected,
                                                      Element element,
                                                      boolean recursive)
            throws Exception {

        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);

        Element root = dom.getRootElement();

        StyleEmulationVisitor visitor = createStyleVisitor();
        visitor.pushElementDownToAllChildren(element, root.getHead(),
            recursive, null);

        String actual = DOMUtilities.toString(root, encoder);
        verifyDOMMatches(null, expected, actual);
    }

    /**
     * Helper method for removing style element test.
     */
    protected void doTestPushElementDownToAllChildren(String input,
                                                      String expected,
                                                      String elementName)
            throws Exception {

        Element element = domFactory.createElement();
        element.setName(elementName);
        doTestPushElementDownToAllChildren(input, expected, element, true);
    }

    /**
     * Test DivisibleStyleElements (not indivisible, special and anti-element)
     * In this case the 'sup' element.
     */
    public void testDivisibleStyleElements() throws Exception {
        String p = getParagraphElement();
        // The 'sup' element exists in HTML v3.2 and not in WML. Therefore
        // in WML it is treated as an indivisible element that cannot contain
        // stylistic elements (which means the b and anti-bold are are not
        // pushed into the sup element since it cannot contain b or anti-b.
        // In HTML, the sup tag is divisible and the 'b' element should be
        // preserved.
        String input =
            "<b>" +
                "<sup>" +
                    "<ANTI-B>" +
                    "[not bold]" +
                    "</ANTI-B>" +
                    "[bold]" +
                "</sup>" +
                "<" + p + ">" +
                    "[bold-paragraph]" +
                "</" + p + ">" +
            "</b>";

        String expected = getExpectedDivisibleStyleElements();
        doTest(input, expected);
    }

    /**
     * Create expected results for {@link #testTransformAtomicity}.
     */
    protected String getExpectedDivisibleStyleElements() {
        String expected =
            "<sup>" +
                "[not bold]" +
                "<b>" +
                    "[bold]" +
                "</b>" +
            "</sup>" +
            "<p>" +
                "<b>" +
                    "[bold-paragraph]" +
                "</b" +
            "></p>";
        return expected;
    }

    /**
     * Test DivisibleStyleElements (not indivisible, special and anti-element)
     * In this case the 'null' named element.
     */
    public void testDivisibleStyleElementsNull() throws Exception {
        String input =
            "<b>" +
                "<" + NULL_ELEMENT + ">" +
                    "<ANTI-B>" +
                        "[not bold]" +
                    "</ANTI-B>" +
                    "[should-be-bold]" +
                "</" + NULL_ELEMENT + ">" +
                "[bold]" +
            "</b>";

        String expected = getExpectedDivisibleStyleElementsNull();
        doTest(input, expected);
    }

    /**
     * Create expected results for {@link #testTransformAtomicity}.
     */
    protected String getExpectedDivisibleStyleElementsNull() {
        String expected =
            "[not bold]" +
            "<b>" +
                "[should-be-bold]" +
            "</b>" +
            "<b>" +
                "[bold]" +
            "</b>";
        return expected;
    }

    /**
     * Create expected results for
     * {@link #testNestedFont}.
     */
    protected abstract String getNestedFont();

    /**
     * Test the simple font (no transformation). This test ensures that the
     * problem seen in VBM 2004120805 does not reoccur.
     */
    public void testNestedFont() throws Exception {
        String p = getParagraphElement();
        String input =
                "<" + p + ">" +
                    "<font size=\"3\">" +
                        "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                            "<tr>" +
                                "<td align=\"center\" bgcolor=\"#c6d3de\">" +
                                    "<font color=\"#ffffff\">" +
                                        "<b>" +
                                            "<a href=\"/wps/portal/!ut/p/.scr/Login\">" +
                                                "<font color=\"#3366cc\">" +
                                                    "<img alt=\"[Log in]\" border=\"0\" height=\"20\" src=\"/wps/themes/xdime/images/nav_login.gif\" width=\"20\"/>" +
                                                "</font>" +
                                            "</a>" +
                                            "<a href=\"/wps/portal/!ut/p/.scr/ForgotPassword\">" +
                                                "<font color=\"#000000\">" +
                                                    "<img alt=\"[I forgot my password]\" border=\"0\" height=\"20\" src=\"/wps/themes/xdime/images/nav_forgot_password.gif\" width=\"23\"/>" +
                                                "</font>" +
                                            "</a>" +
                                        "</b>" +
                                    "</font>" +
                                "</td>" +
                            "</tr>" +
                        "</table>" +
                    "</font>" +
                "</" + p + ">";
        String expected = getNestedFont();
        doTest(input, expected);
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

 09-Aug-05	9211/2	pabbott	VBM:2005080902 End to End CSS emulation test

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 04-Jan-05	6433/6	matthew	VBM:2004120805 recommit

 04-Jan-05	6433/4	matthew	VBM:2004120805 recommit

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Oct-04	5877/7	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 26-Oct-04	5877/5	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 19-Oct-04	5843/6	geoff	VBM:2004100710 Invalid WML is being generated since introduction of theme style options (R599)

 19-Oct-04	5854/1	claire	VBM:2004080905 Allowing multiple levels of anti- elements

 19-Oct-04	5782/1	claire	VBM:2004080905 Allowing multiple levels of anti- elements

 19-Aug-04	5272/1	byron	VBM:2004081902 MCS erroneously outputs anti-xxx elements

 19-Aug-04	5268/1	byron	VBM:2004081902 MCS erroneously outputs anti-xxx elements

 21-Jul-04	4752/7	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - fix anti-size in WML

 14-Jul-04	4783/3	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 14-Jul-04	4752/5	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 13-Jul-04	4752/3	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 ===========================================================================
*/
