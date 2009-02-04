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

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.shared.iteration.IterationAction;

/**
 * Test cases for {@link Element}.
 */
public class ElementIntegrationTestCase
        extends DOMTestCaseAbstract {

    /**
     * Ensure {@link Element#addHead(Node)} works.
     */
    public void testAddHead() throws Exception {

        Element root = factory.createElement("root");
        Text text1 = factory.createText("text1");
        Element element1 = factory.createElement("element1");

        root.addHead(text1);

        assertEquals("<root>text1</root>",
                DOMUtilities.toString(root));

        root.addHead(element1);

        assertEquals("<root><element1/>text1</root>",
                DOMUtilities.toString(root));
    }

    /**
     * Ensure {@link Element#addTail(Node)} works.
     */
    public void testAddTail() throws Exception {

        Element root = factory.createElement("root");
        Text text1 = factory.createText("text1");
        Element element1 = factory.createElement("element1");

        root.addTail(text1);

        assertEquals("<root>text1</root>",
                DOMUtilities.toString(root));

        root.addTail(element1);

        assertEquals("<root>text1<element1/></root>",
                DOMUtilities.toString(root));
    }

    /**
     * Ensure {@link Element#removeChildren()} works on an empty element.
     */
    public void testRemoveChildrenEmpty() throws Exception {


        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final NodeIterateeMock iterateeMock =
                new NodeIterateeMock("iterateeMock", expectations);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Element root = factory.createElement("root");

        assertEquals("<root/>",
                DOMUtilities.toString(root));

        NodeSequence sequence = root.removeChildren();
        sequence.forEach(iterateeMock);

        assertTrue(root.isEmpty());
    }

    /**
     * Ensure {@link Element#removeChildren()} works on an empty element.
     */
    public void testRemoveChildrenNonEmpty() throws Exception {


        Text text1 = factory.createText("text1");

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final NodeIterateeMock iterateeMock =
                new NodeIterateeMock("iterateeMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        iterateeMock.expects.next(text1).returns(IterationAction.CONTINUE);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Element root = factory.createElement("root");
        root.addTail(text1);

        assertFalse(root.isEmpty());

        assertEquals("<root>text1</root>",
                DOMUtilities.toString(root));

        NodeSequence sequence = root.removeChildren();
        sequence.forEach(iterateeMock);

        assertTrue(root.isEmpty());
    }
}
