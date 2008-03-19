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

package com.volantis.mcs.dissection;

/**
 * This interface is used to track usages of shared content.
 * <p>
 * As far as this interface is concerned all the shared content within a
 * document is stored in a single table and is accessed using its index within
 * that table. The first entry within that table has an index of 0, the second
 * has an index of 1 and so on. This class identifies shared content using its
 * index within that table.
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
public interface SharedContentUsages {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * This method creates a new instance of this interface that depends on,
     * i.e. whose parent is, this object.
     * @return A newly created <code>SharedContentUsages</code> that falls back
     * to this object.
     */
    public SharedContentUsages createDependent();

    /**
     * Get the count of the number of entries that this object can hold.
     * @return The number of entries that this object can hold.
     */
    public int getCount();

    /**
     * Check to see whether the specified shared content has already been used.
     * @param sharedContentIndex The index of the shared content.
     * @return True if the shared content has already been used and false
     * otherwise.
     */
    public boolean isSharedContentUsed(int sharedContentIndex);

    /**
     *
     * @param sharedContentIndex
     * @param inherit If true then inherits values from parent, otherwise it
     * does not.
     * @return boolean flag
     */
    public boolean isSharedContentUsed(int sharedContentIndex,
                                       boolean inherit);

    /**
     * Record that the specified shared content has been used within the context
     * of this object.
     * @param sharedContentIndex The index of the entry.
     * @return True if this object changed as a result of this call. i.e. if the
     * shared content had not already been used in the context of this object.
     * @throws java.lang.IllegalStateException If the shared content usage could
     * not be recorded in this table.
     */
    public boolean addSharedContentUsage(int sharedContentIndex);

    /**
     * Add all the shared content usages contained in the specified object
     * into this one.
     * <p>
     * After this method has finished this object contains all usages that were
     * in both this object and the other one. The other object is unchanged.
     * <p>
     * <strong>Note: this method does not support change tracking and will throw
     * an exception if change tracking is enabled.
     * @param usages The other usages to add into this object. If this is null
     * then this method has no effect on this object.
     */
    public void addSharedContentUsages(SharedContentUsages usages);

    /**
     * Create an ChangeSet that can be used to track changes.
     * <p>
     * The returned ChangeSet is not bound to this SharedContentUsages so can
     * be used with other SharedContentTables.
     */
    public ChangeSet createChangeSet();

    /**
     * This method causes the table to begin tracking changes in the specified
     * ChangeSet.
     * <p>
     * The changes are recorded in the specified ChangeSet. It is possible to
     * nest calls to this method. In order for this to work the caller must
     * save away the returned ChangeSet and pass it into the popChangeSet
     * method.
     * <pre>
     *   SharedContentUsages sharedTable = ....;
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
    public ChangeSet pushChangeSet(ChangeSet changes);

    /**
     * This method stops tracking changes in the current ChangeSet and
     * restores the previous ChangeSet if any.
     * @param oldChangeSet The previous change set.
     */
    public void popChangeSet(ChangeSet oldChangeSet);

    /**
     * This method stops tracking changes in the current ChangeSet and
     * restores the previous ChangeSet if any.
     * @param oldChangeSet The previous change set.
     * @param undo If true then the changes since the current change set was
     * pushed are removed.
     */
    public void popChangeSet(ChangeSet oldChangeSet,
                             boolean undo);

    /**
     * This interface represent a set of changes that have been made to a
     * SharedContentUsages.
     */
    public interface ChangeSet {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
