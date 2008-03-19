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
package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.renderer.RendererMenuModelVisitor;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBuffer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBufferLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererSelector;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.wml.WMLConstants;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.Styles;

/**
 * A menu renderer for Openwave menus.
 * <p/>
 * Openwave menus use the p mode=nowrap/select/option tags to render the menu
 * as per the Openwave style guidelines (see R630). The browser then adds
 * numeric shortcuts automagically.
 * <p/>
 * NOTE: there is no rendering of stylistic emulation markup here. This is
 * because select/option can only contain PCDATA, so even if we rendered it
 * it would be invalid and (almost always) removed by the transformer. The
 * only time it would not be removed would be if it was intercepted by the
 * "emulate emphasis" processing in the transformer and translated into just
 * text before and after (eg "[" & "]") - and it's not worth implementing just
 * for this edge condition.
 *
 * @todo test on an emulator as well when this is integrated into protocols.
 */
public class OpenwaveMenuRenderer implements MenuRenderer {

    /**
     * Object which selects and creates the appropriate menu item renderer
     * via composition using the related factory and any style information
     * present.
     */
    private final MenuItemRendererSelector selector;

    /**
     * The locator to use to find the appropriate buffer to write to.
     */
    private final MenuBufferLocator locator;

    /**
     * Construct an instance of this class using the objects provided.
     *
     * @param selector          Used to select/construct a menu item renderer for the
     *                          menu.
     * @param menuBufferLocator The object responsible for locating a menu
     *                          buffer for the menu.
     */
    public OpenwaveMenuRenderer(
            MenuItemRendererSelector selector,
            MenuBufferLocator menuBufferLocator) {
        this.selector = selector;
        this.locator = menuBufferLocator;
    }

    // Javadoc inherited.
    public void render(Menu menu)
            throws RendererException {

        // Select a renderer to render the contents of each option.
        // This can be text and/or image depending on the style of the menu
        // and or content of the menu item.
        final MenuItemRenderer renderer =
                selector.selectMenuItemRenderer(menu);
        if (renderer == null) {
            // We can't find a renderer for menu items, so there's not much
            // point rendering the menu. This will happen in the pathological
            // case where both the image and text styles are set to 'none'.
            return;
        }

        // Construct a visitor which renders the menu.
        // @todo model could use Composite, External and/or Internal Iterator
        RendererMenuModelVisitor visitor = new RendererMenuModelVisitor() {

            public void rendererVisit(MenuItem item)
                    throws RendererException {
                // Use the menu item renderer which we previously selected
                // to render each menu item in a similar fashion.
                renderer.render(locator, item);
            }

            public void rendererVisit(MenuItemGroup group)
                    throws RendererException {
                // For groups of menu items, we ignore the group itself
                // and just render the contained menu items.

                // Iterate over the children in order.
                renderChildren(group);
            }

            public void rendererVisit(Menu menu) throws RendererException {
                // For menus, we render only the root menu.
                if (menu.getContainer() == null) {
                    // process the root menu

                    MenuBuffer menuBuffer = locator.getMenuBuffer(menu);
                    DOMOutputBuffer buffer
                            = (DOMOutputBuffer) menuBuffer.getOutputBuffer();

                    // Open a paragraph for the select to go in.
                    // According to openwave style guide this should be nowrap.
                    // todo: only set whitespace if it was not specified by user
                    // currently we cannot tell, fix when VBM:2005092701 is fixed
                    Styles styles = menu.getElementDetails().getStyles();
                    styles.getPropertyValues().setComputedValue(
                            StylePropertyDetails.WHITE_SPACE,
                            WhiteSpaceKeywords.NOWRAP);
                    Element element = buffer.openStyledElement(
                            WMLConstants.BLOCH_ELEMENT, styles);

                    // Open a select for the menu.
                    element = buffer.openElement("select");
                    if (menu.getTitle() != null) {
                        element.setAttribute("title", menu.getTitle());
                    }

                    // Iterate over the children in order.
                    renderChildren(menu);

                    // Close the select.
                    buffer.closeElement("select");

                    // Close the paragraph.
                    buffer.closeElement(WMLConstants.BLOCH_ELEMENT);
                }
                // else, ignore nested menus for now
                // todo: implement nested menus.
            }
        };
        visitor.accept(menu);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Sep-05	9600/2	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 12-May-04	4279/3	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4164/4	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 04-May-04	4164/1	pduffin	VBM:2004050404 Refactored DefaultMenuRenderer internal visitor class to allow pre and post processing.

 30-Apr-04	4124/1	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 29-Apr-04	4091/1	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers (review comments)

 21-Apr-04	3681/2	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 16-Apr-04	3884/1	claire	VBM:2004040712 Tidied up and supermerged AssetReferenceException

 16-Apr-04	3645/3	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 15-Apr-04	3645/1	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 ===========================================================================
*/
