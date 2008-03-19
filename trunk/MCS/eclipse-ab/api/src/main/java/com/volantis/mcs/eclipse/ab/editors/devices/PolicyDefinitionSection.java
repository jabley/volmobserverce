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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyTypeComposition;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.ObservableProperties;
import com.volantis.mcs.eclipse.common.PresentableItem;
import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.eclipse.controls.ComboViewer;
import com.volantis.mcs.eclipse.controls.ListValueBuilder;
import com.volantis.mcs.eclipse.controls.TextActionable;
import com.volantis.mcs.eclipse.controls.ComboActionable;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Element;
import org.jdom.Namespace;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

/**
 * Form section that allows a policy definition to be editied
 */
public class PolicyDefinitionSection extends FormSection {

    /**
     * The prefix for resource messages associated with this class.
     */
    private static final String RESOURCE_PREFIX =
                "PolicyDefinitionSection."; //$NON-NLS-1$

   /**
     * Constant used as the title for this form section
     */
    private static final String TITLE =
               DevicesMessages.getString(RESOURCE_PREFIX + "title");

    /**
     * Constant used as the message for this form section
     */
    private static final String MESSAGE =
               DevicesMessages.getString(RESOURCE_PREFIX + "message");

    /**
     * Label for the short name control
     */
    private static final String SHORT_NAME_LABEL =
                DevicesMessages.getString(RESOURCE_PREFIX + "shortName.label");

    /**
     * Label for the name control
     */
    private static final String NAME_LABEL =
                DevicesMessages.getString(RESOURCE_PREFIX + "name.label");

    /**
     * Label for the help text control
     */
    private static final String HELP_LABEL =
                DevicesMessages.getString(RESOURCE_PREFIX + "help.label");

    /**
     * Label for the help text control
     */
    private static final int HELP_WIDTH = DevicesMessages.getInteger(
            RESOURCE_PREFIX + "help.width").intValue();

    /**
     * Label for the composition control
     */
    private static final String COMPOSITION_LABEL =
                DevicesMessages.getString(RESOURCE_PREFIX +
                                          "composition.label");

    /**
     * Label for the type control
     */
    private static final String TYPE_LABEL =
                DevicesMessages.getString(RESOURCE_PREFIX + "type.label");

    /**
     * Label for the selection values control
     */
    private static final String SELECTION_VALUES_LABEL =
                DevicesMessages.getString(RESOURCE_PREFIX +
                                          "selectionValues.label");

    /**
     * Label for the rower range control
     */
    private static final String LOWER_RANGE_LABEL =
                DevicesMessages.getString(RESOURCE_PREFIX +
                                          "lowerRange.label");

    /**
     * Label for the upper range control
     */
    private static final String UPPER_RANGE_LABEL =
                DevicesMessages.getString(RESOURCE_PREFIX +
                                          "upperRange.label");

    /**
     * The <code>DeviceEditorContext</code> required by this editor part
     */
    private DeviceEditorContext context;

    /**
     * The display area for this form section
     */
    private Composite displayArea;

    /**
     * Responds to changes when the selected element is modified
     */
    private ODOMChangeListener odomChangeListener;

    /**
     * Used to respond to updates the the policy defintions name and help text
     */
    private PropertyChangeListener propertyChangeListener;

    /**
     * The label that displays the policies short name
     */
    private Label shortNameLabel;

    /**
     * Used to edit the localized name of the policy
     */
    private Text nameField;

    /**
     * Listens to the nameField control in order to update the name in the
     * properties file.
     */
    private ModifyListener nameFieldListener;

    /**
     * Used to edit the localized description of the policy
     */
    private Text helpTextArea;

    /**
     * Listens to the helpTextArea control in order to update the description
     * in the properties file.
     */
    private ModifyListener helpTextAreaListener;

    /**
     * Label to display the policies composition.
     */
    private Label compositionLabel;

    /**
     * Used to set the policies composition. The allowable compositions are
     * determined via the policies composition
     */
    private ComboViewer typeCombo;

