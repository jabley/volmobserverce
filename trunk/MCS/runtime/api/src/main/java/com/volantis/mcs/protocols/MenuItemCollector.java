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
 * $Header: /src/voyager/com/volantis/mcs/protocols/MenuItemCollector.java,v 1.2 2003/04/11 09:15:01 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * -----------  -------------   -----------------------------------------------
 * 28-Mar-2003  Sumit           VBM:2003032714 - Abstraction of the menu item
 *                              addition interface
 * 28-Mar-2003  Sumit           VBM:2003032713 - Fixed Class comment
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import java.util.Collection;

/**
 * This is an abstraction fo the collection of menu items by either a menu item
 * group or a menu
 */
public interface MenuItemCollector {
    /**
     * Add a menu item to the collection of menu itemsAndGroups.
     */
    public abstract void addItem(MenuItem item);
    /**
     * Get the collection of menu itemsAndGroups.
     */
    public abstract Collection getItems();
    
    /**
     * Add a mneu group to the list iof menu itemsAndGroups
     */
    public abstract void addGroup(MenuItemGroupAttributes item);
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
