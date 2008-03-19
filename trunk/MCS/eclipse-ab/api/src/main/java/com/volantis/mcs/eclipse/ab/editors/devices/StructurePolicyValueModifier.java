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

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.jdom.Element;
import org.jdom.Namespace;
import com.volantis.mcs.eclipse.ab.editors.devices.DevicesMessages;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A <code>PolicyValueModifier</code> that allows editing of structured policy
 * types. A structured type consists one or more fields that themselves are
 * typed.
 */
public class StructurePolicyValueModifier extends AbstractPolicyValueModifier {

    /**
     * Used as lookup key prefixes when retrieving resources from the
     * DeviceMessages.properties file.
     */
    private static final String RESOURCE_PREFIX =
                "StructurePolicyValueModifier.";

    /**
     * Constant for layout's margin height value
     */
    private static final int MARGIN_HEIGHT =
                DevicesMessages.getInteger(RESOURCE_PREFIX +
                                           "marginHeight").intValue();

    /**
     * Constant for layout's margin width value
     */
    private static final int MARGIN_WIDTH =
                DevicesMessages.getInteger(RESOURCE_PREFIX +
                                           "marginWidth").intValue();

    /**
     * Constant for layout's horizontal spacing value
     */
    private static final int HORIZONTAL_SPACING =
                DevicesMessages.getInteger(RESOURCE_PREFIX +
                                           "horizontalSpacing").intValue();

    /**
     * Constant for layout's vertical spacing value
     */
    private static final int VERTICAL_SPACING =
                DevicesMessages.getInteger(RESOURCE_PREFIX +
                                           "verticalSpacing").intValue();
    /**
     * This is the composite that contains the controls that are used to
     * modify the various fields of this structure
     */
    private final Control control;

    /**
     * Will contain mappings from a named field to its PolicyValuModifer
     */
    private final Map modifiers;

    /**
     * Used to access the device repository
     */
    private final DeviceRepositoryAccessorManager repositoryManager;

    /**
     * Initializes a <code>StructurePolicyValueModifier<code> instance with
     * the given arguments.
     * @param parent the parent Composite
     * @param style bitset used to specify stylistic info for the control
     * @param policyName the name of the device policy that this control is
     * associated with.
     * @param repositoryManager a DeviceRepositoryAccessorManager that can be
     * used to access/update device repository data.
     */
    public StructurePolicyValueModifier(
                Composite parent,
                int style,
                String policyName,
                DeviceRepositoryAccessorManager repositoryManager) {
        this.modifiers = new HashMap();
        this.repositoryManager = repositoryManager;
        // create the control that allows this policy to be edited
        this.control = createControl(parent,
                                     style,
                                     policyName);
    }

