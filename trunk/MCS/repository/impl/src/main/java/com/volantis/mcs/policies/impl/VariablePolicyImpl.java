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
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.PolicyVisitor;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantBuilder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class VariablePolicyImpl
        extends AbstractConcretePolicy
        implements VariablePolicy {

    private VariablePolicyType variablePolicyType;

    /**
     *
     */
    private String categorizationScheme;

    private List variants;

    public VariablePolicyImpl(VariablePolicyBuilder builder) {
        super(builder);
        variablePolicyType = builder.getVariablePolicyType();
        categorizationScheme = builder.getCategorizationScheme();

        List variants = new ArrayList();
        List variantBuilders = builder.getVariantBuilders();
        for (Iterator i = variantBuilders.iterator(); i.hasNext();) {
            VariantBuilder variantBuilder = (VariantBuilder) i.next();
            variants.add(variantBuilder.getVariant());
        }

        this.variants = Collections.unmodifiableList(variants);
    }

    public PolicyBuilder getPolicyBuilder() {
        return getVariablePolicyBuilder();
    }

    public VariablePolicyBuilder getVariablePolicyBuilder() {
        return new VariablePolicyBuilderImpl(this);
    }

    // Javadoc inherited.
    public void accept(PolicyVisitor visitor) {
        visitor.visit(this);
    }

    public VariablePolicyType getVariablePolicyType() {
        return variablePolicyType;
    }

    public List getVariants() {
        return variants;
    }

    public Iterator variantIterator() {
        return variants.iterator();
    }

    public String getCategorizationScheme() {
        return categorizationScheme;
    }

    public PolicyType getPolicyType() {
        return getVariablePolicyType();
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof VariablePolicyImpl) ?
                equalsSpecific((VariablePolicyImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(VariablePolicyImpl other) {
        return super.equalsSpecific(other) &&
                equals(variablePolicyType, other.variablePolicyType) &&
                equals(categorizationScheme, other.categorizationScheme) &&
                equals(variants, other.variants);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, variablePolicyType);
        result = hashCode(result, categorizationScheme);
        result = hashCode(result, variants);
        return result;
    }

}
