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

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.DOMFactoryMock;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.ElementMock;
import com.volantis.mcs.dom.NodeSequence;
import com.volantis.mcs.dom.TextMock;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.styling.StylesMock;


/**
 * Test the DOMOutputBuffer.
 *
 */
public class DOMOutputBufferTestCase extends OutputBufferTestAbstract {

    DOMOutputBuffer buffer;
    DOMFactoryMock factoryMock;
    ElementMock element;
    ElementMock emptyElement;    
    MCSAttributesMock attributes;
    com.volantis.styling.StylesMock styles;

    public void setUp() throws Exception {
        super.setUp();

        // initialise mock objects
        factoryMock = new DOMFactoryMock("factoryMock", expectations);
        element = new ElementMock("element", expectations);
        emptyElement = new ElementMock("emptyElement", expectations);
        styles = new StylesMock("styles", expectations);
    }    
    
    /**
     * This method sets up the expectations that are required to run the
     * {@link #testAddContentsEmptiesSource} test.
     *
     * @param textNode mock of the textNode that will be used during the test
     * @param textToWrite the text that should be written to the buffer
     */
    void setUpAddContentsEmptiesSourceExpectations(
            TextMock textNode, String textToWrite) {

            factoryMock.expects.createElement().returns(emptyElement);
            emptyElement.expects.getHead().returns(null);
            factoryMock.expects.createElement().returns(element);
            element.expects.getHead().returns(null);

            element.expects.getTail().returns(null);
            factoryMock.expects.createText().returns(textNode);
            textNode.expects.setEncoded(false);
            element.expects.addTail(textNode);
            for (int i = 0; i < textToWrite.length(); i++) {
                textNode.expects.append(textToWrite.charAt(i));
            }
            element.expects.getHead().returns(textNode);

            element.expects.getTail().returns(textNode);
            element.expects.addChildrenToTail(emptyElement);
            element.expects.getHead().returns(null);

//        final NodeSequenceMock nodeSequenceMock =
//                new NodeSequenceMock("nodeSequenceMock", expectations);
//
//        element.expects.removeChildren().returns(nodeSequenceMock);
//        emptyElement.expects.insertAfter(nodeSequenceMock, null);

            emptyElement.expects.getHead().returns(textNode);
    }

    /**
     * Create a DOMOutputBuffer to test.
     * @return The new DOMOutputBuffer to test.
     */
    public OutputBuffer createOutputBuffer() {
        buffer = new DOMOutputBuffer(factoryMock);
        buffer.initialise();
        return buffer;
    }

    /**
     * Tests that the addStyledElement method returns an element with the
     * ClassifiedStylePropertiesContainer set to that of the MCSAttributes
     * passed in.
     */
    public void testAddElementWithStyle() {
        // set expectations
        factoryMock.expects.createElement().returns(emptyElement);
        factoryMock.fuzzy.createStyledElement(attributes).returns(element);
        element.expects.setName("test");
        emptyElement.expects.addTail(element);
        element.expects.getStyles().returns(styles);

        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.addStyledElement("test", attributes);
        assertEquals(element.getStyles(), styles);
    }

    /**
     * Tests that the addElement method returns an element with a null
     * ClassifiedStylePropertiesContainer.
     */
    public void testAddElementWithoutStyle() {
        // set expectations
        factoryMock.expects.createElement().returns(emptyElement);
        factoryMock.expects.createElement("test").returns(element);
        emptyElement.expects.addTail(element);
        element.expects.getStyles().returns(null);

        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.addElement("test");
        assertNull(element.getStyles());
    }

    /**
     * Tests that the openStyledElement method returns an element with the
     * ClassifiedStylePropertiesContainer set to that of the MCSAttributes
     * passed in.
     */
    public void testOpenElementWithStyle() {
        // set expectations
        factoryMock.expects.createElement().returns(emptyElement);

        factoryMock.fuzzy.createStyledElement(attributes).returns(element);
        element.expects.setName("test");
        emptyElement.expects.addTail(element);
        element.expects.getStyles().returns(styles);

        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.openStyledElement("test", attributes);
        assertEquals(element.getStyles(), styles);
    }

