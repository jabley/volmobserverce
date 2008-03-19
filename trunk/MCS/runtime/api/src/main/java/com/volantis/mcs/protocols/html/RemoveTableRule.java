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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.trans.TransTable;

/**
 * Interface for determined whether or not to remove a table during
 * transformation.
 */
public interface RemoveTableRule {
    /**
     * Return true of the provided TransTable can by removed and false
     * otherwise.
     * @param transTable The TransTable that may be removed.
     * @return true if the transTable should be removed; false otherwise.
     */
    public boolean canRemoveTable(TransTable transTable);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Oct-03	1540/1	allan	VBM:2003101101 Fix pane styles and single column table removal

 ===========================================================================
*/
