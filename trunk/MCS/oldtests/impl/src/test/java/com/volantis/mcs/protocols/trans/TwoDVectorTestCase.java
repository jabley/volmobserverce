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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/trans/TwoDVectorTestCase.java,v 1.2 2002/09/25 16:58:33 philws Exp $
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
 * This is the unit test for the BasicMapperElement class.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class TwoDVectorTestCase extends TestCase {
    public TwoDVectorTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Simple method test.
     */
    public void testAdd() {
        int[][] inserts =
            { { 0, 0, 1 },
              { 1, 3, 6 },
              { 4, 2, 3 },
              { 2, 4, 9 },
              { 4, 5, 8 } };
        int[][] expectedResult =
            { { 1, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 0, 0 },
              { 0, 0, 0, 0, 3, 0 },
              { 0, 6, 0, 0, 0, 0 },
              { 0, 0, 9, 0, 0, 0 },
              { 0, 0, 0, 0, 8, 0 },
              { 0, 0, 0, 0, 0, 0 } };

        TwoDVector vector = new TwoDVector();
        vector.setWidth(4);
        vector.setHeight(5);

        for (int i = 0;
             i < inserts.length;
             i++) {
            vector.add(inserts[i][0],
                       inserts[i][1],
                       new Integer(inserts[i][2]));
            assertNotNull("add/get not balanced (null element)",
                          vector.get(inserts[i][0],
                                     inserts[i][1]));
            assertEquals("add/get not balanced",
                         ((Integer)vector.get(inserts[i][0],
                                              inserts[i][1])).intValue(),
                         inserts[i][2]);
        }

        assertEquals("width should have been expanded",
                     vector.getWidth(),
                     5);
        assertEquals("height should have been expanded",
                     vector.getHeight(),
                     6);

        for (int x = 0;
             x < vector.getWidth();
             x++) {
            for (int y = 0;
                 y < vector.getHeight();
                 y++) {
                Integer value = (Integer)vector.get(x, y);
                int intValue = 0;

                if (value != null) {
                    intValue = value.intValue();
                }

                assertEquals("vector value at (" + x + ", " + y +
                             ") does not match expectation",
                             intValue,
                             expectedResult[y][x]);
            }
        }

        try {
            vector.add(11, 3, new Integer(5));

            fail("Adding an element outside the X range should have caused " +
                 "an exception to be thrown");
        } catch (Exception e) {
            // Expected situation
        }

        try {
            vector.add(5, 7, new Integer(5));

            fail("Adding an element outside the Y range should have caused " +
                 "an exception to be thrown");
        } catch (Exception e) {
            // Expected situation
        }

        vector.add(vector.getWidth(), vector.getHeight(), new Integer(999));
    }

    /**
     * Simple method test.
     */
    public void testGet() {
        // This method has been partially tested by testAdd
        TwoDVector vector = new TwoDVector();

        try {
            Object object = vector.get(0, 0);
            fail("getting from an uninitialized 2D vector should fail");
        } catch (Exception e) {
            // Expected situation
        }

        vector.add(0, 0, new Integer(2));

        Integer integer = (Integer)vector.get(0, 0);

        assertEquals("value returned by get unexpected",
                     integer.intValue(),
                     2);

        try {
            Object object = vector.get(vector.getWidth(), vector.getHeight());
            fail("getting from out of bounds should throw an exception");
        } catch (Exception e) {
            // Expected situation
        }
    }

    /**
     * Simple method test.
     */
    public void testGetWidth() {
        TwoDVector vector = new TwoDVector(3, 5);
        assertEquals("width not correctly initialized",
                     vector.getWidth(),
                     0);
        vector.setWidth(16);
        assertEquals("width not correctly updated",
                     vector.getWidth(),
                     16);
        vector.add(16, 0, new Integer(1));
        assertEquals("width not incremented",
                     vector.getWidth(),
                     17);
    }

    /**
     * Simple method test.
     */
    public void testGetHeight() {
        TwoDVector vector = new TwoDVector(3, 5);
        assertEquals("height not correctly initialized",
                     vector.getHeight(),
                     0);
        vector.setHeight(16);
        assertEquals("height not correctly updated",
                     vector.getHeight(),
                     16);
        vector.add(0, 16, new Integer(1));
        assertEquals("height not incremented",
                     vector.getHeight(),
                     17);
    }

    /**
     * Simple method test.
     */
    public void testGetInitialCapacityY() {
        TwoDVector vector = new TwoDVector(0, 5);
        assertEquals("initial y capacity not set correctly",
                     vector.getInitialCapacityY(),
                     5);
        }

    /**
     * Simple method test.
     */
    public void testClear() {
        TwoDVector vector = new TwoDVector();
        vector.setWidth(20);
        vector.setHeight(35);
        vector.add(15, 23, new Integer(3));

        vector.clear();

        assertEquals("width not cleared",
                     vector.getWidth(),
                     0);
        assertEquals("height not cleared",
                     vector.getHeight(),
                     0);
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
