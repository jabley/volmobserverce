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

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.menu.renderer.RendererMenuModelVisitor;
import com.volantis.mcs.protocols.menu.shared.MenuInspector;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorManager;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * This menu renderer is responsible for:
 *
 * <ol>
 *
 * <li>determining the enhanced output buffer to be used, via a {@link
 * MenuBufferLocator}</li>
 *
 * <li>determining, via a {@link MenuRendererSelector}, and invoking the menu
 * renderer for each sub-menu</li>
 *
 * <li>determining, via a {@link MenuItemRendererSelector}, and invoking the
 * menu item renderer for each menu item that belongs to this menu or its
 * immediate menu groups</li>
 *
 * <li>determining, via a {@link MenuSeparatorRendererSelector}, and invoking
 * (indirectly) the separator renderers for menus and menu groups</li>
 *
 * <!--
 *
 * <li>rendering, via a <strong>TBD</strong>, the required indentation before
 * each of its menu items</li>
 *
 * <li>determining, via the current
 * {@link com.volantis.mcs.protocols.menu.shared.renderer.MenuBuffer
 * MenuBuffer}, the separator manager required to manage and render separator
 * markup</li>
 *
 * -->
 *
 * </ol>
 */
public class DefaultMenuRenderer implements MenuRenderer {

    /**
     * The selector to be used to select menu renderers for rendering
     * sub-menus.
     */
    private final MenuRendererSelector rendererSelector;

    private final MenuBracketingRenderer menuBracketingRenderer;

    /**
     * The selector to be used to select menu item renderers for the items in
     * the menu to be rendered.
     */
    private final MenuItemRendererSelector itemRendererSelector;

    /**
     * The selector to be used to select separator renderers for menus and menu
     * item group separation.
     */
    private final MenuSeparatorRendererSelector separatorRendererSelector;

    /**
     * The locator that can be used to obtain the enhanced output buffer
     * required for a given menu item.
     */
    private final MenuBufferLocator bufferLocator;

    /**
     * The renderer that renderers the menu items for this menu.
     */
    private MenuItemRenderer menuItemRenderer;

    /**
     * An means of examining a menu for certain behaviours and properties.
     */
    private final MenuInspector menuInspector;

    /**
     * Flag which is true if the menu being rendered is using iterators to
     * create panes for each of it's items.
     */
    private boolean usingPaneIteration;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param rendererSelector used to select an appropriate renderer for
     *      nested menus. May not be null.
     * @param itemRendererSelector used to select an appropriate renderer for
     *      the menu items in the menu that this renderer renders. May not be
     *      null.
     * @param separatorRendererSelector used to select an appropriate renderer
     *      for the menu and menu item group separators. May not be null.
     * @param bufferLocator used to locate the enhanced output buffer to which
     *      any given menu item or separator should be written. May not be null.
     */
    public DefaultMenuRenderer(
            MenuRendererSelector rendererSelector,
            MenuItemRendererSelector itemRendererSelector,
            MenuSeparatorRendererSelector separatorRendererSelector,
            MenuBracketingRenderer menuBracketingRenderer,
            MenuBufferLocator bufferLocator) {

        if (rendererSelector == null) {
            throw new IllegalArgumentException(
                    "rendererSelector may not be null");
        } else if (itemRendererSelector == null) {
            throw new IllegalArgumentException(
                    "itemRendererSelector may not be null");
        } else if (menuBracketingRenderer == null) {
            throw new IllegalArgumentException(
                    "menuBracketingRenderer may not be null");
        } else if (separatorRendererSelector == null) {
            throw new IllegalArgumentException(
                    "separatorRendererSelector may not be null");
        } else if (bufferLocator == null) {
            throw new IllegalArgumentException(
                    "bufferLocatorFactory may not be null");
        }

        this.rendererSelector = rendererSelector;
        this.itemRendererSelector = itemRendererSelector;
        this.menuBracketingRenderer = menuBracketingRenderer;
        this.separatorRendererSelector = separatorRendererSelector;
        this.bufferLocator = bufferLocator;
        this.menuInspector = new MenuInspector();

    }

    // javadoc inherited
    public void render(Menu menu)
            throws RendererException {

        // Decide if this menu is using iterators to create panes for
        // each of it's items.
        usingPaneIteration =
                menuInspector.isAutoIterationAllocating(menu);

        // Create the menu item renderer used for each of the menu items
        // belonging to this menu.
        menuItemRenderer
                = itemRendererSelector.selectMenuItemRenderer(menu);

        // Get a visitor for rendering the object.
        RendererMenuModelVisitor rendererVisitor = new RendererVisitor();

        // The output buffer for menu markup. This will be null if the
        // menu should not have any markup rendered for it
        OutputBuffer buffer = null;

        // Only render the mender if we have a renderer i.e. one of
        // MCS_MENU_TEXT_STYLE or MCS_MENU_IMAGE_STYLE is not "none".
        if (menuItemRenderer != null) {
            // If the menu is not using pane iteration, then it should be
            // safe to add the menu markup for this menu.

            // NOTE: this means that "scatter" menus which do not add any
            // items into the menu's pane will result in rendundant menu markup
            // being added to the menu pane. This is an obvious candidate for
            // future "optimisation".
            if (!usingPaneIteration) {
                MenuBuffer menuBuffer = bufferLocator.getMenuBuffer(menu);

                // This check is necessary in case the menuBuffer does not
                // exist
                if (menuBuffer != null) {
                    buffer = menuBuffer.getOutputBuffer();

                    menuBracketingRenderer.open(buffer, menu);
                }
            }

            // Render the children of the menu. Note that this avoids visiting the
            // menu being rendered, which means that when a menu is visited in the
            // visitor below, we know it must be a sub-menu. This makes things a
            // little bit clearer.
            rendererVisitor.renderChildren(menu);

            if (buffer != null) {
                // Only close markup that was opened above
                menuBracketingRenderer.close(buffer, menu);
            }
        }
    }

