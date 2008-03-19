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
package com.volantis.xml.expression.atomic.numeric;

import com.volantis.xml.expression.atomic.AtomicValue;
import com.volantis.xml.expression.atomic.AtomicValueTestAbstract;
import com.volantis.xml.expression.atomic.StringValue;

/**
 * Simple test case.
 */
public class SimpleDoubleValueTestCase
        extends AtomicValueTestAbstract {
    
    private static double VALUE = 3.1415926d;

    protected AtomicValue createValue() {
        return new SimpleDoubleValue(expressionFactory, VALUE);
    }

    protected String getStringValue() {
        return Double.toString(VALUE);
    }

    public void testAsJavaDouble() throws Exception {
        assertEquals("asJavaDouble not as",
                     VALUE,
                     new SimpleDoubleValue(expressionFactory, VALUE).asJavaDouble(),
                     0.00000001d);
    }

    public void testOutputNaN() throws Exception {
        assertEquals("NaN", getAsJavaString(Double.NaN));
    }

    public void testOutputPositive0() throws Exception {
        assertEquals("0", getAsJavaString(+0D));
    }

    public void testOutputNegative0() throws Exception {
        assertEquals("0", getAsJavaString(-0D));
    }

    public void testOutputPositiveInfinity() throws Exception {
        assertEquals("Infinity", getAsJavaString(Double.POSITIVE_INFINITY));
    }

    public void testOutputNegativeInfinity() throws Exception {
        assertEquals("-Infinity", getAsJavaString(Double.NEGATIVE_INFINITY));
    }

    public void testOutputInteger() throws Exception {
        assertEquals("12345", getAsJavaString(12345));
        assertEquals("1234567890", getAsJavaString(1234567890));
    }

    public void testOutputDouble() throws Exception {
        assertEquals("1.2345678900987654E9", String.valueOf(1234567890.0987654));
        assertEquals("12345.6789", getAsJavaString(12345.6789));
        assertEquals("1234567890.0987654", getAsJavaString(1234567890.0987654));
    }

    private String getAsJavaString(final double d) {
        SimpleDoubleValue value = new SimpleDoubleValue(expressionFactory, d);
        StringValue stringValue = value.stringValue();
        return stringValue.asJavaString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
