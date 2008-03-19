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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.devices.types;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.editors.devices.PolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.StructurePolicyValueModifier;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.JDOMFactory;

/**
 * The EmulateEmphasisTag policy type.
 */
final class EmulateEmphasisTagType extends PolicyType {

    /**
     * Creates a new EmulateEmphasisTagType. Package local so that only
     * PolicyType can create this and maintain it as a constant.
     */
    EmulateEmphasisTagType() {
        super(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME
        );
    }

    // javadoc inherited
    public PolicyValueModifier createPolicyValueModifier(
            Composite parent,
            int style,
            String policyName,
            DeviceRepositoryAccessorManager dram) {
        // factor a StructurePolicyValueModifier
        return new StructurePolicyValueModifier(parent,
                style,
                policyName,
                dram);
    }

    /**
     * Adds the ref attribute to the given "type" element. This implementation
     * differs from others in that no element is added - just an attribute.
     *
     * <type ref="EmulateEmphasisTag" />
     *
     * @param type the parent element for the type element. Cannot be null.
     * @param factory the factory to use for creating the type element. Cannot
     * be null.
     * @throws IllegalArgumentException if parent or factory is null
     */
    void addPolicyTypeElementImpl(Element type,
                               JDOMFactory factory) {
        if (type == null) {
            throw new IllegalArgumentException("Cannot be null: type.");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Cannot be null: factory.");
        }
        if(!type.getName().equals(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME)) {
            throw new IllegalArgumentException("Expected a " +
                    DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_TYPE_ELEMENT_NAME + " element but was " +
                    type.getName());
        }
        type.setAttribute(
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_REF_ATTRIBUTE_NAME,
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME);
    }

    /**
     * Adds the default value of this policy type to the given element, using
     * the supplied factory and manager. Used by {@link #addDefaultPolicyValue}.
     * @param element the element on which to add the default value. Cannot be
     * null.
     * @param factory the factory to use. cannot be null.
     * @param dram the DeviceRepositoryAccessorManager. Cannot be null.
     * @throws IllegalStateException if there are problems retrieving policy
     * information.
     */
    protected void setDefaultValue(Element element, JDOMFactory factory,
                                   DeviceRepositoryAccessorManager dram) {
        String policyName = element.getAttributeValue(
                DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE);

        if (policyName == null || policyName.length() == 0) {
            throw new IllegalStateException("Cannot retrieve policy name " +
                    "for element " + element.getName());
        }

        // element should be <type name="EmulateEmphasisTag">
        final Element policyTypeElement =
                dram.getTypeDefinitionElement(policyName);

        if (policyTypeElement == null) {
            throw new IllegalStateException("Cannot retrieve policy type " +
                    "element for policy " + policyName);
        }

        final Element structureElement = policyTypeElement.getChild(
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_STRUCTURE_ELEMENT_NAME,
                policyTypeElement.getNamespace());

        if (structureElement == null) {
            throw new IllegalStateException("Cannot retrieve " +
                    DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_STRUCTURE_ELEMENT_NAME +
                    " element for policy " + policyName);
        }

        // Retrieve the children of the structure element.
        List children = structureElement.getChildren(
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_FIELD_ELEMENT_NAME,
                structureElement.getNamespace());

        Iterator it = children.iterator();

        // Iterate over the children creating a field element with the
        // appropriate default value for each child.
        while (it.hasNext()) {

            // element should be e.g. <field name="enable">
            Element typeField = (Element) it.next();

            // retrieve the field name e.g. "enable"
            String fieldName = typeField.getAttributeValue(
                    DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE);

            if (fieldName == null || fieldName.length() == 0) {
                throw new IllegalStateException("Cannot retrieve field name " +
                        "for element " + typeField.getName());
            }

            // element should be <type>
            Element typeElement = typeField.getChild(
                    DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_TYPE_ELEMENT_NAME,
                    structureElement.getNamespace());

            if (typeElement == null) {
                throw new IllegalStateException("Cannot retrieve " +
                        DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_TYPE_ELEMENT_NAME +
                        " element for element " + typeField.getName());
            }

            // Get the element children, filtering also on namespace.
            List typeContent =
                    typeElement.getContent(
                            new ElementFilter(typeElement.getNamespace()));

            // element should be e.g. <boolean />
            Element emptyPolicyType = (Element) typeContent.get(0);

            if (emptyPolicyType == null) {
                throw new IllegalStateException("Cannot retrieve empty " +
                        "policy type element for " + typeElement.getName());
            }

            // name should be e.g. boolean
            String fieldTypeName = emptyPolicyType.getName();

            // get the PolicyType for the name e.g. PolicyType.BOOLEAN
            PolicyType policyType = PolicyType.getType(fieldTypeName);

            if (policyType == null) {
                throw new IllegalStateException("Cannot retrieve a policy " +
                        "type named " + fieldTypeName);
            }

            // Create a new <field> element
            Element fieldElement = factory.element(
                    DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_FIELD_ELEMENT_NAME,
                    // Use namespace of parent (destination) element.
                    element.getNamespace());

            // add the field name e.g. name="enable"
            fieldElement.setAttribute(
                    DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE,
                    fieldName);

            // Add the default value of the PolicyType to <field> e.g.
            // value="false"
            policyType.setDefaultValue(fieldElement, factory, dram);

            element.addContent(fieldElement);
        }
    }

    // javadoc inherited
    PolicyType.PolicyValueStructure getPolicyValueStructure() {
        return PolicyType.PolicyValueStructure.UNSPECIFIED;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Apr-04	4103/1	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 26-Apr-04	4001/2	pcameron	VBM:2004032104 Added NewDevicePolicyWizard and NewDevicePolicyWizardPage

 08-Apr-04	3686/5	pcameron	VBM:2004032204 Some further tweaks to PolicyType

 07-Apr-04	3686/3	pcameron	VBM:2004032204 Some minor refactoring to PolicyType

 06-Apr-04	3686/1	pcameron	VBM:2004032204 Added new methods to PolicyType and refactored

 ===========================================================================
*/
