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

import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.menu.shared.DefaultMenuModule;
import com.volantis.mcs.protocols.renderer.RendererContext;

/**
 * The menu module for open wave protocols.
 * <p/>
 * <p>This module will render menus using the Openwave recommended way using
 * select and option elements, or the standard WML way depending on a stylistic
 * property.</p>
 */
public class OpenwaveMenuModule
        extends DefaultMenuModule {

    /**
     * The menu module that this should defer to.
     */
    private final MenuModule delegate;

    /**
     * Initialise.
     *
     * @param delegate The module that should be delegated to,
     */
    public OpenwaveMenuModule(
            RendererContext context,
            MenuModuleRendererFactory rendererFactory,
            MenuModule delegate) {

        super(context, rendererFactory);

        this.delegate = delegate;
    }

    /**
     * Create an Openwave specific menu renderer selector.
     *
     * @return The menu renderer selector instance.
     */
    protected MenuRendererSelector createMenuRendererSelector() {
        return new OpenwaveMenuRendererSelector(
                delegate.getMenuRendererSelector(),
                getMenuItemRendererSelector(),
                getMenuBufferLocatorFactory());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
