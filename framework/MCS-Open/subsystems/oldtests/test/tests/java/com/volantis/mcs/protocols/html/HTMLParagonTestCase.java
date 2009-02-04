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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/HTMLParagonTestCase.java,v 1.1 2002/11/19 15:14:57 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Nov-02    Byron           VBM:2002110517 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * This unit tests the HTMLParagon protocol
 *
 * @author Byron Wild
 */
public class HTMLParagonTestCase extends TestCase {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    private HTMLParagon protocol;

    /**
     * Create a new instance of this testcase.
     * @param name The name of the testcase.
     */
    public HTMLParagonTestCase(String name) {
        super(name);
    }

    /**
     * This method tests the constructors for
     * the com.volantis.mcs.protocols.html.HTMLParagon class.
     */
    public void notestConstructors() {
        //
        // Test public HTMLParagon() constructor
        //
        Assert.fail("public HTMLParagon() not tested.");
    }

    /**
     * Setup the unit test framework by creating some classes ensuring that each
     * test case starts off with a clean slate to work from
     */
    public void setUp() {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        ProtocolBuilder builder = new ProtocolBuilder();
        protocol = (HTMLParagon) builder.build(
                new ProtocolRegistry.HTMLParagonFactory(), internalDevice);
    }

    /**
     * Tear down the unit test framework by ensuring garbage collection at some
     * later stage.
     */
    public void tearDown() {
        protocol = null;
    }

    /**
     * Test the creation of action element's specific to this protocol.
     * The test actually tests whether the 'input' tag is correctly
     * transformed into a 'button' tag.
     */
    public void testCreateActionElement() {

        assertNotNull("Protocol is null", protocol);

        Element element = domFactory.createElement();
        element.setName("my Name");

        DOMOutputBuffer dom = new DOMOutputBuffer();
        dom.initialise();

        // Test that the item is correctly transformed
        String actionType = "submit";
        String name = "Dumbledore";
        String caption = "This is the caption";
        String src = "/this/image.jpg";
        Element newElement = protocol.createActionElement(dom, actionType, name, 
                caption, src, null);
        checkResults(newElement, actionType, name, caption, src);

        // Test that the item is only transformed when necessary
        actionType = "reset";
        name = "Dumbledore";
        caption = "This is the caption";
        src = "/this/image.jpg";
        newElement = protocol.createActionElement(dom, actionType, name,
                                                  caption, src, null);
        checkResults(newElement, actionType, name, caption, src);

        // Test that the item is only transformed when necessary
        actionType = "image";
        name = "Dumbledore's mugshot";
        caption = "This is the caption";
        src = "/this/dumbledore.jpg";
        newElement = protocol.createActionElement(dom, actionType, name,
                                                  caption, src, null);
        checkResults(newElement, actionType, name, caption, src);
    }

    /**
     * Utility method to check that the results match the stated parameters.
     * @param element the actual element
     * @param actionType the type of action (submit, input, perform, etc)
     * @param name the name of the attribute
     * @param caption the caption for this tag
     * @param src the url to the image source
     */
    private void checkResults(Element element,
                              String actionType,
                              String name,
                              String caption,
                              String src) {

        boolean elementShouldBeTransformed = "submit".equals(actionType) ||
                "button".equals(actionType);

        String elementName = elementShouldBeTransformed ? "button" : "input";
        assertEquals("Element's name incorrect:",
                     elementName,
                     element.getName());

        assertEquals("Element's type attribute incorrect:",
                     actionType,
                     element.getAttributeValue("type"));

        assertEquals("Element's name attribute incorrect:",
                     name,
                     element.getAttributeValue("name"));

        if (elementShouldBeTransformed) {
            Node text = element.getHead();
            assertTrue(text instanceof Text);

            String str = (new String(((Text)text).getContents())).trim();
            assertEquals(str, caption);
        }
        assertEquals("Element's value attribute incorrect:",
                     caption,
                     element.getAttributeValue("value"));

        if (elementShouldBeTransformed) {
            src = null;
        }
        assertEquals("Element's src attribute incorrect:",
                     src,
                     element.getAttributeValue("src"));

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
