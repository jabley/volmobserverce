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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/TemporalFormatIteratorInstanceTestCase.java,v 1.2 2002/12/10 18:14:28 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Dec-2002  Chris W         VBM:2002111103 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;

/**
 * This class tests the TemporalFormatIteratorInstance class
 *
 * TODO: this now mostly tests Format.getDimensions ... refactor when we
 * decide if this is where getDimensions should be permanently.
 */
public class TemporalFormatIteratorInstanceTestCase
        extends FormatInstanceTestAbstract {
    private SpatialFormatIteratorInstance sfiInstance;
    private SpatialFormatIterator sfi;
    private TemporalFormatIteratorInstance tfiInstance;
    private TemporalFormatIterator tfi;
    private TemporalFormatIteratorInstance tfi2Instance;
    private TemporalFormatIterator tfi2;
    private TestDeviceLayoutContext deviceLayoutContext;
    private Pane pane;
    private PaneInstance paneContext;


    public void setUp() {
        pane = new Pane(null);
        paneContext = new TestPaneInstance();

        deviceLayoutContext = new TestDeviceLayoutContext();

        tfi2 = new TemporalFormatIterator(null);
        tfi2.setInstance(0);

        tfi2Instance = new TemporalFormatIteratorInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        tfi2Instance.setDeviceLayoutContext(deviceLayoutContext);
        tfi2Instance.setFormat(tfi2);

        tfi = new TemporalFormatIterator(null);
        tfi.setInstance(1);

        tfiInstance = new TemporalFormatIteratorInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        tfiInstance.setDeviceLayoutContext(deviceLayoutContext);
        tfiInstance.setFormat(tfi);

        sfi = new SpatialFormatIterator(null);
        sfi.setInstance(3);

        sfiInstance = new SpatialFormatIteratorInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        sfiInstance.setDeviceLayoutContext(deviceLayoutContext);
        sfiInstance.setFormat(sfi);

        deviceLayoutContext.setFormatInstance(tfi,
                                              NDimensionalIndex.ZERO_DIMENSIONS,
                                              tfiInstance);
        deviceLayoutContext.setFormatInstance(sfi,
                                              NDimensionalIndex.ZERO_DIMENSIONS,
                                              sfiInstance);
    }

    public void tearDown() {
        pane = null;
        paneContext = null;
        tfiInstance = null;
        tfi = null;
        deviceLayoutContext = null;
    }

    private void create1d() {
        tearDown();
        setUp();

        // Create a 1d temporal format iterator containing a pane
        pane = new Pane(null);
        pane.setName("a");
        pane.setInstance(2);
        pane.setParent(tfi);
        paneContext.setDeviceLayoutContext(deviceLayoutContext);
        paneContext.setFormat(pane);
        deviceLayoutContext.setFormatInstance(pane,
                                              NDimensionalIndex.ZERO_DIMENSIONS,
                                              paneContext);
        try {
            tfi.setChildAt(pane, 0);
        } catch (LayoutException e) {
            fail("should not throw LayoutException when setting child");
        }

        // Must call initialise after setting up the tfi and before running
        // any tests.
        tfiInstance.setFormat(tfi);
        tfiInstance.initialise();
    }

    private void create2d() {
        tearDown();
        setUp();

        // Create a 2d temporal format iterator containing a temporal format
        // iterator which contains a pane.
        pane = new Pane(null);
        pane.setName("a");
        pane.setInstance(2);
        pane.setParent(tfi);
        paneContext.setDeviceLayoutContext(deviceLayoutContext);
        paneContext.setFormat(pane);
        deviceLayoutContext.setFormatInstance(pane,
                                              NDimensionalIndex.ZERO_DIMENSIONS,
                                              paneContext);
        try {
            tfi.setChildAt(pane, 0);
        } catch (LayoutException e) {
            fail("should not throw LayoutException when setting child");
        }

        tfi.setParent(tfi2);
        deviceLayoutContext.setFormatInstance(tfi,
                                              NDimensionalIndex.ZERO_DIMENSIONS,
                                              tfiInstance);
        try {
            tfi2.setChildAt(tfi, 0);
        } catch (LayoutException e) {
            fail("should not throw LayoutException when setting child");
        }

        // Must call initialise after setting up the tfi and before running
        // any tests.
        tfiInstance.setFormat(tfi);
        tfiInstance.initialise();
        tfi2Instance.setFormat(tfi2);
        tfi2Instance.initialise();
    }

    private void createTemporalInSpatial() {
        tearDown();
        setUp();

        // Create a spatial format iterator containing a temporal format
        // iterator which contains a pane.
        pane = new Pane(null);
        pane.setName("a");
        pane.setInstance(2);
        pane.setParent(tfi);

        paneContext = new EmptyPaneInstance(null);
        paneContext.setDeviceLayoutContext(deviceLayoutContext);
        paneContext.setFormat(pane);
        deviceLayoutContext.setFormatInstance(pane,
                                              NDimensionalIndex.ZERO_DIMENSIONS,
                                              paneContext);

        try {
            tfi.setChildAt(pane, 0);
        } catch (LayoutException e) {
            fail("should not throw LayoutException when setting child");
        }

        tfi.setParent(sfi);
        deviceLayoutContext.setFormatInstance(tfi,
                                              NDimensionalIndex.ZERO_DIMENSIONS,
                                              tfiInstance);
        try {
            sfi.setChildAt(tfi, 0);
        } catch (LayoutException e) {
            fail("should not throw LayoutException when setting child");
        }

        // Must call initialise after setting up the tfi and before running
        // any tests.
        tfiInstance.setFormat(tfi);
        tfiInstance.initialise();
        sfiInstance.setFormat(sfi);
        sfiInstance.initialise();
    }


    private void createEmpty() {
        tearDown();
        setUp();

        tfiInstance.initialise();
    }

    public void testIsEmptyWithPopulatedTemporalFormatIterator() {
        // Test a fully populated 1d spatial format iterator
        create1d();
        assertTrue("tfi should be full - enclosed pane not empty",
                   !tfiInstance.isEmpty());
    }

    public void testIsEmptyWithEmptyTemporalFormatIterator() {
        // test an empty spatial format iterator
        createEmpty();
        assertTrue("tfi should be empty", tfiInstance.isEmpty());
    }

    public void testIsEmptyWithPopulated2DTemporalFormatIterator() {
        // Test a fully populated 2d spatial format iterator
        create2d();
        assertTrue("tfi should be full- enclosed pane not empty",
                   !tfiInstance.isEmpty());
        assertTrue("tfi2 should be full - enclosed pane not empty",
                   !tfi2Instance.isEmpty());
    }

    public void testIsEmptyWithTemporalInSpatial() {
        // Test a spatial format iterator containing an empty temporal
        createTemporalInSpatial();
        // The definition of emptyness with respect to spatial format iterators
        // is that child formats must have actual content targeted at them.
        // In this test the Pane p does not have any content written to it,
        // so the spatial iterator is considered to be empty.
        assertTrue("sfi should be empty - enclosed pane is empty",
                   sfiInstance.isEmpty());
        assertTrue("tfi should be empty - enclosed pane is empty",
                   tfiInstance.isEmpty());
    }

    public void testGetDimensions() {
        // Test a fully populated 1d spatial format iterator
        create1d();
        assertEquals("tfi should have 0 dimensions", 0, tfi.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        tfiInstance.setFormat(tfi);
        assertEquals("tfi should have 0 dimensions", 0, tfi.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 1 dimension", 1, pane.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 1 dimension", 1, pane.getDimensions());

        // Test a fully populated 2d spatial format iterator
        create2d();
        assertEquals("tfi2 should have 0 dimensions", 0, tfi2.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        tfi2Instance.setFormat(tfi2);
        assertEquals("tfi2 should have 0 dimensions", 0, tfi2.getDimensions());
        assertEquals("tfi should have 1 dimension", 1, tfi.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        tfiInstance.setFormat(tfi);
        assertEquals("tfi should have 1 dimension", 1, tfi.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 2 dimension", 2, pane.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 2 dimension", 2, pane.getDimensions());

        createEmpty();
        assertEquals("tfi should have 0 dimensions", 0, tfi.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        tfiInstance.setFormat(tfi);
        assertEquals("tfi should have 0 dimensions", 0, tfi.getDimensions());

        // Test a fully populated spatial format iterator containing a temporal
        createTemporalInSpatial();
        assertEquals("sfi should have 0 dimensions", 0, sfi.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        sfiInstance.setFormat(sfi);
        assertEquals("sfi should have 0 dimensions", 0, sfi.getDimensions());
        assertEquals("tfi should have 1 dimension", 1, tfi.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        tfiInstance.setFormat(tfi);
        assertEquals("tfi should have 1 dimension", 1, tfi.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 2 dimension", 2, pane.getDimensions());
        paneContext.setFormat(pane);
        assertEquals("pane should have 2 dimension", 2, pane.getDimensions());
    }

    /**
     * Mock PaneInstance representing an empty pane.
     */
    private class EmptyPaneInstance extends PaneInstance {
        public EmptyPaneInstance(NDimensionalIndex index) {
            super(index);
        }

        // Javadoc inherited from super class.
        protected boolean isEmptyImpl() {
            return true;
        }
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Jul-05	8927/1	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 30-Jun-05	8734/5	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/3	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/1	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

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
