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
package com.volantis.mcs.eclipse.builder.editors.policies;

import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.VariantType;

/**
 * Editor for link policies.
 */
public class LinkPolicyEditor extends VariablePolicyEditor {
    // Javadoc inherited
    protected PolicyType getPolicyType() {
        return VariablePolicyType.LINK;
    }

    // Javadoc inherited
    protected VariantType getDefaultVariantType() {
        return VariantType.LINK;
    }

    // Javadoc inherited
    protected boolean hasMetaData() {
        return false;
    }
}
