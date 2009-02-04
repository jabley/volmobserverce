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
package com.volantis.mcs.expression.functions.device;

import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.functions.AbstractFunction;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.expression.sequence.Item;

import java.util.StringTokenizer;

/**
 * Superclass for functions that access device-specific information.
 */
public abstract class DeviceAccessorFunction extends AbstractFunction {

    /**
     * If a policy value is a list of values its string representaion consists
     * of each value separated by a comma.
     */
    private static final String POLICY_VALUE_SEPARATOR = ",";

    /**
     * Creates a {@link Value} representation of the policy value argument.
     * @param policyValue the policy value
     * @return A <code>Value<code> that represents the policy value argument.
     * If the policy value is null an empty sequence is returned. If the policy
     * value is a comma separated list then a sequence is returned. Otherwise
     * a StringValue is returned.
     */
    protected Value createValue(ExpressionFactory factory,
                                String policyValue) {
        Value value = null;
        if (policyValue == null) {
            // if the policy value does not exist then return an empty
            // sequence.
            value = Sequence.EMPTY;
        } else if(policyValue.indexOf(POLICY_VALUE_SEPARATOR) == -1) {
            // policy value is a single policy value.
            // create a StringValue representation of this value.
            value = factory.createStringValue(policyValue);
        } else {
            // policy value is a comma seperated list of items.
            StringTokenizer tokenizer =
                    new StringTokenizer(policyValue,
                                        POLICY_VALUE_SEPARATOR);

            // Item array that will contain the individual policy values.
            Item[] items = new Item[tokenizer.countTokens()];
            // populate the item array
            for(int i=0; tokenizer.hasMoreTokens(); i++) {
                // each item is trimed
                items[i] = factory.createStringValue(
                        tokenizer.nextToken().trim());
            }
            // factor a sequence of items
            value = factory.createSequence(items);
        }
        // return the value
        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Nov-05	10049/3	schaloner	VBM:2005092818 Added a getDeviceName function to the runtime

 03-Nov-05	10049/1	schaloner	VBM:2005092818 Ongoing device name support

 ===========================================================================
*/
