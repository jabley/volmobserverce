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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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

    private PolicyWizardPage[] policyPages;

    protected String policyTypeName;

    // Cache of the newly created policy
    private IFile newPolicy;

    /**
     * Factory for policy file accessors.
     */
    private static final PolicyFileAccessorFactory POLICY_FILE_ACCESSOR_FACTORY =
            PolicyFileAccessorFactory.getDefaultInstance();

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

        return canFinish;
    }

    /**
     * Selects the next page in the wizard.
     *
     * <p>If the current page was capable of causing a change in the project,
     * pass this information on to all relevant pages.</p>
     *
     * @param currentPage The current page
     * @return The next page
     */
    public IWizardPage getNextPage(IWizardPage currentPage) {
        IWizardPage nextPage = super.getNextPage(currentPage);
        if (currentPage instanceof ProjectProvider) {
            projectChanged(((ProjectProvider) currentPage).getProject());
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

        policyPages = getPolicyWizardPages();
        for (int i = 0; i < policyPages.length; i++) {
            policyPages[i].setImageDescriptor(imageDescriptor);
            policyPages[i].setTitle(title);
            addPage(policyPages[i]);
        }
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
