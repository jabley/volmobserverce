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

package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.ConcreteMenuBufferFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBuffer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBufferLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBufferLocatorFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererSelector;
import com.volantis.mcs.protocols.menu.shared.renderer.SingleMenuBufferLocator;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMenuLinkStyleKeywords;

/**
 * An Openwave specific implemention of a means to select a menu renderer
 * for the specified menu.
 */
public class OpenwaveMenuRendererSelector
        extends DefaultMenuRendererSelector {

    /**
     * The module that this should use to retrieve a MenuRenderer from
     * if it cannot retrieve one here.
     */
    private final MenuRendererSelector delegateRendererSelector;

    /**
     * Initialise a new instance of this menu renderer selector using the
     * parameters provided.
     *
     */
    public OpenwaveMenuRendererSelector(
            MenuRendererSelector delegateRendererSelector,
            MenuItemRendererSelector itemRendererSelector,
            MenuBufferLocatorFactory bufferLocatorFactory) {

        super(itemRendererSelector,
              MenuSeparatorRendererSelector.UNUSED,
              MenuBracketingRenderer.UNUSED,
              bufferLocatorFactory);

        this.delegateRendererSelector = delegateRendererSelector;
    }

    public MenuRenderer selectMenuRenderer(Menu menu)
            throws RendererException {

        // Sort out the styles from the menu
        ElementDetails elementDetails = menu.getElementDetails();

        // Get the menu link style
        StyleValue menuLinkStyle = elementDetails.getStyles()
                .getPropertyValues().getComputedValue(
                StylePropertyDetails.MCS_MENU_LINK_STYLE);

        // See if numeric shortcuts were specified in the style
        if (menuLinkStyle == MCSMenuLinkStyleKeywords.NUMERIC_SHORTCUT) {

            ConcreteMenuBufferFactory bufferFactory
                    = new ConcreteMenuBufferFactory(SeparatorRenderer.NULL);

            // Get the underlying menu buffer locator.
            MenuBufferLocator bufferLocator
                    = bufferLocatorFactory.createMenuBufferLocator(
                            bufferFactory);

            MenuBuffer buffer = bufferLocator.getMenuBuffer(menu);

            SingleMenuBufferLocator atomicLocator
                    = new SingleMenuBufferLocator(buffer);

            // Numeric shortcuts were specified so return an Openwave renderer
            return new OpenwaveMenuRenderer(itemRendererSelector,
                                            atomicLocator);
        } else {
            // Otherwise fallback to WML renderering
            return delegateRendererSelector.selectMenuRenderer(menu);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/2	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/6	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/5	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 11-May-04	4217/3	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 10-May-04	4233/4	claire	VBM:2004050710 Fixed MenuRendererSelection null pointer and tidied class naming

 10-May-04	4233/1	claire	VBM:2004050710 Fixed MenuRendererSelection null pointer and tidied class naming

 10-May-04	4227/1	philws	VBM:2004050706 Unique Menu Buffer Locator per Menu

 06-May-04	4174/3	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/6	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring (review feedback)

 06-May-04	4153/4	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 05-May-04	4124/7	claire	VBM:2004042805 Refining menu renderer selectors

 30-Apr-04	4124/5	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 29-Apr-04	4091/1	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 ===========================================================================
*/
