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

import com.volantis.mcs.layouts.spatial.CoordinateConverter;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.spatial.RequiredSlices;
import com.volantis.mcs.protocols.renderer.layouts.spatial.RequiredSlicesCalculatorImpl;

/**
 * Calculates the required slices for a spatial iterator containing a single
 */
public class NestedRequiredSlicesCalculatorImpl
        extends RequiredSlicesCalculatorImpl {

    /**
     * The required rows.
     *
     * @see RequiredSlices
     */
    private final boolean[] requiredRows;

    /**
     * The required columns.
     *
     * @see RequiredSlices
     */
    private final boolean[] requiredColumns;

    /**
     * Initialise.
     *
     * @param formatInstance        the spatial iterator instance to be
     *                              processed
     * @param formatRendererContext the context with which the iterator is
     *                              associated
     * @param coordinateConverter   the coordinate converter
     */
    public NestedRequiredSlicesCalculatorImpl(
            final FormatInstance formatInstance,
            final FormatRendererContext formatRendererContext,
            final CoordinateConverter coordinateConverter) {
        super(formatInstance, formatRendererContext, coordinateConverter);

        requiredRows = new boolean[rows];
        requiredColumns = new boolean[columns];
    }

    public RequiredSlices getRequiredSlices() {

        if (calculate()) {
            return new NestedRequiredSlicesImpl(requiredRows, requiredColumns);
        } else {
            return null;
        }
    }


    protected void updateInstance(FormatInstance instance, int row, int col) {
        requiredRows[row] = true;
        requiredColumns[col] = true;
    }
}
