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

import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.properties.CollaborativeWorkingComponent;
import com.volantis.mcs.eclipse.builder.wizards.WizardMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import java.io.File;
import java.lang.reflect.InvocationTargetException;


/**
 * Wizard to create a new project with the nature <code>MCSProjectNature</code>.
 *
 * Processing in this wizard is modelled after the platform new project wizard.
 *
 * @see org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard
 */
public class NewMCSProject
        extends Wizard
        implements IExecutableExtension, INewWizard {

    /**
     * Reuse of the new project page provided by the platform UI dialogs
     */
    private WizardNewProjectCreationPage mainPage;

    /**
     * The repository wizard page.
     */
    private MCSProjectPropertiesPage repositoryWizardPage;

    /**
     *  Store the newly-created project.
     */
    private IProject newProject;

    /**
     * The resource prefix.
     */
    private static final String RESOURCE_PREFIX = "NewMCSProjectWizard.";

    /**
     * The descriptor for the image to be displayed on the top right of each
     * wizard page.
     */
    private ImageDescriptor imageDescriptor = null;

    /**
     * The IConfigurationElement that allows a perspective to be
     * associated with this project.
     */
    private IConfigurationElement configurationElement;

    /**
     * Add the page(s) to the wizard; the re-used page <code>WizardNewProjectCreationPage</code>
     * and the <code>MCSProjectPropertiesPage</code>.
     *
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages() {

        setWindowTitle(getResource("windowtitle")); //$NON-NLS-1$

        // Create and add Page 1.
        mainPage = new WizardNewProjectCreationPage("NewMCSProject"); //$NON-NLS-1$
        mainPage.setDescription(getResource("message")); //$NON-NLS-1$
        mainPage.setTitle(getResource("project")); //$NON-NLS-1$
        mainPage.setImageDescriptor(imageDescriptor);
        addPage(mainPage);

        // Create and add Page 2.
        repositoryWizardPage = new MCSProjectPropertiesPage("deviceRepository");
        repositoryWizardPage.setImageDescriptor(imageDescriptor);
        addPage(repositoryWizardPage);
    }

    /**
     * Returns the newly created project.
     *
     * @return the created project, or <code>null</code>
     *   if project not created
     */
    public IProject getNewProject() {
        return newProject;
    }

    public IWizardPage getNextPage(final IWizardPage currentPage) {
        final IWizardPage nextPage = super.getNextPage(currentPage);
        if (nextPage == repositoryWizardPage) {
            final String projectName = mainPage.getProjectName();
            final CollaborativeWorkingComponent collaborativeWorkingComponent =
                repositoryWizardPage.getCollaborativeWorkingComponent();
            collaborativeWorkingComponent.setProjectName(projectName);
        }
        return nextPage;
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


    /**
     * Initializes this creation wizard using the passed workbench an object selection.
     * <p>
     * This method is called after the no argument constructor and before other methods are called.
     * </p>
     *
     * @param workbench the current workbench
     * @param selection the current object selection
     *
     * @see org.eclipse.ui.IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        imageDescriptor = BuilderPlugin.getImageDescriptor(
                "icons/full/wizban/mcs_project_banner.gif"); //$NON-NLS-1$
    }

    /**
     * Creates a project with a <code>MCSProjectNature</code> nature association.
     *
     * @return <code>true</code> to indicate the finish request was accepted,
     *         and <code>false</code> to indicate that the finish request was
     *         refused
     * @see org.eclipse.jface.wizard.IWizard#performFinish()
     */
    public boolean performFinish() {
        boolean result = createNewProject() != null;
        if (result) {
            result = populateProject(newProject);
        }

        if (result) {
            BasicNewProjectResourceWizard.updatePerspective(configurationElement);
        }
        return result;
    }

    /**
     * Creates a new project resource with the name selected in the wizard page.
     * Project creation is wrapped in a <code>WorkspaceModifyOperation</code>.
     * <p>
     *
     * @see org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard#createNewProject()
     * @see org.eclipse.ui.actions.WorkspaceModifyOperation
     *
     * @return the created project resource, or <code>null</code> if the project
     *    was not created
     */
    public IProject createNewProject() {
        if (newProject == null) {
            try {
                // get a project handle
                final IProject newProjectHandle = mainPage.getProjectHandle();

                // get a project descriptor
                IPath defaultPath = Platform.getLocation();
                IPath newPath = mainPage.getLocationPath();
                if (defaultPath.equals(newPath)) {
                    newPath = null;
                }

                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                final IProjectDescription description =
                        workspace.newProjectDescription(newProjectHandle.
                        getName());
                description.setLocation(newPath);

                // create the new project operation
                WorkspaceModifyOperation op =
                        new WorkspaceModifyOperation() {
                            protected void execute(IProgressMonitor monitor)
                                    throws CoreException {
                                createProject(description, newProjectHandle,
                                        monitor);
                            }
                        };

                // run the new project creation operation
                getContainer().run(false, true, op);
                newProject = newProjectHandle;

                // Initialise the MCS properties after the project has been
                // created to ensure that a build is not attempted before
                // the MCS properties are available which are used by the
                // builder.
                initialiseMCSProperties();
            } catch (InterruptedException e) {
                EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
            } catch (InvocationTargetException e) {
                EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
            } catch (CoreException e) {
                EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
            }
        }
        return newProject;
    }

    /**
     * Populate the project with any initial content.
     *
     * @param project The project to populate
     * @return True if the project was populated successfully, false otherwise
     */
    protected boolean populateProject(final IProject project) {
        return true;
    }

    /**
     * Initialise the MCS specific properties of the project being created.
     * Specifically these properties are the MCS nature, the policy source
     * directory and the device repository location. The latter two properties
     * cannot be created unless the project folder physically exists. If the
     * MCS nature is added to the project prior to running the project
     * creation process then a build may be attempted which would fail because
     * the policy source is not available at that time.
     * @throws CoreException If there is a problem setting the policy source
     * or device location.
     */
    protected void initialiseMCSProperties()
            throws CoreException {

        // Save the device repository name
        MCSProjectNature.setDeviceRepositoryName(newProject,
                repositoryWizardPage.getRepositoryFilename());

        // Save the default policy source directory.
        String policySrc =
                repositoryWizardPage.getPolicySourceDirectory();

        MCSProjectNature.setPolicySourcePath(newProject, policySrc);


        final CollaborativeWorkingComponent collaborativeWorkingComponent =
            repositoryWizardPage.getCollaborativeWorkingComponent();
        collaborativeWorkingComponent.setProject(newProject);
        collaborativeWorkingComponent.saveCollaborativeWorking();

        // Create the directory (if it doesn't exist already).
        String policySrcFile = newProject.getLocation().
                toOSString() + File.separatorChar + policySrc;
        final File file = new File(policySrcFile);
        if (!file.exists()) {
            file.mkdirs();
            // Refresh the newProject so that the folder appears
            // in the Eclipse workspace without having to manually
            // refresh.
            newProject.refreshLocal(IResource.DEPTH_INFINITE,
                    null);
        }

        addMCSNature(newProject);
    }

    /**
     * Creates a project resource given the project handle and description.
     *
     * @param description   the project description to create a project
     *                      resource for
     * @param projectHandle the project handle to create a project resource
     *                      for
     * @param monitor       the progress monitor to show visual progress with
     * @throws CoreException              if the operation fails
     * @throws OperationCanceledException if the operation is cancelled
     */
    public void createProject(IProjectDescription description,
                              IProject projectHandle,
                              IProgressMonitor monitor)
            throws CoreException, OperationCanceledException {
        try {
            monitor.beginTask("", 2000); //$NON-NLS-1$

            projectHandle.create(description,
                    new SubProgressMonitor(monitor, 1000));

            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }

            projectHandle.open(new SubProgressMonitor(monitor, 1000));
        } finally {
            monitor.done();
        }
    }

    /**
     * Add the nature to the project.
     */
    public void addMCSNature(IProject project)
            throws CoreException {

        try {
            IProjectDescription description = project.getDescription();
            String[] natures = description.getNatureIds();
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = BuilderPlugin.NATURE_ID;
            description.setNatureIds(newNatures);
            project.setDescription(description, null);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
        }
    }

    // javadoc inherited
    public void
            setInitializationData(IConfigurationElement configurationElement,
                                  String s, Object o) throws CoreException {
        this.configurationElement = configurationElement;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Dec-04	6348/1	allan	VBM:2004113003 Add an MCS perspective

 17-May-04	4231/1	tom	VBM:2004042704 Fixedup the 2004032606 change

 27-Feb-04	3200/1	allan	VBM:2004022410 Basic Update Client Wizard.

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Jan-04	2659/1	allan	VBM:2003112801 The ThemeDesign Editor

 06-Jan-04	2414/1	byron	VBM:2003121803 Misleading package id in MCSProjectNature

 04-Jan-04	2371/1	allan	VBM:2003121727 Update mcs project icons.

 31-Dec-03	2321/1	pcameron	VBM:2003121807 Rework issues and enhanced validation for container name

 30-Dec-03	2317/1	pcameron	VBM:2003121806 Project now refreshes upon creation

 22-Dec-03	2273/1	byron	VBM:2003121807 Need to make policy source settable by the user

 19-Dec-03	2237/1	byron	VBM:2003112804 Provide an MCS project builder

 16-Nov-03	1909/1	allan	VBM:2003102405 Done Image Component Wizard.

 05-Nov-03	1803/1	byron	VBM:2003102901 Device Asset creation wizard page

 31-Oct-03	1642/1	byron	VBM:2003102409 Add support for webcontent directory - addressed some rework issues

 27-Oct-03	1618/4	byron	VBM:2003100804 Create a new project wizard for MCS projects

 09-Oct-03	1515/1	steve	VBM:2003100704 Create MCS project Nature

 ===========================================================================
*/
