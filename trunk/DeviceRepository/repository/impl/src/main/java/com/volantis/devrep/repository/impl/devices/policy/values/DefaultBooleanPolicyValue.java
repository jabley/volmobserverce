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

import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.shared.metadata.value.immutable.ImmutableBooleanValue;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;
import com.volantis.mcs.devices.policy.values.BooleanPolicyValue;

/**
 * This class provides the default implementation of a Boolean Policy Value.
 */
public class DefaultBooleanPolicyValue
        extends DefaultSimplePolicyValue
        implements BooleanPolicyValue {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Default true boolean value.
     */
    private static final ImmutableBooleanValue TRUE;

    /**
     * Default false boolean value.
     */
    private static final ImmutableBooleanValue FALSE;

    static {
        MutableBooleanValue booleanValue = VALUE_FACTORY.createBooleanValue();
        booleanValue.setValue(Boolean.TRUE);
        TRUE = (ImmutableBooleanValue) booleanValue.createImmutable();

        booleanValue.setValue(Boolean.FALSE);
        FALSE = (ImmutableBooleanValue) booleanValue.createImmutable();
    }

    /**
     * The value that this object is wrapping, appropriately typed
     */
    protected Boolean value = null;

    /**
     * Initialize a new instance of this immutable value.  Any necessary
     * conversions on the parameter will be done as this object is
     * constructed.
     *
     * @param value The value to include in this object
     */
    DefaultBooleanPolicyValue(String value) {
        this.value = new Boolean(value);
    }

    // JavaDoc inherited
    public String getAsString() {
        return value.toString();
    }

    // JavaDoc inherited
    public Boolean getValueAsBoolean() {
        return value;
    }

    // Javadoc inherited
    public ImmutableMetaDataValue createMetaDataValue() {
        return (value.booleanValue() ? TRUE : FALSE);
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

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
