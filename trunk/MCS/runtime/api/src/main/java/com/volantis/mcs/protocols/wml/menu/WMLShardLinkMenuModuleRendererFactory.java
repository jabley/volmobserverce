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
package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.protocols.menu.DelegatingMenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBracketingRenderer;

/**
 * A rather bogus delegating menu module renderer factory which simply
 * hard codes the menu bracketing renderer to one that renders a
 * &lt;p mode=wrap&gt;, specifically for WML shard link menus.
 *
 * @todo replace this with a DeprecatedBlockOutput?
 */
public class WMLShardLinkMenuModuleRendererFactory
        extends DelegatingMenuModuleRendererFactory {

    /**
     * Create an instance of this class.
     *
     * @param rendererFactory the delegate.
     */
    public WMLShardLinkMenuModuleRendererFactory(
            MenuModuleRendererFactory rendererFactory) {

        super(rendererFactory);
    }

    /**
     * Creates a menu bracketing renderer which always renders
     * &lt;p mode=wrap&gt;.
     */
    // Other javadoc inherited.
    public MenuBracketingRenderer createMenuBracketingRenderer() {
        return new WMLShardLinkParagraphMenuBracketingRenderer();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 ===========================================================================
*/
