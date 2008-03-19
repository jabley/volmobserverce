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
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererFactory;
import com.volantis.mcs.protocols.renderer.RendererContext;

public class HTMLLiberateMenuModuleRendererFactory 
        extends XHTMLFullMenuModuleRendererFactory {

    public HTMLLiberateMenuModuleRendererFactory(RendererContext context,
            DeprecatedOutputLocator outputLocator, 
            MenuModuleCustomisation customisation) {
        
        super(context, outputLocator, customisation);
    }

    /**
     * Override to provide an HTMLLiberate specialisation.
     *
     * <p>The returned specialisation provides support for HTML liberate access
     * keys.</p>
     */
    public MenuItemRendererFactory createMenuItemRendererFactory() {
        return new HTMLLiberateMenuItemRendererFactory(context, outputLocator,
                customisation);
    }
    

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 ===========================================================================
*/