    /**
     * Factors the control that this PolicyValueModifier uses
     * @param parent the parent Composite. Cannot be null.
     * @param style bitset used to specify stylistic info for the control
     * @param policyName the name of the device policy that this control is
     * associated with. Cannot be null.
     * @return a Control that can be used to view and update the associated
     * policy.
     * @throws IllegalArgumentException if the parent or policyName parameters
     * are null.
     */
    private Control createControl(Composite parent,
                                  int style,
                                  String policyName) {
        // parent cannot be null
        if (parent == null) {
            throw new IllegalArgumentException("parent cannot be null");
        }
        // policy name cannot be null
        if (policyName == null) {
            throw new IllegalArgumentException(
                        "policyName cannot be null");
        }

        // create a composite that will the various controls used to update
        // the fields.
        Composite control = new Composite(parent, SWT.NONE);

        // controls will be layed out in a two column grid.
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = MARGIN_HEIGHT;
        layout.marginWidth = MARGIN_WIDTH;
        layout.horizontalSpacing = HORIZONTAL_SPACING;
        layout.verticalSpacing = VERTICAL_SPACING;
        control.setLayout(layout);

        // retrieve the policy type element for this policy
        Element typeElement =
                    repositoryManager.getTypeDefinitionElement(policyName);
        if (typeElement == null) {
            throw new IllegalStateException(
                        "Could not obtain the type element for the policy " +
                        policyName);
        }

        // this type element should have a child "structure" element that
        // contains one or more field elements
        StringBuffer pathBuffer = new StringBuffer();
        pathBuffer.append("ns:")
                  .append(DeviceRepositorySchemaConstants.
                            POLICY_DEFINITION_STRUCTURE_ELEMENT_NAME)
                  .append("/ns:")
                  .append(DeviceRepositorySchemaConstants.
                            POLICY_DEFINITION_FIELD_ELEMENT_NAME);
        // create an XPath that can be used to retrieve the field elements
        XPath xpath = new XPath(pathBuffer.toString(),
                                new Namespace[] {
                                    Namespace.getNamespace(
                                                "ns",
                                                typeElement.getNamespaceURI())
                                });

        try {
            List fields = xpath.selectNodes(typeElement);
            // we must have at least one field
            if (fields.isEmpty()) {
                throw new IllegalStateException("The structure element must " +
                                                "contain at least one field");
            }
            // iterate over the fields so that we can add the correct label and
            // control for each field
            for (Iterator i=fields.iterator(); i.hasNext();) {
                // no need to check the element is a "field" element
                final Element field = (Element) i.next();
                final PolicyType fieldType = getPolicyTypeForField(field);
                final String fieldName = getFieldName(field);

                // create the label for the field. Note, the PolicyType
                // determines wether the field requires a label, the label
                // returned could be null.
                Label label = createLabel(control, fieldType, fieldName);

                // create the policy value modifier
                PolicyValueModifier policyValueModifier =
                            fieldType.createPolicyValueModifier(
                                        control,
                                        style,
                                        policyName,
                                        repositoryManager);
                // add a listener to the policy value modifier so that we
                // can forward the any notifications to listeners registered
                // with this StructurePolicyValueModifier
                policyValueModifier.addModifyListener(new ModifyListener() {
                    // javadoc inherited
                    public void modifyText(ModifyEvent event) {
                        // We treat the enable field specially. If it is
                        // a Boolean type then we disable all other controls
                        // in the structure.
                        if (fieldType == PolicyType.BOOLEAN &&
                            DeviceRepositorySchemaConstants.
                                    POLICY_DEFINITION_ENABLE_FIELD_NAME.equals(
                                                fieldName)) {
                            // need to enable/disable the other fields
                            setEnabled(Boolean.valueOf(
                                        event.data.toString()).booleanValue());
                        }
                        // ensure modify listeners that are registered with
                        // this class are notified of the modification.
                        fireModifyEvent(event);
                    }
                });

                GridData gridData = new GridData();
                // if there is no associated label then the control should span
                // both columns
                gridData.horizontalSpan = (label == null) ? 2 : 1;
                gridData.grabExcessHorizontalSpace = true;
                gridData.horizontalAlignment = GridData.FILL;
                policyValueModifier.getControl().setLayoutData(gridData);

                // cache the PolicyValueModifier
                modifiers.put(fieldName, policyValueModifier);
            }
            // return the composite control
            return control;
        } catch (XPathException e) {
            throw new IllegalStateException(
                        "policy '" + policyName + "' is not a structure type");
        }
    }

    // javadoc inherited
    public Control getControl() {
        return control;
    }

    // javadoc inherited
    protected void refreshControl() {
        // retrieve all the field children of the policy element.
        List fields = policyElement.getChildren(
                    DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_FIELD_ELEMENT_NAME,
                    policyElement.getNamespace());

        // the policy must have at least one field.
        if (fields == null || fields.size() == 0) {
            throw new IllegalStateException(
                        "A structured policy must have at least " +
                        "one field child");
        }

        // We need to use indexing to iterate over the fields rather than
        // an Iterator because we don't know if one of the methods we
        // call will change one of the elements. If this did happen when using
        // an Iterator then we would receive a ConcurrentModificationException.
        // Note that using the indexing approach makes the assumption that no
        // elements are added or removed from the policyElement. Currently
        // this assumption is valid and is for the foreseeable future.
        for (int i=0; i<fields.size(); i++) {
            // obtain the field element
            Element field = (Element) fields.get(i);
            // get the fields name
            String fieldName = getFieldName(field);
            // obtain the PolicyValueModifier for this field
            AbstractPolicyValueModifier policyValueModifier =
                        (AbstractPolicyValueModifier) modifiers.get(fieldName);
            // we must have a policy value modifier
            if (policyValueModifier == null) {
                throw new IllegalStateException(
                            "Unexpected field '" + fieldName);
            }
            // pass the field element to the policy value modifier

            policyValueModifier.setModifiableElement(field);

            // if the policy value modifier is a Boolean type then
            // we need to provide a specific label for the controls button
            if (policyValueModifier instanceof BooleanPolicyValueModifier) {
                if (fieldName.equals(DeviceRepositorySchemaConstants.
                                        POLICY_DEFINITION_ENABLE_FIELD_NAME)) {
                    // need to disable the other fields if this is an enable
                    // field that is set to false.
                    setEnabled(Boolean.valueOf(
                                field.getAttributeValue(
                                            DeviceRepositorySchemaConstants.
                                                    POLICY_VALUE_ATTRIBUTE)).
                                                        booleanValue());
                }
            }
        }
    }

