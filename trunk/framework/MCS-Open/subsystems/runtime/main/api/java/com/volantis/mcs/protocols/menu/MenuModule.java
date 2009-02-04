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

package com.volantis.mcs.protocols.menu;

import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.menu.shared.AbstractMenuModule;

/**
 * The part of the protocol that supports menus.
 */
public interface MenuModule {

    /**
     * Indicates that the protocol does not support menus.
     */
    public static final MenuModule UNSUPPORTED = new AbstractMenuModule() {

        /**
         * Return a null selector to indicate that menus are not supported.
         *
         * @return null. 
         */
        protected MenuRendererSelector createMenuRendererSelector() {
            return null;
        }
    };

    /**
     * This method provides a means of retrieving the current menu renderer
     * selector to be used when creating menus.  Only one instance is created
     * per initialisation of the <code>VolantisProtocol</code>.
     * @return
     */
    public MenuRendererSelector getMenuRendererSelector();

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
