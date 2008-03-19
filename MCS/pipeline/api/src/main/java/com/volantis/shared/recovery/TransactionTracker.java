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
 * This utility class is used to keep track of the depth of transaction
 * nesting.
 */
public class TransactionTracker {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * The depth of transactions.
     */
    private int transactionDepth = 0;

    /**
     * Start a new transaction.
     */
    public void startTransaction() {
        transactionDepth++;
    }

    /**
     * Commit a transaction.
     */
    public void commitTransaction() {
        if (transactionDepth == 0) {
            throw new IllegalStateException(
                    "Cannot commit outside of a transaction.");
        }
        transactionDepth--;
    }

    /**
     * Rollback a transaction
     */
    public void rollbackTransaction() {
        if (transactionDepth == 0) {
            throw new IllegalStateException(
                    "Cannot commit outside of a transaction.");
        }
        transactionDepth--;
    }

    /**
     * This tells us if the object is currently processing a transaction.
     * @return true if the object is currently processing a transaction
     */
    public boolean inTransaction() {
        return (transactionDepth > 0);
    }
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
