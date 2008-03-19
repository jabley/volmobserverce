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
 * Allows a menu to be rendered using a specified markup generator.
 *
 * @mock.generate 
 */
public interface MenuRenderer {

    /**
     * Render the menu.
     * @param menu The menu to render.
     * @throws RendererException If there was a problem rendering the menu.
     */
    public void render(Menu menu)
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

 19-Mar-04	3478/3	claire	VBM:2004031805 Update JavaDoc

 19-Mar-04	3478/1	claire	VBM:2004031805 Removed MenuMarkupGenerator and associated code

 11-Mar-04	3274/2	pduffin	VBM:2004022704 Initial API for menu renderers and markup generators

 ===========================================================================
*/
