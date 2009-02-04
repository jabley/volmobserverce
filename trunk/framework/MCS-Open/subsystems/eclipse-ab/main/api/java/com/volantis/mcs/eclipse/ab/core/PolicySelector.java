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
package com.volantis.mcs.eclipse.ab.core;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.ProjectProvider;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.eclipse.validation.PolicyNameValidator;
import com.volantis.mcs.eclipse.validation.Validated;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.controls.TextButton;
import com.volantis.mcs.eclipse.controls.SelectionDialogDetails;
import com.volantis.mcs.eclipse.controls.ControlsMessages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

import java.util.HashMap;

/**
 * A control that allows the user to enter a policy. This controller provides
 * a browser from which to select a specified kind of policy.
 */
public class PolicySelector extends TextButton implements Validated {

   /**
    * The selection dialog message key prefix.
    */
   static final String MESSAGE_KEY_PREFIX =
           "PolicySelector.message."; //$NON-NLS-1$

   /**
    * The selection dialog title key prefix.
    */
   static final String TITLE_KEY_PREFIX =
           "PolicySelector.title."; //$NON-NLS-1$

   /**
    * The message key for when no policies are available.
    */
   static final String EMPTY_LIST_KEY_PREFIX =
           "PolicySelector.emptyList."; //$NON-NLS-1$

    /**
     * The session property prefix for PolicySelector session properties.
     */
    private static final String SESSION_PROPERTY_PREFIX = "PolicySelector."; //$NON-NLS-1$

    /**
     * Mapping between fault types understood by this page and
     * message keys in the Wizards properties.
     */
    private static final HashMap MESSAGE_KEY_MAPPINGS;

    /**
     * The PolicyNameValidator for this class.
     */
    private final PolicyNameValidator policyNameValidator;

    static {
        // Initialize message key mappings
        MESSAGE_KEY_MAPPINGS = new HashMap();
        MESSAGE_KEY_MAPPINGS.put(
            FaultTypes.INVALID_CHARACTER,
            "PolicySelector.invalidNameCharacter"); //$NON-NLS-1$
        MESSAGE_KEY_MAPPINGS.put(
            FaultTypes.INVALID_EXTENSION,
            "PolicySelector.invalidExtension"); //$NON-NLS-1$
        MESSAGE_KEY_MAPPINGS.put(
                FaultTypes.INVALID_FIRST_CHARACTER,
                "PolicySelector.invalidFirstCharacter"); //$NON-NLS-1$

        MESSAGE_KEY_MAPPINGS.put(
            FaultTypes.TOO_MANY_CHARACTERS,
            "PolicySelector.nameTooLong"); //$NON-NLS-1$
    }

    /**
     * The selection dialog for this PolicySelector.
     */
    private final PolicyTreeSelectionDialog treeSelectionDialog;

    /**
     * The selection dialog details
     */
    private final SelectionDialogDetails selectionDialogDetails;

    /**
     * The validation message builder
     */
    private final ValidationMessageBuilder validationMsgBuilder;
    /**
     * Construct a PolicySelector
     * @param parent The parent widget; required for superclass constructor
     * @param style This is just here to follow protocol. Currently there are
     * no specific style settings
     * @param selectionDialogDetails Used to control the selection dialog
     * traits
     * @param project The parent project
     */
    public PolicySelector(Composite parent, int style,
        SelectionDialogDetails selectionDialogDetails,
        final IProject project) {
        this(parent, style, selectionDialogDetails, new ProjectProvider() {
            public IProject getProject() {
                return project;
            }
        });
    }

    /**
     * Construct a PolicySelector
     * @param parent The parent widget; required for superclass constructor
     * @param style This is just here to follow protocol. Currently there are
     * no specific style settings
     * @param selectionDialogDetails Used to control the selection dialog
     * traits
     * @param projectProvider The provider for the project that contain
     * the policies that are selectable.
     */
    public PolicySelector(Composite parent, int style,
        SelectionDialogDetails selectionDialogDetails,
        ProjectProvider projectProvider) {
        super(parent, style);

        // Set up blank finals that depend on no other
        this.selectionDialogDetails = selectionDialogDetails;

        // Set the validator, which depends on the selection dialog details
        // (because that contains the extensions)
        this.policyNameValidator =
            new PolicyNameValidator(null,
                this.selectionDialogDetails.fileExtensions, false);

        // Construct the selection dialog, which depends on the selection
        // dialog details (for appearance) and the validator (for the extensions)
        this.treeSelectionDialog =
            new PolicyTreeSelectionDialog(
                this.getShell(),
                this.selectionDialogDetails,
                new QualifiedName(
                    null,
                    SESSION_PROPERTY_PREFIX
                        + this.selectionDialogDetails.entityId),
                projectProvider,
                this.policyNameValidator);

        // Set up the message builder for errors etc
        this.validationMsgBuilder =
            new ValidationMessageBuilder(
                ControlsMessages.getResourceBundle(),
                MESSAGE_KEY_MAPPINGS,
                new Object[] {
                    EclipseCommonMessages.getLocalizedPolicyName(
                        selectionDialogDetails.entityId)});

        // Set the button to invoke the selection dialog, and if the dialog
        // returns OK, stick the selection in the text widget
        getButton().addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                if (treeSelectionDialog.doSelectionDialog() == ElementTreeSelectionDialog.OK) {
                    setValue(treeSelectionDialog.getPolicyReference());
                }
            }
        });
    }

    /**
     * Validate that the Text contains a valid policy name.
     * @return the validation status
     */
    public ValidationStatus validate() {
        return policyNameValidator.validate(getValue(), validationMsgBuilder);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-May-04	4231/2	tom	VBM:2004042704 Fixedup the 2004032606 change

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 27-Feb-04	3246/3	byron	VBM:2004021205 Lack of validation for policy file extensions - fixed '/' appending

 27-Feb-04	3246/1	byron	VBM:2004021205 Lack of validation for policy file extensions

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 13-Feb-04	2985/3	allan	VBM:2004012803 Fix for null project in ProjectProviders

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 05-Dec-03	2128/2	pcameron	VBM:2003112105 Added TextDefinition and refactored PolicySelector

 18-Nov-03	1878/10	richardc	VBM:2003110901 Refactor and use IFolder for session stuff

 14-Nov-03	1878/6	richardc	VBM:2003110901 Moved SESSION stuff back to PolicySelector

 14-Nov-03	1878/4	richardc	VBM:2003110901 VBM 2003110901

 31-Oct-03	1642/5	byron	VBM:2003102409 Add support for webcontent directory - addressed some rework issues

 31-Oct-03	1642/3	byron	VBM:2003102409 Add support for webcontent directory

 24-Oct-03	1636/1	pcameron	VBM:2003102402 Renamed some missed methods after refactoring

 21-Oct-03	1502/15	allan	VBM:2003092202 Don't validate extension when minChars is < 1.

 20-Oct-03	1502/13	allan	VBM:2003092202 Rework fixes.

 20-Oct-03	1502/11	allan	VBM:2003092202 Completed validation for PolicySelector.

 17-Oct-03	1502/7	allan	VBM:2003092202 Set the selected policy in the Text properly. Improved error handling.

 17-Oct-03	1502/5	allan	VBM:2003092202 Component selection dialog with filtering and error handling

 16-Oct-03	1502/3	allan	VBM:2003092202 Updated TextButton

 ===========================================================================
*/
