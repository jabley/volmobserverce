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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.accessors.xml.jibx.JiBXWriter;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorUtils;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.builder.common.policies.CreatePolicyConfiguration;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessor;
import com.volantis.mcs.eclipse.builder.editors.LayoutModificationListener;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ProxyModelChangedEvent;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.xml.PolicySchemas;
import com.volantis.shared.content.BinaryContentInput;
import com.volantis.shared.throwable.ExtendedIOException;
import com.volantis.xml.schema.W3CSchemata;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Specialisation of PolicyEditorContext for editing layouts
 */
public class LayoutEditorContext extends PolicyEditorContext {
    private LayoutOverview overviewPage;
    private final LayoutEditor editor;
    private final Map variantBuilderProxyToIFile;
    private final Map variantBuilderProxyToRootElement;
    private IFile file;
    private ODOMEditorContext odomEditorContext;
    
    /**
     * The variant type proxy for the currently selected variant.
     */ 
    private Proxy variantTypeProxy;

    /**
     * A list of modification listeners to notify when the model is updated.
     */
    private List modificationListeners = new ArrayList();

    /**
     * Listener for changes in the type of the currently selected variant, to
     * pick up on changes to and from null variant.
     */
    private InteractionEventListener typeChangeListener =
            new InteractionEventListenerAdapter() {
                public void proxyModelChanged(ProxyModelChangedEvent event) {
                    // Treat this in the same way as a change of the selected
                    // variant
                    selectedVariantChanged();
                }
            };
    
    public LayoutEditorContext(final LayoutEditor editor, IFile file)
            throws PolicyFileAccessException {

        this.editor = editor;
        this.file = file;
        loadResource(file);
        variantBuilderProxyToIFile = new HashMap();
        variantBuilderProxyToRootElement = new HashMap();
        addSelectedVariantListener(new BuilderSelectionListener() {
            public void selectionMade(BuilderSelectionEvent event) {
                selectedVariantChanged();
            }
        });
    }

    /**
     * Set the layout overview page associated with this context. This is a
     * hack to allow the layout design page (which should generally unaware of
     * the other GUI pages) to delegate save requests to the model-aware
     * overview.
     *
     * @param overview The layout overview page
     */
    public void setLayoutOverview(LayoutOverview overview) {
        overviewPage = overview;
    }

    /**
     * Retrieve the layout overview page associated with this context.
     *
     * @return The layout overview page associated with this context, or null
     *         if none has been set
     * @see #setLayoutOverview(LayoutOverview)
     */
    public LayoutOverview getLayoutOverview() {
        return overviewPage;
    }

    /**
     * Respond to changes in the selected variant.
     */
    private void selectedVariantChanged() {
        if (variantTypeProxy != null) {
            variantTypeProxy.removeListener(typeChangeListener);
        }

        final BeanProxy variantBuilderProxy = getSelectedVariant();
        if (variantBuilderProxy != null) {
            variantTypeProxy = variantBuilderProxy.
                    getPropertyProxy(PolicyModel.VARIANT_TYPE);
            if (variantTypeProxy != null) {
                variantTypeProxy.addListener(typeChangeListener, false);
            }

            try {
                final ODOMElement rootElement =
                    odomEditorContext.getRootElement();
                if (rootElement != null) {
                    odomEditorContext.removeRootElement(rootElement);
                }

                // Check whether the variant is null;
                boolean isNullVariant = false;
                if (variantTypeProxy != null) {
                    isNullVariant =  (VariantType.NULL ==
                            variantTypeProxy.getModelObject());
                }

                ODOMElement newRootElement = null;

                if (isNullVariant) {
                    // Pretend we've got no selection so that we display that
                    // there is no content to edit.
                    odomEditorContext.getODOMSelectionManager().setSelection(
                            new ArrayList());
                } else {
                    final IFile ifile = getIFileForVariant(variantBuilderProxy);
                    odomEditorContext.setPolicyResource(ifile);
                    newRootElement =
                        getRootElementForVariant(variantBuilderProxy, ifile);

                    odomEditorContext.addRootElement(newRootElement, null);

                    odomEditorContext.getODOMSelectionManager().setSelection(
                        Collections.singletonList(newRootElement));
                }
            } catch (Exception e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            }
        }
    }

