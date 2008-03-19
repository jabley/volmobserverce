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

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.PolicyVisitor;
import com.volantis.mcs.policies.RolloverImagePolicy;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;

public class RolloverImagePolicyImpl
        extends AbstractConcretePolicy
        implements RolloverImagePolicy {

    private final PolicyReference normalPolicy;

    private final PolicyReference overPolicy;

    public RolloverImagePolicyImpl(RolloverImagePolicyBuilder builder) {
        super(builder);
        normalPolicy = builder.getNormalPolicy();
        overPolicy = builder.getOverPolicy();
    }

    public PolicyBuilder getPolicyBuilder() {
        return getRolloverImagePolicyBuilder();
    }

    public RolloverImagePolicyBuilder getRolloverImagePolicyBuilder() {
        return new RolloverImagePolicyBuilderImpl(this);
    }

    public void accept(PolicyVisitor visitor) {
        visitor.visit(this);
    }

    public PolicyReference getNormalPolicy() {
        return normalPolicy;
    }

    public PolicyReference getOverPolicy() {
        return overPolicy;
    }

    public PolicyType getPolicyType() {
        return PolicyType.ROLLOVER_IMAGE;
    }
}
