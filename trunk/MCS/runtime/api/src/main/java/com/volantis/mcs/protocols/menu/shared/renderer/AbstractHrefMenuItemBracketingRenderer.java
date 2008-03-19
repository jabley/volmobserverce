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
 * Base for those bracketing renderers that only render output if they have
 * a valid href.
 */
public abstract class AbstractHrefMenuItemBracketingRenderer
        implements MenuItemBracketingRenderer {

    /**
     * Return false if the menu item link could not be resolved and otherwise
     * delegates to {@link #open(OutputBuffer, MenuItem, String).
     */
    public boolean open(OutputBuffer buffer, MenuItem item)
            throws RendererException {

        String href = item.getHref().getURL();
        if (href == null) {
            return false;
        } else {
            return open(buffer, item, href);
        }
    }

    /**
     * Generate open markup for a fully resolved href.
     * @param buffer The target buffer.
     * @param item The menu item.
     * @param href The fully resolved href.
     * @return True if any content was rendered and false otherwise.
     * @throws RendererException If there was a problem rendering.
     */
    protected abstract
            boolean open(OutputBuffer buffer, MenuItem item, String href)
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

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
