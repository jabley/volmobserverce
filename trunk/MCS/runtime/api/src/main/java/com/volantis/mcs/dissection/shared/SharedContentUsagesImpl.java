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

import com.volantis.mcs.dissection.SharedContentUsages;

/**
 * This class contains entries for each of the different pieces of shared
 * content. It is used to track usages of shared content.
 * <p>
 * As far as this class is concerned all the shared content within a document
 * is stored in a single table and is accessed using its index within that
 * table. The first entry within that table has an index of 0, the second has an
 * index of 1 and so on. This class identifies shared content using its index
 * within that table.
 * <p>
 * These objects are organized into a multi level hierarchy with each object
 * referring back to its parent it it has one. Checking for a previous usage of
 * shared content searches the object first and then its parent. New usages
 * of shared content are added to this object.
 * <p>
 * In this way usages that are common to significant parts of the document can
 * be shared.
 * <h3>Tracking Changes</h3>
 * <p>
 * This class needs to be able to track any changes that are made to it as they
 * may need to be undone later for example when the dissector has to back track.
 * However, not all changes to this table need tracking so it must be optionally
 * controllable.
 * <p>
 * The {@link #pushChangeSet} details how to track changes.
 */
public class SharedContentUsagesImpl implements SharedContentUsages {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The parent shared content table. If this is null then this table must
     * be the fixed content table, otherwise the parent is the fixed content
     * table and this is a shard table.
     * <p>
     * Once set this never changes.
     */
    private final SharedContentUsagesImpl parent;

    /**
     * The set of entries.
     */
    private EntryBitSet entries;

    /**
     * The number of entries in the table.
     * <p>
     * Once set this never changes.
     */
    private final int count;

    /**
     * EntrySet that is used to track changes to this table. If this is null
     * then changes are not tracked.
     */
    private ChangeSetImpl changes;

    /**
     * Create a shared content table that has the specified number of entries.
     */
    public SharedContentUsagesImpl(int count) {
        this(null, count);
    }

    /**
     * Create a shared content table that has the specified number of entries.
     */
    private SharedContentUsagesImpl(SharedContentUsagesImpl parent, int count) {
        this.parent = parent;
        this.count = count;

        entries = new EntryBitSet(count);
    }

    /**
     * Creates a new instance of this object.
     */
    public SharedContentUsages createDependent() {
        return new SharedContentUsagesImpl(this, count);
    }

    public int getCount() {
        return count;
    }

    /**
     * Check to see whether the specified shared content has already been used.
     * @param sharedContentIndex The index of the shared content.
     * @return True if the shared content has already been used and false
     * otherwise.
     */
    public boolean isSharedContentUsed(int sharedContentIndex) {

        return isSharedContentUsed(sharedContentIndex, true);
    }

    public boolean isSharedContentUsed(int sharedContentIndex,
                                       boolean inherit) {

        // Make sure that the index is within range.
        if (sharedContentIndex < 0 || sharedContentIndex >= count) {
            throw new ArrayIndexOutOfBoundsException(sharedContentIndex);
        }

        // Check this object first, if it is not here then check the parent.
        if (entries != null
            && entries.containsEntry(sharedContentIndex)) {
            return true;
        }

        // Check the parent.
        if (inherit
            && parent != null
            && parent.isSharedContentUsed(sharedContentIndex)) {
            return true;
        }

        return false;
    }

    /**
     * Record that the specified shared content has been used.
     * @param entryIndex The index of the entry.
     * @return True if this table changed as a result of this call. i.e. if the
     * entry was not already in the table.
     * @throws java.lang.IllegalStateException If the entry could not be added to this
     * table.
     */
    public boolean addSharedContentUsage(int entryIndex) {

        // If the EntrySet does not exist then create it.
        if (entries == null) {
            entries = new EntryBitSet(count);
        } else {
            // Check to see whether the entry is already in the table or the
            // parent one. If it is we can exit immediately.
            if (isSharedContentUsed(entryIndex)) {
                return false;
            }
        }

        // If we have been asked to record changes then update it.
        if (changes != null) {
            changes.addEntry(entryIndex);
        }

        // Add the entry to the EntrySet.
        return entries.addEntry(entryIndex);
    }

    /**
     * Add the entries that are in the specified table into this one.
     * <p>
     * After this method has finished this table is equivalent to the union
     * of the original table and the specified table.
     * <p>
     * <strong>Note: this method does not support change tracking and will throw
     * an exception if the
     */
    public void addSharedContentUsages(SharedContentUsages usages) {
        if (changes != null) {
            throw new IllegalStateException("Method does not support changes");
        }
        if (usages != null) {
            entries.addEntries(((SharedContentUsagesImpl) usages).entries);
        }
    }

    /**
     * Create an ChangeSet that can be used to track changes.
     * <p>
     * The returned ChangeSet is not bound to this SharedContentUsagesImpl so can
     * be used with other SharedContentTables.
     */
    public SharedContentUsages.ChangeSet createChangeSet() {
        return new ChangeSetImpl(count);
    }

    /**
     * This method causes the table to begin tracking changes in the specified
     * ChangeSet.
     * <p>
     * The changes are recorded in the specified ChangeSet. It is possible to
     * nest calls to this method. In order for this to work the caller must
     * save away the returned ChangeSet and pass it into the popChangeSet
     * method.
     * <pre>
     *   SharedContentUsagesImpl sharedTable = ....;
     *   ChangeSet changes = sharedTable.createChangeSet ();
     *   ChangeSet savedChangeSet = pushChangeSet (changes);
     *     :
     *     :
     *   popChangeSet (savedChangeSet);
     * </pre>
     * @param changes The ChangeSet into which changes to this table should
     * be recorded.
     * @return The previous object into which changes are being tracked.
     */
    public SharedContentUsages.ChangeSet pushChangeSet(SharedContentUsages.ChangeSet changes) {
        SharedContentUsages.ChangeSet oldChanges = changes;
        this.changes = (ChangeSetImpl) changes;
        return oldChanges;
    }

    /**
     * This method stops tracking changes in the current ChangeSet and
     * restores the previous ChangeSet if any.
     * @param oldChangeSet The previous change set.
     */
    public void popChangeSet(SharedContentUsages.ChangeSet oldChangeSet) {
        popChangeSet(oldChangeSet, false);
    }

    /**
     * This method stops tracking changes in the current ChangeSet and
     * restores the previous ChangeSet if any.
     * @param oldChangeSet The previous change set.
     * @param undo If true then the changes since the current change set was
     * pushed are removed.
     */
    public void popChangeSet(SharedContentUsages.ChangeSet oldChangeSet,
                             boolean undo) {
        if (changes == null) {
            throw new IllegalStateException("No change set");
        }

        if (undo) {
            entries.removeEntries((ChangeSetImpl) changes);
        }

        changes = (ChangeSetImpl) oldChangeSet;
    }

    /**
     * Implementation of a ChangeSet.
     */
    private class ChangeSetImpl
        extends EntryBitSet
        implements SharedContentUsages.ChangeSet {

        public ChangeSetImpl(int count) {
            super(count);
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

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
