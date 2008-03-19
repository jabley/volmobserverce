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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyTypeComposition;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

import java.util.List;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;

/**
 * A factory for creating PolicyValueModifiers. The factory uses a
 * DeviceRepositoryAccessorManager to retrieve policy information.
 * PolicyValueModifiers are created with the {@link #createPolicyValueModifier}
 * method.
 */
public class PolicyValueModifierFactory {

    /**
     * The DeviceRepositoryAccessorManager used by this factory.
     */
    private final DeviceRepositoryAccessorManager deviceRAM;

    /**
     * Constructs a PolicyValueModifierFactory.
     * @param deviceRAM the DeviceRepositoryAccessorManager to use
     * @throws IllegalArgumentException if deviceRAM is null
     */
    PolicyValueModifierFactory(DeviceRepositoryAccessorManager deviceRAM) {
        if (deviceRAM == null) {
            throw new IllegalArgumentException("Cannot be null: deviceRAM.");
        }
        this.deviceRAM = deviceRAM;
    }


    /**
     * Creates a new PolicyValueModifier.
     * @param parent the parent Composite for the PolicyValueModifier
     * @param style the style for the PolicyValueModifier's control
     * @param policyName the name of the policy modified by the new
     *                   PolicyValueModifier
     * @return the PolicyValueModifier
     * @throws IllegalStateException if the policy type element for the named
     *                               policy does not have a single child, or if
     *                               the policy type element is null, or if
     *                               the single child of the policy type element
     *                               has no associated PolicyType.
     */
    public PolicyValueModifier createPolicyValueModifier(Composite parent,
                                                         int style,
                                                         String policyName) {
        // Get the type element for the named policy,
        Element typeElement = deviceRAM.getTypeDefinitionElement(policyName);
        if (typeElement == null) {
            throw new IllegalStateException("The type element for " +
                                            policyName + " is null.");
        }
        PolicyTypeComposition composition =
                PolicyTypeComposition.getComposition(typeElement);

        if (composition == null) {
            throw new IllegalStateException(
                        "There is no policy type composition for the policy " +
                    "named " + policyName);
        }

        return composition.createPolicyValueModifier(parent,
                                                    style,
                                                    policyName,
                                                    deviceRAM);
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

 06-Apr-04	3686/5	pcameron	VBM:2004032204 Added new methods to PolicyType and refactored

 01-Apr-04	3602/1	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 23-Mar-04	3523/4	pcameron	VBM:2004030802 JDOM's getChildren() doesn't return null

 ===========================================================================
*/
