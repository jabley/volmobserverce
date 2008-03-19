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
/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuItem;

/**
 */
public class ConcreteMenuItemGroupTestCase
        extends AbstractMenuEntryTestAbstract {

    /**
     * This method tests the getSize() method in the MenuItemGroup interface as
     * well as the add() method in ConcreteMenuItemGroup.
     */
    public void testGetSize() {
        ConcreteMenuItemGroup itemGroup = createBasicMenuItemGroup(false);

        ConcreteMenuItem[] items = MenuModelHelper.createItemArray();

        // Test no items
        assertEquals("Should be no items in the list", 0, itemGroup.getSize());

        // Test one item
        itemGroup.add(items[0]);
        assertEquals("Should be one item in the list", 1, itemGroup.getSize());

        // Test multipleItems
        itemGroup.add(items[1]);
        assertEquals("Should be many items in the list",
                2, itemGroup.getSize());
        itemGroup.add(items[2]);
        assertEquals("Should be many items in the list",
                3, itemGroup.getSize());
    }

    /**
     * A utility method to create a basic instance of a menu item group
     * with a suitably initialised menu parent.  This is needed to prevent
     * problems when adding menu items to the group during testing which tests
     * for an legal menu parent.
     *
     * @param fill If true the group is populated with some sample menu items,
     *             otherwise it is returned with no children.
     * @return     An instance of ConcreteMenuItemGroup with a valid menu
     *             container
     */
    private ConcreteMenuItemGroup createBasicMenuItemGroup(boolean fill) {
        return MenuModelHelper.createMenuItemGroup(fill,
                        MenuModelHelper.createMenu(false, null));
    }

    /**
     * This method tests the get() method
     */
    public void testGet() {
        ConcreteMenuItemGroup itemGroup = createBasicMenuItemGroup(true);
        int maxItems = itemGroup.getSize();

        // Test get within bounds
        MenuItem item = itemGroup.get(1);
        assertNotNull("Menu item should not be null", item);

        // Test get out of bounds
        try {
            item = itemGroup.get(maxItems + 1);
            fail("get() call should have thrown an exception");
        } catch (IndexOutOfBoundsException iob) {
            // Test success - this exception should have been thrown
        }
    }

    // JavaDoc inherited
    protected AbstractModelElement createTestInstance(
            ElementDetails elementDetails) {
        return new ConcreteMenuItemGroup(elementDetails);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Mar-04	3274/1	pduffin	VBM:2004022704 Initial API for menu renderers and markup generators

 11-Mar-04	3306/7	claire	VBM:2004022706 Removed uncessary specialisation equals and hashcode methods

 11-Mar-04	3306/5	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
