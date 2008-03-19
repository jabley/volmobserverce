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
 * $Header:  $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Jun-03    Byron           VBM:2003042910 - Created in order to test the
 *                              calculateOutput method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.NDimensionalIndex;

/**
 * Provide a mechanism for testing the AbstractGridInstance class.
 */
public class AbstractGridInstanceTestCase
        extends FormatInstanceTestAbstract {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Test the calculation of row output. This is bit tricky to test since
     * the method alters the state of private variables which aren't accessible
     * for the test case.
     */
    public void testCalculateOutput() throws Exception {
        AbstractGridInstance instance = new AbstractGridInstance(
                NDimensionalIndex.ZERO_DIMENSIONS) { };
        TestableGrid format = new TestableGrid(new CanvasLayout());

        format.setRows(1);
        format.setColumns(1);
        format.setNumChildren(1);

        instance.setFormat(format);
        instance.initialise();

        instance.calculateOutput();


        boolean[] requiredColumns = instance.getRequiredColumns();
        assertEquals(requiredColumns.length, 1);
        for (int i = 0; i < requiredColumns.length; i++) {
            assertEquals(requiredColumns[i], false);
        }

        boolean[] requiredRows = instance.getRequiredRows();
        assertEquals(requiredRows.length, 1);
        for (int i = 0; i < requiredRows.length; i++) {
            assertEquals(requiredRows[i], false);
        }
    }

    /**
     * Provide a testable Grid (permits access to certain protected methods)
     */
    public class TestableGrid extends Grid {
        public TestableGrid(CanvasLayout canvasLayout) {
            super(canvasLayout);
        }

        // javadoc inherited
        protected void setNumChildren(int numChildren) {
            super.setNumChildren(numChildren);
        }

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

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 ===========================================================================
*/
