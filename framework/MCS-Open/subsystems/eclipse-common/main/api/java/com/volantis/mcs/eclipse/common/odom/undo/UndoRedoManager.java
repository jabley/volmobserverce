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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common.odom.undo;

import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;


/**
 * An object responsible for tracking changes to a ODOMElement so that they can
 * be undone or redone (after an undo).
 * <p>
 * ODOMChangeEvents that will be undone/redone collectively are collected into
 * ordered sets (Unit of Work).
 * </p>
 * <p>
 * The redo capability becomes active after undoing some changes and becomes
 * inactive again when other unrelated changes are applied to the tracked document.<br/>
 * Changes to the status of the undo/redo capability are propagated to any
 * <code>UndoRedoStatusListener</code>s that are registered with this manager.
 * </p>
 *
 * <p>
 * This class is designed for <em>non concurrent</em> usage.
 * </p>
 */
public class UndoRedoManager {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(UndoRedoManager.class);

    /**
     * Used to store the elements whose changes are to be tracked
     */
    private final Set trackedElements;

    /**
     * the internal listener to document changes,
     * so that this manager does not have to publically
     * declare to implement the ODOMChangeListener interface which
     * would not be appropriate to call from its clients.
     */
    private final ODOMChangeListener changeListener;

    /**
     * stacks of undo units of work.
     *
     * the concrete list implementation type is specified to be able to
     * use its specific API
     *
     * @todo to save memory, a limited depth stack may be used here
     */
    private final LinkedList undoableUOWs;

    /**
     * stacks of redo units of work.
     *
     * the concrete list implementation type is specified to be able to
     * use its specific API
     *
     * A size-limited list is unlikely to be useful for redo, as this list
     * is emptied after any user edit following an undo
     */
    private final LinkedList redoableUOWs;

    /**
     * The current UOW where change events are collected.
     * It's the fundamental piece of state : if null,
     * any new changeEvents will end up in a new UOW.
     */
    private UndoRedoUnitOfWork currentUOW = null;

    /**
     * state flag used to ignore change events fired happening during undo/redo
     */
    private boolean ignoreChangeEvents = false;

    /**
     * List of <code>UndoRedoStatusListener</code>s interested in changes
     * to the canUndo/canRedo status
     */
    private ArrayList undoRedoListeners = null;

    /**
     * The &quot;context&quot; in which this manager executes
     */
    private final UndoRedoMementoOriginator mementoOriginator;

    /**
     * Used to indicate whether this undo redo manager is enabled.
     */
    private boolean isEnabled = true;

    /**
     * Constructs an UndoRedoManager tracking a specific ODOMElement.
     * <p>
     * Typically, the tracked ODOMElement will be the root element of a
     * Document, so that this manager will receive events for any change to the
     * Document.
     * </p>
     * The object that controls the lifecycle of an UndoRedoManager should be
     * responsible for calling {@link #dispose()} when it does not need it anymore.
     *
     * @param trackedElement the element whose changes are to be tracked by
     *                       this manager; it must not be null.
     * @param mementoOriginator The &quot;context&quot; in which this manager
     * executes; must not be null.
     * @throws IllegalArgumentException if trackedElement is null
     */
    public UndoRedoManager(final ODOMElement trackedElement,
                           final UndoRedoMementoOriginator mementoOriginator) {
        this(mementoOriginator);
        startTrackingElement(trackedElement);
    }

    /**
     * Constructs an UndoRedoManager without an element to track
     * <p>
     * This manager will not be tracking changes to any elements. The
     * {@link #startTrackingElement} method should be used to provide the
     * element(s) to track.
     * </p>
     * The object that controls the lifecycle of an UndoRedoManager should be
     * responsible for calling {@link #dispose()} when it does not need it anymore.
     *
     * @param mementoOriginator The &quot;context&quot; in which this manager
     * executes; must not be null.
     * @throws IllegalArgumentException if trackedElement is null
     */
    public UndoRedoManager(final UndoRedoMementoOriginator mementoOriginator) {
        if (mementoOriginator == null) {
            throw new IllegalArgumentException(
                    "'mementoOriginator' argument must not be null"); //$NON-NLS-1$
        }
        this.mementoOriginator = mementoOriginator;

        trackedElements = new HashSet();
        undoableUOWs = new LinkedList();
        redoableUOWs = new LinkedList();

        changeListener = new ODOMChangeListener() {
            public void changed(ODOMObservable node, ODOMChangeEvent event) {
                UndoRedoManager.this.changed(node, event);
            }
        };
    }


