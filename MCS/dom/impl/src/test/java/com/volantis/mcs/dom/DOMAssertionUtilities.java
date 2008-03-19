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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom;

import junit.framework.Assert;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Provides support for making assertions about DOM-related items. Separated
 * out when {@link com.volantis.mcs.dom.debug.DOMUtilities} was moved, so that
 * assertion / test-related functionality isn't in core product.
 *
 */
public class DOMAssertionUtilities {

    // @todo these compete with similar things in DOMProtocolTestAbstract
    // refactor?

    /**
     * Assert that the node provided is an {@link Element} of the name
     * provided.
     *
     * @param expectedName expected name of the {@link Element}.
     * @param actualNode actual {@link Node} to check.
     * @return {@link Node} cast to an {@link Element} for convenience.
     */
    public static Element assertElement(String expectedName, Node actualNode) {
        Assert.assertTrue("Node instanceof Element: " + actualNode,
                actualNode instanceof Element);
        Element element = (Element) actualNode;
        TestCaseAbstract.assertEquals((Object)expectedName, element.getName());
        return element;
    }

    /**
     * Assert that the node provided is an {@link Element} of the name
     * provided.
     *
     * @param expectedContent expected content of the {@link Text}
     * @param actualNode actual {@link Node} to check.
     * @return {@link Node} cast to a {@link Text} for convenience.
     */
    public static Text assertText(String expectedContent, Node actualNode) {
        Assert.assertTrue(actualNode instanceof Text);
        Text text = (Text) actualNode;
        TestCaseAbstract.assertEquals((Object)expectedContent,
                                      stringValue(text));
        return text;
    }

    /**
     * Assert that the node provided has an attribute of the name provided
     * with the expected value provided.
     *
     * @param name name of the element's attribute.
     * @param expectedValue expected value of the attribute
     * @param actualElement element to query for actual attribute value.
     */
    public static void assertAttributeEquals(String name, String expectedValue,
                                             Element actualElement) {
        String actualValue = actualElement.getAttributeValue(name);
        Assert.assertEquals("attribute " + name + " mismatch, expected " +
                expectedValue + ", actual " + actualValue,
                expectedValue, actualValue);
    }

    /**
     * Assert that the node provided has no child nodes.
     *
     * @param element {@link Element} to check.
     */
    public static void assertNoChildren(Element element) {
        Assert.assertNull("element " + element + " has unexpected child " +
                element.getHead(), element.getHead());
    }

    /**
     * Assert that the node provided has no peer nodes.
     *
     * @param node {@link Node} to check.
     */
    public static void assertNoPeers(Node node) {
        Assert.assertNull("node " + node + " has unexpected previous " +
                node.getPrevious(), node.getPrevious());
        Assert.assertNull("node " + node + " has unexpected next " +
                node.getNext(), node.getNext());
    }

    /**
     * Obtain the value of the {@link com.volantis.mcs.dom.Text} node as a {@link java.lang.String}.
     *
     * @param text the {@link com.volantis.mcs.dom.Text} to get the value of.
     * @return the {@link java.lang.String} value of text.
     */
    private static String stringValue(Text text) {
        return new String(text.getContents(), 0, text.getLength());
    }

}
