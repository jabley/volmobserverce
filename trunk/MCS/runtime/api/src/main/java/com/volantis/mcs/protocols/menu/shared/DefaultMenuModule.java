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

package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuBufferLocatorFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuItemRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuSeparatorRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBufferLocatorFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererSelector;
import com.volantis.mcs.protocols.renderer.RendererContext;

/**
 * The default menu module.
 *
 * <p>It has default behaviour that is generally consistent with a broad range
 * of protocols, e.g. HTML and WML.</p>
 */
public class DefaultMenuModule
        extends AbstractMenuModule {

    /**
     * The contextual information.
     */
    private final RendererContext context;

    /**
     * A lazily created instance.
     */
    private MenuSeparatorRendererSelector menuSeparatorRendererSelector;

    /**
     * A lazily created instance.
     */
    private MenuBufferLocatorFactory menuBufferLocatorFactory;

    /**
     * A lazily created instance.
     */
    private MenuItemRendererSelector menuItemRendererSelector;

    /**
     * A lazily created instance.
     */
    private MenuSeparatorRendererFactory menuSeparatorRendererFactory;

    /**
     * A lazily created instance.
     */
    private MenuItemRendererFactory menuItemRendererFactory;

    /**
     * A lazily created instance.
     */
    private MenuBracketingRenderer menuBracketingRenderer;

    private final MenuModuleRendererFactory rendererFactory;

    /**
     * Initialise.
     * @param context Contains contextual information.
     */
    public DefaultMenuModule(RendererContext context,
            MenuModuleRendererFactory rendererFactory) {

        this.context = context;
        this.rendererFactory = rendererFactory;
    }

    /**
     * Create the default menu renderer selector.
     */
    protected MenuRendererSelector createMenuRendererSelector() {
        return new DefaultMenuRendererSelector(
                getMenuItemRendererSelector(),
                getMenuSeparatorRendererSelector(),
                getMenuBracketingRenderer(),
                getMenuBufferLocatorFactory());
    }

    /**
     * Get the factory to use to create menu buffer locators, creating one if
     * necessary.
     *
     * @return The factory, may not be null.
     */
    protected final MenuBufferLocatorFactory getMenuBufferLocatorFactory() {
        if (menuBufferLocatorFactory == null) {
            menuBufferLocatorFactory = createMenuBufferLocatorFactory();
        }

        return menuBufferLocatorFactory;
    }

    /**
     * Create a factory for creating menu buffer locators.
     *
     * <p>The returned instance may be used for rendering multiple menus,
     * including sub menus within a single request.</p>
     *
     * @return The factory instance, may not be null.
     */
    private MenuBufferLocatorFactory createMenuBufferLocatorFactory() {

        return new DefaultMenuBufferLocatorFactory(
                context.getOutputBufferResolver());
    }

    /**
     * Get the instance to use to select menu item renderers, creating one if
     * necessary.
     *
     * @return The selector instance, may not be null.
     */
    protected final MenuItemRendererSelector getMenuItemRendererSelector() {
        if (menuItemRendererSelector == null) {
            menuItemRendererSelector = createMenuItemRendererSelector();
        }

        return menuItemRendererSelector;
    }

    /**
     * Create a menu item renderer selector.
     *
     * <p>The returned instance may be used for rendering multiple menus,
     * including sub menus within a single request.</p>
     *
     * @return The selector instance, may not be null.
     */
    private MenuItemRendererSelector createMenuItemRendererSelector() {

        // Create a default instance parameterised with protocol specific
        // objects.
        return new DefaultMenuItemRendererSelector(
                getMenuItemRendererFactory(),
                getMenuSeparatorRendererSelector());
    }

    /**
     * Get the separator renderer selector, creating one if it has not already
     * been created.
     *
     * @return The selector instance, may not be null.
     */
    private MenuSeparatorRendererSelector getMenuSeparatorRendererSelector() {

        if (menuSeparatorRendererSelector == null) {
            menuSeparatorRendererSelector = createMenuSeparatorRendererSelector();
        }

        return menuSeparatorRendererSelector;
    }

    /**
     * Create the separator renderer selector.
     *
     * <p>The returned instance may be used for rendering multiple menus,
     * including sub menus within a single request.</p>
     *
     * @return The selector instance, may not be null.
     */
    private MenuSeparatorRendererSelector createMenuSeparatorRendererSelector() {
        return new DefaultMenuSeparatorRendererSelector(
                getMenuSeparatorRendererFactory(),
                context.getAssetResolver(),
                context.getStylePropertyResolver());
    }

    /**
     * Get the menu separator renderer factory, creating one if it has not
     * already been created.
     *
     * @return The factory instance, may not be null.
     */
    private MenuSeparatorRendererFactory getMenuSeparatorRendererFactory() {

        if (menuSeparatorRendererFactory == null) {
            menuSeparatorRendererFactory =
                    rendererFactory.createMenuSeparatorRendererFactory();
        }

        return menuSeparatorRendererFactory;
    }

    /**
     * Get the menu item renderer factory, creating one if it has not
     * already been created.
     *
     * @return The factory instance, may not be null.
     */
    private MenuItemRendererFactory getMenuItemRendererFactory() {
        if (menuItemRendererFactory == null) {
            menuItemRendererFactory =
                    rendererFactory.createMenuItemRendererFactory();
        }

        return menuItemRendererFactory;
    }

    /**
     * Get the renderer for bracketing menu content, creating one if necessary.
     *
     * @return The renderer instance, may not be null.
     */
    private MenuBracketingRenderer getMenuBracketingRenderer() {
        if (menuBracketingRenderer == null) {
            menuBracketingRenderer =
                    rendererFactory.createMenuBracketingRenderer();
        }
        return menuBracketingRenderer;
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

 17-May-04	4440/1	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
