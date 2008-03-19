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

package com.volantis.mcs.protocols.menu.model;

/**
 * An implementation of this interface can be used to handle traversal of a
 * menu model instance hierarchy.
 */
public interface MenuModelVisitor {
    /**
     * Called when a menu item is found during a menu model traversal.
     *
     * @param item the menu item that has been found
     * @throws MenuModelVisitorException if there is a problem during the
     *                                   visitation
     */
    void visit(MenuItem item) throws MenuModelVisitorException;

    /**
     * Called when a menu group is found during a menu model traversal.
     *
     * @param group the menu group that has been found
     * @throws MenuModelVisitorException if there is a problem during the
     *                                   visitation
     */
    void visit(MenuItemGroup group) throws MenuModelVisitorException;

    /**
     * Called when a menu is found during a menu model traversal.
     *
     * @param menu the menu that has been found
     * @throws MenuModelVisitorException if there is a problem during the
     *                                   visitation
     */
    void visit(Menu menu) throws MenuModelVisitorException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Apr-04	3733/1	philws	VBM:2004040504 Added MenuModelVisitorException

 10-Mar-04	3306/1	claire	VBM:2004022706 Implementation of the menu model

 03-Mar-04	3288/1	philws	VBM:2004022702 Add Menu Model API

 ===========================================================================
*/
