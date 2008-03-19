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
 * A menu group contains one or more menu items and may itself appear in a
 * menu. They are used to group together a set of related menu items and may
 * have some form of (visual) delimitation controlled by stylistic properties.
 */
public interface MenuItemGroup extends MenuEntry {
    /**
     * @labelDirection forward
     * @directed
     * @supplierRole items
     * @supplierCardinality 1..* 
     * @link aggregationByValue
     */
    /*# MenuItem lnkMenuItem; */

    /**
     * Returns the number of immediate child menu items contained in this
     * menu group.
     *
     * @return the number of immediate items in this menu group (may not be
     *         zero)
     */
    int getSize();

    /**
     * Returns the specified child of this menu group.
     *
     * @param index the index of the child to return, in the range
     *              [0 .. size() - 1]
     * @return the requested child of this menu group
     * @throws IndexOutOfBoundsException if the specified index is not within
     *                                   range
     */
    MenuItem get(int index) throws IndexOutOfBoundsException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Mar-04	3288/1	philws	VBM:2004022702 Add Menu Model API

 ===========================================================================
*/
