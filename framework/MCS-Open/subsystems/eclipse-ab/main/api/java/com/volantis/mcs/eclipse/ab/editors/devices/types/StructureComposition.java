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
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;
import org.jdom.input.JDOMFactory;

/**
 * Designates a policy type that has a structure composition i.e. consists
 * of a series of name/value pairs represented by field elements.
 */
final class StructureComposition extends PolicyTypeComposition {

    /**
     * Constant for the supported types.
     */
    private static final PolicyType SUPPORTED_TYPES [] =
            new PolicyType [] { PolicyType.EMULATE_EMPHASIS_TAG };

    /**
     * Construct a new StructureComposition.
     */
    StructureComposition() {
        super("structure");
    }

    public PolicyValueModifier createPolicyValueModifier(
            Composite parent, int style,
            String policyName,
            DeviceRepositoryAccessorManager dram) {
        Element policyTypeElement =
                dram.getTypeDefinitionElement(policyName);
                PolicyType policyType = PolicyType.getType(policyTypeElement);
        return policyType.createPolicyValueModifier(parent, style, policyName,
                dram);
    }

    // javadoc inherited
    void addTypeElementImpl(Element parent,
                            PolicyType type,
                            JDOMFactory factory) {
        type.addPolicyTypeElement(parent, factory);
    }

    // javadoc inherited
    public void addDefaultPolicyValue(Element element,
                                      String policyName,
                                      PolicyType type,
                                      JDOMFactory factory,
                                      DeviceRepositoryAccessorManager dram) {
        type.addDefaultPolicyValue(element, policyName, factory, dram);
    }

    // javadoc inherited
    boolean canHandleType(String policyTypeName) {
        return policyTypeName.equals(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME);
    }

    // javadoc inherited
    boolean canHandleType(Element type) {
        // the type name is provided via a name attribute
        String typeName = type.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_TYPE_NAME_ATTRIBUTE_NAME);
        return DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_EMULATE_EMPHASIS_TAG_NAME.equals(
                                typeName);
    }

    // javadoc inherited
    public PolicyType[] getSupportedPolicyTypes() {
        return SUPPORTED_TYPES;
    }

    // javadoc inherited
    PolicyType getPolicyTypeImpl(Element type) {
        // the type name is provided via a name attribute
        String typeName = type.getAttributeValue(
                    DeviceRepositorySchemaConstants.
                        POLICY_DEFINITION_TYPE_NAME_ATTRIBUTE_NAME);
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

 11-May-04	4161/1	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 10-May-04	4068/1	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 29-Apr-04	4103/3	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 ===========================================================================
*/
