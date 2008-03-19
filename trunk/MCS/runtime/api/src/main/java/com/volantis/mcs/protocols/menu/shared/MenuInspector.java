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
package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSMenuItemIteratorAllocationKeywords;
import com.volantis.styling.values.PropertyValues;

/**
 * Provides various methods that can be used to examine a menu to determine
 * specific, <strong>derived</strong> information about that menu.
 */
public class MenuInspector {

    /**
     * Returns true if the specified menu is correctly configured to perform
     * automatic iteration allocation.
     *
     * @param menu the menu to be tested
     * @return true if the menu is correctly configured to perform automatic
     *         iteration allocation, false otherwise
     */
    public boolean isAutoIterationAllocating(Menu menu) {
        boolean result = false;
        FormatReference menuReference = menu.getPane();

        if (menuReference != null) {
            int dimensions = menuReference.getIndex().getDimensions();

            if ((dimensions > 0) &&
                    (menuReference.getIndex().getSpecified() <
                    dimensions)) {
                // The menu is targeted at a pane that is spatially
                // iterated but not fully specified
                final PropertyValues properties = menu.getElementDetails().
                        getStyles().getPropertyValues();

                StyleValue keyword = properties.getComputedValue(
                        StylePropertyDetails.MCS_MENU_ITEM_ITERATOR_ALLOCATION);
                if (keyword == MCSMenuItemIteratorAllocationKeywords.AUTOMATIC) {
                    // The entry's menu is performing automatic iteration
                    // allocation
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * Returns true if the specified menu's direct container is a menu
     * <strong>AND</strong> is correctly configured to perform automatic
     * iteration allocation.
     *
     * @param menu The menu to be tested
     * @return true if the menu's direct parent (a menu) is correctly
     * configured to perform automatic iteration allocation, false otherwise
     */
    public boolean isParentAutoIterating(Menu menu) {
        boolean result = false;

        // Check the containing item if it is a menu
        MenuEntry parent = menu.getContainer();
        if (parent != null && (parent instanceof Menu)) {
            result = isAutoIterationAllocating((Menu)parent);
        }

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 13-May-04	4297/5	claire	VBM:2004051110 Enhanced Menu: Bugs: Automatic allocation in spatial iterators failing

 07-May-04	4204/1	philws	VBM:2004042810 Suppress Menu Item Group separators when needed

 ===========================================================================
*/
