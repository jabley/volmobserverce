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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoStatusEvent;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoStatusListener;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.texteditor.ResourceAction;


/**
 * An action that interacts with an <code>UndoRedoManager</code>
 * by invoking its <code>undo/redo</code> methods and with the corresponding
 * <code>ODOMEditorPart</code>, by setting focus to the last changed element.
 * <p>
 * Its enabled state is set by listening to the <code>UndoRedoManager</code>
 * status change events.
 * The object that controls the lifecycle of this action,
 * should call {@link #dispose()}  method when it no longer needs it.
 * <p/>
 * IMPLEMENTATION NOTES:
 * <ul>
 * <li> This class is an action upon ODOM objects, but it is a 'friend' of
 * ODOMEditorPart so it wasn't written as an ODOMAction but rather placed
 * in the same package as ODOMEditorPart.
 * <li>It looked simple to use conditionals instead of polymorphism
 * to get undo or redo behavior (e.g., using polymorphism would have required
 * template methods in a common superclass).
 * Anyway, this implementation choice is hidden to the clients,
 * who are forced to instantiate it using two factory methods.
 * </ul>
 */
class ODOMUndoRedoAction extends ResourceAction {

    /**
     * declared constant to avoid creating new strings from constants
     */
    private static final String RESOURCE_PREFIX_REDO =
            ODOMEditorContext.RESOURCE_PREFIX + "redo."; //$NON-NLS-1$
    /**
     * declares constant to avoid creating new strings from constants
     */
    private static final String RESOURCE_PREFIX_UNDO =
            ODOMEditorContext.RESOURCE_PREFIX + "undo."; //$NON-NLS-1$

    /**
     * flag used to implement undo from redo behavior
     */
    private final boolean isUndo;

    /**
     * the manager to invoke undo/redo upon
     */
    private UndoRedoManager undoRedoManager;

    /**
     * listens to changes in the manager's status and set the appropriate
     * enabled state to this action
     */
    private final UndoRedoStatusListener undoRedoStatusListener;

    /**
     * Factory method to create an instance of this action
     * configured for UNDO capability
     *
     * @param undoRedoManager the manager to invoke undo/redo upon
     * @return an instance of this action configured for UNDO capability
     */
    public static ODOMUndoRedoAction createUndoAction(
            UndoRedoManager undoRedoManager) {
        return new ODOMUndoRedoAction(true, undoRedoManager);
    }


    /**
     * Factory method to create an instance of this action
     * configured for REDO capability
     *
     * @param undoRedoManager the manager to invoke undo/redo upon
     * @return an instance of this action configured for REDO capability
     */
    public static ODOMUndoRedoAction createRedoAction(
            UndoRedoManager undoRedoManager) {
        return new ODOMUndoRedoAction(false, undoRedoManager);
    }


    /**
     * private constructor used by factory methods
     *
     * @param undo       true if the instance will have undo behavior, false if redo
     * @param manager    the manager to invoke undo/redo upon
     */
    private ODOMUndoRedoAction(boolean undo,
                               UndoRedoManager manager) {

        super(EditorMessages.getResourceBundle(),
              undo ? RESOURCE_PREFIX_UNDO : RESOURCE_PREFIX_REDO,
              IAction.AS_PUSH_BUTTON);

        this.isUndo = undo;
        this.undoRedoManager = manager;

        updateEnabledState();

        undoRedoStatusListener = new UndoRedoStatusListener() {
            public void undoStatusChanged(UndoRedoStatusEvent event) {
                setEnabled(isUndo ? event.canUndo() : event.canRedo());
            }
        };

        undoRedoManager.addUndoRedoListener(undoRedoStatusListener);
    }

    /**
     * Update the enabled state of the action.
     */
    private void updateEnabledState() {
        setEnabled(isUndo ?
                   undoRedoManager.canUndo() :
                   undoRedoManager.canRedo());
    }

    /**
     * Set the {@link UndoRedoManager} to the value passed in and remove/add
     * listeners as necessary.
     *
     * @param undoRedoManager the {@link UndoRedoManager} instance.
     */
    public void setUndoRedoManager(UndoRedoManager undoRedoManager) {
        if (undoRedoManager == null) {
            throw new IllegalArgumentException(
                    "UndoRedoManager parameter may not be null.");
        }
        if (this.undoRedoManager != undoRedoManager) {
            // Remove the current status listener from the current manager.
            if (this.undoRedoManager != null) {
                this.undoRedoManager.removeUndoRedoListener(undoRedoStatusListener);
            }
            // Update the manager.
            this.undoRedoManager = undoRedoManager;

            // Update the new manager with the status listener.
            if (this.undoRedoManager != null) {
                this.undoRedoManager.addUndoRedoListener(undoRedoStatusListener);
            }
        }
        updateEnabledState();
    }

    /**
     * invokes <code>undoRedoManager.undo()</code> or
     * <code>undoRedoManager.redo()</code>
     * and then attempts to restore the UI to a state that is informative for
     * the user about the changes performed by this action
     */
    public void run() {
        if (isUndo) {
            undoRedoManager.undo();
        } else {
            undoRedoManager.redo();
        }
    }


    /**
     * the object that controls the lifecycle of this action,
     * must call this method when it no longer needs it.
     */
    public void dispose() {
        undoRedoManager.removeUndoRedoListener(undoRedoStatusListener);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Mar-04	3346/1	byron	VBM:2004021308 Cannot undo deletion or cutting of single or multiple assets

 17-Feb-04	2988/2	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 09-Feb-04	2800/7	eduardo	VBM:2004012802 codestyle fixes

 09-Feb-04	2800/5	eduardo	VBM:2004012802 undo redo works from outline view

 05-Feb-04	2800/3	eduardo	VBM:2004012802 undo redo hooked in eclipse with demarcation. Designed just for single page editors

 ===========================================================================
*/
