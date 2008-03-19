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
package com.volantis.mcs.protocols.html.menu;

import com.volantis.mcs.protocols.menu.MenuModuleCustomisation;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.NumericShortcutEmulationRenderer;
import com.volantis.mcs.protocols.renderer.RendererContext;

/**
 * A menu item renderer factory for HTMLLiberate.
 * <p>
 * This extends the XHTMLFull version to add a special HTMLLiberate specific
 * bit of javascript to implement shortcut keys.
 */
public class HTMLLiberateMenuItemRendererFactory
        extends XHTMLFullMenuItemRendererFactory {

    /**
     * Object which can handle rendering shortcuts.
     */
    private final DeprecatedExternalShortcutRenderer shortcutRenderer;

    /**
     * Construct an instance of this class.
     *
     * @param context Contains contextual information.
     * @param outputLocator Contains references to markup generators.
     */
    public HTMLLiberateMenuItemRendererFactory(
            RendererContext context,
            DeprecatedOutputLocator outputLocator,
            MenuModuleCustomisation customisation) {

        super(context, outputLocator, customisation);

        this.shortcutRenderer = outputLocator.getExternalShortcutRenderer();
    }

    // Javadoc inherited.
    public MenuItemBracketingRenderer createOuterLinkRenderer
            (NumericShortcutEmulationRenderer numericEmulation) {

        // Wrap the normal renderer with our special one which additionally
        // renders the shortcut.
        return new HTMLLiberateShortcutMenuItemRenderer(
                super.createOuterLinkRenderer(numericEmulation),
                shortcutRenderer);
    }

    // Javadoc inherited.
    public MenuItemBracketingRenderer createOuterRenderer() {

        // Wrap the normal renderer with our special one which additionally
        // renders the shortcut.
        return new HTMLLiberateShortcutMenuItemRenderer(
                super.createOuterRenderer(),
                shortcutRenderer);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/1	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 27-Apr-04	4025/1	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/