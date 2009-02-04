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
import com.volantis.mcs.eclipse.ab.editors.devices.ListPolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.PolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.PolicyValueSelectionDialog;
import com.volantis.mcs.eclipse.controls.ListValueBuilder;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.jdom.Element;
import org.jdom.input.JDOMFactory;

import java.util.List;

/**
 * Designates a policy type composition that is composed of one or more
 * single value policy types in an ordered set.
 */
final class OrderedSetComposition extends PolicyTypeComposition {

    /**
     * Construct an new OrderedSetComposition.
     */
    OrderedSetComposition() {
        super("orderedSet");
    }

    // javadoc inherited
    public PolicyValueModifier createPolicyValueModifier(
            Composite parent, int style,
            String policyName,
            DeviceRepositoryAccessorManager dram) {


        if (dram == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "dram.");
        }

        ListPolicyValueModifier listPVM = null;

        // Retrieve the policy type element.
        Element policyTypeElement =
                dram.getTypeDefinitionElement(policyName);

        if (policyTypeElement != null) {

            if (!policyTypeElement.getName().
                    equals(DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_TYPE_ELEMENT_NAME)) {
                throw new IllegalStateException("The policy type " +
                        "element for " + policyName + " is " +
                        "named " + policyTypeElement.getName() +
                        ". Expected " + DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_TYPE_ELEMENT_NAME);
            }

            SelectionDialog dialog = null;

            Element orderedSetElement = policyTypeElement.getChild(
                    DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_ORDEREDSET_ELEMENT_NAME,
                    policyTypeElement.getNamespace());

            // Retrieve the selection element for the named policy.
            Element selectionElement = orderedSetElement.getChild(
                    DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_SELECTION_ELEMENT_NAME,
                    orderedSetElement.getNamespace());

            if (selectionElement != null) {
                dialog = new PolicyValueSelectionDialog(
                        parent.getShell(),
                        policyName, selectionElement);
            }
            ListValueBuilder listVB =
                    new ListValueBuilder(parent, true, dialog);

            listPVM = new ListPolicyValueModifier(listVB, policyName, dram);
        } else {
            throw new IllegalStateException("The policy type " +
                    "element for " + policyName + " is null.");
        }
        return listPVM;
    }

    // javadoc inherited
    void addTypeElementImpl(Element parent,
                            PolicyType type,
                            JDOMFactory factory) {
        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: parent.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Cannot be null: type.");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Cannot be null: factory.");
        }

        Element orderedSetElement =
                factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_ORDEREDSET_ELEMENT_NAME,
                        parent.getNamespace());

        type.addPolicyTypeElement(orderedSetElement, factory);

        parent.addContent(orderedSetElement);
    }

    // javadoc inherited
    public void addDefaultPolicyValue(Element element,
                                      String policyName,
                                      PolicyType type,
                                      JDOMFactory factory,
                                      DeviceRepositoryAccessorManager dram) {
        Element policy = factory.element(DeviceRepositorySchemaConstants.
                POLICY_ELEMENT_NAME, element.getNamespace());
        policy.setAttribute(DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE,
                policyName);
        element.addContent(policy);
    }

    // javadoc inherited
    boolean canHandleType(String policyTypeName) {
        return policyTypeName.equals(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_ORDEREDSET_ELEMENT_NAME);
    }

    // javadoc inherited
    boolean canHandleType(Element type) {
        Element policyTypeElement = (Element) type.getChildren().get(0);
        return policyTypeElement.getName().
                equals(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_ORDEREDSET_ELEMENT_NAME);
    }

    // javadoc inherited
    public PolicyType[] getSupportedPolicyTypes() {
        return PolicyType.getSingleValuedPolicyTypesWithoutBoolean();
    }

    // javadoc inherited
    PolicyType getPolicyTypeImpl(Element type) {
        Element orderedSet = (Element) type.getChildren().get(0);
        List children = orderedSet.getChildren();
        String typeName = null;
        if (children.size() == 1) {
            typeName = ((Element) children.get(0)).getName();
        }
        return PolicyType.getType(typeName);
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

 15-Nov-04	6199/1	adrianj	VBM:2004102512 Added accessibility support to PolicyValueModifiers

 11-May-04	4161/1	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 10-May-04	4068/1	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 29-Apr-04	4103/1	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 ===========================================================================
*/
