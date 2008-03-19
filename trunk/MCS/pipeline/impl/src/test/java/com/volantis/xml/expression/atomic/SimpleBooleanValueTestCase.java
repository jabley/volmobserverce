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
package com.volantis.xml.expression.atomic;



/**
 * Simple test case.
 */
public class SimpleBooleanValueTestCase extends AtomicValueTestAbstract {

    protected BooleanValue createValue(boolean bool) {
        return new SimpleBooleanValue(expressionFactory, bool);
    }

    protected AtomicValue createValue() {
        return createValue(true);
    }

    protected String getStringValue() {
        return "true";
    }

    public void testAsJavaBoolean() throws Exception {
        assertTrue("true not true!",
                   createValue(true).asJavaBoolean());
        assertFalse("false not false!",
                    createValue(false).asJavaBoolean());
    }

    public void testGetStringValueFalse() throws Exception {
        assertEquals("SimpleBooleanValue(false).stringValue() not as",
                     "false",
                     createValue(false).stringValue().
                     asJavaString());
    }

    public void testBooleanValueConstants() throws Exception {
        assertEquals("SimpleBooleanValue(true) not BooleanValue.TRUE",
                     createValue(true).asJavaBoolean(),
                     BooleanValue.TRUE.asJavaBoolean());
        assertEquals("SimpleBooleanValue(false) not BooleanValue.FALSE",
                     createValue(false).asJavaBoolean(),
                     BooleanValue.FALSE.asJavaBoolean());
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
