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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions;

import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * Provide the default value from an array of Values. This is defined
 * as a class so that it can be used for delegation. Specializations will
 * want to override provideDefaultValue() to implement specific mechanisms
 * for providing the default.
 */
public class DefaultValueProvider {
    /**
     * Provide the default value from an array of Values. If values is null
     * or has a length of <= 1 this will return null.
     * 
     * @param values            An array of Values
     * @return The default Value. Will return null if the default cannot be
     * found - unless an exception is thrown.
     * @throws ExpressionException If there is a problem providing
     * the default Value.
     */
    public Value provideDefaultValue(Value values[])
            throws ExpressionException {
        Value defaultValue = null;
        if(values!=null && values.length>1) {

            defaultValue = provideDefaultValue(values[1]);
        }

        return defaultValue;
    }

    /**
     * Ensure that the default value is returned in the correct format. Will
     * return null if the Value passed in is null.
     *
     * @param value             to be returned in the correct format
     * @return Value that have been converted to the correct format. May be
     * null if the Value passed in was null
     * @throws ExpressionException if there is a problem providing the default
     * value
     */
    public Value provideDefaultValue(Value value) throws ExpressionException {
        Value defaultValue = value;

        if (value != null) {
            if (!(value instanceof Sequence) ||
                 (value instanceof StringValue)) {
                defaultValue = value.stringValue();
            }
        }
        return defaultValue;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Sep-05	9415/2	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Aug-03	1017/2	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 ===========================================================================
*/
