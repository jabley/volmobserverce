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
 * This class contains a set of entries.
 */
public class EntryBitSet
    implements EntrySet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private static final int BIT_2_INT_SHIFT = 5;
    private static final int BIT_2_INT_MASK = 0x1f;

    /**
     * The number of entries in the set.
     * <p>
     * Once set this never changes.
     */
    private final int count;

    /**
     * Fixed size array of integers that is used as a bit set where bit N is
     * used to determine whether entry N is in the set.
     */
    private final int[] bits;

    /**
     * Create an entry set that can contain the specified number of entries
     * whose indexes are in the range from 0 to <code>count - 1</code>.
     */
    public EntryBitSet(int count) {

        this.count = count;

        // Calculate how many integers are needed to hold the specified number
        // of entries.
        int icount = (count + 31) >> 5;
        bits = new int[icount];
    }

    /**
     * Check to see whether the set contains a particular entry.
     * @param entryIndex The index of the entry.
     * @return True if the set contains the entry and false otherwise.
     */
    public boolean containsEntry(int entryIndex) {

        // Make sure that the index is within range.
        if (entryIndex < 0 || entryIndex >= count) {
            throw new ArrayIndexOutOfBoundsException(entryIndex);
        }

        // Calculate the index of the cell in the array that contains the
        // entry's bit.
        int cellIndex = entryIndex >> BIT_2_INT_SHIFT;

        // Get the contents of the cell.
        int cell = bits[cellIndex];

        // Perform a quick check to see whether any bits are set. This should
        // be faster than calculating the actual bit.
        if (cell != 0) {
            // Calculate the bit within the integer.
            int bit = 1 << (entryIndex & BIT_2_INT_MASK);

            // Return true if the bit is set and false otherwise.
            return ((cell & bit) != 0);
        }

        // The bit is not set so return.
        return false;
    }

    /**
     * Add the entry to the set.
     * @param entryIndex The index of the entry.
     * @return True if this set changed as a result of this call. i.e. if the
     * entry was not already in the set.
     * @throws java.lang.IllegalStateException If the entry could not be added to this
     * set.
     */
    public boolean addEntry(int entryIndex) {

        // Make sure that the index is within range.
        if (entryIndex < 0 || entryIndex >= count) {
            throw new ArrayIndexOutOfBoundsException(entryIndex);
        }

        // Calculate the index of the cell in the array that contains the
        // entry's bit.
        int cellIndex = entryIndex >> BIT_2_INT_SHIFT;

        // Get the contents of the cell.
        int cell = bits[cellIndex];

        // Calculate the bit within the integer.
        int bit = 1 << (entryIndex & BIT_2_INT_MASK);

        // Remember whether the bit was clear.
        boolean clear = ((cell & bit) == 0);

        // Set the bit.
        cell |= bit;

        // Store the value back in the array.
        bits[cellIndex] = cell;

        // Return true if the bit was clear before we set it.
        return clear;
    }

    /**
     * Add the specified entries to this set.
     * <p>
     * The specified EntrySet must be exactly the same size as this one.
     * <p>
     * The effect of this method on this object is equivalent to unioning
     * this set with the specified set.
     * @param entries The entries to add.
     */
    public void addEntries(EntryBitSet entries) {
        if (entries.count != count) {
            throw new IllegalArgumentException
                ("EntrySet must have exactly " + count + " entries");
        }

        /**
         * Iterate over the array of bits and add all the entries from the
         * set.
         */
        for (int i = 0; i < bits.length; i += 1) {
            int cell = entries.bits[i];
            bits[i] |= cell;
        }
    }

    /**
     * Remove the specified entries from this set.
     * <p>
     * The specified EntrySet must be exactly the same size as this one.
     * <p>
     * The effect of this method on this object is equivalent to intersecting
     * this set with the inverse of the specified set.
     * @param entries The entries to remove.
     */
    public void removeEntries(EntryBitSet entries) {
        if (entries.count != count) {
            throw new IllegalArgumentException
                ("EntrySet must have exactly " + count + " entries");
        }

        /**
         * Iterate over the array of bits and remove all the entries from the
         * set.
         */
        for (int i = 0; i < bits.length; i += 1) {
            int cell = ~entries.bits[i];
            bits[i] &= cell;
        }
    }
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
