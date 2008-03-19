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
package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.protocols.menu.shared.renderer.DelegatingMenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.NumericShortcutEmulationRenderer;

/**
 * A decorating menu item renderer factory for shard links which decorates the
 * outer menu item renderers returned with shard link renderers which add the
 * special shard link markup expected by the dissector for the shard link 
 * menu items. 
 */ 
public class ShardLinkMenuItemRendererFactory 
        extends DelegatingMenuItemRendererFactory {

    /**
     * Construct an instance of this class.
     * 
     * @param delegate the normal factory which we are decorating.
     */ 
    public ShardLinkMenuItemRendererFactory(MenuItemRendererFactory delegate) {
        
        super(delegate);
    }

    /**
     * Create an outer link renderer which decorates the delegate's version
     * with a SHARD LINK ELEMENT element.
     */ 
    // Other javadoc inherited.
    public MenuItemBracketingRenderer createOuterLinkRenderer(
            NumericShortcutEmulationRenderer emulation) {
        
        MenuItemBracketingRenderer renderer
                = super.createOuterLinkRenderer(emulation);

        return new ShardLinkMenuItemRenderer(renderer);
    }

    /**
     * Create an outer renderer which decorates the delegate's version
     * with a SHARD LINK ELEMENT element.
     */ 
    // Other javadoc inherited.
    public MenuItemBracketingRenderer createOuterRenderer() {
        
        MenuItemBracketingRenderer renderer
                = super.createOuterRenderer();

        return new ShardLinkMenuItemRenderer(renderer);
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

 13-May-04	4315/2	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 28-Apr-04	4048/2	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
