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

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * The NodeSelectionDialog is a SelectionDialog which displays a tree according
 * to the specified root node, content provider and label provider. The dialog
 * displays a tree whose nodes are single-select, and returns the selected node
 * with {@link #getResult} when the OK button is pressed. If Cancel is pressed
 * then {@link #getResult} returns null.
 */
public class NodeSelectionDialog extends SelectionDialog {

    /**
     * Resource prefix for the NodeSelectionDialog.
     */
    private final static String RESOURCE_PREFIX =
                "NodeSelectionDialog."; //$NON-NLS-1$

    /**
     * The margin height for the dialog.
     */
    private static final int MARGIN_HEIGHT = ControlsMessages.getInteger(
                RESOURCE_PREFIX + "marginHeight").intValue(); //$NON-NLS-1$

    /**
     * The margin width for the dialog.
     */
    private static final int MARGIN_WIDTH = ControlsMessages.getInteger(
                RESOURCE_PREFIX + "marginWidth").intValue(); //$NON-NLS-1$

    /**
     * The vertical spacing between widgets.
     */
    private static final int VERTICAL_SPACING = ControlsMessages.getInteger(
                RESOURCE_PREFIX + "verticalSpacing").intValue(); //$NON-NLS-1$

    /**
     * The width hint for the dialog.
     */
    private static final int WIDTH_HINT = ControlsMessages.getInteger(
                RESOURCE_PREFIX + "widthHint").intValue(); //$NON-NLS-1$

    /**
     * The height hint for the dialog.
     */
    private static final int HEIGHT_HINT = ControlsMessages.getInteger(
                RESOURCE_PREFIX + "heightHint").intValue(); //$NON-NLS-1$

    /**
     * The content provider for the tree viewer.
     */
    private final ITreeContentProvider contentProvider;

    /**
     * The label provider for the tree viewer.
     */
    private final ILabelProvider labelProvider;

    /**
     * The tree viewer used to display the tree according to the content and
     * label providers. The tree viewer also listens for selection changes.
     */
    private TreeViewer treeViewer;

    /**
     * The root node of the tree.
     * <STRONG>
     * Note that the root node is not displayed by the tree viewer.
     * </STRONG>
     */
    private final Object rootNode;

    /**
     * The text to display in a Label above the tree viewer.
     */
    private final String prompt;

    /**
     * Creeats a new NodeSelectionDialog.
     * @param parent the parent Shell of the dialog. Cannot be null.
     * @param prompt the text to display on the dialog for prompting a
     *               selection. Cannot be null or empty.
     * @param rootNode the root node for the tree.
     *                 <STRONG>This node is not displayed.</STRONG> Cannot be
     *                 null. The root node must also be of the same type as
     *                 the objects manipulated by the content provider and the
     *                 label provider if necessary.
     * @param contentProvider the content provider to use for describing the
     *                        tree's contents.
     * @param labelProvider the label provider to use for supplying labels for
     *                      the tree's nodes.
     */
    public NodeSelectionDialog(Shell parent,
                               String prompt,
                               Object rootNode,
                               ITreeContentProvider contentProvider,
                               ILabelProvider labelProvider) {
        super(parent);
        if (prompt == null || prompt.length() == 0) {
            throw new IllegalArgumentException("Cannot be null or empty: " +
                    "prompt.");
        }
        if (rootNode == null) {
            throw new IllegalArgumentException("Cannot be null: rootNode.");
        }
        if (contentProvider == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "contentProvider.");
        }
        if (labelProvider == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "labelProvider.");
        }
        this.prompt = prompt;
        this.rootNode = rootNode;
        this.contentProvider = contentProvider;
        this.labelProvider = labelProvider;
    }

    /**
     * Creates the dialog's controls. This is called each time the dialog is
     * opened.
     * @param parent the parent Composite of the dialog's control
     * @return the created control
     */
    protected Control createDialogArea(Composite parent) {
        Composite topLevel = (Composite) super.createDialogArea(parent);
        GridLayout topLevelGrid = new GridLayout();
        topLevelGrid.verticalSpacing = VERTICAL_SPACING;
        topLevelGrid.marginHeight = MARGIN_HEIGHT;
        topLevelGrid.marginWidth = MARGIN_WIDTH;
        GridData topLevelData = new GridData();
        topLevelData.heightHint = 400;
        topLevelData.widthHint = 300;
        topLevel.setLayout(topLevelGrid);
        topLevel.setLayoutData(topLevelData);

        // Add the label with prompt message.
        addLabel(topLevel);

        // Create the Tree widget with scroll bars.
        Tree tree = new Tree(topLevel, SWT.BORDER |
                SWT.H_SCROLL | SWT.V_SCROLL);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Create the TreeViewer and set its providers and root node.
        treeViewer = new TreeViewer(tree);
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(labelProvider);
        treeViewer.setInput(rootNode);
        // expand the tree so that the 2nd level
        treeViewer.expandToLevel(2);
        
        // Listen for selection changes and update {@link currentSelection}
        // accordingly.
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection =
                        (IStructuredSelection) treeViewer.getSelection();
                setResult(selection.toList());
            }
        });

        return topLevel;
    }

    /**
     * Creates and adds a label with the prompt message.
     * @param container
     */
    private void addLabel(Composite container) {
        Label promptLabel = new Label(container, SWT.NONE);
        promptLabel.setText(prompt);
    }

    /**
     * Overidden to clear a selection when Cancel is pressed. Calls to
     * {@link #getResult} will subsequently return null.
     */
    protected void cancelPressed() {
        setResult(null);
        super.cancelPressed();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Apr-04	3878/2	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 30-Mar-04	3634/1	pcameron	VBM:2004032210 Added NodeSelectionDialog

 ===========================================================================
*/
