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

package com.volantis.shared.recovery;

/**
 * This interface provides a common set of methods for those Classes which must
 * be able to:
 * <ul>
 * <li> Capture their state at a known instant.
 * <li> Restore the captured state under error conditions.
 * </ul>
 *
 * Typically implementors will be expected to handle multiply nested
 * recoverable transactions, that is to say they should be able to capture
 * multiple states.  In such cases, under an error condition, the state
 * captured at the start of the currently uncompleted transaction should be
 * restored.
 *
 * Where a nested transaction has been completed implementors should consider
 * that all actions within that transaction are part of the parent transaction.
 */
public interface RecoverableTransaction {

    /**
     * This method will typically be implemented to...
     * <ul>
     * <li> Record the current state of the Object by storing a clone.
     * <li> Initialise any data structures required to record changes to the
     *      state.
     * <li> Mark the Object as being in transaction mode.
     * </ul>
     */
    void startTransaction();

    /**
     * This method will typically be implemented such that...
     * <ul>
     * <li> For an unnested transaction it will release any resources the
     *      object was asked to discard during the transaction.
     * <li> For a nested transaction it will add the actions for the
     *      transaction to the parent transaction.
     * </ul>
     */
    void commitTransaction();

    /**
     * This method will typically be implemented to...
     * <ul>
     * <li> Release an resources added by actions during the transaction.
     * <li> Restore the Object to the state recorded at the start of the
     *      transaction
     * </ul>
     */
    void rollbackTransaction();

//    /**
//     * This tells us if the object is currently processing a
//     * recoverable transaction.
//     * @return true if the object is currently processing a recoverable
//     * transaction
//     */
//    boolean inTransaction();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
