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
 * Base for those classes that convert coordinates between 1 and 2 dimensions.
 */
public abstract class AbstractCoordinateConverter
        implements CoordinateConverter {

    /**
     * The width of the coordinate system.
     */
    protected final int width;

    /**
     * The height of the coordinate system.
     */
    protected final int height;

    /**
     * Initialise.
     *
     * @param width The width of the coordinates
     * @param height The height of the coordinates.
     */
    protected AbstractCoordinateConverter(int width, int height) {
        this.width = width;
        this.height = height;

        if (width < 0) {
            throw new IllegalArgumentException(
                    "Width " + width + " is invalid, must be > 0");
        }
        if (height < 0) {
            throw new IllegalArgumentException(
                    "Height " + height + " is invalid, must be > 0");
        }
    }

    // Javadoc inherited.
    public int getPosition(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException(
                    "(" + x + ", " + y + ") is out of the range (0, 0) to (" +
                    width + ", " + height + ")");
        }

        return calculatePosition(x, y);
    }

    /**
     * Override to calculate the 1D position from the 2D coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     *
     * @return The position.
     */
    protected abstract int calculatePosition(int x, int y);
    
    /**
     * @inheritDoc
     */
    public int getColumns() {
        return width;
    }

    /**
     * @inheritDoc
     */
    public int getRows() {
        return height;
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
