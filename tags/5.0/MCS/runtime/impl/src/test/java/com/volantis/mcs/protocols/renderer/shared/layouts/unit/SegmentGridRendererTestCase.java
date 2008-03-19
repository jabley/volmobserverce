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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts.unit;

import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.SegmentGridMock;
import com.volantis.mcs.protocols.AttributesTestHelper;
import com.volantis.mcs.protocols.SegmentGridAttributesMock;
import com.volantis.mcs.protocols.layouts.FormatInstanceMock;
import com.volantis.mcs.protocols.layouts.SegmentGridInstanceMock;
import com.volantis.mcs.protocols.renderer.shared.layouts.SegmentGridRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

/**
 * Tests rendering functionality of default segment grid renderer.
 */
public class SegmentGridRendererTestCase
        extends AbstractSegmentRendererTestAbstract {

    private static final String COLUMN_STYLE_CLASS = "column-class";
    private static final String ROW_STYLE_CLASS = "row-class";
    private static final String FRAME_SPACING_STRING = "10";
    private static final int FRAME_SPACING_INT = 10;
    private static final int ROWS = 1;
    private static final int COLUMNS = 1;

    /**
     * Tests that attributes are passed through correctly.
     * @throws Exception if an error occurs
     */
    public void testAttributesPassed() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final SegmentGridInstanceMock segmentGridInstanceMock =
                new SegmentGridInstanceMock(
                        "segmentGridInstanceMock", expectations,
                        NDimensionalIndex.ZERO_DIMENSIONS);


        final SegmentGridMock segmentGridMock =
                LayoutTestHelper.createSegmentGridMock(
                "segmentGridMock", expectations, montageLayoutMock,
                ROWS, COLUMNS);

        final SegmentGridAttributesMock segmentGridAttributesMock =
                (SegmentGridAttributesMock)
                AttributesTestHelper.createMockAttributes(
                SegmentGridAttributesMock.class, "montage",
                "segmentGridAttributesMock", expectations);

        final FormatInstanceMock childInstanceMock =
                new FormatInstanceMock("childInstanceMock", expectations,
                                       NDimensionalIndex.ZERO_DIMENSIONS);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the grid rows and columns.
        LayoutTestHelper.initialiseRows(segmentGridMock, ROW_STYLE_CLASS);
        LayoutTestHelper.initialiseColumns(segmentGridMock, COLUMN_STYLE_CLASS);

        // Initialise the format context.
        final FormatMock childMock = (FormatMock) segmentGridMock.getChildAt(0);

        // Create an association between the child instance and its format.

        FormatRendererTestHelper.connectFormatInstanceToFormat(
                formatRendererContextMock,
                childInstanceMock.expects, childMock, NDimensionalIndex.ZERO_DIMENSIONS);

        // Initialise the instance.
        segmentGridInstanceMock.expects.isEmpty().returns(false).any();
        segmentGridInstanceMock.expects.getFormat()
                .returns(segmentGridMock)
                .any();
        segmentGridInstanceMock.expects.getAttributes()
                .returns(segmentGridAttributesMock)
                .any();
        segmentGridInstanceMock.expects.getIndex()
                .returns(NDimensionalIndex.ZERO_DIMENSIONS)
                .any();

        // Initialise properties of segment grid.
        segmentGridMock.expects
                .getAttribute(FormatConstants.BORDER_COLOUR_ATTRIBUTE)
                .returns(BORDER_COLOR)
                .atLeast(1);
        segmentGridMock.expects
                .getAttribute(FormatConstants.BORDER_WIDTH_ATTRIBUTE)
                .returns(BORDER_WIDTH_STRING)
                .atLeast(1);
        segmentGridMock.expects
                .getAttribute(FormatConstants.FRAME_BORDER_ATTRIBUTE)
                .returns(FRAME_BORDER_STRING)
                .atLeast(1);
        segmentGridMock.expects
                .getAttribute(FormatConstants.FRAME_SPACING_ATTRIBUTE)
                .returns(FRAME_SPACING_STRING)
                .atLeast(1);

        // Make sure that the properties are initialised correctly.
        segmentGridAttributesMock.expects.setBorderColor(BORDER_COLOR)
                .atLeast(1);
        segmentGridAttributesMock.expects.setBorderWidth(BORDER_WIDTH_INT)
                .atLeast(1);
        segmentGridAttributesMock.expects.setFrameBorder(FRAME_BORDER_BOOLEAN)
                .atLeast(1);
        segmentGridAttributesMock.expects.setFrameSpacing(FRAME_SPACING_INT)
                .atLeast(1);

        final int[] columnWidths = new int[] {100};
        final String[] columnWidthUnits = new String[] {
            FormatConstants.WIDTH_UNITS_VALUE_PERCENT
        };

        segmentGridAttributesMock.expects
                .setColumnWidths(columnWidths)
                .atLeast(1);
        segmentGridAttributesMock.expects
                .setColumnWidthUnits(columnWidthUnits)
                .atLeast(1);

        final int[] rowHeights = new int[]{100};
        final String[] rowHeightUnits = new String[]{
            FormatConstants.HEIGHT_UNITS_VALUE_PIXELS
        };

        segmentGridAttributesMock.expects.setRowHeights(rowHeights)
                .atLeast(1);
        segmentGridAttributesMock.expects.setRowHeightUnits(rowHeightUnits)
                .atLeast(1);

        // Make sure that the layout module is invoked properly.
        expectations.add(new OrderedExpectations() {
            public void add() {

                layoutModuleMock.expects
                        .writeOpenSegmentGrid(segmentGridAttributesMock);

                formatRendererContextMock.expects
                        .renderFormat(childInstanceMock);

                layoutModuleMock.expects
                        .writeCloseSegmentGrid(segmentGridAttributesMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        SegmentGridRenderer renderer = new SegmentGridRenderer();
        renderer.render(formatRendererContextMock,  segmentGridInstanceMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/2	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 ===========================================================================
*/
