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

package com.volantis.devrep.repository.impl.devices.policy.values;

import com.volantis.devrep.repository.api.devices.policy.values.InternalPolicyValue;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.devices.policy.values.UnorderedSetPolicyValue;
import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.shared.metadata.value.mutable.MutableSetValue;

import java.util.Iterator;
import java.util.Set;

/**
 * This class provides the default implementation of an Unordered Set Policy
 * Value.
 */
public class DefaultUnorderedSetPolicyValue
        extends DefaultSetPolicyValue
        implements UnorderedSetPolicyValue {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Initialize a new instance of this immutable value.  Any necessary
     * conversions on the parameter will be done as this object is
     * constructed.
     *
     * @param memberType  The type of each element in the set
     * @param valueString The value to convert to items based on the member
     *                    type and include in this object.
     * @throws DeviceRepositoryException if any of the given values are not of
     * the specified policy type.
     */
    public DefaultUnorderedSetPolicyValue(PolicyType memberType,
                                          String valueString)
            throws DeviceRepositoryException {
        super(memberType, valueString);
    }

    // Javadoc inherited
    public ImmutableMetaDataValue createMetaDataValue() {
        MutableSetValue setValue = VALUE_FACTORY.createSetValue();
        Set contents = setValue.getContentsAsMutableSet();

        for (Iterator i = values.iterator(); i.hasNext();) {
            InternalPolicyValue policyValue = (InternalPolicyValue) i.next();
            contents.add(policyValue.createMetaDataValue());
        }

        return (ImmutableMetaDataValue) setValue.createImmutable();
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

 21-Sep-04	5569/1	pcameron	VBM:2004091719 NumberFormatException handled within device repository PAPI

 20-Sep-04	5563/4	pcameron	VBM:2004091719 NumberFormatExcetion handled within device repository PAPI

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
