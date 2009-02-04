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

package com.volantis.mcs.eclipse.controls;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * Handles the redirection of the global Cut, Copy, Paste, and Select All
 * actions to either the current inline text control or the part's supplied
 * action handler. <p> Example usage:
 * <pre>
 * handler = new ActionableHandler(getEditorSite().getActionBars());
 * handler.addText(new Actionable((Text)aCompositite.getTextControl()));
 * handler.addText(new Actionable((Combo)aComposite.getComboControl()));
 * handler.setSelectAllAction(selectAllAction);
 * </pre>
 * </p>
 */
public class ActionableHandler {

    /**
     * The delete action handler.
     */
    private DeleteActionHandler deleteActionHandler = new DeleteActionHandler();

    /**
     * The cut action handler.
     */
    private CutActionHandler cutActionHandler = new CutActionHandler();

    /**
     * The copy action handler.
     */
    private CopyActionHandler copyActionHandler = new CopyActionHandler();

    /**
     * The paste action handler.
     */
    private PasteActionHandler pasteActionHandler = new PasteActionHandler();

    /**
     * The select all action handler.
     */
    private SelectAllActionHandler selectAllActionHandler = new SelectAllActionHandler();

    /**
     * The delete action.
     */
    private IAction deleteAction;

    /**
     * The cut action.
     */
    private IAction cutAction;

    /**
     * The copy action.
     */
    private IAction copyAction;

    /**
     * The paste action.
     */
    private IAction pasteAction;

    /**
     * The select all action.
     */
    private IAction selectAllAction;

    /**
     * The delete action listener that listeners to property changes on the
     * source actionable.
     */
    private IPropertyChangeListener deleteActionListener =
            new PropertyChangeListener(deleteActionHandler);

    /**
     * The cut action listener that listeners to property changes on the
     * source actionable.
     */
    private IPropertyChangeListener cutActionListener =
            new PropertyChangeListener(cutActionHandler);

    /**
     * The copy action listener that listeners to property changes on the
     * source actionable.
     */
    private IPropertyChangeListener copyActionListener =
            new PropertyChangeListener(copyActionHandler);

    /**
     * The paste action listener that listeners to property changes on the
     * source actionable.
     */
    private IPropertyChangeListener pasteActionListener =
            new PropertyChangeListener(pasteActionHandler);

    /**
     * The select all action listener that listeners to property changes on the
     * source actionable.
     */
    private IPropertyChangeListener selectAllActionListener =
            new PropertyChangeListener(selectAllActionHandler);

    /**
     * The actionable listener that listeners to activate and de-activate events.
     */
    private Listener controlListener = new ControlListener();

    /**
     * The actionable control itself.
     */
    private Actionable actionable;

