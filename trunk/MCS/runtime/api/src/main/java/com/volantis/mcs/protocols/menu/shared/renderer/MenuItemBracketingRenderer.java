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
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * Instances of this can bracket the contents of a menu item within some
 * markup.
 */
public interface MenuItemBracketingRenderer {

    /**
     * A bracketing renderer that renders nothing itself but does allow the
     * content it is bracketing to be rendered.
     */
    public static final MenuItemBracketingRenderer NULL
            = new MenuItemBracketingRenderer() {

        public boolean open(OutputBuffer buffer, MenuItem item)
                throws RendererException {

            return true;
        }

        public void close(OutputBuffer buffer, MenuItem item)
                throws RendererException {
        }
    };

    /**
     * Called before the menu item contents.
     *
     * <p>This method can determine whether the content that it brackets is
     * written out or not. It does this using its return value. If it returns
     * true then the contents is written and {@link #close} is called,
     * otherwise the contents are not written and {@link #close} is not called,
     * </p>
     *
     * @param buffer The target buffer.
     * @param item The menu item being bracketed.
     * @return An indication as to whether the contents that this renderer is
     * bracketing should be written out.
     * @throws RendererException If there was a problem rendering..
     */
    public boolean open(OutputBuffer buffer, MenuItem item)
        throws RendererException;

    /**
     * Called after the menu item contents.
     *
     * @param buffer The target buffer.
     * @param item The menu item being bracketed.
     * @throws RendererException If there was a problem rendering..
     */
    public void close(OutputBuffer buffer, MenuItem item)
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

 30-Apr-04	4013/3	pduffin	VBM:2004042210 Javadoc improvements

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
