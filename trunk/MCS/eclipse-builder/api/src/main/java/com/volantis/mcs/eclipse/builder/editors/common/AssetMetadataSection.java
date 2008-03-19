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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyModelSet;
import com.volantis.mcs.interaction.BaseProxy;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ProxyModelChangedEvent;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.path.PropertyStep;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * Form section containing the metadata for an asset.
 */
public class AssetMetadataSection extends AssetEditorSection {
    private static final String RESOURCE_PREFIX = "AssetMetadataSection.";

    private static final PolicyFactory POLICY_FACTORY = PolicyFactory.getDefaultInstance();

    private PolicyEditorContext context;

    private boolean handlingPropertyChanges = true;

    private InteractionEventListener readOnlyListener =
            new InteractionEventListenerAdapter() {
                public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
                    // This appears to only ever be called for read-only events - is this correct?
                    if (event.isOriginator ()) {
                        setEnabled(!event.isReadOnly());
                    }
                }
            };

    private InteractionEventListener typeListener =
        new InteractionEventListenerAdapter() {
            public void proxyModelChanged(ProxyModelChangedEvent event) {
                if (event.isOriginator()) {
                    selectedVariantTypeChanged(event.getNewValue());
                }
            }
        };



    /**
     * The properties composite displaying the metadata properties.
     */
    private PropertiesComposite composite;

    public AssetMetadataSection(Composite parent, int style,
                                EditorContext context) {
        this(parent, style, context, null);
    }

    public AssetMetadataSection(Composite parent, int style,
                                EditorContext context, Map comboDescriptors) {
        super(parent, style, RESOURCE_PREFIX);
        this.context = (PolicyEditorContext) context;
        createDisplayArea(comboDescriptors);
        this.context.addSelectedVariantListener(new BuilderSelectionListener() {
            public void selectionMade(BuilderSelectionEvent event) {
                selectedVariantChanged(event);
            }
        });

        // Initialise the metadata section with the currently selected variant
        selectedVariantChanged(null);

    }

    private void createDisplayArea(Map comboDescriptors) {
        Composite displayArea = createDefaultDisplayArea();

        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        displayArea.setLayout(layout);

        PolicyModelSet modelSet = PolicyModelSet.getModelSet(context.getPolicyType());
        BeanClassDescriptor typeDescriptor = (BeanClassDescriptor)
                PolicyModel.MODEL_DESCRIPTOR.
                getTypeDescriptorStrict(modelSet.getMetaDataClass());

        List propertyList = typeDescriptor.getPropertyDescriptors();
        PropertyDescriptor[] properties = new PropertyDescriptor[propertyList.size()];
        for (int i = 0; i < properties.length; i++) {
            properties[i] = (PropertyDescriptor) propertyList.get(i);
        }

        composite = new PropertiesComposite(displayArea, SWT.NORMAL,
                properties, context, false, comboDescriptors, null);
        setDefaultColour(composite);

        composite.addPropertiesCompositeChangeListener(
                new PropertiesCompositeChangeListener() {
                    public void propertyChanged(PropertiesComposite composite,
                                                PropertyDescriptor property,
                                                Object newValue) {
                        metadataPropertyChanged(property, newValue);
                    }
                });

        GridData data = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(data);
    }

    /**
     * Handle changes in metadata properties.
     *
     * @param descriptor The descriptor of the property that has changed
     * @param newValue The new value of the property
     */
    private void metadataPropertyChanged(PropertyDescriptor descriptor, Object newValue) {
        if (handlingPropertyChanges) {
            BeanProxy metadataProxy = getMetadataProxy();
            if (metadataProxy != null) {
                PropertyIdentifier identifier = descriptor.getIdentifier();
                if (identifier == PolicyModel.CONVERSION_MODE) {
                    updateImagePreserveControlState((ImageConversionMode) newValue);
                }

                Proxy propertyProxy = metadataProxy.getPropertyProxy(descriptor.getIdentifier());
                Operation setValue = propertyProxy.prepareSetModelObjectOperation(newValue);
                context.executeOperation(setValue);
            }
        }
    }

    /**
     * Respond to changes in the selected variant.
     */
    private void selectedVariantChanged(BuilderSelectionEvent event) {
        handlingPropertyChanges = false;

        BeanProxy oldVariant =
                (BeanProxy) (event == null ? null : event.getOldSelection());
        BeanProxy newVariant = context.getSelectedVariant();

        Proxy oldType = null;
        if (oldVariant != null) {
            oldVariant.removeListener(readOnlyListener);

            oldType = oldVariant.getPropertyProxy(PolicyModel.VARIANT_TYPE);
            oldType.removeListener(typeListener);
        }

        if (newVariant == null) {
            composite.clear();
            setEnabled(false);
        } else {

            // Assume that all the controls are enabled.
            setEnabled(true);

            // The variant type has changed then make sure that the controls
            // are enabled properly.
            Proxy newType =
                newVariant.getPropertyProxy(PolicyModel.VARIANT_TYPE);
            boolean newTypeIsNull = newType.getModelObject() == VariantType.NULL;

            BeanProxy metadata = getMetadataProxy();

            if (metadata == null || newTypeIsNull) {
                composite.clear();
                setEnabled(false);
            } else {
                composite.updateFromProxy(metadata);

                // Update the state of the image preserve controls if
                // necessary.
                ImageConversionMode conversionMode = (ImageConversionMode)
                        composite.getProperty(PolicyModel.CONVERSION_MODE);
                if (conversionMode != null) {
                    updateImagePreserveControlState(conversionMode);
                }
            }

            // Note: Enable state should not be set to true after this point
            // as otherwise it blows away the work done in
            // updateImagePreserveControls.

            // If the variant is read only then disable all controls.
            if (newVariant.isReadOnly()) {
                setEnabled(false);
            }

            newVariant.addListener(readOnlyListener, false);
            newType.addListener(typeListener, false);
        }

        handlingPropertyChanges = true;
    }

    /**
     * Enable / disable the image preserve controls depending on the value of
     * the conversion mode.
     *
     * @param conversionMode The conversion mode.
     */
    private void updateImagePreserveControlState(
            ImageConversionMode conversionMode) {

        boolean enablePreserve =
                (conversionMode == ImageConversionMode.ALWAYS_CONVERT);
        composite.setPropertyEnabled(PolicyModel.PRESERVE_LEFT,
                enablePreserve);
        composite.setPropertyEnabled(PolicyModel.PRESERVE_RIGHT,
                enablePreserve);
    }

    /**
     * React to a possible change in the type of the currently selected
     * variant's type.
     *
     * @param newVariantType proxy pointing to the current type
     */
    private void selectedVariantTypeChanged(Object newVariantType) {
        boolean enabled = updateMetaDataProxy(newVariantType);

        setEnabled(enabled);
        if (enabled) {
            // Update the state of the image preserve controls if
            // necessary.
            ImageConversionMode conversionMode = (ImageConversionMode)
                    composite.getProperty(PolicyModel.CONVERSION_MODE);
            if (conversionMode != null) {
                updateImagePreserveControlState(conversionMode);
            }
        }
    }

    /**
     * Update the meta data proxy object based on whether the variant type is
     * null, or not.
     *
     * <p>If the variant type is null then the meta data proxy should be
     * discarded.</p>
     *
     * @param newVariantType The new variant type.
     * @return The new enabled state of the meta data section.
     */
    private boolean updateMetaDataProxy(Object newVariantType) {
        boolean enabled;
        if (newVariantType == VariantType.NULL) {

            BeanProxy selectedVariant = context.getSelectedVariant();
            if (selectedVariant != null) {
                BaseProxy metadataBase =
                    (BaseProxy)selectedVariant
                        .getPropertyProxy(PolicyModel.META_DATA);
                if (metadataBase != null) {
                    Operation setValue =
                        metadataBase.prepareSetModelObjectOperation(null);
                    context.executeOperation(setValue);
                }
            }

            enabled = false;
        } else {
            enabled = true;
        }

        return enabled;
    }

    /**
     * Gets the metadata proxy for the currently selected variant.
     *
     * @return The metadata proxy, or null if none exists
     */
    private BeanProxy getMetadataProxy() {
        BeanProxy metadataProxy = null;
        BeanProxy selectedVariant = context.getSelectedVariant();
        if (selectedVariant != null) {
            BaseProxy metadataBase = (BaseProxy) selectedVariant.getPropertyProxy(PolicyModel.META_DATA);
            if (metadataBase != null) {
                metadataProxy = (BeanProxy) metadataBase.getConcreteProxy();
                // If we have no concrete metadata, create an empty one of the
                // appropriate type unless the data is read only or the variant
                // type precludes this.
                Proxy typeProxy = selectedVariant.getPropertyProxy(PolicyModel.VARIANT_TYPE);
                if (metadataProxy == null && !metadataBase.isReadOnly() &&
                        typeProxy.getModelObject() != VariantType.NULL) {
                    MetaDataBuilder metaDataBuilder = getDefaultMetaDataBuilder();
                    metadataBase.setModelObject(metaDataBuilder);

                    // Now that we've set a concrete model object, we should
                    // have a concrete proxy.
                    metadataProxy = (BeanProxy) metadataBase.getConcreteProxy();
                }
            }
        }
        return metadataProxy;
    }

    // Javadoc inherited
    public void setFocus(Path path) {
        boolean seekingMetadata = true;
        int stepCount = path.getStepCount();
        for (int i = 0; i < stepCount && seekingMetadata; i++) {
            Step step = path.getStep(i);
            if (step instanceof PropertyStep) {
                PropertyStep property = (PropertyStep) step;
                if (PolicyModel.META_DATA.getName().equals(property.getProperty())) {
                    if ((i + 1) < stepCount) {
                        Step stylePropertyStep = path.getStep(i + 1);
                        if (stylePropertyStep instanceof PropertyStep) {
                            String propertyName = ((PropertyStep) stylePropertyStep).getProperty();
                            focusMetadata(propertyName);
                        }
                    }
                    seekingMetadata = false;
                }
            }
        }
    }

    /**
     * Set the focus to a specified metadata property.
     *
     * @param propertyName The name of the metadata property
     */
    private void focusMetadata(String propertyName) {
        boolean seekingProperty = true;
        Set styleProperties = composite.getSupportedPropertyIdentifiers();
        Iterator propertiesIterator = styleProperties.iterator();
        while (seekingProperty && propertiesIterator.hasNext()) {
            PropertyIdentifier identifier = (PropertyIdentifier) propertiesIterator.next();
            if (identifier.getName().equals(propertyName)) {
                seekingProperty = false;
                composite.selectProperty(identifier);
            }
        }
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        composite.setEnabled(b);
    }

    /**
     * Returns an empty instance of the default metadata type for this policy.
     *
     * @todo better There must be some way of doing this automatically (and if not, there should be).
     * @return An empty instance of the default metadata type for this policy,
     *         or null if one could not be created
     */
    private MetaDataBuilder getDefaultMetaDataBuilder() {
        MetaDataBuilder metaDataBuilder = null;
        PolicyType policyType = context.getPolicyType();
        if (policyType == VariablePolicyType.TEXT) {
            metaDataBuilder = POLICY_FACTORY.createTextMetaDataBuilder();
        } else if (policyType == VariablePolicyType.CHART) {
            metaDataBuilder = POLICY_FACTORY.createChartMetaDataBuilder();
        } else if (policyType == VariablePolicyType.AUDIO) {
            metaDataBuilder = POLICY_FACTORY.createAudioMetaDataBuilder();
        } else if (policyType == VariablePolicyType.VIDEO) {
            metaDataBuilder = POLICY_FACTORY.createVideoMetaDataBuilder();
        } else if (policyType == VariablePolicyType.IMAGE) {
            metaDataBuilder = POLICY_FACTORY.createImageMetaDataBuilder();
        } else if (policyType == VariablePolicyType.SCRIPT) {
            metaDataBuilder = POLICY_FACTORY.createScriptMetaDataBuilder();
        }
        return metaDataBuilder;
    }
}