    /**
     * private implementation of {@link ODOMChangeListener#changed }
     *
     * @param event
     */
    private void changed(ODOMObservable node, ODOMChangeEvent event) {

        //assertion
        if (!trackedElements.contains(node)) {
            throw new IllegalStateException(
                    "received a change event from unexpected node"); //$NON-NLS-1$
        }

        if (ignoreChangeEvents) {
            return;
        }

        boolean fireStatusChange = false;

        if (currentUOW == null) {
            currentUOW =
            new UndoRedoUnitOfWork(mementoOriginator.takeSnapshot());
            undoableUOWs.addLast(currentUOW);
            redoableUOWs.clear();

            fireStatusChange = true;
        }

        currentUOW.addChangeEvent(event);

        if (fireStatusChange) {
            fireUndoStatusChanged();
        }
    }

    /**
     * Enables or disables this UndoRedoManager. When this UndoRedoManger
     * is disabled any changes to the tracked elements are ignored.
     * @param enable true to enable, false to disable.
     */
    public void enable(boolean enable) {
        ignoreChangeEvents = !enable;
    }

    /**
     * Allows changes to the element to be tracked by this UndoRedoManager.
     * If the element is already being tracked then this method does nothing.
     * @param element the {@link ODOMElement} whose changes are to be tracked.
     * Cannot be null.
     * @throws IllegalArgumentException if the element argument is null.
     */
    public void startTrackingElement(ODOMElement element) {
        if (element == null) {
            throw new IllegalArgumentException("element cannot be null");
        }
        if (!trackedElements.contains(element)) {
            element.addChangeListener(changeListener);
            trackedElements.add(element);
        }
    }

    /**
     * Allows changes to the element to stop being tracked by this UndoRedoManager.
     * If the element is not already being tracked then this method does nothing.
     * @param element the {@link ODOMElement} whose changes are to be tracked.
     * Cannot be null.
     * @throws IllegalArgumentException if the element argument is null.
     */
    public void stopTrackingElement(ODOMElement element) {
        if (element == null) {
            throw new IllegalArgumentException("element cannot be null");
        }
        if (!trackedElements.contains(element)) {
            element.removeChangeListener(changeListener);
            trackedElements.remove(element);
        }
    }

    /**
     * Instructs this manager that all events received from now on will
     * have to be collected in a new UnitOfWork.
     * <p>
     * It is a very cheap operation,
     * idempotent if invoked multiple times in succession.
     * Interaction with the contextual {@link UndoRedoMementoOriginator}
     * happens only whena new UOW is constructed.
     * </p>
     */
    public void demarcateUOW() {
        currentUOW = null;
    }


    /**
     * @return true if there are any changes that can be undone, false
     *         otherwise
     */
    public boolean canUndo() {
        return undoableUOWs.size() > 0;
    }


    /**
     * @return true if there are any changes that can be redone (must have been
     *         undone before), false otherwise
     */
    public boolean canRedo() {
        return redoableUOWs.size() > 0;
    }


    /**
     * Modifies the tracked ODOMElement by undoing the last set of changes and
     * invokes the contextual {@link UndoRedoMementoOriginator#restoreSnapshot}.
     *
     * <p>
     * After invoking undo:
     * <ol>
     *
     * <li> the {@link #redo()} method becomes available;</li>
     *
     * <li> all events received from now on will be collected in a
     * new UnitOfWork.</li>
     *
     * </ol>
     * </p>
     *
     * <p>
     * This method is a no-op if {@link #canUndo()} returns false
     * </p>
     */
    public void undo() {

        if (canUndo()) {
            demarcateUOW();

            //undo in a guarded block against changes caused by this undo itself
            try {
                UndoRedoUnitOfWork uow = (UndoRedoUnitOfWork) undoableUOWs.removeLast();

                ignoreChangeEvents = true;
                UndoRedoInfo undoRedoInfo = uow.undo();
                redoableUOWs.addLast(uow);
                mementoOriginator.restoreSnapshot(undoRedoInfo);

            } finally {
                ignoreChangeEvents = false;
            }

            //notify any listeners
            fireUndoStatusChanged();
        }

    }


