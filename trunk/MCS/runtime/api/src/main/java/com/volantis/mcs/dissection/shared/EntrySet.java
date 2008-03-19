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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.shared;

/**
 * This interface defines the methods that need to be supported by objects that
 * track a set of entries.
 * <p>
 * An entry is identified by a number from 0 to the maximum number of entries
 * - 1.
 */
public interface EntrySet {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Check to see whether the set contains a particular entry.
     * @param entryIndex The index of the entry.
     * @return True if the set contains the entry and false otherwise.
     */
    public boolean containsEntry(int entryIndex);

    /**
     * Add the entry to the set.
     * @param entryIndex The index of the entry.
     * @return True if this set changed as a result of this call. i.e. if the
     * entry was not already in the set.
     * @throws java.lang.IllegalStateException If the entry could not be added to this
     * set.
     */
    public boolean addEntry(int entryIndex);

    /**
     * Remove the specified entries from this set.
     * <p>
     * The effect of this method on this object is equivalent to intersecting
     * this set with the inverse of the specified set.
     * @param entries The entries to remove.
     */
    //public void removeEntries (EntrySet entries);
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
