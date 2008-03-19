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
package com.volantis.mcs.eclipse.builder.wizards.policies;

import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.wizards.WizardMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.ProjectProvider;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.eclipse.validation.PolicyNameValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.utilities.FaultTypes;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.internal.ide.misc.ResourceAndContainerGroup;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Wizard page for creating a new policy of a predetermined type.
 */
public class NewPolicyPage extends WizardPage implements PolicyWizardPage, Listener, ProjectProvider {
    private static final String RESOURCE_PREFIX = "NewPolicyPage.";
    private Button lockForEditing;

    /**
     * @param policyTypeNameAttribute The localized name of the type of the
     * name attribute of the policy being created (e.g. Image Component).
     * @param policyExtension The policy extension.
     * @param selection The current resource selection
     */
    public NewPolicyPage(String policyTypeNameAttribute,
                         String policyExtension,
                         IStructuredSelection selection) {
        super("NewPolicyPage");

        String nameLabelMessage = WizardMessages.getResourceBundle().
                getString("NewPolicyPage.nameLabel");

        String[] args = {
            policyTypeNameAttribute
        };
        String policyNameLabel = MessageFormat.format(nameLabelMessage, args);

        setPageComplete(false);
        this.policyExtension = policyExtension;
        this.policyTypeNameAttribute = policyTypeNameAttribute;
        supplementaryFormatArgs[0] = policyTypeNameAttribute;
        messageBuilder =
                new ValidationMessageBuilder(WizardMessages.
                getResourceBundle(), MESSAGE_KEY_MAPPINGS,
                        supplementaryFormatArgs);
        this.policyNameLabel = policyNameLabel;
        this.currentSelection = selection;
    }

    /**
     * Copied verbatim from WizardNewFileCreationPage.
     */
    private static final int SIZING_CONTAINER_GROUP_HEIGHT = SWT.DEFAULT;

    /**
     * Mapping between fault types understood by this page and
     * message keys in the Wizards properties.
     */
    private static final HashMap MESSAGE_KEY_MAPPINGS;

    /**
     * The PolicyNameValidator for this class.
     */
    private static final PolicyNameValidator VALIDATOR;

