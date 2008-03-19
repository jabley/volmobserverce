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
package com.volantis.mcs.eclipse.ab.actions;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;

/**
 * An implementation of ODOMActionDetails based on an array.
 */
public class ArrayBasedActionDetails implements ODOMActionDetails {

    /**
     * immutable array constant can avoid some garbage
     */
    private static final ODOMElement[] NO_ELEMENTS = new ODOMElement[0];

    /**
     * The element selections associated with the actionContext.
     * Can be null.
     */
    private ODOMElement[] elements;

    /**
     * Initializes the new instance using the given parameters.
     */
    public ArrayBasedActionDetails() {
        this.elements = NO_ELEMENTS;
    }

    // javadoc inherited
    public int getNumberOfElements() {
        return elements.length;
    }

    // javadoc inherited
    public ODOMElement getElement(int i) {
        return elements[i];
    }

    // javadoc inherited
    public ODOMElement[] getElementsClone() {
        if (elements.length > 0) {
            return (ODOMElement[]) elements.clone();
        } else {
            return NO_ELEMENTS;
        }
    }

    /**
     * Used to (re-)set the element selection array for this actionContext.
     * The given array is simply held by reference, thus avoiding garbage
     *
     * @param elements the new selection. May be null
     */
    public void setElements(ODOMElement[] elements) {
        this.elements = elements != null ? elements : NO_ELEMENTS;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 ===========================================================================
*/
