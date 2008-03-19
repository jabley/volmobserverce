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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.policies;

import com.volantis.mcs.policies.variants.VariantType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The type of a policy.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public class PolicyType {

    private static final List MUTABLE_TYPES = new ArrayList();
    private static final List MUTABLE_VARIABLE_TYPES = new ArrayList();

    /**
     * The policy can only contain {@link VariantType#AUDIO} and
     * {@link VariantType#NULL} variants.
     */
    public static final VariablePolicyType AUDIO = add(
            new VariablePolicyType("Audio"));

    /**
     * The policy can only contain {@link VariantType#CHART} and
     * {@link VariantType#NULL} variants.
     */
    public static final VariablePolicyType CHART = add(
            new VariablePolicyType("Chart"));

    /**
     * The policy can only contain {@link VariantType#IMAGE} and
     * {@link VariantType#NULL} variants.
     */
    public static final VariablePolicyType IMAGE = add(
            new VariablePolicyType("Image"));

    /**
     * The policy can only contain {@link VariantType#LAYOUT} and
     * {@link VariantType#NULL} variants.
     */
    public static final VariablePolicyType LAYOUT = add(
            new VariablePolicyType("Layout"));

    /**
     * The policy can only contain {@link VariantType#LINK} and
     * {@link VariantType#NULL} variants.
     */
    public static final VariablePolicyType LINK = add(
            new VariablePolicyType("Link"));

    /**
     * The policy can contain any component variants types, i.e.
     * <ul>
     * <li>{@link VariantType#AUDIO}</li>
     * <li>{@link VariantType#IMAGE}</li>
     * <li>{@link VariantType#LINK}</li>
     * <li>{@link VariantType#TEXT}</li>
     * <li>{@link VariantType#VIDEO}</li>
     * <li>{@link VariantType#NULL}</li>
     * </ul>
     */
    public static final VariablePolicyType RESOURCE = add(
            new VariablePolicyType("Resource"));

    /**
     * The policy can only contain {@link VariantType#SCRIPT} and
     * {@link VariantType#NULL} variants.
     */
    public static final VariablePolicyType SCRIPT = add(
            new VariablePolicyType("Script"));

    /**
     * The policy can only contain {@link VariantType#TEXT} and
     * {@link VariantType#NULL} variants.
     */
    public static final VariablePolicyType TEXT = add(
            new VariablePolicyType("Text"));

    /**
     * The policy can only contain {@link VariantType#THEME} and
     * {@link VariantType#NULL} variants.
     */
    public static final VariablePolicyType THEME = add(
            new VariablePolicyType("Theme"));

    /**
     * The policy can only contain {@link VariantType#VIDEO} and
     * {@link VariantType#NULL} variants.
     */
    public static final VariablePolicyType VIDEO = add(
            new VariablePolicyType("Video"));

    /**
     * The type of a {@link BaseURLPolicy}.
     */
    public static final SupportingPolicyType BASE_URL = add(
            new SupportingPolicyType("BaseURL"));

    /**
     * The type of a {@link ButtonImagePolicy}.
     */
    public static final CompositePolicyType BUTTON_IMAGE = add(
            new CompositePolicyType("Button"));

    /**
     * The type of a {@link RolloverImagePolicy}.
     */
    public static final CompositePolicyType ROLLOVER_IMAGE = add(
            new CompositePolicyType("Rollover"));

    private static final List ALL_TYPES = Collections.unmodifiableList(
            MUTABLE_TYPES);
    private static final List VARIABLE_TYPES = Collections.unmodifiableList(
            MUTABLE_VARIABLE_TYPES);

    /**
     * The name of the policy.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name.
     */
    public PolicyType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public static Collection getPolicyTypes() {
        return ALL_TYPES;
    }

    public static Collection getVariablePolicyTypes() {
        return VARIABLE_TYPES;
    }

    private static VariablePolicyType add(VariablePolicyType type) {
        addPolicyType(type);
        addVariablePolicyType(type);
        return type;
    }

    private static SupportingPolicyType add(SupportingPolicyType type) {
        addPolicyType(type);
        return type;
    }

    private static CompositePolicyType add(CompositePolicyType type) {
        addPolicyType(type);
        return type;
    }

    private static void addPolicyType(PolicyType type) {
        MUTABLE_TYPES.add(type);
    }

    private static void addVariablePolicyType(PolicyType type) {
        MUTABLE_VARIABLE_TYPES.add(type);
    }
}