    static {
        // Initialize validator
        char[] invalidChars = {'.', '/'};
        VALIDATOR = new PolicyNameValidator(invalidChars, null, true);

        // Initialize message key mappings
        MESSAGE_KEY_MAPPINGS = new HashMap();
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_CHARACTER,
                RESOURCE_PREFIX + "invalidNameCharacter");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_FIRST_CHARACTER,
                RESOURCE_PREFIX + "invalidFirstCharacter");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.TOO_MANY_CHARACTERS,
                RESOURCE_PREFIX + "nameTooLong");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.CANNOT_BE_NULL,
                RESOURCE_PREFIX + "emptyName");
    }

    /**
     * The current resource selection.
     */
    private IStructuredSelection currentSelection;

    /**
     * The localized name of the type of the name attributes of the policy
     * being created.
     */
    private String policyTypeNameAttribute;

    /**
     * Supplementary message format args for use when building error messages.
     */
    private Object supplementaryFormatArgs [] = new Object[1];

    /**
     * The file extension for the kind of policy created with this page.
     */
    private String policyExtension;

    /**
     * The ValidationMessageBuilder for this page.
     */
    ValidationMessageBuilder messageBuilder;

    /**
     * The localized text for the label of the name field.
     */
    private String policyNameLabel;

    // widgets
    private ResourceAndContainerGroup resourceGroup;

    // initial value stores
    private String initialPolicyName;
    private IPath initialContainerFullPath;

    /**
     * Get the file extension for policies created using this page.
     * @return the policy extension.
     */
    public String getPolicyExtension() {
        return policyExtension;
    }

    public void performFinish(List policies) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * todo later this inner class may need to be 'Volantisized'.
     */
    private class WebContentResourceAndContainerGroup
            extends ResourceAndContainerGroup {

        private String message = null;
        private int problemType = PROBLEM_NONE;

        // javadoc inherited.
        public WebContentResourceAndContainerGroup(Composite parent,
                                                   Listener client,
                                                   String resourceFieldLabel,
                                                   String resourceType,
                                                   boolean showClosedProjects,
                                                   int heightHint) {
            super(parent, client, resourceFieldLabel, resourceType,
                    showClosedProjects, heightHint);
        }

        // javadoc inherited.
        protected boolean validateContainer() {
            message = null;
            problemType = PROBLEM_NONE;
            boolean result = super.validateContainer();
            if (result) {
                IPath policySourcePath = getPolicySourcePath();
                if (policySourcePath != null) {
                    String policySource = policySourcePath.toOSString();

                    int index = super.getContainerFullPath().toOSString().
                            indexOf(policySource);
                    if (index < 0) {
                        Object formatArgs[] = new Object[]{policySourcePath};
                        String pattern = WizardMessages.getResourceBundle().
                                getString(RESOURCE_PREFIX + "invalidDirectory");
                        message = new MessageFormat(pattern).format(formatArgs);
                        problemType = PROBLEM_PATH_INVALID;
                        result = false;
                    }
                }
            }
            return result;
        }

        // javadoc inherited.
        public String getProblemMessage() {
            if (message != null) {
                return message;
            }
            return super.getProblemMessage();
        }

        // javadoc inherited.
        public int getProblemType() {
            if (problemType != PROBLEM_NONE) {
                return problemType;
            }
            return super.getProblemType();
        }

        // javadoc inherited.
        public boolean areAllValuesValid() {
            if (problemType == PROBLEM_NONE) {
                return super.areAllValuesValid();
            }
            return false;
        }

        // javadoc inherited
        public void handleEvent(Event e) {
        	super.handleEvent(e);
        	final IPath containerPath = getContainerFullPath();
        	if(containerPath != null){
        		final String projectName = containerPath.segment(0);
        		final IProject project =
        			ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        		if (project != null && lockForEditing != null) {
        			lockForEditing.setEnabled(
        					MCSProjectNature.getCollaborativeWorking(project));
        		}
        	}
        }
    }

    // javadoc inherited
    public void createControl(Composite parent) {
        initializeDialogUnits(parent);
        // top level group
        Composite topLevel = new Composite(parent, SWT.NONE);
        topLevel.setLayout(new GridLayout());
        topLevel.setLayoutData(new GridData(
                GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
        topLevel.setFont(parent.getFont());

        // Context sensitive help not yet implemented. When it is
        // uncomment the next line and update the second argument.
        // WorkbenchHelp.setHelp(topLevel, IHelpContextIds.NEW_FILE_WIZARD_PAGE);

        // resource and container group
        resourceGroup = new WebContentResourceAndContainerGroup(topLevel, this,
                policyNameLabel, policyTypeNameAttribute, false,
                SIZING_CONTAINER_GROUP_HEIGHT);

        resourceGroup.setAllowExistingResources(false);

        initialPopulateContainerNameField();
        if (initialPolicyName != null) {
            resourceGroup.setResource(initialPolicyName);
        }

        final IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        boolean foundCollaborativeProject = false;
        for (int i = 0; i < projects.length && !foundCollaborativeProject; i++) {
            final IProject project = projects[i];
            if (project.isOpen() &&
                MCSProjectNature.getCollaborativeWorking(project)) {
                foundCollaborativeProject = true;
            }
        }

        if (foundCollaborativeProject) {
            lockForEditing = new Button(topLevel, SWT.CHECK);
            lockForEditing.setText(WizardMessages.getResourceBundle().
                    getString("NewPolicyPage.lockForEditing"));
            final GridData lockForEditingGridData =
                new GridData(GridData.FILL_HORIZONTAL);
            lockForEditingGridData.horizontalSpan = 2;
            lockForEditing.setLayoutData(lockForEditingGridData);
            final IProject project = getProject();
            if (project != null) {
                lockForEditing.setEnabled(
                    MCSProjectNature.getCollaborativeWorking(project));
            }
        } else {
            lockForEditing = null;
        }

        validatePage();
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(topLevel);
    }

    public void handleEvent(Event event) {
        setPageComplete(validatePage());
    }


    /**
     * Returns the current policy name as entered by the user, or its
     * anticipated initial value.
     *
     * @return the policy name, its anticipated initial value, or
     * <code>null</code> if no policy name is known.
     */
    public String getPolicyName() {
        return resourceGroup == null ? initialPolicyName :
                resourceGroup.getResource();
    }


    /**
     * Sets the value of this page's policy name field, or stores
     * it for future use if this page's controls do not exist yet.
     *
     * @param value new policy name
     */
    public void setPolicyName(String value) {
        if (resourceGroup == null) {
            initialPolicyName = value;
        } else {
            resourceGroup.setResource(value);
        }
    }


    /**
     * Returns the current container full path as entered by the user, or its
     * anticipated initial value.
     *
     * @return The container full path, its anticipated initial value, or
     * <code>null</code> if no policy name is known.
     */
    public IPath getContainerFullPath() {
        return resourceGroup == null ? initialContainerFullPath :
                resourceGroup.getContainerFullPath();
    }


    /**
     * Sets the value of this page's container path field, or stores
     * it for future use if this page's controls do not exist yet.
     *
     * @param path The container path.
     */
    public void setContainerFullPath(IPath path) {
        if (resourceGroup == null) {
            initialContainerFullPath = path;
        } else {
            resourceGroup.setContainerFullPath(path);
        }
    }

    /**
     * Get the policy source path if it is available.
     *
     * @return The policy source path or null if the project does not have an
     * MCSProjectNature.
     */
    protected IPath getPolicySourcePath() {
        IPath policySourcePath = null;
        IProject project = getProject();
        try {
            if (project.hasNature(MCSProjectNature.NATURE_ID)) {
                policySourcePath =
                        MCSProjectNature.getMCSProjectNature(getProject()).
                        getPolicySourcePath();
            }
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
        }

        return policySourcePath;
    }

    /**
     * Return the project from the current selection or null if it cannot be
     * obtained.
     *
     * @return the project from the current selection or null if it cannot be
     *         obtained.
     */
    public IProject getProject() {
        IPath containerPath = getContainerFullPath();
        if(containerPath == null){
        	return null;
        }
        String projectName = containerPath.segment(0);
        IProject project = ResourcesPlugin.getWorkspace().getRoot().
                getProject(projectName);

        return project;
    }

    // Javadoc inherited
    public boolean canFlipToNextPage() {
        return validatePage();
    }

    // javadoc inherited
    public boolean isPageComplete() {
        return validatePage();
    }

    /**
     * Returns whether this page's controls currently all contain valid values.
     *
     * Check if the current policy name entered by the user already exists in
     * the folder that the user wants to put the new policy in.
     *
     * @return <code>true</code> if all controls are valid, and
     *         <code>false</code> if at least one is invalid
     */
    protected boolean validatePage() {
        boolean valid = true;

        if (!resourceGroup.areAllValuesValid()) {
            setErrorMessage(resourceGroup.getProblemMessage());
            valid = false;
        } else {
            ValidationStatus status =
                    VALIDATOR.validate(getPolicyName(), messageBuilder);

            if (status.getSeverity() == Status.ERROR) {
                setErrorMessage(status.getMessage());
                valid = false;
            } else if (status.getSeverity() == Status.INFO) {
                setErrorMessage(null);
                setMessage(status.getMessage());
                valid = false;
            } else {
                // Attempt to reduce garbage by using a StringBuffer and storing
                // the result of  the toString in a variable. Also the check for
                // policy existence is performed after other validation succeeds
                // which potentially reduces calls to this section of code.
                StringBuffer fullPolicyName = new StringBuffer(getPolicyName());
                fullPolicyName.append('.').append(getPolicyExtension());
                String policyName = fullPolicyName.toString();

                if (policyExists(policyName)) {
                    String args [] = {policyName};
                    MessageFormat format = new MessageFormat(
                            WizardMessages.getResourceBundle().getString(
                                    RESOURCE_PREFIX + "duplicatePolicy"));
                    setErrorMessage(format.format(args));
                    setMessage(null);
                    valid = false;
                } else {
                    setErrorMessage(null);
                    setMessage(null);
                }
            }
        }
        return valid;
    }

    /**
     * Return true if the policy with the specified name exists in this project,
     * false otherwise.
     *
     * @param fullPolicyName the full policy name which includes the extension.
     *                       For example, 'imagename.vic'
     * @return true if the policy with the specified name exists in this
     *         project, false otherwise.
     */
    private boolean policyExists(String fullPolicyName) {
        StringBuffer fullPolicyPathName = new StringBuffer(
                getProject().getLocation().toOSString());
        String policySourcePath =
            resourceGroup.getContainerFullPath().toString();
        // remove the "/project-name/" part
        policySourcePath =
            policySourcePath.substring(getProject().getName().length() + 2);
        fullPolicyPathName.append(File.separatorChar).append(policySourcePath).
                append(File.separatorChar).append(fullPolicyName);

        return new File(fullPolicyPathName.toString()).exists();
    }

    /**
     * Sets the initial contents of the container name entry field, based upon
     * either a previously-specified initial value or the ability to determine
     * such a value.
     */
    protected void initialPopulateContainerNameField() {
        if (initialContainerFullPath != null) {
            resourceGroup.setContainerFullPath(initialContainerFullPath);
        } else {
            Iterator it = currentSelection.iterator();
            if (it.hasNext()) {
                Object object = it.next();
                IResource selectedResource = null;
                if (object instanceof IResource) {
                    selectedResource = (IResource) object;
                } else if (object instanceof IAdaptable) {
                    selectedResource = (IResource) ((IAdaptable) object).
                            getAdapter(IResource.class);
                }
                if (selectedResource != null) {
                    if (selectedResource.getType() == IResource.FILE) {
                        selectedResource = selectedResource.getParent();
                    }
                    if (selectedResource.isAccessible()) {
                        resourceGroup.setContainerFullPath(selectedResource.
                                getFullPath());
                    }
                }
            }
        }
    }

    public boolean isInitiallyLocked() {
        boolean result = false;
        if (lockForEditing != null) {
            result = lockForEditing.getSelection();
        }
        return result;
    }
}
