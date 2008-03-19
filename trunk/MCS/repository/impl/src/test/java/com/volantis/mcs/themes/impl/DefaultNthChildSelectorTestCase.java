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
package com.volantis.mcs.themes.impl;

import com.volantis.synergetics.testtools.TestCaseAbstract;

public class DefaultNthChildSelectorTestCase extends TestCaseAbstract {

    /**
     * Ensure that all possible nth-child arguments are handled correctly.
     *
     * @throws Exception if there was a problem running the test
     */
    public void testUpdateCoefficients() throws Exception {

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    int a = k * i;
                    int b = k * j;
                    doTestUpdateCoefficients(a, b);
                }
            }
        }
    }

    /**
     * Test that creating an NthChildSelector with the given arguments is
     * handled correctly.
     *
     * @param a first coefficient of the nth child argument of the form an+b
     * @param b second coefficient of the nth child argument of the form an+b
     */
    public void doTestUpdateCoefficients(int a, int b) {

        final StringBuffer argument = new StringBuffer();

        if (a != 0) {
            argument.append(a).append("n");
        }
        if (b > 0) {
            argument.append("+").append(b);
        } else if (b < 0) {
            argument.append(b);
        }

        System.out.println("Argument is " + argument);
        if (argument.length() == 0) {
            DefaultNthChildSelector selector =
                    new DefaultNthChildSelector(argument.toString());
            assertEquals(0, selector.getA());
            assertEquals(0, selector.getB());
            assertEquals("", selector.getExpression());
        } else {

            DefaultNthChildSelector selector =
                    new DefaultNthChildSelector(argument.toString());
            assertTrue(argument.toString().equals(selector.getExpression()));
            assertEquals(a, selector.getA());
            assertEquals(b, selector.getB());
        }
    }

    public void testUpdateCoefficients2() {
        final String argument = "n-1";
        DefaultNthChildSelector selector =
                    new DefaultNthChildSelector(argument);
        assertTrue(argument.equals(selector.getExpression()));
        assertEquals(1, selector.getA());
        assertEquals(-1, selector.getB());
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 ===========================================================================
*/
