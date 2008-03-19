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
 * Delegates methods to instance passed into the constructor.
 */
public class DelegatingMenuItemBracketingRenderer
        implements MenuItemBracketingRenderer {

    /**
     * The renderer to delegate to.
     */
    private final MenuItemBracketingRenderer delegate;

    /**
     * Initialise.
     * @param delegate The renderer to delegate to.
     */
    protected DelegatingMenuItemBracketingRenderer(MenuItemBracketingRenderer delegate) {
        this.delegate = delegate;
    }

    /**
     * Delegate.
     */
    public boolean open(OutputBuffer buffer, MenuItem item)
            throws RendererException {
        return delegate.open(buffer, item);
    }

    /**
     * Delegate.
     */
    public void close(OutputBuffer buffer, MenuItem item)
            throws RendererException {
        delegate.close(buffer, item);
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

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
