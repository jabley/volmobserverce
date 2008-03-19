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

import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.shared.MenuEntityCreation;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.separator.SeparatorArbitrator;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.separator.shared.UseDeferredSeparatorArbitrator;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;

/**
 * This class provides a base class for all subclass tests for classes
 * implementing WrappedMenuItemRenderer, either directly or indirectly.
 * <p>
 * There are several convenience methods for setting up buffers etc. that
 * are available to subclasses for use, or to override if specific behaviour
 * is required.
 * </p>
 * <p>
 * The render method is tested in this class and it uses the other methods
 * to retrieve a renderer, locator, item, and expected values.  Any abstract
 * methods that are implemented or any overridden methods may be used by this
 * code.
 * </p>
 */
public abstract class WrappedMenuItemRendererTestAbstract
        extends MenuItemBracketingRendererTestAbstract {

    /**
     * Reference to a creator that provides a means of construcitng various
     * menu items and their associated elements.
     */
    protected MenuEntityCreation creator;

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        creator = new MenuEntityCreation();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        creator = null;
        super.tearDown();
    }

    /**
     * This tests the render method.  In doing so (based on the abstract
     * implementation) this will test the open, close, and content methods.
     */
    public void testRender() throws Exception {
        // Create the test renderer
        MenuItemBracketingRenderer renderer = createTestRenderer();

        MenuBuffer buffer = createMenuBuffer();
        MenuBufferLocator locator = createLocator(buffer);
        MenuItem item = createMenuItem();

        String output = getRenderOutputAsString(renderer, item);
        String expected = DOMUtilities.
                             provideDOMNormalizedString(getExpectedString());

        assertNotNull("There should be output", output);
        assertEquals("Output does not match", expected, output);
    }

    /**
     * Create an instance of a menu item renderer to test.  This is used to
     * create the object under test in these hierarchical tests.
     *
     * @return An initialised instance of a menu item renderer.
     */
    public abstract MenuItemBracketingRenderer createTestRenderer();

    /**
     * Create an instance of a menu item.  This is used (amongst other possible
     * uses) when rendering output.
     *
     * @return An initialised instance of a menu item.
     */
    protected abstract MenuItem createMenuItem();

    /**
     * Provide access to the expected string to be generated from rendering
     * the menu item available from {@link #createMenuItem} when rendered
     * using {@link #createTestRenderer}.  <strong>This is required to be
     * valid and accurate in order for the render test to succeed.  Failure
     * in the rendering test will cause the testsuite to fail.</strong>
     *
     * @return A string containing an exact match of the output expected from
     *         rendering the menu item provided with the renderer provided.
     */
    protected abstract String getExpectedString();

    /**
     * A conventience method that creates a locator for buffers for outputting
     * menu items.  It takes a buffer to use and then wraps this in the
     * returned locator object.
     *
     * @param buffer The buffer to wrap into the locator
     * @return       An new instance of a menu buffer locator that returns
     *               thr provided buffer when a buffer is requested from it.
     */
    protected MenuBufferLocator createLocator(final MenuBuffer buffer) {
        return new MenuBufferLocator() {
            public MenuBuffer getMenuBuffer(MenuEntry entry) {
                return buffer;
            }
        };
    }

    /**
     * A convenience method that creates an output buffer for use in the
     * tests.
     *
     * @return A new instance of a suitable output buffer.
     */
    protected OutputBuffer createOutputBuffer() {
        return new TestDOMOutputBuffer();
    }

    /**
     * A convenience method that creates a menu buffer for use in the tests.
     *
     * @return A new instance of a suitable menu buffer.
     */
    protected MenuBuffer createMenuBuffer() {
        return new ConcreteMenuBuffer(createOutputBuffer(),
                                      SeparatorRenderer.NULL);
    }

    /**
     * A utility method that takes a menu buffer and returns a string
     * representation of the contents of that buffer.  The string is
     * produced using a conversion so that the generated output is correctly
     * marked up as represented by the buffer.
     *
     * @param buffer The buffer to be converted to a string
     * @return       A string representation of the contents of the buffer
     * @throws IOException If there is a problem creating the output string
     */
    protected String bufferToString(MenuBuffer buffer) throws IOException {
        TestDOMOutputBuffer outputBuffer =
                (TestDOMOutputBuffer) buffer.getOutputBuffer();
        return DOMUtilities.toString(outputBuffer.getRoot());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/2	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	3999/2	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 27-Apr-04	4025/1	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 20-Apr-04	3715/3	claire	VBM:2004040201 Improving WML menu item renderers

 16-Apr-04	3715/1	claire	VBM:2004040201 Enhanced Menu: WML Menu Item Renderers

 ===========================================================================
*/