    /**
     * Tests that the openElement method returns an element with a null
     * ClassifiedStylePropertiesContainer.
     */
    public void testOpenElementWithoutStyle() {
        // set expectations
        factoryMock.expects.createElement().returns(emptyElement);

        factoryMock.expects.createElement("test").returns(element);
        emptyElement.expects.addTail(element);
        element.expects.getStyles().returns(null);

        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.openElement("test");
        assertNull(element.getStyles());
    }

    /**
     * Tests that the allocateStyledElement method returns an element with the
     * ClassifiedStylePropertiesContainer set to that of the MCSAttributes
     * passed in.
     *
     * todo XDIME-CP fix this
     */
    public void notestAllocateElementWithStyle() {
        // set expectations
        factoryMock.expects.createElement().returns(emptyElement);
        emptyElement.expects.setName(null);

        factoryMock.fuzzy.createStyledElement(attributes).
                returns(element);
        element.expects.setName("test");
        element.expects.getStyles().returns(styles);

        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.allocateStyledElement("test", styles);
        assertEquals(element.getStyles(), styles);
    }

    /**
     * Tests that the allocateElement method returns an element with a null
     * ClassifiedStylePropertiesContainer.
     */
    public void testAllocateElementWithoutStyle() {
        // set expectations
        factoryMock.expects.createElement().returns(emptyElement);

        factoryMock.expects.createElement("test").returns(element);
        element.expects.getStyles().returns(null);

        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.allocateElement("test");
        assertNull(element.getStyles());
    }

    /**
     * Ensure that adding white space does not create an empty text node.
     */
    public void testAddWhitespaceDoesNotCreateEmptyTextNode()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DOMOutputBuffer buffer = new DOMOutputBuffer();

        assertTrue(buffer.isEmpty());

        buffer.writeText("    ");
        assertTrue(buffer.isEmpty());
    }

    /**
     * Ensure that adding text makes an empty buffer not empty.
     */
    public void addTextMakesNonEmpty()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DOMOutputBuffer buffer = new DOMOutputBuffer();

        assertTrue(buffer.isEmpty());

        buffer.writeText("some text");
        assertFalse(buffer.isEmpty());
    }

    /**
     * Test that adding some preformatted white space makes an empty buffer
     * not empty.
     */
    public void testAddingPreformattedWhitespaceMakesNonEmpty()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.setElementIsPreFormatted(true);

        assertTrue(buffer.isEmpty());

        buffer.writeText("   ");
        assertFalse(buffer.isEmpty());
    }

    /**
     * Test that adding an element makes an empty buffer not empty.
     */
    public void testAddingElementMakesNonEmpty()
            throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DOMOutputBuffer buffer = new DOMOutputBuffer();

        assertTrue(buffer.isEmpty());

        buffer.addElement("element");
        assertFalse(buffer.isEmpty());
    }

    /**
     * Ensure that adding a node sequence updates the position correctly.
     */
    public void testAddNodeSequence() throws Exception {

        DOMOutputBuffer buffer = new DOMOutputBuffer();

        // Add an element.
        buffer.addElement("a");

        // Add a sequence.
        NodeSequence nodes = DOMUtilities.readSequence("<b/><c/>");
        buffer.addNodeSequence(nodes);

        // Add an element.
        buffer.addElement("d");
        String actual = DOMUtilities.toString(buffer.getRoot());
        assertEquals("<a/><b/><c/><d/>", actual);
    }

    /**
     * Ensure that adding the contents from one buffer to another empties the
     * source buffer.
     */ 
    public void testAddContentsEmptiesSource() throws Exception {

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.addNodeSequence(DOMUtilities.readSequence(
                "<a><b>B</b>C<d>D</d></a><e/>"));

        DOMOutputBuffer target = new DOMOutputBuffer();

        assertFalse(buffer.isEmpty());
        assertTrue(target.isEmpty());

        target.openElement("r");
        target.transferContentsFrom(buffer);
        target.closeElement("r");

        assertTrue(buffer.isEmpty());
        assertFalse(target.isEmpty());
        String actual;
        actual = DOMUtilities.toString(buffer.getRoot());
        assertEquals("Source buffer", "", actual);
        actual = DOMUtilities.toString(target.getRoot());
        assertEquals("Target buffer", "<r><a><b>B</b>C<d>D</d></a><e/></r>",
                actual);
    }
 }

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8483/6	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/2	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/5	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 09-Jun-05	8665/3	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
