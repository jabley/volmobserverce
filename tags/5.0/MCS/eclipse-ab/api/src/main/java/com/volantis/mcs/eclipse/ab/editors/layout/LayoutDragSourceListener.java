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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

/**
 * DragSourceListener for dragging layout elements.
 */
public class LayoutDragSourceListener extends DragSourceAdapter {

    /**
     * The Transfer associated with this LayoutDragSourceListener.
     */
    private final Transfer transfer;

    /**
     * The ODOMActionDetails associated with this LayoutDragSourceListener.
     */
    private final ODOMActionDetails actionDetails;

    /**
     * Construct a new LayoutDragSourceListener.
     * @param transfer the Transfer detailing the kinds of transfer this
     * listener is interested in
     * @param actionDetails the ODOMActionDetails that provides the
     * ODOMElements to drag.
     */
    public LayoutDragSourceListener(Transfer transfer,
                                    ODOMActionDetails actionDetails) {
        assert(transfer != null);
        assert(actionDetails != null);
        this.transfer = transfer;
        this.actionDetails = actionDetails;
    }

    // javadoc inherited
    public void dragStart(DragSourceEvent event) {
        // Only start the drag if there is a single element selected
        event.doit = actionDetails.getNumberOfElements() == 1;
    }

    // javadoc inherited
    public void dragSetData(DragSourceEvent event) {
        // Provide the data of the requested type.
        if (transfer.isSupportedType(event.dataType)) {
            ODOMElement elements [] = actionDetails.getElementsClone();
            event.data = elements;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/4	allan	VBM:2005012102 Drag n Drop support

 ===========================================================================
*/