    /**
     * Used to enable and disable all the PolicyValueModifiers that constitute
     * this structure.
     * <p><strong>
     * Note if there is a field with the name "enable" then it's associated
     * PolicyValueModifier control is never enabled/disabled.
     * </strong><p>
     * @param enable true if the PolicyValueModifiers are to be enabled, false
     * if they are to be disabled
     */
    private void setEnabled(boolean enable) {
        for (Iterator i = modifiers.entrySet().iterator(); i.hasNext();)  {
            Map.Entry entry = (Map.Entry) i.next();
            if (!DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_ENABLE_FIELD_NAME.equals(
                                    entry.getKey())) {
                PolicyValueModifier modifier =
                            (PolicyValueModifier) entry.getValue();
                modifier.getControl().setEnabled(enable);
            }
        }
    }

    /**
     * Helper method that returns a PolicyType instance that represents
     * the type of the field element.
     * @param field the Element that specifies the field
     * @return a PolicyType instance.
     * @throws IllegalStateException if the element does not have a "type"
     * child element or the child type element does not specify a recognised
     * policy type
     */
    private PolicyType getPolicyTypeForField(Element field) {
        // the field element should have a "type" child element that
        // specifies the fields type.
        Element fieldType = field.getChild(
                    DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_TYPE_ELEMENT_NAME,
                    field.getNamespace());
        // A type element must exist
        if (fieldType == null) {
            throw new IllegalStateException(
                        DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_FIELD_ELEMENT_NAME +
                        " element named " + getFieldName(field) +
                        " must have a child " +
                        DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_TYPE_ELEMENT_NAME +
                        " element");
        }
        // Obtain the type element for the field
        PolicyType policyType = PolicyType.getType(fieldType);
        // if type is null then the type element did not specify a recognised
        // type.
        if (policyType == null) {
            throw new IllegalStateException(
                        DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_FIELD_ELEMENT_NAME +
                        " element named " + getFieldName(field) +
                        " does not has a valid " +
                        DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_TYPE_ELEMENT_NAME +
                        " element child");
        }
        return policyType;
    }

    /**
     * Returns the name of a field, given a field element.
     * @param field the Element representation of the field
     * @return the fields name.
     * @throws IllegalStateException if the field element does not have a
     * name attribute.
     */
    private String getFieldName(Element field) {
        // the name is specified via the name attribute
        String fieldName = field.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                        POLICY_NAME_ATTRIBUTE);
        if (fieldName == null)  {
            throw new IllegalStateException("Field element must have a name");
        }
        return fieldName;
    }

    /**
     * Creates a <code>Label</code> for a given field. If the fields type
     * does not require a label this factory method will return null.
     * @param parent the Composite to use as the labels parent
     * @param policyType the PolicyType associated          with the field
     * @param field the name of the field
     * @return the fields Label or null if the field does not require a label.
     */
    private Label createLabel(Composite parent,
                              PolicyType policyType,
                              String field) {
        Label label = null;
        // Structure and boolean types do not require a label
        if (policyType != PolicyType.BOOLEAN &&
            policyType != PolicyType.EMULATE_EMPHASIS_TAG) {
            // we need a label for this policyType
            String labelText = DevicesMessages.getString(
                        createFieldLabelKey(field));
            // add the label to the control
            label = new Label(parent, SWT.NONE);
            label.setText(labelText);
            label.setBackground(label.getDisplay().
                    getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        }
        return label;
    }

    /**
     * Creates the key that should be used when retrieving the label text
     * for a named field
     * @param field the name of the field
     * @return the String that should be used to lookup the label
     */
    private String createFieldLabelKey(String field) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(RESOURCE_PREFIX)
              .append(field)
              .append(".label");
        return buffer.toString();
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

 29-Apr-04	4103/1	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 22-Apr-04	3975/1	allan	VBM:2004042005 Fix multi-value policy migration and related issues.

 06-Apr-04	3686/1	pcameron	VBM:2004032204 Added new methods to PolicyType and refactored

 01-Apr-04	3602/3	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 01-Apr-04	3602/1	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 ===========================================================================
*/
