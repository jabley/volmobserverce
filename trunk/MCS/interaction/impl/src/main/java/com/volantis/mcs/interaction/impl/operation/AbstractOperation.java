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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.interaction.impl.operation;

import com.volantis.mcs.interaction.impl.InternalProxy;
import com.volantis.mcs.interaction.operation.Operation;

/**
 * Base of all the operations.
 *
 * <p>This manages the state of the operation to make sure that it can only be
 * used in the appropriate state.</p>
 *
 * @mock.generate
 */
public abstract class AbstractOperation
        implements Operation {

    private static final String EXECUTE = "execute";
    private static final String UNDO = "undo";
    private static final String REDO = "redo";

    /**
     * The proxy at which this operation is targeted.
     */
    private final InternalProxy proxy;

    /**
     * The modification count of the proxy at the time this operation was
     * created.
     */
    private int modificationCount;

    /**
     * The state of the operation. This determines which methods can be
     * invoked.
     */
    private String state;

    /**
     * Initialise.
     */
    protected AbstractOperation(InternalProxy proxy) {
        this.proxy = proxy;
        this.state = EXECUTE;
        this.modificationCount = proxy.getModificationCount();
    }

    // Javadoc inherited.
    public final void execute() {
        checkState(EXECUTE);
        checkWritable();

        executeImpl();

        // Update the state to allow this to be undone.
        updateState(UNDO);
    }

    /**
     * Operation specific implementation of the execute method.
     */
    protected abstract void executeImpl();

    // Javadoc inherited.
    public final void redo() {
        checkState(REDO);
        checkWritable();

        redoImpl();

        // Update the state to allow this to be undone.
        updateState(UNDO);
    }

    /**
     * Operation specific implementation of the redo method.
     *
     * <p>By default this invokes the {@link #executeImpl} method as redoing is
     * often the same as doing it in the first place.</p>
     */
    protected void redoImpl() {
        executeImpl();
    }

    // Javadoc inherited.
    public final void undo() {
        checkState(UNDO);
        checkWritable();

        undoImpl();

        // Update the state to allow this to be redone.
        updateState(REDO);
    }

    /**
     * Operation specific implementation of the undo method.
     */
    protected abstract void undoImpl();

    /**
     * Check that this is in the correct state.
     *
     * <p>This must be in the expected state and the modification count of the
     * proxy must be the same as the stored one.</p>
     *
     * @param expectedState
     */
    private void checkState(final String expectedState) {
        if (state != expectedState) {
            throw new IllegalStateException("Cannot " + expectedState + " " +
                    this +
                    " expecting to " + state);
        }

        // Make sure that the proxy is in the correct state.
        int proxyModificationCount = proxy.getModificationCount();
        if (proxyModificationCount != modificationCount) {
            throw new IllegalStateException("Cannot perform operation as proxy is in the wrong state," +
                    " expected modification count of " + modificationCount +
                    " was " + proxyModificationCount);
        }
    }

    /**
     * Check that the proxy is writable.
     */
    private void checkWritable() {
        if (proxy.isReadOnly()) {
            throw new IllegalStateException("Cannot perform operation as " +
                    "proxy is read-only");
        }
    }

    /**
     * Update the state.
     *
     * <p>The expected state gets updated as does the stored modification
     * count.</p>
     *
     * @param newState The new state.
     */
    private void updateState(String newState) {
        state = newState;
        int proxyModificationCount = proxy.getModificationCount();

        /**
         * Removing this check so that setting properties to the same value they
         * used to hold no longer causes failures.
        if (proxyModificationCount <= modificationCount) {
            // Don't worry about wrapping back to 0.
            throw new IllegalStateException("Operation did not increase modification count, previous " +
                    modificationCount + ", new " + proxyModificationCount);
        }
         **/
        this.modificationCount = proxyModificationCount;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 09-Nov-05	10199/1	pduffin	VBM:2005110413 Committing moving of paths from interaction to model subsystem.

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
