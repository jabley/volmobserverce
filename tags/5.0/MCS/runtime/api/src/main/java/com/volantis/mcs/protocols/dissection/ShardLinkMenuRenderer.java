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
package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBuffer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBufferLocator;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A decorating menu renderer which renders shard link conditional markup
 * around a standard menu renderer.
 * <p>
 * This is used to render menus for use in shard link menus.
 *
 * @see ShardLinkMenuModelBuilder
 * @todo only used by testcase now - delete
 */
public final class ShardLinkMenuRenderer implements MenuRenderer {

    /**
     * The copyright statement.
     */
    private static final String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The menu renderer which we delegate to to render the standard markup.
     */
    private final MenuRenderer delegate;

    /**
     * The locator.
     */
    private final MenuBufferLocator menuBufferLocator;

    /**
     * Construct an instance of this class.
     *
     * @param locator the locator to use to retrieve the buffer.
     * @param delegate the renderer which will render the standard markup for
     *      the menu.
     */
    public ShardLinkMenuRenderer(MenuBufferLocator locator,
                                 MenuRenderer delegate) {

        this.menuBufferLocator = locator;
        this.delegate = delegate;
    }

    // Javadoc inherited.
    public void render(Menu menu)
            throws RendererException {

        MenuBuffer menuBuffer = menuBufferLocator.getMenuBuffer(menu);
        DOMOutputBuffer buffer = (DOMOutputBuffer) menuBuffer.getOutputBuffer();

        // Downcast to the shard link specific menu so we can extract the
        // shard link group attributes we prepared earlier.
        ShardLinkMenu shardLinkMenu = (ShardLinkMenu) menu;

        // Open the SHARD LINK GROUP element.
        Element shardLinkGroup = buffer.openElement(
                DissectionConstants.SHARD_LINK_GROUP_ELEMENT);

        // Add the shard link group attributes we prepared earlier.
        shardLinkGroup.setAnnotation(
                shardLinkMenu.getShardLinkGroupAttributes());

        // Render the standard markup for the menu.
        delegate.render(menu);

        // Close the SHARD LINK GROUP element.
        buffer.closeElement(shardLinkGroup);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Mar-05	7357/2	pcameron	VBM:2005030906 Fixed node annotation for dissection

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 28-Apr-04	4048/3	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