    /**
     * Used to respond to selection the user makes via the typeCombo.
     */
    private SelectionListener typeSelectionListener;

    /**
     * This control is only displayed if the policies type is the "Selection"
     * type. It allows the selections keywords to be defined.
     */
    private ListValueBuilder selectionValueBuilder;

    /**
     * Label for the selectionValueBuilder control
     */
    private Label selectionValueBuilderLabel;

    /**
     * Used to respond to user selection in the selectionValueBuilder control
     */
    private ModifyListener selectionKeywordListener;

    /**
     * This control is only displayed if the policy being edited is of the
     * "Range" type. It allows the user to enter the lower bound for the range.
     */
    private Text rangeLowerBoundField;

    /**
     * Label for rangeLowerBoundField control. This label is only displayed if
     * the policy type is a "range" type
     */
    private Label lowerBoundLabel;

    /**
     * This control is only displayed if the policy being edited is of the
     * "Range" type. It allows the user to enter the upper bound for the range
     */
    private Text rangeUpperBoundField;

    /**
     * Label for rangeUpperBoundField control. This label is only displayed if
     * the policy type is a "range" type
     */
    private Label upperBoundLabel;

    /**
     * Listener that will be listening to both the rangeLowerBoundField and
     * rangeUpperBoundField controls in order to update the policy type element
     */
    private ModifyListener rangeBoundListener;

    /**
     * The ODOM element that represents the currently selected policy
     */
    private ODOMElement policyDefinition;

    /**
     * Initializes a <code>PolicyDefinitionSection</code> instance with the
     * given arguments
     * @param parent the parent composite
     * @param style the style bitset
     * @param context the associated DeviceEditorContext
     */
    public PolicyDefinitionSection(Composite parent,
                                   int style,
                                   DeviceEditorContext context) {
        super(parent, style);
        this.context = context;

        // create this forms display area
        createDisplayArea(TITLE, MESSAGE);

        // Will respond to changes to the policy definition element in order
        // to update the controls
        odomChangeListener = new ODOMChangeListener() {
            // javadoc inherited
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                // Only update the attribute controls if the event is not
                // a detach.
                if(!(event.getChangeQualifier().
                        equals(ChangeQualifier.HIERARCHY) &&
                        event.getNewValue()==null)) {
                    updateAttributeControls();
                }
            }
        };

        // Will respond to changes to the policy definition properties file
        // in order to update the propertis file
        propertyChangeListener = new PropertyChangeListener() {
            // javadocu updated
            public void propertyChange(PropertyChangeEvent evt) {
                updatePropertyControls();
            }
        };

