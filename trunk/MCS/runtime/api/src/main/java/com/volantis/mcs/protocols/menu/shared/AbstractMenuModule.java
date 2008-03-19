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

package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;

/**
 * Base class of all the <code>MenuModule</code> implementations.
 */
public abstract class AbstractMenuModule
        implements MenuModule {

    /**
     * The instance of <code>MenuRendererSelector</code> to be used for the
     * current initialisation of VolantisProtocol.  This is initialised lazily
     * in {@link #getMenuRendererSelector} as required, and relies on the
     * protocol specific implementations that are accessed through
     * {@link #getMenuRendererSelector}.
     *
     * <p>This is initialised to {@link MenuRendererSelector#NULL} because
     * the null value is used to indicate that the protocol does not support
     * menus at all.</p>
     */
    private MenuRendererSelector menuRendererSelector
            = MenuRendererSelector.NULL;

    // Javadoc inherited.
    public MenuRendererSelector getMenuRendererSelector() {
        if (menuRendererSelector == MenuRendererSelector.NULL) {
            menuRendererSelector = createMenuRendererSelector();
        }
        return menuRendererSelector;
    }

    /**
     * This method returns a protocol specific instance of a
     * <code>MenuRendererSelector</code>.  This is required for producing the
     * correct markup for menus for a given protocol.
     *
     * @return A menu renderer selector that can provide suitable menu
     * renderers.  This may be null if the protocol does not suupport menus.
     */
    protected abstract MenuRendererSelector createMenuRendererSelector();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
