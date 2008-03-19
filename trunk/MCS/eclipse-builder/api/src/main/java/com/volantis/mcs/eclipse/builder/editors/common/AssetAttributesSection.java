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

import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.devrep.repository.api.devices.policy.PolicyDescriptorFactory;
import com.volantis.mcs.devices.policy.types.SelectionPolicyType;
import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.common.InteractionFocussable;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyModelSet;
import com.volantis.mcs.eclipse.builder.editors.themes.PolicySelectorBrowseAction;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InteractionEvent;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.utilities.StringUtils;
import com.volantis.synergetics.ObjectHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Form section displaying the attributes of a given asset.
 */
public class AssetAttributesSection extends FormSection
        implements InteractionFocussable {
    private static final String RESOURCE_PREFIX = "AssetAttributesSection.";

    private static final PolicyDescriptorFactory POLICY_DESCRIPTOR_FACTORY =
            PolicyDescriptorFactory.getDefaultInstance();

    private PolicyEditorContext context;

    private PropertyDescriptor[] attributes;

    private Map policyReferenceTypes;

    private Map comboDescriptors;

    /**
     * A list of controls that should be enabled/disabled when this section
     * is enabled/disabled.
     */
    private List enableableControls = new ArrayList();

    protected EditorContext getContext() {
        return context;
    }

    public AssetAttributesSection(Composite composite, int i,
                                  EditorContext context) {
        this(composite, i, context, null, null, null);
    }

    public AssetAttributesSection(Composite composite, int i,
                                  EditorContext context,
                                  PropertyDescriptor[] attributes,
                                  Map policyReferenceTypes,
                                  Map comboDescriptors) {
        super(composite, i);
        this.context = (PolicyEditorContext) context;
        this.attributes = attributes;
        this.policyReferenceTypes = policyReferenceTypes;
        this.comboDescriptors = comboDescriptors;
        createDisplayArea();
    }

    /**
     * Create the GUI components for this control.
     */
    private void createDisplayArea() {
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);

        Section section =
                createSection(this, EditorMessages.getString(RESOURCE_PREFIX +
                        "title"), null, Section.EXPANDED);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        section.setLayoutData(data);

        Composite displayWrapper = new Composite(section, SWT.NONE);
        section.setClient(displayWrapper);
        setDefaultColour(displayWrapper);
        layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        displayWrapper.setLayout(layout);

        addDescriptionBar(displayWrapper);
        displayWrapper.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        PropertiesComposite displayArea =
                new PropertiesComposite(displayWrapper,
                        SWT.NONE, context);
        displayArea.setEnabled(true);
        displayArea.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Add a category selector if the policy is variable
        if (context.getPolicyType() instanceof VariablePolicyType) {
            addCategorySelector(displayArea);
        }

        if (attributes != null && attributes.length > 0) {

            // Add the attributes after the other controls.
            displayArea.addProperties(attributes, false, comboDescriptors,
                    policyReferenceTypes);

            // Set the values of the properties
            BeanProxy interaction = (BeanProxy) context.getInteractionModel();
            displayArea.updateFromProxy(interaction);

            displayArea.addPropertiesCompositeChangeListener(
                    new PropertiesCompositeChangeListener() {
                        public void propertyChanged(
                                PropertiesComposite composite,
                                PropertyDescriptor property,
                                Object newValue) {
                            Proxy propertyProxy = ((BeanProxy) context.
                                    getInteractionModel()).getPropertyProxy(
                                    property.getIdentifier());
                            Operation setOp = propertyProxy.
                                    prepareSetModelObjectOperation(newValue);
                            context.executeOperation(setOp);
                        }
                    });
        }

        // Add fallback selectors if any are specified for this policy type
        addFallbackSelectors(displayArea);

        setDefaultColour(displayArea);
        setDefaultColour(this);

        enableableControls.add(displayArea);

        addReadWriteListener();

        readWriteStateChanged();
        context.getInteractionModel()
                .addListener(new InteractionEventListenerAdapter() {
                    public void readOnlyStateChanged(
                            ReadOnlyStateChangedEvent event) {
                        readWriteStateChanged();
                    }
                }, false);
    }

    private void addReadWriteListener() {
        InteractionEventListener listener =
                new InteractionEventListenerAdapter() {
                    public void readOnlyStateChanged(
                            final ReadOnlyStateChangedEvent event) {
                        boolean enabled = !event.isReadOnly();
                        Iterator it = enableableControls.iterator();
                        while (it.hasNext()) {
                            ((Control) it.next()).setEnabled(enabled);
                        }
                    }
                };
        context.getInteractionModel().addListener(listener, false);

        // Set the initial enabled state
        boolean enabled = !context.getInteractionModel().isReadOnly();
        Iterator it = enableableControls.iterator();
        while (it.hasNext()) {
            ((Control) it.next()).setEnabled(enabled);
        }
    }

    // Javadoc inherited
    // TODO later implement this
    public void setFocus(Path path) {
    }


    /**
     * Add the GUI components for specifying the category.
     *
     * @param parent The parent component (must be a two-column grid)
     */
    private void addCategorySelector(Composite parent) {
        Label categorySelectorLabel = new Label(parent, SWT.NONE);
        setDefaultColour(categorySelectorLabel);
        categorySelectorLabel.setText("Device category policy:");
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        categorySelectorLabel.setLayoutData(data);
        enableableControls.add(categorySelectorLabel);

        final ComboViewer combo = new ComboViewer(parent, SWT.READ_ONLY);
        //Default sorter for elements in ComboViewer.
        //Elements will be displayed in alphabetical order
        combo.setSorter(new ViewerSorter());
        data = new GridData(GridData.FILL_HORIZONTAL);
        combo.getControl().setLayoutData(data);
        enableableControls.add(combo.getControl());

        final Map namesToDescriptors = new HashMap();

        List policies = new ArrayList();
        try {
            DeviceRepository repository = ProjectDeviceRepositoryAccessor
                    .getProjectDeviceRepository(context);
            Iterator policyNames = repository.getDevicePolicyNames().iterator();
            while (policyNames.hasNext()) {
                String policyName = (String) policyNames.next();
                PolicyDescriptor policyDescriptor =
                        repository.getPolicyDescriptor(policyName,
                                Locale.getDefault());
                if (policyDescriptor != null) {
                    if (policyDescriptor.getPolicyType()
                            instanceof SelectionPolicyType) {
                        namesToDescriptors.put(policyName, policyDescriptor);
                        policies.add(policyName);
                    }
                } else {
                    // Should never happen.
                    throw new IllegalStateException(
                            "Policy descriptor cannot be found for " +
                                    policyName);
                }
            }
        } catch (IOException ioe) {
            EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                    getClass(), ioe);
        } catch (DeviceRepositoryException dre) {
            EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                    getClass(), dre);
        }

        addNullDescriptor(namesToDescriptors, policies);

        combo.setLabelProvider(new LabelProvider() {
            public String getText(Object o) {
                String policyName = (String) o;
                PolicyDescriptor descriptor = (PolicyDescriptor)
                        namesToDescriptors.get(policyName);
                return descriptor.getPolicyDescriptiveName();
            }
        });

        combo.setContentProvider(new ArrayContentProvider());
        combo.setInput(policies);

        combo.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection =
                        (IStructuredSelection) combo.getSelection();
                String policyName = (String) selection.getFirstElement();
                PolicyDescriptor descriptor = (PolicyDescriptor)
                        namesToDescriptors.get(policyName);
                SelectionPolicyType policyType =
                        (SelectionPolicyType) descriptor.getPolicyType();
                context.setCategoryValues(policyType.getKeywords());

                BeanProxy model = (BeanProxy) context.getInteractionModel();
                Proxy categorisationScheme = model.getPropertyProxy(
                        PolicyModel.CATEGORISATION_SCHEME);

                if (!ObjectHelper.equals(policyName,
                        categorisationScheme.getModelObject())) {
                    Operation setSchemeOp = categorisationScheme
                            .prepareSetModelObjectOperation(policyName);
                    context.executeOperation(setSchemeOp);
                }
            }
        });

        BeanProxy model = (BeanProxy) context.getInteractionModel();
        final Proxy categorisationScheme =
                model.getPropertyProxy(PolicyModel.CATEGORISATION_SCHEME);
        String selectedPolicy = (String) categorisationScheme.getModelObject();
        if (selectedPolicy != null) {
            combo.setSelection(new StructuredSelection(selectedPolicy));
            PolicyDescriptor descriptor = (PolicyDescriptor)
                    namesToDescriptors.get(selectedPolicy);
            SelectionPolicyType policyType =
                    (SelectionPolicyType) descriptor.getPolicyType();
            context.setCategoryValues(policyType.getKeywords());
        }

        categorisationScheme.addListener(new InteractionEventListenerAdapter() {
            protected void interactionEvent(InteractionEvent event) {
                String selectedPolicy =
                        (String) categorisationScheme.getModelObject();
                if (selectedPolicy != null) {
                    combo.setSelection(new StructuredSelection(selectedPolicy));
                    PolicyDescriptor descriptor = (PolicyDescriptor)
                            namesToDescriptors.get(selectedPolicy);
                    SelectionPolicyType policyType =
                            (SelectionPolicyType) descriptor.getPolicyType();
                    context.setCategoryValues(policyType.getKeywords());
                }
            }
        }, false);
    }

    /**
     * Add a null or empty descriptor to the list.
     * This will allow users to unset the device category policy.
     *
     * @param namesToDescriptors
     * @param policies
     */
    private void addNullDescriptor(Map namesToDescriptors, List policies) {
        // Add a null descriptor which matches to nothing
        SelectionPolicyType selectionType = new SelectionPolicyType() {
            public List getKeywords() {
                return new ArrayList();
            }
        };
        final PolicyDescriptor nullDescriptor =
                POLICY_DESCRIPTOR_FACTORY
                        .createPolicyDescriptor(selectionType, "");
        namesToDescriptors.put("", nullDescriptor);
        policies.add("");

    }

    /**
     * Add the GUI components for specifying fallback selectors.
     *
     * @param parent The parent component (must be a two-column grid)
     */
    private void addFallbackSelectors(final Composite parent) {
        PolicyModelSet policyModelSet =
                PolicyModelSet.getModelSet(context.getPolicyType());
        final PolicyType[] types = policyModelSet.getFallBackTypes();
        Map typesToTextComponents = new HashMap();

        if (types != null && types.length > 0) {
            for (int i = 0; i < types.length; i++) {
                final PolicyType type = types[i];
                String labelString = EditorMessages.getString(
                        RESOURCE_PREFIX + StringUtils.toLowerIgnoreLocale(
                                types[i].toString()) + ".fallback.label");

                Label fallBackLabel = new Label(parent, SWT.NONE);
                setDefaultColour(fallBackLabel);
                fallBackLabel.setText(labelString);
                GridData data =
                        new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
                fallBackLabel.setLayoutData(data);
                enableableControls.add(fallBackLabel);

                Composite entry = new Composite(parent, SWT.NONE);
                GridLayout layout = new GridLayout(2, false);
                layout.marginHeight = 0;
                layout.marginWidth = 0;
                entry.setLayout(layout);
                data = new GridData(GridData.FILL_HORIZONTAL);
                entry.setLayoutData(data);
                setDefaultColour(entry);

                final Text fallBackText = new Text(entry, SWT.BORDER);
                data = new GridData(GridData.FILL_HORIZONTAL);
                fallBackText.setLayoutData(data);
                typesToTextComponents.put(type, fallBackText);
                enableableControls.add(fallBackText);

                final PolicySelectorBrowseAction browseAction =
                        new PolicySelectorBrowseAction(FileExtension.
                                getFileExtensionForPolicyType(types[i]));

                Button button = new Button(entry, SWT.NONE);
                button.setText("Browse...");
                button.addSelectionListener(new SelectionListener() {
                    private void handleSelection() {
                        String oldText = fallBackText.getText();
                        String policyValue = browseAction.doBrowse(
                                oldText, parent, context);

                        // Has anything changed?
                        if (policyValue != null &&
                                !policyValue.equals(oldText)) {
                            fallBackText.setText(policyValue);
                            setFallbackPolicy(type, policyValue);
                        }
                    }

                    public void widgetSelected(SelectionEvent event) {
                        handleSelection();
                    }

                    public void widgetDefaultSelected(SelectionEvent event) {
                        handleSelection();
                    }
                });
                enableableControls.add(button);
            }

            // Initialise the fallback policies
            ListProxy alternatePolicies =
                    (ListProxy) ((BeanProxy) context.getInteractionModel()).
                            getPropertyProxy(PolicyModel.ALTERNATE_POLICIES);
            int size = alternatePolicies.size();

            for (int i = 0; i < size; i++) {
                Proxy itemProxy = alternatePolicies.getItemProxy(i);
                Object modelObject = itemProxy.getModelObject();
                PolicyReference ref = (PolicyReference) modelObject;
                Text text = (Text) typesToTextComponents
                        .get(ref.getExpectedPolicyType());
                text.setText(ref.getName());
            }

            // Register the listeners for the fallback GUI components
            for (int i = 0; i < types.length; i++) {
                final PolicyType type = types[i];
                final Text text = (Text) typesToTextComponents.get(type);
                text.addModifyListener(new ModifyListener() {
                    public void modifyText(ModifyEvent event) {
                        setFallbackPolicy(type, text.getText());
                    }
                });
            }
        }
    }

    /**
     * Adds the description of this section (along with the lock/unlock buttons
     * if necessary).
     *
     * @param parent The parent composite for the bar
     */
    private void addDescriptionBar(Composite parent) {
        String description = EditorMessages.getString(RESOURCE_PREFIX +
                "subheading");
        Composite descriptionBar = new Composite(parent, SWT.NORMAL);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        descriptionBar.setLayoutData(data);
        GridLayout layout = new GridLayout(2, false);
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        descriptionBar.setLayout(layout);

        Label descriptionLabel = new Label(descriptionBar, SWT.NONE);
        descriptionLabel.setText(description);
        data = new GridData(GridData.FILL_HORIZONTAL);
        descriptionLabel.setLayoutData(data);

        Composite buttons = new Composite(descriptionBar, SWT.NONE);
        data = new GridData(GridData.HORIZONTAL_ALIGN_END);
        buttons.setLayoutData(data);
        buttons.setLayout(new RowLayout(SWT.HORIZONTAL));

        createButtons(buttons);

        setDefaultColour(descriptionBar);
        setDefaultColour(descriptionLabel);
        setDefaultColour(buttons);
    }

    /**
     * Create any required buttons for this section and their corresponding
     * actions.
     *
     * @param buttons The parent composite for the buttons.
     */
    protected void createButtons(Composite buttons) {
    }

    /**
     * Set a fallback policy for a specified type - in order to do this any
     * previous policy for this type must first be removed.
     *
     * @param type   The fallback policy type
     * @param policy The new value
     */
    private void setFallbackPolicy(PolicyType type, String policy) {
        ListProxy alternatePolicies =
                (ListProxy) ((BeanProxy) context.getInteractionModel()).
                        getPropertyProxy(PolicyModel.ALTERNATE_POLICIES);
        int size = alternatePolicies.size();
        for (int i = size - 1; i >= 0; i--) {
            PolicyReference reference = (PolicyReference)
                    alternatePolicies.getItemProxy(i).getModelObject();
            if (reference.getExpectedPolicyType().equals(type)) {
                Operation removeOp = alternatePolicies
                        .prepareRemoveProxyItemOperation(
                                alternatePolicies.getItemProxy(i));
                context.executeOperation(removeOp);
            }
        }

        // Do not add an empty policy value as this is illlegal
        if (policy != null && policy.trim().length() > 0) {
            PolicyReference newReference = PolicyFactory.getDefaultInstance().
                    createPolicyReference(policy, type);
            Operation addOp =
                    alternatePolicies
                            .prepareAddModelItemOperation(newReference);
            context.executeOperation(addOp);
        }
    }

    /**
     * Update the enabled state of the lock/unlock actions.
     */
    public void readWriteStateChanged() {
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/2	adrianj	VBM:2005111601 Add style rule view

 06-Dec-05	10652/1	adrianj	VBM:2005112110 Add context menu for StyleCategoriesComposite

 06-Dec-05	10625/1	adrianj	VBM:2005112110 Support synchronizable categories

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9886/3	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
