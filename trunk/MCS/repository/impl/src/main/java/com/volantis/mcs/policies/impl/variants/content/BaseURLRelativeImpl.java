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

package com.volantis.mcs.policies.impl.variants.content;

import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.impl.EqualsHashCodeBase;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.BaseURLRelative;
import com.volantis.mcs.policies.variants.content.BaseURLRelativeBuilder;

public abstract class BaseURLRelativeImpl
        extends EqualsHashCodeBase
        implements BaseURLRelative {

    private final PolicyReference baseURLPolicyReference;

    /**
     * The location of the resource.
     */
    private final BaseLocation baseLocation;

    public BaseURLRelativeImpl(BaseURLRelativeBuilder builder) {
        baseURLPolicyReference = builder.getBaseURLPolicyReference();
        baseLocation = builder.getBaseLocation();
    }

    // Javadoc inherited.
    public PolicyReference getBaseURLPolicyReference() {
        return baseURLPolicyReference;
    }

    public BaseLocation getBaseLocation() {
        return baseLocation;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof BaseURLRelativeImpl) ?
                equalsSpecific((BaseURLRelativeImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(BaseURLRelativeImpl other) {
        return super.equalsSpecific(other) &&
                equals(baseURLPolicyReference, other.baseURLPolicyReference) &&
                equals(baseLocation, other.baseLocation);

    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, baseURLPolicyReference);
        result = hashCode(result, baseLocation);
        return result;
    }
}
