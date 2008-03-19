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

import com.volantis.mcs.layouts.iterators.IteratorSizeConstraint;

/**
 * Base class for {@link CoordinateConverter} choosers.
 *
 * <p>The size of the coordinate space expands to accomodate the specified
 * number of instances. The direction of expansion is dependent on the
 * row and column constraints.</p> 
 */
public abstract class AbstractVariableConverterChooser
         extends AbstractConverterChooser {
    /**
     * Initialise.
     *
     * @param colsConstraint The column constraint.
     * @param rowsConstraint The row constraint.
     */
    protected AbstractVariableConverterChooser(
        IteratorSizeConstraint colsConstraint,
        IteratorSizeConstraint rowsConstraint) {
        super(colsConstraint, rowsConstraint);
    }

    /**
     * Calculate the size of the 2nd dimension. If a format iterator renders
     * AcrossDown then the 2nd dimension is the number of rows. If a format
     * iterator renders DownAcross then the 2nd dimension is the number of
     * columns. This is why we have to use generic method names and parameters
     * rather than calculateRows etc.
     *
     * <p><strong>NOTE:</strong> This method must be called after {@link
     * #calculateSizeOfDimensionOne} as the firstDim parameter must be the
     * value calculated by that method.</p>
     *
     * @param cells    The number of cells found in the markup
     * @param constraint The constraint on the maximum number of elements
     *                 specified in the 2nd dimension by the Policy Manager.
     * @param firstDim The number of elements that we are rendering in the 1st
     *                 dimension.
     * @return int     The number of elements to render in the 2nd dimension
     */
    protected int calculateSizeOfDimensionTwo(
            int cells, IteratorSizeConstraint constraint, int firstDim) {

        // The Policy Manager specified a variable number of rows.
        //
        // If the number of rows specifed is 0, there is no range
        // checking so the number of rows rendered equals the
        // number of cells/number of columns rounded up to nearest the
        // int.
        //
        // If number of cells/number of columns rounded up to nearest the int
        // <= number of rows (as found in gui), then render this value.
        //
        // Otherwise the number of rows rendered equals the number of rows (as
        // found in gui) i.e. number of rows is max. number rendered.
        //
        // The following relies on double division by zero returning NaN which
        // becomes 0 when converted to an int. 
        int newRows = (int) Math.ceil((double) cells / firstDim);
        return constraint.getConstrained(newRows);
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
