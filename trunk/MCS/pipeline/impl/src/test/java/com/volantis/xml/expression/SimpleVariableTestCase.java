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
package com.volantis.xml.expression;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.xml.expression.impl.SimpleVariable;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.MutableExpandedName;

/**
 * Simple test case.
 */
public class SimpleVariableTestCase extends TestCaseAbstract {

    private ExpressionFactory factory;

    protected void setUp() throws Exception {
        super.setUp();

        factory = ExpressionFactory.getDefaultInstance();
    }

    public void testGetName() throws Exception {
        ExpandedName name = new ImmutableExpandedName("uri://",
                                                      "fred");
        ExpandedName other = new MutableExpandedName("uri://",
                                                     "jim");
        Value value = new SimpleStringValue(factory, "hello");
        Variable nameVar = new SimpleVariable(factory,
                                              name,
                                              value);
        Variable otherVar = new SimpleVariable(factory,
                                               other,
                                               value);

        assertEquals("name for nameVar not as",
                     name,
                     nameVar.getName());
        assertTrue("name for nameVar not immutable",
                   nameVar.getName() instanceof ImmutableExpandedName);

        assertEquals("name for otherVar not as",
                     other,
                     otherVar.getName());
        assertTrue("name for otherVar not immutable",
                   otherVar.getName() instanceof ImmutableExpandedName);
    }

    public void testGetValue() throws Exception {
        ExpandedName name = new ImmutableExpandedName("uri://",
                                                      "fred");
        ExpandedName other = new MutableExpandedName("uri://",
                                                     "jim");
        Value value = new SimpleStringValue(factory, "hello");
        Variable nameVar = new SimpleVariable(factory,
                                              name,
                                              value);
        Variable otherVar = new SimpleVariable(factory,
                                               other,
                                               value);

        assertSame("value for nameVar not as",
                   value,
                   nameVar.getValue());
        assertSame("value for otherVar not as",
                   value,
                   otherVar.getValue());
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
