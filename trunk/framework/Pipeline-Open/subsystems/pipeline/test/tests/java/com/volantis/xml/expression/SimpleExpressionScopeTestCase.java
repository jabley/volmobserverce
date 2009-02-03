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
import com.volantis.xml.expression.impl.SimpleExpressionScope;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;

/**
 * Simple test case.
 */
public class SimpleExpressionScopeTestCase extends TestCaseAbstract {
    private ExpressionFactory factory;

    protected void setUp() throws Exception {
        super.setUp();

        factory = ExpressionFactory.getDefaultInstance();
    }

    public void testGetEnclosingScope() throws Exception {

        ExpressionScope scopeA = new SimpleExpressionScope(factory,
                                                           null);
        ExpressionScope scopeB = new SimpleExpressionScope(factory,
                                                           scopeA);

        assertNull("Scope A should have a null enclosing scope",
                   scopeA.getEnclosingScope());
        assertSame("Scope B's enclosing scope should be scope A",
                   scopeA,
                   scopeB.getEnclosingScope());
    }

    public void testDeclareVariable() throws Exception {

        ExpandedName nameX = new ImmutableExpandedName("", "x");
        ExpandedName nameY = new ImmutableExpandedName("", "y");
        ExpandedName nameX2 = new ImmutableExpandedName("http://myNamespace",
                                                        "x");
        Value x = factory.createStringValue("x");
        Value y = factory.createStringValue("y");
        Value x2 = factory.createStringValue("x2");

        ExpressionScope scopeA = new SimpleExpressionScope(factory,
                                                           null);
        ExpressionScope scopeB = new SimpleExpressionScope(factory,
                                                           scopeA);

        scopeA.declareVariable(nameX, x);

        try {
            scopeB.declareVariable(nameX, x2);
        } catch (IllegalArgumentException e) {
            fail("Declaring variable x in scope B should be OK even though " +
                 "it is declared in scope A");
        }

        try {
            scopeA.declareVariable(nameX2, x2);
        } catch (IllegalArgumentException e) {
            fail("Declaring another variable x but in a different namespace " +
                 "should not cause a problem");
        }

        scopeB.declareVariable(nameY, y);

        try {
            scopeB.declareVariable(nameY, x2);

            fail("Should have had an IllegalArgumentException when an " +
                 "attempt was made to redeclare the y variable in scope B");
        } catch (IllegalArgumentException e) {
            // Expected condition
        }
    }

    public void testResolveVariable() throws Exception {

        ExpandedName nameX = new ImmutableExpandedName("", "x");
        ExpandedName nameY = new ImmutableExpandedName("", "y");
        ExpandedName nameX2 = new ImmutableExpandedName("http://myNamespace",
                                                        "x");
        ExpandedName duff = new ImmutableExpandedName("http://myNamespace",
                                                      "duff");
        Value x = factory.createStringValue("x");
        Value y = factory.createStringValue("y");
        Value x2 = factory.createStringValue("x2");

        ExpressionScope scopeA = new SimpleExpressionScope(factory,
                                                           null);
        ExpressionScope scopeB = new SimpleExpressionScope(factory,
                                                           scopeA);

        scopeA.declareVariable(nameX, x);
        scopeB.declareVariable(nameX, x2);
        scopeA.declareVariable(nameX2, x2);
        scopeB.declareVariable(nameY, y);

        assertSame("Scope A's variable x should have been available",
                   x,
                   scopeA.resolveVariable(nameX).getValue());
        assertSame("Scope A's variable x should be hidden",
                   x2,
                   scopeB.resolveVariable(nameX).getValue());
        assertSame("Scope A's variable x2 should have been available",
                   x2,
                   scopeB.resolveVariable(nameX2).getValue());
        assertNull("The duff variable should not have been available",
                   scopeB.resolveVariable(duff));
        assertNull("Scope A's variable y should not have been available",
                   scopeA.resolveVariable(nameY));
        assertSame("Scope B's variable y should have been available",
                   y,
                   scopeB.resolveVariable(nameY).getValue());
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
