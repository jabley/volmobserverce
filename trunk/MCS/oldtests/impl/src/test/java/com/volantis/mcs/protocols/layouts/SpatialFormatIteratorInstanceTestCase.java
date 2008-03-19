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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/SpatialFormatIteratorInstanceTestCase.java,v 1.2 2002/12/10 18:14:28 sumit Exp $
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
import com.volantis.mcs.protocols.layouts.SpatialFormatIteratorInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.AbstractOutputBuffer;
import junit.framework.TestCase;

import java.io.Writer;

/**
 * This class tests the SpatialFormatIteratorInstance class
 *
 * TODO: this now mostly tests Format.getDimensions ... refactor when we
 * decide if this is where getDimensions should be permanently.
 */
public class SpatialFormatIteratorInstanceTestCase
        extends FormatInstanceTestAbstract {

    /**
     * One dimensional index used throughout the test cases.
     */
    protected final static NDimensionalIndex ONE_DIMENSIONAL_INDEX =
            new NDimensionalIndex(new int[] {1});

    /**
     * Two dimensinal index used throughout the test cases.
     */
    protected final static NDimensionalIndex TWO_DIMENSIONAL_INDEX =
            new NDimensionalIndex(new int[] {1, 1});

    private SpatialFormatIteratorInstance sfiInstance;
    private SpatialFormatIterator sfi;
    private SpatialFormatIteratorInstance sfi2Instance;
    private SpatialFormatIterator sfi2;
    private MyDeviceLayoutContext deviceLayoutContext;
    private Pane pane;
    private PaneInstance paneInstance;

    public void setUp() {
        pane = new Pane(null);
        paneInstance = new PaneInstance(ONE_DIMENSIONAL_INDEX);

        deviceLayoutContext = new MyDeviceLayoutContext();

        sfi2 = new SpatialFormatIterator(null);
        sfi2.setInstance(0);

        sfi2Instance = new SpatialFormatIteratorInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        sfi2Instance.setDeviceLayoutContext(deviceLayoutContext);
        sfi2Instance.setFormat(sfi2);

        sfi = new SpatialFormatIterator(null);
        sfi.setInstance(1);

        sfiInstance = new SpatialFormatIteratorInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        sfiInstance.setDeviceLayoutContext(deviceLayoutContext);
        sfiInstance.setFormat(sfi);

        deviceLayoutContext.setFormatInstance(sfi,
                                              NDimensionalIndex.ZERO_DIMENSIONS,
                                              sfiInstance);
    }

    public void tearDown() {
        pane = null;
        paneInstance = null;
        sfiInstance = null;
        sfi = null;
        deviceLayoutContext = null;
    }

    /**
     * Creates a 1 dimensional spatial format iterator containing a pane.
     *
     * @param currentIndex the current index to be set on
     * {@link #deviceLayoutContext}
     */
    private void create1d(NDimensionalIndex currentIndex) {
        tearDown();
        setUp();

        // set the current index
        deviceLayoutContext.setCurrentFormatIndex(currentIndex);

        // Create a 1d spatial format iterator containing a pane
        pane = new Pane(null);
        pane.setName("a");
        pane.setInstance(2);
        pane.setParent(sfi);
        paneInstance.setDeviceLayoutContext(deviceLayoutContext);
        paneInstance.setFormat(pane);


        deviceLayoutContext.setFormatInstance(pane, ONE_DIMENSIONAL_INDEX,
                                              paneInstance);
        try {
            sfi.setChildAt(pane, 0);
        } catch (LayoutException e) {
            fail("should not throw LayoutException when setting child");
        }

        // Must call initialise after setting up the sfi and before running
        // any tests.
        sfiInstance.setFormat(sfi);
        sfiInstance.initialise();
    }

    /**
     * Creates a 2d spatial format iterator containing a spatial format
     * iterator which contains a pane.
     *
     * @param currentIndex the current index to be set on
     * {@link #deviceLayoutContext}
     */
    private void create2d(NDimensionalIndex currentIndex) {
        tearDown();
        setUp();

        // set the current index
        deviceLayoutContext.setCurrentFormatIndex(currentIndex);

        // Create a 2d spatial format iterator containing a spatial format
        // iterator which contains a pane.
        pane = new Pane(null);
        pane.setName("a");
        pane.setInstance(2);
        pane.setParent(sfi);
        paneInstance.setDeviceLayoutContext(deviceLayoutContext);
        paneInstance.setFormat(pane);

        deviceLayoutContext.setFormatInstance(pane, TWO_DIMENSIONAL_INDEX,
                                              paneInstance);
        try {
            sfi.setChildAt(pane, 0);
        } catch (LayoutException e) {
            fail("should not throw LayoutException when setting child");
        }

        sfi.setParent(sfi2);
        deviceLayoutContext.setFormatInstance(sfi,
                                              NDimensionalIndex.ZERO_DIMENSIONS,
                                              sfiInstance);
        try {
            sfi2.setChildAt(sfi, 0);
        } catch (LayoutException e) {
            fail("should not throw LayoutException when setting child");
        }

        // Must call initialise after setting up the sfi and before running
        // any tests.
        sfiInstance.setFormat(sfi);
        sfiInstance.initialise();
        sfi2Instance.setFormat(sfi2);
        sfi2Instance.initialise();
    }

    private void createEmpty() {
        tearDown();
        setUp();

        sfiInstance.initialise();
    }

    public void testIsEmptyWith1DSpatialIteratorThatIsEmpty() {

        // Test a fully populated 1d spatial format iterator
        NDimensionalIndex currentIndex = ONE_DIMENSIONAL_INDEX;
        create1d(currentIndex);
        assertTrue("Should be empty - format has no content",
                   sfiInstance.isEmpty());
    }

    public void testIsEmptyWith1DSpatialIteratorWithActualContent() {
        NDimensionalIndex currentIndex = ONE_DIMENSIONAL_INDEX;
        create1d(currentIndex);

        writeToPane(pane, "Some content.", ONE_DIMENSIONAL_INDEX);

        assertFalse("SFI should have content",
                    sfiInstance.isEmpty());
    }

    public void testIsEmptyWith1DSpiWhenContentNotRelativeToCurrentIndex() {
        NDimensionalIndex currentIndex = ONE_DIMENSIONAL_INDEX;
        create1d(currentIndex);

        NDimensionalIndex indexNotRelativeToCurrentIndex =
                new NDimensionalIndex(new int[] {3});
        writeToPane(pane, "Some content.", indexNotRelativeToCurrentIndex);

        assertTrue("SFI does not have content relative to current index.",
                    sfiInstance.isEmpty());
    }

    public void testIsEmptyWith2DSpatialIteratorThatIsEmpty() {
        create2d(ONE_DIMENSIONAL_INDEX);
        assertTrue("sfi2 should be empty - has no actual content",
                   sfi2Instance.isEmpty());
    }

    public void testIsEmptyWith2DSpatialIteratorWithActualContent() {
        create2d(ONE_DIMENSIONAL_INDEX);
        writeToPane(pane, "Some content.", TWO_DIMENSIONAL_INDEX);
        assertFalse("SFI should have content",
                    sfi2Instance.isEmpty());
    }

    public void testIsEmptyWith2DSpiWhenContentNotRelativeToCurrentIndex() {
        NDimensionalIndex currentIndex = new NDimensionalIndex(new int[] {3});
        create2d(currentIndex);
        writeToPane(pane, "Some content.", TWO_DIMENSIONAL_INDEX);
        assertTrue("SFI2 does not have content relative to current index.",
                    sfi2Instance.isEmpty());
    }

    public void testGetDimensions() {
        // Test a fully populated 1d spatial format iterator
        create1d(ONE_DIMENSIONAL_INDEX);
        assertEquals("sfi should have 0 dimensions", 0, sfi.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        sfiInstance.setFormat(sfi);
        assertEquals("sfi should have 0 dimensions", 0, sfi.getDimensions());
        paneInstance.setFormat(pane);
        assertEquals("pane should have 1 dimension", 1, pane.getDimensions());
        paneInstance.setFormat(pane);
        assertEquals("pane should have 1 dimension", 1, pane.getDimensions());

        // Test a fully populated 2d spatial format iterator
        create2d(ONE_DIMENSIONAL_INDEX);
        assertEquals("sfi2 should have 0 dimensions", 0, sfi2.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        sfi2Instance.setFormat(sfi2);
        assertEquals("sfi2 should have 0 dimensions", 0, sfi2.getDimensions());
        assertEquals("sfi should have 1 dimension", 1, sfi.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        sfiInstance.setFormat(sfi);
        assertEquals("sfi should have 1 dimension", 1, sfi.getDimensions());
        paneInstance.setFormat(pane);
        assertEquals("pane should have 2 dimension", 2, pane.getDimensions());
        paneInstance.setFormat(pane);
        assertEquals("pane should have 2 dimension", 2, pane.getDimensions());

        createEmpty();
        assertEquals("sfi should have 0 dimensions", 0, sfi.getDimensions());
        // Check number of dimensions doesn't change. This was a bug.
        sfiInstance.setFormat(sfi);
        assertEquals("sfi should have 0 dimensions", 0, sfi.getDimensions());
    }


    /**
     * Helper method to write content to a pane at the specified index.
     *
     * @param pane the pane to write to.
     * @param theContent the content to be written.
     * @param index the index at which the content is to be written to.
     */
    private void writeToPane(Pane pane,
                             String theContent,
                             NDimensionalIndex index) {

        PaneInstance paneInstance = (PaneInstance)
                deviceLayoutContext.getFormatInstance(pane, index);
        OutputBuffer buffer = paneInstance.getCurrentBuffer();
        buffer.writeText(theContent);
    }

    private class MyDeviceLayoutContext extends TestDeviceLayoutContext {

        // javadoc inherited.
        public OutputBuffer allocateOutputBuffer() {
            return new MockOutputBuffer();
        }
    }

    /**
     * Provides a mock ouput buffer to be used by MyDeviceLayout.
     */
    private class MockOutputBuffer extends AbstractOutputBuffer {

        /**
         * Holds all content written to this buffer.
         */
        private StringBuffer content = new StringBuffer();

        // Javadoc inherited.
        public Writer getWriter() {
            throw new UnsupportedOperationException();
        }

        // Javadoc inherited.
        public OutputBuffer getCurrentBuffer() {
            return this;
        }

        // Javadoc inherited.
        public boolean isEmpty() {
            return content.length() == 0;
        }

        // Javadoc inherited.
        public void writeText(char[] text, int off, int len) {
            throw new UnsupportedOperationException();
        }

        // Javadoc inherited.
        public void writeText(char[] text, int off, int len,
                              boolean preEncoded) {
            throw new UnsupportedOperationException();
        }

        // Javadoc inherited.
        public void writeText(String text, boolean preEncoded) {
            content.append(text);
        }

        // Javadoc inherited.
        public void writeText(String text) {
            this.writeText(text, false);
        }

        //javadoc inherited
        public void handleOpenElementWhitespace() {        
        }

        //javadoc inherited
        public void handleCloseElementWhitespace() {
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10562/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 01-Dec-05	10517/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 01-Jul-05	8927/1	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 30-Jun-05	8734/3	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

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
