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

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorManager;

public final class DefaultMenuItemRenderer
    implements MenuItemRenderer {

    /**
     * The renderer responsible for rendering the component or components of
     * the menu item.
     */
    private final MenuItemComponentRenderer renderer;

    /**
     * A buffer for temporarily storing the result of rendering the menu item.
     */
    private final OutputBuffer temporaryBuffer;

    public DefaultMenuItemRenderer(MenuItemComponentRenderer renderer,
                                   OutputBufferFactory factory) {

        this.renderer = renderer;

        temporaryBuffer = factory.createOutputBuffer();
    }

    public void render(MenuBufferLocator locator,
                       MenuItem item)
        throws RendererException {

        // Get the menu buffer from the locator.
        MenuBuffer menuBuffer = locator.getMenuBuffer(item);

        // This check is necessary in case the menuBuffer is null.
        if (menuBuffer != null) {
            // Render the menu item into the temporary buffer.
            MenuItemRenderedContent componentType
                    = renderer.render(temporaryBuffer, item);

            // Get the SeparatorManager from the MenuBuffer.
            SeparatorManager separatorManager
                    = menuBuffer.getSeparatorManager();

            // Notify the manager of the componentType of content that we are
            // about to write to its managed OutputBuffer.
            separatorManager.beforeContent(componentType);

            // Get the OutputBuffer.
            OutputBuffer outputBuffer = separatorManager.getOutputBuffer();

            // Append the contents of temporary OutputBuffer to the managed one.
            outputBuffer.transferContentsFrom(temporaryBuffer);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4274/2	claire	VBM:2004051101 Enhanced Menus: Fix explicit format null pointer on menu buffers

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
