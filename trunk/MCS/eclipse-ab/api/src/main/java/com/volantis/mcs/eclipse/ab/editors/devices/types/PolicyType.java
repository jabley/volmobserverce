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
import org.jdom.Namespace;
import org.jdom.input.JDOMFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The PolicyType class is a typesafe enumeration of the available policy types.
 * Each policy type has a createPolicyValueModifier factory method that creates
 * the PolicyValueModifier to use for modifing the policy's values.
 */
public abstract class PolicyType {

    /**
     * A list of PolicyTypes.
     */
    private final static List POLICY_TYPES = new ArrayList(6);

    /**
     * The boolean policy type.
     */
    public static final PolicyType BOOLEAN = new BooleanType();

    /**
     * The emulateEmphasisTag policy type.
     */
    public static final PolicyType EMULATE_EMPHASIS_TAG =
            new EmulateEmphasisTagType();

    /**
     * The int policy type.
     */
    public static final PolicyType INT = new IntType();

    /**
     * The range policy type.
     */
    public static final PolicyType RANGE = new RangeType();

    /**
     * The selection policy type.
     */
    public static final PolicyType SELECTION = new SelectionType();

    /**
     * The text policy type.
     */
    public static final PolicyType TEXT = new TextType();

    /**
     * The name of the policy type.
     */
    private final String name;

