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
import com.volantis.mcs.eclipse.ab.editors.devices.BooleanPolicyValueModifier;
import com.volantis.mcs.eclipse.ab.editors.devices.PolicyValueModifier;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;
import org.jdom.input.JDOMFactory;

/**
 * The Boolean policy type.
 */
final class BooleanType extends PolicyType {

    /**
     * Creates a new BooleanType. Package local so that only PolicyType
     * can create this and maintain it as a constant.
     */
    BooleanType() {
        super(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_BOOLEAN_ELEMENT_NAME
        );
    }

    /**
     * Creates the PolicyValueModifier used to modify values of
     * boolean policy types.
     * @param dram the DeviceRepositoryAccessorManager to use for
     *             retrieving policy information. Cannot be null.
     * @throws java.lang.IllegalArgumentException if dram is null
     */
    // rest of javadoc inherited
    public PolicyValueModifier createPolicyValueModifier(
            Composite parent, int style,
            String policyName,
            DeviceRepositoryAccessorManager dram) {

        BooleanPolicyValueModifier booleanPVM =
                new BooleanPolicyValueModifier(parent, dram, policyName);
        
        return booleanPVM;
    }

    /**
     * Creates a <boolean /> element.
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

        Element booleanElement =
                factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_BOOLEAN_ELEMENT_NAME,
                        parent.getNamespace());

        parent.addContent(booleanElement);
    }

    /**
     * Adds the default value "false" to the given element.
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
                POLICY_VALUE_ATTRIBUTE, "false");
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

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 29-Apr-04	4103/3	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 26-Apr-04	4001/2	pcameron	VBM:2004032104 Added NewDevicePolicyWizard and NewDevicePolicyWizardPage

 08-Apr-04	3686/5	pcameron	VBM:2004032204 Some further tweaks to PolicyType

 07-Apr-04	3686/3	pcameron	VBM:2004032204 Some minor refactoring to PolicyType

 06-Apr-04	3686/1	pcameron	VBM:2004032204 Added new methods to PolicyType and refactored

 ===========================================================================
*/
