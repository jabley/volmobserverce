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
 * Converts a fixed two dimensional set of integer coordinates to and from a
 * position in a one dimensional line.
 *
 * <p>Given the sequence of letters
 * <strong><code>ABCDEFGHIJKLMNOPQRSTUVWXYZ</code></strong>
 * where each letter represents a point on the line, this could be mapped to
 * any of the following grids depending on the orientation and the sizes of
 * the dimensions.</p>
 *
 * <pre>
 *    ABCDE
 *    FGHIJ
 *    KLMNO
 *    PQRTS
 *    UVWXY
 *    Z
 * </pre>
 *
 * <pre>
 *    ABCDEFGHIJKLM
 *    NOPQRSTUVWXYZ
 * </pre>
 *
 * <pre>
 *    AEIMQUY
 *    BFJNRVZ
 *    CGKOSW
 *    DHLPTX
 * </pre>
 *
 * @mock.generate
 */
public interface CoordinateConverter {

    /**
     * Get the 1D position given the 2D coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     *
     * @return The 1D position.
     */
    int getPosition(int x, int y);

    /**
     * Get the 2D x coordinate from the 1D position.
     *
     * @param position The 1D position.
     *
     * @return The 2D x coordinate.
     */
    int getX(int position);


    /**
     * Get the 2D y coordinate from the 1D position.
     *
     * @param position The 1D position.
     *
     * @return The 2D y coordinate.
     */
    int getY(int position);
    
    /**
     * Returns the number of columns.
     * 
     * @return The number of columns.
     */
    int getColumns();
    
    /**
     * Returns the number of rows.
     * 
     * @return The number of rows.
     */
    int getRows();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
