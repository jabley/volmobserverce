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

package com.volantis.mcs.runtime.selection;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.PolicyFetcher;
import com.volantis.mcs.runtime.policies.PolicyVariantSelector;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.mcs.runtime.policies.SelectedVariantImpl;
import com.volantis.mcs.runtime.policies.SelectionContext;

public class VariantSelectionPolicyImpl
        implements VariantSelectionPolicy {

    private static final SelectedVariant POLICY_NOT_FOUND =
            new SelectedVariantMarker();

    private final PolicyVariantSelector policyVariantSelector;

    public VariantSelectionPolicyImpl() {
        policyVariantSelector = new PolicyVariantSelector();
    }

    public SelectedVariant retrieveBestObject(
            SelectionContext context,
            RuntimePolicyReference reference,
            EncodingCollection requiredEncodings) {

        PolicyFetcher fetcher = context.getPolicyFetcher();

        // Get the policy, if it could not be found then return immediately.
        // There is no need to cache the fact that it could not be found as
        // that will already have been done in a lower level accessor.
        ActivatedVariablePolicy policy = (ActivatedVariablePolicy)
                fetcher.fetchPolicy(reference);
        if (policy == null) {
            return null;
        }

        // Cache selection.
        final InternalDevice device = context.getDevice();
        Object key = new DeviceSpecificKey(device.getName(), requiredEncodings);
        SelectedVariant selected;
        synchronized (policy) {
            selected = policy.getSelected(key);
            if (selected == null) {
                selected = selectBest(context, policy, requiredEncodings);
                policy.putSelected(key, selected);
            }
        }

        // If nothing was found return the policy wrapped in a variant
        if (selected == POLICY_NOT_FOUND || selected == null) {
            selected = new SelectedVariantImpl(policy,  null, null, null);
        }

        return selected;
    }

    private SelectedVariant selectBest(
            SelectionContext context,
            ActivatedVariablePolicy policy,
            EncodingCollection requiredEncodings) {

        SelectedVariant selection = policyVariantSelector.selectVariant(
                context, policy, requiredEncodings);

        if (selection == null) {

            // Try fallback.
            PolicyType policyType = policy.getPolicyType();
            RuntimePolicyReference fallbackReference = (RuntimePolicyReference)
                    policy.getAlternatePolicy(policyType);
            if (fallbackReference != null) {
                PolicyFetcher fetcher = context.getPolicyFetcher();
                policy = (ActivatedVariablePolicy)
                        fetcher.fetchPolicy(fallbackReference);
                if (policy == null) {
                    selection = POLICY_NOT_FOUND;
                } else {
                    return selectBest(context, policy, requiredEncodings);
                }
            }
        } else {
            Variant variant = selection.getVariant();
            VariantType variantType = variant.getVariantType();
            if (variantType == VariantType.NULL) {
                selection = new SelectedVariantImpl(policy, null,
                        selection.getDevice(), null);
            }
        }

        return selection;
    }
}
