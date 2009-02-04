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

import java.util.ArrayList;
import java.util.List;

/**
 * The PolicyTypeComposition class is a typesafe enumeration of the available
 * composition types for {@link PolicyType}s.
 */
public abstract class PolicyTypeComposition {

    /**
     * A list of all PolicyTypeCompositions.
     */
    private static final List POLICY_TYPE_COMPOSITIONS = new ArrayList(4);

    /**
     * The SingleComposition.
     */
    public static final PolicyTypeComposition SINGLE = new SingleComposition();

    /**
     * The OrderedSetComposition.
     */
    public static final PolicyTypeComposition ORDERED_SET =
            new OrderedSetComposition();

    /**
     * The UnorderedSetComposition.
     */
    public static final PolicyTypeComposition UNORDERED_SET =
            new UnorderedSetComposition();

    /**
     * The StructureComposition.
     */
    public static final PolicyTypeComposition STRUCTURE =
            new StructureComposition();

    /**
     * The name of the composition.
     */
    public final String name;

    /**
     * Creates a new PolicyTypeComposition.
     * @param name the name of the composition
     */
    PolicyTypeComposition(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Cannot be null nor empty: " +
                    "name.");
        }
        this.name = name;
        POLICY_TYPE_COMPOSITIONS.add(this);
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
     * Creates and returns a policy type element for the specified PolicyType
     * using the given factory and namespace. This is used by
     * {@link #addTypeElement} to provide the content for its type element.
     * @param parent the parent element of the type content
     * @param subType the type of policy type element to create
     * @param factory the factory to use for creating the type element
     */
    abstract void addTypeElementImpl(Element parent,
                                     PolicyType subType,
                                     JDOMFactory factory);

    /**
     * Creates the type element using the factory for this policy type with
     * optional subtype content. The type element is added to the
     * specified parent element and the parent's namespace is used for all new
     * content.
     * @param parent the parent element for the type element. Cannot be null.
     * @param type the policy type..
     * @param factory the factory to use for creating the type element. Cannot
     * be null.
     * @throws IllegalArgumentException if parent, subType or factory is null
     */
    public void addTypeElement(Element parent, PolicyType type,
                               JDOMFactory factory) {

        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: parent.");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Cannot be null: factory.");
        }

        Element typeElement =
                factory.element(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME, parent.getNamespace());

        addTypeElementImpl(typeElement, type, factory);

        parent.addContent(typeElement);
    }

    /**
     * Adds the named policy to the given policies element with its default
     * value, using the factory and, if necessary, the
     * DeviceRepositoryAccessorManager. The named policy must not already be
     * present.
     * @param element the element on which to add the default value. Cannot be
     * null. Must be a policies element.
     * @param policyName the policy name of interest. Cannot be null or empty.
     * @param type the PolicyType of the policy
     * @param factory the factory to use for creating elements. Cannot be null.
     * @param dram the DeviceRepositoryAccessorManager to use. Can be null.
     * @throws IllegalArgumentException if element is null, policyName is null
     * or empty, factory is null, element is not a policies element, or the
     * named policy is already present.
     */
    public abstract void addDefaultPolicyValue(Element element, 
                                        String policyName,
                                        PolicyType type,
                                        JDOMFactory factory,
                                        DeviceRepositoryAccessorManager dram);

    /**
     * Gets the policy type composition for the given name.
     * @param name the name of the policy type composition to retrieve
     * @return the policy type composition or null if not found
     */
    public static PolicyTypeComposition getNamedComposition(String name) {
        PolicyTypeComposition ptc = null;
        if (name != null && name.length() > 0) {
            for (int i = 0; i < POLICY_TYPE_COMPOSITIONS.size() &&
                    ptc == null; i++) {
                PolicyTypeComposition comp =
                        (PolicyTypeComposition) POLICY_TYPE_COMPOSITIONS.get(i);
                ptc = comp.name.equals(name) ? comp : null;
            }
        }
        return ptc;
    }

    /**
     * Get the PolicyTypeComposition that understands how to handle a
     * given "type" element.
     * @param typeElement the type element
     * @return the PolicyTypeComposition that knows how to handle the given
     * typeElement.
     */
    public static PolicyTypeComposition getComposition(Element typeElement) {
        if (typeElement == null) {
            throw new IllegalArgumentException("Cannot be null: typeElement");
        }

        if (!typeElement.getName().equals(DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME)) {
            throw new IllegalArgumentException("Expected a " +
                    DeviceRepositorySchemaConstants.POLICY_DEFINITION_TYPE_ELEMENT_NAME +
                    " element for typeElement but was a " +
                    typeElement.getName());
        }

        PolicyTypeComposition ptc = null;
        for (int i = 0; i < POLICY_TYPE_COMPOSITIONS.size() &&
                ptc == null; i++) {
            PolicyTypeComposition composition =
                    (PolicyTypeComposition) POLICY_TYPE_COMPOSITIONS.get(i);
            ptc = composition.canHandleType(typeElement) ? composition :
                    null;
        }

        return ptc;
    }

    /**
     * Used to return a Compositions underlying type. For example if a
     * UnorderedSet composition contained boolean value the
     * {@link PolicyType#BOOLEAN} type would be returned.
     * <p><strong>This should not be invoked directly by clients. Instead
     * the static {@link #getPolicyType} method should be used</p></strong>
     * @param type the type element whose underlying type is required
     * @return the underlying PolicyType.
     */
    abstract PolicyType getPolicyTypeImpl(Element type);

    /**
     * Returns the PolicyType that the type element specifies.
     * @param type the type element. Cannot be null
     * @return the PolicyType of null if the element does not reference a
     * known policy type.
     */
    public static PolicyType getPolicyType(Element type) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (!DeviceRepositorySchemaConstants.
                POLICY_DEFINITION_TYPE_ELEMENT_NAME.equals(type.getName())) {
            throw new IllegalArgumentException(
                    "expected a 'type' element but got a '" +
                    type.getName() + "' element");
        }

        PolicyType policyType = null;
        for (int i = 0;
             i < POLICY_TYPE_COMPOSITIONS.size() && policyType == null;
             i++) {

            PolicyTypeComposition composition =
                        (PolicyTypeComposition) POLICY_TYPE_COMPOSITIONS.get(i);
            if (composition.canHandleType(type)) {
                policyType = composition.getPolicyTypeImpl(type);
            }
        }
        return policyType;
    }

    /**
     * Get the PolicyTypeComposition that understands how to handle a
     * particular policy type.
     *
     * Note that this version of getComposition uses policy type names
     * rather than "type" elements and cannot be utilized by the
     * getCompoistion(Element) version. This is because some "type" elements
     * specify their policy type using a reference attribute rather than
     * a child element.
     *
     * @param policyTypeName the name of the policy type
     * @return the PolicyTypeComposition that knows how to handle the named
     * policy type.
     */
    public static PolicyTypeComposition getComposition(String policyTypeName) {
        PolicyTypeComposition ptc = null;
        for (int i = 0; i < POLICY_TYPE_COMPOSITIONS.size() &&
                ptc == null; i++) {
            PolicyTypeComposition composition =
                    (PolicyTypeComposition) POLICY_TYPE_COMPOSITIONS.get(i);
            ptc = composition.canHandleType(policyTypeName) ? composition :
                    null;
        }

        return ptc;
    }

    /**
     * Determine whether or not a PolicyTypeComposition can handle a named
     * policy type.
     *
     * NOTE: this is not the same as policy types that are supported by
     * a particular compostion. Supported policy types are the kinds of
     * PolicyType that a composition can be comprized of whereas handleable
     * PolicyTypes are refer to imeadiate children of policy type elements
     * (e.g. orderedSet, text, etc) that are understood by a particular
     * PolicyTypeComposition. For example an OrderedSetComposition
     * supports all single valued policy types but it handles only
     * orderedSet policy type elements.
     *
     * @param policyTypeName the name of the policy type
     * @return true if the PolicyTypeComposition can handle the named policy
     * type; false otherwise.
     */
    abstract boolean canHandleType(String policyTypeName);

    /**
     * Determine whether or not a PolicyTypeComposition can handle a given
     * "type" element.
     *
     * NOTE: this is not the same as policy types that are supported by
     * a particular compostion. Supported policy types are the kinds of
     * PolicyType that a composition can be comprized of whereas handleable
     * PolicyTypes are refer to imeadiate children of policy type elements
     * (e.g. orderedSet, text, etc) that are understood by a particular
     * PolicyTypeComposition. For example an OrderedSetComposition
     * supports all single valued policy types but it handles only
     * orderedSet policy type elements.
     *
     * @param type the type element
     * @return true if the PolicyTypeComposition can handle the named policy
     * type; false otherwise.
     */
    abstract boolean canHandleType(Element type);

    /**
     * Gets all available policy type compositions.
     * @return an array of all compositions
     */
    public static PolicyTypeComposition[] getPolicyTypeCompositions() {
        PolicyTypeComposition[] ptcArray =
                new PolicyTypeComposition[POLICY_TYPE_COMPOSITIONS.size()];
        POLICY_TYPE_COMPOSITIONS.toArray(ptcArray);

        return ptcArray;
    }

    /**
     * Provide the PolicyTypes that are supported by this PolicyTypeComposition.
     * @return the PolicyTypes supported by this PolicyTypeComposition.
     */
    public abstract PolicyType[] getSupportedPolicyTypes();
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

 10-May-04	4068/5	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/3	allan	VBM:2004032103 Structure page policies section.

 29-Apr-04	4103/1	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 26-Apr-04	4001/3	pcameron	VBM:2004032104 Refactored the device policy wizard and page so that no policy element is created or returned

 26-Apr-04	4001/1	pcameron	VBM:2004032104 Added NewDevicePolicyWizard and NewDevicePolicyWizardPage

 ===========================================================================
*/
