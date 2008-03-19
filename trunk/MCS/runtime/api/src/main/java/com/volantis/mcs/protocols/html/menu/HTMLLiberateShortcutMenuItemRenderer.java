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

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.renderer.DelegatingMenuItemBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A menu item renderer which decorates a child menu item renderer and adds
 * the ability to render a shortcut which is external to the markup created
 * for the menu item.
 * <p>
 * This is useful for HTMLLiberate where we render the shortcut as a bunch of 
 * javascript in the page head. 
 */ 
class HTMLLiberateShortcutMenuItemRenderer 
        extends DelegatingMenuItemBracketingRenderer {

    /**
     * Object which can handle rendering shortcuts external to a menu item.
     */ 
    private final DeprecatedExternalShortcutRenderer shortcutRenderer;
    
    /**
     * Construct an instance of this class.
     * 
     * @param delegate renders entire menu items (apart from the shortcut).
     * @param shortcutRenderer renders shortcuts external to the menu item.
     */ 
    public HTMLLiberateShortcutMenuItemRenderer(
            MenuItemBracketingRenderer delegate,
            DeprecatedExternalShortcutRenderer shortcutRenderer) {

        super(delegate);

        this.shortcutRenderer = shortcutRenderer;
    }

    /**
     * Add a shortcut for the menu item.
     */ 
    public void close(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        super.close(buffer, item);

        // Add the shortcut outside the menu item.
        shortcutRenderer.renderShortcut(item);
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

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 26-Apr-04	3920/3	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/
