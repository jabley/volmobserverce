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

import java.util.List;

/**
 * Definition of an object that handles ODOMElement selections. Implementors
 * of this interface manage ODOMElementSelectionListener objects allowing
 * addition and removal of listeners. The usage of the listeners is not
 * defined by this interface. 
 */
public interface ODOMElementSelectionProvider {
    
    /** 
     * Permits a listener to be added to the set of listeners that will be
     * notified of selection changes by the {@link #update} method. Does
     * nothing if the listener is already listening.
     *
     * @param listener the listener to add
     * @see ODOMElementSelectionListener
     */
    void addSelectionListener(ODOMElementSelectionListener listener);

    /** 
     * Permits a listener to be removed to the set of listeners that will be
     * notified of selection changes by the {@link #update} method. Does
     * nothing if the listener is not currently listening.
     *
     * @param listener the listener to remove
     * @see ODOMElementSelectionListener
     */
    void removeSelectionListener(ODOMElementSelectionListener listener);

    /**
     * Notifies the registered listeners of a selection update. This is not
     * intended for external invocation, but rather by the
     * {@link ODOMSelectionManager}.
     *
     * @param elements the set of elements selected
     */
    void update(List elements);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-03	2064/1	philws	VBM:2003112901 Correct the result of resolved selections

 20-Nov-03	1960/2	steve	VBM:2003111902 Classes Renamed

 19-Nov-03	1939/1	steve	VBM:2003111802 ODOM Selection events

 ===========================================================================
*/
