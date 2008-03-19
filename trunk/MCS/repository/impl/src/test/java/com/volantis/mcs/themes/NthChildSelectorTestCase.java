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

package com.volantis.mcs.themes;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.themes.impl.DefaultNthChildSelector;

public class NthChildSelectorTestCase
 extends TestCaseAbstract {

    /**
     * Test that 'odd' is a valid expression for nth-child.
     */
    public void testOdd() {
        DefaultNthChildSelector selector = new DefaultNthChildSelector();
        selector.setExpression("odd");
        assertEquals("A", 2, selector.getA());
        assertEquals("B", 1, selector.getB());
    }

    /**
     * Test that 'even' is a valid expression for nth-child.
     */
    public void testEven() {
        DefaultNthChildSelector selector = new DefaultNthChildSelector();
        selector.setExpression("even");
        assertEquals("A", 2, selector.getA());
        assertEquals("B", 0, selector.getB());
    }

    /**
     * Test that 'an' is a valid expression for nth-child.
     */
    public void testAn() {
        DefaultNthChildSelector selector = new DefaultNthChildSelector();
        selector.setExpression("5n");
        assertEquals("A", 5, selector.getA());
        assertEquals("B", 0, selector.getB());
    }

    /**
     * Test that 'b' is a valid expression for nth-child.
     */
    public void testB() {
        DefaultNthChildSelector selector = new DefaultNthChildSelector();
        selector.setExpression("5");
        assertEquals("A", 0, selector.getA());
        assertEquals("B", 5, selector.getB());
    }

    /**
     * Test that 'an+b' is a valid expression for nth-child.
     */
    public void testAnPlusB() {
        DefaultNthChildSelector selector = new DefaultNthChildSelector();
        selector.setExpression("4n+5");
        assertEquals("A", 4, selector.getA());
        assertEquals("B", 5, selector.getB());
    }

    /**
     * Test that 'invalid' is an invalid expression for nth-child.
     */
    public void testInvalid() {
        DefaultNthChildSelector selector = new DefaultNthChildSelector();

        selector.setExpression("invalid");
        // Verify that it succeeds in creating an invalid selector.
        assertEquals(0, selector.getA());
        assertEquals(0, selector.getB());
        assertEquals("invalid", selector.getExpression());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 ===========================================================================
*/
