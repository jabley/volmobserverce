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

import com.volantis.mcs.policies.BaseURLPolicy;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.PolicyVisitor;
import com.volantis.mcs.policies.variants.content.BaseLocation;

public class BaseURLPolicyImpl
        extends AbstractPolicy
        implements BaseURLPolicy {

    private final String baseURL;
    private final BaseLocation baseLocation;

    public BaseURLPolicyImpl(BaseURLPolicyBuilder builder) {
        super(builder);
        baseURL = builder.getBaseURL();
        baseLocation = builder.getBaseLocation();
    }

    public PolicyBuilder getPolicyBuilder() {
        return getBaseURLPolicyBuilder();
    }

    public BaseURLPolicyBuilder getBaseURLPolicyBuilder() {
        return new BaseURLPolicyBuilderImpl(this);
    }

    public void accept(PolicyVisitor visitor) {
        visitor.visit(this);
    }

    public String getBaseURL() {
        return baseURL;
    }

    public PolicyType getPolicyType() {
        return PolicyType.BASE_URL;
    }

    public BaseLocation getBaseLocation() {
        return baseLocation;
    }

}
