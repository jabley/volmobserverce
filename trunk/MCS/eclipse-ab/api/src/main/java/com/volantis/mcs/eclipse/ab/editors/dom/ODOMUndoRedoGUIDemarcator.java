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
import com.volantis.mcs.eclipse.common.EclipsePatches;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.FocusEvent;

 /**
  * Object that executes Undo/Redo Unit of work Demarcation based
  * on common UI user gestures (e.g. leaving a widget).
  */
public class ODOMUndoRedoGUIDemarcator {

    final UndoRedoManager undoRedoManager;

    /**
     * Attached to widgets, invokes the undoRedoManager.demarcateUOW method
     */
    private FocusListener  focusDrivenUndoRedoDemarcator;

    /**
     * object applying a workaround to an Eclipse bug
     */
    private EclipsePatches.ComboFocusBugOnSwtGtk2 eclipseComboGtk2Patch;

     /**
      * Costructor that requires an <code>UndoRedoManager</code>
      * whose <code>demarcateUOW()</code> method will be invoked.
      * <p>
      * No demarcation is set up at construction time.
      * </p>
      * @param undoRedoManager must not be null
      */
    public ODOMUndoRedoGUIDemarcator(final UndoRedoManager undoRedoManager) {
        if (undoRedoManager==null) {
            throw new IllegalArgumentException(
                    "'undoRedoManager' must not be null");
        }
        this.undoRedoManager = undoRedoManager;

        /* Arguably, only focusGained <em>or</em> focusLost should be relevant,
         * but because of a bug in Linux SWT 2.1.x (where comboboxes do not fire
         * these events) we handle both these events so that the occurrence of the
         * bug can be minimized. This is feasible because demarcation is a very
         * cheap operation.
         */
         this.focusDrivenUndoRedoDemarcator = new FocusListener() {
             public void focusGained(FocusEvent event) {
                 undoRedoManager.demarcateUOW();
             }

             public void focusLost(FocusEvent event) {
                 undoRedoManager.demarcateUOW();
             }
         };

         this.eclipseComboGtk2Patch =
         new EclipsePatches.ComboFocusBugOnSwtGtk2(undoRedoManager);

    }

    /**
     * Adds a focus listener to the given control (and all its children) that
     * will be used to demarcate undo/redo UOWs when the user moves focus
     * away from an editing widget
     *
     * @param control the control to add a focus listener to
     */
    public void addFocusDrivenUndoRedoDemarcatorFor(Control control) {
        addFocusListenerRecursively(control, focusDrivenUndoRedoDemarcator);
    }

    /**
     * Traverses a widget tree and sets a focus listener recursively.
     * <p>
     * <strong>NOTE:</strong> this utility method could find its place
     * somewhere else (swt utilities ?) but is placed here because
     * its current usages are in EditorContext's clients.
     * It's not static only because it calls the combo-gtk2-patch
     * </p>
     *
     * @param control    a node of a widget tree
     * @param focusListener the focus listener to be added
     */
    private void addFocusListenerRecursively(Control control,
                                             FocusListener focusListener) {
        control.addFocusListener(focusListener);

        eclipseComboGtk2Patch.workaround(control);

        if (control instanceof Composite) {
            Composite composite = (Composite) control;
            Control[] children = composite.getChildren();
            for (int i = 0; i < children.length; i++) {
                Control child = children[i];
                addFocusListenerRecursively(child,
                                            focusListener);
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

 17-Mar-04	3346/1	byron	VBM:2004021308 Cannot undo deletion or cutting of single or multiple assets

 17-Feb-04	2988/1	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 ===========================================================================
*/
