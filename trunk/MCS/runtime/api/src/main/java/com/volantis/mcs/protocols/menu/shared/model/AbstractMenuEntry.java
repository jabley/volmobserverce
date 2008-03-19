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

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuEntry;

/**
 * Menus are hierarchies constructed from various flavours of menu entry. A
 * menu entry structure may be visited using a
 * {@link com.volantis.mcs.protocols.menu.model.MenuModelVisitor MenuModelVistor}
 */
public abstract class AbstractMenuEntry extends AbstractModelElement
    implements MenuEntry, MutablePaneTargeted {

    /**
     * The containing menu entry provides a means for traversal from a child
     * menu entry to its parent.  This may be null if this menu entry is the
     * root of the menu structure.
     */
    private MenuEntry container;

    /**
     * The pane that this menu is targeted at.
     */
    protected FormatReference pane;

    /**
     * Initialises a new instance of AbstractMenuEntry for use in a menu
     * hierarchy.
     *
     * @param elementDetails information about the PAPI element that is
     *                       associated with this menu entry. Must not be null.
     */
    protected AbstractMenuEntry(ElementDetails elementDetails) {
        super(elementDetails);

        if (elementDetails == null) {
            throw new IllegalArgumentException("elementDetails must not be null");
        }
    }

    // JavaDoc inherited
    public MenuEntry getContainer() {
        return container;
    }

    /**
     * Sets the container that this menu entry is held within.  It is valid
     * to call this with a null parameter if this menu entry is the root of
     * the menu model.
     * <p>
     * This method is intentionally package protected to prevent any
     * specializations outside of this package using it either directly
     * or through overriding.
     *
     * @param container The parent of this menu entry
     */
    void setContainer(MenuEntry container) {
        this.container = container;
    }

    // JavaDoc inherited
    public FormatReference getPane() {
        return pane;
    }

    // JavaDoc inherited
    public void setPane(FormatReference pane) {
        this.pane = pane;
    }

/* Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        // This class extends one that provides its own equals implementation,
        // so call it
        if (!super.equals(o)) {
            return false;
        }

        final AbstractMenuEntry abstractMenuEntry = (AbstractMenuEntry) o;

        // Don't check the container for equality - will cause nasty recursion

        // The containers are equal so check the pane for equality
        if ((pane != null && !pane.equals(abstractMenuEntry.pane)) ||
                (pane == null && abstractMenuEntry.pane != null)) {
            return false;
        }

        // Must be equal if it gets to here :-)
        return true;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result = super.hashCode();
        // Not calling container.hashCode() because of interesting recursion
        result = 29 * result + (container != null ? 10000 : 0);
        result = 29 * result + (pane != null ? pane.hashCode() : 0);
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

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 23-Mar-04	3491/2	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 11-Mar-04	3306/7	claire	VBM:2004022706 Removed uncessary specialisation equals and hashcode methods

 11-Mar-04	3306/5	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 10-Mar-04	3306/1	claire	VBM:2004022706 Implementation of the menu model

 ===========================================================================
*/
