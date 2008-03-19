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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.ab.editors.dom.MultiPageODOMEditor;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorPart;
import com.volantis.mcs.eclipse.ab.views.layout.FormatAttributesView;
import com.volantis.mcs.eclipse.builder.common.InteractionFocussable;
import com.volantis.mcs.eclipse.builder.common.ResourceDiagnosticsAdapter;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.eclipse.builder.editors.common.PolicyBuilderEditor;
import com.volantis.mcs.eclipse.builder.editors.common.ResourceCloseListener;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMemento;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.ReadWriteState;
import com.volantis.mcs.layouts.LayoutSchemaType;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.views.contentoutline.ContentOutline;

/**
 * The layout editor. Acts as a simple container for the overview and design
 * pages.
 */
public class LayoutEditor extends MultiPageODOMEditor
        implements InteractionFocussable, PolicyBuilderEditor, IGotoMarker {
    /**
     * The prefix for resources used by this class.
     */
    private static final String RESOURCE_PREFIX =
            "LayoutEditor.";

    /**
     * The name of the root element.
     */
    private static final String ROOT_ELEMENT_NAME =
            LayoutSchemaType.LAYOUT.getName();

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

    /**
     * The layout design part of this multi-part editor.
     */
    private LayoutDesignPart layoutDesignPart;

    /**
     * The layout overview of this multi-part editor.
     */
    private LayoutOverview layoutOverview;

    /**
     * The index for the layout design part
     * <p>
     * -1 is an invalid value, initialization of any instance of the editor
     * will set this correctly
     * </p>
     */
    private int indexOfDesignPart = -1;

    /**
     * The index for the layout overview part.
     *
     * <p>-1 is an invalid value, initialization of any instance of the editor
     * will set this correctly</p>
     */
    private int indexOfOverviewPart = -1;

    private LayoutEditorContext editorContext;

    /**
     * A listener for events indicating that the associated resource has moved
     * or been deleted, or that its project has been closed or deleted. If any
     * of these situations occur, the listener will close the editor.
     */
    private ResourceCloseListener resourceCloseListener;

    /**
     * Construct a new LayoutEditor.
     */
    public LayoutEditor() {
        super(ROOT_ELEMENT_NAME);
    }

    // javadoc inherited
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException {
        // Initialise the model-based editor context
        IFile file = null;
        if (input instanceof IFileEditorInput) {
            file = ((IFileEditorInput) input).getFile();
        }
        try {
            editorContext = LayoutEditorContextFactory.getDefaultInstance().
                    createLayoutEditorContext(this, file);

            // Register listener to handle project/resource being closed,
            // moved or deleted
            resourceCloseListener = new ResourceCloseListener(this);
            resourceCloseListener.startListener();
        } catch (PolicyFileAccessException ce) {
            String errorMessage = file.exists() ? LOAD_ERROR_MESSAGE :
                    MISSING_FILE_MESSAGE;
            throw new PartInitException(errorMessage);
        }

        // Allow the superclass to initialize the editor context
        super.init(site, input);
    }

    // javadoc inherited
    protected ODOMEditorContext createODOMEditorContext(
            final String rootElementName, final IFile file) {
        final IFile iFile = new LayoutIFile(null, file);

        LayoutODOMEditorContext odomEditorContext = null;
        try {
            odomEditorContext = new LayoutODOMEditorContext(editorContext,
                iFile, createUndoRedoMementoOriginator());
        } catch (Exception e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        editorContext.setOdomEditorContext(odomEditorContext);

        return odomEditorContext;
    }

    // javadoc inherited
    protected void createPages() {
        try {
            layoutOverview = new LayoutOverview(editorContext);
            indexOfOverviewPart = addPage(layoutOverview, getEditorInput());
            setPageText(indexOfOverviewPart, EditorMessages.getString(
                    RESOURCE_PREFIX + "overview.label"));

            final ODOMEditorContext odomEditorContext = getODOMEditorContext();
            layoutDesignPart =
                    new LayoutDesignPart(odomEditorContext, editorContext);
            indexOfDesignPart = addPage(layoutDesignPart, getEditorInput());
            setPageText(indexOfDesignPart, EditorMessages.getString(RESOURCE_PREFIX +
                    "design.label"));

        } catch (PartInitException e) {
            // if this is just because the wizard was cancelled then just show an error message
            if (e.getStatus().getCode() == ODOMEditorPart.MCS_ASSIGN_WIZARD_CANCELLED_CODE) {
                showMCSProjectAssignmentCancelledDialog(e);
            } else {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            }
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
    }

    // javadoc inherited
    public void gotoMarker(IMarker marker) {
        // As validation is now taking place through the model rather than
        // schema validation, we avoid direct access to the ODOM editor's
        // marker handling code, and delegate to the layout-specific classes
        // using the Path provided by model-based validation.
        try {
            Object pathString = marker.getAttribute(
                    ResourceDiagnosticsAdapter.DIAGNOSTIC_PATH_KEY);
            if (pathString instanceof String) {
                Path path = ModelFactory.getDefaultInstance().parsePath(
                        (String) pathString);
                setFocus(path);
            }
        } catch (CoreException ce) {
            // Could not retrieve the location of the marker
            EclipseCommonPlugin.logError(ABPlugin.getDefault(), getClass(), ce);
        }

    }

    /**
     * Override pageChange to ensure the format attributes view is shown
     * if the page is changing to the design page.
     */
    // rest of javadoc inherited
    protected void pageChange(int newPageIndex) {
        if (newPageIndex == indexOfDesignPart) {
            if (editorContext.getSelectedVariant() != null) {
                try {
                    IWorkbenchPage activePage =
                            PlatformUI.getWorkbench().
                            getActiveWorkbenchWindow().
                            getActivePage();
                    IWorkbenchPart activePart = activePage.getActivePart();
                    activePage.
                            showView("com.volantis.mcs.eclipse.ab.views.layout.FormatAttributesView");
                    activePage.activate(activePart);
                } catch (PartInitException e) {
                    EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
                }
            }
        }
    }

    /**
     * Override getAdapter() to delegate to getAdapter() in the layout design
     * page.
     */
    // rest of javadoc inherited
    public Object getAdapter(Class required) {
        return layoutDesignPart.getAdapter(required);
    }


    //javadoc inherited
    protected UndoRedoMementoOriginator createUndoRedoMementoOriginator() {
        return new MultiPageODOMEditor.MPOEMementoOriginator() {
            public UndoRedoMemento takeSnapshot() {
                return new LEMemento();
            }
        };
    }

    // Javadoc inherited
    public void setFocus(Path path) {
        // Set the focus for the overview page - if this path represents data
        // within a variant, this will also have the effect of selecting that
        // variant.
        layoutOverview.setFocus(path);

        // The design page only displays the content for a variant. If the path
        // contains content, then focus the design page and display it.
        boolean seekingContent = true;
        for (int i = 0; i < path.getStepCount() && seekingContent; i++) {
            Step step = path.getStep(i);
            if (step instanceof PropertyStep) {
                PropertyStep property = (PropertyStep) step;
                if (PolicyModel.CONTENT.getName().equals(property.getProperty())) {
                    layoutDesignPart.setFocus(path);
                    setActivePage(indexOfDesignPart);
                    seekingContent = false;
                }
            }
        }

        // If we are still seeking content after iterating through the whole
        // path, the path must represent a non-content element on the overview
        // page, so we switch to the overview page.
        if (seekingContent) {
            setActivePage(indexOfOverviewPart);
        }
    }

    /**
     * Records information about the page that should be displayed on
     * {@link UndoRedoMementoOriginator#restoreSnapshot}
     * so that if at Mmento's creation time the ContentOutline or the
     * FormatAttributesView is the active part, the Layout Design Editor
     * page will be displayed.
     */
    protected class LEMemento extends MultiPageODOMEditor.MPOEMemento {

        public LEMemento() {
            super();

            IWorkbenchPage activePage =
                    PlatformUI.getWorkbench().
                    getActiveWorkbenchWindow().
                    getActivePage();

            IWorkbenchPart iWorkbenchPart = activePage.getActivePart();

            if (iWorkbenchPart instanceof ContentOutline ||
                iWorkbenchPart instanceof FormatAttributesView) {
                //force LayoutDesignEditor to be displayed on undo/redo
                this.pageIndex = indexOfDesignPart;
            }
        }
    }

    public PolicyBuilder getPolicyBuilder() {
        return (PolicyBuilder) editorContext.getInteractionModel().getModelObject();
    }

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
            EclipseCommonPlugin.logError(ABPlugin.getDefault(),
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

        editorContext.refreshCachedDOM();
    }

    public String getPolicyName() {
        IFile file = (IFile) editorContext.getResource();
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

    public IProject getProject() {
        return editorContext.getProject();
    }

    public EditorContext getContext() {
        return editorContext;
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

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 18-May-04	4231/5	tom	VBM:2004042704 rework for 2004042704

 07-May-04	4068/5	allan	VBM:2004032103 Structure page policies section.

 06-May-04	4068/3	allan	VBM:2004032103 Structure page policies section.

 05-May-04	4115/2	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 16-Apr-04	3743/3	doug	VBM:2004032101 Added a DeviceEditorContext class

 30-Mar-04	3614/1	byron	VBM:2004022404 Layout: Panes are allowed same names

 17-Feb-04	2988/4	eduardo	VBM:2004020908 fixed static page index

 17-Feb-04	2988/2	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 23-Jan-04	2659/1	allan	VBM:2003112801 The ThemeDesign Editor

 23-Jan-04	2720/1	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 22-Jan-04	2540/5	byron	VBM:2003121505 Added main formats attribute page

 19-Jan-04	2562/10	allan	VBM:2003112010 Fix imports and javadoc.

 19-Jan-04	2562/8	allan	VBM:2003112010 Fix imports and javadoc.

 19-Jan-04	2562/6	allan	VBM:2003112010 Handle outline view showing and closing.

 18-Jan-04	2562/4	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 07-Jan-04	2447/1	philws	VBM:2004010609 Initial code for revised validation mechanism

 04-Jan-04	2309/2	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 17-Dec-03	2213/3	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 ===========================================================================
*/
