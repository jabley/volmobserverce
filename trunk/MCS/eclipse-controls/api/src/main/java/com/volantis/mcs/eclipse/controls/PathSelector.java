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

package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.validation.Validated;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * The abstract class for Path Selections.
 */
public abstract class PathSelector
        extends TextButton
        implements Validated {

    private static String mark = "(c) Volantis Systems Ltd 2003."; //$NON-NLS-1$

    /**
     * Store the qualified name of this resource. Note that this qualified
     * name should be unique depending on the the context that the dialog
     * is invoked from. This will allow the initial selection to be settable
     * for various contexts on a session basis (improving usability).
     */
    private QualifiedName qNameInitialSelection;

    /**
     * Construct the object with the specified parent and style.
     *
     * @param parent the parent composite.
     * @param style  the style to use.
     */
    protected PathSelector(Composite parent,
                               int style,
                               QualifiedName qName) {
        super(parent, style);
        this.qNameInitialSelection = qName;

        getButton().addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                buttonSelected();
            }
        });
    }

    /**
     * React when the button is selected. This should show the dialog and update
     * the initial selection if the result isn't null.
     */
    protected void buttonSelected() {
        String result = showDialog();
        if (result != null) {
            setValue(result);
            updateInitialSelection(getValue());
        }
    }

    /**
     * Display the file selection dialog.
     *
     * @return the file name that has been selected.
     */
    protected abstract String showDialog();

    /**
     * Get the intitial selection from the session.
     *
     * @return the intitial selection from the session.
     */
    protected Object getInitialSelection() {
        String result = null;
        try {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            result = (String)root.getSessionProperty(qNameInitialSelection);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
        return result;
    }

    /**
     * Update the initial selection with the specified file name.
     *
     * @param initialValue the initial value.
     */
    protected void updateInitialSelection(Object initialValue) {
        try {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            root.setSessionProperty(qNameInitialSelection, initialValue);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
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

 27-Feb-04	3200/2	allan	VBM:2004022410 Basic Update Client Wizard.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 04-Nov-03	1790/3	byron	VBM:2003102408 Provide a FolderSelector - addressed rework issues

 03-Nov-03	1790/1	byron	VBM:2003102408 Provide a FolderSelector

 ===========================================================================
*/
