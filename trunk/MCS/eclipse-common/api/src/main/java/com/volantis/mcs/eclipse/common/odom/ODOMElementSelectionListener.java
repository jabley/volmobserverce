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

/**
 * Definition of a listener that is interested in ODOM Element selections.
 * The selectionChanged method in implementors of this method will be called
 * with an <code>ODOMElementSelectionEvent</code> containing a list
 * of selected ODOMElement objects.
 */
public interface ODOMElementSelectionListener {
    
    /**
     * Receives an ODOMElementSelectionEvent. 
     * @see ODOMElementSelectionEvent
     */
    void selectionChanged(ODOMElementSelectionEvent event);
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-03	1939/1	steve	VBM:2003111802 ODOM Selection events

 ===========================================================================
*/
