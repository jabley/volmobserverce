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


import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.policy.types.BooleanPolicyType;
import com.volantis.mcs.devices.policy.types.IntPolicyType;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.devices.policy.types.RangePolicyType;
import com.volantis.mcs.devices.policy.types.SelectionPolicyType;
import com.volantis.mcs.devices.policy.types.SimplePolicyType;
import com.volantis.mcs.devices.policy.types.TextPolicyType;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.mcs.devices.policy.values.SetPolicyValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class provides an abstract realisation of a Set Policy Value.
 */
public abstract class DefaultSetPolicyValue
        extends DefaultCompositePolicyValue
        implements SetPolicyValue {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The list of values contained within the set
     */
    protected List values = null;

    /**
     * The value as a string with values space separated
     */
    protected String valueString = null;

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
    public DefaultSetPolicyValue(PolicyType memberType, String valueString)
            throws DeviceRepositoryException {
        values = new ArrayList();

        // Convert string of values to list entries...
        StringTokenizer tokeniser = new StringTokenizer(valueString, ",");
        while (tokeniser.hasMoreTokens()) {
            addSetItem(tokeniser.nextToken(), memberType);
        }

        complete();
    }

    /**
     * Adds an item into the list as a correctly initialised and typed policy
     * value.
     *
     * @param item The item to add as a policy value
     * @param type The type of policy value instance to create
     * @throws DeviceRepositoryException if the given item is not of the
     * specified policy type.
     */
    protected void addSetItem(String item, PolicyType type)
            throws DeviceRepositoryException {
        // Validate the type of the items in the set
        if (!(type instanceof SimplePolicyType)) {
            throw new IllegalArgumentException(
                    "Sets can only contain SimplePolicyValues");
        }

        PolicyValue newValue = null;

        // Clean off any whitespace that occurred because of the tokenising
        item = item.trim();
        
        // Create the new value
        if (type instanceof BooleanPolicyType) {
            newValue = new DefaultBooleanPolicyValue(item);
        } else if (type instanceof IntPolicyType) {
            newValue = new DefaultIntPolicyValue(item);
        } else if (type instanceof RangePolicyType) {
            newValue = new DefaultRangePolicyValue(item);
        } else if (type instanceof SelectionPolicyType) {
            newValue = new DefaultSelectionPolicyValue(item);
        } else if (type instanceof TextPolicyType) {
            newValue = new DefaultTextPolicyValue(item);
        }

        // And add it to the list
        if (newValue != null) {
            values.add(newValue);
        }
    }

    // JavaDoc inherited
    protected void complete() {
        ensureIncomplete();
        values = Collections.unmodifiableList(values);
        complete = true;
    }

    // JavaDoc inherited
    public String getAsString() {
        if (valueString == null) {
            StringBuffer buffer =  new StringBuffer();
            for (Iterator i = values.iterator(); i.hasNext(); ) {
                buffer.append(((PolicyValue)i.next()).getAsString());
                if (i.hasNext()) {
                    buffer.append(" ");
                }
            }
            valueString = buffer.toString();
        }
        return valueString;
    }

    // JavaDoc inherited
    public List getValueAsList() {
        ensureComplete();
        return values;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 21-Sep-04	5569/1	pcameron	VBM:2004091719 NumberFormatException handled within device repository PAPI

 20-Sep-04	5563/3	pcameron	VBM:2004091719 NumberFormatExcetion handled within device repository PAPI

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
