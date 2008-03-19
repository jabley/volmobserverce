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

package com.volantis.mcs.policies.impl.validation;

import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantType;

import java.util.Set;

public class VariablePolicyValidator {

    private final Set allowableVariants;

    public VariablePolicyValidator(Set allowableVariants) {
        this.allowableVariants = allowableVariants;
    }

    /**
     * Check that the variants is of a valid type to be added to the policy.
     *
     * @param reporter The object to which any diagnostics are sent.
     * @param policy   The policy to which the variants is being added.
     * @param variant  The variants being added.
     */
    public void checkVariantAllowed(
            DiagnosticReporter reporter, VariablePolicy policy,
            Variant variant) {

        VariantType variantType = variant.getVariantType();
        if (allowableVariants.contains(variantType)) {
            return;
        }

        // The variants type is not allowed.
        error(reporter, PolicyMessages.VARIANT_TYPE_NOT_ALLOWED,
                new Object[]{
                    policy.getName(),
                    variantType
                });
    }

    protected void error(
            DiagnosticReporter reporter, String key, Object[] args) {
        if (reporter == null) {
            throw new IllegalArgumentException(
                    PolicyMessages.EXCEPTION_LOCALIZER.format(key, args));
        } else {
            reporter.error(key, args);
        }
    }
}
