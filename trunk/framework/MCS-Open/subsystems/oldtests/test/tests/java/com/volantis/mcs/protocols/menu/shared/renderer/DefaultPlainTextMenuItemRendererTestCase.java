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

package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.model.MenuText;
import com.volantis.mcs.protocols.menu.shared.MenuEntityCreation;


/**
 * This tests the operation of the default plain text renderer.  The output is
 * simplistic as the menu renderers are split into small isolated pieces of
 * functionality.  The plain text renderer is one of the most basic of these.
 */
public class DefaultPlainTextMenuItemRendererTestCase
        extends MenuItemComponentRendererTestAbstract {

    /**
     * A reference to an object that allows for easy creation of various
     * menu entities for use in tests.
     */
    private MenuEntityCreation entity;


    /**
     * Creates a new instance of this test class.
     */
    public DefaultPlainTextMenuItemRendererTestCase() {
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        entity = new MenuEntityCreation();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        entity = null;
    }

    /**
     * This tests the render method to ensure the correct output is generated.
     */
    public void testRender() throws Exception {
        String testTextString = "test menu item";

        // Create the test DOM for the text buffer
        TestDOMOutputBuffer textBuffer = new TestDOMOutputBuffer();
        Element b = textBuffer.allocateElement("b");
        Text textElement = domFactory.createText();
        textElement.append(testTextString);
        b.addTail(textElement);
        textBuffer.addElement(b);

        // Create the test text, label, and then the menu item
        MenuText text = entity.createTestMenuText(textBuffer);
        MenuLabel label = entity.createTestMenuLabel(text);
        MenuItem item = entity.createTestMenuItem(label);

        // Create test renderer object
        DefaultPlainTextMenuItemRenderer renderer =
                new DefaultPlainTextMenuItemRenderer();

        // Create expected
        String required = "<b>" + testTextString + "</b>";
        String expected = DOMUtilities.provideDOMNormalizedString(required);

        // Create test buffer locator
        String actual = getRenderOutputAsString(renderer, item);
        assertNotNull("The actual string should exist", actual);

        // Compare state and expected
        assertEquals("Strings should match", expected, actual);
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 20-Apr-04	3715/6	claire	VBM:2004040201 Improving WML menu item renderers

 16-Apr-04	3715/1	claire	VBM:2004040201 Enhanced Menu: WML Menu Item Renderers

 ===========================================================================
*/
