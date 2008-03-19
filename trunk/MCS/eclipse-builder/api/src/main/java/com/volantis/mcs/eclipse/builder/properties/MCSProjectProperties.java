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

package com.volantis.mcs.eclipse.builder.properties;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.controls.FileSelector;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.eclipse.validation.DeviceRepositoryFileValidator;
import com.volantis.mcs.eclipse.validation.ProjectContainerNameValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.objects.FileExtension;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.jface.dialogs.DialogPage;

import java.io.File;
import java.text.MessageFormat;


/**
 * Allows the user to modify MCS project properties such as the device
 * repository location.
 */
public class MCSProjectProperties
        extends PropertyPage
        implements IWorkbenchPropertyPage, Listener {

    /**
     * Error message for reporting a failure to create the policy directory
     */
    private static final String UNABLE_TO_CREATE_MSG = EclipseCommonMessages.
            getString("MCSProjectProperties.messages." + //$NON-NLS-1$
            "unableToCreate"); //$NON-NLS-1$

    /**
     * The repository name validator for this class.
     */
    private static final DeviceRepositoryFileValidator
            REPOSITORY_NAME_VALIDATOR =
            new DeviceRepositoryFileValidator();

    /**
     * The file/folder name validator for this class.
     */
    private static ProjectContainerNameValidator
            PROJECT_CONTAINER_NAME_VALIDATOR;

    /**
     * Store the validation message builder.
     */
    private static final ValidationMessageBuilder messageBuilder =
            new ValidationMessageBuilder(null, null, null);


    /**
     * The file selector widget control.
     */
    private FileSelector fileSelector;

    /**
     * The policy source text control.
     */
    private Text policySource;
    private CollaborativeWorkingComponent collaborativeComposite;

    /**
     * Creates the user interface for the property page and set the default
     * values for the controls.
     */
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));

        Label standardLabel = new Label(composite, SWT.NONE);
        standardLabel.setText(getCommonResource("standard.label")); //$NON-NLS-1$

        fileSelector = new FileSelector(composite, SWT.NONE);
        final String[] filterSelections = new String[]{
            "*." + FileExtension.DEVICE_REPOSITORY //$NON-NLS-1$
        };
        fileSelector.getText().addListener(SWT.Modify, this);
        fileSelector.setFilterSelections(filterSelections);

        final IProject project = ((IResource) getElement()).getProject();
        final boolean collaborativeProject =
            MCSProjectNature.getCollaborativeWorking(project);

        String label = EclipseCommonMessages.getString(
                "MCSProjectProperties.policySourceDirectory"); //$NON-NLS-1$
        Label policySrcLabel = new Label(composite, SWT.NONE);
        policySrcLabel.setText(label);

        policySource = new Text(composite, SWT.BORDER | SWT.SINGLE);
        GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
        textGridData.widthHint = EclipseCommonMessages.getInteger(
                "MCSProjectProperties.Text.textWidth").intValue(); //$NON-NLS-1$
        policySource.setLayoutData(textGridData);
        policySource.addListener(SWT.Modify, this);
        policySource.setEnabled(!collaborativeProject);

        if (collaborativeProject) {
            collaborativeComposite =
                new CollaborativeWorkingComponent(project, composite, SWT.NONE);
            final GridData collaborativeCompositeGridData =
                new GridData(GridData.FILL_HORIZONTAL);
            collaborativeCompositeGridData.horizontalSpan = 2;
            collaborativeComposite.setLayoutData(collaborativeCompositeGridData);
            collaborativeComposite.setEnabled(false);
        } else {
            collaborativeComposite = null;
        }

        IResource resource = (IResource) getElement();
        PROJECT_CONTAINER_NAME_VALIDATOR =
                new ProjectContainerNameValidator(resource.getProject());
        performDefaults();

        return composite;
    }

    /**
     * Saves a persistent property for the current resource. This method
     * is triggered when either the Apply or Ok push button has been selected.
     */
    public boolean performOk() {
        String errorMessage = null;
        IResource resource = (IResource) getElement();
        try {
            String policyDir = policySource.getText().trim();
            IProject project = resource.getProject();
            File file = new File(project.getLocation().toOSString() +
                    File.separatorChar + policyDir);
            if (!file.exists()) {
                // File does not yet exist, so attempt to create it
                if (!file.mkdirs()) {
                    Object formatArgs [] = new Object[]{file.getName()};
                    MessageFormat mf = new MessageFormat(UNABLE_TO_CREATE_MSG);
                    errorMessage = mf.format(formatArgs);
                }
                project.refreshLocal(IResource.DEPTH_INFINITE, null);
            }
            if (errorMessage == null) {
                //Only set properties if no errors
                MCSProjectNature.setDeviceRepositoryName(project,
                        fileSelector.getValue());

                MCSProjectNature.setPolicySourcePath(project, policyDir);
            }
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
        }

        // This also clears the message if null.
        setErrorMessage(errorMessage);
        return errorMessage == null;
    }

    // javadoc inherited
    protected void performDefaults() {
        IResource resource = (IResource) getElement();
        final IProject project = resource.getProject();
        fileSelector.setValue(MCSProjectNature.getDeviceRepositoryName(project));
        policySource.setText(MCSProjectNature.getPolicySourcePath(project));
        if (MCSProjectNature.getCollaborativeWorking(project)) {
            collaborativeComposite.loadCollaborativeWorking();
            collaborativeComposite.setEnabled(false);
        }
    }

    // javadoc inherited
    public void handleEvent(Event event) {
        setValid(validatePage());
    }

    /**
     * Return the common resource string for the specified name.
     *
     * @param name the name to use to locate the resource.
     * @return the common resource string for the specified name.
     */
    private String getCommonResource(String name) {
        return EclipseCommonMessages.getString(
                EclipseCommonMessages.DEVICE_REPOSITORY_RESOURCE_PREFIX + name);
    }

    /**
     * Returns whether this page's controls currently all contain valid
     * values.
     *
     * @return <code>true</code> if all controls are valid, and
     *   <code>false</code> if at least one is invalid
     * @todo If this logic changes, you must also change the
     * validatePage method in MCSProjectPropertiesPage.
     */
    protected boolean validatePage() {
        boolean valid = true;

        ValidationStatus status = REPOSITORY_NAME_VALIDATOR.validate(
                fileSelector.getValue(), messageBuilder);

        if (status.getSeverity() == Status.ERROR) {
            setErrorMessage(status.getMessage());
            valid = false;
        } else if (status.getSeverity() == Status.INFO) {
            setErrorMessage(null);
            setMessage(status.getMessage());
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
        }
        return valid;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 08-Sep-05	9467/1	rgreenall	VBM:2005082306 mergevbm from 330: Enhanced validation for reserved filename characters in Windows.

 08-Sep-05	9459/1	rgreenall	VBM:2005082306 Enhanced validation for reserved filename characters in Windows.

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4231/2	tom	VBM:2004042704 Fixedup the 2004032606 change

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 31-Dec-03	2321/4	pcameron	VBM:2003121807 A few tweaks

 31-Dec-03	2321/2	pcameron	VBM:2003121807 Rework issues and enhanced validation for container name

 29-Dec-03	2258/3	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 22-Dec-03	2273/2	byron	VBM:2003121807 Need to make policy source settable by the user

 19-Dec-03	2237/1	byron	VBM:2003112804 Provide an MCS project builder

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 16-Nov-03	1909/1	allan	VBM:2003102405 Done Image Component Wizard.

 30-Oct-03	1639/5	byron	VBM:2003101602 Create a MCS Project properties page - addressed requirement change and other minor issues

 30-Oct-03	1639/3	byron	VBM:2003101602 Create a MCS Project properties page - addressed various review issues

 30-Oct-03	1639/1	byron	VBM:2003101602 Create a MCS Project properties page

 ===========================================================================
*/
