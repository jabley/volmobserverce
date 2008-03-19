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

import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;

/**
 * An WML specific implemention of a means to select a menu renderer
 * for the specified menu.
 */
public class DefaultMenuRendererSelector
        implements MenuRendererSelector {

    /**
     * A means of locating a menu buffer for use when rendering.
     */
    protected final MenuBufferLocatorFactory bufferLocatorFactory;

    /**
     * A means of selecing a menu separator renderer.
     */
    private final MenuSeparatorRendererSelector separatorSelector;

    /**
     * A means of selecting a menu item renderer.
     */
    protected final MenuItemRendererSelector itemRendererSelector;

    /**
     * The renderer responsible for rendering markup that brackets the menu.
     */
    private final MenuBracketingRenderer bracketingRenderer;

    /**
     * Initialise a new instance of this WML menu renderer selector using the
     * parameters provided.
     *
     * todo javadoc parameters
     */
    public DefaultMenuRendererSelector(
            MenuItemRendererSelector itemRendererSelector,
            MenuSeparatorRendererSelector separatorSelector,
            MenuBracketingRenderer bracketingRenderer,
            MenuBufferLocatorFactory bufferLocatorFactory) {

        if (itemRendererSelector == null) {
            throw new IllegalArgumentException(
                    "Item renderer factory cannot be null");
        } else if (separatorSelector == null) {
            throw new IllegalArgumentException(
                    "Separator selector cannot be null");
        } else if (bracketingRenderer == null) {
            throw new IllegalArgumentException(
                    "Bracketing renderer cannot be null");
        } else if (bufferLocatorFactory == null) {
            throw new IllegalArgumentException(
                    "Buffer locator factory cannot be null");
        }

        this.itemRendererSelector = itemRendererSelector;
        this.separatorSelector = separatorSelector;
        this.bufferLocatorFactory = bufferLocatorFactory;
        this.bracketingRenderer = bracketingRenderer;
    }

    /**
     * Create a parameterised instance of {@link DefaultMenuRenderer}.
     */
    public MenuRenderer selectMenuRenderer(Menu menu)
            throws RendererException {

        // Get the orientation separator.
        final SeparatorRenderer orientationSeparator
                = separatorSelector.selectMenuSeparator(menu);

        // Create a menu buffer factory that will use the appropriate
        // orientation renderer.
        MenuBufferFactory bufferFactory = new ConcreteMenuBufferFactory(orientationSeparator);

        // Create a new locator for the menu renderer. This is because it must
        // not be shared across multiple menus.
        MenuBufferLocator locator
                = bufferLocatorFactory.createMenuBufferLocator(bufferFactory);

        return new DefaultMenuRenderer(this, itemRendererSelector,
                                       separatorSelector, bracketingRenderer,
                                       locator);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/3	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 10-May-04	4233/6	claire	VBM:2004050710 Fixed MenuRendererSelection null pointer and tidied class naming

 10-May-04	4233/4	claire	VBM:2004050710 Fixed MenuRendererSelection null pointer and tidied class naming

 10-May-04	4227/1	philws	VBM:2004050706 Unique Menu Buffer Locator per Menu

 07-May-04	4204/3	philws	VBM:2004042810 Suppress Menu Item Group separators when needed

 06-May-04	4174/7	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4174/5	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4174/3	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/4	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring (review feedback)

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 05-May-04	4124/5	claire	VBM:2004042805 Refining menu renderer selectors

 30-Apr-04	4124/3	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 30-Apr-04	4124/1	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 ===========================================================================
*/
