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

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.policy.values.IntPolicyValue;
import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.shared.metadata.value.mutable.MutableNumberValue;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * This class provides the default implementation of a Integer Policy Value.
 */
public class DefaultIntPolicyValue
        extends DefaultSimplePolicyValue
        implements IntPolicyValue {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            DefaultIntPolicyValue.class);
    
    /**
     * The value that this object is wrapping, appropriately typed
     */
    protected Integer value;

    /**
     * Initialize a new instance of this immutable value.  Any necessary
     * conversions on the parameter will be done as this object is
     * constructed.
     *
     * @param value The value to include in this object
     * @throws DeviceRepositoryException if the given value is not an integer.
     */
    DefaultIntPolicyValue(String value) throws DeviceRepositoryException {
        try {
            this.value = new Integer(value);
        } catch (NumberFormatException nfe) {
            throw new DeviceRepositoryException(
                        exceptionLocalizer.format("value-not-integer", value),
                        nfe);
        }
    }

    // JavaDoc inherited
    public String getAsString() {
        return value.toString();
    }

    // JavaDoc inherited
    public Integer getValueAsInteger() {
        return value;
    }

    // Javadoc inherited
    public ImmutableMetaDataValue createMetaDataValue() {
        MutableNumberValue numberValue = VALUE_FACTORY.createNumberValue();
        numberValue.setValue(value);

        return (ImmutableMetaDataValue) numberValue.createImmutable();
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

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 21-Sep-04	5569/1	pcameron	VBM:2004091719 NumberFormatException handled within device repository PAPI

 20-Sep-04	5563/3	pcameron	VBM:2004091719 NumberFormatExcetion handled within device repository PAPI

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
