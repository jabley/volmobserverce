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
package com.volantis.mcs.eclipse.ab.wizards;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.eclipse.common.ProjectProvider;
import com.volantis.mcs.eclipse.validation.PolicyNameValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
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
import org.eclipse.ui.internal.ide.misc.ResourceAndContainerGroup;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A WizardPage that creates policy that has no content.
 *
 * Much of the code for this wizard is derived from WizardNewFileCreationPage.
 * Unfortunately WizardNewFileCreationPage is not flexible enough to be
 * either used, specialized or delegated to for our purposes.
 */
public class GenericPolicyCreationPage extends WizardPage
        implements Listener, ProjectProvider {

    /**
     * The prefix for the name of this page.
     */
    private static final String PAGE_NAME_PREFIX =
            "GenericPolicyCreationPage: ";

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
                "GenericPolicyCreationPage.invalidNameCharacter");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_FIRST_CHARACTER,
                "GenericPolicyCreationPage.invalidFirstCharacter");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.TOO_MANY_CHARACTERS,
                "GenericPolicyCreationPage.nameTooLong");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.CANNOT_BE_NULL,
                "GenericPolicyCreationPage.emptyName");
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
     * Creates a new generic policy creation wizard page. If the initial
     * resource selection contains exactly one container resource then it will
     * be used as the default container resource.
     *
     * @param policyTypeNameAttribute The localized name of the type of the
     * name attribute of the policy being created (e.g. Image Component).
     * @param policyExtension The policy extension.
     * @param selection The current resource selection
     */
    public GenericPolicyCreationPage(String policyTypeNameAttribute,
                                     String policyExtension,
                                     IStructuredSelection selection) {
        super(PAGE_NAME_PREFIX + policyTypeNameAttribute);

        String nameLabelMessage = WizardMessages.getResourceBundle().
                getString("GenericPolicyCreationPage.nameLabel");

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
     * Get the file extension for policies created using this page.
     * @return the policy extension.
     */
    public String getPolicyExtension() {
        return policyExtension;
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
                                getString("GenericPolicyCreationPage.invalidDirectory");
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
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
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
        String projectName = containerPath.segment(0);
        IProject project = ResourcesPlugin.getWorkspace().getRoot().
                getProject(projectName);

        return project;
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
                                    "GenericPolicyCreationPage.duplicatePolicy"));
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
        fullPolicyPathName.append(File.separatorChar).append(getPolicySourcePath()).
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
            Iterator resourceEenum = currentSelection.iterator();
            if (resourceEenum.hasNext()) {
                Object object = resourceEenum.next();
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
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Nov-04	6221/1	allan	VBM:2004101907 Ensure validation is done in tab order.

 16-Nov-04	6213/3	adrianj	VBM:2004101908 Changed name label in new policy wizards (rework)

 16-Nov-04	6213/1	adrianj	VBM:2004101908 Changed name label in new policy wizards

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 17-May-04	4231/1	tom	VBM:2004042704 Fixedup the 2004032606 change

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 13-Feb-04	2985/2	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 19-Dec-03	2237/1	byron	VBM:2003112804 Provide an MCS project builder

 21-Nov-03	1980/1	byron	VBM:2003111901 Duplicate validation required on policy creation

 18-Nov-03	1878/7	richardc	VBM:2003110901 Refactor and use IFolder for session stuff

 14-Nov-03	1878/2	richardc	VBM:2003110901 VBM 2003110901

 16-Nov-03	1909/3	allan	VBM:2003102405 Done Image Component Wizard.

 31-Oct-03	1642/4	byron	VBM:2003102409 Add support for webcontent directory - addressed some rework issues

 31-Oct-03	1642/2	byron	VBM:2003102409 Add support for webcontent directory

 20-Oct-03	1502/2	allan	VBM:2003092202 Completed validation for PolicySelector.

 10-Oct-03	1512/3	allan	VBM:2003100702 Generic policy wizard with first wizard page

 ===========================================================================
*/
