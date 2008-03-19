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

import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.themes.StylePairEditor;
import com.volantis.mcs.eclipse.builder.editors.themes.StylePropertyBrowseAction;
import com.volantis.mcs.eclipse.builder.editors.themes.StylePropertyMetadata;
import com.volantis.mcs.eclipse.builder.editors.themes.StyleValueEditor;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.model.ThemeModel;
import com.volantis.mcs.themes.types.StyleChoiceType;
import com.volantis.mcs.themes.types.StyleFractionType;
import com.volantis.mcs.themes.types.StylePairType;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.styling.properties.StyleProperty;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

/**
 * Composite for displaying and editing a series of properties.
 */
public class PropertiesComposite extends Composite {
    /**
     * The resource prefix for this class.
     */
    private static final String RESOURCE_PREFIX = "PropertiesComposite.";

    /**
     * The characters that are valid in integers
     */
    private static final String DIGITS = "0123456789";

    /**
     * A map of the property accessors for this composite, associated with their
     * corresponding property descriptors.
     */
    private Map accessors = new HashMap();

    /**
     * A list of properties composite change listeners.
     */
    private List listeners = new ArrayList();

    /**
     * The shared context for the editor containing this composite.
     */
    private final EditorContext context;

    /**
     * The descriptors used to initialise this composite.
     */
    private final Set supportedPropertyIdentifiers;

    /**
     * An unmodifiable wrapper around the descriptors that is returned to the
     * users.
     */
    private final Set unmodifiableSupportedPropertyIdentifiers;

    /**
     * A list of {@link SynchronizationGroup}.
     */
    private List synchronizedPropertiesList;

    /**
     * A flag to indicate whether events should be suppressed when the
     * corresponding property is disabled.
     */
    private boolean suppressEventsWhenDisabled = false;

    /**
     * A map from either {@link PropertyIdentifier} or
     * {@link SynchronizationGroup} to {@link PropertyControls}.
     */
    private Map allPropertyControls = new HashMap();

    /**
     * Create a new properties composite.
     *
     * @param parent The parent GUI component
     * @param style The SWT style to apply
     * @param context The editor context for the current editor
     */
    public PropertiesComposite(Composite parent, int style,
                               EditorContext context) {
        this(parent, style, null, context, false, null, null);
    }

    /**
     * Create a new properties composite.
     *
     * @param parent The parent GUI component
     * @param style The SWT style to apply
     * @param properties An array of property descriptors to render/edit
     * @param context The editor context for the current editor
     * @param synchronizable True if the editor contains a series of properties
     *                       of the same type that can be edited as one by a
     *                       single editor.
     *
     * @see #addProperties
     */
    public PropertiesComposite(Composite parent, int style,
                               PropertyDescriptor[] properties,
                               EditorContext context, boolean synchronizable) {
        this(parent, style, properties, context, synchronizable, null, null);
    }

    /**
     * Create a new properties composite.
     *
     * @param parent The parent GUI component
     * @param style The SWT style to apply
     * @param properties An array of property descriptors to render/edit
     * @param context The editor context for the current editor
     * @param synchronizable True if the editor contains a series of properties
     *                       of the same type that can be edited as one by a
     *
     * @see #addProperties
     */
    public PropertiesComposite(Composite parent, int style,
                               PropertyDescriptor[] properties,
                               EditorContext context, boolean synchronizable,
                               Map comboDescriptors, Map policyTypes) {
        super(parent, style);

        this.context = context;
        this.supportedPropertyIdentifiers = new HashSet();
        unmodifiableSupportedPropertyIdentifiers =
                Collections.unmodifiableSet(supportedPropertyIdentifiers);

        GridLayout layout = new GridLayout(2, false);
        layout.makeColumnsEqualWidth = false;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);

