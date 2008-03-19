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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for the {@link AbstractMenuRenderer} class
 */
public abstract class AbstractMenuRendererTestAbstract extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Instance of the class that is being tested
     */
    protected AbstractMenuRenderer renderer;

    /**
     * Common menuAttributes that are used by various tests
     */
    protected MenuAttributes menuAttributes;

    /**
     * A DOMOutputBuffer that the renderer will write to
     */
    protected DOMOutputBuffer outputBuffer;


    /**
     * A DOMProtocol that is need in order to initialise the DOMOutputBuffer
     */
    protected DOMProtocol protocol;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        protocol = createProtocol();
        outputBuffer = createDOMOutputBuffer();
        menuAttributes = createMenuAttributes();
        outputBuffer.initialise();

        // it is important that this method is invoked last. Implementations
        // will probably want access to the other members that have been
        // initialised
        renderer = createTestableMenuRenderer();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        renderer = null;
        outputBuffer = null;
        menuAttributes = null;
        protocol = null;
    }


    /**
     * Factory method that creates an instance of the renderer that is
     * being tested.
     * @return the <code>AbstractMenuRenderer</code> that is to be tested
     */
    protected abstract AbstractMenuRenderer createTestableMenuRenderer();

    /**
     * Factory method for creating a <code>DOMOutputBuffer</code>
     * @return a DOMOutputBuffer instance
     */
    protected DOMOutputBuffer createDOMOutputBuffer() {
        return new TestDOMOutputBuffer();
    }

    /**
     * Factory method for creating a <code>DOMProtocol</code>
     * @return a DOMProtocol instance
     */
    protected DOMProtocol createProtocol() {

        InternalDevice internalDevice = InternalDeviceTestHelper.createTestDevice();

        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                internalDevice);
        return protocol;
    }

    /**
     * Factory method that creates the <code>MenuAttributes</code> that the
     * various tests use.
     * @return a MenuAttributes instance
     */
    protected MenuAttributes createMenuAttributes() {
        MenuAttributes atts = new MenuAttributes();
        atts.setTitle("TestTitle");
        atts.addItem(createMenuItem("http://foo.com", "foo", "fooTitle"));
        atts.addItem(createMenuItem(
                "http://foobar.com", "foobar", "foobarTitle"));
        atts.setPane(new Pane(new CanvasLayout()));
        final StylingFactory factory = StylingFactory.getDefaultInstance();
        atts.setStyles(factory.createStyles(factory.createPropertyValues(
                StylePropertyDetails.getDefinitions())));
        MenuItemGroupAttributes group = createMenuGroup();
        group.addItem(createMenuItem("http://bar.com", "bar", "barTitle"));
        group.addItem(createMenuItem("http://fred.com", "fred", "fredTitle"));
        atts.addGroup(group);
        return atts;
    }

    /**
     * Helper method that factors a <code>MenuItem</code> instance.
     * @param href the href attribute value
     * @param text the textual label for the menu item
     * @param title the title attribute value
     * @return a MenuItem instance
     */
    protected MenuItem createMenuItem(String href, String text, String title) {
        MenuItem item = new MenuItem();
        item.setHref(href);
        item.setText(text);
        item.setTitle(title);
        return item;
    }

    /**
     * Helper method that factors a <code>MenuItemGroupAttributes</code>
     * instance
     * @return a MenuItemGroupAttributes instance
     */
    protected MenuItemGroupAttributes createMenuGroup() {
        return new MenuItemGroupAttributes();
    }

    /**
     * Converst the outputBuffer member to a string
     * @return the string representation of the outputBuffer member
     * @throws Exception if an error occurs
     */
    protected String getOutputBufferAsString() throws Exception {
        StyledDOMTester tester = new StyledDOMTester();
        return tester.render(outputBuffer.getRoot());
    }

    /**
     * tests both the {@link AbstractMenuRenderer#openMenu} and
     * {@link AbstractMenuRenderer#closeMenu} methods on the
     * renderer. Concrete AbstractMenuRenderer test cases
     * should override the {@link #getExpectedOpenCloseMenuOutput}
     * method in order to provide the expected output string
     * @throws Exception if an error occurs
     */
    public void testOpenCloseMenu() throws Exception {
        renderer.openMenu(outputBuffer, menuAttributes);
        renderer.closeMenu(outputBuffer, menuAttributes);

        assertEquals("openMenu & closeMenu produced unexpected markup",
                     getExpectedOpenCloseMenuOutput(),
                     getOutputBufferAsString());
    }

    /**
     * Sublclasseses should implement this method in order to provide the
     * expected output of the {@link #testOpenCloseMenu} test.
     * @return the expected string
     */
    protected abstract String getExpectedOpenCloseMenuOutput();

    /**
     * tests both the {@link AbstractMenuRenderer#openMenuGroup} and
     * {@link AbstractMenuRenderer#closeMenuGroup} methods on the
     * renderer. Concrete AbstractMenuRenderer test cases
     * should override the {@link #getExpectedOpenCloseMenuGroupOutput}
     * method in order to provide the expected output string
     * @throws Exception if an error occurs
     */
    public void testOpenCloseMenuGroup() throws Exception {
        MenuItemGroupAttributes groupAtts = createMenuGroup();
        renderer.openMenuGroup(outputBuffer, groupAtts);
        renderer.closeMenuGroup(outputBuffer, groupAtts);

        assertEquals("Menu Groups should not be written out",
                     getExpectedOpenCloseMenuGroupOutput(),
                     getOutputBufferAsString());
    }

    /**
     * Sublclasseses should implement this method in order to provide the
     * expected output of the {@link #testOpenCloseMenuGroup} test.
     * @return the expected string
     */
    protected abstract String getExpectedOpenCloseMenuGroupOutput();

    /**
     * tests the {@link AbstractMenuRenderer#writeMenuItem}
     * methods on the renderer. Concrete AbstractMenuRenderer test cases
     * should override the {@link #getExpectedWriteMenuItemOutput}
     * method in order to provide the expected output string
     * @throws Exception if an error occurs
     */
    public void testWriteMenuItem() throws Exception {
        MenuItem item = createMenuItem("http://foobar.com",
                                       "foobar",
                                       "foobarTitle");
        renderer.writeMenuItem(outputBuffer, item);

        assertEquals("Unexpected output for menu item",
                     getExpectedWriteMenuItemOutput(),
                     getOutputBufferAsString());
    }

    /**
     * Sublclasseses should implement this method in order to provide the
     * expected output of the {@link #testWriteMenuItem} test.
     * @return the expected string
     */
    protected abstract String getExpectedWriteMenuItemOutput();

    /**
     * tests the {@link AbstractMenuRenderer#renderMenu}
     * methods on the renderer. Concrete AbstractMenuRenderer test cases
     * should override the {@link #getExpectedRenderMenuOutput}
     * method in order to provide the expected output string
     * @throws Exception if an error occurs
     */
    public void testRenderMenu() throws Exception {
        renderer.renderMenu(menuAttributes);

        assertEquals("renderMenu produced unexpected markup",
                     getExpectedRenderMenuOutput(),
                     getOutputBufferAsString());
    }

    /**
     * Sublclasseses should implement this method in order to provide the
     * expected output of the {@link #testRenderMenu} test.
     * @return the expected string
     */
    protected abstract String getExpectedRenderMenuOutput();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 17-Sep-03	1394/1	doug	VBM:2003090902 added support for openwave numeric shortcut menus

 ===========================================================================
*/