    /**
     * Returns a layout IFile that contains the layout specified by the variant
     * builder.
     *
     * @param variantBuilderProxy the variant builder that describes the layout
     * @return IFile for the layout
     */
    private IFile getIFileForVariant(final BeanProxy variantBuilderProxy)
            throws RepositoryException {

        IFile layoutIFile =
            (IFile) variantBuilderProxyToIFile.get(variantBuilderProxy);
        if (layoutIFile == null) {

            final VariantBuilder variantBuilder =
                (VariantBuilder) variantBuilderProxy.getModelObject();
            ContentBuilder contentBuilder = variantBuilder.getContentBuilder();

            if (contentBuilder == null) {
                final InternalLayoutContentBuilder layoutBuilder =
                    InternalPolicyFactory.getInternalInstance().
                        createLayoutContentBuilder();
                final CanvasLayout layout = new CanvasLayout();
                layoutBuilder.setLayout(layout);
                contentBuilder = layoutBuilder;
            }
            final JiBXWriter writer = new JiBXWriter();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            writer.write(new OutputStreamWriter(baos), contentBuilder);
            layoutIFile = new LayoutIFile(baos.toByteArray(), file);
            variantBuilderProxyToIFile.put(variantBuilderProxy, layoutIFile);
        }
        return layoutIFile;
    }

    /**
     * Returns an ODOM layout root element for the layout specified by the
     * variant builder.
     *
     * @param variantBuilderProxy the variant builder that describes the layout
     * @param file the file to read from the root element if it was not cached
     * @return the ODOM root element
     */
    private ODOMElement getRootElementForVariant(
            final BeanProxy variantBuilderProxy, final IFile file)
        throws IOException, CoreException, JDOMException {

        ODOMElement root = (ODOMElement)
            variantBuilderProxyToRootElement.get(variantBuilderProxy);
        if (root == null) {
            root = ODOMEditorUtils.createRootElement(file,
                ODOMEditorContext.ODOM_FACTORY);
            // Add the schema location for the LPDM schema.
            Namespace xsiNamespace =
                    Namespace.getNamespace("xsi",
                            W3CSchemata.XSI_NAMESPACE);
            root.setAttribute("schemaLocation",
                    PolicySchemas.MARLIN_LPDM_CURRENT.getNamespaceURL() +
                    " " + PolicySchemas.MARLIN_LPDM_CURRENT.getLocationURL(),
                    xsiNamespace);
            variantBuilderProxyToRootElement.put(variantBuilderProxy, root);
            final ODOMElement odomContentRoot = root;
            root.addChangeListener(new ODOMChangeListener() {
                public void changed(ODOMObservable node, ODOMChangeEvent event) {
                    odomNodeChanged(variantBuilderProxy, odomContentRoot);
                }
            });
        }
        return root;
    }

    /**
     * Handle a change within an ODOM element associated with a particular
     * variant proxy.
     *
     * <p>When the ODOM changes, we convert it to the model format and update
     * it against the proxy structure to allow for model-based validation.</p>
     *
     * @param variantProxy The variant proxy to which the ODOM element maps
     * @param odomRoot The ODOM root element representing the content
     */
    private void odomNodeChanged(BeanProxy variantProxy, ODOMElement odomRoot) {
        if (!variantProxy.isReadOnly()) {
            try {
                Proxy contentProxy =
                        variantProxy.getPropertyProxy(PolicyModel.CONTENT);
                ContentBuilder content =
                        documentToBuilder(odomRoot.getDocument(), contentProxy);
                contentProxy.setModelObject(content);
                fireLayoutChanged(
                        ((InternalLayoutContentBuilder) content).getLayout());
            } catch (IOException re) {
                // EclipseCommonPlugin.handleError(ABPlugin.getDefault(), re);
            }
        }
    }

