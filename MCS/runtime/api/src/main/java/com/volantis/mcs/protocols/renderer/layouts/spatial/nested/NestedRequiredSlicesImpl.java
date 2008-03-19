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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.layouts.spatial.nested;

import com.volantis.mcs.protocols.renderer.layouts.spatial.RequiredSlices;

/**
 * Implementation of {@link RequiredSlices}.
 *
 * The following grid is nested within the spatial iterator and contains two
 * panes, A and B.
 *
 * <pre>
 * +----+----+
 * | A  | B  |
 * +----+----+
 * </pre>
 *
 * The following is the result after the spatial iterator has iterated over 6
 * instances of the grid across then down with a maximum columns size of 3.
 *
 * <pre>
 * <--------- 3 columns --------->
 * +====+====+====+====+====+====+  ^
 * # A0 | B0 # A1 | A1 # A2 | A2 #  |
 * +====+====+====+====+====+====+  | 2 rows
 * # A3 | B3 # A4 | B4 # A5 | B5 #  |
 * +====+====+====+====+====+====+  v
 * </pre>
 *
 * The index for the outer most array is the row / column within the spatial
 * iterator. i.e. in the above example the rows will range from 0 to 1 and
 * columns from 0 to 2.
 *
 * The index for the inner most array is the row / column within the nested
 * grid. i.e. in the above example the rows will be 0 and the columns will range
 * from 0 to 1.
 */
public class NestedRequiredSlicesImpl
        implements RequiredSlices {

    /**
     * The required rows.
     */
    private final boolean[] requiredRows;

    /**
     * The required columns.
     */
    private final boolean[] requiredColumns;

    public NestedRequiredSlicesImpl(
            boolean[] requiredRows,
            boolean[] requiredCols) {

        this.requiredRows = requiredRows;
        this.requiredColumns = requiredCols;
    }

    // Javadoc inherited.
    public boolean isSpatialRowRequired(int row) {
        return requiredRows[row];
    }

    // Javadoc inherited.
    public boolean isSpatialColumnRequired(int column) {
        return requiredColumns[column];
    }

    public boolean[] getSubColumns(int column) {
        throw new UnsupportedOperationException();
    }


    public boolean[] getSubRows(int row) {
        throw new UnsupportedOperationException();
    }

    public int getTotalSubColumnsAcrossAllSpatialColumns() {
        throw new UnsupportedOperationException();
//        return totalSubColumnsAcrossAllSpatialColumns;
    }

    public int getMaxSubRowsPerSpatialCell() {
        throw new UnsupportedOperationException();
//        return maxSubRowsPerSpatialCell;
    }
}
