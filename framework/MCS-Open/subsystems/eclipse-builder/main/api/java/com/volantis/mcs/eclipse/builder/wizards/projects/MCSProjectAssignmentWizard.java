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
package com.volantis.mcs.eclipse.builder.wizards.projects;

import com.volantis.mcs.eclipse.builder.properties.CollaborativeWorkingComponent;
import com.volantis.mcs.eclipse.builder.wizards.WizardMessages;
import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import java.io.File;


/**
 * Wizard to assign an MCSProjectNature to an existing project
 */
public class MCSProjectAssignmentWizard
        extends Wizard
        implements INewWizard {

    /**
     * The repository wizard page.
     */
    private MCSProjectPropertiesPage projectPropertiesPage;

    /**
     *  Store the newly-created project.
     */
    private IProject mcsProject;

    /**
     * The resource prefix.
     */
    private static final String RESOURCE_PREFIX = "MCSProjectAssignmentWizard.";


    /**
     * The descriptor for the image to be displayed on the top right of each
     * wizard page.
     */
    private ImageDescriptor imageDescriptor = null;

    /**
     *
     */
    public MCSProjectAssignmentWizard(IProject project) {
        super();
        mcsProject = project;
    }

    /**
     * Add the page(s) to the wizard; the re-used page <code>WizardNewProjectCreationPage</code>
     * and the <code>MCSProjectPropertiesPage</code>.
     *
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages() {

        setWindowTitle(getResource("windowTitle"));

        // Create and add only page.
        projectPropertiesPage = new MCSProjectPropertiesPage("deviceRepository");
        projectPropertiesPage.setImageDescriptor(imageDescriptor);
        addPage(projectPropertiesPage);
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
                "icons/full/wizban/mcs_project_banner.gif");
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
        return updateProject();
    }

    /**
     * Initialise the MCS specific properties of the project being created.
     * Specifically these properties are the MCS nature, the policy source
     * directory and the device repository location. The latter two properties
     * cannot be created unless the project folder physically exists. If the
     * MCS nature is added to the project prior to running the project
     * creation process then a build may be attempted which would fail because
     * the policy source is not available at that time.
     */
    private boolean updateProject() {
        boolean success = true;

        try {
            // Save the device repository name
            MCSProjectNature.setDeviceRepositoryName(mcsProject,
                    projectPropertiesPage.getRepositoryFilename());

            // Save the default policy source directory.
            String policySrc =
                    projectPropertiesPage.getPolicySourceDirectory();
            MCSProjectNature.setPolicySourcePath(mcsProject, policySrc);

            final CollaborativeWorkingComponent collaborativeWorkingComponent =
                projectPropertiesPage.getCollaborativeWorkingComponent();
            collaborativeWorkingComponent.setProject(mcsProject);
            collaborativeWorkingComponent.saveCollaborativeWorking();

            // Create the directory (if it doesn't exist already).
            String policySrcFile = mcsProject.getLocation().
                    toOSString() + File.separatorChar + policySrc;
            final File file = new File(policySrcFile);
            if (!file.exists()) {
                file.mkdirs();
                // Refresh the newProject so that the folder appears
                // in the Eclipse workspace without having to manually
                // refresh.
                mcsProject.refreshLocal(IResource.DEPTH_INFINITE,
                        null);
            }

            IProjectDescription description = mcsProject.getDescription();
            String[] natures = description.getNatureIds();
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = BuilderPlugin.NATURE_ID;
            description.setNatureIds(newNatures);
            mcsProject.setDescription(description, null);
        } catch (CoreException e) {
            success = false;
        }
        return success;
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

 18-May-04	4231/4	tom	VBM:2004042704 rework for 2004042704

 17-May-04	4231/2	tom	VBM:2004042704 Fixedup the 2004032606 change

 15-Apr-04	3881/1	steve	VBM:2004032606 Allow assignment of MCS nature to imported projects

 ===========================================================================
*/
