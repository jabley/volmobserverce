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
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBufferFactory;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.assets.implementation.LiteralImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.css.DefaultStylePropertyResolver;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.builder.ConcreteMenuModelBuilder;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;

/**
 * Tests {@link DefaultMenuItemRendererSelector}.
 */
public class DefaultMenuItemRendererSelectorTestCase
    extends TestCaseAbstract {

    /**
     * Provides an output buffer that can be used in the testing.
     *
     * @return an output buffer for the testing
     */
    protected OutputBuffer createOutputBuffer() {
        return new TestDOMOutputBuffer();
    }

    /**
     * Creates a menu buffer that can be used in the testing. Calls
     * {@link #createOutputBuffer}.
     *
     * @return a menu buffer for the testing
     */
    protected MenuBuffer createMenuBuffer() {
        OutputBuffer outputBuffer = createOutputBuffer();
        return new ConcreteMenuBuffer(outputBuffer, SeparatorRenderer.NULL);
    }

    /**
     * Creates a menu item renderer factory that can be used in the testing.
     *
     * @return a menu item renderer factory for the testing
     */
    protected MenuItemRendererFactory createFactory() {
        TestDOMOutputBufferFactory factory
                = new TestDOMOutputBufferFactory();
        return new TestMenuItemRendererFactory(factory);
    }

    /**
     * Creates a menu item renderer selector that can be used in the testing.
     * Calls {@link #createFactory} and {@link #createSeparatorSelector}.
     *
     * @return a menu item renderer selector for the testing
     */
    protected MenuItemRendererSelector createSelector() {
        return new DefaultMenuItemRendererSelector(
                createFactory(),
                createSeparatorSelector());
    }

    /**
     * Creates a menu separator renderer selector that can be used in the
     * testing. Calls {@link #createSeparatorFactory} and {@link
     * #createAssetResolver}.
     *
     * @return a menu separator renderer selector for the testing
     */
    protected MenuSeparatorRendererSelector createSeparatorSelector() {
        return new DefaultMenuSeparatorRendererSelector(
                createSeparatorFactory(), createAssetResolver(),
                new DefaultStylePropertyResolver(null, null));
    }

    /**
     * Creates an asset resolver that can be used in the testing.
     *
     * @return an asset resolver for the testing
     */
    protected AssetResolver createAssetResolver() {
        return new AssetResolverMock("assetResolverMock", expectations);
    }

    /**
     * Creates a menu separator renderer factory that can be used in the
     * testing.
     *
     * @return a menu separator renderer factory for the testing
     */
    protected MenuSeparatorRendererFactory createSeparatorFactory() {
        return new TestMenuSeparatorRendererFactory();
    }

    /**
     * Creates a menu buffer locator that can be used in the testing.
     *
     * @param buffer a menu buffer instance to be returned by the locator
     * @return a menu buffer locator for the testing
     */
    protected MenuBufferLocator createMenuBufferLocator(
            final MenuBuffer buffer) {
        return new MenuBufferLocator() {
            public MenuBuffer getMenuBuffer(MenuEntry entry) {
                return buffer;
            }
        };
    }

    /**
     * A utility method to return a string of the markup in the given menu
     * buffer's associated output buffer.
     *
     * @param buffer the buffer for which the markup string is required
     * @return a string of the markup from the given buffer's output buffer
     */
    protected String toString(MenuBuffer buffer)
        throws IOException {

        TestDOMOutputBuffer outputBuffer =
                (TestDOMOutputBuffer) buffer.getOutputBuffer();
        return DOMUtilities.toString(outputBuffer.getRoot());
    }

    /**
     * A utility method to create a valid menu where the menu has the specified
     * style property values.
     *
     * @param menuStyles the styles for the menu
     * @return a valid menu with the specified style property values
     */
    protected Menu createMenu(Styles menuStyles)
        throws Exception {

        MenuModelBuilder builder = new ConcreteMenuModelBuilder();
        builder.startMenu();
        builder.setElementDetails("menu", "menu1", menuStyles);
        builder.startMenuItem();
        builder.setElementDetails("menuitem", "menuitem1",
                StylesBuilder.getInitialValueStyles());
        builder.startLabel();
        builder.startIcon();
        builder.setNormalImageURL(new LiteralImageAssetReference("normal.gif"));
        builder.setOverImageURL(new LiteralImageAssetReference("over.gif"));
        builder.endIcon();
        builder.startText();

        OutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.writeText("text");

        builder.setText(buffer);
        builder.endText();
        builder.endLabel();
        builder.setHref(new LiteralLinkAssetReference("href.xml"));
        builder.endMenuItem();
        builder.endMenu();

        return builder.getCompletedMenuModel();
    }

    /**
     * Tests that the right menu item renderer for the specified configuration.
     */
    public void testPlainImage()
        throws Exception {

        MenuItemRendererSelector selector = createSelector();

        Menu menu = createMenu(StylesBuilder.getCompleteStyles(
                "mcs-menu-image-style: plain; " +
                "mcs-menu-text-style: none"));

        MenuBuffer buffer = createMenuBuffer();

        MenuBufferLocator locator = createMenuBufferLocator(buffer);

        MenuItemRenderer renderer = selector.selectMenuItemRenderer(menu);
        assertNotNull("No renderer selected", renderer);

        renderer.render(locator, (MenuItem) menu.get(0));

        String string = toString(buffer);
        //System.out.println("String: " + string);
        assertEquals("Output does not match",
                     "<link" +
//                     " class=\"menu-item-class\"" +
                     " href=\"href.xml\">" +
                     "<plain-image src=\"normal.gif\"/>" +
                     "</link>", string);
    }

    /**
     * Tests that the right menu item renderer for the specified configuration.
     */
    public void testRolloverImage()
        throws Exception {

        MenuItemRendererSelector selector = createSelector();

        Menu menu = createMenu(StylesBuilder.getCompleteStyles(
                "mcs-menu-image-style: rollover; " +
                "mcs-menu-text-style: none"));

        MenuBuffer buffer = createMenuBuffer();

        MenuBufferLocator locator = createMenuBufferLocator(buffer);

        MenuItemRenderer renderer = selector.selectMenuItemRenderer(menu);
        assertNotNull("No renderer selected", renderer);

        renderer.render(locator, (MenuItem) menu.get(0));

        String string = toString(buffer);
        //System.out.println("String: " + string);
        assertEquals("Output does not match",
                     "<link" +
//                     " class=\"menu-item-class\"" +
                     " href=\"href.xml\">" +
                     "<rollover-image normal=\"normal.gif\" over=\"over.gif\"/>" +
                     "</link>", string);
    }

    /**
     * Tests that the right menu item renderer for the specified configuration.
     */
    public void testPlainText()
        throws Exception {

        MenuItemRendererSelector selector = createSelector();

        Menu menu = createMenu(StylesBuilder.getCompleteStyles(
                "mcs-menu-image-style: none; " +
                "mcs-menu-text-style: plain"));

        MenuBuffer buffer = createMenuBuffer();

        MenuBufferLocator locator = createMenuBufferLocator(buffer);

        MenuItemRenderer renderer = selector.selectMenuItemRenderer(menu);
        assertNotNull("No renderer selected", renderer);

        renderer.render(locator, (MenuItem) menu.get(0));

        String string = toString(buffer);
        //System.out.println("String: " + string);
        assertEquals("Output does not match",
                     "<link " +
//                     "class=\"menu-item-class\" " +
                     "href=\"href.xml\">" +
                     "<plain-text>text</plain-text>" +
                     "</link>", string);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/2	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	3999/3	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 22-Apr-04	3681/6	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 08-Apr-04	3514/5	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 ===========================================================================
*/
