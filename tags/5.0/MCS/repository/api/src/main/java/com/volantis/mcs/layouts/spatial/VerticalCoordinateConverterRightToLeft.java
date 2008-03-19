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

package com.volantis.mcs.layouts.spatial;

/**
 * A {@link CoordinateConverter} that traverses vertically first and then
 * horizontally right to left.
 */
public class VerticalCoordinateConverterRightToLeft
        extends AbstractCoordinateConverter {

    /**
     * Initialise.
     *
     * @param width The width of the 2D coordinate space.
     * @param height The height of the 2D coordinate space.
     */
    public VerticalCoordinateConverterRightToLeft(int width, int height) {
        super(width, height);
    }

    // Javadoc inherited.
    protected int calculatePosition(int x, int y) {
        return (width - x - 1) * height + y;
    }

    // Javadoc inherited.
    public int getX(int position) {
        return width - (position / height) - 1;
    }

    // Javadoc inherited.
    public int getY(int position) {
        return position % height;
    }
}
