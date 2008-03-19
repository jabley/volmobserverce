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

/**
 * Simple test case.
 */
public class SimpleIntValueTestCase extends AtomicValueTestAbstract {
    private static int VALUE = 42;

    protected AtomicValue createValue() {
        return new SimpleIntValue(expressionFactory, VALUE);
    }

    protected String getStringValue() {
        return Integer.toString(VALUE);
    }

    public void testAsJavaInt() throws Exception {
        assertEquals("asJavaInt not as",
                     VALUE,
                     new SimpleIntValue(expressionFactory,
                                        VALUE).asJavaInt());
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