    /**
     * The visitor that is used to visit and render the contents of the menu
     * model.
     *
     * <p>This inner class could be merged into the containing class in order
     * to reduce the number of objects being created but that for the moment
     * it is separate as that is easier to handle.</p>
     */
    private class RendererVisitor
            extends RendererMenuModelVisitor {

        // JavaDoc inherited
        public void rendererVisit(MenuItem item) throws RendererException {

            // Render the menu item.
            // if the item to be rendered is implicitly or explictly targeted
            // at a pane which is different to it's parent, then we will need
            // to render menu markup around it to ensure it has the parent
            // menu's style inherited.
            // NOTE: for menus which contain more than one item which
            // explicitly target the same pane, this will cause the same menu
            // markup to be written into that pane more than once. This is an
            // obvious candidate for future "optimisation".
            if (usingPaneIteration || item.getPane() != null) {
                // Render the "isolated" item with surrounding menu markup,
                // as it is outside the markup of it's parent menu.
                MenuBuffer menuBuffer = bufferLocator.getMenuBuffer(item);

                // This check is necessary in case the menuBuffer does not exist
                if (menuBuffer != null) {
                    OutputBuffer buffer = menuBuffer.getOutputBuffer();
                    menuBracketingRenderer.open(buffer, item.getMenu());
                    menuItemRenderer.render(bufferLocator, item);
                    menuBracketingRenderer.close(buffer, item.getMenu());
                }
            } else {
                // Render the "contained" item without surrounding menu markup,
                // as this pane we are rendering to will already have had the
                // menu markup added when we rendered the menu.
                menuItemRenderer.render(bufferLocator, item);
            }
        }

        // JavaDoc inherited
        public void rendererVisit(MenuItemGroup group)
                throws RendererException {

            // Get the SeparatorManager to use to render the separators.
            MenuBuffer menuBuffer = bufferLocator.getMenuBuffer(group);

            // This check is necessary in case the menuBuffer does not exist
            if (menuBuffer != null) {
                SeparatorManager separatorManager =
                        menuBuffer.getSeparatorManager();

                // Render the separator before.
                final SeparatorRenderer before =
                        separatorRendererSelector.selectMenuItemGroupSeparator(
                                group, true);
                if (before != null) {
                    separatorManager.queueSeparator(before);
                }

                // We always render the items in the menu group
                renderChildren(group);

                // Render the separator after.
                final SeparatorRenderer after =
                        separatorRendererSelector.selectMenuItemGroupSeparator(
                                group, false);
                if (after != null) {
                    separatorManager.queueSeparator(after);
                }
            }
        }

        // JavaDoc inherited
        public void rendererVisit(Menu menu) throws RendererException {

            // NOTE: This method will only be called for sub-menus, and is
            // only for rendering the labels of those sub-menus. The original
            // menu is rendered in the render method, and contained menus
            // get their own menu renderer as shown below.

            // Render a menu item that represents this sub-menu
            MenuItem item = menu.getAsMenuItem();
            // Check for whether it should be rendered.  The check for
            // auto-iterating is necessary because for pane targeted
            // (which this menu has become if its parent automatically
            // spatially iterates) then the label should not be output.
            if (item != null &&
                    !menuInspector.isParentAutoIterating(menu)) {
                rendererVisit(item);
            }

            // Render the sub-menu in a separate renderer
            MenuRenderer renderer = rendererSelector.
                    selectMenuRenderer(menu);

            renderer.render(menu);

            // Sub-menus' content should not be visited here since they
            // are processed separately above

        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4424/1	geoff	VBM:2004051414 Enhanced Menus: extra divs being written for labels.

 14-May-04	4318/4	pduffin	VBM:2004051207 Integrated separators into menu rendering

 13-May-04	4348/1	philws	VBM:2004051304 Fix menu close markup rendering issue

 13-May-04	4297/4	claire	VBM:2004051110 Enhanced Menu: Bugs: Automatic allocation in spatial iterators failing

 12-May-04	4297/2	claire	VBM:2004051110 Enhanced Menu: Bugs: Automatic allocation in spatial iterators failing

 12-May-04	4328/1	claire	VBM:2004051209 Enhance Menu Support: Bugs: Correct buffer null pointers

 12-May-04	4279/5	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/3	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 11-May-04	4274/1	claire	VBM:2004051101 Enhanced Menus: Fix explicit format null pointer on menu buffers

 07-May-04	4164/4	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 04-May-04	4164/1	pduffin	VBM:2004050404 Refactored DefaultMenuRenderer internal visitor class to allow pre and post processing.

 22-Apr-04	3681/3	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