    /**
     * Fire notification to all listeners that the layout has changed.
     *
     * @param layout The changed layout.
     */
    private void fireLayoutChanged(Layout layout) {
        Iterator it = modificationListeners.iterator();
        while (it.hasNext()) {
            LayoutModificationListener listener =
                    (LayoutModificationListener) it.next();
            listener.layoutModified(layout);
        }
    }

    // Javadoc not required
    public void addLayoutModificationListener(
            LayoutModificationListener listener) {
        modificationListeners.add(listener);
    }

    // javadoc inherited
    protected Class getModelType() {
        return VariablePolicy.class;
    }

    /**
     * A layout context will always represent a layout variable policy type.
     *
     * @return The layout variable policy type
     */
    public PolicyType getPolicyType() {
        return VariablePolicyType.LAYOUT;
    }

    /**
     * Returns the layout editor associated with this context.
     *
     * @return the layout editor
     */
    public LayoutEditor getLayoutEditor() {
        return editor;
    }

    /**
     * Sets the odom editor context.
     *
     * @param odomEditorContext the new odom editor context
     */
    public void setOdomEditorContext(final ODOMEditorContext odomEditorContext) {
        this.odomEditorContext = odomEditorContext;
    }

    /**
     * Creates a policy file accessor that updates the model object from the
     * ODOM where necessary before saving.
     *
     * @return A policy file accessor for this layout
     */
    public PolicyFileAccessor getPolicyFileAccessor() {
        final PolicyFileAccessor underlying = super.getPolicyFileAccessor();
        return new PolicyFileAccessor() {
            public IResource createPolicy(IPath location, PolicyBuilder policy, CreatePolicyConfiguration configuration) throws PolicyFileAccessException {
                return underlying.createPolicy(location, policy, configuration);
            }

            public void deletePolicy(IResource policyResource) throws PolicyFileAccessException {
                underlying.deletePolicy(policyResource);
            }

            public void renamePolicy(IResource policyResource, IPath destination) throws PolicyFileAccessException {
                underlying.renamePolicy(policyResource, destination);
            }

            public void savePolicy(PolicyBuilder policy, IResource policyResource, IProgressMonitor monitor) throws PolicyFileAccessException {
                try {
                    final Set entries = variantBuilderProxyToRootElement.entrySet();
                    for (Iterator iter = entries.iterator(); iter.hasNext(); ) {
                        final Map.Entry entry = (Map.Entry) iter.next();
                        final BeanProxy variantBuilderProxy =
                            (BeanProxy) entry.getKey();
                        final Proxy contentBuilderProxy =
                            variantBuilderProxy.getPropertyProxy(PolicyModel.CONTENT);
                        if (VariantType.NULL == variantBuilderProxy.getPropertyProxy(
                                PolicyModel.VARIANT_TYPE).getModelObject()) {
                            contentBuilderProxy.setModelObject(null);
                        } else if (!contentBuilderProxy.isReadOnly()) {
                            final ODOMElement element = (ODOMElement) entry.getValue();
                            final ContentBuilder contentBuilder = documentToBuilder(
                                    element.getDocument(), contentBuilderProxy);
                            contentBuilderProxy.setModelObject(contentBuilder);
                        }
                    }
                } catch (IOException e) {
                    throw new PolicyFileAccessException(e);
                }
                underlying.savePolicy(policy, policyResource, monitor);
            }

            public PolicyBuilder loadPolicy(IResource policyResource) throws PolicyFileAccessException {
                return underlying.loadPolicy(policyResource);
            }

            public Proxy wrapPolicy(PolicyBuilder builder, ModelDescriptor descriptor, IProject project) throws PolicyFileAccessException {
                return underlying.wrapPolicy(builder, descriptor, project);
            }

            public void updatePolicyProxyState(Proxy proxy, IProject project) throws PolicyFileAccessException {
                underlying.updatePolicyProxyState(proxy, project);
            }
        };
    }

