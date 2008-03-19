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

package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitor;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitorException;

public abstract class AbstractMenuModelVisitor
        implements MenuModelVisitor {

    /**
     * Visit the children of the menu.
     *
     * @param menu The menu whose children are to be visited.
     *
     * @throws com.volantis.mcs.protocols.menu.model.MenuModelVisitorException If a problem was encountered while
     * visting the children.
     */
    protected void visitChildren(Menu menu)
            throws MenuModelVisitorException {

        int numChildren = menu.getSize();
        for (int i = 0; i < numChildren; i++) {
            menu.get(i).accept(this);
        }
    }

    /**
     * Visit the children of the menu item group.
     *
     * @param group The menu item group whose children are to be visited.
     *
     * @throws MenuModelVisitorException If a problem was encountered while
     * visting the children.
     */
    protected void visitChildren(MenuItemGroup group)
            throws MenuModelVisitorException {

        int numChildren = group.getSize();
        for (int i = 0; i < numChildren; i++) {
            group.get(i).accept(this);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-May-04	4164/1	pduffin	VBM:2004050404 Refactored DefaultMenuRenderer internal visitor class to allow pre and post processing.

 ===========================================================================
*/
