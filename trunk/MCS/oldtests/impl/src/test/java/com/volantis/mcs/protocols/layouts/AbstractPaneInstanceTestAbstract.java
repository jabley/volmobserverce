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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;

/**
 * This is the base class for all class that test AbstractPaneInstance and its
 * subclasses.
 * 
 * TODO: this now mostly tests Format.isSkippable ... refactor when we decide
 * if this is where isSkippable should be permanently.
 */
public class AbstractPaneInstanceTestAbstract
    extends FormatInstanceTestAbstract {

    static private CanvasLayout canvasLayout;

    private Format format;
    private Grid grid;
    private TestMarinerPageContext pageContext;
    private NDimensionalIndex index;

    /**
     * Set up the tests in this class
     */
    private void privateSetUp() {
        canvasLayout = new CanvasLayout();

        pageContext = new TestMarinerPageContext();
        format = new Pane(canvasLayout);
        format.setName("pane");
        pageContext.addPaneMapping((Pane)format);
        grid = new Grid(canvasLayout);
        grid.setName("grid");
        //logger.debug("setup");

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        pageContext.setDeviceLayout(runtimeDeviceLayout);
    }

    private AbstractPaneInstance createPaneContext(
            final NDimensionalIndex index) {

        AbstractPaneInstance paneInstance;
        paneInstance = createTestableAbstractPaneContext(
                index);
        paneInstance.setFormat(this.format);
        paneInstance.setDeviceLayoutContext(new TestDeviceLayoutContext());
        paneInstance.initialise();
        return paneInstance;
    }

    private PaneInstance createPaneContext(NDimensionalIndex index,
            Format format) {
        
        PaneInstance paneInstance=new PaneInstance(index);
        paneInstance.setFormat(format);
        paneInstance.setDeviceLayoutContext(new TestDeviceLayoutContext());
        paneInstance.initialise();
        return paneInstance;
    }

    
    /**
     * Factory method to create a testable AbstractPaneInstance
     * @return AbstractPaneInstance
     */
    protected AbstractPaneInstance createTestableAbstractPaneContext(
            NDimensionalIndex index) {
        
        return new AbstractPaneInstance(index) {

            PaneAttributes attrs = new PaneAttributes();

            public void endCurrentBuffer() {
            }

            public PaneAttributes getAttributes() {
                return attrs;
            }
        };
    }
    
    /**
     * Factory method to create a Pane
     * @return Pane
     */
    public Pane createPane(){
        Pane pane = new Pane(canvasLayout);
        pane.setName("pane");
        return pane; 
    }
    
    /**
     * Test FormatInstance.isSkippable with a single format that is NOT
     * contained within a spatial or tmeporal format iterator
     */    
    public void testIsSkippableSinglePane() throws Exception
    {
        privateSetUp();
        
        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("shouldn't skip format", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {0});
        assertTrue("should treat format.0 as format", 
                !createPaneContext(index).ignore());
    }
    
    /**
     * Test release method resets the array we use to store the max. no. cells
     * in each dimension.
     */
    
    /**
     * Test FormatInstance.isSkippable with a 1D spatial format iterator
     * with fixed rows and columns.     
     */
    public void testIsSkippableSpatial1D1() throws Exception
    {        
        privateSetUp();
        
        SpatialFormatIterator sfi = new SpatialFormatIterator(canvasLayout);
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "2");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "2");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "fixed");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "fixed");
        format.setParent(sfi);
        
        index = new NDimensionalIndex(new int[] {0});
        assertTrue("shouldn't skip format.0", 
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {1});
        assertTrue("shouldn't skip format.1", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {2});
        assertTrue("shouldn't skip format.2", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {3});
        assertTrue("shouldn't skip format.3", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {4});
        assertTrue("should skip format.4", 
                createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format as format.0", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1, 2});
        assertTrue("should treat format1.2 as format.1", 
                !createPaneContext(index).ignore());
    }

    /**
     * Test FormatInstance.isSkippable with a 1D spatial format iterator
     * with variable rows and fixed columns.     
     */
    public void testIsSkippableSpatial1D2() throws Exception
    {
        privateSetUp();
                
        SpatialFormatIterator sfi = new SpatialFormatIterator(canvasLayout);
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "2");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "3");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "variable");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "fixed");
        format.setParent(sfi);
        
        index = new NDimensionalIndex(new int[] {0});
        assertTrue("shouldn't skip format.0", 
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {2});
        assertTrue("shouldn't skip format.2", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {5});
        assertTrue("shouldn't skip format.5", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {6});
        assertTrue("should skip format.6", 
                createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {7});
        assertTrue("should skip format.7", 
                createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format as format.0", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1, 2});
        assertTrue("should treat format1.2 as format.1", 
                !createPaneContext(index).ignore());
    }

    /**
     * Test FormatInstance.isSkippable with a 1D spatial format iterator
     * with variable rows and variable columns.     
     */
    public void testIsSkippableSpatial1D3() throws Exception
    {
        privateSetUp();
                
        SpatialFormatIterator sfi = new SpatialFormatIterator(canvasLayout);
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "1");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "3");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "variable");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "variable");
        format.setParent(sfi);
        
        index = new NDimensionalIndex(new int[] {0});
        assertTrue("shouldn't skip format.0", 
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {1});
        assertTrue("shouldn't skip format.1", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {2});
        assertTrue("shouldn't skip format.2", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {3});
        assertTrue("should skip format.3", 
                createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {4});
        assertTrue("should skip format.4",
                createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format as format.0", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1, 2});
        assertTrue("should treat format1.2 as format.1", 
                !createPaneContext(index).ignore());
    }

    /**
     * Test FormatInstance.isSkippable with a 1D spatial format iterator
     * with fixed rows and variable columns.     
     */
    public void testIsSkippableSpatial1D4() throws Exception
    {
        privateSetUp();
                
        SpatialFormatIterator sfi = new SpatialFormatIterator(canvasLayout);
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "5");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "0");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "fixed");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "variable");
        format.setParent(sfi);
        
        index = new NDimensionalIndex(new int[] {0});
        assertTrue("shouldn't skip format.0", 
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {3});
        assertTrue("shouldn't skip format.3", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {4});
        assertTrue("shouldn't skip format.4", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {5});
        assertTrue("shouldn't skip format.5", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1000});
        assertTrue("shouldn't skip format.1000",
                !createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format as format.0", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1, 2});
        assertTrue("should treat format1.2 as format.1",
                !createPaneContext(index).ignore());
    }

    /**
     * Test FormatInstance.isSkippable with a 1D spatial format iterator
     * with fixed rows and columns, both of which are set to zero - so nothing is rendered.    
     */
    public void testIsSkippableSpatial1D5() throws Exception
    {
        privateSetUp();
                
        SpatialFormatIterator sfi = new SpatialFormatIterator(canvasLayout);
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "0");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "0");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "fixed");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "variable");
        format.setParent(sfi);
        
        index = new NDimensionalIndex(new int[] {0});
        assertTrue("should skip format.0", 
                createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {3});
        assertTrue("should skip format.3", 
                createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {4});
        assertTrue("should skip format.4", 
                createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {5});
        assertTrue("should skip format.5",
                createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1000});
        assertTrue("should skip format.1000",
                createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format as format.0",
                createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1, 2});
        assertTrue("should treat format1.2 as format.1",
                createPaneContext(index).ignore());
    }

    /**
     * Test FormatInstance.isSkippable with a temporal format iterator
     * containing a grid of 2 formats with a variable number of cells set to 0
     */
    public void testIsSkippableTemporal1() throws Exception
    {
        privateSetUp();
        
        TemporalFormatIterator tfi = new TemporalFormatIterator(canvasLayout);
        tfi.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT, "0");
        tfi.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS, "variable");

        Format format2 = new Pane(canvasLayout);
        format2.setName("pane2");
        pageContext.addPaneMapping((Pane)format2);

        grid.setRows(1);
        grid.setColumns(2);
        grid.setParent(tfi);
        grid.insertChildAt(format, 0);
        format.setParent(grid);
        
        grid.insertChildAt(format2, 1);
        format2.setParent(grid);

        index = new NDimensionalIndex(new int[] {0});
        assertTrue("shouldn't skip format.0", 
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {3});
        assertTrue("shouldn't skip format2.3", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {4});
        assertTrue("shouldn't skip format.4", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {5});
        assertTrue("shouldn't skip format2.5", 
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {1000});
        assertTrue("shouldn't skip format.1000", 
                !createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format2 as format.0", 
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {1, 2});
        assertTrue("should treat format.1.2 as format.1", 
                !createPaneContext(index).ignore());
    }

    /**
     * Test FormatInstance.isSkippable with a temporal format iterator
     * containing a grid of 2 formats with a fixed number of cells
     */
    public void testIsSkippableTemporal2() throws Exception
    {
        privateSetUp();
        
        TemporalFormatIterator tfi = new TemporalFormatIterator(canvasLayout);
        tfi.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT, "4");
        tfi.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS, "fixed");

        Format format2 = new Pane(canvasLayout);
        format2.setName("pane2");
        pageContext.addPaneMapping((Pane)format2);

        grid.setRows(1);
        grid.setColumns(2);
        grid.setParent(tfi);
        grid.insertChildAt(format, 0);
        format.setParent(grid);
        
        grid.insertChildAt(format2, 1);
        format2.setParent(grid);

        index = new NDimensionalIndex(new int[] {0});
        assertTrue("shouldn't skip format.0",
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {1});
        assertTrue("shouldn't skip format2.1",
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {3});
        assertTrue("shouldn't skip format.3",
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {4});
        assertTrue("should skip format2.4", 
                createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {5});
        assertTrue("should skip format.5",
                createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format2 as format.0",
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {1, 2});
        assertTrue("should treat format.1.2 as format.1",
                !createPaneContext(index).ignore());
    }

    /**
     * Test FormatInstance.isSkippable with a temporal format iterator
     * containing a grid of 2 formats with a variable number of cells
     */
    public void testIsSkippableTemporal3() throws Exception
    {
        privateSetUp();
        
        TemporalFormatIterator tfi = new TemporalFormatIterator(canvasLayout);
        tfi.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT, "4");
        tfi.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS, "variable");

        Format format2 = new Pane(canvasLayout);
        format2.setName("pane2");
        pageContext.addPaneMapping((Pane)format2);

        grid.setRows(1);
        grid.setColumns(2);
        grid.setParent(tfi);
        grid.insertChildAt(format, 0);
        format.setParent(grid);
        
        grid.insertChildAt(format2, 1);
        format2.setParent(grid);

        index = new NDimensionalIndex(new int[] {0});
        assertTrue("shouldn't skip format.0",
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {1});
        assertTrue("shouldn't skip format2.1",
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {3});
        assertTrue("shouldn't skip format.3",
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {4});
        assertTrue("should skip format2.4",
                createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {5});
        assertTrue("should skip format.5",
                createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format2 as format.0",
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {1, 2});
        assertTrue("should treat format.1.2 as format.1", 
                !createPaneContext(index).ignore());
    }

    /**
     * Test FormatInstance.isSkippable with a temporal format iterator
     * containing a grid of 2 formats with a fixed number of cells. The number
     * of cells is set to 0 so nothing should be rendered. 
     */
    public void testIsSkippableTemporal4() throws Exception
    {
        privateSetUp();
        
        TemporalFormatIterator tfi = new TemporalFormatIterator(canvasLayout);
        tfi.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT, "0");
        tfi.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS, "fixed");

        Format format2 = new Pane(canvasLayout);
        format2.setName("pane2");
        pageContext.addPaneMapping((Pane)format2);

        grid.setRows(1);
        grid.setColumns(2);
        grid.setParent(tfi);
        grid.insertChildAt(format, 0);
        format.setParent(grid);
        
        grid.insertChildAt(format2, 1);
        format2.setParent(grid);

        index = new NDimensionalIndex(new int[] {0});
        assertTrue("should skip format.0", createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {1});
        assertTrue("should skip format2.1", createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {3});
        assertTrue("should skip format.3", createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {4});
        assertTrue("should skip format2.4", createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {5});
        assertTrue("should skip format.5", createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format2 as format.0", createPaneContext(index, format2).ignore());
    
        index = new NDimensionalIndex(new int[] {1, 2});
        assertTrue("should treat format.1.2 as format.1", createPaneContext(index).ignore());
    }

    /**
     * Test FormatInstance.isSkippable with a 2D spatial format iterator
     * containing a format and another spatial format iterator.    
     */
    public void testIsSkippableSpatial2D() throws Exception
    {   
        privateSetUp();
             
        SpatialFormatIterator sfi = new SpatialFormatIterator(canvasLayout);
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "2");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "0");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "fixed");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "variable");
        sfi.setName("spatial1");

        SpatialFormatIterator sfi2 = new SpatialFormatIterator(canvasLayout);
        sfi2.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "2");
        sfi2.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "3");
        sfi2.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "fixed");
        sfi2.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "fixed");
        sfi2.setName("spatial2");

        Format format2 = new Pane(canvasLayout);
        format2.setName("pane2");
        pageContext.addPaneMapping((Pane)format2);
        format2.setParent(sfi2);

        grid.setRows(1);
        grid.setColumns(2);
        grid.setParent(sfi);
        grid.insertChildAt(format, 0);
        format.setParent(grid);
       
        grid.insertChildAt(sfi2, 1);
        sfi2.setParent(grid);

        index = new NDimensionalIndex(new int[] {0});
        assertTrue("shouldn't skip format.0",
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {3});
        assertTrue("shouldn't skip format.3",
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {4});
        assertTrue("shouldn't skip format.4", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {5});
        assertTrue("shouldn't skip format.5", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1000});
        assertTrue("shouldn't skip format.1000", 
                !createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format as format.0", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1, 2, 3});
        assertTrue("should treat format.1.2.3 as format.1",
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {0, 0});
        assertTrue("shouldn't skip format2.0.0", 
                !createPaneContext(index, format2).ignore());
        
        index = new NDimensionalIndex(new int[] {3, 4});
        assertTrue("shouldn't skip format2.3.4",
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {4, 5});
        assertTrue("shouldn't skip format2.4.5", 
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {5, 6});
        assertTrue("should skip format2.5.6",
                createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {1000, 5});
        assertTrue("shouldn't skip format2.1000.5",
                !createPaneContext(index, format2).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format2 as format2.0.0",
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {1, 2, 3});
        assertTrue("should treat format2.1.2.3 as format2.1.2",
                !createPaneContext(index, format2).ignore());
    }

    /**
     * Test FormatInstance.isSkippable with a 2D spatial format iterator
     * containing a format and another spatial format iterator.    
     */
    public void testIsSkippableSpatial3D() throws Exception
    {        
        privateSetUp();
        
        SpatialFormatIterator sfi = new SpatialFormatIterator(canvasLayout);
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "2");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "2");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "fixed");
        sfi.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "variable");
        sfi.setName("spatial1");

        SpatialFormatIterator sfi2 = new SpatialFormatIterator(canvasLayout);
        sfi2.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT, "2");
        sfi2.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT, "3");
        sfi2.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS, "fixed");
        sfi2.setAttribute(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS, "fixed");
        sfi2.setName("spatial2");

        TemporalFormatIterator tfi = new TemporalFormatIterator(canvasLayout);
        tfi.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT, "4");
        tfi.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS, "variable");
        tfi.setName("temporal");

        Format format2 = new Pane(canvasLayout);
        format2.setName("pane2");
        pageContext.addPaneMapping((Pane)format2);

        Format format3 = new Pane(canvasLayout);
        format3.setName("pane3");
        pageContext.addPaneMapping((Pane)format3);
        format3.setParent(tfi);

        Grid grid2 = new Grid(canvasLayout);
        grid2.setName("grid2");       
        grid2.setRows(2);
        grid2.setColumns(1);
        grid2.setParent(sfi2);
        grid2.insertChildAt(format2, 0);
        format2.setParent(grid2);
        
        grid2.insertChildAt(tfi, 1);
        tfi.setParent(grid2);

        grid2.setParent(sfi2);

        grid.setRows(2);
        grid.setColumns(1);
        grid.setParent(sfi);
        grid.insertChildAt(format, 0);
        format.setParent(grid);
       
        grid.insertChildAt(sfi2, 1);
        sfi2.setParent(grid);
 
        index = new NDimensionalIndex(new int[] {0});
        assertTrue("shouldn't skip format.0",
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {3});
        assertTrue("shouldn't skip format.3",
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {4});
        assertTrue("should skip format.4", 
                createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {5});
        assertTrue("should skip format.5", 
                createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1000});
        assertTrue("should skip format.1000", 
                createPaneContext(index).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format as format.0", 
                !createPaneContext(index).ignore());

        index = new NDimensionalIndex(new int[] {1, 2, 3});
        assertTrue("should treat format.1.2.3 as format.1",
                !createPaneContext(index).ignore());
        
        index = new NDimensionalIndex(new int[] {0, 0});
        assertTrue("shouldn't skip format2.0.0", 
                !createPaneContext(index, format2).ignore());
        
        index = new NDimensionalIndex(new int[] {3, 4});
        assertTrue("shouldn't skip format2.3.4", 
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {4, 5});
        assertTrue("should skip format2.4.5", 
                createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {4, 6});
        assertTrue("should skip format2.4.6",
                createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {1000, 5});
        assertTrue("should skip format2.1000.5",
                createPaneContext(index, format2).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format2 as format2.0.0", 
                !createPaneContext(index, format2).ignore());

        index = new NDimensionalIndex(new int[] {1, 2, 3});
        assertTrue("should treat format2.1.2.3 as format2.1.2",
                !createPaneContext(index, format3).ignore());

        index = new NDimensionalIndex(new int[] {0, 0, 0});
        assertTrue("shouldn't skip format3.0.0",
                !createPaneContext(index, format3).ignore());
        
        index = new NDimensionalIndex(new int[] {3, 4, 3});
        assertTrue("shouldn't skip format3.3.4.3",
                !createPaneContext(index, format3).ignore());

        index = new NDimensionalIndex(new int[] {3, 5, 4});
        assertTrue("should skip format3.3.5.4", 
                createPaneContext(index, format3).ignore());

        index = new NDimensionalIndex(new int[] {4, 6, 1});
        assertTrue("should skip format3.4.6.1", 
                createPaneContext(index, format3).ignore());

        index = new NDimensionalIndex(new int[] {1000, 5, 2});
        assertTrue("should skip format3.1000.5.2",
                createPaneContext(index, format3).ignore());

        index = NDimensionalIndex.ZERO_DIMENSIONS;
        assertTrue("should treat format3 as format3.0.0.0", 
                !createPaneContext(index, format3).ignore());

        index = new NDimensionalIndex(new int[] {4, 6, 3});
        assertTrue("should treat format3.4.6.3.4 as format3.4.6.3", 
                createPaneContext(index, format3).ignore());
    }    
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 23-Feb-05	7114/1	geoff	VBM:2005021402 Exception when addressing row/column iterator panes using pane element

 23-Feb-05	7079/1	geoff	VBM:2005021402 Exception when addressing row/column iterator panes using pane element

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/6	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/9	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 16-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 04-Jul-03	337/1	chrisw	VBM:2003020609 implemented rework, added testcases

 ===========================================================================
*/
