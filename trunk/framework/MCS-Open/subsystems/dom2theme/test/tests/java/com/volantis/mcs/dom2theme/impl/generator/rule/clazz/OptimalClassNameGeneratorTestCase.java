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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom2theme.impl.generator.rule.clazz;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link OptimalClassNameGenerator}.
 */
public class OptimalClassNameGeneratorTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that the class name generation produces the correct results.
     */
    public void testGenerateClassName() throws Exception {

        ClassNameGenerator generator = new OptimalClassNameGenerator();

        String[] expected = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z", "a0", "b0", "c0", "d0",
            "e0", "f0", "g0", "h0", "i0", "j0", "k0", "l0", "m0", "n0",
            "o0", "p0", "q0", "r0", "s0", "t0", "u0", "v0", "w0", "x0",
            "y0", "z0", "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
            "i1", "j1", "k1", "l1", "m1", "n1", "o1", "p1", "q1", "r1",
            "s1", "t1", "u1", "v1", "w1", "x1", "y1", "z1", "a2", "b2"
        };

        for (int i = 0; i < 80; i += 1) {
            assertEquals(i + " mismatch", expected[i],
                    generator.getClassName(i));
        }

        assertEquals("a", generator.getClassName(0));
        assertEquals("z", generator.getClassName(25));
        assertEquals("a0", generator.getClassName(26));
        assertEquals("f2", generator.getClassName(83));
    }

}
