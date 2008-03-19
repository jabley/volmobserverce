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

import com.volantis.mcs.policies.ConcretePolicy;
import com.volantis.mcs.policies.ConcretePolicyBuilder;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractConcretePolicy
        extends AbstractPolicy
        implements ConcretePolicy {

    private final List alternatePolicies;

    public AbstractConcretePolicy(ConcretePolicyBuilder builder) {
        super(builder);

        alternatePolicies = Collections.unmodifiableList(
                builder.getAlternatePolicies());
    }

    // Javadoc inherited.
    public List getAlternatePolicies() {
        return alternatePolicies;
    }

    public PolicyReference getAlternatePolicy(PolicyType policyType) {
        for (Iterator i = alternatePolicies.iterator(); i.hasNext();) {
            PolicyReference reference = (PolicyReference) i.next();
            if (reference.getExpectedPolicyType() == policyType) {
                return reference;
            }
        }
        
        return null;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AbstractConcretePolicy) ?
                equalsSpecific((AbstractConcretePolicy) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AbstractConcretePolicy policy) {
        return super.equalsSpecific(policy) &&
                equals(alternatePolicies, policy.alternatePolicies);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, alternatePolicies);
        return result;
    }
}
