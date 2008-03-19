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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/trans/LCMImplTestCase.java,v 1.2 2002/09/25 16:58:33 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Sep-02    Phil W-S        VBM:2002091901 - Created. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;

import junit.framework.*;

/**
 * This is the unit test for the LCMImpl class and LCM interface.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class LCMImplTestCase extends TestCase {
    public LCMImplTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Ensures that the singleton mechanism works.
     */
    public void testGetInstance() {
        assertNotNull("The singleton should exist but is null",
            LCMImpl.getInstance());
        assertTrue("The singleton should implement LCM but does not",
            LCMImpl.getInstance() instanceof LCM);
    }

    /**
     * Tests the LCM calculation embodied in the LCM implementation.
     */
    public void testGetLCM() {
        LCM lcm = LCMImpl.getInstance();

        int[][] quantities =
            { { 2, 3 },
              { 30, 45 },
              { 3, 4, 10, 22 } };

        // For a hint about how to work this out by hand, see
        // http://www.math.com/school/subject1/lessons/S1U3L3DP.html
        int[] expectedResult =
            { 2 * 3,
              2 * 3 * 3 * 5,
              2 * 2 * 3 * 5 * 11 };
        int result;

        for (int i = 0;
             i < expectedResult.length;
             i++) {
            result = lcm.getLCM(quantities[i]);
            assertEquals("the LCM calculation failed.",
                         expectedResult[i],
                         result);
        }
    }

    /**
     * Tests the underlying prime factor calculation used by the LCM
     * calculation.
     */
    public void testGetPrimeFactors() {
        LCMImpl lcm = (LCMImpl)LCMImpl.getInstance();

        int[] number = { 30, 45 };

        // For a hint about how to work this out by hand, see
        // http://www.math.com/school/subject1/lessons/S1U3L3DP.html
        int[][] expectedResult =
            { { 2, 3, 5 },
              { 3, 3, 5 } };
        int[] result;

        for (int i = 0;
             i < number.length;
             i++) {
            result = lcm.getPrimeFactors(number[i]);

            assertEquals("the number of factors received was wrong.",
                         expectedResult[i].length,
                         result.length);
            for (int j = 0;
                 j < result.length;
                 j++) {
                assertEquals("factor " + j + " was wrong.",
                             expectedResult[i][j],
                             result[j]);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
