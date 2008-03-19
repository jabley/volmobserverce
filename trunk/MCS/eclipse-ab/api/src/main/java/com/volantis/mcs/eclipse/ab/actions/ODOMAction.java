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
package com.volantis.mcs.eclipse.ab.actions;

import java.util.ResourceBundle;

import org.eclipse.ui.texteditor.ResourceAction;

import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;

/**
 * This class should be used for resource actions that operate on ODOMs in some
 * way, where the actions may or may not depend on an ODOM element selection to
 * determine their enablement status. The action processing is delegated to the
 * associated {@link ODOMActionCommand}.
 *
 * <p><strong>NOTE</strong>: the {@link #dispose} method must be called when
 * this action is no longer required.</p>
 *
 * <p><em>Implementation Note</em>: It is quite possible that this mechanism could be
 * effected via explicit calls using the Eclipse ISelectionProvider facilities
 * if the ODOMSelectionManager were to be updated to become a selection
 * provider. See <a
 * href="http://mantis:8080/mantis/Mantis_View_Body.jsp?mantisid=2003121511#detail23400">
 * 2003121511</a> for further discussion. This would
 * avoid the need to use ODOM events and listeners at all as long as:</p>
 *
 * <ol>
 *
 * <li>the ODOMSelectionManager could provide an optionally filtered
 * selection</li>
 *
 * <li>the action enablement is set immediately prior to display within a
 * menu</li>
 *
 * </ol>
 */
public class ODOMAction extends ResourceAction {

    /**
     * The command to which this action delegates enablement and processing
     *
     * @supplierCardinality 1
     */
    private ODOMActionCommand command;

    /**
     * The editor context for this action if it is selection-sensitive
     * and demarcates undo/redo
     *
     * @supplierCardinality 0..1
     */
    private ODOMEditorContext editorContext;

    /**
     * The selection filter for this action if it is selection-sensitive and
     * the selection needs to be filtered
     *
     * @supplierCardinality 0..1
     */
    private ODOMSelectionFilter filter;

    /**
     * This internal listener used to listen for selection changes
     *
     * @supplierCardinality 0..1
     */
    private ODOMElementSelectionListener listener;

    /**
     * This event contains the latest selection information
     *
     * @supplierCardinality 1
     */
    private ArrayBasedActionDetails actionDetails;

