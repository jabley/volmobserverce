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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/TwoDVector.java,v 1.2 2002/09/25 16:58:33 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;
import java.util.Vector;

/**
 * Provides a "two dimensional" version of the standard Java Vector class (with a much smaller interface). Iteration is performed using indices only.
 * 
 * The vector will only grow in either direction by a single index at a time.
 * 
 * Any cells within the bounds of the vector's dimensions that have not had a value set for them will return a null value.
 */
public final class TwoDVector {
    /**
     * Basic initialization. 
     */
    public TwoDVector() {
        vectors = new Vector();
    }

    /**
     * The initial capacity hints can be given to improve insertion performance. 
     */
    public TwoDVector(int xInitialCapacity, int yInitialCapacity) {
        vectors = new Vector(xInitialCapacity);
        initialCapacityY = yInitialCapacity;
    }

    /**
     * The given object is added to the grid at the given (x, y) location. If the location already exists its content is replaced. Indices start at zero. An exception will be thrown if x is outside [0..getWidth()] or y is outside [0..getHeight()]. If x equals getWidth(), the width is incremented. If y equals getHeight(), the height is incremented.
     */
    public void add(int x, int y, Object object)
        throws ArrayIndexOutOfBoundsException {
        if (x > width) {
            throw new ArrayIndexOutOfBoundsException("x index " + x +
                                                     " out of bounds (0.." +
                                                     (width - 1) + ")");
        } else if (y > height) {
            throw new ArrayIndexOutOfBoundsException("y index " + y +
                                                     " out of bounds (0.." +
                                                     (height - 1) + ")");
        } else {
            Vector vector;

            if (x >= vectors.size()) {
                vectors.setSize(x + 1);
            }

            vector = (Vector)vectors.get(x);

            if (vector == null) {
                if (initialCapacityY != 0) {
                    vectors.set(x, vector = new Vector(initialCapacityY));
                } else {
                    vectors.set(x, vector = new Vector());
                }
            }

            if (y >= vector.size()) {
                vector.setSize(y + 1);
            }

            vector.set(y, object);

            if (x == width) {
                width++;
            }

            if (y == height) {
                height++;
            }
        }
    }

    /**
     * The object in the grid at the given (x, y) location is returned. Indices start at zero. If the indices are within range but have never been visited, null is returned. An exception will be thrown if x is outside [0..getWidth() - 1] or y is outside [0..getHeight() - 1].
     */
    public Object get(int x, int y) throws ArrayIndexOutOfBoundsException {
        Object result = null;

        if (x >= width) {
            throw new ArrayIndexOutOfBoundsException("x index " + x +
                                                     " out of bounds (0.." +
                                                     (width - 1) + ")");
        } else if (y >= height) {
            throw new ArrayIndexOutOfBoundsException("y index " + y +
                                                     " out of bounds (0.." +
                                                     (height - 1) + ")");
        } else if (x < vectors.size()) {
            Vector vector = (Vector)vectors.get(x);

            if ((vector != null) &&
                (y < vector.size())) {
                result = vector.get(y);
            }
        }

        return result;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;

        if (vectors.size() > width) {
            vectors.setSize(width);
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        int size = vectors.size();

        this.height = height;

        for (int x = 0;
             x < size;
             x++) {
            Vector vector = (Vector)vectors.get(x);

            if ((vector != null) &&
                (vector.size() > height)) {
                vector.setSize(height);
            }
        }

    }

    protected int getInitialCapacityY() {
        return initialCapacityY;
    }

    /**
     * Empties the 2D vector, resetting width and height to 0. 
     */
    public void clear() {
        int size = vectors.size();

        for (int x = 0;
             x < size;
             x++) {
            Vector vector = (Vector)vectors.get(x);

            if (vector != null) {
                vector.clear();
            }
        }

        vectors.clear();

        width = 0;
        height = 0;
    }

    /**
     * The set of vectors used to implement the grid.
     * @associates Vector
     */
    private final Vector vectors;

    /**
     * The height of the two-D grid
     */
    private int height;

    /**
     * The width of the two-D grid
     */
    private int width;

    /**
     * The initial capacity that should be applied against lazy instantiation
     * of the vectors held in the vectors aggregation.
     */
    private int initialCapacityY;
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
