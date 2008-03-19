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
 * A {@link CoordinateConverter} that traverses horizontally first and then
 * vertically.
 */
public class HorizontalCoordinateConverter
        extends AbstractCoordinateConverter {

    /**
     * Initialise.
     *
     * @param width The width of the 2D coordinate space.
     * @param height The height of the 2D coordinate space.
     */
    public HorizontalCoordinateConverter(int width, int height) {
        super(width, height);
    }

    // Javadoc inherited.
    protected int calculatePosition(int x, int y) {
        return x + y * width;
    }

    // Javadoc inherited.
    public int getX(int position) {
        return position % width;
    }

    // Javadoc inherited.
    public int getY(int position) {
        return position / width;
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
