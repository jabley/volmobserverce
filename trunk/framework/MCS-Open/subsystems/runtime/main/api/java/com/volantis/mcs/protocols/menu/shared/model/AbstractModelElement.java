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
import com.volantis.mcs.protocols.menu.model.ModelElement;

/**
 * This implementation provides access to the details about the PAPI element
 * associated with the menu entity that extends and realises this class.
 */
public abstract class AbstractModelElement implements ModelElement {

    /**
     * The information about the PAPI element associated with this entity.
     */
    private ElementDetails elementDetails;

    /**
     * Initialises a new instance of AbstractModelElement to allow element
     * details to be held.  This is used by menu entities.
     *
     * @param elementDetails The element details that should be used. May be null
     */
    protected AbstractModelElement(ElementDetails elementDetails) {
        this.elementDetails = elementDetails;
    }

    // JavaDoc inherited
    public ElementDetails getElementDetails() {
        return elementDetails;
    }

    /**
     * Used to update the element details stored in this instance.
     *
     * @param elementDetails The element information that should be used. May be null
     */
    public void setElementDetails(ElementDetails elementDetails) {
        this.elementDetails = elementDetails;
    }
    
/* Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        // This is the top level equals method implementation, so check that
        // the object to be compared is of the correct type
        if (this == o) {
            return true;
        } else if (o == null || !(o.getClass().equals(this.getClass()))) {
            return false;
        }

        final AbstractModelElement abstractModelElement = (AbstractModelElement) o;

        // Check the elementDetails for equality
        if ((elementDetails != null && !elementDetails.equals(abstractModelElement.elementDetails))
                || (elementDetails == null && abstractModelElement.elementDetails != null)) {
            return false;
        }

        // Must be equal if it gets to here :-)
        return true;
    }

    // JavaDoc inherited
    public int hashCode() {
        return (elementDetails != null ? elementDetails.hashCode() : 0);
    }
*/
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/3	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 10-Mar-04	3306/1	claire	VBM:2004022706 Implementation of the menu model

 ===========================================================================
*/
