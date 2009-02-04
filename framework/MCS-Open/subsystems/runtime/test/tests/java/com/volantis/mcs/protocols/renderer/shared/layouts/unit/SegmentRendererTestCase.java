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

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.SegmentMock;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.protocols.AttributesTestHelper;
import com.volantis.mcs.protocols.SegmentAttributes;
import com.volantis.mcs.protocols.SegmentAttributesMock;
import com.volantis.mcs.protocols.renderer.shared.layouts.SegmentRenderer;
import com.volantis.mcs.protocols.layouts.SegmentInstanceMock;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

/**
 * Tests rendering functionality of default segment renderer.
 */
public class SegmentRendererTestCase
        extends AbstractSegmentRendererTestAbstract {

    private static final String MARGIN_HEIGHT_STRING = "1";
    private static final int MARGIN_WIDTH_INT = 2;
    private static final String MARGIN_WIDTH_STRING = "2";
    private static final int MARGIN_HEIGHT_INT = 1;
    private static final String RESIZE_VALUE_STRING = "true";
    private static final boolean RESIZE_VALUE_BOOLEAN = true;

    /**
     * Tests that attributes are passed through correctly.
     * @throws Exception if an error occurs
     */
    public void testAttributesPassed() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final SegmentInstanceMock segmentInstanceMock =
                new SegmentInstanceMock("segmentInstanceMock", expectations,
                                        NDimensionalIndex.ZERO_DIMENSIONS);

        final SegmentMock segmentMock = LayoutTestHelper.createSegmentMock(
                "segmentMock", expectations, montageLayoutMock);

        final SegmentAttributesMock segmentAttributesMock =
                (SegmentAttributesMock)
                AttributesTestHelper.createMockAttributes(
                        SegmentAttributesMock.class, "segment",
                        "segmentAttributesMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        segmentInstanceMock.expects.isEmpty().returns(false).any();
        segmentInstanceMock.expects.getFormat().returns(segmentMock).any();
        segmentInstanceMock.expects.getAttributes()
                .returns(segmentAttributesMock)
                .any();

        // Initialise properties of segment.
        segmentMock.expects.getAttribute(FormatConstants.BORDER_COLOUR_ATTRIBUTE)
                .returns(BORDER_COLOR).atLeast(1);
        segmentMock.expects.getAttribute(FormatConstants.FRAME_BORDER_ATTRIBUTE)
                .returns(FRAME_BORDER_STRING).atLeast(1);
        segmentMock.expects.getAttribute(FormatConstants.MARGIN_HEIGHT_ATTRIBUTE)
                .returns(MARGIN_HEIGHT_STRING).atLeast(1);
        segmentMock.expects.getAttribute(FormatConstants.MARGIN_WIDTH_ATTRIBUTE)
                .returns(MARGIN_WIDTH_STRING).atLeast(1);
        segmentMock.expects.getAttribute(FormatConstants.RESIZE_ATTRIBUTE)
                .returns(RESIZE_VALUE_STRING).atLeast(1);
        // NB: Name attribute is not passed through
        segmentMock.expects.getAttribute(FormatConstants.SCROLLING_ATTRIBUTE)
                .returns(FormatConstants.SCROLLING_VALUE_YES).atLeast(1);
        // NB: Style class should NOT be passed through for segments

        // Make sure that the properties are set on the attributes.
        segmentAttributesMock.expects.setBorderColor(BORDER_COLOR).atLeast(1);
        segmentAttributesMock.expects.setFrameBorder(FRAME_BORDER_BOOLEAN)
                .atLeast(1);
        segmentAttributesMock.expects.setMarginWidth(MARGIN_WIDTH_INT)
                .atLeast(1);
        segmentAttributesMock.expects.setMarginHeight(MARGIN_HEIGHT_INT)
                .atLeast(1);
        segmentAttributesMock.expects
                .setScrolling(SegmentAttributes.SCROLLING_YES)
                .atLeast(1);
        segmentAttributesMock.expects.setResize(RESIZE_VALUE_BOOLEAN)
                .atLeast(1);

        // Make sure that the layout module is invoked properly.
        expectations.add(new OrderedExpectations() {
            public void add() {

                layoutModuleMock.expects.writeOpenSegment(segmentAttributesMock);

                layoutModuleMock.expects.writeCloseSegment(segmentAttributesMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        SegmentRenderer renderer = new SegmentRenderer();
        renderer.render(formatRendererContextMock, segmentInstanceMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/1	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 ===========================================================================
*/
