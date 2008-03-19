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

import com.volantis.mcs.policies.ConcretePolicyBuilder;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.ConcretePolicy;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.path.Step;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public abstract class AbstractConcretePolicyBuilder
        extends AbstractPolicyBuilder
        implements ConcretePolicyBuilder {

    private List alternatePolicies;
    private transient List externalAlternatePolicies;

    public AbstractConcretePolicyBuilder(ConcretePolicy concretePolicy) {
        super(concretePolicy);

        if (concretePolicy == null) {
            alternatePolicies = new ArrayList();
        } else {
            alternatePolicies = new ArrayList(concretePolicy.getAlternatePolicies());
        }

        externalAlternatePolicies = createExternalList(alternatePolicies);
    }

    /**
     * Make sure that object is in the correct state after being initialised by
     * JIBX.
     */ 
    void jibxPostSet() {
        if (alternatePolicies == null) {
            alternatePolicies = new ArrayList();
        }
        externalAlternatePolicies = createExternalList(alternatePolicies);
    }

    // Javadoc inherited.
    public void addAlternatePolicy(PolicyReference reference) {
        externalAlternatePolicies.add(reference);
    }

    // Javadoc inherited.
    public List getAlternatePolicies() {
        return externalAlternatePolicies;
    }

    public PolicyReference getAlternatePolicy(PolicyType policyType) {
        for (Iterator i = externalAlternatePolicies.iterator(); i.hasNext();) {
            PolicyReference reference = (PolicyReference) i.next();
            if (reference.getExpectedPolicyType() == policyType) {
                return reference;
            }
        }

        return null;
    }

    boolean jibxHasAlternatePolicies() {
        return externalAlternatePolicies != null && !externalAlternatePolicies.isEmpty();
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AbstractConcretePolicyBuilder) ?
                equalsSpecific((AbstractConcretePolicyBuilder) obj) : false;
    }

    /**
     * @see #equalsSpecific(EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AbstractConcretePolicyBuilder policy) {
        return super.equalsSpecific(policy) &&
                equals(externalAlternatePolicies, policy.externalAlternatePolicies);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, externalAlternatePolicies);
        return result;
    }

    /**
     * Validate the alternates.
     *
     * @param context validation context
     */
    protected void validateAlternates(ValidationContext context) {

        int altPolicyCount = alternatePolicies.size();

        // Are there any alternatives to validate?
        if (altPolicyCount > 0) {
            Set allowableAlternates = getAllowableAlternatives();

            Step altStep =
                context.pushPropertyStep(PolicyModel.ALTERNATE_POLICIES);

            HashSet foundAlternates = new HashSet();

            // Iterate over the alternate policies.
            for (int i = 0; i < altPolicyCount; i++) {
                PolicyReference altBuilder =
                    (PolicyReference)alternatePolicies.get(i);

                Step step = context.pushIndexedStep(i);

                // Make sure that the alternate is allowed within this policy
                // type.
                PolicyType altType = altBuilder.getExpectedPolicyType();
                String altName = altBuilder.getName();

                if (!allowableAlternates.contains(altType)) {
                    // Illegal or missing type
                    String typeString = "";
                    if (altType != null) {
                        typeString = altType.toString();
                    }
                    context
                        .addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                            context.createMessage(PolicyMessages.ALTERNATE_TYPE_ILLEGAL,
                                typeString, altName));
                } else if (foundAlternates.contains(altType)) {
                    // Already seen this alternate type
                    context
                        .addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                            context.createMessage(PolicyMessages.ALTERNATE_MULTIPLE_TYPES,
                                altType));
                } else if (altName == null || altName.length() == 0) {
                    // Illegal (missing) alternate name
                        context
                            .addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                                context.createMessage(PolicyMessages.ALTERNATE_NAME_ILLEGAL,
                                    altName, altType));

                } else {
                    foundAlternates.add(altType);
                }

                context.popStep(step);
            }

            context.popStep(altStep);
        }

    }

    /**
     * Returns a set of alternative types which are valid for this Policy.
     *
     * @return a set of policy types, may be null
     */
    protected abstract Set getAllowableAlternatives();

}
