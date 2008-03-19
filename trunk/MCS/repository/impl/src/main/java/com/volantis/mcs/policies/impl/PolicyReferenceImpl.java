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

import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;

public class PolicyReferenceImpl
        extends EqualsHashCodeBase
        implements PolicyReference {

    private PolicyType expectedPolicyType;

    private String name;

    /**
     * JiBX.
     */
    PolicyReferenceImpl() {
    }

    public PolicyReferenceImpl(String name, PolicyType expectedType) {
        this.name = name;
        this.expectedPolicyType = expectedType;
    }

    public String getName() {
        return name;
    }

    public PolicyType getExpectedPolicyType() {
        return expectedPolicyType;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof PolicyReferenceImpl) ?
                equalsSpecific((PolicyReferenceImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(PolicyReferenceImpl other) {
        return super.equalsSpecific(other) &&
                equals(name, other.name) &&
                equals(expectedPolicyType, other.expectedPolicyType);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result =  super.hashCode();
        result = hashCode(result, name);
        result = hashCode(result, expectedPolicyType);
        return result;
    }
                                                   
    public String toString() {
        return super.toString() + "[" + name + ", " + expectedPolicyType + "]";
    }
}
