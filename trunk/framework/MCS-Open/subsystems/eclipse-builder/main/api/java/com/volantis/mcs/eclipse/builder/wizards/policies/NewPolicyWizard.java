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
import com.volantis.mcs.eclipse.builder.common.policies.CollaborativeCreatePolicyConfiguration;
import com.volantis.mcs.eclipse.builder.common.policies.CreatePolicyConfiguration;
import com.volantis.mcs.eclipse.builder.common.policies.GenericCreatePolicyConfiguration;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessor;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessorFactory;
import com.volantis.mcs.eclipse.builder.wizards.WizardMessages;
import com.volantis.mcs.eclipse.builder.wizards.projects.MCSProjectPropertiesPage;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.ProjectProvider;
import com.volantis.mcs.eclipse.common.ProjectReceiver;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyType;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Wizard for creating new policies using the Java model and JiBX to
 * write the initial file out.
 */
public abstract class NewPolicyWizard extends Wizard implements INewWizard {
    private static final String RESOURCE_PREFIX = "NewPolicyWizard.";

    private static final PolicyFactory POLICY_FACTORY =
            PolicyFactory.getDefaultInstance();

    protected static PolicyFactory getPolicyFactory() {
        return POLICY_FACTORY;
    }

    private NewPolicyPage creationPage;

    private MCSProjectPropertiesPage projectPropertiesPage;

    private PolicyWizardPage[] policyPages;

    protected String policyTypeName;

    // Cache of the newly created policy
    private IFile newPolicy;

    /**
     * The IProject associated with the policy being created.
     */
    private IProject project;
    
    /**
     * Factory for policy file accessors.
     */
    private static final PolicyFileAccessorFactory POLICY_FILE_ACCESSOR_FACTORY =
            PolicyFileAccessorFactory.getDefaultInstance();

    private Boolean useProjectPropertiesPage = Boolean.FALSE;

