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

import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * Implementations of this are responsible for rendering menu items.
 * <p>A single instance will be used to render all the immediate child menu
 * items for the same menu.</p>
 * <p>Where possible implementations should not contain any information that
 * would prevent it being used by multiple threads.</p>
 */
public interface MenuItemRenderer {

    /**
     * Adds markup for the menu item to the specified buffer.
     * @param locator The locator that can be queried for the buffer(s) to use.
     * @param item The menu item to render.
     */
    public void render(MenuBufferLocator locator,
                       MenuItem item)
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

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 08-Apr-04	3514/3	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 26-Mar-04	3612/3	pduffin	VBM:2004032508 Corrections to MenuItemRenderer and removed some ambiguity in java doc

 26-Mar-04	3612/1	pduffin	VBM:2004032508 Definition of menu renderer API

 ===========================================================================
*/
