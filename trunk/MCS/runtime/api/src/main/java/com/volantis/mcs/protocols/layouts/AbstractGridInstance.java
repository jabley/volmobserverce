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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Oct-01    Paul            VBM:2001102901 - Created.
 * 28-Feb-02    Paul            VBM:2002022804 - Made release method
 *                              consistently call super.release after it has
 *                              released this class's resources.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 31-Oct-02    Sumit           VBM:2002111103 - isEmptyImpl now takes
 *                              a FormatInstanceReference as an ignored param
 * 29-Nov-02    Sumit           VBM:2002112806 - calculateOutput, getRequired
 *                              Rows/Columns keeps track of FIR. Added a set
 *                              calculated that maintains FIR list
 * 02-Jun-03    Byron           VBM:2003042910 - Modified calculateOutput to
 *                              always set the requiredRows/Columns.
 * 04-Jun-03    Byron           VBM:2003042910 - Modified calculateOutput to
 *                              always reset the requiredRows/Columns.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.AbstractGrid;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;

/**
 * Abstract some of the common contextual information for Grid subclasses.
 *
 * @mock.generate base="FormatInstance"
 */
public abstract class AbstractGridInstance
        extends FormatInstance {

    /**
     * The number of rows in the grid.
     */
    private int rows;

    /**
     * The number of columns in the grid.
     */
    private int columns;

    /**
     * An array of flags which determine whether a row is required
     * (i.e. is not completeley empty) or not.
     */
    private boolean[] requiredRows;

    /**
     * An array of flags which determine whether a column is required
     * (i.e. is not completeley empty) or not.
     */
    private boolean[] requiredColumns;
    /**
     * Indicates whether the set of required rows and columns 
     * for a FormatInstanceIterator has been calculated yet.
     */
    private boolean calculated;

    /**
     * Create a new <code>AbstractGridInstance</code>.
     */
    public AbstractGridInstance(NDimensionalIndex index) {
        super(index);
    }

    // Javadoc inherited from super class.
    public void initialise() {
        super.initialise();

        AbstractGrid grid = (AbstractGrid) format;

        rows = grid.getRows();
        columns = grid.getColumns();
        requiredRows = new boolean[rows];
        requiredColumns = new boolean[columns];
    }

    /**
     * Get the array of flags which determine whether a row is required or
     * not.
     * <p>
     * This calculates the array if necessary.
     * </p>
     * @return The array of flags which determine whether a row is required or
     * not.
     */
    public boolean[] getRequiredRows() {

        if (!calculated) {
            calculateOutput();
        }

        return requiredRows;
    }

    /**
     * Return flag indicating if a row is required.
     *
     * @param position of row to test
     * @return true if the row is required.
     * @throws IndexOutOfBoundsException if the position is <0 or >rowCount
     */
    public boolean isRowRequired(int position) {
        if (!calculated) {
            calculateOutput();
        }

        return requiredRows[position];
    }

    /**
     * Get the array of flags which determine whether a column is required or
     * not.
     * <p>
     * This calculates the array if necessary.
     * </p>
     * @return The array of flags which determine whether a column is required or
     * not.
     */
    public boolean[] getRequiredColumns() {

        if (!calculated) {
            calculateOutput();
        }

        return requiredColumns;
    }

    /**
     * Calculate which rows and which columns need to be output.
     */
    protected void calculateOutput() {

        AbstractGrid grid = (AbstractGrid) format;

        // We need to reset the required rows and columns array to 'not required'
        // in order for this method to work for spatial iterators. This  method
        // sets the values of the column/row to be true only if that row/column
        // is not empty. Given that there isn't an array of columns for each row,
        // this is preferred method. Ideally we should store an array of columns
        // for each row and if one column is required then that row is required.
        // The additional memory and processing required to achieve this probably
        // outweighs simply resetting the arrays below (most rows and columns
        // should contain only a few items).
        for (int i = 0; i < requiredColumns.length; i++) {
            requiredColumns[i] = false;
        }

        for (int i = 0; i < requiredRows.length; i++) {
            requiredRows[i] = false;
        }

        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < columns; c++) {
                int gridIndex = r * columns + c;
                Format child = grid.getChildAt(gridIndex);

                // Ignore children which do not exist.
                if (child == null) {
                    continue;
                }

                // If the child is not empty then the row and column are needed.
                if (!context.isFormatEmpty(child)) {
                    requiredRows[r] = true;
                    requiredColumns[c] = true;
                }
            }
        }

        calculated = true;
    }

    // Javadoc inherited from super class.
    protected boolean isEmptyImpl() {

        if (!calculated) {
            calculateOutput();
        }

        for (int r = 0; r < rows; r++) {
            if (requiredRows[r]) {
                return false;
            }
        }

        for (int c = 0; c < columns; c++) {
            if (requiredColumns[c]) {
                return false;
            }
        }

        return true;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 ===========================================================================
*/
