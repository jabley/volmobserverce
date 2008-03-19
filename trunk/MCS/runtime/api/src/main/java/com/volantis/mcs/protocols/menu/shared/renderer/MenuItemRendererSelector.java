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

package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * Selects a menu item renderer for the specified menu.
 * <p>At the moment each menu item within a single menu is rendered in
 * exactly the same way, hence the reason that the
 * {@link #selectMenuItemRenderer} takes a {@link Menu} instead of a
 * {@link MenuItem}. This means that the instance returned from this method has
 * to render multiple menu items.</p>
 * <p>Where possible implementations should not contain any information that
 * would prevent it being used by multiple threads.</p>
 */
public interface MenuItemRendererSelector {

    /**
     * Returns a MenuItemRenderer instance that must be used to render the
     * menu items within the specified menu.
     * <p>The returned MenuItemRenderer instance will be used to render all the
     * specified menu's items (includes immediate children, children of child
     * menu item groups but not items of a nested menu.).</p>
     * @param menu The menu for whose menu items the renderer is needed, may not
     * be null.
     * @return The MenuItemRenderer instance, may be null if styles indicate
     * that text and images should not be displayed.
     * @throws RendererException If there was a problem selecting an
     * appropriate renderer.
     */
    public MenuItemRenderer selectMenuItemRenderer(Menu menu)
        throws RendererException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 08-Apr-04	3514/3	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 26-Mar-04	3612/3	pduffin	VBM:2004032508 Corrections to MenuItemRenderer and removed some ambiguity in java doc

 26-Mar-04	3612/1	pduffin	VBM:2004032508 Definition of menu renderer API

 ===========================================================================
*/