        // register the property change listener with the device properties
        context.getDeviceRepositoryAccessorManager().
                    getProperties().addPropertyChangeListener(
                                propertyChangeListener);
    }

    /**
     * Creates this forms display area
     */
    private void createDisplayArea(String title, String message) {
        Section section =
                SectionFactory.createSection(this, title, message);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        displayArea = new Composite(section, SWT.NONE);
        section.setClient(displayArea);

        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        data = new GridData(GridData.FILL_BOTH);
        displayArea.setLayout(layout);
        displayArea.setLayoutData(data);
        displayArea.setBackground(
                    getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // add the control for the short name
        addShortNameLabel();

        // add a control for the long name
        addNameControl();

        // add a control for the help text
        addHelpTextControl();

        // add the composition label
        addCompositionLabel();

        // add the policy type combo
        addPolicyTypeCombo();
    }

    /**
     * Adds the control to the display area for the policy definitions short
     * name.
     */
    private void addShortNameLabel() {
        // add a control for the short name
        createLabel(SHORT_NAME_LABEL);
        shortNameLabel = createLabel("");
    }

    /**
     * Adds the label for the policies short name
     */
    private void updateShortNameLabel() {
        String shortName = getPolicyName();
        // only update the label if needed
        if (!shortNameLabel.getText().equals(shortName)) {
            shortNameLabel.setText(shortName);
            shortNameLabel.pack();
        }
    }

    /**
     * Adds the control to the display area for the policy definitions long
     * name.
     */
    private void addNameControl() {
        createLabel(NAME_LABEL);
        nameField = new Text(displayArea, SWT.BORDER);
        context.getHandler().addControl(new TextActionable(nameField));
        nameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nameFieldListener = new ModifyListener() {
            // javadoc inherited
            public void modifyText(ModifyEvent event) {
                // update the properties file
                updateNameProperty();
            }
        };
        nameField.addModifyListener(nameFieldListener);
        // Associate an actionable handler with the control to provide
        // cut, copy, paste and delete actions.
        context.getHandler().addControl(new TextActionable(nameField));
    }

    /**
     * Updates the Text field that displays the policies localized name
     */
    private void updateNameControl() {
        DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();
        // get the policies localized name
        String localizedName = dram.getLocalizedPolicyName(getPolicyName());
        // only update the control if required
        if (!localizedName.equals(nameField.getText())) {
            // remove the controls listener
            nameField.removeModifyListener(nameFieldListener);
            try {
                // update the control
                nameField.setText(localizedName);
            } finally {
                // re-register the listener
                nameField.addModifyListener(nameFieldListener);
            }
        }
    }

    /**
     * Set the properties localized name to the text that is in the nameField
     * control
     */
    private void updateNameProperty() {
        DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();
        // get the name of the policy that is being edited
        String policy = getPolicyName();
        // obtain the localized name from the control
        String localizedName = nameField.getText();
        if (!localizedName.equals(
                    dram.getLocalizedPolicyName(policy))) {
            ObservableProperties properties = dram.getProperties();
            // remove the listener that is registered
            properties.removePropertyChangeListener(propertyChangeListener);
            try {
                // update the properties
                dram.setLocalizedPolicyName(policy, localizedName);
            } finally {
                // re-register the property change listener
                properties.addPropertyChangeListener(propertyChangeListener);
            }

        }
    }

    /**
     * Adds the control to the display area that allows the
     */
    private void addHelpTextControl() {
        createLabel(HELP_LABEL);
        helpTextArea = new Text(displayArea, SWT.MULTI | SWT.BORDER | SWT.WRAP);
        context.getHandler().addControl(new TextActionable(helpTextArea));
        helpTextArea.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = HELP_WIDTH;
        helpTextArea.setLayoutData(gridData);
        helpTextAreaListener = new ModifyListener() {
            // javadoc inherited
            public void modifyText(ModifyEvent event) {
                updateHelpProperty();
            }
        };
        helpTextArea.addModifyListener(helpTextAreaListener);
        // Associate an actionable handler with the control to provide
        // cut, copy, paste and delete actions.
        context.getHandler().addControl(new TextActionable(helpTextArea));

    }

    /**
     * Updates the helpTextArea control so that it displays the text as
     * specified in the devices properties file.
     */
    private void updateHelpControl() {
        DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();
        // get the policies localized name
        String helpText = dram.getPolicyDescription(getPolicyName());
        // only update the control if required
        if (!helpText.equals(helpTextArea.getText())) {
            // remove the controls listener
            helpTextArea.removeModifyListener(helpTextAreaListener);
            try {
                // update the control
                helpTextArea.setText(helpText);
            } finally {
                // re-register the listener
                helpTextArea.addModifyListener(helpTextAreaListener);
            }
        }
    }

    /**
     * Updates the policies description in the properties file to reflect
     * the value in the help text area.
     */
    private void updateHelpProperty() {
        DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();
        // get the name of the policy that is being edited
        String policy = getPolicyName();
        // obtain the help text in the control
        String helpText = helpTextArea.getText();
        if (!helpText.equals(
                    dram.getPolicyDescription(policy))) {
            ObservableProperties properties = dram.getProperties();
            // remove the listener that is registered
            properties.removePropertyChangeListener(propertyChangeListener);
            try {
                // update the properties
                dram.setPolicyDescription(policy, helpText);
            } finally {
                // re-register the property change listener
                properties.addPropertyChangeListener(propertyChangeListener);
            }

        }
    }

    /**
     * Adds a label that will be used to display the selected policies
     * composition
     */
    private void addCompositionLabel() {
        createLabel(COMPOSITION_LABEL);
        compositionLabel = createLabel("");
    }

    /**
     * Updates the composition label so that it displays the composition of
     * the currently selected policy definition.
     */
    private void updateCompositionLabel() {
        DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();

        Element typeElement = dram.getTypeDefinitionElement(getPolicyName());

        PolicyTypeComposition composition =
                    PolicyTypeComposition.getComposition(typeElement);
        String localizedComposition =
                    DevicesMessages.getLocalizedCompositionName(composition);
        if (!localizedComposition.equals(compositionLabel.getText())) {
            compositionLabel.setText(localizedComposition);
            compositionLabel.pack();
        }
    }

    /**
     * Adds a Combo control that will be used to edit the type of the currently
     * selected policy.
     */
    private void addPolicyTypeCombo() {
        createLabel(TYPE_LABEL);
        typeCombo = new ComboViewer(displayArea,
                                    SWT.READ_ONLY,
                                    new PresentableItem[] {
                                        new PresentableItem("", "")
                                    });

        typeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        typeSelectionListener = new SelectionAdapter() {
            // javadoc inherited
            public void widgetSelected(SelectionEvent event) {
                updatePolicyTypeValue();
            }
        };
        typeCombo.addSelectionListener(typeSelectionListener);

        // Associate an actionable handler with the control to provide
        // cut, copy, paste and delete actions.
        context.getHandler().
                addControl(new ComboActionable(typeCombo.getCombo()));
    }

    /**
     * Updates the policy type combo to reflect the currently selected policy
     * definition elements type.
     */
    private void updatePolicyTypeCombo() {
        DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();

        Element typeElement = dram.getTypeDefinitionElement(getPolicyName());
        PolicyType type = PolicyType.getType(typeElement);
        if (!typeCombo.getValue().equals(type)) {
            typeCombo.removeSelectionListener(typeSelectionListener);
            try {
                // update the types in the compo
                typeCombo.setItems(DevicesMessages.getLocalizedPolicyTypes(
                    PolicyTypeComposition.getComposition(typeElement)));
                // set the selected type
                typeCombo.setValue(type);
            } finally {
                typeCombo.addSelectionListener(typeSelectionListener);
            }
        }
    }

    /**
     * Updates the selected policy definition so that it's type is
     * as specified in the type combo
     */
    private void updatePolicyTypeValue() {
        DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();

        Element typeElement = dram.getTypeDefinitionElement(getPolicyName());

        PolicyTypeComposition composition =
                    PolicyTypeComposition.getComposition(typeElement);

        PolicyType policyType = (PolicyType) typeCombo.getValue();
        PolicyType currentType =
                    PolicyTypeComposition.getPolicyType(typeElement);

        if (currentType != policyType) {
            policyDefinition.removeChangeListener(odomChangeListener);
            try {
                // detatch type element from the policy definition element
                typeElement.detach();
                // add the new type element
                composition.addTypeElement(policyDefinition,
                                           policyType,
                                           context.getODOMFactory());
                // update the optional controls that may be required if the
                // new type requires optional controls.
                updateOptionalTypeControls();
            } finally {
                policyDefinition.addChangeListener(odomChangeListener);
            }
        }
    }

    /**
     * Adds/removes any optional controls that are required for the current
     * policy type.
     */
    private void updateOptionalTypeControls() {
        DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();

        Element typeElement = dram.getTypeDefinitionElement(getPolicyName());
        PolicyType type = PolicyType.getType(typeElement);

        // If the optional controls already exist for the policy type, then we
        // do not recreate them - this prevents flicker as related values are
        // changed.
        if (type == PolicyType.RANGE) {
            if (rangeLowerBoundField == null || rangeUpperBoundField == null) {
                disposeOptionalControls();
                addRangeBoundControls();
            }
        } else if (type == PolicyType.SELECTION) {
            if (selectionValueBuilder == null) {
                disposeOptionalControls();
                addSelectionValueBuilder();
            }
        } else {
            disposeOptionalControls();
        }
    }

    /**
     * Disposes of all optional controls required by the current policy type.
     */
    private void disposeOptionalControls() {
        if (rangeLowerBoundField != null) {
            rangeLowerBoundField.removeModifyListener(rangeBoundListener);
            lowerBoundLabel.dispose();
            rangeLowerBoundField.dispose();
            rangeLowerBoundField = null;
        }
        if (rangeUpperBoundField != null) {
            rangeUpperBoundField.removeModifyListener(rangeBoundListener);
            upperBoundLabel.dispose();
            rangeUpperBoundField.dispose();
            rangeUpperBoundField = null;
        }
        if (selectionValueBuilder != null) {
            selectionValueBuilder.removeModifyListener(
                        selectionKeywordListener);
            selectionValueBuilderLabel.dispose();
            selectionValueBuilder.dispose();
            selectionValueBuilder = null;
        }
        getParent().layout();
    }

    /**
     * Adds the text fields that allow a range types lower and upper bounds
     * to be edited
     */
    private void addRangeBoundControls() {

        rangeBoundListener = new ModifyListener() {
            // javadoc inherited
            public void modifyText(ModifyEvent event) {
                updateRangeBoundsValue();
            }
        };

        lowerBoundLabel = createLabel(LOWER_RANGE_LABEL);
        rangeLowerBoundField = new Text(displayArea, SWT.BORDER);
        context.getHandler().addControl(new TextActionable(rangeLowerBoundField));
        rangeLowerBoundField.setLayoutData(new GridData(
                    GridData.FILL_HORIZONTAL));
        rangeLowerBoundField.addModifyListener(rangeBoundListener);

        upperBoundLabel = createLabel(UPPER_RANGE_LABEL);
        rangeUpperBoundField = new Text(displayArea, SWT.BORDER);
        context.getHandler().addControl(new TextActionable(rangeUpperBoundField));
        rangeUpperBoundField.setLayoutData(new GridData(
                    GridData.FILL_HORIZONTAL));
        rangeUpperBoundField.addModifyListener(rangeBoundListener);
        updateRangeBoundsControls();
        getParent().layout();
    }

    /**
     * Updates the lower and upper range text fields to reflect the value
     * of the range type element.
     */
    public void updateRangeBoundsControls() {
        DeviceRepositoryAccessorManager dram =
                            context.getDeviceRepositoryAccessorManager();
        Element typeElement = dram.getTypeDefinitionElement(getPolicyName());
        Element rangeElement = findDescendantElement(
                    DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_RANGE_ELEMENT_NAME,
                    typeElement);

        String lower = rangeElement.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                        RANGE_MIN_INCLUSIVE_ATTRIBUTE);

        String upper = rangeElement.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                        RANGE_MAX_INCLUSIVE_ATTRIBUTE);
        try {
            rangeLowerBoundField.removeModifyListener(rangeBoundListener);
            rangeUpperBoundField.removeModifyListener(rangeBoundListener);
            if (!rangeLowerBoundField.getText().equals(lower)) {
                rangeLowerBoundField.setText(lower);
            }
            if (!rangeUpperBoundField.getText().equals(upper)) {
                rangeUpperBoundField.setText(upper);
            }
        } finally {
            rangeLowerBoundField.addModifyListener(rangeBoundListener);
            rangeUpperBoundField.addModifyListener(rangeBoundListener);
        }
    }

    /**
     * Updates the range type element for the currently selected policy so
     * that is is in sync with the values typed into the lower and upper
     * range text fields
     */
    public void updateRangeBoundsValue() {
        String lowerBound = rangeLowerBoundField.getText();
        String upperBound = rangeUpperBoundField.getText();
        DeviceRepositoryAccessorManager dram =
                            context.getDeviceRepositoryAccessorManager();

        Element typeElement = dram.getTypeDefinitionElement(getPolicyName());
        Element rangeElement = findDescendantElement(
                    DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_RANGE_ELEMENT_NAME,
                    typeElement);

        String currentLower = rangeElement.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                        RANGE_MIN_INCLUSIVE_ATTRIBUTE);

        String currentUpper = rangeElement.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                        RANGE_MAX_INCLUSIVE_ATTRIBUTE);

        policyDefinition.removeChangeListener(odomChangeListener);
        try {
            if (!upperBound.equals(currentUpper)) {
                rangeElement.setAttribute(
                            DeviceRepositorySchemaConstants.
                                RANGE_MAX_INCLUSIVE_ATTRIBUTE,
                            upperBound);
            }
            if (!lowerBound.equals(currentLower)) {
                rangeElement.setAttribute(
                            DeviceRepositorySchemaConstants.
                                RANGE_MIN_INCLUSIVE_ATTRIBUTE,
                            lowerBound);
            }
        } finally {
            policyDefinition.addChangeListener(odomChangeListener);
        }
    }

    /**
     * Creates the ListValueBuilder control that allows the selection types
     * keywords to be defined.
     */
    private void addSelectionValueBuilder() {
        selectionValueBuilderLabel = createLabel(SELECTION_VALUES_LABEL);
        selectionValueBuilder = new ListValueBuilder(displayArea, true, null);
        selectionValueBuilder.setLayoutData(
                    new GridData(GridData.FILL_HORIZONTAL));
        selectionValueBuilder.setBackground(
                    getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        selectionKeywordListener = new ModifyListener() {
            // javadoc inheriteed
            public void modifyText(ModifyEvent event) {
                updateSelectionValueKeywords();
            }
        };
        selectionValueBuilder.addModifyListener(selectionKeywordListener);
        updateSelectionValueBuilder();
        getParent().layout();
    }

    /**
     * Updates the ListValueBuilder control that allows the selection type
     * keywords to be defined. The keywords are obtained from the policy
     * definition element
     */
    private void updateSelectionValueBuilder() {
        DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();

        Element typeElement = dram.getTypeDefinitionElement(getPolicyName());
        Namespace namespace = typeElement.getNamespace();
        Element selectionElement = findDescendantElement(
                    DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_SELECTION_ELEMENT_NAME,
                    typeElement);
        List keywordList = selectionElement.getChildren(
                    DeviceRepositorySchemaConstants.
                            POLICY_DEFINITION_KEYWORD_ELEMENT_NAME,
                    namespace);
        selectionValueBuilder.removeModifyListener(selectionKeywordListener);
        try {
            String[] keywords = new String[keywordList.size()];
            for (int i=0; i<keywordList.size(); i++) {
                keywords[i] = ((Element) keywordList.get(i)).getText();
            }
            if (!Arrays.equals(keywords,  selectionValueBuilder.getItems())) {
                selectionValueBuilder.setItems(keywords);
            }
        } finally {
            selectionValueBuilder.addModifyListener(selectionKeywordListener);
        }
    }

    /**
     * Updates the policy definitions "selection" type element so that it
     * reflects the selection keywords specified in the selectionValueBuilder
     * control
     */
    private void updateSelectionValueKeywords() {
        DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();

        Element typeElement = dram.getTypeDefinitionElement(getPolicyName());
        Namespace namespace = typeElement.getNamespace();
        Element selectionElement = findDescendantElement(
                    DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_SELECTION_ELEMENT_NAME,
                    typeElement);

        policyDefinition.removeChangeListener(odomChangeListener);
        try {
            selectionElement.removeChildren(
                        DeviceRepositorySchemaConstants.
                            POLICY_DEFINITION_KEYWORD_ELEMENT_NAME,
                        namespace);
            ODOMFactory factory = context.getODOMFactory();
            Object[] keywords = selectionValueBuilder.getItems();
            for (int i=0; i<keywords.length; i++) {
                Element keyword = factory.element(
                            DeviceRepositorySchemaConstants.
                                POLICY_DEFINITION_KEYWORD_ELEMENT_NAME,
                            namespace);
                keyword.addContent(factory.text((String) keywords[i]));
                selectionElement.addContent(keyword);
            }
        } finally {
            policyDefinition.addChangeListener(odomChangeListener);
        }
    }

    /**
     * Factory method for creating a text label with a white backround
     * @param labelText the text to use for the label
     * @return a Label instance
     */
    private Label createLabel(String labelText) {
        Label label = new Label(displayArea, SWT.NONE);
        label.setText(labelText);
        label.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        return label;
    }

    /**
     * Set the <code>ODOMElement</code> for the policy definition that is to
     * be edited.
     * @param policyDefinition the ODOMElement representation of the policy
     * definition.
     */
    public void setElement(ODOMElement policyDefinition) {
        if (policyDefinition == null) {
            throw new IllegalArgumentException(
                        "Cannot display a null policy definition");
        }
        if (this.policyDefinition != null) {
            this.policyDefinition.removeChangeListener(odomChangeListener);
        }

        this.policyDefinition = policyDefinition;
        this.policyDefinition.addChangeListener(odomChangeListener);

        updateAttributeControls();
        updatePropertyControls();
    }

    /**
     * Updates this section controls so that the reflect the currently selected
     * element. Only updates those controls that obtain their value from the
     * policy definition ODOMElement (and not the properties file).
     */
    private void updateAttributeControls() {
        // display the short name
        updateShortNameLabel();
        // display the composition
        updateCompositionLabel();

        // update the type combo
        updatePolicyTypeCombo();

        // update the optional controls.
        updateOptionalTypeControls();
    }

    /**
     * Updates this section controls so that the reflect the currently selected
     * element. Only updates those controls that obtain their value from the
     * properties file (and not the policy definition ODOMElement)
     */
    private void updatePropertyControls() {
        // update the localized name
        updateNameControl();
        // update the help text field
        updateHelpControl();
    }

    /**
     * Finds the element with a given name that is a descendant of the context
     * element
     * @param name that name of the element to locate
     * @param context the context that the descendant belongs to
     * @return the descendant <code>Element</code> or null if the named
     * element could not be found.
     */
    private Element findDescendantElement(String name, Element context) {
        Element descendant = null;
        StringBuffer buffer = new StringBuffer();
        // note using "a" as the namespace prefix
        buffer.append("descendant::a:").append(name);
        // create the XPath. Bind the prefix "a" to the namespace URI that
        // the context belongs to.
        XPath xpath = new XPath(buffer.toString(),
                                new Namespace[] {
                                    Namespace.getNamespace(
                                                "a",
                                                context.getNamespaceURI())});
        try {
            descendant = (Element) xpath.selectSingleNode(context);
        } catch (XPathException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
        return descendant;
    }

    /**
     * Returns the short name of the currently selected policy definition.
     * @return The name of the currently selected policy or null if no policy
     * is selected.
     */
    private String getPolicyName() {
        return policyDefinition.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_NAME_ATTRIBUTE);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 16-Nov-04	4394/8	allan	VBM:2004051018 Undo/Redo in device editor.

 14-May-04	4394/2	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 13-May-04	4301/2	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 19-May-04	4498/1	byron	VBM:2004051214 Help text in Device Editor controls does not wrap

 14-May-04	4369/1	allan	VBM:2004051311 Override fixed, widget dispose fix, new button fix.

 12-May-04	4313/1	pcameron	VBM:2004051203 Added scrolling to DeviceStructurePart and fixed a few layout issues

 11-May-04	4161/4	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 11-May-04	4161/1	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 ===========================================================================
*/
