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
package com.volantis.mcs.eclipse.builder.editors.policies;

import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.common.InteractionFocussable;
import com.volantis.mcs.eclipse.builder.common.ResourceDiagnosticsAdapter;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.editors.common.AlertsActionsSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetAttributesSection;
import com.volantis.mcs.eclipse.builder.editors.common.BuilderEditorPart;
import com.volantis.mcs.eclipse.builder.editors.common.PolicyBuilderEditor;
import com.volantis.mcs.eclipse.builder.editors.common.ResourceCloseListener;
import com.volantis.mcs.eclipse.builder.editors.common.AssetSectionFactory;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.ReadWriteState;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.VariantBuilder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;

import java.util.Map;

/**
 * A common superclass for all policy editors.
 */
public abstract class PolicyEditor extends BuilderEditorPart
        implements PolicyBuilderEditor, IGotoMarker {
    private AlertsActionsSection alertsActionsSection;
    private AssetAttributesSection assetAttributesSection;

    /**
     * A listener for events indicating that the associated resource has moved
     * or been deleted, or that its project has been closed or deleted. If any
     * of these situations occur, the listener will close the editor.
     */
    private ResourceCloseListener resourceCloseListener;

    public PolicyEditor() {
        super(new PolicyEditorContext());
        ((PolicyEditorContext) getContext()).setPolicyType(getPolicyType());
    }

    // Javadoc inherited
    public void setFocus() {
    }

    /**
     * Get the policy type edited by this editor.
     *
     * @return The policy type edited by this editor
     */
    protected abstract PolicyType getPolicyType();

    /**
     * Sets the colour of a control to the default background colour for the
     * GUI (the list background colour).
     *
     * @param control The control to set the colour for
     */
    protected void setDefaultColour(Control control) {
        control.setBackground(control.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    }

    protected void createAlertsActionsSection(Composite parent) {
        alertsActionsSection =
                new AlertsActionsSection(parent, SWT.NONE, getContext());
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        alertsActionsSection.setLayoutData(data);
    }

    protected void createAssetAttributesSection(Composite parent) {
        FormToolkit formToolkit =
                new FormToolkit(parent.getShell().getDisplay());
        Form form = formToolkit.createForm(parent);
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);

        GridData data = new GridData(GridData.FILL_BOTH);
        form.setLayoutData(data);

        assetAttributesSection = AssetSectionFactory.getDefaultInstance().
                createAssetAttributesSection(form.getBody(), getContext(),
                        getAttributeDescriptors(), getPolicyReferenceTypes(),
                        getComboDescriptors());
        data = new GridData(GridData.FILL_BOTH);
        assetAttributesSection.setLayoutData(data);
    }

    protected PropertyDescriptor[] getAttributeDescriptors() {
        return null;
    }

    protected Map getPolicyReferenceTypes() {
        return null;
    }

    /**
     * Retrieves a map of combo descriptors for this editor.
     *
     * <p>By default there are no combo descriptors, but subclasses can
     * override this method to provide them.</p>
     *
     * @return A map associating property identifiers with the corresponding
     *         combo descriptor, or null if there are no combo descriptors to
     *         use.
     */
    protected Map getComboDescriptors() {
        return null;
    }

    // Javadoc inherited
    public PolicyBuilder getPolicyBuilder() {
        return (PolicyBuilder) getContext().getInteractionModel().getModelObject();
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
        IFile file = (IFile) getContext().getResource();
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
        return getContext().getProject();
    }

    /**
     * Set the focus of an editor component to the specified path if it is
     * non-null.
     *
     * @param focussable The editor component to set (may be null)
     * @param path The path to focus on if the component is non-null
     */
    protected void setFocusIfExists(InteractionFocussable focussable, Path path) {
        if (focussable != null) {
            focussable.setFocus(path);
        }
    }

    // Javadoc inherited
    public void setFocus(Path path) {
        setFocusIfExists(assetAttributesSection, path);
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

    // Javadoc inherited
    public void init(IEditorSite iEditorSite, IEditorInput iEditorInput)
            throws PartInitException {
        super.init(iEditorSite, iEditorInput);

        // Register listener to handle project/resource being closed,
        // moved or deleted
        resourceCloseListener = new ResourceCloseListener(this);
        resourceCloseListener.startListener();
    }
}
