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

import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @todo javadoc this class
 */
public class PathSelectionDialog extends ListDialog {
    private static final String PATH_LABEL =
            ControlsMessages.getString("PathSelectionDialog.path.label");

    private String selectedPath;

    public PathSelectionDialog(Shell shell, String[] paths) {
        super(shell);
        setContentProvider(new ArrayContentProvider());
        setLabelProvider(new LabelProvider());
        setInput(paths);
        setBlockOnOpen(true);
    }

    public Control createDialogArea(Composite parent) {

        Composite topLevel = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        topLevel.setLayout(layout);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = 400;
        topLevel.setLayoutData(data);

        layout = new GridLayout();
        topLevel.setLayout(layout);
        Composite pathComposite = new Composite(topLevel, SWT.NONE);
        layout = new GridLayout(2, false);
        data = new GridData(GridData.FILL_HORIZONTAL);

        pathComposite.setLayoutData(data);
        pathComposite.setBackground(parent.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        pathComposite.setLayout(layout);
        Label label = new Label(pathComposite, SWT.NONE);
        label.setText(PATH_LABEL);
        label.setBackground(parent.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        final Text text = new Text(pathComposite, SWT.SINGLE | SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(data);
        getTableViewer().addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if (!event.getSelection().isEmpty()) {
                    IStructuredSelection selection =
                            (IStructuredSelection) event.getSelection();
                    text.setText((String) selection.getFirstElement());
                }
            }
        });

        text.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                selectedPath = text.getText();
            }

        });
        return topLevel;
    }

    protected void cancelPressed() {
        selectedPath = null;
    }

    public String getSelectedPath() {
        return selectedPath;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 ===========================================================================
*/