    // Javadoc inherited
    public boolean performFinish() {
        PolicyBuilder policyBuilder = getPolicy();
        try {
            // Generate the path to which the policy will be written
            final IPath containerPath = creationPage.getContainerFullPath();
            IPath newPolicyPath = containerPath.append(creationPage.getPolicyName());
            newPolicyPath = newPolicyPath.
                    addFileExtension(creationPage.getPolicyExtension());

            IProject project = creationPage.getProject();

            // Add an MCSProjectNature derived from the projectPropertiesPage to the
            // project associated with this Wizard
            if (!project.hasNature(MCSProjectNature.NATURE_ID)) {
                addMCSProjectNatureToProject();
            }

            PolicyFileAccessor accessor = POLICY_FILE_ACCESSOR_FACTORY.getPolicyFileAccessor(project);

            // Create the policy
            final CreatePolicyConfiguration configuration;
            if (MCSProjectNature.getCollaborativeWorking(project)) {
                configuration = new CollaborativeCreatePolicyConfiguration(
                    null, creationPage.isInitiallyLocked());
            } else {
                configuration = new GenericCreatePolicyConfiguration(null);
            }
            newPolicy = (IFile) accessor.createPolicy(newPolicyPath,
                                policyBuilder, configuration);
            if (newPolicy == null) {
                // the policy could not be created because it already exists,
                // so use the existing one.
                newPolicy = ResourcesPlugin.getWorkspace().getRoot().
                        getFile(newPolicyPath);
            }

            // Open the policy
            IEditorRegistry editorRegistry =
                    PlatformUI.getWorkbench().getEditorRegistry();
            String editorId =
                    editorRegistry.getDefaultEditor(newPolicy.getName()).getId();
            IWorkbenchPage activePage =
                    PlatformUI.getWorkbench().
                    getActiveWorkbenchWindow().
                    getActivePage();
            activePage.openEditor(new FileEditorInput(newPolicy),
                    editorId, true);
        } catch (PolicyFileAccessException pfae) {
            EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), pfae);
        } catch (PartInitException e) {
            EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
        }

        return true;
    }

    /**
        * Add an MCSProjectNature derived from the projectPropertiesPage to the
        * project associated with this Wizard. This method relies on the
        * projectPropertiesPage to provide the information associated with
        * the MCSProjectNature e.g. the policy source directory and the location
        * of the device repository.
        */
       private void addMCSProjectNatureToProject() throws CoreException {

           // Save the device repository name
           MCSProjectNature.setDeviceRepositoryName(project,
                   projectPropertiesPage.getRepositoryFilename());

           // Save the default policy source directory.
           String policySrc =
                   projectPropertiesPage.getPolicySourceDirectory();
           MCSProjectNature.setPolicySourcePath(project, policySrc);

           // Create the nature.
           MCSProjectNature nature = new MCSProjectNature();
           nature.setProject(project);
           nature.configure();

           // Add the nature to the project.
           IProjectDescription projectDescription = project.getDescription();
           String natureIds [] = projectDescription.getNatureIds();
           // Create an ArrayList out of the asList() method because the
           // asList() method provides an immutable list.
           List natureIdList = new ArrayList(Arrays.asList(natureIds));
           natureIdList.add(MCSProjectNature.NATURE_ID);
           String newNatureIds [] = new String[natureIdList.size()];
           natureIdList.toArray(newNatureIds);
           projectDescription.setNatureIds(newNatureIds);
           project.setDescription(projectDescription, null);
       }

       /**
        * Remove the MCSProjectNature from the project associated with this page.
        */
       private void removeMCSProjectNatureFromProject() throws CoreException {
           // Unset the device repository name
           MCSProjectNature.setDeviceRepositoryName(project, null);

           // Unset the default policy source directory.
           MCSProjectNature.setPolicySourcePath(project, null);

           // Create the nature.
           MCSProjectNature nature = new MCSProjectNature();
           nature.setProject(project);
           nature.configure();

           // Add the nature to the project.
           IProjectDescription projectDescription = project.getDescription();
           String natureIds [] = projectDescription.getNatureIds();
           // Create an ArrayList out of the asList() method because the
           // asList() method provides an immutable list.
           List natureIdList = new ArrayList(Arrays.asList(natureIds));
           natureIdList.remove(MCSProjectNature.NATURE_ID);
           String newNatureIds [] = new String[natureIdList.size()];
           natureIdList.toArray(newNatureIds);
           projectDescription.setNatureIds(newNatureIds);
           project.setDescription(projectDescription, null);
       }


    protected PolicyBuilder getPolicy() {
        List policies = new ArrayList();
        PolicyBuilder policy = getPolicyBuilder();
        policies.add(policy);

        for (int i = 0; i < policyPages.length; i++) {
            policyPages[i].performFinish(policies);
        }

        // Retrieve the return value from the list, in case any pages have
        // replaced it.
        return (PolicyBuilder) policies.get(0);
    }

    // Javadoc inherited
    public boolean canFinish() {
        // For the moment we'll say that we can finish if all the policy pages
        // can finish.
        boolean canFinish = true;
        for (int i = 0; canFinish &&  i < policyPages.length; i++) {
            canFinish = policyPages[i].isPageComplete();
        }

        canFinish = canFinish && creationPage.isPageComplete();

        // in case project is not of MCS nature we have to convert/initialize it prior
        // to creating any policy
        try {
           if (!creationPage.getProject().hasNature(MCSProjectNature.NATURE_ID)) {
               canFinish = false;
           }
        } catch (CoreException e) {
           EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
        }


        return canFinish;
    }

    public boolean performCancel() {

        if (useProjectPropertiesPage == Boolean.TRUE) {
            try {
                removeMCSProjectNatureFromProject();
            } catch (CoreException e) {
                EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
            }
        }

        return true;
    }



    /**
     * Selects the next page in the wizard. Implementation handles showing
     * project properties page in case current project has non-mcs type/nature 
     *
     * <p>If the current page was capable of causing a change in the project,
     * pass this information on to all relevant pages.</p>
     *
     * @param currentPage The current page
     * @return The next page
     */
    public IWizardPage getNextPage(IWizardPage currentPage) {
        IWizardPage nextPage = super.getNextPage(currentPage);

        try {
            if (currentPage instanceof ProjectProvider) {
                project = ((ProjectProvider) currentPage).getProject();
            }

            if (nextPage == projectPropertiesPage) {
                //skip this wizard page or prompt policy source location
                if (project.hasNature(MCSProjectNature.NATURE_ID)) {
                    nextPage = super.getNextPage(nextPage);
                } else {
                    projectPropertiesPage.setPolicySource(getPolicySource());
                }
            } else if (currentPage == projectPropertiesPage) {
                addMCSProjectNatureToProject();
                useProjectPropertiesPage = Boolean.TRUE;
            }

            // change to the repository path can be done here so
            // we mark this; hack of storing this information in project session
            // is done in order to let setProject method in TargerSelector class know
            // that repository should be loaded/reloaded to populate DeviceSelectionList
            // and DeviceSelectionTree
            if (currentPage == projectPropertiesPage) {
                project.setSessionProperty(
                        new QualifiedName(null, "device.repository.refresh.required"),
                        new Boolean(true));
            }

            if (currentPage instanceof ProjectProvider || currentPage == projectPropertiesPage) {
                projectChanged(project);
            }

        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);;
        }

        return nextPage;
    }

    /**
     * Selects the previous page in the wizard.
     *
     * <p>If the current page was capable of causing a change in the project,
     * pass this information on to all relevant pages.</p>
     *
     * @param currentPage The current page
     * @return The previous page
     */
    public IWizardPage getPreviousPage(IWizardPage currentPage) {
        IWizardPage prevPage = super.getPreviousPage(currentPage);
        if (currentPage instanceof ProjectProvider) {
            projectChanged(((ProjectProvider) currentPage).getProject());
        }
        return prevPage;
    }

    /**
     * Handles a potential change in the project caused by a wizard page that
     * can modify the project. Notifies all pages that are project receivers of
     * the new project. Note that it is their responsibility to track whether
     * the project has actually changed, and to cache any data accordingly.
     *
     * @param project The new project
     */
    protected void projectChanged(IProject project) {
        for (int i = 0; i < policyPages.length; i++) {
            if (policyPages[i] instanceof ProjectReceiver) {
                ((ProjectReceiver) policyPages[i]).setProject(project);
            }
        }
    }

    // Javadoc inherited
    public void init(IWorkbench iWorkbench, IStructuredSelection iStructuredSelection) {
        policyTypeName = getPolicyTypeName();
        String imagePath = WizardMessages.getString(RESOURCE_PREFIX + policyTypeName + ".banner");
        ImageDescriptor imageDescriptor = BuilderPlugin.getImageDescriptor(imagePath);
        String title = WizardMessages.getString(RESOURCE_PREFIX + policyTypeName + ".title");

        creationPage = new NewPolicyPage("",
                FileExtension.getFileExtensionForPolicyType(
                        getPolicyType()).getExtension(),
                iStructuredSelection);
        creationPage.setImageDescriptor(imageDescriptor);
        creationPage.setTitle(title);
        addPage(creationPage);

        projectPropertiesPage =
            new MCSProjectPropertiesPage("deviceRepository");
        projectPropertiesPage.setImageDescriptor(imageDescriptor);
        addPage(projectPropertiesPage);

        policyPages = getPolicyWizardPages();
        for (int i = 0; i < policyPages.length; i++) {
            policyPages[i].setImageDescriptor(imageDescriptor);
            policyPages[i].setTitle(title);
            addPage(policyPages[i]);
        }
    }

    /**
     * Derive the policy source directory from the container full path
     * of the creationPage. The first segment of the container full path
     * is the project. The rest is the policy source.
     */
    private String getPolicySource() {
        IPath fullPath = creationPage.getContainerFullPath();
        fullPath = fullPath.removeFirstSegments(1);
        fullPath = fullPath.makeRelative();
        return fullPath.toOSString();
    }

    /**
     * Returns the type of policy created by this wizard.
     *
     * @return The type of policy created by this wizard
     */
    protected abstract PolicyType getPolicyType();

    /**
     * Returns an appropriate policy builder for the policy created by this
     * wizard.
     *
     * @return A policy builder appropriate to this wizard
     */
    protected abstract PolicyBuilder getPolicyBuilder();

    /**
     * Returns the pages related to creating the policy.
     *
     * @return An array of pages for creating the policy
     */
    protected abstract PolicyWizardPage[] getPolicyWizardPages();

    protected abstract String getPolicyTypeName();
}
