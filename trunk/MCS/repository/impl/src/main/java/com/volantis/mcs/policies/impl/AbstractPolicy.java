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

import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.PolicyBuilder;

public abstract class AbstractPolicy
        extends EqualsHashCodeBase
        implements InternalPolicy {

    private final String name;

    private final CacheControl cacheControl;

    public AbstractPolicy(PolicyBuilder builder) {
        name = builder.getName();
        CacheControlBuilder cacheControlBuilder =
                builder.getCacheControlBuilder();
        if (cacheControlBuilder == null) {
            cacheControl = null;
        } else {
            cacheControl = cacheControlBuilder.getCacheControl();
        }
    }

    public String getName() {
        return name;
    }

    public CacheControl getCacheControl() {
        return cacheControl;
    }

//    public boolean equals(Object obj) {
//        if (!super.equals(obj)) {
//            return false;
//        }
//
//        if (!(obj instanceof AbstractPolicy)) {
//            return false;
//        }
//
//        AbstractPolicy policy = (AbstractPolicy) obj;
//        return equals(name, policy.name) &&
//                equals(cacheControl, policy.cacheControl);
//    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AbstractPolicy) ?
                equalsSpecific((AbstractPolicy) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AbstractPolicy policy) {
        return super.equalsSpecific(policy) &&
                equals(name, policy.name) &&
                equals(cacheControl, policy.cacheControl);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, name);
        result = hashCode(result, cacheControl);
        return result;
    }
}
