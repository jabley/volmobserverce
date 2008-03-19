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

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.ProjectProvider;
import com.volantis.mcs.eclipse.validation.PolicyNameValidator;
import com.volantis.mcs.eclipse.controls.ControlsPlugin;
import com.volantis.mcs.eclipse.controls.SelectionDialogDetails;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * The dialog control for selecting policy(s).
 */
public class PolicyTreeSelectionDialog extends ElementTreeSelectionDialog {

    /**
     * The PolicyNameValidator for this class.
     */
    private final PolicyNameValidator policyNameValidator;

    /**
     * The qualified name of the entity which is to be selected (derived from
     * constructor parameters)
     */
    public final QualifiedName qualifiedEntityId;

    /**
     * The ProjectProvider used to provide the project from which the policy
     * selection is made.
     */
    private final ProjectProvider projectProvider;

    /**
     * The error status for invalid selections.
     */
    private static final IStatus INVALID_SELECTION =
            new Status(Status.ERROR,
                    ControlsPlugin.PLUGIN_ID, Status.ERROR,
                    "", null); //$NON-NLS-1$

    /**
     * The ok status for valid selection.
     */
    private static final IStatus VALID_SELECTION =
            new Status(Status.OK,
                    ControlsPlugin.PLUGIN_ID, Status.OK,
                    "", null); //$NON-NLS-1$

    /**
     * Construct a new PolicyTreeSelectionDialog.  If the specified project is
     * null then the dialog will show all appropriate policies in all projects
     * in the workspace.  In such cases a policy reference cannot be retrieved
     * using the method {@link #getPolicyReference}
     *
     * @param parentShell The shell for the dialog.
     * @param selectionDialogDetails The details with which to configure the
     * dialog.
     * @param qualifiedEntityId The QualifiedName used to find the previous
     * selection for this dialog.
     * @param project The project from which the selection is available.
     * @param policyNameValidator The validator with which to validate this
     * dialog.
     */
    public PolicyTreeSelectionDialog(
            Shell parentShell,
            SelectionDialogDetails selectionDialogDetails,
            QualifiedName qualifiedEntityId,
            final IProject project,
            PolicyNameValidator policyNameValidator) {
        this(parentShell, selectionDialogDetails, qualifiedEntityId,
                project == null ? null :
                new ProjectProvider() {
                    public IProject getProject() {
                        return project;
                    }
                },
                policyNameValidator);
    }

    /**
     * Construct a new PolicyTreeSelectionDialog.  If the specified
     * ProjectProvider is null then the dialog will show all appropriate
     * policies in all projects in the workspace.  In such cases a policy
     * reference cannot be retrieved using the method
     * {@link #getPolicyReference}
     *
     * @param parentShell The shell for the dialog.
     * @param selectionDialogDetails The details with which to configure the
     * dialog.
     * @param qualifiedEntityId The QualifiedName used to find the previous
     * selection for this dialog.
     * @param projectProvider The ProjectProvider used to obtain the project
     * from which the selection is available.
     * @param policyNameValidator The validator with which to validate this
     * dialog.
     */
    public PolicyTreeSelectionDialog(Shell parentShell,
                                     SelectionDialogDetails selectionDialogDetails,
                                     QualifiedName qualifiedEntityId,
                                     ProjectProvider projectProvider,
                                     PolicyNameValidator policyNameValidator) {

        super(parentShell, new WorkbenchLabelProvider(),
                new WorkbenchContentProvider());

        // Set the instance entities
        this.policyNameValidator = policyNameValidator;
        this.qualifiedEntityId = qualifiedEntityId;

        // Control the properties of the dialog
        setAllowMultiple(false);
        setMessage(selectionDialogDetails.dialogMessage);
        setTitle(selectionDialogDetails.dialogTitle);
        setEmptyListMessage(selectionDialogDetails.noItemsMessage);
        addFilter(createPolicyFilter());
        setValidator(createSelectionStatusValidator());
        setBlockOnOpen(true);

        this.projectProvider = projectProvider;
    }

