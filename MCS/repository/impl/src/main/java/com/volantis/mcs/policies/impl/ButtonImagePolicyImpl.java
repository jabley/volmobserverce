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

import com.volantis.mcs.policies.ButtonImagePolicy;
import com.volantis.mcs.policies.ButtonImagePolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.PolicyVisitor;

public class ButtonImagePolicyImpl
        extends AbstractConcretePolicy
        implements ButtonImagePolicy {

    private final PolicyReference upPolicy;

    private final PolicyReference downPolicy;

    private final PolicyReference overPolicy;

    public ButtonImagePolicyImpl(ButtonImagePolicyBuilder builder) {
        super(builder);
        upPolicy = builder.getUpPolicy();
        downPolicy = builder.getDownPolicy();
        overPolicy = builder.getOverPolicy();

    }

    public PolicyBuilder getPolicyBuilder() {
        return getButtonImagePolicyBuilder();
    }

    public ButtonImagePolicyBuilder getButtonImagePolicyBuilder() {
        return new ButtonImagePolicyBuilderImpl(this);
    }

    public void accept(PolicyVisitor visitor) {
        visitor.visit(this);
    }

    public PolicyReference getUpPolicy() {
        return upPolicy;
    }

    public PolicyReference getDownPolicy() {
        return downPolicy;
    }

    public PolicyReference getOverPolicy() {
        return overPolicy;
    }

    public PolicyType getPolicyType() {
        return PolicyType.ROLLOVER_IMAGE;
    }
}
