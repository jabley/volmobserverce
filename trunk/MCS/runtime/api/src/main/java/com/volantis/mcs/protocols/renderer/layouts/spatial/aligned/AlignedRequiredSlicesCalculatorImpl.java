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

package com.volantis.mcs.protocols.renderer.layouts.spatial.aligned;

import com.volantis.mcs.layouts.spatial.CoordinateConverter;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.GridInstance;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.spatial.RequiredSlices;
import com.volantis.mcs.protocols.renderer.layouts.spatial.RequiredSlicesCalculatorImpl;

/**
 * Calculates the required slices for a spatial iterator containing a single
 */
public class AlignedRequiredSlicesCalculatorImpl
        extends RequiredSlicesCalculatorImpl {

    /**
     * The required rows.
     *
     * @see RequiredSlices
     */
    private final boolean[][] requiredRows;

    /**
     * The required columns.
     *
     * @see RequiredSlices
     */
    private final boolean[][] requiredColumns;

    /**
     * Initialise.
     *
     * @param formatInstance        the spatial iterator instance to be
     *                              processed
     * @param formatRendererContext the context with which the iterator is
     *                              associated
     * @param coordinateConverter   the coordinate converter
     */
    public AlignedRequiredSlicesCalculatorImpl(
            final FormatInstance formatInstance,
            final FormatRendererContext formatRendererContext,
            final CoordinateConverter coordinateConverter) {
        super(formatInstance, formatRendererContext, coordinateConverter);

        requiredRows = new boolean[rows][];
        requiredColumns = new boolean[columns][];
    }

    public RequiredSlices getRequiredSlices() {

        if (calculate()) {
            int totalSubColumnsAcrossAllSpatialColumns = 0;
            for (int i = 0; i < requiredColumns.length; i++) {
                boolean[] subColumns = requiredColumns[i];
                if (subColumns != null) {
                    for (int j = 0; j < subColumns.length; j++) {
                        boolean subColumn = subColumns[j];
                        totalSubColumnsAcrossAllSpatialColumns +=
                                subColumn ? 1 : 0;
                    }
                }
            }

            int maxSubRowsPerSpatialCell = 0;
            for (int i = 0; i < requiredRows.length; i++) {
                boolean[] subRows = requiredRows[i];
                if (subRows != null) {
                    maxSubRowsPerSpatialCell = Math.max(
                            maxSubRowsPerSpatialCell, subRows.length);
                }
            }

            return new AlignedRequiredSlicesImpl(requiredRows, requiredColumns,
                    totalSubColumnsAcrossAllSpatialColumns,
                    maxSubRowsPerSpatialCell);
        } else {
            return null;
        }
    }

    protected void updateInstance(FormatInstance instance, int row, int col) {
        GridInstance grid = (GridInstance) instance;

        boolean[] gridRows = grid.getRequiredRows();
        boolean[] gridCols = grid.getRequiredColumns();

        updateRequiredIndices(row, gridRows, requiredRows);
        updateRequiredIndices(col, gridCols, requiredColumns);
    }

    /**
     * Updates the overall required row or column required indeces from the
     * given grid instance specific values.
     *
     * @param index               the row or column index being updated
     * @param gridRequiredIndices the grid instance row or column required
     *                            indeces
     * @param allRequiredIndices  the overall required row or column required
     *                            indeces
     */
    private void updateRequiredIndices(
            int index,
            boolean[] gridRequiredIndices,
            final boolean[][] allRequiredIndices) {
        final int size = gridRequiredIndices.length;

        if (allRequiredIndices[index] == null) {
            allRequiredIndices[index] = new boolean[size];

            System.arraycopy(gridRequiredIndices,
                    0, allRequiredIndices[index], 0, size);
        } else {
            for (int i = 0; i < size; i++) {
                allRequiredIndices[index][i] |= gridRequiredIndices[i];
            }
        }
    }
}