    /**
     * Saves the layout variants to the specified file using the policy file
     * accessor (which handles collaborative working if necessary).
     *
     * @param saveAsFile target file
     * @param monitor progress monitor
     */
    public void saveFile(final IFile saveAsFile, IProgressMonitor monitor)
            throws CoreException {

        if (saveAsFile != null) {
            file = saveAsFile;
        }

        PolicyBuilder builder = (PolicyBuilder)
                getInteractionModel().getModelObject();
        try {
            getPolicyFileAccessor().savePolicy(builder, saveAsFile, monitor);
        } catch (PolicyFileAccessException pfae) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), pfae);
        }
    }

    /**
     * Builds a ContentBuilder from the document using JiBX.
     *
     * @param document the source document
     * @param contentProxy The content proxy corresponding to the document
     * @return the content builder read from the document
     */
    private ContentBuilder documentToBuilder(final Document document,
                                             final Proxy contentProxy)
            throws IOException {

        // Some parts of the layout are edited from the overview page and may
        // never make it into the ODOM. Preserve these so we can add them later
        String defaultFragment = null;
        String defaultSegment = null;
        if (contentProxy != null) {
            Object contentModel = contentProxy.getModelObject();
            if (contentModel != null &&
                    contentModel instanceof InternalLayoutContentBuilder) {
                Layout layout = ((InternalLayoutContentBuilder) contentModel).
                        getLayout();
                if (layout != null) {
                    defaultFragment = layout.getDefaultFragmentName();
                    defaultSegment = layout.getDefaultSegmentName();
                }
            }
        }

        XMLOutputter outputter = new XMLOutputter();
        String docString = outputter.outputString(document);
        byte[] bytes = null;
        try {
            bytes = docString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        final JiBXReader reader;
        try {
            reader = new JiBXReader(Class.forName(
                "com.volantis.mcs.policies.impl.variants.layout.LayoutContentBuilderImpl"));
        } catch (ClassNotFoundException e) {
            throw new ExtendedIOException(e);
        }
        final BinaryContentInput content = new BinaryContentInput(inputStream);
        ContentBuilder contentBuilder = null;
        try {
            contentBuilder =
                (ContentBuilder) reader.read(content, file.getName());
        } catch (ArrayIndexOutOfBoundsException e) {
            // HACK : This can happen if we are in the process of removing
            // a grid column. First the grid column count is updated then
            // the columns are removed. But a change event and layout
            // validation is triggered when the column count is altered. As
            // the model is in an invalid state it fails to read in
            // correctly.
            throw new IOException("Failed to read layout due to ArrayIndexOutOfBoundsException");
        }

        // If we have a valid content builder with layout (which should always
        // be the case) re-insert the data that was edited from the overview
        // page.
        if (contentBuilder instanceof InternalLayoutContentBuilder) {
            Layout layout = ((InternalLayoutContentBuilder) contentBuilder).getLayout();
            if (layout != null) {
                if (defaultFragment != null) {
                    layout.setDefaultFragmentName(defaultFragment);
                }

                if (defaultSegment != null) {
                    layout.setDefaultSegmentName(defaultSegment);
                }
            }
        }

        return contentBuilder;
    }

    /**
     * Returns true iff there is no selected variant or it is read only
     * @return the read only status of the selected variant
     */
    public boolean isSelectedVariantReadOnly() {
        boolean readOnly = false;
        final BeanProxy selectedVariantProxy = getSelectedVariant();
        if (selectedVariantProxy == null) {
            readOnly = true;
        } else {
            final VariantBuilder selectedVariant =
                (VariantBuilder) selectedVariantProxy.getModelObject();
            if (selectedVariant == null) {
               readOnly = true;
            }
        }
        return readOnly;
    }

    /**
     * Flush the caches of variant to ODOM/IFile information.
     */
    public void refreshCachedDOM() {
        variantBuilderProxyToIFile.clear();
        variantBuilderProxyToRootElement.clear();
        setSelectedVariant(getSelectedVariant());
    }
}
