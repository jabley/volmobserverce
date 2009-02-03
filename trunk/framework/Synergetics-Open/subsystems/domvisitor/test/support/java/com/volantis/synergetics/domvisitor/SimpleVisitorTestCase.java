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
package com.volantis.synergetics.domvisitor;

import java.lang.reflect.Method;

/**
 * Checks that the SimpleVisitor returns the correct value from its nextHeader
 * methods.
 */
public class SimpleVisitorTestCase extends VisitorTestAbstract {
    /**
     * This test uses reflection to ensure that the {@link SimpleVisitor}
     * implements all nextHeader methods to return {@link Visitor.Action#CONTINUE
     * CONTINUE}.
     */
    public void testVisit() throws Exception {
        Method[] methods = Visitor.class.getMethods();
        Visitor visitor = new SimpleVisitor();
        Object[] args = { null };

        for (int i = 0;
             i < methods.length;
             i++) {
            Method method = methods[i];

            if (method.getName().equals("nextHeader") &&
                (method.getParameterTypes().length == 1)) {
                Object result = method.invoke(visitor, args);

                assertTrue("result of call to " + method +
                           " not a Visitor.Action",
                           result instanceof Visitor.Action);

                assertSame(method + " didn't return expected value",
                           Visitor.Action.CONTINUE,
                           result);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Apr-05	7572/2	geoff	VBM:2005040612 Device repository merge report: create event model and XML report listeners

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Dec-03	2226/1	philws	VBM:2003121115 Provide JDOM traversal walker/visitor

 ===========================================================================
*/
