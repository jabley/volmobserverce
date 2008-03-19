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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.renderer.RendererMenuModelVisitor;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * A (currently bodgy) test menu renderer.
 * <p>
 * This was created by cloning the extremely simplistic OpenwaveMenuRenderer 
 * and is not complete or correct generally, athough it really ought to be.
 * It cannot be thus as the separator stuff and menu rendering has not been
 * done yet.
 *  
 * @todo will probably need to be refactored when we sort out menu rendering.
 */ 
public class TestMenuRenderer implements MenuRenderer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Object which selects and creates the appropriate menu item renderer
     * via composition using the related factory and any style information 
     * present. 
     */ 
    private MenuItemRendererSelector itemSelector;

    /**
     * Object which selects and creates the appropriate separators
     * via composition using the related factory and any style information 
     * present. 
     */ 
    private MenuSeparatorRendererSelector separatorSelector;
    
    /**
     * A simple menu item buffer locator which points to the output buffer 
     * above. 
     */ 
    private TestMenuBufferLocator locator;

    /**
     * Construct an instance of this class using the objects provided.
     * 
     * @param itemSelector used to select/construct a menu item renderer for 
     *      the menu.
     * @param separatorSelector used to select/construct the separators for 
     *      the menu.
     * @param locator the menu buffer locator used to find the output buffer.
     */ 
    public TestMenuRenderer(MenuItemRendererSelector itemSelector, 
            MenuSeparatorRendererSelector separatorSelector, 
            TestMenuBufferLocator locator) {
        
        this.itemSelector = itemSelector;
        this.separatorSelector = separatorSelector;
        this.locator = locator;
    }

    // Javadoc inherited.
    public void render(Menu menu)
            throws RendererException {

        // Select a renderer to render the contents of each option.
        // This can be text and/or image depending on the style of the menu 
        // and or content of the menu item.
        final MenuItemRenderer renderer = 
                itemSelector.selectMenuItemRenderer(menu);
        if (renderer == null) {
            // We can't find a renderer for menu items, so there's not much
            // point rendering the menu. This will happen in the pathological
            // case where both the image and text styles are set to 'none'.
            return;
        }

        final SeparatorRenderer separator = 
                separatorSelector.selectMenuItemSeparator(menu);
        
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
                    
                    DOMOutputBuffer dom = locator.getOutputBuffer();

                    // Open a menu element so we can see this renderer in the 
                    // output.
                    Element menuElement = dom.openElement("menu");

                    // Iterate over the children in order.
                    renderChildren(menu);

                    // Close the menu element.
                    dom.closeElement(menuElement);
                } 
                // else, ignore nested menus for now
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 04-May-04	4164/1	pduffin	VBM:2004050404 Refactored DefaultMenuRenderer internal visitor class to allow pre and post processing.

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
