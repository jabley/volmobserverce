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

import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;

/**
 * Menu items represent actionable entries in menus (directly or within menu
 * groups). These items have associated stylistic properties that can be used
 * to control behaviour and presentation.
 *
 * @mock.generate 
 */
public interface MenuItem extends MenuEntry, EventTarget, Titled {
    /**
     * A menu item's presentational information is stored in the associated
     * label.
     */
    /*# MenuLabel lnkMenuLabel; */

    /**
     * The menu provides a link from a given menu item to its containing menu
     * (this may be the item's immediate parent or could be arbitrarilly deeply
     * separated from the menu item by interceeding menu groups).
     */
    /*# Menu lnkMenu; */
    
    /**
     * Returns the menu label associated with this menu item.
     *
     * @return the label associated with the menu item. Will not be null
     */
    MenuLabel getLabel();

    /**
     * Returns the URL that is the target of this menu item.
     *
     * @return the URL that the menu item is targetted at
     */
    LinkAssetReference getHref();

    /**
     * Returns the segment (by name) at which this menu item is (optionally)
     * targetted. For use in a Montage context.
     *
     * @return the name of the segment to which the menu item is targetted
     */
    String getSegment();

    /**
     * Returns the optionally defined destination area for use in a WAPTV
     * context.
     *
     * @return the name of the destination area for this menu item
     */
    String getTarget();

    /**
     * Returns the prompt value associated with this menu. This can be a
     * Component ID or literal text.
     *
     * @return the prompt for this menu
     */
    TextAssetReference getPrompt();

    /**
     * Returns the shortcut associated with this menu item. This can be a
     * component ID or literal text.
     *
     * <p>This is also aliased from accessKey.</p>
     *
     * @return the shortcut or null if one has not been defined
     */
    TextAssetReference getShortcut();

    /**
     * Returns the menu (directly or indirectly) containing this menu item.
     *
     * @return the closest menu ancestor container for this menu item. Will not
     *         be null
     */
    Menu getMenu();

    /**
     * A means of checking whether the menu item is a representation for a
     * submenu or whether it is just a menu item.
     *
     * @return true if the item represents a submenu, false otherwise
     */
    boolean isSubMenuItem();
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 20-Apr-04	3945/1	claire	VBM:2004042006 Providing menu as menu item functionality

 26-Mar-04	3491/3	philws	VBM:2004031912 Add handling of title to Menu Label

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 15-Mar-04	3342/1	philws	VBM:2004022707 Review comment updates and changing MenuItem href to Object

 03-Mar-04	3288/3	philws	VBM:2004022702 Add Menu Model API

 ===========================================================================
*/
