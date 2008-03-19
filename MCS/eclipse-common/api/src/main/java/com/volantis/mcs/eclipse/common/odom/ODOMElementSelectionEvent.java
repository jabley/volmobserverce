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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common.odom;

import java.util.Arrays;
import java.util.List;


/**
 * A class holding an immutable list of ODOMElements which constitute this
 * event.
 */
public class ODOMElementSelectionEvent {
    /**
     * The ODOMElementSelection associated with this ODOMElementSelectionEvent.
     */
    private final ODOMElementSelection selection;

    /**
     * Create a selection event from a list of elements. Add the individual
     * items to the selection list after explicitly casting each item to an
     * ODOMElement (enforce type-checking).
     *
     * @param elements a <code>List</code> of <code>ODOMElement</code> objects
     *                 to which the event pertains.
     */
    public ODOMElementSelectionEvent(List elements) {
        this(new ODOMElementSelection(elements));
    }

    /**
     * Create a selection event from an array of elements.
     * @param elements an array of <code>ODOMElement</code> objects to which
     * the event pertains.
     */
    public ODOMElementSelectionEvent(ODOMElement[] elements) {
        this(new ODOMElementSelection(Arrays.asList(elements)));

    }

    /**
     * Create a selection event from an ODOMElementSelection.
     * @param selection The ODOMElementSelection.
     */
    public ODOMElementSelectionEvent(ODOMElementSelection selection) {
        this.selection = selection;
    }

    /**
     * Get the ODOMElementSelection for this ODOMElementSelectionEvent.
     * @return The ODOMElementSelection associated with this
     * ODOMElementSelectionEvent.
     */
    public ODOMElementSelection getSelection() {
        return selection;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jan-04	2618/1	allan	VBM:2004011510 Provide an IStructuredSelection for selected ODOMElements.

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 27-Nov-03	2036/1	byron	VBM:2003111902 Element Selection implementation - added testcase and fixed bugs

 24-Nov-03	2005/1	byron	VBM:2003112006 Eclipse to ODOM events

 20-Nov-03	1939/7	steve	VBM:2003111802 Enhance testsuite, code format and add new constructor to Event class

 19-Nov-03    1939/5    steve    VBM:2003111802 Next attempt to commit

 19-Nov-03    1939/3    steve    VBM:2003111802 ODOM Selection events

 ===========================================================================
*/
