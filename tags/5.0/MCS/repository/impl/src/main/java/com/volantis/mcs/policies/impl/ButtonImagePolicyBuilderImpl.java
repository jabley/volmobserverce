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
import com.volantis.mcs.policies.ButtonImagePolicy;
import com.volantis.mcs.policies.ButtonImagePolicyBuilder;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyBuilderVisitor;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;

import java.util.HashSet;
import java.util.Set;

public class ButtonImagePolicyBuilderImpl
    extends AbstractConcretePolicyBuilder
    implements ButtonImagePolicyBuilder {

    // Valid alternatives for Button Image Policy
    private static final Set validAlternatives = new HashSet();

    static {
        validAlternatives.add(PolicyType.TEXT);
    }

    private ButtonImagePolicy buttonImagePolicy;

    private PolicyReference upPolicy;

    private PolicyReference downPolicy;

    private PolicyReference overPolicy;

    public ButtonImagePolicyBuilderImpl(ButtonImagePolicy buttonImagePolicy) {
        super(buttonImagePolicy);
        if (buttonImagePolicy != null) {
            this.buttonImagePolicy = buttonImagePolicy;
            upPolicy = buttonImagePolicy.getUpPolicy();
            downPolicy = buttonImagePolicy.getDownPolicy();
            overPolicy = buttonImagePolicy.getOverPolicy();
        }
    }

    public ButtonImagePolicyBuilderImpl() {
        this(null);
    }

    public Policy getPolicy() {
        return getButtonImagePolicy();
    }

    public ButtonImagePolicy getButtonImagePolicy() {
        if (buttonImagePolicy == null) {
            // Make sure only valid instances are built.
            validate();
            buttonImagePolicy = new ButtonImagePolicyImpl(this);
        }

        return buttonImagePolicy;
    }

    protected void clearBuiltObject() {
        buttonImagePolicy = null;
    }

    public void accept(PolicyBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public PolicyReference getUpPolicy() {
        return upPolicy;
    }

    public void setUpPolicy(PolicyReference upPolicy) {
        if (!equals(this.upPolicy, upPolicy)) {
            stateChanged();
        }

        this.upPolicy = upPolicy;
    }

    public PolicyReference getDownPolicy() {
        return downPolicy;
    }

    public void setDownPolicy(PolicyReference downPolicy) {
        if (!equals(this.downPolicy, downPolicy)) {
            stateChanged();
        }

        this.downPolicy = downPolicy;
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
        checkReference(context, upPolicy, PolicyType.IMAGE, true);
        checkReference(context, overPolicy, PolicyType.IMAGE, true);
        checkReference(context, downPolicy, PolicyType.IMAGE, true);
    }

    public PolicyType getPolicyType() {
        return PolicyType.BUTTON_IMAGE;
    }

    boolean jibxHasUp() {
        return upPolicy != null;
    }

    boolean jibxHasDown() {
        return downPolicy != null;
    }

    boolean jibxHasOver() {
        return overPolicy != null;
    }

    // Javadoc inherited.
    protected Set getAllowableAlternatives() {
        return validAlternatives;
    }
}
