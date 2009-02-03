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

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.pipeline.localization.LocalizationFactory;

import java.util.Stack;

/**
 * This abstract class provides common functionality to those classes that
 * implement RecoverableTransaction.
 */
public abstract class AbstractRecoverableTransaction
        implements RecoverableTransaction, Cloneable {

    /**
     * The volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AbstractRecoverableTransaction.class);

    /**
     * This stack is used to store clones of AbstractRecoverableTransaction
     * as a means of maintaining nestable snapshots of the Object state.
     */
    protected Stack clones;

    /**
     * This flag allows us to determine whether the we are currently processing
     * a recoverable transaction.
     */
    protected boolean inTransaction = false;

    /**
     * Create a new instance of AbstractRecoverableTransaction
     */
    public AbstractRecoverableTransaction() {
        clones = new Stack();
    }

    // Javadoc inherited from Cloneable interface.
    protected Object clone() throws CloneNotSupportedException {
        AbstractRecoverableTransaction clone = null;
        try {
            clone = (AbstractRecoverableTransaction)getClass().newInstance();
        } catch (Exception e) {
            throw new CloneNotSupportedException(e.getMessage());
        }
        clone.inTransaction = inTransaction;
        clone.clones = (Stack)clones.clone();
        return clone;
    }

    // Javadoc inherited from RecoverableTransaction interface
    public final void startTransaction() {
        Object clone = null;
        try {
            clone = clone();
        } catch (CloneNotSupportedException e) {
            logger.error("recoverable-transaction-clone-failure",
                         new Object[] {
                             getClass().getName(),
                             AbstractRecoverableTransaction.class.getName()});
            final String errorMsg = "Could not clone " + getClass().getName() +
                    " because: " + e.getMessage() + ".  Specialisations of " +
                    AbstractRecoverableTransaction.class.getName() +
                    " must be cloneable.  Cannot store state required " +
                    "to facilitate a recoverable transaction.";
            throw new IllegalStateException(errorMsg);
        }
        clones.push(clone);
        inTransaction = true;

        startTransactionImpl();
    }

    /**
     * Implementation method for {@link #startTransaction}.  Typically this
     * will be implemented to perform and additional processing required to
     * initialize the state to begin a recoverable transaction.
     */
    protected abstract void startTransactionImpl();

    // Javadoc inherited from RecoverableTransaction interface
    public final void commitTransaction() {
        if (!inTransaction) {
            logger.error("transaction-required-to-perform-commit");
            throw new IllegalStateException(
                        "Must be in a transaction to commit it.");
        }

        // Get the clone which holds the state at the start of the transaction.
        AbstractRecoverableTransaction clone =
                (AbstractRecoverableTransaction)clones.pop();

        commitTransactionImpl(clone);
    }

    /**
     * Implementation method for {@link #commitTransaction}.  Typically this
     * will be implemented to perform any additional processing required to
     * complete the transaction and update the Object state.
     *
     * @param poppedState This AbstractRecoverableTransaction contains the
     * state stored at the last call to {@link #startTransaction}
     */
    protected abstract void commitTransactionImpl(
            AbstractRecoverableTransaction poppedState);

    // Javadoc inherited from RecoverableTransaction interface
    public final void rollbackTransaction() {
        if (!inTransaction) {
            logger.error("transaction-required-to-perform-rollback");
            throw new IllegalStateException(
                        "Must be in a transaction to rollback.");
        }

        // Get the clone which holds the state at the start of the transaction.
        AbstractRecoverableTransaction clone =
                (AbstractRecoverableTransaction)clones.pop();

        rollbackTransactionImpl(clone);

        inTransaction = clone.inTransaction;
        clones = clone.clones;
    }

    /**
     * Implementation method for {@link #rollbackTransaction}.  Typically this
     * will be implemented to perform any additional processing required to
     * revert Object back to the state recorded at the last call to the
     * method {@link #startTransaction}
     *
     * @param poppedState This AbstractRecoverableTransaction contains the
     * state stored at the last call to {@link #startTransaction}
     */
    protected abstract void rollbackTransactionImpl(
            AbstractRecoverableTransaction poppedState);

    // Javadoc inherited from RecoverableTransaction interface
    public boolean inTransaction() {
        return inTransaction;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
