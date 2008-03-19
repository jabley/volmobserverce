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

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.renderer.RendererException;

public interface MenuBracketingRenderer {

    /**
     * An instance that should be used anywhere that requires a non null
     * instance but which is never used.
     */
    public static final MenuBracketingRenderer UNUSED
            = new MenuBracketingRenderer() {

        public void open(OutputBuffer buffer, Menu menu)
                throws RendererException {

            throw new IllegalStateException("Renderer failed");
        }

        public void close(OutputBuffer buffer, Menu menu)
                throws RendererException {

            throw new IllegalStateException("Renderer failed");
        }
    };


    /**
     * Called before the menu item contents.
     *
     * @param buffer The target buffer.
     * @param menu The menu item being bracketed.
     * @throws RendererException If there was a problem rendering.
     */
    public void open(OutputBuffer buffer, Menu menu)
        throws RendererException;

    /**
     * Called after the menu item contents.
     *
     * @param buffer The target buffer.
     * @param menu The menu item being bracketed.
     * @throws RendererException If there was a problem rendering.
     */
    public void close(OutputBuffer buffer, Menu menu)
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

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 ===========================================================================
*/
