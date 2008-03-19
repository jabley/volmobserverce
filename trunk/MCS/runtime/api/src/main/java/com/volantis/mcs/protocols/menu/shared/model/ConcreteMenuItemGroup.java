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

import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitor;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitorException;

import java.util.ArrayList;
import java.util.List;


/**
 * A menu group contains one or more menu items and may itself appear in a
 * menu. They are used to group together a set of related menu items and may
 * have some form of (visual) delimitation controlled by stylistic properties.
 */
public final class ConcreteMenuItemGroup extends AbstractMenuEntry
                                   implements MenuItemGroup {

    /**
     * A menu item group can contain an arbitrary number of menu items.
     */
    private final List menuItems;

    /**
     * Initialises a new instance of a menu item group with the information
     * about the associated PAPI element that was provided.
     *
     * @param elementDetails information about the associated PAPI element
     */
    public ConcreteMenuItemGroup(ElementDetails elementDetails) {
        super(elementDetails);
        menuItems = new ArrayList();
    }

    // JavaDoc inherited
    public void accept(MenuModelVisitor visitor)
            throws MenuModelVisitorException {
        visitor.visit(this);
    }

    // JavaDoc inherited
    public  MenuItem get(int index) throws IndexOutOfBoundsException {
        return (MenuItem) menuItems.get(index);
    }

    /**
     * Add another entry to the collection of objects held by this menu item
     * group.  This will set the parent of the entry being added to the
     * instance of this class that will hold it.
     *
     * @param newEntry The entry to add
     */
    public void add(MenuItem newEntry) {
        menuItems.add(newEntry);
        ((ConcreteMenuItem)newEntry).setContainer(this);
    }

    // JavaDoc inherited
    public int getSize() {
        return menuItems.size();
    }

/* Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        // This class extends one that provides its own equals implementation,
        // so call it
        if (!super.equals(o)) {
            return false;
        }

        final ConcreteMenuItemGroup concreteMenuItemGroup =
                (ConcreteMenuItemGroup) o;

        // Test menuItems for equality
        if ((menuItems != null &&
                !menuItems.equals(concreteMenuItemGroup.menuItems)) ||
                (menuItems == null &&
                    concreteMenuItemGroup.menuItems != null)) {
            return false;
        }

        // Must be equal if it gets to here :-)
        return true;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (menuItems != null ?
                                menuItems.hashCode() : 0);
        return result;
    }
*/
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 05-Apr-04	3733/1	philws	VBM:2004040504 Added MenuModelVisitorException

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