    /**
     * Setup the selection dialog for choosing a policy of the right type
     * and return the result of calling the open method on this dialog.
     * <p> If a Project, or ProjectProvider, was passed in on construction then
     * the (hidden) root of the selection tree will be the project.  Otherwise
     * it will be the current workspace, allowing selection from policies in
     * all open projects.
     * @return ElementTreeSelectionDialog.OK if the user selects the OK
     * button and ElementTreeSelectionDialog.CANCEL if the user selects
     * cancel or closes the dialog window.
     */
    public int doSelectionDialog() {

        // Populate the initial selection
        Object selection = null;
        try {

            // Get the policy source path
            if (projectProvider != null) {
                IProject project = projectProvider.getProject();
                IPath policySourcePath = MCSProjectNature.
                        getMCSProjectNature(project).getPolicySourcePath();
                setInput(project.getFolder(policySourcePath));
            } else {
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                setInput(root);
            }

            selection = ResourcesPlugin.getWorkspace().
                    getRoot().getSessionProperty(this.qualifiedEntityId);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
        if (selection != null) {
            this.setInitialSelection(selection);
        }

        // Open the dialog
        final int returnCode = this.open();

        // If the dialog has something to return, put it in the session
        if (returnCode == ElementTreeSelectionDialog.OK) {
            try {
                ResourcesPlugin.getWorkspace().getRoot().setSessionProperty(
                        this.qualifiedEntityId,
                        selection);
            } catch (CoreException e) {
                EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
            }
        }
        return returnCode;
    }

    /**
     * Provide a filter that filters out files that are not selectable
     * and IContainers that have no selectable files within their hierarchy.
     * NOTE: no performance optimizations have been implemented for
     * this method though some could be and should be if this becomes an
     * issue.
     * @return A ViewFilter that filters out resources that should not
     * be visible in the selection dialog.
     */
    private ViewerFilter createPolicyFilter() {
        return new ViewerFilter() {
            public boolean select(
                    Viewer viewer,
                    Object parentElement,
                    Object element) {

                boolean select =
                        element instanceof IFile
                        && fileIsSelectable((IFile) element);

                if (!select && element instanceof IContainer) {
                    // Search the container hierarchy
                    IContainer container = (IContainer) element;
                    try {
                        IResource members[] = container.members();
                        if (members != null) {
                            for (int i = 0;
                                 i < members.length && !select;
                                 i++) {
                                select =
                                        select(viewer, parentElement, members[i]);
                            }
                        }
                    } catch (CoreException e) {
                        EclipseCommonPlugin.handleError(
                                ControlsPlugin.getDefault(),
                                e);
                    }
                }

                return select;
            }
        };
    }

    /**
     * Create a selection validator that provides a valid status only if
     * the selected item is an instance of IFile. This method relies on
     * the filter filtering out files that should not be selectable.
     *
     * The message associated with the IStatus returned from the
     * validate method of the ISelectionStatusValidator is always empty
     * as specified by the architecture.
     *
     * @return The ISelectionStatusValidator that validates the
     * selection dialog.
     */
    private ISelectionStatusValidator createSelectionStatusValidator() {
        return new ISelectionStatusValidator() {
            public IStatus validate(Object[] objects) {
                if (objects == null || objects.length == 0) {
                    return INVALID_SELECTION;
                } else if (!(objects[0] instanceof IFile)) {
                    return INVALID_SELECTION;
                } else {
                    return VALID_SELECTION;
                }
            }
        };
    }

    /**
     * Determine if a file should be selectable in the selection dialog. .
     * @param file The IFile to check.
     * @return true if the specified IFile should be selectable and
     * false otherwize.
     */
    private boolean fileIsSelectable(IFile file) {
        // Note that this returns false if the file has no extension
        return policyNameValidator.isValidExtension(file.getFullPath().
                getFileExtension());
    }


    /**
     * Create a policy reference from the selected IFile. This means using the
     * segments of the IFile full path, making them relative to the project
     * and ensuring that the separators are '/'s because the policy reference
     * is a URI.
     * @return The policy reference to the specified IFile with a leading slash (/)
     * @throws IllegalStateException if there was not a Project or
     * ProjectProvider passed in on construction.
     */
    public String getPolicyReference() {
        if (projectProvider == null) {
            throw new IllegalStateException("ProjectProvider must not be null");
        }

        IPath path = ((IFile) getFirstResult()).getFullPath().makeRelative();
        String segments[] = path.segments();

        // The 1st segment is the project. Subsequent segments for the number
        // of segments in the policy source are the policy source. All these
        // segments need to be stripped.

        // Get the policy source path
        IProject project = projectProvider.getProject();
        IPath policySourcePath =
                MCSProjectNature.getMCSProjectNature(project).
                getPolicySourcePath();

        int startIndex = policySourcePath.segmentCount() + 1;
        StringBuffer ref = new StringBuffer("/").append(segments[startIndex]);

        int i = startIndex + 1;
        while (i < segments.length) {
            //Literal forward slash is used and not File.separatorChar
            //because the reference is a URI.
            ref.append('/');
            ref.append(segments[i]);
            i++;
        }
        return ref.toString();
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

 19-Aug-04	5247/1	adrian	VBM:2004070805 Updated PolicyTreeSelectionDialog to allow use without a Project

 17-May-04	4231/2	tom	VBM:2004042704 Fixedup the 2004032606 change

 27-Feb-04	3246/1	byron	VBM:2004021205 Lack of validation for policy file extensions - fixed '/' appending

 13-Feb-04	2985/3	allan	VBM:2004012803 Fix for null project in ProjectProviders

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 19-Dec-03	2237/1	byron	VBM:2003112804 Provide an MCS project builder

 05-Dec-03	2128/5	pcameron	VBM:2003112105 Renamed FileExtension.match to matches

 05-Dec-03	2128/1	pcameron	VBM:2003112105 Added TextDefinition and refactored PolicySelector

 19-Nov-03	1878/5	richardc	VBM:2003110901 Throw IllegalStateException if no policy source path

 19-Nov-03	1878/3	richardc	VBM:2003110901 Fixed bug in setInput and 2 other minor bugs

 18-Nov-03	1878/1	richardc	VBM:2003110901 Refactor and use IFolder for session stuff

 ===========================================================================
*/
