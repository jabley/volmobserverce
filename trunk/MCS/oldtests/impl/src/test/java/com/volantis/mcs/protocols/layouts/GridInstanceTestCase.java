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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/GridInstanceTestCase.java,v 1.2 2002/12/10 18:14:28 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Dec-02    Chris W         VBM:2002111103 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;

/**
 * This class unit tests the GridInstance class.
 */
public class GridInstanceTestCase
        extends AbstractGridInstanceTestCase {

    private TestDeviceLayoutContext deviceLayoutContext;
    private Grid grid;
    private GridInstance gridInstance;
    private Pane pane;
    private TestPaneInstance paneContext;

    public void setUp() {
        pane = new Pane(null);
        paneContext = new TestPaneInstance();

        deviceLayoutContext = new TestDeviceLayoutContext();

        grid = new Grid(null);
        grid.setInstance(0);

        gridInstance = new GridInstance(NDimensionalIndex.ZERO_DIMENSIONS);
        gridInstance.setDeviceLayoutContext(deviceLayoutContext);
        gridInstance.setFormat(grid);

        deviceLayoutContext.setFormatInstance(grid, NDimensionalIndex.ZERO_DIMENSIONS, gridInstance);
    }

    public void tearDown() {
        pane = null;
        paneContext = null;
        gridInstance = null;
        grid = null;
        deviceLayoutContext = null;
    }

    private void create2x2() {
        tearDown();
        setUp();

        // Create a 2 by 2 grid where each cell contains a pane.
        grid.setRows(2);
        grid.setColumns(2);

        pane = new Pane(null);
        pane.setName("a");
        pane.setInstance(1);
        pane.setParent(grid);
        deviceLayoutContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneContext);
        grid.insertChildAt(pane, 0);

        pane = new Pane(null);
        pane.setName("b");
        pane.setInstance(2);
        pane.setParent(grid);
        deviceLayoutContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneContext);
        grid.insertChildAt(pane, 1);

        pane = new Pane(null);
        pane.setName("c");
        pane.setInstance(3);
        pane.setParent(grid);
        deviceLayoutContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneContext);
        grid.insertChildAt(pane, 2);

        pane = new Pane(null);
        pane.setName("d");
        pane.setInstance(4);
        pane.setParent(grid);
        deviceLayoutContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneContext);
        grid.insertChildAt(pane, 3);

        // Must call initialise after setting up the grid and before running
        // any tests.
        gridInstance.initialise();
    }

    private void create3x3Sparse() {
        tearDown();
        setUp();

        // Create a sparsely populated 3 by 3 grid but with no empty rows or
        // columns
        grid.setRows(3);
        grid.setColumns(3);
        grid.insertChildAt(null, 0);

        pane = new Pane(null);
        pane.setName("a");
        pane.setInstance(1);
        pane.setParent(grid);
        deviceLayoutContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneContext);
        grid.insertChildAt(pane, 1);

        grid.insertChildAt(null, 2);

        pane = new Pane(null);
        pane.setName("b");
        pane.setInstance(2);
        pane.setParent(grid);
        deviceLayoutContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneContext);
        grid.insertChildAt(pane, 3);

        for (int i = 4; i < 8; i++) {
            grid.insertChildAt(null, i);
        }

        pane = new Pane(null);
        pane.setName("c");
        pane.setInstance(3);
        pane.setParent(grid);
        deviceLayoutContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneContext);
        grid.insertChildAt(pane, 8);

        // Must call initialise after setting up the grid and before running
        // any tests.
        gridInstance.initialise();
    }

    private void create3x3AlmostEmpty() {
        tearDown();
        setUp();

        // Create an almost empty populated 3 by 3 grid but with 1 empty row
        // and 2 empty columns
        grid.setRows(3);
        grid.setColumns(3);
        grid.insertChildAt(null, 0);

        pane = new Pane(null);
        pane.setName("a");
        pane.setInstance(1);
        pane.setParent(grid);
        deviceLayoutContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneContext);
        grid.insertChildAt(pane, 1);

        for (int i = 2; i < 7; i++) {
            grid.insertChildAt(null, i);
        }

        pane = new Pane(null);
        pane.setName("b");
        pane.setInstance(2);
        pane.setParent(grid);
        deviceLayoutContext.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneContext);
        grid.insertChildAt(pane, 7);

        grid.insertChildAt(null, 8);
        // Must call initialise after setting up the grid and before running
        // any tests.
        gridInstance.initialise();
    }

    private void create3x3Empty() {
        tearDown();
        setUp();

        grid.setRows(3);
        grid.setColumns(3);

        for (int i = 0; i < 9; i++) {
            grid.insertChildAt(null, i);
        }
        gridInstance.initialise();
    }

    private void createEmptyGrid() {
        tearDown();
        setUp();

        grid.setRows(0);
        grid.setColumns(0);
        gridInstance.initialise();
    }

    public void testGetRequiredRows() {
        // Check what appens when the grid is empty
        createEmptyGrid();
        boolean[] rows = gridInstance.getRequiredRows();
        assertEquals("should only be 0 rows", 0, rows.length);

        // Check a simple 2x2 grid where each cell contains a pane
        create2x2();
        rows = gridInstance.getRequiredRows();
        assertEquals("should only be 2 rows", 2, rows.length);
        for (int r = 0; r < rows.length; r++) {
            assertTrue("row is needed", rows[r]);
        }

        // Create a sparsely populated 3 by 3 grid but with no empty rows or
        // columns
        create3x3Sparse();
        rows = gridInstance.getRequiredRows();
        assertEquals("should only be 3 rows", 3, rows.length);
        for (int r = 0; r < rows.length; r++) {
            assertTrue("row is needed", rows[r]);
        }

        // Create a sparsely populated 3 by 3 grid but with an empty row and
        // two empty columns
        create3x3AlmostEmpty();
        rows = gridInstance.getRequiredRows();
        assertEquals("should only be 3 rows", 3, rows.length);
        assertTrue("row is needed", rows[0]);
        assertTrue("row is not needed", !rows[1]);
        assertTrue("row is needed", rows[2]);

        // Create an empty 3 by 3 grid
        create3x3Empty();
        rows = gridInstance.getRequiredRows();
        assertEquals("should only be 3 rows", 3, rows.length);
        assertTrue("row is not needed", !rows[0]);
        assertTrue("row is not needed", !rows[1]);
        assertTrue("row is not needed", !rows[2]);
    }

    public void testGetRequiredColumns() {
        // Check a simple 2x2 grid where each cell contains a pane
        createEmptyGrid();
        boolean[] columns = gridInstance.getRequiredColumns();
        assertEquals("should only be 0 columns", 0, columns.length);

        // Check a simple 2x2 grid where each cell contains a pane
        create2x2();
        columns = gridInstance.getRequiredColumns();
        assertEquals("should only be 2 columns", 2, columns.length);
        for (int c = 0; c < columns.length; c++) {
            assertTrue("column is needed", columns[c]);
        }

        // Create a sparsely populated 3 by 3 grid but with no empty rows or
        // columns
        create3x3Sparse();
        columns = gridInstance.getRequiredColumns();
        assertEquals("should only be 3 columns", 3, columns.length);
        for (int c = 0; c < columns.length; c++) {
            assertTrue("column is needed", columns[c]);
        }

        // Create a sparsely populated 3 by 3 grid but with an empty row and
        // two empty columns
        create3x3AlmostEmpty();
        columns = gridInstance.getRequiredColumns();
        assertEquals("should only be 3 columns", 3, columns.length);
        assertTrue("column is not needed", !columns[0]);
        assertTrue("column is needed", columns[1]);
        assertTrue("column is not needed", !columns[2]);

        // Create an empty 3 by 3 grid
        create3x3Empty();
        columns = gridInstance.getRequiredColumns();
        assertEquals("should only be 3 columns", 3, columns.length);
        assertTrue("column is not needed", !columns[0]);
        assertTrue("column is not needed", !columns[1]);
        assertTrue("column is not needed", !columns[2]);
    }

    public void testIsEmptyImpl() {
        // Test an empty grid
        createEmptyGrid();
        assertTrue("grid should be empty", gridInstance.isEmptyImpl());

        // Test a fully populated 2x2 grid
        create2x2();
        assertTrue("grid should be full", !gridInstance.isEmptyImpl());

        // Test a sparsely populated 3x3 grid
        create3x3Sparse();
        assertTrue("grid should be full", !gridInstance.isEmptyImpl());

        // Test a more sparsely populated 3x3 grid
        create3x3AlmostEmpty();
        assertTrue("grid should be full", !gridInstance.isEmptyImpl());

        // Test an empty 3x3 grid
        create3x3Empty();
        assertTrue("grid should be empty", gridInstance.isEmptyImpl());
    }

    public void testIsEmpty() {
        // Test an empty grid
        createEmptyGrid();
        assertTrue("grid should be empty", gridInstance.isEmpty());

        // Test a fully populated 2x2 grid
        create2x2();
        assertTrue("grid should be full", !gridInstance.isEmpty());

        // Test a sparsely populated 3x3 grid
        create3x3Sparse();
        assertTrue("grid should be full", !gridInstance.isEmpty());

        // Test a more sparsely populated 3x3 grid
        create3x3AlmostEmpty();
        assertTrue("grid should be full", !gridInstance.isEmpty());

        // Test an empty 3x3 grid
        create3x3Empty();
        assertTrue("grid should be empty", gridInstance.isEmpty());
    }

    /**
     * TODO: this now mostly tests Format.getDimensions ... refactor when we
     * decide if this is where getDimensions should be permanently.
     */
    public void testGetDimensions() {
        // Test an empty grid
        createEmptyGrid();
        assertEquals("grid should have 0 dimensions", 0, grid.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        gridInstance.setFormat(grid);
        assertEquals("grid should have 0 dimensions", 0, grid.getDimensions());

        // Test a fully populated 2x2 grid
        create2x2();
        assertEquals("grid should have 0 dimensions", 0, grid.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        gridInstance.setFormat(grid);
        assertEquals("grid should have 0 dimensions", 0, grid.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 0 dimensions", 0, pane.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 0 dimensions", 0, pane.getDimensions());

        // Test a sparsely populated 3x3 grid
        create3x3Sparse();
        assertEquals("grid should have 0 dimensions", 0, grid.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        gridInstance.setFormat(grid);
        assertEquals("should have 0 dimensions", 0, grid.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 0 dimensions", 0, pane.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 0 dimensions", 0, pane.getDimensions());

        // Test a more sparsely populated 3x3 grid
        create3x3AlmostEmpty();
        assertEquals("should have 0 dimensions", 0, grid.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        gridInstance.setFormat(grid);
        assertEquals("should have 0 dimensions", 0, grid.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 0 dimensions", 0, pane.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 0 dimensions", 0, pane.getDimensions());

        // Test an empty 3x3 grid
        create3x3Empty();
        assertEquals("should have 0 dimensions", 0, grid.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        gridInstance.setFormat(grid);
        assertEquals("should have 0 dimensions", 0, grid.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 0 dimensions", 0, pane.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 0 dimensions", 0, pane.getDimensions());

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/6	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 16-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
