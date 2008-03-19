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
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemBracketingRenderer;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A decorating menu item renderer which renders shard link conditional markup
 * around a standard menu item renderer.
 * <p>
 * This is used to render menu items for use in shard link menus.
 *
 * @see ShardLinkMenuModelBuilder
 */
public final class ShardLinkMenuItemRenderer
        implements MenuItemBracketingRenderer {

    /**
     * The copyright statement.
     */
    private static final String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The item renderer which we delegate to to render the standard markup.
     */
    private final MenuItemBracketingRenderer delegate;

    /**
     * Construct an instance of this class.
     *
     * @param delegate the renderer which will render the standard markup for
     *      the menu item.
     */
    public ShardLinkMenuItemRenderer(MenuItemBracketingRenderer delegate) {
        this.delegate = delegate;
    }

    /**
     * Start a SHARD LINK ELEMENT element and open the delegate.
     */
    // Other javadoc inherited
    public boolean open(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;

        // Downcast to the shard link specific menu item so we can extract the
        // shard link attributes we prepared earlier.
        ShardLinkMenuItem shardLinkMenuItem = (ShardLinkMenuItem) item;

        // Open the SHARD LINK element.
        Element element;
        element = dom.openElement(DissectionConstants.SHARD_LINK_ELEMENT);

        // Add the shard link attributes we prepared earlier.
        element.setAnnotation(shardLinkMenuItem.getShardLinkAttributes());

        return delegate.open(buffer, item);
    }

    /**
     * Close the delegate and end the SHARD LINK ELEMENT element.
     */
    // Other javadoc inherited
    public void close(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        delegate.close(buffer, item);

        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;
        dom.closeElement(DissectionConstants.SHARD_LINK_ELEMENT);
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

 13-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