    /**
     * Mouse Adapter that updates the enabled state of the controls.
     */
    private MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mouseUp(MouseEvent e) {
            updateActionsEnableState();
        }
    };

    /**
     * Key Adapter that updates the enabled state of the controls.
     */
    private KeyAdapter keyAdapter = new KeyAdapter() {
        public void keyReleased(KeyEvent e) {
            if (e.keyCode == SWT.TAB) {
                // If we tab to the control we need update the actionable field
                // so that the actions, e.g. COPY become active if necessary..
                actionable = (Actionable)e.widget.getData(
                        Actionable.ACTIONABLE_DATA_KEY);
            }
            updateActionsEnableState();
        }
    };

    /**
     * The actionable listener that updates the enabled state based on whether or
     * not the actionable is adtived or de-activated.
     */
    private class ControlListener implements Listener {
        // javadoc inherited.
        public void handleEvent(Event event) {
            switch (event.type) {
                case SWT.Activate:
                    actionable = (Actionable)event.widget.getData(
                            Actionable.ACTIONABLE_DATA_KEY);
                    updateActionsEnableState();
                    break;
                case SWT.Deactivate:
                    actionable = null;
                    updateActionsEnableState();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * The property change listener that updates the status of the action
     * based on property change events.
     */
    private class PropertyChangeListener implements IPropertyChangeListener {
        /**
         * The action handler action.
         */
        private IAction actionHandler;

        /**
         * Default constructor.
         * @param actionHandler the action handler.
         */
        protected PropertyChangeListener(IAction actionHandler) {
            super();
            this.actionHandler = actionHandler;
        }

        // javadoc inherited.
        public void propertyChange(PropertyChangeEvent event) {
            if (actionable != null) {
                return;
            }
            if (event.getProperty().equals(IAction.ENABLED)) {
                actionHandler.setEnabled(
                        ((Boolean) event.getNewValue()).booleanValue());
            }
        }
    };

    /**
     * The delete action handler which delegates delete actions to the
     * associated actionable.
     */
    private class DeleteActionHandler extends Action {
        /**
         * Construct the action handler.
         */
        protected DeleteActionHandler() {
            super("Delete");
            setId("ActionableDeleteActionHandler");
            setEnabled(false);
        }

        // javadoc inherited.
        public void runWithEvent(Event event) {
            if (actionable != null && !actionable.isDisposed()) {
                actionable.clearSelection();
                return;
            }
            if (deleteAction != null) {
                deleteAction.runWithEvent(event);
                return;
            }
        }

        /**
         * Update the enabled state for this action based on the current
         * selection state of the actionable.
         */
        public void updateEnabledState() {
            if (actionable != null && !actionable.isDisposed()) {
                setEnabled(actionable.isEnabled(IWorkbenchActionConstants.DELETE));
                return;
            }
            if (deleteAction != null) {
                setEnabled(deleteAction.isEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    /**
     * The cut action handler.
     */
    private class CutActionHandler extends Action {
        /**
         * Construct the action handler.
         */
        protected CutActionHandler() {
            super("Cut");
            setId("ActionableCutActionHandler");
            setEnabled(false);
        }

        // javadoc inherited
        public void runWithEvent(Event event) {
            if (actionable != null && !actionable.isDisposed()) {
                actionable.cut();
                return;
            }
            if (cutAction != null) {
                cutAction.runWithEvent(event);
                return;
            }
        }

        /**
         * Update the enabled state for this action based on the current
         * selection state of the actionable.
         */
        public void updateEnabledState() {
            if (actionable != null && !actionable.isDisposed()) {
                setEnabled(actionable.isEnabled(IWorkbenchActionConstants.CUT));
                return;
            }
            if (cutAction != null) {
                setEnabled(cutAction.isEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    /**
     * Copy action handler.
     */
    private class CopyActionHandler extends Action {
        /**
         * Construct the action handler.
         */
        protected CopyActionHandler() {
            super("Copy");
            setId("ActionableCopyActionHandler");
            setEnabled(false);
        }

        // javadoc inherited.
        public void runWithEvent(Event event) {
            if (actionable != null && !actionable.isDisposed()) {
                actionable.copy();
                return;
            }
            if (copyAction != null) {
                copyAction.runWithEvent(event);
                return;
            }
        }

        /**
         * Update the enabled state for this action based on the current
         * selection state of the actionable.
         */
        public void updateEnabledState() {
            if (actionable != null && !actionable.isDisposed()) {
                setEnabled(actionable.isEnabled(IWorkbenchActionConstants.COPY));
                return;
            }
            if (copyAction != null) {
                setEnabled(copyAction.isEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    /**
     * Paste action handler.
     */
    private class PasteActionHandler extends Action {
        /**
         * Construct the action handler.
         */
        protected PasteActionHandler() {
            super("Paste");
            setId("ActionablePasteActionHandler");
            setEnabled(false);
        }

        // javadoc inherited
        public void runWithEvent(Event event) {
            if (actionable != null && !actionable.isDisposed()) {
                actionable.paste();
                return;
            }
            if (pasteAction != null) {
                pasteAction.runWithEvent(event);
                return;
            }
        }

        /**
         * Update the enabled state for this action based on the current
         * selection state of the actionable.
         */
        public void updateEnabledState() {
            if (actionable != null && !actionable.isDisposed()) {
                setEnabled(true);
                return;
            }
            if (pasteAction != null) {
                setEnabled(pasteAction.isEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    /**
     * Select all action handler.
     */
    private class SelectAllActionHandler extends Action {
        /**
         * Construct the action handler.
         */
        protected SelectAllActionHandler() {
            super("TextAction.selectAll");
            setId("ActionableSelectAllActionHandler");
            setEnabled(false);
        }

        // javadoc inherited.
        public void runWithEvent(Event event) {
            if (actionable != null && !actionable.isDisposed()) {
                actionable.selectAll();
                return;
            }
            if (selectAllAction != null) {
                selectAllAction.runWithEvent(event);
                return;
            }
        }

        /**
         * Update the enabled state of this action.
         */
        public void updateEnabledState() {
            if (actionable != null && !actionable.isDisposed()) {
                setEnabled(true);
                return;
            }
            if (selectAllAction != null) {
                setEnabled(selectAllAction.isEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    /**
     * Creates a <code>Actionable</code> actionable action handler
     * for the global Cut, Copy, Paste, Delete, and Select All
     * of the action bar.
     *
     * @param actionBar the action bar to register global
     *    action handlers for Cut, Copy, Paste, Delete,
     * 	  and Select All
     */
    public ActionableHandler(IActionBars actionBar) {
        super();
        actionBar.setGlobalActionHandler(IWorkbenchActionConstants.CUT,
                cutActionHandler);
        actionBar.setGlobalActionHandler(IWorkbenchActionConstants.COPY,
                copyActionHandler);
        actionBar.setGlobalActionHandler(IWorkbenchActionConstants.PASTE,
                pasteActionHandler);
        actionBar.setGlobalActionHandler(IWorkbenchActionConstants.SELECT_ALL,
                selectAllActionHandler);
        actionBar.setGlobalActionHandler(IWorkbenchActionConstants.DELETE,
                deleteActionHandler);
    }

    /**
     * Add a <code>Actionable</code> actionable to the handler
     * so that the Cut, Copy, Paste, Delete, and Select All
     * actions are redirected to it when active.
     *
     * @param control the inline <code>Actionable</code> actionable
     */
    public void addControl(Actionable control) {
        if (control == null) {
            return;
        }

        this.actionable = control;
        control.addListener(SWT.Activate, controlListener);
        control.addListener(SWT.Deactivate, controlListener);

        // We really want a selection listener but it is not supported so we
        // use a key listener and a mouse listener to know when selection changes
        // may have occured.
        control.addKeyListener(keyAdapter);
        control.addMouseListener(mouseAdapter);
    }

    /**
     * Dispose of this action handler
     */
    public void dispose() {
        setCutAction(null);
        setCopyAction(null);
        setPasteAction(null);
        setSelectAllAction(null);
        setDeleteAction(null);
    }

    /**
     * Removes a <code>Actionable</code> actionable from the handler
     * so that the Cut, Copy, Paste, Delete, and Select All
     * actions are no longer redirected to it when active.
     *
     * @param control the inline <code>Actionable</code> actionable
     */
    public void removeControl(Actionable control) {
        if (control == null) {
            return;
        }

        control.removeListener(SWT.Activate, controlListener);
        control.removeListener(SWT.Deactivate, controlListener);

        control.removeMouseListener(mouseAdapter);
        control.removeKeyListener(keyAdapter);

        this.actionable = null;
        updateActionsEnableState();
    }

    /**
     * Set the default <code>IAction</code> handler for the Copy action. This
     * <code>IAction</code> is run only if no active inline text control.
     *
     * @param action the <code>IAction</code> to run for the Copy action, or
     *               <code>null</null> if not interested.
     */
    public void setCopyAction(IAction action) {
        if (copyAction == action) {
            return;
        }

        if (copyAction != null) {
            copyAction.removePropertyChangeListener(copyActionListener);
        }

        copyAction = action;

        if (copyAction != null) {
            copyAction.addPropertyChangeListener(copyActionListener);
        }

        copyActionHandler.updateEnabledState();
    }

    /**
     * Set the default <code>IAction</code> handler for the Cut action. This
     * <code>IAction</code> is run only if no active inline text control.
     *
     * @param action the <code>IAction</code> to run for the Cut action, or
     *               <code>null</null> if not interested.
     */
    public void setCutAction(IAction action) {
        if (cutAction == action) {
            return;
        }

        if (cutAction != null) {
            cutAction.removePropertyChangeListener(cutActionListener);
        }

        cutAction = action;

        if (cutAction != null) {
            cutAction.addPropertyChangeListener(cutActionListener);
        }

        cutActionHandler.updateEnabledState();
    }

    /**
     * Set the default <code>IAction</code> handler for the Paste action. This
     * <code>IAction</code> is run only if no active inline text control.
     *
     * @param action the <code>IAction</code> to run for the Paste action, or
     *               <code>null</null> if not interested.
     */
    public void setPasteAction(IAction action) {
        if (pasteAction == action) {
            return;
        }

        if (pasteAction != null) {
            pasteAction.removePropertyChangeListener(pasteActionListener);
        }

        pasteAction = action;

        if (pasteAction != null) {
            pasteAction.addPropertyChangeListener(pasteActionListener);
        }

        pasteActionHandler.updateEnabledState();
    }

    /**
     * Set the default <code>IAction</code> handler for the Select All action.
     * This <code>IAction</code> is run only if no active inline text
     * control.
     *
     * @param action the <code>IAction</code> to run for the Select All action,
     *               or <code>null</null> if not interested.
     */
    public void setSelectAllAction(IAction action) {
        if (selectAllAction == action) {
            return;
        }

        if (selectAllAction != null) {
            selectAllAction.removePropertyChangeListener(selectAllActionListener);
        }

        selectAllAction = action;

        if (selectAllAction != null) {
            selectAllAction.addPropertyChangeListener(selectAllActionListener);
        }

        selectAllActionHandler.updateEnabledState();
    }

    /**
     * Set the default <code>IAction</code> handler for the Delete action. This
     * <code>IAction</code> is run only if no active inline text control.
     *
     * @param action the <code>IAction</code> to run for the Delete action, or
     *               <code>null</null> if not interested.
     */
    public void setDeleteAction(IAction action) {
        if (deleteAction == action) {
            return;
        }

        if (deleteAction != null) {
            deleteAction.removePropertyChangeListener(deleteActionListener);
        }

        deleteAction = action;

        if (deleteAction != null) {
            deleteAction.addPropertyChangeListener(deleteActionListener);
        }

        deleteActionHandler.updateEnabledState();
    }

    /**
     * Update the enable state of the Cut, Copy, Paste, Delete, and Select All
     * action handlers
     */
    private void updateActionsEnableState() {
        cutActionHandler.updateEnabledState();
        copyActionHandler.updateEnabledState();
        pasteActionHandler.updateEnabledState();
        selectAllActionHandler.updateEnabledState();
        deleteActionHandler.updateEnabledState();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Nov-05	10064/1	adrianj	VBM:2005110106 Allow GUI to run in Eclipse 3.1

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Feb-04	3057/3	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors - fixed javadoc

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 ===========================================================================
*/
