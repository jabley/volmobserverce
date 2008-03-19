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

package com.volantis.mcs.protocols.menu.renderer;

import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * Selects a menu renderer for the specified menu.
 *
 * @mock.generate 
 */
public interface MenuRendererSelector {

    /**
     * A null instance of a <code>MenuRendererSelector</code> that can be
     * used to represent uninitialised references or null menu renderer
     * selector objects but where the standard Java <code>null</code> may have
     * another meaning.
     */
    MenuRendererSelector NULL = new MenuRendererSelector() {
        public MenuRenderer selectMenuRenderer(Menu menu)
                throws RendererException {
            throw new UnsupportedOperationException("Not a usable instance");
        }
    };

    /**
     * Returns a MenuRenderer instance that must be used to render the
     * specified menu.
     * <p>The returned MenuRenderer instance must render the specified menu and
     * all its contained items and item groups but not nested menus.</p>
     * @param menu The menu that the renderer is needed for, may not be null.
     * @return The MenuRenderer instance, will not be null.
     * @throws RendererException If there was a problem selecting an
     * appropriate renderer.
     */
    MenuRenderer selectMenuRenderer(Menu menu) throws RendererException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4174/3	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 29-Apr-04	4091/1	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 08-Apr-04	3514/2	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 07-Apr-04	3735/1	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 11-Mar-04	3274/2	pduffin	VBM:2004022704 Initial API for menu renderers and markup generators

 ===========================================================================
*/
