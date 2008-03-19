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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import junitx.util.PrivateAccessor;

/**
 * Test that the {@link AbstractDivRemover} works as expected..
 */
public abstract class AbstractDivRemoverTestAbstract
        extends MockTestCaseAbstract {

    private StrictStyledDOMHelper helper;

    protected void setUp() throws Exception {
        super.setUp();

        setStrictStyledDOMHelper(new StrictStyledDOMHelper(null));
    }

    /**
     * Set the StrictStyledDOMHelper field.
     *
     * @param helper
     */
    protected void setStrictStyledDOMHelper(StrictStyledDOMHelper helper) {
        this.helper = helper;
    }

    /**
     * Return the StrictStyledDOMHelper.
     *
     * @return a StrictStyledDOMHelper - may be null depending on what has
     * called {@link #setStrictStyledDOMHelper(com.volantis.mcs.runtime.debug.StrictStyledDOMHelper)}
     */
    protected StrictStyledDOMHelper getStrictStyledDOMHelper() {
        return this.helper;
    }

     /**
     * Specialized visitor to rename 'null' dom items to null
     */
    class NullRemoverTreeVisitor extends RecursingDOMVisitor {


         // Javadoc inherited.
         public void visit(Document document) {
             document.forEachChild(this);
         }

         public void visit(Element element) {
            if ("null".equals(element.getName())) {
                element.setName(null);
            }
            element.forEachChild(this);
        }
    }

    /**
     * Return the {@link AbstractDivRemover} instance with which the tests
     * should be run.
     *
     * @return AbstractDivRemover
     */
    public abstract AbstractDivRemover getDivRemover();


    /**
     * Perform the find test by first checking that we have found the correct
     * item in the sibling list and then performing the find itself.
     *
     * @param  dom         the document containing the dom tree
     * @param  num         the string representing the index of the child in
     *                     the list.
     * @param  divRemover  the transformer used to find the only child.
     * @return             the only child, or null if not found
     */
    private Element doFindTest(Document dom,
                               String num,
                               AbstractDivRemover divRemover) {
        Element element = getChild((Element) dom.getRootElement().getHead(),
                                   Integer.parseInt(num));
        assertEquals("Name should match", "div", element.getName());
        assertEquals("Foo id should match", num, element.getAttributeValue("foo"));
        return  divRemover.findOnlyChild(element, "div");
    }

    /**
     * Test the finding of inner elements. Particularly those element that have
     * null children...
     */
    public void testFindOnlyChild() {
        String domString =
                "<root>" +
                "<table>" +
                    "<div foo=\"1\">" +
                      "<div foo=\"1-1\">" +
                      "</div>" +
                    "</div>" +
                    "<div foo=\"2\">" +
                      "<null>" +
                        "<div foo=\"2-1\">" +
                        "</div>" +
                      "</null>" +
                    "</div>" +
                    "<div foo=\"3\">" +
                      "<null>" +
                        "<div foo=\"3-1\">" +
                        "</div>" +
                      "</null>" +
                      "<null>" +
                      "</null>" +
                    "</div>" +
                    "<div foo=\"4\">" +
                      "<null>" +
                      "</null>" +
                      "<div foo=\"4-1\">" +
                      "</div>" +
                    "</div>" +
                    "<div foo=\"5\">" +
                      "<null>" +
                      "</null>" +
                      "<div foo=\"5-1\">" +
                      "</div>" +
                      "<div foo=\"5-2\">" +
                      "</div>" +
                    "</div>" +
                    "<div foo=\"6\">" +
                      "<null>" +
                        "<null>" +
                            "<div foo=\"6-1\">" +
                            "</div>" +
                        "</null>" +
                      "</null>" +
                      "<null>" +
                      "</null>" +
                    "</div>" +
                    "<div foo=\"7\">" +
                      "<null>" +
                          "<div foo=\"7-1\">" +
                          "</div>" +
                        "</null>" +
                        "<null>" +
                            "<div foo=\"7-2\">" +
                            "</div>" +
                        "</null>" +
                    "</div>" +
                    "<div foo=\"8\">" +
                      "<p>" +
                         "<div foo=\"8-1\">" +
                         "</div>" +
                      "</p>" +
                    "</div>" +
                "</table>" +
                "</root>";

        try {
            Document dom = getStrictStyledDOMHelper().parse(domString);

            Element element = dom.getRootElement();
            dom.accept(new NullRemoverTreeVisitor());

            AbstractDivRemover divRemover = getDivRemover();

            // Should find 'table' as top element
            element = divRemover.findOnlyChild(element, "table");
            assertEquals("Element should match", "table", element.getName());

            // Should find multiple divs because table has two div children.
            element = divRemover.findOnlyChild(element, "div");
            assertSame(element, getMultipleChildrenConstant());

            // Start with the root and get the first child
            element = doFindTest(dom, "1", divRemover);
            assertEquals("Element should match", "1-1",
                    element.getAttributeValue("foo"));

            element = doFindTest(dom, "2", divRemover);
            assertNotNull("Element shouldn't be null", element);
            assertEquals("Element should match", "2-1",
                    element.getAttributeValue("foo"));

            element = doFindTest(dom, "3", divRemover);
            assertNotNull("Element shouldn't be null", element);
            assertEquals("Element should match", "3-1",
                    element.getAttributeValue("foo"));

            element = doFindTest(dom, "4", divRemover);
            assertNotNull("Element shouldn't be null", element);
            assertEquals("Element should match", "4-1",
                    element.getAttributeValue("foo"));

            element = doFindTest(dom, "5", divRemover);
            assertSame(element, getMultipleChildrenConstant());

            element = doFindTest(dom, "6", divRemover);
            assertNotNull("Element shouldn't be null", element);
            assertEquals("Element should match", "6-1",
                    element.getAttributeValue("foo"));

            element = doFindTest(dom, "7", divRemover);
            assertSame(element, getMultipleChildrenConstant());

            element = doFindTest(dom, "8", divRemover);
            assertSame(element, getMultipleChildrenConstant());

        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
    }

    /**
     * Tests that findOnlyChild finds multiple element matches when a div has
     * only two non-null children, only one of which is a div.
     */
    public void testFindOnlyChildMultipleMatchesWithDivAndNonDivChildren()
            throws Exception {
        String domString =
                "<root>" +
                "<div foo=\"VF-0\">" +
                  "<null>" +
                    "<null>" +
                      "<div foo=\"VF-1\">" +
                        "<mydiv foo=\"Content\">" +
                          "<p foo=\"VE-p\">Some text</p>" +
                        "</mydiv>" +
                        "<div foo=\"ItemSeparator\">" +
                          "<img foo=\"VE-img\" src=\"myimg.gif\"/>" +
                        "</div>" +
                      "</div>" +
                    "</null>" +
                  "</null>" +
                "</div>" +
                "</root>";

        Document dom = getStrictStyledDOMHelper().parse(domString);

        // Turn the elements named "null" into elements whose name is actually
        // null.
        dom.accept(new NullRemoverTreeVisitor());

        AbstractDivRemover divRemover = getDivRemover();
        Element element = divRemover.findOnlyChild(dom.getRootElement(), "div");

        // First div (foo VF-0) should have been found.
        assertNotNull(element);
        assertTrue(element != getMultipleChildrenConstant());
        assertEquals("Foo attribute should be the same",
                element.getAttributeValue("foo"), "VF-0");

        element = divRemover.findOnlyChild(element, "div");

        // Second div (foo VF-1) should have been found.
        assertNotNull(element);
        assertTrue(element != getMultipleChildrenConstant());
        assertEquals("Foo attribute should be the same",
                element.getAttributeValue("foo"), "VF-1");

        // Do the transformation.
        element = divRemover.findOnlyChild(element, "div");

        // The VF-1 div has two children one of which has a div.
        assertNotNull(element);
        assertTrue(element == getMultipleChildrenConstant());
    }

    /**
     * Tests that findOnlyChild finds multiple element matches when a div has
     * only two non-null desendants, only one of which is a div.
     */
    public void testFindOnlyChildMultipleMatchesWithDivAndNonDivDescendants()
            throws Exception {
        String domString =
                "<root>" +
                "<div foo=\"VF-0\">" +
                  "<null>" +
                    "<null>" +
                      "<div foo=\"VF-1\">" +
                        "<null>" +
                          "<mydiv foo=\"Content\">" +
                            "<p foo=\"VE-p\">Some text</p>" +
                          "</mydiv>" +
                        "</null>" +
                        "<null>" +
                          "<div foo=\"ItemSeparator\">" +
                            "<img foo=\"VE-img\" src=\"myimg.gif\"/>" +
                          "</div>" +
                        "</null>" +
                      "</div>" +
                    "</null>" +
                  "</null>" +
                "</div>" +
                "</root>";

        Document dom = getStrictStyledDOMHelper().parse(domString);

        // Turn the elements named "null" into elements whose name is actually
        // null.
        dom.accept(new NullRemoverTreeVisitor());

        AbstractDivRemover divRemover = getDivRemover();
        Element element = divRemover.findOnlyChild(dom.getRootElement(), "div");

        // First div (foo VF-0) should have been found.
        assertNotNull(element);
        assertTrue(element != getMultipleChildrenConstant());
        assertEquals("Foo attribute should be the same",
                element.getAttributeValue("foo"), "VF-0");

        element = divRemover.findOnlyChild(element, "div");

        // Second div (foo VF-1) should have been found.
        assertNotNull(element);
        assertTrue(element != getMultipleChildrenConstant());
        assertEquals("Foo attribute should be the same",
                element.getAttributeValue("foo"), "VF-1");

        // Do the transformation.
        element = divRemover.findOnlyChild(element, "div");

        // The VF-1 div has two children one of which has a div.
        assertNotNull(element);
        assertTrue(element == getMultipleChildrenConstant());
    }


    /**
     * Tests that findOnlyChild finds multiple element matches when a div has
     * two div descendants, and no other non-null descendants.
     */
    public void testFindOnlyChildMultipleDivMatches() throws Exception {
        String domString =
                "<root>" +
                "<div foo=\"VF-0\">" +
                  "<null>" +
                    "<null>" +
                      "<div foo=\"VF-1\">" +
                        "<null>" +
                          "<div foo=\"Content\">" +
                            "<p foo=\"VE-p\">Some text</p>" +
                          "</div>" +
                        "</null>" +
                        "<null>" +
                          "<div foo=\"ItemSeparator\">" +
                            "<img foo=\"VE-img\" src=\"myimg.gif\"/>" +
                          "</div>" +
                        "</null>" +
                      "</div>" +
                    "</null>" +
                  "</null>" +
                "</div>" +
                "</root>";

        Document dom = getStrictStyledDOMHelper().parse(domString);

        // Turn the elements named "null" into elements whose name is actually
        // null.
        dom.accept(new NullRemoverTreeVisitor());

        AbstractDivRemover divRemover = getDivRemover();
        Element element = divRemover.findOnlyChild(dom.getRootElement(), "div");

        // First div (foo VF-0) should have been found.
        assertNotNull(element);
        assertTrue(element != getMultipleChildrenConstant());
        assertEquals("Foo attribute should be the same",
                element.getAttributeValue("foo"), "VF-0");

        element = divRemover.findOnlyChild(element, "div");

        // Second div (foo VF-1) should have been found.
        assertNotNull(element);
        assertTrue(element != getMultipleChildrenConstant());
        assertEquals("Foo attribute should be the same",
                element.getAttributeValue("foo"), "VF-1");

        // Search for the children.
        element = divRemover.findOnlyChild(element, "div");

        // The VF-1 div has two children each of which has a div so multiple
        // match.
        assertNotNull(element);
        assertTrue(element == getMultipleChildrenConstant());
    }

    /**
     * Test that {@link AbstractDivRemover#findSibling} correctly identifies
     * an element's siblings.
     */
    public void testFindSibling() {
        // Div tag is a marker to use as the start tag therefore it is advised
        // that only one div tag be used in each test case (for simplicity).
        String domTree[] = {
          "<html><div></div></html>",
          "<html><div></div><h1></h1></html>",
          "<html><h1></h1><div></div><h2></h2></html>",
          "<html><null></null><div></div><br></br></html>",
          "<html><null><pre></pre></null><div></div><br></br></html>",

          "<html>Txt<div></div></html>",
          "<html>Txt<div></div><br></br>></html>",
          "<html><null>Txt</null><div></div><null><br></br></null></html>",
          "<html><null><null>Txt</null></null><div></div><null></null><br></br></html>",
          "<html><null><null><h3>Txt</h3></null></null><div></div><null></null><br></br></html>",

          "<html><null></null><h3>Txt</h3><null></null><div></div>Txt</html>"
        };
        String values[][] = {
            {domTree[0], null, null,  null},
            {domTree[1], null, "h1", "div" },
            {domTree[2], "h1", "h2", "div"},
            {domTree[3], null, "br", "div"},
            {domTree[4], "pre", "br", "div"},

            {domTree[5], "Txt", null, "div"},
            {domTree[6], "Txt", "br", "div"},
            {domTree[7], "Txt", "br", "div"},
            {domTree[8], "Txt", "br", "div"},
            {domTree[9], "h3", "br", "div"},

            {domTree[10], "h3", "Txt", "div"}
        };
        final int DOM = 0;
        final int EXPECTED_PREVIOUS = 1;
        final int EXPECTED_NEXT = 2;
        final int MATCH = 3;

        AbstractDivRemover divRemover = getDivRemover();

        for (int i=0; i < values.length; i++) {

            Document dom = null;
            try {
                dom = getStrictStyledDOMHelper().parse(values[i][DOM]);
                dom.accept(new NullRemoverTreeVisitor());

                // 'html' node
                Node node = dom.getRootElement();
                assertTrue(node instanceof Element);
                assertEquals("html", ((Element)node).getName());

                node = ((Element) node).getHead();
                if (values[i][MATCH] != null) {
                    String nodeName = getNodeName(node);
                    while ((node != null) && !values[i][MATCH].equals(nodeName)) {
                        node = node.getNext();
                        nodeName = getNodeName(node);
                    }
                    assertEquals(values[i][MATCH], nodeName);
                }
                assertNotNull(node);
                // Previous sibling test
                Node testNode = divRemover.findSibling(node.getPrevious(), false);
                assertEquals("TestPrev " + i, values[i][EXPECTED_PREVIOUS], getNodeName(testNode));

                // Next sibling test
                testNode = divRemover.findSibling(node.getNext(), true);
                assertEquals("TestNext " + i, values[i][EXPECTED_NEXT], getNodeName(testNode));

            } catch (Exception e) {
                //System.out.println("i = " + i);
                e.printStackTrace();
                fail("Unexpected exception thrown");
            }
        }
    }

    /**
     * Test the XHTMLBasicTransformer's inner foo' TreeVisitor method for
     * getting/counting the number of children a particular node has.
     */
    public void testCountChildren() {

        String domString =
                "<root>" +
                "<table>" +
                    "<tr>" +
                      "<td>" +
                      "</td>" +
                      "<td>" +
                      "</td>" +
                    "</tr>" +
                    "<tr>" +
                      "<td>" +
                      "</td>" +
                    "</tr>" +
                "</table>" +
                "</root>";

        try {
            Document dom = getStrictStyledDOMHelper().parse(domString);

            AbstractDivRemover divRemover = getDivRemover();
            Element element = dom.getRootElement();
            element = divRemover.findOnlyChild(element, "table");

            final String MSG = "Expected number of children not found";
            // Test 1: Count the children of <table> (which should be two
            // for the 2 rows)
            int actualChildren = divRemover.countChildren(element);
            assertTrue(MSG + " (test 1)", actualChildren == 2);


            // Test 2: Count the children of <table> (which should be three
            // one for 2nd row and two for the 1st row's children <td>
            ((Element)element.getHead()).setName(null);
            actualChildren = divRemover.countChildren(element);
            assertTrue(MSG + " (test 2)", actualChildren == 3);


            // Test 3: Count the children of <table> (which should be one
            // one for 2nd row and 1 for the 1st row's child <td> (has null
            // name)
            ((Element)((Element)element.getHead()).getHead()).setName(null);
            actualChildren = divRemover.countChildren(element);
            assertTrue(MSG + " (test 3)", actualChildren == 2);

        } catch (Exception e) {
            fail("Unable to complete the test for counting children");
        }
    }

    private String getNodeName(Node node) {
        String result = null;
        if (node != null) {
            if (node instanceof Element) {
                result = ((Element) node).getName();
            } else {
                result = new String(((Text)node).getContents()).trim();
            }
        }
        return result;
    }

    /**
     * Used in testFindInnerElement to return the child of an element with the
     * specified index offset
     *
     * @param  parent the element that will be used to find the n'th child
     * @param  index  the index of the n'th child requested
     * @return        the element matching the index offset, or null
     */
    private Element getChild(Element parent, int index) {
        Element child = (Element)parent.getHead();
        for (int i=0; i < (index - 1); i++) {
            child = (Element)child.getNext();
        }
        return child;
    }

    /**
     * Helper method to return a needed private constant.
     */
    private Object getMultipleChildrenConstant() throws Exception {
        return PrivateAccessor.getField(AbstractDivRemover.class,
                "MULTIPLE_CHILDREN_FOUND");
    }
}


