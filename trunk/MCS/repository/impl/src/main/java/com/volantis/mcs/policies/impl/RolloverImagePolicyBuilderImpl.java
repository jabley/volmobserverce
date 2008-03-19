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

package com.volantis.mcs.policies.impl;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyBuilderVisitor;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.RolloverImagePolicy;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;

import java.util.HashSet;
import java.util.Set;

public class RolloverImagePolicyBuilderImpl
        extends AbstractConcretePolicyBuilder
        implements RolloverImagePolicyBuilder {

    // Valid alternatives for Rollover Image Policy
    private static final Set validAlternatives = new HashSet();

    static {
        validAlternatives.add(PolicyType.TEXT);
    }

    private RolloverImagePolicy rolloverImagePolicy;

    private PolicyReference normalPolicy;

    private PolicyReference overPolicy;

    public RolloverImagePolicyBuilderImpl() {
        this(null);
    }

    public RolloverImagePolicyBuilderImpl(
            RolloverImagePolicy rolloverImagePolicy) {
        super(rolloverImagePolicy);

        if (rolloverImagePolicy != null) {
            this.rolloverImagePolicy = rolloverImagePolicy;
            normalPolicy = rolloverImagePolicy.getNormalPolicy();
            overPolicy = rolloverImagePolicy.getOverPolicy();
        }
    }

    public Policy getPolicy() {
        return getRolloverImagePolicy();
    }

    public RolloverImagePolicy getRolloverImagePolicy() {
        if (rolloverImagePolicy == null) {
            // Make sure only valid instances are built.
            validate();
            rolloverImagePolicy = new RolloverImagePolicyImpl(this);
        }

        return rolloverImagePolicy;
    }

    protected void clearBuiltObject() {
        rolloverImagePolicy = null;
    }

    public void accept(PolicyBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public PolicyReference getNormalPolicy() {
        return normalPolicy;
    }

    public void setNormalPolicy(PolicyReference normalPolicy) {
        if (!equals(this.normalPolicy, normalPolicy)) {
            stateChanged();
        }

        this.normalPolicy = normalPolicy;
    }

    public PolicyReference getOverPolicy() {
        return overPolicy;
    }

    public void setOverPolicy(PolicyReference overPolicy) {
        if (!equals(this.overPolicy, overPolicy)) {
            stateChanged();
        }

        this.overPolicy = overPolicy;
    }

    public void validate(ValidationContext context) {
        checkReference(context, normalPolicy, PolicyType.IMAGE, true);
        checkReference(context, overPolicy, PolicyType.IMAGE, true);
    }

    public PolicyType getPolicyType() {
        return PolicyType.ROLLOVER_IMAGE;
    }

    boolean jibxHasNormal() {
        return normalPolicy != null;
    }

    boolean jibxHasOver() {
        return overPolicy != null;
    }

    // Javadoc inherited.
    protected Set getAllowableAlternatives() {
        return validAlternatives;
    }
}
