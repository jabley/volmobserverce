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

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.BaseURLPolicy;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyBuilderVisitor;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.content.BaseLocation;

public class BaseURLPolicyBuilderImpl
        extends AbstractPolicyBuilder
        implements BaseURLPolicyBuilder {

    private BaseURLPolicy baseURLPolicy;

    private String baseURL;

    private BaseLocation baseLocation = BaseLocation.DEFAULT;

    public BaseURLPolicyBuilderImpl() {
        this(null);
    }

    public BaseURLPolicyBuilderImpl(BaseURLPolicy baseURLPolicy) {
        super(baseURLPolicy);

        if (baseURLPolicy != null) {
            this.baseURLPolicy = baseURLPolicy;
            baseURL = baseURLPolicy.getBaseURL();
            baseLocation = baseURLPolicy.getBaseLocation();
        }
    }

    public Policy getPolicy() {
        return getBaseURLPolicy();
    }

    public BaseURLPolicy getBaseURLPolicy() {
        if (baseURLPolicy == null) {
            // Make sure only valid instances are built.
            validate();
            baseURLPolicy = new BaseURLPolicyImpl(this);
        }

        return baseURLPolicy;
    }

    protected void clearBuiltObject() {
        baseURLPolicy = null;
    }

    public void accept(PolicyBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        if (!equals(this.baseURL, baseURL)) {
            stateChanged();
        }

        this.baseURL = baseURL;
    }

    public void validate(ValidationContext context) {
        if (baseURL == null) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(PolicyMessages.BASE_URL_UNSPECIFIED));
        }
    }

    public PolicyType getPolicyType() {
        return PolicyType.BASE_URL;
    }

    public void setBaseLocation(BaseLocation baseLocation) {
        if (baseLocation == null) {
            baseLocation = BaseLocation.DEFAULT;
        }

        if (!equals(this.baseLocation, baseLocation)) {
            stateChanged();
        }

        this.baseLocation = baseLocation;
    }

    public BaseLocation getBaseLocation() {
        return baseLocation;
    }

    void jibxPostSet() {
         if (baseLocation == null) {
             baseLocation = BaseLocation.DEFAULT;
         }
    }

    boolean jibxHasBaseLocation() {
        return baseLocation != null && baseLocation != BaseLocation.DEFAULT;
    }

}