        if (properties != null) {
            addProperties(properties, synchronizable, comboDescriptors,
                    policyTypes);

            setEnabled(false);
        }
    }

    /**
     * Specifies whether event propagation should be suppressed when the
     * properties composite or the specific property is disabled.
     *
     * @param suppress True to suppress events, false otherwise
     */
    public void setSuppressEventsWhenDisabled(boolean suppress) {
        suppressEventsWhenDisabled = suppress;
    }

    /**
     * Add a group of properties to this composite.
     *
     * <p>Adding the properties causes the appropriate controls to be created
     * and added to the end of this composite.</p>
     *
     * <p>If the synchronizable flag is true then this group of properties must
     * all be of the same type. An additional set of controls is created
     * associated with this group of properties. When edited they cause all the
     * properties to be set to the value that is entered.</p>
     *
     * <p>At the moment the only type of property that can be synchronized are
     * StyleValues.</p>
     *
     * <p>If a property can have one of a fixed number of values and the user
     * interface must be represented as an uneditable combo box then a
     * {@link ComboDescriptor} needs to be added to the comboDescriptors
     * map for that property.</p>
     *
     * <p>If a property is a {@link PolicyReference} and can have a single
     * policy type then the allowable policy type should be added for to the
     * policyTypes map for that property.</p>
     *
     * @param properties The group of properties.
     * @param synchronizable The synchronizable flag.
     * @param comboDescriptors A map from {@link PropertyIdentifier} to
     * {@link ComboDescriptor}.
     * @param policyTypes A map from {@link PropertyIdentifier} to {@link PolicyType}.
     */
    public void addProperties(
            PropertyDescriptor[] properties, boolean synchronizable,
            Map comboDescriptors, Map policyTypes) {

        // Can't synchronize with less than two properties
        if (properties.length < 2) {
            synchronizable = false;
        }

        SynchronizationGroup synchronizationGroup = null;
        if (synchronizable) {
            synchronizationGroup = new SynchronizationGroup(properties);

            if (synchronizedPropertiesList == null) {
                synchronizedPropertiesList = new ArrayList();
            }
            synchronizedPropertiesList.add(synchronizationGroup);

            // Add the synchronizing control (based on the first property -
            // all properties must be the same type for a category to be
            // synchronized, so this is a reasonable assumption).
            addSynchronizingControl(properties[0], synchronizationGroup);
        }

        for (int i = 0; i < properties.length; i++) {
            addProperty(properties[i], comboDescriptors, policyTypes, synchronizationGroup);
        }
    }

    /**
     * Set a specified property if it exists. If this composite does not
     * contain the property requested, nothing is done.
     *
     * @param property The property to set
     * @param value The new value
     */
    public void setProperty(PropertyIdentifier property, Object value) {
        PropertyAccessor accessor = (PropertyAccessor) accessors.get(property);
        if (accessor != null) {
            accessor.setPropertyValue(value);
            // todo update synchronization stuff.
        }
    }

    /**
     * Retrieve the value of a specified property. If this composite does not
     * contain the property requested, null is returned.
     *
     * <p>Note that returning null does not necessarily mean the property is
     * not in the composite - it is possible for a property to have a null
     * value.</p>
     *
     * @param property The property being requested
     * @return The value of that property, or null if the property is not present
     */
    public Object getProperty(PropertyIdentifier property) {
        Object propertyValue = null;
        PropertyAccessor accessor = (PropertyAccessor) accessors.get(property);
        if (accessor != null) {
            propertyValue = accessor.getPropertyValue();
        }
        return propertyValue;
    }

    /**
     * Retrieves a set containing the properties supported by this composite.
     *
     * @return A set containing the properties supported by this composite
     */
    public Set getSupportedPropertyIdentifiers() {
        return unmodifiableSupportedPropertyIdentifiers;
    }

    /**
     * Adds the synchronizing control.
     *
     * <p>The synchronizing control is the control that when edited causes
     * all the other properties in the specified set to be set to the same
     * value.</p>
     *
     * @param descriptor            The descriptor of the synchronizing property.
     * @param synchronizationGroup The set of property descriptors that are
     */
    private void addSynchronizingControl(
            PropertyDescriptor descriptor,
            SynchronizationGroup synchronizationGroup) {

        Class propertyType = descriptor.getPropertyType().getTypeClass();
        if (PropertyValue.class == propertyType) {
            addStyleValue(descriptor, synchronizationGroup, true);
        } else {
            throw new UnsupportedOperationException(
                    "Synchronisation is not supported for property type '" +
                    propertyType + "'");
        }
    }

    /**
     * Adds the structure for a single specified property.
     *
     * @param descriptor The descriptor of the property to add
     * @param comboDescriptors
     * @param policyTypes
     * @param synchronizationGroup Used for style properties,
     * 							   can be null for not synchronized properties
     * 
     */
    private void addProperty(
            PropertyDescriptor descriptor,
            final Map comboDescriptors, 
            final Map policyTypes, 
            final SynchronizationGroup synchronizationGroup) {

        supportedPropertyIdentifiers.add(descriptor.getIdentifier());

        if (comboDescriptors != null && comboDescriptors.get(descriptor.getIdentifier()) != null) {
            ComboDescriptor combo = (ComboDescriptor) comboDescriptors.get(descriptor.getIdentifier());
            addCombo(descriptor,  combo.getValues(), combo.getLabelProvider());
        } else {
            Class propertyType = descriptor.getPropertyType().getTypeClass();
            if (Boolean.class == propertyType || boolean.class == propertyType) {
                addCheckbox(descriptor);
            } else if (PropertyValue.class == propertyType) {
                addStyleValue(descriptor, synchronizationGroup, false);
            } else if (Integer.class == propertyType || Integer.TYPE == propertyType) {
                addInteger(descriptor);
            } else if (PolicyReference.class == propertyType) {
                PolicyType policyType = null;
                if (policyTypes != null) {
                    policyType = (PolicyType) policyTypes.get(descriptor.getIdentifier());
                }
                addPolicyReference(descriptor, policyType);
            } else {
                addText(descriptor);
            }
        }
    }

    /**
     * Adds the structure for a single specified property to be rendered as a
     * checkbox.
     *
     * @param descriptor The descriptor for the property to add
     */
    private void addCheckbox(final PropertyDescriptor descriptor) {

        PropertyIdentifier identifier = descriptor.getIdentifier();

        final Button checkBox = new Button(this, SWT.CHECK);

        GridData data = new GridData();
        data.horizontalSpan = 2;
        checkBox.setLayoutData(data);

        checkBox.setText(getLabel(descriptor));

        checkBox.addSelectionListener(
            new SelectionListener() {
                public void widgetSelected(SelectionEvent event) {
                    propertyChanged(descriptor,
                            Boolean.valueOf(checkBox.getSelection()));
                }

                public void widgetDefaultSelected(SelectionEvent event) {
                    propertyChanged(descriptor,
                            Boolean.valueOf(checkBox.getSelection()));
                }
            }
        );

        // Remember the controls that are used for this property.
        addControls(identifier, checkBox, Collections.EMPTY_LIST);

        accessors.put(identifier, new PropertyAccessor() {
            public Object getPropertyValue() {
                return Boolean.valueOf(checkBox.getSelection());
            }

            public void setPropertyValue(Object newValue) {
                boolean selected = newValue != null &&
                        ((Boolean) newValue).booleanValue();
                checkBox.setSelection(selected);
            }
        });
    }

    /**
     * Remember the controls that are used for each property.
     *
     * @param key           The key.
     * @param activeControl The active control, i.e. the one with which the
     *                      user interacts.
     * @param otherControls Any other controls that need to be enabled, or have
     *                      their background colour set.
     */
    private void addControls(Object key, Control activeControl,
                             List otherControls) {

        allPropertyControls.put(key,
                new PropertyControls(activeControl, otherControls));
    }

    /**
     * Remember the controls that are used for each property.
     *
     * @param key           The key.
     * @param activeControl The active control, i.e. the one with which the
     *                      user interacts.
     * @param label         The label that needs to be enabled, or have its
     *                      background colour set.
     */
    private void addControls(
            Object key, Control activeControl, Control label) {
        addControls(key, activeControl, Collections.singletonList(label));
    }

    /**
     * Adds the structure for a single specified property to be rendered as a
     * string.
     *
     * @param descriptor The descriptor for the property to add
     */
    private void addText(final PropertyDescriptor descriptor) {

        PropertyIdentifier identifier = descriptor.getIdentifier();
        final Text text = addTextBasedProperty(descriptor);

        text.addModifyListener(
            new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    propertyChanged(descriptor, text.getText());
                }
            }
        );

        accessors.put(identifier, new PropertyAccessor() {
            public Object getPropertyValue() {
                return text.getText();
            }

            public void setPropertyValue(Object newValue) {
                if (newValue == null) {
                    newValue = "";
                }
                text.setText(newValue.toString());
            }
        });
    }

    /**
     * Add a property that is based on a Text edit field.
     *
     * @param descriptor The property descriptor.
     */
    private Text addTextBasedProperty(
            final PropertyDescriptor descriptor) {

        PropertyIdentifier identifier = descriptor.getIdentifier();
        Label label = new Label(this, SWT.NONE);
        label.setText(getLabel(descriptor));

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        label.setLayoutData(data);

        Composite textParent = this;
        String suffixLabel = getSuffix(descriptor);

        List otherControls = new ArrayList();
        otherControls.add(label);

        if (suffixLabel != null) {
            textParent = new Composite(this, SWT.NORMAL);
            textParent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            GridLayout layout = new GridLayout(2, false);
            layout.marginHeight = 0;
            layout.marginWidth = 0;
            textParent.setLayout(layout);

            otherControls.add(textParent);
        }

        final Text text = new Text(textParent, SWT.BORDER | SWT.SINGLE);

        data = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(data);

        if (suffixLabel != null) {
            Label suffix = new Label(textParent, SWT.NONE);
            setDefaultColour(suffix);
            suffix.setText(suffixLabel);
            data = new GridData(GridData.HORIZONTAL_ALIGN_END);
            suffix.setLayoutData(data);

            otherControls.add(suffix);
        }

        addControls(identifier, text, otherControls);

        return text;
    }

    /**
     * Adds the structure for a single specified property to be rendered as a
     * non-editable combo selecting from a list of options.
     *
     * @param descriptor The descriptor for the property to add
     * @param comboValues The possible values to display
     * @param labelProvider A label provider for rendering the options
     */
    private void addCombo(final PropertyDescriptor descriptor,
                          final List comboValues,
                          ILabelProvider labelProvider) {

        PropertyIdentifier identifier = descriptor.getIdentifier();
        Label label = new Label(this, SWT.NONE);
        label.setText(getLabel(descriptor));

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        label.setLayoutData(data);

        final ComboViewer combo = new ComboViewer(this, SWT.BORDER | SWT.READ_ONLY);
        combo.setLabelProvider(labelProvider);
        combo.setContentProvider(new ArrayContentProvider());
        combo.setInput(comboValues);

        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        combo.getControl().setLayoutData(data);

        combo.addSelectionChangedListener(
                new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                        propertyChanged(descriptor, selection.getFirstElement());
                    }
                });

        addControls(identifier, combo.getControl(), label);

        accessors.put(identifier, new PropertyAccessor() {
            public Object getPropertyValue() {
                IStructuredSelection selection = (IStructuredSelection) combo.getSelection();
                return selection.getFirstElement();
            }

            public void setPropertyValue(final Object newValue) {
                IStructuredSelection selection;
                if (newValue == null) {
                    selection = new StructuredSelection();
                } else {
                    selection = new StructuredSelection(newValue);
                }

                combo.setSelection(selection);
            }
        });
    }

    /**
     * Adds the structure for a single specified property to be rendered as a
     * non-editable combo selecting from a list of options.
     *
     * @param descriptor The descriptor for the property to add
     * @param policyType The type of policy to add
     */
    private void addPolicyReference(
            final PropertyDescriptor descriptor,
            final PolicyType policyType) {

        PropertyIdentifier identifier = descriptor.getIdentifier();
        Label label = new Label(this, SWT.NONE);
        label.setText(getLabel(descriptor));

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        label.setLayoutData(data);

        final PolicyReferenceEditor editor = new PolicyReferenceEditor(
                this, SWT.NONE, policyType, context);
        setDefaultColour(editor);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        editor.setLayoutData(data);


        editor.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                PolicyReference newValue = editor.getValue();
                propertyChanged(descriptor, newValue);
            }
        });

        // Remember the controls that are used for this property.
        addControls(identifier, editor, label);

        accessors.put(identifier, new PropertyAccessor() {
            public Object getPropertyValue() {
                return editor.getValue();
            }

            public void setPropertyValue(final Object newValue) {
                if (newValue instanceof PolicyReference) {
                    editor.setValue((PolicyReference) newValue);
                } else {
                    editor.setValue(null);
                }
            }
        });
    }


    /**
     * Adds the structure for a single specified property to be rendered as an
     * integer.
     *
     * @param descriptor The descriptor for the property to add
     */
    private void addInteger(final PropertyDescriptor descriptor) {

        final PropertyIdentifier identifier = descriptor.getIdentifier();
        final Text text = addTextBasedProperty(descriptor);

        text.addModifyListener(
            new ModifyListener() {
                public void modifyText(ModifyEvent event) {
                    propertyChanged(descriptor, getProperty(identifier));
                }
            }
        );

        // Use a verify listener to make sure only valid digits are entered.
        text.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent event) {
                if (event.text != null) {
                    boolean validInteger = true;
                    for (int i = 0; i < event.text.length(); i++) {
                        char nextChar = event.text.charAt(i);
                        if (DIGITS.indexOf(nextChar) == -1) {
                            validInteger = false;
                        }
                    }

                    if (!validInteger) {
                        event.doit = false;
                    }
                }
            }
        });

        accessors.put(identifier, new PropertyAccessor() {
            public Object getPropertyValue() {
                Integer value = null;
                try {
                    value = new Integer(text.getText());
                } catch (NumberFormatException nfe) {
                    // If the number format was invalid, we return null to
                    // indicate no valid number.
                }
                return value;
            }

            public void setPropertyValue(Object newValue) {
                if (newValue == null) {
                    newValue = "";
                }

                text.setText(newValue.toString());
            }
        });
    }

    /**
     * Adds the structure for a single specified property to be rendered as a
     * style value.
     *
     * @param descriptor               The descriptor for the property to add
     * @param synchronizationGroup     The set of descriptors to update, null if this is not
     * @param isSpecialSynchronization When set to true special type of synchronization is used.
     */
    private void addStyleValue(final PropertyDescriptor descriptor,
                               SynchronizationGroup synchronizationGroup,
                               boolean isSpecialSynchronization) {
    	
        PropertyIdentifier identifier = descriptor.getIdentifier();
        final StyleProperty styleProperty =
                ThemeModel.getStylePropertyForPropertyIdentifier(identifier);        
        if (StylePropertyMetadata.editAsPair(styleProperty)) {
            if (synchronizationGroup != null) {
                throw new UnsupportedOperationException(
                        "Synchronisation is not supported for style pairs.");
            }

            addPairStyleValue(descriptor, styleProperty);
        } else if (StylePropertyMetadata.editAsFraction(styleProperty)){
            addFractionStyleValue(descriptor, styleProperty);
        } else {
            addSimpleStyleValue(descriptor, styleProperty,
                    synchronizationGroup, isSpecialSynchronization);
        }
    }

    /**
     * Adds the structure for a single specified property to be rendered as a
     * style pair.
     *
     * @param descriptor        The descriptor for the property to add
     * @param styleProperty     property for which to add a pair editor
     */
    private void addPairStyleValue(final PropertyDescriptor descriptor,
                                   final StyleProperty styleProperty) {

        // Create the property label and horizontal divider.
        Composite labelContainer = new Composite(this, SWT.NONE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        labelContainer.setLayoutData(data);
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        labelContainer.setLayout(layout);
        Label label = new Label(labelContainer, SWT.NONE);
        label.setText(getLabel(descriptor));
        data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        label.setLayoutData(data);
        Label divider = new Label(labelContainer, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(GridData.FILL_HORIZONTAL);
        divider.setLayoutData(data);
        setDefaultColour(label);
        setDefaultColour(labelContainer);
        setDefaultColour(divider);

        List otherControls = new ArrayList();
        otherControls.add(label);
        otherControls.add(divider);

        // Find the pair type component of the supported structure.
        StyleType type = styleProperty.getStandardDetails().getSupportedStructure();
        StylePairType pairType = null;
        if (type instanceof StylePairType) {
            pairType = (StylePairType) type;
        } else if (type instanceof StyleChoiceType) {
            StyleChoiceType choice = (StyleChoiceType) type;
            List types = choice.getTypes();
            Iterator it = types.iterator();
            while (pairType == null && it.hasNext()) {
                StyleType next = (StyleType) it.next();
                if (next instanceof StylePairType) {
                    pairType = (StylePairType) next;
                }
            }
        }

        // Create an editor for the pairType.
        if (pairType == null) {
            throw new IllegalStateException("Can't make pair editor for " +
                    "non-pair type");
        } else {
            PropertyIdentifier identifier = descriptor.getIdentifier();
            String firstName = EditorMessages.getString(RESOURCE_PREFIX +
                    identifier.getName() + ".first.label");
            String secondName = EditorMessages.getString(RESOURCE_PREFIX +
                    identifier.getName() + ".second.label");

            final StylePairEditor styleEditor = new StylePairEditor(this,
                    SWT.NONE, styleProperty, pairType, true, context,
                    firstName, secondName);

            data = new GridData(GridData.FILL_HORIZONTAL |
                    GridData.GRAB_HORIZONTAL);
            data.verticalSpan = 2;
            data.horizontalSpan = 2;
            styleEditor.setLayoutData(data);

            // Add the accessors for the values represented by this pair editor.
            accessors.put(identifier, new PropertyAccessor() {
                public Object getPropertyValue() {
                    String textValue = styleEditor.getStringValue();
                    final EditorPropertyParser parser = new EditorPropertyParser();
                    return parser.parsePropertyValue(styleProperty, textValue,
                            styleEditor.isImportant());
                }

                public void setPropertyValue(Object newValue) {
                    if (newValue instanceof PropertyValue) {
                        styleEditor.setValue((PropertyValue) newValue);
                    } else {
                        styleEditor.setValue(null);
                    }
                }
            });

            // Add a modification listener.
            styleEditor.addModifyListener(
                new ModifyListener() {
                    public void modifyText(ModifyEvent event) {
                        propertyChanged(descriptor, styleEditor.getValues());
                    }
                }
            );

            // Update the list of controls managed by this composite.
            addControls(identifier, styleEditor, otherControls);
        }
    }

    /**
     * Adds the structure for a single specified property to be rendered as a
     * style fraction.
     *
     * @param descriptor The descriptor for the property to add
     * @param styleProperty
     */
    private void addFractionStyleValue(final PropertyDescriptor descriptor,
                                       final StyleProperty styleProperty) {

        // Create the property label and add it to the otherControls list
        Label label = new Label(this, SWT.NONE);
        label.setText(getLabel(descriptor));
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        label.setLayoutData(data);
        setDefaultColour(label);

        List otherControls = new ArrayList();
        otherControls.add(label);

        // StyleFractions are displayed using a StylePairEditor, which are
        // composed of two StyleValueEditors. The first editor is used to
        // display all the allowable values for this property, but the
        // second is only enabled if representing a StyleFraction. For
        // example, mcs-marquee-speed can be a length, keyword, fraction or
        // inherit. The first editor can display either a length, keyword,
        // inherit or the numerator of the fraction, but the second can
        // only display the denominator of the fraction.
        StyleType type = styleProperty.getStandardDetails().
                getSupportedStructure();
        StyleFractionType fractionType = null;
        if (type instanceof StyleFractionType) {
            fractionType = (StyleFractionType) type;
        } else if (type instanceof StyleChoiceType) {
            StyleChoiceType choice = (StyleChoiceType) type;
            List types = choice.getTypes();
            Iterator it = types.iterator();
            while (fractionType == null && it.hasNext()) {
                StyleType next = (StyleType) it.next();
                if (next instanceof StyleFractionType) {
                    fractionType = (StyleFractionType) next;
                }
            }
        }

        // Create the editor for this fraction type.
        if (fractionType == null) {
            throw new IllegalStateException("Can't make fraction editor for " +
                    "non-fraction type");
        } else {
            PropertyIdentifier identifier = descriptor.getIdentifier();

            // We pass the fraction type in as the second type (rather than the
            // denominator type) to guarantee that we are editing a fraction.
            final StylePairEditor styleEditor = new StylePairEditor(this,
                    SWT.NONE, styleProperty, type, fractionType, true, context);

            // Add accessors for the values which can be edited by this editor.
            accessors.put(identifier, new PropertyAccessor() {
                public Object getPropertyValue() {
                    return styleEditor.getPropertyValue();
                }

                public void setPropertyValue(Object newValue) {
                    if (newValue instanceof PropertyValue) {
                        styleEditor.setValue((PropertyValue) newValue);
                    } else {
                        styleEditor.setValue(null);
                    }
                }
            });

            // Plug in a change listener.
            styleEditor.addModifyListener(
                new ModifyListener() {
                    public void modifyText(ModifyEvent event) {
                        propertyChanged(descriptor, styleEditor.getValues());
                    }
                }
            );
            // Update the list of controls managed by this composite.
            addControls(identifier, styleEditor, otherControls);
        }
    }

    /**
     * Adds the structure for a single specified property to be rendered as a
     * simple (non-pair) style value.
     *
     * @param descriptor The descriptor for the property to add
     */
    private void addSimpleStyleValue(
            final PropertyDescriptor descriptor,
            final StyleProperty styleProperty,
            final SynchronizationGroup synchronizationGroup,
            final boolean isSpecialSynchronization) {
    	
        Label label = new Label(this, SWT.NONE);

        if (isSpecialSynchronization) {
            label.setText(EditorMessages.getString(RESOURCE_PREFIX + "all.label"));            
        } else {
            label.setText(getLabel(descriptor));
        }

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        label.setLayoutData(data);

        StylePropertyBrowseAction browseAction = StylePropertyMetadata.getBrowseAction(styleProperty);

        StyleType supportedStructure = styleProperty.getStandardDetails().
                getSupportedStructure();
        final StyleValueEditor styleEditor = new StyleValueEditor(this,
                SWT.NONE, styleProperty, supportedStructure, true,
                browseAction, context);

        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        styleEditor.setLayoutData(data);

        if (isSpecialSynchronization) {
			// Special synchronization
			if (synchronizationGroup != null) {
				// Setting up a modify listener to update the properties
				// if the synchronization control is set
				styleEditor.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						PropertyAccessor synchAccessor = (PropertyAccessor) accessors
								.get(synchronizationGroup);
						Object value = synchAccessor.getPropertyValue();
						PropertyDescriptor[] controlledProperties = synchronizationGroup
								.getControlledDescriptors();
						for (int i = 0; i < controlledProperties.length; i++) {
							PropertyIdentifier identifier = controlledProperties[i]
									.getIdentifier();
							PropertyAccessor accessor = (PropertyAccessor) accessors
									.get(identifier);
							accessor.setPropertyValue(value);
							propertyChanged(controlledProperties[i], value);
						}
					}
				});
			} else {
				// Synchronization group must not be null
				// when isSpecialSynchronization is set to true
				throw new IllegalArgumentException(
						"Synchronization group for special synchronization must not be null");
			}
		} else {
			// Normal synchronization
			if (synchronizationGroup != null) {
				// Setting up a modify listener to update the synchronization
				// control if one of the synchronized properties changes
				styleEditor.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						propertyChanged(descriptor, styleEditor.getValue());
						updateSynchControl(synchronizationGroup);
					}
				});
			} else {
				// Setting up a normal modify listener
				styleEditor.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						propertyChanged(descriptor, styleEditor.getValue());
					}
				});
			}
		}

        final Object key = isSpecialSynchronization ?
                (Object) synchronizationGroup : descriptor.getIdentifier();

        addControls(key, styleEditor, label);

        accessors.put(key, new PropertyAccessor() {
            public Object getPropertyValue() {
                String textValue = styleEditor.getValue();
                final EditorPropertyParser parser = new EditorPropertyParser();
                return parser.parsePropertyValue(styleProperty, textValue,
                        styleEditor.isImportant());
            }

            public void setPropertyValue(Object newValue) {
                if (newValue instanceof PropertyValue) {
                    styleEditor.setPropertyValue((PropertyValue) newValue);
                } else {
                    styleEditor.setPropertyValue(null);
                }
            }
        });
    }

    /**
	 * Returns the label text for a specified property.
	 * 
	 * @param descriptor
	 *            The descriptor of the property being queried
	 * @return The label text for the specified property
	 */
    private String getLabel(PropertyDescriptor descriptor) {
        return EditorMessages.getString(RESOURCE_PREFIX +
                descriptor.getIdentifier().getName() + ".label");
    }

    /**
     * Returns the suffix text for a specified property.
     *
     * @param descriptor The descriptor of the property being queried
     * @return The label text for the specified property
     */
    private String getSuffix(PropertyDescriptor descriptor) {
        String suffix = null;
        try {
            suffix = EditorMessages.getString(RESOURCE_PREFIX +
                    descriptor.getIdentifier().getName() + ".suffix");
        } catch (MissingResourceException mre) {
            // No suffix is defined for this property - ignore it silently, and
            // no suffix will be displayed. Although using exceptions as part of
            // the normal flow of execution is Bad and Wrong, there's no easy
            // way to check whether a resource exists with a bundle, and since
            // this is a one-off check on creation of a composite, the cost of
            // this approach should be limited.
        }
        return suffix;
    }

    // Javadoc inherited
    public void setBackground(Color color) {
        super.setBackground(color);

        Iterator it = allPropertyControls.values().iterator();
        while (it.hasNext()) {
            ((PropertyControls) it.next()).setBackground(color);
        }
    }

    // Javadoc inherited
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        Iterator it = allPropertyControls.values().iterator();
        while (it.hasNext()) {
            ((PropertyControls) it.next()).setEnabled(enabled);
        }
    }

    /**
     * Set the enabled state of the controls for a property.
     *
     * <p>If the control is disabled then it is cleared.</p>
     *
     * @param identifier The property identifier.
     * @param enabled    The enabled flag.
     */
    public void setPropertyEnabled(
            PropertyIdentifier identifier, boolean enabled) {

        PropertyControls propertyControls = (PropertyControls)
                allPropertyControls.get(identifier);
        propertyControls.setEnabled(enabled);

        if (!enabled) {
            setProperty(identifier, null);
        }
    }

    /**
     * Register a properties composite change listener with this properties
     * composite.
     *
     * @param listener The listener to add
     */
    public void addPropertiesCompositeChangeListener(
            PropertiesCompositeChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Unregister a properties composite change listener from this properties
     * composite.
     *
     * @param listener The listener to remove
     */
    public void removePropertiesCompositeChangeListener(
            PropertiesCompositeChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notify listeners that a property has changed.
     *
     * @param descriptor The property that has changed
     * @param newValue The new value
     */
    private void propertyChanged(PropertyDescriptor descriptor,
                                 Object newValue) {
        // Propagate the event unless we're suppressing events for disabled
        // events and either the composite or this specific property is
        // disabled.
        boolean propagateEvent = true;
        if (suppressEventsWhenDisabled) {
            if (!isEnabled()) {
                propagateEvent = false;
            } else {
                PropertyControls controls = (PropertyControls)
                        allPropertyControls.get(descriptor.getIdentifier());
                propagateEvent = controls.isEnabled();
            }
        }

        if (propagateEvent) {
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                ((PropertiesCompositeChangeListener) it.next()).
                        propertyChanged(this, descriptor, newValue);
            }
        }
    }

    /**
     * Set all properties to null.
     */
    public void clear() {
        Iterator it = accessors.values().iterator();
        while (it.hasNext()) {
            PropertyAccessor accessor = (PropertyAccessor) it.next();
            accessor.setPropertyValue(null);
        }
    }

    /**
     * Set the focus for a specified property.
     *
     * @param identifier The identifier of the property to be selected
     */
    public void selectProperty(PropertyIdentifier identifier) {
        Control control = getActiveControl(identifier);
        control.setFocus();
    }

    /**
     * Type unsafe method for accessing the active control for either a
     * property, or a synchronization group.
     *
     * @param key The key to use.
     * @return The active control.
     */
    private Control getActiveControlUnsafe(Object key) {
        PropertyControls controls = (PropertyControls)
                allPropertyControls.get(key);
        Control active = controls.getActiveControl();
        return active;
    }

    /**
     * Type safe wrapper around {@link #getActiveControlUnsafe(Object)}.
     *
     * @param identifier The property identifier.
     * @return The active control.
     */
    private Control getActiveControl(PropertyIdentifier identifier) {
        return getActiveControlUnsafe(identifier);
    }

    /**
     * Type safe wrapper around {@link #getActiveControlUnsafe(Object)}.
     *
     * @param synchronizationGroup The synchronized properties.
     * @return The active control.
     */
    private Control getActiveControl(SynchronizationGroup synchronizationGroup) {
        return getActiveControlUnsafe(synchronizationGroup);
    }

    /**
     * Update the value of the synchronisation control if necessary.
     */
    private void updateSynchControl(SynchronizationGroup synchronizationGroup) {
        StyleValueEditor synchControl = (StyleValueEditor)
                getActiveControl(synchronizationGroup);
        if (synchControl != null) {
            PropertyDescriptor[] descriptors =
                    synchronizationGroup.getControlledDescriptors();
            StyleValueEditor firstControl = (StyleValueEditor)
                    getActiveControl(descriptors[0].getIdentifier());
            String firstValue = firstControl.getValue();
            boolean valuesEqual = true;
            for (int i = 1; valuesEqual && i < descriptors.length; i++) {
                StyleValueEditor control = (StyleValueEditor)
                        getActiveControl(descriptors[i].getIdentifier());
                if (!firstValue.equals(control.getValue()) ||
                        firstControl.isImportant() != control.isImportant()) {
                    valuesEqual = false;
                }
            }

            PropertyAccessor synchronizingAccessor = (PropertyAccessor)
                    accessors.get(synchronizationGroup);

            if (valuesEqual) {
                Object value = ((PropertyAccessor) accessors.get(descriptors[0].getIdentifier())).getPropertyValue();
                synchronizingAccessor.setPropertyValue(value);
            } else {
                synchronizingAccessor.setPropertyValue(null);
            }
        }
    }

    /**
     * Sets the colour of a control to the default background colour for the
     * GUI (the list background colour).
     *
     * @param control The control to set the colour for
     */
    private void setDefaultColour(Control control) {
        control.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    }

    /**
     * Copies the properties from the proxy object to this composite.
     *
     * <p>All properties supported by the composite will be copied.</p>
     *
     * @param proxy The proxy from which the properties will be copied
     */
    public void updateFromProxy(BeanProxy proxy) {
        Iterator properties = getSupportedPropertyIdentifiers().iterator();
        while (properties.hasNext()) {
            PropertyIdentifier property = (PropertyIdentifier) properties.next();
            Proxy propertyProxy = proxy.getPropertyProxy(property);
            Object propertyValue;
            if (propertyProxy == null) {
                propertyValue = null;
            } else {
                propertyValue = propertyProxy.getModelObject();
            }

            PropertyAccessor accessor = (PropertyAccessor) accessors.get(property);
            if (accessor != null) {
                accessor.setPropertyValue(propertyValue);
            }
        }

        // Now iterate over the synchronized properties and update the
        // synchronizing control.
        if (synchronizedPropertiesList != null) {
            for (Iterator i = synchronizedPropertiesList.iterator();
                 i.hasNext();) {

                SynchronizationGroup synchronizationGroup =
                        (SynchronizationGroup) i.next();
                updateSynchControl(synchronizationGroup);
            }
        }
    }

    /**
     * Encapsulates information about properties that are synchronized.
     */
    private static class SynchronizationGroup {

        private final PropertyDescriptor[] controlledDescriptors;

        /**
         * Initialise.
         *
         * @param controlledDescriptors The set of descriptors that will be
         *                              updated by the synchronizing control.
         */
        public SynchronizationGroup(PropertyDescriptor[] controlledDescriptors) {
            this.controlledDescriptors = controlledDescriptors;
        }

        /**
         * Get the set of descriptors belong to this synchronization group.
         * @return The set of descriptors belong to this group.
         */
        public PropertyDescriptor[] getControlledDescriptors() {
            return controlledDescriptors;
        }
    }

    /**
     * Encapsulates the set of controls that are used for a particular
     * property.
     */
    private class PropertyControls {

        /**
         * The active control for the property.
         */
        private final Control activeControl;

        /**
         * Any other controls.
         */
        private final List otherControls;

        /**
         * Initialise.
         *
         * @param activeControl The active control.
         * @param otherControls The list of other controls.
         */
        public PropertyControls(Control activeControl, List otherControls) {
            if (activeControl == null) {
                throw new IllegalArgumentException("activeControl cannot be null");
            }
            if (otherControls == null) {
                throw new IllegalArgumentException("otherControls cannot be null");
            }

            this.activeControl = activeControl;
            this.otherControls = otherControls;
        }

        /**
         * Set the enabled state of all controls.
         *
         * @param enabled The enabled state.
         */
        public void setEnabled(boolean enabled) {
            activeControl.setEnabled(enabled);
            for (Iterator i = otherControls.iterator(); i.hasNext();) {
                Control control = (Control) i.next();
                control.setEnabled(enabled);
            }
        }

        /**
         * Check the enabled state of the control (assumed to be the same as
         * the state for the active control).
         */
        public boolean isEnabled() {
            return activeControl.isEnabled();
        }

        /**
         * Set the background color of all controls.
         *
         * @param color The background color.
         */
        public void setBackground(Color color) {
            activeControl.setBackground(color);
            for (Iterator i = otherControls.iterator(); i.hasNext();) {
                Control control = (Control) i.next();
                control.setBackground(color);
            }
        }

        /**
         * Get the active control.
         *
         * @return The active control.
         */
        public Control getActiveControl() {
            return activeControl;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/3	adrianj	VBM:2005111601 Add style rule view

 06-Dec-05	10652/1	adrianj	VBM:2005112110 Add context menu for StyleCategoriesComposite

 06-Dec-05	10625/3	adrianj	VBM:2005112110 Support synchronizable categories

 06-Dec-05	10625/1	adrianj	VBM:2005112110 Support synchronizable categories

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 01-Dec-05	10512/1	pduffin	Quick commit for GUI fixes

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 09-Nov-05	10197/3	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 10-Nov-05	10246/1	adrianj	VBM:2005110434 Allow user-friendly data entry for style properties

 09-Nov-05	10197/3	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9992/5	emma	VBM:2005101811 Adding new style property validation

 31-Oct-05	9886/8	adrianj	VBM:2005101811 New theme GUI

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 31-Oct-05	9886/6	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/4	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
