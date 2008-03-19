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
 * $Header: /src/voyager/com/volantis/mcs/papi/MenuItemCollectorContainer.java,v 1.1 2003/04/08 11:37:44 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 31-Mar-2003  Sumit       VBM:2003032714 - Interface that allows implementors 
 *                          to return their enclosed protocol attributes
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.papi;

/**
 * Interface that allows implementors to return their enclosed protocol attributes
 * @todo: ensure that this interface is defuncted once the implementation of enhanced menus is completed
 * @todo: this will have a knock-on effect on some test cases which need rewriting or defuncting. 
 */
interface MenuItemCollectorContainer {
    
   
    /**
     * Get implementor's element's protocol attributes.
     */
    com.volantis.mcs.protocols.MenuItemCollector getMenuItemCollector ();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Mar-04	3412/1	claire	VBM:2004031201 Early implementation of new menus in PAPI

 ===========================================================================
*/
