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

package com.volantis.mcs.protocols.renderer.layouts.spatial;

/**
 * Encapsulates information about the required instances with a grid.
 *
 * @mock.generate
 */
public interface RequiredSlices {

    /**
     * Indicates whether the specified spatial iterator row (which may consist
     * of a number of sub rows) is required.
     *
     * <p>This will return true if any of the sub rows are required. It will
     * only return false if none of the sub rows are required.</p>
     *
     * @param row The row within the spatial format iterator.
     * @return True if the row is required and false otherwise.
     */
    boolean isSpatialRowRequired(int row);

    /**
     * Indicates whether the specified spatial iterator column (which may
     * consist of a number of sub columns) is required.
     *
     * <p>This will return true if any of the sub columns are required. It will
     * only return false if none of the sub columns are required.</p>
     *
     * @param column The column within the spatial format iterator.
     * @return True if the column is required and false otherwise.
     */
    boolean isSpatialColumnRequired(int column);

    /**
     * Return a array of flags indicating which sub-rows of the given spatial
     * iterator row are required.
     *
     * @param row spatial iterator row
     * @return array of required row flags
     */
    boolean[] getSubRows(int row);

    /**
     * Return a array of flags indicating which sub-columns of the given spatial
     * iterator column are required.
     *
     * @param column spatial iterator column
     * @return array of required column flags
     */
    boolean[] getSubColumns(int column);

    int getTotalSubColumnsAcrossAllSpatialColumns();

    int getMaxSubRowsPerSpatialCell();
}