    /**
     * @see #isDemarcateUndoRedoUOW()
     */
    private boolean demarcateUndoRedoUOW = true;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param command the command that actually determines enablement and
     *                performs action processing
     * @param bundle  the bundle from which the action's properties will be
     *                obtained. Must be specified
     * @param prefix  the resource naming prefix. May be null
     */
    public ODOMAction(ODOMActionCommand command,
                      ResourceBundle bundle,
                      String prefix) {
        this(command, null, null, bundle, prefix);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param command the command that actually determines enablement and
     *                performs action processing
     * @param bundle  the bundle from which the action's properties will be
     *                obtained. Must be specified
     * @param prefix  the resource naming prefix. May be null
     * @param style   the style of the action, see
     *                {@link org.eclipse.jface.action.IAction} for the
     *                style options
     */
    public ODOMAction(ODOMActionCommand command,
                      ResourceBundle bundle,
                      String prefix,
                      int style) {
        this(command, null, null, bundle, prefix, style);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param command       the command that actually determines enablement and
     *                      performs action processing
     * @param editorContext the ODOMEditorContext that can provide this action
     *                      with a selection manager and an undoRedo manager.
     *                      May be null
     * @param filter        the filter that should be used when registering the
     *                      action. May be null
     * @param bundle        the bundle from which the action's properties will
     *                      be obtained. Must be specified
     * @param prefix        the resource naming prefix. May be null
     * @param style         the style of the action, see
     *                      {@link org.eclipse.jface.action.IAction IAction}
     *                      for the style options
     */
    public ODOMAction(ODOMActionCommand command,
                      ODOMEditorContext editorContext,
                      ODOMSelectionFilter filter,
                      ResourceBundle bundle,
                      String prefix,
                      int style) {
        super(bundle, prefix, style);

        initialize(command, editorContext, filter);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param command       the command that actually determines enablement and
     *                      performs action processing
     * @param editorContext the ODOMEditorContext that can provide this action
     *                      with a selection manager and an undoRedo manager.
     *                      May be null
     * @param filter        the filter that should be used when registering the
     *                      action. May be, and should commonly be, null since
     *                      all selections are probably important to this action
     *                      in determining its enablement status
     * @param bundle        the bundle from which the action's properties will
     *                      be obtained. Must be specified
     * @param prefix        the resource naming prefix. May be null
     */
    public ODOMAction(ODOMActionCommand command,
                      ODOMEditorContext editorContext,
                      ODOMSelectionFilter filter,
                      ResourceBundle bundle,
                      String prefix) {
        super(bundle, prefix);

        initialize(command, editorContext, filter);
    }

    /**
     * Initializes the manager, filter and event members.
     *
     * @param editorContext the editorContext to be used
     * @param filter        the filter to be used
     */
    protected void initialize(ODOMActionCommand command,
                              ODOMEditorContext editorContext,
                              ODOMSelectionFilter filter) {
        if (command == null) {
            throw new IllegalArgumentException(
                "A non-null command must be associated " + //$NON-NLS-1$
                "with this action"); //$NON-NLS-1$
        }

        this.actionDetails = new ArrayBasedActionDetails();

        this.command = command;
        this.editorContext = editorContext;
        this.filter = filter;

        // Before registering (in case we get notified of an existing selection
        // on registration), set the action to a sensible default enablement
        // condition
        setEnabled(command.enable(actionDetails));
        
        if ((editorContext != null) && (editorContext.getODOMSelectionManager()!=null)) {
            // Set up a listener that will update the action's enablement
            // status, using the {@link ODOMActionCommand#enable} method return
            // value, storing the selection in the actionDetails
            listener = new ODOMElementSelectionListener() {
                public void selectionChanged(ODOMElementSelectionEvent event) {
                    actionDetails.setElements(event.getSelection().
                            toODOMElementArray());

                    setEnabled(ODOMAction.this.command.enable(actionDetails));
                }
            };

            editorContext.getODOMSelectionManager().addSelectionListener(listener, this.filter);
        }
    }

    /**
     * This method must be called by the user of the action when the action is
     * no longer needed.
     */
    public void dispose() {
        if ((editorContext != null) && (editorContext.getODOMSelectionManager()!=null) && (listener != null)) {
            editorContext.getODOMSelectionManager().removeSelectionListener(listener, filter);
        }
    }

    // javadoc inherited
    public void run() {
        if (command.enable(actionDetails)) {
            //since demarcateUOW is idempotent and cheap, we can afford to do
            //undo/redo demarcation before and after the command run, thus
            //minimizing missed demarcations because of focus bug
            checkDemarcateUndoRedoUOW();

            command.run(actionDetails);

            checkDemarcateUndoRedoUOW();
        }
    }


    /**
     * Boolean property that determines whether running this action will
     * demarcate an undo/redo Unit of Work.
     * <p>
     * Its value is relevant when an editorContext
     * with undoRedomanager is available for this action
     * </p>
     * @return the value of the property
     */
    public boolean isDemarcateUndoRedoUOW() {
        return demarcateUndoRedoUOW;
    }


    /**
     * @see #isDemarcateUndoRedoUOW()
     */
    public void setDemarcateUndoRedoUOW(boolean demarcateUndoRedoUOW) {
        this.demarcateUndoRedoUOW = demarcateUndoRedoUOW;
    }


    /**
     * demarcates an Undo/Redo unit of work,
     * if an editorContext with undoRedomanager is available for this action.
     */
    private void checkDemarcateUndoRedoUOW() {
        if (demarcateUndoRedoUOW  &&
            editorContext != null &&
            editorContext.getUndoRedoManager() != null) {
            editorContext.getUndoRedoManager().demarcateUOW();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Feb-04	2924/1	eduardo	VBM:2004021003 editor actions demarcate UndoRedo UOWs

 11-Feb-04	2939/1	eduardo	VBM:2004020506 ODOM DeleteActionCommand changed to be undo/redo friendly

 09-Feb-04	2800/1	eduardo	VBM:2004012802 undo redo works from outline view

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 29-Jan-04	2797/1	philws	VBM:2004012903 Make the layout editor context menu device layout type sensitive

 23-Jan-04	2720/1	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 15-Jan-04	2618/1	allan	VBM:2004011510 Provide an IStructuredSelection for selected ODOMElements.

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
