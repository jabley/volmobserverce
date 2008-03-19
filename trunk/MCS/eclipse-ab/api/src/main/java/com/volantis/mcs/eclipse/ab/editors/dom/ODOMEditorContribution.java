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

import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * The ODOM editor contribution delegate class. This class exists to re-use
 * shared code in {@link MultiPageODOMEditorContributor} and {@link ODOMEditorContributor}.
 */
public class ODOMEditorContribution {

    /**
     * Store the current editor so we know when the active editor has actually
     * changed.
     */
    private ODOMEditorPart currentEditor;

    /**
     * The method installs the global action handlers for the given editor.
     */
    protected void doSetActiveEditor(IEditorPart part) {

        // Check to see if the current editor has actually changed.
        if (currentEditor == part) {
            return;
        }

        ODOMEditorPart editor = null;
        if (part instanceof ODOMEditorPart) {
            editor = (ODOMEditorPart) part;
        }

        currentEditor = editor;

        // The editor has changed so we need to swap the UndoRedoManager instance
        // with the current global UNDO/REDO actions.
        if (currentEditor != null) {
            ODOMEditorContext odomEditorContext = currentEditor.getODOMEditorContext();
            UndoRedoManager newManager = odomEditorContext.getUndoRedoManager();
            IActionBars actionBars = odomEditorContext.getActionBars();

            // Ensure that the current action for UNDO/REDO has been created
            // and assigned to the global action handler. Thus ensuring
            // only one UNDO and one REDO action is created.
            updateManagerForAction(actionBars, newManager,
                    IWorkbenchActionConstants.UNDO);
            updateManagerForAction(actionBars, newManager,
                    IWorkbenchActionConstants.REDO);
        }
    }

    /**
     * Update the <code>UndoRedoManager</code> for the action identified by the
     * key.
     *
     * @param actionBars used to obtain/set the action
     * @param manager    the new manager to be associated with the the action.
     * @param key        the action key (either UNDO or REDO).
     */
    private void updateManagerForAction(IActionBars actionBars,
                                        UndoRedoManager manager,
                                        String key) {
        assignUndoRedoAction(actionBars, manager, key);

        ODOMUndoRedoAction action = ((ODOMUndoRedoAction)
                actionBars.getGlobalActionHandler(key));
        action.setUndoRedoManager(manager);
    }

    /**
     * Assign the action to the global action handler for the specified action
     * key.
     *
     * @param actionBars the action bars.
     * @param manager    the {@link UndoRedoManager} instance.
     * @param key        the action key.
     */
    public static void assignUndoRedoAction(IActionBars actionBars,
                                            UndoRedoManager manager,
                                            String key) {

        // Obtain the current action that is associated with this key.
        ODOMUndoRedoAction currentAction = (ODOMUndoRedoAction)
                actionBars.getGlobalActionHandler(key);

        if (currentAction == null) {
            // Create the action and associate it with the key.
            if (IWorkbenchActionConstants.UNDO.equals(key)) {
                currentAction = ODOMUndoRedoAction.createUndoAction(manager);
            } else if (IWorkbenchActionConstants.REDO.equals(key)) {
                currentAction = ODOMUndoRedoAction.createRedoAction(manager);
            } else {
                // Unknown key.
                throw new IllegalStateException("Unknown key: " + key);
            }
            actionBars.setGlobalActionHandler(key, currentAction);
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

 17-Mar-04	3346/3	byron	VBM:2004021308 Cannot undo deletion or cutting of single or multiple assets - fixed javadoc

 17-Mar-04	3346/1	byron	VBM:2004021308 Cannot undo deletion or cutting of single or multiple assets

 ===========================================================================
*/
