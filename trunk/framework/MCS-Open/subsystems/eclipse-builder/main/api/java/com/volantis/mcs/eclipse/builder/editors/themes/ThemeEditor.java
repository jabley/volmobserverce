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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.wizards.projects.MCSProjectAssignmentWizard;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.eclipse.builder.common.InteractionFocussable;
import com.volantis.mcs.eclipse.builder.common.ResourceDiagnosticsAdapter;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.eclipse.builder.editors.common.PolicyBuilderEditor;
import com.volantis.mcs.eclipse.builder.editors.common.ResourceCloseListener;
import com.volantis.mcs.eclipse.builder.editors.common.SelectedVariantMonitor;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.ReadWriteState;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.PropertyStep;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.variants.VariantBuilder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart2;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.wizard.WizardDialog;

/**
 * The theme editor.
 */
public class ThemeEditor extends MultiPageEditorPart
        implements InteractionFocussable, PolicyBuilderEditor, IGotoMarker {
    private static final String RESOURCE_PREFIX = "ThemeEditor.";

    private static final String OVERVIEW_LABEL_KEY =
            RESOURCE_PREFIX + "overview.label";

    private static final String DESIGN_LABEL_KEY =
            RESOURCE_PREFIX + "design.label";

    /**
     * The error message for an attempt to load a missing file.
     */
    private static final String MISSING_FILE_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "loadError.deleted");

    /**
     * The error message for miscellaneous load errors.
     */
    private static final String LOAD_ERROR_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "loadError.general");

    private ThemeEditorContext context;

    private ThemeOverview overview;

    private ThemeDesign design;

    private int overviewIndex;

    private int designIndex;

    /**
     * A listener for events indicating that the associated resource has moved
     * or been deleted, or that its project has been closed or deleted. If any
     * of these situations occur, the listener will close the editor.
     */
    private ResourceCloseListener resourceCloseListener;

    /**
     * Listener for identifying and mimicing changes to the part name of the
     * pages of this multi-page editor.
     */
    private IPropertyListener partNameListener = new IPropertyListener() {
        public void propertyChanged(Object source, int property) {
            if (property == IWorkbenchPartConstants.PROP_PART_NAME) {
                setPartName(((IWorkbenchPart2) source).getPartName());
            }
        }
    };

    /**
     * Checks project nature and, if required, launches project wizard to assign mcs
     * nature to the project
     *
     * @param iEditorSite
     * @param file
     * @throws PartInitException
     */
    private void checkProjectNature(IEditorSite iEditorSite, IFile file)
            throws PartInitException {

        if (file != null) {
            boolean validDeviceRepository = false;
            // if this is an mcs project then check if the device repository is
            // valid
            IProject project = file.getProject();
            try {
                if (project.hasNature(MCSProjectNature.NATURE_ID)) {
                    MCSProjectNature projectNature;
                    projectNature = MCSProjectNature.getMCSProjectNature(project);
                    validDeviceRepository = projectNature.
                            hasValidDeviceRepository();
                }
            } catch (CoreException coreException) {
                EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), coreException);
            }
            if (validDeviceRepository == false) {
                // project does not have an MCS nature or the device repository has
                // become corrupt so launch wizard to add MCS nature to the project
                MCSProjectAssignmentWizard wizard =
                        new MCSProjectAssignmentWizard(project);
                // Instantiates the wizard container with the wizard and opens it
                Shell shell = iEditorSite.getShell();
                WizardDialog dialog = new WizardDialog(shell, wizard);
                dialog.create();
                int action = dialog.open();
                if (action == WizardDialog.CANCEL) {
                    String errorMessage = EditorMessages.getString(RESOURCE_PREFIX +
                            "projectAssignmentWizardCancelled.reason");
                    throw new PartInitException(errorMessage);
                }
            }
        }
    }

    // Javadoc inherited
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException {
        IFile file = null;
        if (input instanceof IFileEditorInput) {
            file = ((IFileEditorInput) input).getFile();
        }

        checkProjectNature(site, file);

        try {
            if (context == null) {
                context = new ThemeEditorContext(this);
            }
            context.loadResource(file);
            setSite(site);
            setInput(input);

            // Register listener to handle project/resource being closed,
            // moved or deleted
            resourceCloseListener = new ResourceCloseListener(this);
            resourceCloseListener.startListener();
        } catch (PolicyFileAccessException ce) {
            String errorMessage =
                file != null && file.exists() ? LOAD_ERROR_MESSAGE :
                    MISSING_FILE_MESSAGE;
            throw new PartInitException(errorMessage);
        }
    }

    // Javadoc inherited
    protected void createPages() {
        try {
            if (context == null) {
                context = new ThemeEditorContext(this);
            }
            overview = new ThemeOverview(context);
            design = new ThemeDesign(context);
            context.setDesignPage(design);

            overview.addPropertyListener(partNameListener);
            design.addPropertyListener(partNameListener);

            overviewIndex = addPage(overview, getEditorInput());
            setPageText(overviewIndex, EditorMessages.getString(OVERVIEW_LABEL_KEY));
            designIndex = addPage(design, getEditorInput());
            setPageText(designIndex, EditorMessages.getString(DESIGN_LABEL_KEY));

            new SelectedVariantMonitor(context) {
                // Javadoc inherited
                public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
                    boolean enableDesign = !event.isReadOnly();
                    if (enableDesign != design.isEnabled()) {
                        design.setEnabled(enableDesign);
                    }
                }

                // Javadoc inherited
                public void selectionMade(BuilderSelectionEvent event) {
                    BeanProxy selectedVariant = context.getSelectedVariant();
                    if (selectedVariant != null) {
                        boolean enableDesign = !selectedVariant.isReadOnly();
                        if (enableDesign != design.isEnabled()) {
                            design.setEnabled(enableDesign);
                        }
                    }
                }
            };
        } catch (PartInitException pie) {
            EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                    getClass(), pie);
        }
    }

    // Javadoc inherited
    public void doSave(IProgressMonitor progressMonitor) {
        getActiveEditor().doSave(progressMonitor);
    }

    // Javadoc inherited
    public void doSaveAs() {
        getActiveEditor().doSaveAs();
    }

    // Javadoc inherited
    public boolean isSaveAsAllowed() {
        return getActiveEditor().isSaveAsAllowed();
    }

    // Javadoc inherited
    public Object getAdapter(Class aClass) {
        Object returnValue = null;
        if (EditorContext.class == aClass) {
            returnValue = context;
        } else {
            returnValue = super.getAdapter(aClass);
        }
        return returnValue;
    }

    // Javadoc inherited
    public void setFocus(Path path) {
        // Set the focus for the overview page - if this path represents data
        // within a variant, this will also have the effect of selecting that
        // variant.
        overview.setFocus(path);

        // The design page only displays the content for a variant. If the path
        // contains content, then focus the design page and display it.
        boolean seekingContent = true;
        for (int i = 0; i < path.getStepCount() && seekingContent; i++) {
            Step step = path.getStep(i);
            if (step instanceof PropertyStep) {
                PropertyStep property = (PropertyStep) step;
                if (PolicyModel.CONTENT.getName().equals(property.getProperty())) {
                    design.setFocus(path);
                    setActivePage(designIndex);
                    seekingContent = false;
                }
            }
        }

        // If we are still seeking content after iterating through the whole
        // path, the path must represent a non-content element on the overview
        // page, so we switch to the overview page.
        if (seekingContent) {
            setActivePage(overviewIndex);
        }
    }

    // Javadoc inherited
    public PolicyBuilder getPolicyBuilder() {
        return (PolicyBuilder) context.getInteractionModel().getModelObject();
    }

    // Javadoc inherited
    public void setPolicyBuilder(PolicyBuilder newBuilder) {
        // Get the previously selected variant builder so that we can attempt
        // to reselect it after the change.
        PolicyEditorContext context = (PolicyEditorContext) getContext();
        Proxy previousSelectedProxy = context.getSelectedVariant();
        VariantBuilder previouslySelectedBuilder =
                previousSelectedProxy == null ?
                null : (VariantBuilder) previousSelectedProxy.getModelObject();

        Proxy policyProxy = getContext().getInteractionModel();
        policyProxy.setReadWriteState(ReadWriteState.READ_WRITE);
        // Because the merge process may have modified the existing model
        // directly before calling this method, we must force the proxy's
        // structure to be updated to ensure new variants are added and
        // appropriate change events are fired.
        policyProxy.setModelObject(newBuilder, true);
        try {
            context.getPolicyFileAccessor().updatePolicyProxyState(
                    policyProxy, context.getProject());
        } catch (PolicyFileAccessException pfae) {
            EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                    getClass(), pfae);
        }

        if (previouslySelectedBuilder == null) {
            context.setSelectedVariant(null);
        } else {
            BeanProxy modelProxy = (BeanProxy) context.getInteractionModel();
            ListProxy variants = (ListProxy)
                    modelProxy.getPropertyProxy(PolicyModel.VARIANTS);
            boolean setSelection = false;
            for (int i = 0; !setSelection && i < variants.size(); i++) {
                VariantBuilder builder = (VariantBuilder)
                        variants.getItemProxy(i).getModelObject();
                if (previouslySelectedBuilder.equals(builder)) {
                    context.setSelectedVariant((BeanProxy) variants.getItemProxy(i));
                    setSelection = true;
                }
            }

            if (!setSelection) {
                context.setSelectedVariant(null);
            }
        }
    }

    // Javadoc inherited
    public String getPolicyName() {
        IFile file = (IFile) context.getResource();
        MCSProjectNature nature = MCSProjectNature.getMCSProjectNature(getProject());
        IPath policySourcePath = nature.getPolicySourcePath();
        IFolder policySourceFolder = getProject().getFolder(policySourcePath);
        IPath policyRoot = policySourceFolder.getProjectRelativePath();
        IPath policyPath = file.getProjectRelativePath();
        String policyName = null;
        if (policyRoot.isPrefixOf(policyPath)) {
            IPath relativePolicyPath =
                    policyPath.removeFirstSegments(
                            policyRoot.segmentCount());
            policyName = relativePolicyPath.toString();
            if (!policyName.startsWith("/")) {
                policyName = "/" + policyName;
            }
        } else {
            throw new IllegalStateException("Unexpected policy location");
        }
        return policyName;
    }

    // Javadoc inherited
    public IProject getProject() {
        return context.getProject();
    }

    // Javadoc inherited
    public EditorContext getContext() {
        return context;
    }

    // Javadoc inherited
    public void gotoMarker(IMarker iMarker) {
        try {
            Object pathString = iMarker.getAttribute(
                    ResourceDiagnosticsAdapter.DIAGNOSTIC_PATH_KEY);
            if (pathString instanceof String) {
                Path path = ModelFactory.getDefaultInstance().parsePath(
                        (String) pathString);
                setFocus(path);
            }
        } catch (CoreException ce) {
            // Could not retrieve the location of the marker
            EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                    getClass(), ce);
        }
    }

    // Javadoc inherited
    public void dispose() {
        super.dispose();
        if (resourceCloseListener != null) {
            resourceCloseListener.stopListener();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/4	adrianj	VBM:2005111601 Add style rule view

 01-Nov-05	10062/1	adrianj	VBM:2005101811 New theme GUI

 01-Nov-05	9886/3	adrianj	VBM:2005101811 New theme GUI

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
