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
package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.menu.MenuModuleCustomisation;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBracketingRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuSeparatorRendererFactory;
import com.volantis.mcs.protocols.renderer.RendererContext;

public class DefaultMenuModuleRendererFactory 
        implements MenuModuleRendererFactory {

    protected final RendererContext context;
    
    protected final DeprecatedOutputLocator outputLocator;
    
    protected final MenuModuleCustomisation customisation;

    public DefaultMenuModuleRendererFactory(RendererContext context, 
            DeprecatedOutputLocator outputLocator, 
            MenuModuleCustomisation customisation) {
        
        this.context = context;
        this.outputLocator = outputLocator;
        this.customisation = customisation;
    }

    public MenuSeparatorRendererFactory createMenuSeparatorRendererFactory() {
        
        return new DefaultMenuSeparatorRendererFactory(
                outputLocator.getImageOutput(),
                outputLocator.getLineBreakOutput());
    }

    public MenuItemRendererFactory createMenuItemRendererFactory() {
        
        return new DefaultMenuItemRendererFactory(context, outputLocator,
                                                  customisation);
    }
    
    public MenuBracketingRenderer createMenuBracketingRenderer() {
        
        return new DefaultMenuBracketingRenderer(outputLocator.getDivOutput());
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 13-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 ===========================================================================
*/
