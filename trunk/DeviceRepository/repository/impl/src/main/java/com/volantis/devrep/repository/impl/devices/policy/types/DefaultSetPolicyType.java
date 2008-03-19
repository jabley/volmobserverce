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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.impl.devices.policy.types;

import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.devices.policy.types.SetPolicyType;

/**
 * Abstract default implementation of {@link SetPolicyType}.
 */
public abstract class DefaultSetPolicyType
        extends DefaultPolicyType
        implements SetPolicyType {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The policy type of the members of this set.
     */
    private PolicyType memberType;

    /**
     * Initalise.
     *
     * @param memberType the policy type of the members of this set.
     */
    public DefaultSetPolicyType(PolicyType memberType) {
        this.memberType = memberType;
    }

    /**
     * Get the policy type of the members of this set.
     *
     * @return the policy type of the members of this set.
     */
    public PolicyType getMemberPolicyType() {
        return memberType;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 23-Jul-04	4945/1	geoff	VBM:2004072205 Public API for Device Repository: Common Metadata Infrastructure

 ===========================================================================
*/
