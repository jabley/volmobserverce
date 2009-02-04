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

package com.volantis.mcs.eclipse.builder.wizards.projects;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.controls.FileSelector;
import com.volantis.mcs.eclipse.validation.DeviceRepositoryFileValidator;
import com.volantis.mcs.eclipse.validation.ProjectContainerNameValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.eclipse.builder.properties.CollaborativeWorkingComponent;
import com.volantis.mcs.eclipse.builder.wizards.WizardMessages;
import com.volantis.mcs.objects.FileExtension;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;


/**
 * Handle the properties (device repository location) wizard page.
 *
 * Note that if the properties have been verified to be OK they should be
 * saved as persistent resource using a fully qualified name (only when
 * the 'Finish' button is selected.
 */
public class MCSProjectPropertiesPage
        extends WizardPage
        implements Listener {

    /**
     * The prefix for the name of this page.
     */
    private static final String PAGE_NAME_PREFIX = "MCSProjectPropertiesPage: ";

    /**
     * Resource prefix for obtaining text resources (in Wizard.properties file).
     */
    private static final String RESOURCE_PREFIX = "MCSProjectPropertiesPage.";

    /**
     * The file selector widget used to select repository files.
     */
    private FileSelector fileSelector;

    /**
     * The repository name validator for this class.
     */
    private static final DeviceRepositoryFileValidator REPOSITORY_NAME_VALIDATOR =
            new DeviceRepositoryFileValidator();

    /**
     * The file/folder name validator for this class.
     */
    private static final ProjectContainerNameValidator PROJECT_CONTAINER_NAME_VALIDATOR =
            new ProjectContainerNameValidator(null);

    /**
     * Store the validation message builder.
     */
    private static final ValidationMessageBuilder messageBuilder =
            new ValidationMessageBuilder(null, null, null);

    /**
     * The policy source text control.
     */
    private Text policySource;

    /**
     * The policy source text as set by the setPolicySource method.
     */
    private String origPolicySource;

    private CollaborativeWorkingComponent collaborativeComponent;

    /**
     * Construct the properties page with the selection and element name.
     *
     * @param elementName the element name.
     */
    public MCSProjectPropertiesPage(String elementName) {
        super(PAGE_NAME_PREFIX + elementName);
        setTitle(getResource("pageTitle"));
        setPageComplete(false);
    }

    // javadoc inherited.
    public void createControl(Composite parent) {
        initializeDialogUnits(parent);

        Composite topLevel = new Composite(parent, SWT.NONE);
        // Use grid layout with 2 columns (one for label and the other for
        // the FileSelector control).
        topLevel.setLayout(new GridLayout(2, false));

        Label standardLabel = new Label(topLevel, SWT.NONE);
        standardLabel.setText(getCommonResource("standard.label"));

        setDescription(getResource("pageMessage"));

        fileSelector = new FileSelector(topLevel, SWT.NONE);

        final String[] filterSelections = new String[]{
            "*." + FileExtension.DEVICE_REPOSITORY
        };
        fileSelector.setFilterSelections(filterSelections);
        fileSelector.getText().addListener(SWT.Modify, this);

        String label = EclipseCommonMessages.getString(
                "MCSProjectProperties.policySourceDirectory");
        Label policySrcLabel = new Label(topLevel, SWT.NONE);
        policySrcLabel.setText(label);

        policySource = new Text(topLevel, SWT.BORDER | SWT.SINGLE);
        GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
        textGridData.widthHint = EclipseCommonMessages.getInteger(
                "MCSProjectProperties.Text.textWidth").intValue();
        policySource.setLayoutData(textGridData);
        policySource.setText(MCSProjectNature.DEFAULT_POLICY_SRC_PATH.toOSString());
        policySource.addListener(SWT.Modify, this);

        collaborativeComponent =
            new CollaborativeWorkingComponent(null, topLevel, SWT.NONE);
        final GridData collaborativeCompositeGridData =
            new GridData(GridData.FILL_HORIZONTAL);
        collaborativeCompositeGridData.horizontalSpan = 2;
        collaborativeComponent.setLayoutData(collaborativeCompositeGridData);
        collaborativeComponent.addModifyListener(this);

        setControl(topLevel);

        validatePage();
    }

    /**
     * Helper method to return the resource identified by the name.
     *
     * @param name the name to use as part of the resource key.
     * @return the resource value.
     */
    private String getResource(String name) {
        return WizardMessages.getString(RESOURCE_PREFIX + name);
    }

    private String getCommonResource(String name) {
        return EclipseCommonMessages.getString(
                EclipseCommonMessages.DEVICE_REPOSITORY_RESOURCE_PREFIX + name);
    }

    // javadoc inherited.
    public void handleEvent(Event event) {
        setPageComplete(validatePage());
    }

    /**
     * Set the policy source for this page. Note that if this method is used
     * to set the policy source and the user subsequently changes the value
     * of the policy source then this page will become invalid and will
     * remain invalid until this method is called again. This feature allows
     * the user to change the policy source that is implied by a previous
     * page. If they do so they must change the value implied by the
     * previous page.
     * @param policySourceText The policy source.
     */
    public void setPolicySource(String policySourceText) {
        policySource.setText(policySourceText);
        origPolicySource = policySourceText;
    }

    /**
     * Returns whether this page's controls contain valid values.
     *
     * @return <code>true</code> if all controls are valid, and
     *         <code>false</code> if at least one is invalid
     * @todo If this logic changes, you may also need to change the
     * validatePage method in MCSProjectproperties.
     */
    public boolean validatePage() {
        boolean valid = true;
        ValidationStatus status = null;

        if (origPolicySource != null &&
                !origPolicySource.equals(policySource.getText())) {
            valid = false;
            String message = WizardMessages.getString(RESOURCE_PREFIX +
                    "policySourceConflict.message");
            status = new ValidationStatus(Status.ERROR, message);
        } else {
            status = REPOSITORY_NAME_VALIDATOR.validate(
                    fileSelector.getValue(), messageBuilder);
        }

        if (status.getSeverity() == Status.ERROR) {
            setErrorMessage(status.getMessage());
            valid = false;
        } else if (status.getSeverity() == Status.INFO) {
            setErrorMessage(null);
            setMessage(status.getMessage(), DialogPage.INFORMATION);
        } else {
            status = PROJECT_CONTAINER_NAME_VALIDATOR.validate(
                    policySource.getText().trim(), messageBuilder);
            if (status.getSeverity() == Status.ERROR) {
                setErrorMessage(status.getMessage());
                valid = false;
            } else if (status.getSeverity() == Status.INFO) {
                setErrorMessage(null);
                setMessage(status.getMessage(), DialogPage.INFORMATION);
            } else {
                setErrorMessage(null);
                setMessage(null);
            }
            if (valid) {
                valid = collaborativeComponent.validate(this, messageBuilder);
            }
        }
        return valid;
    }

    /**
     * Get the repository file name.
     *
     * @return the repository file name.
     */
    public String getRepositoryFilename() {
        return fileSelector.getValue();
    }

    /**
     * Get the policy source directory name.
     *
     * @return the policy source directory name.
     */
    public String getPolicySourceDirectory() {
        return policySource.getText().trim();
    }

    /**
     * Returns the collaborative working component.
     * @return the collaborative working component
     */
    public CollaborativeWorkingComponent getCollaborativeWorkingComponent() {
        return collaborativeComponent;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4231/3	tom	VBM:2004042704 Fixedup the 2004032606 change

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 31-Dec-03	2321/1	pcameron	VBM:2003121807 Rework issues and enhanced validation for container name

 22-Dec-03	2273/1	byron	VBM:2003121807 Need to make policy source settable by the user

 19-Dec-03	2237/1	byron	VBM:2003112804 Provide an MCS project builder

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 16-Nov-03	1909/1	allan	VBM:2003102405 Done Image Component Wizard.

 30-Oct-03	1639/5	byron	VBM:2003101602 Create a MCS Project properties page - addressed requirement change and other minor issues

 30-Oct-03	1639/3	byron	VBM:2003101602 Create a MCS Project properties page - addressed various review issues

 30-Oct-03	1639/1	byron	VBM:2003101602 Create a MCS Project properties page

 ===========================================================================
*/
