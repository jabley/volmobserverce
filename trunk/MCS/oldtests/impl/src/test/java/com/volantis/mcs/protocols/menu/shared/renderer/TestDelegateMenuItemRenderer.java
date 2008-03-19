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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A menu item renderer which is useful for testing menu item renderers which 
 * contain other menu item renderers when you do not have a complete menu 
 * (item) available for testing. 
 * <p>
 * This just renders a fixed string regardless of the content of the menu item,
 * which allows you to use stub content for the menu item.
 */ 
public class TestDelegateMenuItemRenderer implements MenuItemRenderer {
    
    private String content;

    public TestDelegateMenuItemRenderer(String content) {
        this.content = content;
    }

    // Javadoc inherited.
    public void render(MenuBufferLocator locator,
                       MenuItem item)
            throws RendererException {
        
        locator.getMenuBuffer(item).getOutputBuffer().
                writeText(content);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 26-Apr-04	3920/2	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/
