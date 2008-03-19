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
package com.volantis.mcs.protocols.menu;

import com.volantis.mcs.protocols.menu.shared.renderer.MenuBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererFactory;

/**
 * Does what it says on the tin.
 */ 
public class DelegatingMenuModuleRendererFactory 
        implements MenuModuleRendererFactory {

    /**
     * The object we delegate to.
     */ 
    protected final MenuModuleRendererFactory delegate;

    /**
     * Construct an instance of this class.
     * 
     * @param rendererFactory
     */
    protected DelegatingMenuModuleRendererFactory(
            MenuModuleRendererFactory rendererFactory) {
        
        this.delegate = rendererFactory;
    }

    /**
     * Delegate to the contained factory, if not overriden.
     */ 
    public MenuSeparatorRendererFactory createMenuSeparatorRendererFactory() {
        
        return delegate.createMenuSeparatorRendererFactory();
    }

    /**
     * Delegate to the contained factory, if not overriden.
     */ 
    public MenuItemRendererFactory createMenuItemRendererFactory() {
        
        return delegate.createMenuItemRendererFactory();
    }

    /**
     * Delegate to the contained factory, if not overriden.
     */ 
    public MenuBracketingRenderer createMenuBracketingRenderer() {
        
        return delegate.createMenuBracketingRenderer();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 ===========================================================================
*/
