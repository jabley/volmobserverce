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

import com.volantis.mcs.protocols.menu.model.MenuEntry;

/**
 * A menu buffer locator that returns the same <code>MenuBuffer</code> for the
 * whole menu.
 *
 * <p>This wraps another <code>MenuBufferLocator</code> and uses the menu
 * buffer that the wrapped locator returns for the menu. This has the effect
 * of causing the whole menu to be rendered atomically into that buffer.</p>
 */
public class SingleMenuBufferLocator
        implements MenuBufferLocator {

    /**
     * The locator to which the request is delegated.
     */
    private final MenuBuffer buffer;

    /**
     * Initialise.
     * @param buffer The buffer that this object will return.
     */
    public SingleMenuBufferLocator(MenuBuffer buffer) {
        this.buffer = buffer;
    }

    // Javadoc inherited.
    public MenuBuffer getMenuBuffer(MenuEntry entry) {
        return buffer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 ===========================================================================
*/
