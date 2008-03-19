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

package com.volantis.mcs.layouts.spatial;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base for all {@link CoordinateConverter} tests.
 */
public abstract class CoordinateConverterTestAbstract
        extends TestCaseAbstract {

    /**
     * The width of the coordinate space.
     */
    protected static final int WIDTH = 4;

    /**
     * The height of the coordinate space.
     */
    protected static final int HEIGHT = 5;

    /**
     * Create a {@link CoordinateConverter} for the coordinate space with the
     * specified dimensions.
     *
     * @param width The width of the coordinate space.
     * @param height The height of the coordinate space.
     * @return
     */
    protected abstract CoordinateConverter createCoordinateConverter(
            int width, int height);

    /**
     * Test that error checking is done on creation.
     */
    public void testCreationErrorChecking() {
        doTestErrorChecking(-2, 10);
        doTestErrorChecking(10, -2);
    }

    /**
     * Test that converting 2D coordinates to a position and back again result
     * in the same coordinates.
     */
    public void testRoundTrip2DToPosition() {
        CoordinateConverter converter =
                createCoordinateConverter(WIDTH, HEIGHT);

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                int position = converter.getPosition(x, y);
                int rx = converter.getX(position);
                int ry = converter.getY(position);
                assertEquals("X for position " + position, x, rx);
                assertEquals("Y for position " + position, y, ry);
            }
        }
    }

    /**
     * Test that converting 2D coordinates to a position and back again result
     * in the same coordinates.
     */
    public void testRoundTripPositionTo2D() {
        CoordinateConverter converter =
                createCoordinateConverter(WIDTH, HEIGHT);

        for (int position = 0; position < WIDTH * HEIGHT; position += 1) {
            int x = converter.getX(position);
            int y = converter.getY(position);
            int rp = converter.getPosition(x, y);
            assertEquals("Position for (" + x + ", " + y + ")", position, rp);
        }
    }

    /**
     * Create a coordinate converter with invalid dimensions and fail if an
     * error is not thrown.
     * @param width The width.
     * @param height The height.
     */
    private void doTestErrorChecking(final int width, final int height) {
        try {
            createCoordinateConverter(width, height);
            fail("Did not prevent converter being constructed with width " +
                 width + " and height " + height);
        } catch (IllegalArgumentException expected) {
            // Expected.
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
