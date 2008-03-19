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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.eclipse.validation.PolicyNameValidator;
import com.volantis.mcs.eclipse.controls.SelectionDialogDetails;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

/**
 * StylePropertyBrowseAction implementation that displays
 * a PolicyTreeSelectionDialog for a given policy type
 */
public class PolicySelectorBrowseAction implements StylePropertyBrowseAction {
    private static final String RESOURCE_PREFIX = "PolicySelectorBrowseAction.";

    /**
     * FileExtension array.
     */
    private final FileExtension[] extensions;

    /**
     * The policy name validator
     */
    private final PolicyNameValidator policyNameValidator;

    /**
     * Initializes a <code>PolicySelectorBrowseAction</code> instance
     * with the given parameter
     * @param extension the FileExtension for the policy type that
     * the dialog should allow users to select
     */
    public PolicySelectorBrowseAction(FileExtension extension) {
        this.extensions = new FileExtension[]{extension};
        // create the validator
        this.policyNameValidator =
                    new PolicyNameValidator(null, extensions, false);
    }

    /**
     * Creates a SelectionDialogDetails
     * @param entityID the entity ID
     * @return a SelectionDialogDetails instance
     */
    private SelectionDialogDetails createSelectionDialogDetails(
                String entityID) {
        String policyTypeName = extensions[0].getPolicyTypeName();
        // Obtain the dialogs title from the themes bundle
        String title = ThemesMessages.getString(RESOURCE_PREFIX +
                                                "title." +
                                                policyTypeName);
        // Obtain the dialogs message from the themes bundle
        String message = ThemesMessages.getString(RESOURCE_PREFIX +
                                                  "message." +
                                                  policyTypeName);
        // Obtain the dialogs empty list message from the themes bundle
        String emptyListMessage = ThemesMessages.getString(RESOURCE_PREFIX +
                                                           "emptyList." +
                                                           policyTypeName);
        // factor the selection dialog details
        return new SelectionDialogDetails(entityID,
                                          title,
                                          message,
                                          emptyListMessage,
                                          extensions);
    }

    // javadoc inherited
    public String doBrowse(String value, Composite parent, EditorContext context) {
        String policyTypeName = extensions[0].getPolicyTypeName();
        PolicyTreeSelectionDialog dialog =
                    new PolicyTreeSelectionDialog(
                                parent.getShell(),
                                createSelectionDialogDetails(value),
                                new QualifiedName(policyTypeName,
                                                  parent.toString()),
                                context.getProject(),
                                policyNameValidator);
        // if cancel is pressed the BrowseAction contract requires the
        // value that was passed in to be returned.
        String selectedPolicy = value;
        // If OK was selected, return the selected value
        if (dialog.doSelectionDialog() == ElementTreeSelectionDialog.OK) {
            selectedPolicy = dialog.getPolicyReference();
        }
        return selectedPolicy;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Nov-05	10197/1	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 ===========================================================================
*/
