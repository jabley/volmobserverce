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
package com.volantis.mcs.protocols.vdxml.menu;

import com.volantis.mcs.protocols.menu.MenuModuleCustomisation;
import com.volantis.mcs.protocols.menu.shared.DefaultMenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRendererFactory;
import com.volantis.mcs.protocols.renderer.RendererContext;

/**
 * A VDXML specific factory to create the appropriate renderer factories for
 * the VDXML menu module.
 */
public final class VDXMLMenuModuleRendererFactory
        extends DefaultMenuModuleRendererFactory {

    /**
     * Renders the RACCOURCI markup.
     */
    private final DeprecatedExternalLinkOutput externalLinkOutput;

    /**
     * Initialise.
     *
     * @param context
     * @param outputLocator
     * @param customisation
     * @param externalLinkOutput Renders the RACCOURCI markup.
     */
    public VDXMLMenuModuleRendererFactory(RendererContext context,
            DeprecatedOutputLocator outputLocator,
            MenuModuleCustomisation customisation,
            DeprecatedExternalLinkOutput externalLinkOutput) {
        
        super(context, outputLocator, customisation);
        this.externalLinkOutput = externalLinkOutput;
    }

    /**
     * Create an VDXML specific menu item renderer factory that will render
     * RACCOURCI elements for the link shortcut and href separate to the
     * link text.
     */
    public MenuItemRendererFactory createMenuItemRendererFactory() {

        return new VDXMLMenuItemRendererFactory(context, outputLocator,
                externalLinkOutput);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/2	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/