    /**
     * Creates a new PolicyType.
     * @param name the name of the policy type. Cannot be null or empty.
     * @throws java.lang.IllegalArgumentException if name is null or empty.
     */
    public PolicyType(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Cannot be null nor empty: " +
                    "name.");
        }
        this.name = name;
        POLICY_TYPES.add(this);
    }

    /**
     * Creates the PolicyValueModifier used to modify values of this policy
     * type.
     * @param parent the parent Composite for the PolicyValueModifier's control.
     *               Cannot be null.
     * @param style the style of the PolicyValueModifier's control.
     * @param policyName the name of the policy. Cannot be null or empty.
     * @param dram the DeviceRepositoryAccessorManager to use, if any, for
     *             retrieving policy information. Can be null.
     * @return the PolicyValueModifier
     * @throws java.lang.IllegalArgumentException if parent is null or
     * policyName is null or empty
     */
    public abstract PolicyValueModifier createPolicyValueModifier(
            Composite parent, int style,
            String policyName,
            DeviceRepositoryAccessorManager dram);

    /**
     * Creates and adds a policy type element for the specified PolicyType
     * to a given "type" element using the given factory and the namespace
     * of type.
     * @param parent the parent element of the type content
     * @param factory the factory to use for creating the type element
     */
    abstract void addPolicyTypeElementImpl(Element parent,
                                           JDOMFactory factory);

    /**
     * Creates the element for this specific policy type using a factory. The
     * element is added to the specified parent element and uses its namespace.
     * @param parent the parent element for the policy type element.
     * Cannot be null.
     * @param factory the factory to use for creating the type element. Cannot
     * be null.
     * @throws IllegalArgumentException if parent or factory is null
     */
    void addPolicyTypeElement(Element parent,
                              JDOMFactory factory) {

        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: type.");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Cannot be null: factory.");
        }

        addPolicyTypeElementImpl(parent, factory);
    }

    /**
     * Adds the named policy to the given policies element with its default
     * value, using the factory and, if necessary, the
     * DeviceRepositoryAccessorManager. The named policy must not already be
     * present.
     * @param element the element on which to add the default value. Cannot be
     * null. Must be a policies element.
     * @param policyName the policy name of interest. Cannot be null or empty.
     * @param factory the factory to use for creating elements. Cannot be null.
     * @param dram the DeviceRepositoryAccessorManager to use. Can be null.
     * @throws IllegalArgumentException if element is null, policyName is null
     * or empty, factory is null, element is not a policies element, or the
     * named policy is already present.
     */
    void addDefaultPolicyValue(Element element,
                               String policyName,
                               JDOMFactory factory,
                               DeviceRepositoryAccessorManager dram) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot be null: element.");
        }

        // Check that element is a policies element.
        if (!element.getName().equals(
                DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME)) {
            throw new IllegalArgumentException("Expected an element named " +
                    DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME +
                    ". Got " + element.getName());
        }

        if (policyName == null || policyName.length() == 0) {
            throw new IllegalArgumentException("Cannot be null nor empty: " +
                    "policyName.");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Cannot be null: factory.");
        }

        final Namespace ns = element.getNamespace();

        // Check that the named policy is not already present.
        Element child = element.getChild(policyName, ns);
        if (child != null) {
            throw new IllegalArgumentException("Element " +
                    DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME +
                    " already has a child named " + policyName);
        }

        // Create the new child policy element using the element's namespace.
        Element policyElement = factory.element(
                DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME, ns);
        // Set the policy name.
        policyElement.setAttribute(
                DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE,
                policyName);

        setDefaultValue(policyElement, factory, dram);

        element.addContent(policyElement);
    }

    /**
     * Adds the default value for this policy type to the specified element
     * using the given factory. This is used by {@link #addDefaultPolicyValue}
     * to add the default value.
     * @param element the element on which to add the default value.
     * @param factory the factory to use.
     * @param dram the DeviceRepositoryAccessorManager to use
     */
    protected abstract void setDefaultValue(Element element,
                                            JDOMFactory factory,
                                            DeviceRepositoryAccessorManager dram);

    /**
     * Gets the name of this policy type.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the policy type for the given name.
     * @param name the name of the policy type to retrieve
     * @return the policy type or null if there is no type for the given name
     */
    public static PolicyType getType(String name) {
        PolicyType type = null;
        if (name != null && name.length() > 0) {
            for (int i = 0; i < POLICY_TYPES.size() && type == null; i++) {
                PolicyType pt = (PolicyType) POLICY_TYPES.get(i);
                type = pt.name.equals(name) ? pt : null;
            }
        }

        return type;
    }

    /**
     * Returns the PolicyType for the given "type" element.
     * @param type the Element that specifies the type
     * @return the appropriate PolicyType or null if the type element did not
     * match a given policy type
     */
    public static PolicyType getType(Element type) {
        return PolicyTypeComposition.getPolicyType(type);
    }

    /**
     * Provide an array of all the PolicyTypes whose PolicyValueStructure is
     * SINGLE_VALUED.
     * @return the array of all SINGLE_VALUED policy types.
     */
    static PolicyType[] getSingleValuedPolicyTypes() {
        ArrayList svpt = new ArrayList();
        for (int i = 0; i < POLICY_TYPES.size(); i++) {
            PolicyType type = (PolicyType) POLICY_TYPES.get(i);
            if (type.getPolicyValueStructure() ==
                    PolicyValueStructure.SINGLE_VALUED) {
                svpt.add(type);
            }
        }

        PolicyType svpta [] = new PolicyType[svpt.size()];
        svpta = (PolicyType[]) svpt.toArray(svpta);

        return svpta;
    }

    /**
     * Provide an array of all the PolicyTypes whose PolicyValueStructure is
     * SINGLE_VALUED except the Boolean type.
     * @return the array of all SINGLE_VALUED policy types without Boolean.
     */
    static PolicyType[] getSingleValuedPolicyTypesWithoutBoolean() {
        ArrayList svpt = new ArrayList();
        for (int i = 0; i < POLICY_TYPES.size(); i++) {
            PolicyType type = (PolicyType) POLICY_TYPES.get(i);
            if (type != BOOLEAN && type.getPolicyValueStructure() ==
                    PolicyValueStructure.SINGLE_VALUED) {
                svpt.add(type);
            }
        }

        PolicyType svpta [] = new PolicyType[svpt.size()];
        svpta = (PolicyType[]) svpt.toArray(svpta);

        return svpta;
    }

    /**
     * Get the PolicyValueStructure for this PolicyType.
     * @return the PolicyValueStructure for this PolicyType.
     */
    abstract PolicyValueStructure getPolicyValueStructure();

    /**
     * Type safe enumerator for specifying the structure of the value supported
     * by a PolicyType.
     */
    static class PolicyValueStructure {
        /**
         * A single valued policy value structure.
         */
        static final PolicyValueStructure SINGLE_VALUED =
                new PolicyValueStructure();

        /**
         * An unspecified policy value structure.
         */
        static final PolicyValueStructure UNSPECIFIED =
                new PolicyValueStructure();

        /**
         * The private constructor.
         */
        private PolicyValueStructure() {
        }
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

 11-May-04	4161/2	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 10-May-04	4068/1	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 29-Apr-04	4103/3	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 26-Apr-04	4001/4	pcameron	VBM:2004032104 Refactored the device policy wizard and page so that no policy element is created or returned

 26-Apr-04	4001/2	pcameron	VBM:2004032104 Added NewDevicePolicyWizard and NewDevicePolicyWizardPage

 22-Apr-04	3975/1	allan	VBM:2004042005 Fix multi-value policy migration and related issues.

 08-Apr-04	3686/5	pcameron	VBM:2004032204 Some further tweaks to PolicyType

 07-Apr-04	3686/3	pcameron	VBM:2004032204 Some minor refactoring to PolicyType

 06-Apr-04	3686/1	pcameron	VBM:2004032204 Added new methods to PolicyType and refactored

 29-Mar-04	3574/1	allan	VBM:2004032401 Completed device repository merging. Needs more testing.

 22-Mar-04	3480/4	pcameron	VBM:2004030410 Added some keyword constants and some element name checking

 22-Mar-04	3480/2	pcameron	VBM:2004030410 Added PolicyType

 ===========================================================================
*/
