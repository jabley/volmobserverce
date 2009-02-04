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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.eclipse.builder.common.targets.TargetSelector;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.controls.MessageAreaSelectionDialog;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.variants.selection.CategoryReference;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A dialog for selecting target devices/categories.
 */
public class TargetSelectionDialog extends MessageAreaSelectionDialog {
    /**
     * Policy factory for creating category references.
     */
    private static final PolicyFactory POLICY_FACTORY =
            PolicyFactory.getDefaultInstance();

    /**
     * Editor context shared by editors for this resource.
     */
    private EditorContext context;

    /**
     * The target selector component used to select the targets.
     */
    private TargetSelector selector;

    /**
     * The initially selected targets.
     */
    private Collection initialTargets;

    /**
     * Constructor for a TargetSelectionDialog.
     *
     * @param parent the parent shell of the dialog
     * @param originalList The list of targets that are selected initially
     * @param context The editor context for the resource being edited
     */
    public TargetSelectionDialog(Shell parent, Collection originalList, EditorContext context) {
        super(parent);
        this.initialTargets = originalList;
        this.context = context;
        setTitle("Target Selection");
    }

    // Javadoc inherited
    protected Control createDialogArea(Composite composite) {
        // Retrieve the category values from the context
        List categories = new ArrayList();
        List categoryValues = ((PolicyEditorContext) context).getCategoryValues();
        if (categoryValues != null && !categoryValues.isEmpty()) {
            Iterator it = categoryValues.iterator();
            while (it.hasNext()) {
                String value = (String) it.next();
                CategoryReference categoryRef =
                        POLICY_FACTORY.createCategoryReference(value);
                categories.add(categoryRef);
            }
        }

        Composite container = new Composite(composite, SWT.NONE);
        container.setLayout(new GridLayout(1, true));

        selector = new TargetSelector(container, categories, initialTargets, context.getProject());

        return container;
    }

    /**
     * Returns the targets selected by this dialog.
     *
     * @return The targets selected by this dialog
     */
    public Collection getSelectedTargets() {
        return selector.getSelectedTargets();
    }

    /**
     * Convenience method for displaying a target selection dialog to edit a
     * provided list of targets.
     *
     * @param currentSelection The list of targets to be edited - will be
     *                         modified by this method call
     * @param parent The parent shell to display the dialog over
     * @param context The context for the resource being edited
     */
    public static void editTargetSelection(Collection currentSelection, Shell parent, EditorContext context) {
        TargetSelectionDialog dialog = new TargetSelectionDialog(parent, currentSelection, context);
        int returnCode = dialog.open();
        Collection selection = dialog.getSelectedTargets();
        if (returnCode == IDialogConstants.OK_ID) {
            currentSelection.clear();
            currentSelection.addAll(selection);
        }
    }
}
