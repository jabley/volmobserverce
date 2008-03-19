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
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitorException;

/**
 * Provides a partial implementation of
 * {@link com.volantis.mcs.protocols.menu.model.MenuModelVisitor MenuModelVisitor}
 * for handling traversals of a menu model instance hierarchy.  The actual
 * code called when visiting needs to be provided by specializations of this
 * class.
 */
public abstract class AbstractMenuModelHandler
        extends AbstractMenuModelVisitor {

    /**
     * Called when a menu item is found during a menu model traversal.
     *
     * @param item the menu item that has been found
     * @throws MenuModelVisitorException if there is a problem during the
     *                                   visitation
     */
    public void visit(MenuItem item) throws MenuModelVisitorException {
        handle(item);
        // There are no children with a MenuItem...
    }

    /**
     * Called when a menu group is found during a menu model traversal.
     *
     * @param group the menu group that has been found
     * @throws MenuModelVisitorException if there is a problem during the
     *                                   visitation
     */
    public final void visit(MenuItemGroup group)
            throws MenuModelVisitorException {
        if (handle(group)) {
            visitChildren(group);
        }
    }

    /**
     * Called when a menu is found during a menu model traversal.
     *
     * @param menu the menu that has been found
     * @throws MenuModelVisitorException if there is a problem during the
     *                                   visitation
     */
    public void visit(Menu menu) throws MenuModelVisitorException {
        if (handle(menu)) {
            visitChildren(menu);
        }
    }

    /**
     * This method actually visits the menu item when doing a menu model
     * traversal.  Its return value indicates whether any children should
     * be visited.  In the case of a MenuItem this should always return false
     * as true has no meaning.
     *
     * @param item The menu item to visit
     * @return     false if children should not be visited, true if they should
     * @throws MenuModelVisitorException if there is a problem during the
     *                                   handling
     */
    protected abstract boolean handle(MenuItem item)
            throws MenuModelVisitorException;

    /**
     * This method actually visits the menu item group when doing a menu model
     * traversal.  Its return value indicates whether any children should
     * be visited.
     *
     * @param group The menu item to visit
     * @return      false if children should not be visited, true if they should
     * @throws MenuModelVisitorException if there is a problem during the
     *                                   handling
     */
    protected abstract boolean handle(MenuItemGroup group)
            throws MenuModelVisitorException;

    /**
     * This method actually visits the menu when doing a menu model
     * traversal.  Its return value indicates whether any children should
     * be visited.
     *
     * @param menu The menu item to visit
     * @return     false if children should not be visited, true if they should
     * @throws MenuModelVisitorException if there is a problem during the
     *                                   handling
     */
    protected abstract boolean handle(Menu menu)
            throws MenuModelVisitorException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-May-04	4164/1	pduffin	VBM:2004050404 Refactored DefaultMenuRenderer internal visitor class to allow pre and post processing.

 05-Apr-04	3733/1	philws	VBM:2004040504 Added MenuModelVisitorException

 11-Mar-04	3306/5	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