    /**
     * Modifies the tracked ODOMElement by re-applying the changes that were
     * undone last (that is, this method 'undoes the undo') and
     * invokes the contextual {@link UndoRedoMementoOriginator#restoreSnapshot}.
     * <p>
     * This method is a no-op if {@link #canRedo()} returns false
     * </p>.
     */
    public void redo() {

        if (canRedo()) {
            //redo in a guarded block against changes caused by this redo itself
            try {
                UndoRedoUnitOfWork uow = (UndoRedoUnitOfWork) redoableUOWs.removeLast();
                ignoreChangeEvents = true;
                UndoRedoInfo undoRedoInfo = uow.redo();
                undoableUOWs.addLast(uow);
                mementoOriginator.restoreSnapshot(undoRedoInfo);
            } finally {
                ignoreChangeEvents = false;
            }

            //notify any listeners
            fireUndoStatusChanged();
        }
    }


    /**
     * Registers a <code>UndoRedoStatusListener</code> to be informed of changes in
     * the canUndo/canRedo status.
     *
     * @param listener must not be null
     * @return true if the listsner has been registered within this method call,
     *         false if it hasn't (because it was already registered)
     */
    public boolean addUndoRedoListener(UndoRedoStatusListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException(
                    "Argument 'listener' must not be null");
        }

        if (undoRedoListeners == null) {  //lazy initialization
            undoRedoListeners = new ArrayList(2);
        }

        if (!undoRedoListeners.contains(listener)) {
            return undoRedoListeners.add(listener);
        } else {
            return false;
        }
    }


    /**
     * Deregisters a <code>UndoRedoStatusListener</code>.
     *
     * @param listener must not be null
     * @return true if the listener was registered,
     *         false if it wasn't (i.e., no removal actually occurred)
     */
    public boolean removeUndoRedoListener(UndoRedoStatusListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException(
                    "Argument 'listener' must not be null");
        }

        if (undoRedoListeners != null) {
            return undoRedoListeners.remove(listener);
        } else {
            return false;
        }
    }


    /**
     * The object responsible for the lifecycle of this UndoRedoManager
     * must call this method when no longer needs it.
     *
     * <p>
     * The manager will not be usable anymore after this call,
     * which severes the event notification relationships that it has
     * with other objects.
     * </p>
     */
    public void dispose() {
        // unregister the change listener with the tracked elements
        for (Iterator i=trackedElements.iterator(); i.hasNext();) {
            ((ODOMElement) i.next()).removeChangeListener(changeListener);
        }

        //see comment in ODOMEditorContext.dispose()
        if (undoRedoListeners != null) {
            undoRedoListeners.clear();
        }
    }


    /**
     * notification to listeners of undo/redo status change
     *
     * <strong>NOTE: should we be concerned about add/remove happening concurrently to fire ?</strong>
     *
     * @todo do we need synchronization w.r.t add/remove listeners ?
     */
    private void fireUndoStatusChanged() {
        if (undoRedoListeners != null) {
            UndoRedoStatusEvent event = new UndoRedoStatusEvent(this);

            try {
                for (int i = 0; i < undoRedoListeners.size(); i++) {
                    ((UndoRedoStatusListener) undoRedoListeners.get(i)).undoStatusChanged(
                            event);
                }
            } catch (RuntimeException e) {
                logger.error("undo-redo-status-listener-error", e);
            }
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 05-May-04	4115/1	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 22-Apr-04	3878/1	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 16-Apr-04	3743/3	doug	VBM:2004032101 Added a DeviceEditorContext class

 16-Apr-04	3743/1	doug	VBM:2004032101 Added a DeviceEditorContext class

 17-Feb-04	2988/3	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 12-Feb-04	2924/2	eduardo	VBM:2004021003 editor actions demarcate UndoRedo UOWs

 09-Feb-04	2800/10	eduardo	VBM:2004012802 codestyle fixes

 09-Feb-04	2800/8	eduardo	VBM:2004012802 undo redo works from outline view

 05-Feb-04	2800/5	eduardo	VBM:2004012802 undo redo hooked in eclipse with demarcation. Designed just for single page editors

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 29-Jan-04	2689/4	eduardo	VBM:2003112407 formatting changes

 29-Jan-04	2689/2	eduardo	VBM:2003112407 undo/redo manager for ODOM
 ===========================================================================
*/
