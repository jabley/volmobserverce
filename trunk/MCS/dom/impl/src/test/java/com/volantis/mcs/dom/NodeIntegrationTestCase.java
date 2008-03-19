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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import com.volantis.mcs.dom.debug.DOMUtilities;

/**
 * Test cases for {@link Node}.
 */
public class NodeIntegrationTestCase
        extends MockTestCaseAbstract {

    /**
     * Ensure {@link Node#replaceWith(NodeSequence)} method works correctly
     * with empty sequence
     */
    public void testReplaceWithEmpty() throws Exception {

        // Create document to modify.
        Document document = DOMUtilities.read("<root><marker/></root>");
        Element root = (Element) document.getRootElement();
        assertEquals("root", root.getName());
        Element marker = (Element) root.getHead();
        assertEquals("marker", marker.getName());

        // Create sequence to replace.
        NodeSequence insert = DOMUtilities.readSequence("");

        // Replace the node with the sequence.
        marker.replaceWith(insert);

        String result = DOMUtilities.toString(document);
        assertEquals("<root/>", result);
    }

    /**
     * Ensure {@link Node#replaceWith(NodeSequence)} method works correctly
     * with a non emtpy sequence.
     */
    public void testReplaceWithNonEmpty() throws Exception {

        // Create document to modify.
        Document document = DOMUtilities.read("<root><marker/></root>");
        Element root = (Element) document.getRootElement();
        assertEquals("root", root.getName());
        Element marker = (Element) root.getHead();
        assertEquals("marker", marker.getName());

        // Create sequence to replace.
        NodeSequence insert = DOMUtilities.readSequence("Text<element/>");

        // Replace the node with the sequence.
        marker.replaceWith(insert);

        String result = DOMUtilities.toString(document);
        assertEquals("<root>Text<element/></root>", result);
    }
}
