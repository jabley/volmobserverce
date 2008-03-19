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

/**
 * Test cases for {@link HorizontalCoordinateConverter}.
 */
public class HorizontalCoordinateConverterTestCase
        extends CoordinateConverterTestAbstract {

    // Javadoc inherited.
    protected CoordinateConverter createCoordinateConverter(
            int width, int height) {

        return new HorizontalCoordinateConverter(width, height);
    }

    /**
     * Test that it maps coordinates to positions properly.
     */
    public void testHorizontalCoordinateMapper() {

        HorizontalCoordinateConverter mapper =
                new HorizontalCoordinateConverter(WIDTH, HEIGHT);

        assertEquals("(0, 0)", 0, mapper.getPosition(0, 0));
        assertEquals("(1, 0)", 1, mapper.getPosition(1, 0));
        assertEquals("(0, 1)", WIDTH, mapper.getPosition(0, 1));
        assertEquals("(1, 1)", WIDTH + 1, mapper.getPosition(1, 1));
        assertEquals("(2, 3)", WIDTH * 3 + 2, mapper.getPosition(2, 3));
        assertEquals("(3, 2)", WIDTH * 2 + 3, mapper.getPosition(3, 2));
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
