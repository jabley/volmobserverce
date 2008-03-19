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
 * Menus are hierarchies constructed from various flavours of menu entry. A
 * menu entry structure may be visited using a {@link MenuModelVisitor}.
 */
public interface MenuEntry extends ModelElement, PaneTargeted {
    /**
     * The containing menu entry provides a means for traversal from a child
     * menu entry to its parent.
     *
     * @supplierRole container
     * @labelDirection forward
     * @directed
     * @supplierCardinality 0..1
     */
    /*# MenuEntry lnkMenuEntry; */

    /**
     * A visitation of this entry is requested by calling this method. The
     * specified visitor will be called back, in some way, with the entry.
     *
     * @param visitor the visitor that should be called back with this entry
     * @throws MenuModelVisitorException if there is a problem during the
     *                                   visitation
     */
    void accept(MenuModelVisitor visitor) throws MenuModelVisitorException;

    /**
     * Returns the menu entry containing this menu entry or null if there is
     * none.
     *
     * @return the menu entry containing this menu entry. May be null
     */
    MenuEntry getContainer();

    // Other JavaDoc inherited
    /**
     * @return the details of the PAPI element that is associated with the #
     * menu entry. Will not be null
     */
    ElementDetails getElementDetails();
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 05-Apr-04	3733/1	philws	VBM:2004040504 Added MenuModelVisitorException

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 03-Mar-04	3288/3	philws	VBM:2004022702 Add Menu Model API

 ===========================================================================
*/
