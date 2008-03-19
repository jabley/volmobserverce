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
import com.volantis.mcs.eclipse.ab.editors.devices.ComboPolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.PolicyValueModifier;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;
import org.jdom.input.JDOMFactory;

/**
 * The Selection policy type.
 */
final class SelectionType extends PolicyType {

    /**
     * Creates a new SelectionType. Package local so that only PolicyType
     * can create this and maintain it as a constant.
     */
    SelectionType() {
        super(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_SELECTION_ELEMENT_NAME
        );
    }

    /**
     * Creates the PolicyValueModifier used to modify values of selection policy
     * types.
     *
     * @param parent     the parent Composite for the PolicyValueModifier's
     *                   control. Cannot be null.
     * @param style      the style of the PolicyValueModifier's control.
     * @param policyName the name of the policy. Cannot be null or empty.
     * @param dram       the DeviceRepositoryAccessorManager to use for
     *                   retrieving policy information. Cannot be null.
     * @return the new created PolicyValueModifier, or null if it could not be
     *         created.
     * @throws IllegalArgumentException if dram is null
     * @throws IllegalStateException    if the policy type element retrieved by
     *                                  the dram for the named policy is null or
     *                                  is not the expected element or has no
     *                                  values.
     */
    public PolicyValueModifier
            createPolicyValueModifier(Composite parent,
                                      int style,
                                      String policyName,
                                      DeviceRepositoryAccessorManager dram)
            throws IllegalArgumentException, IllegalStateException {

        if (dram == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "dram.");
        }

        ComboPolicyValueModifier comboPVM = null;

        // Retrieve the policy type element for the named policy.
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

            comboPVM = new ComboPolicyValueModifier(parent, style,
                    getValues(policyTypeElement), policyName, dram);
        } else {
            throw new IllegalStateException("The policy type " +
                    "element for " + policyName + " is null.");
        }
        return comboPVM;
    }

    /**
     * Gets the values of the policy type element to use for
     * populating the ComboPolicyValueModifier.
     * @param policyTypeElement the policy type element
     * @return the values
     */
    private String[] getValues(Element policyTypeElement) {

        // Retrieve the selection element of the policy type
        // element.
        Element selectionElement = policyTypeElement.getChild(
                DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_SELECTION_ELEMENT_NAME,
                policyTypeElement.getNamespace());

        String[] values = null;
        List children = null;

        if (selectionElement != null) {
            // Get the "keyword" element children of the selection
            // element.
            children = selectionElement.getChildren(
                    DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_KEYWORD_ELEMENT_NAME,
                    policyTypeElement.getNamespace());

            // Retrieve and store all the values, if any.
            values =
                    new String[children == null
                    ? 0 : children.size()];
            if (children != null) {
                Iterator it = children.iterator();
                int index = 0;
                while (it.hasNext()) {
                    Element child = (Element) it.next();
                    values[index++] = child.getText();
                }
            }
        } else {
            throw new IllegalStateException("The policy type " +
                    "element " + policyTypeElement.getName() +
                    " has a null selection element.");
        }
        return values;
    }

    /**
     * Creates a <selection /> element.
     * @param parent the parent element. Cannot be null.
     * @param factory the factory to use. Cannot be null.
     * @throws IllegalArgumentException if parent or factory is null
     */
    // rest of javadoc inherited
    public void addPolicyTypeElementImpl(Element parent,
                                      JDOMFactory factory) {
        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: parent.");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Cannot be null: factory.");
        }
        Element selectionElement =
                factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_SELECTION_ELEMENT_NAME,
                        parent.getNamespace());

        parent.addContent(selectionElement);
    }

    /**
     * Adds the default value "" to the given element.
     * @param element the element on which to add the default value. Cannot be
     * null.
     * @param factory not used. Can be null.
     * @param dram not used. Can be null.
     * @throws IllegalArgumentException if element is null
     */
    protected void setDefaultValue(Element element, JDOMFactory factory,
                                   DeviceRepositoryAccessorManager dram) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot be null: element.");
        }
        element.setAttribute(DeviceRepositorySchemaConstants.
                POLICY_VALUE_ATTRIBUTE, "");
    }

    // javadoc inherited
    PolicyType.PolicyValueStructure getPolicyValueStructure() {
        return PolicyType.PolicyValueStructure.SINGLE_VALUED;
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

 11-May-04	4276/1	byron	VBM:2004051007 New policy creation fails for selection type policies

 29-Apr-04	4103/3	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 26-Apr-04	4001/2	pcameron	VBM:2004032104 Added NewDevicePolicyWizard and NewDevicePolicyWizardPage

 08-Apr-04	3686/5	pcameron	VBM:2004032204 Some further tweaks to PolicyType

 07-Apr-04	3686/3	pcameron	VBM:2004032204 Some minor refactoring to PolicyType

 06-Apr-04	3686/1	pcameron	VBM:2004032204 Added new methods to PolicyType and refactored

 ===========================================================================
*/
