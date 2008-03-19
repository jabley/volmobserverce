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
 * Provides access to details relating to the action being performed. An
 * instance of this interface is passed by the ODOMAction to the
 * ODOMActionCommand.
 */
public interface ODOMActionDetails {
    /**
     * Returns the number of elements in the selection.
     *
     * @return the number of elements in the selection
     */
    int getNumberOfElements();

    /**
     * Returns the <dfn>i</dfn>th element from the selection.
     *
     * @param i the selection index. Must be &gt;= 0 and &lt; {@link
     *          #getNumberOfElements}
     * @return the <dfn>i</dfn>th selected element
     */
    ODOMElement getElement(int i);

    /**
     * Returns a copy of the underlying element selection data. Will not
     * return null.
     *
     * @return a copy of the underlying element selection data
     */
    ODOMElement[] getElementsClone();
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Feb-04	2939/1	eduardo	VBM:2004020506 ODOM DeleteActionCommand changed to be undo/redo friendly

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
