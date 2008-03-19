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

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Segment;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.MontageLayout;
import com.volantis.mcs.protocols.layouts.SegmentInstance;

/**
 * Tests rendering functionality of default segment renderer.
 *
 * @todo Remove when code coverage indicates that unit tests have same coverage.
 */
public class SegmentRendererTestCase
        extends AbstractFormatRendererTestAbstract {

    protected Layout createDeviceLayout() {
        return new MontageLayout();
    }

    /**
     * Tests that attributes are passed through correctly.
     * @throws Exception if an error occurs
     */
    public void testAttributesPassed() throws Exception {
        // Create a segment
        Segment segment = new Segment((MontageLayout) layout);

        segment.setAttribute(FormatConstants.BORDER_COLOUR_ATTRIBUTE, "teal");
        segment.setAttribute(FormatConstants.FRAME_BORDER_ATTRIBUTE, "true");
        segment.setAttribute(FormatConstants.MARGIN_HEIGHT_ATTRIBUTE, "1");
        segment.setAttribute(FormatConstants.MARGIN_WIDTH_ATTRIBUTE, "2");
        // NB: Name attribute is not passed through 
        segment.setAttribute(FormatConstants.NAME_ATTRIBUTE, "seggy");
        segment.setAttribute(FormatConstants.SCROLLING_ATTRIBUTE,
                             FormatConstants.SCROLLING_VALUE_YES);
        // NB: Style class should NOT be passed through for segments
        segment.setAttribute(FormatConstants.STYLE_CLASS, "rowIteratorStyle");

        SegmentInstance instance =
                (SegmentInstance) dlContext.getFormatInstance(segment,
                NDimensionalIndex.ZERO_DIMENSIONS);

        // Expected output for attributes specified above
        String expected = "<segment borderColor=\"teal\" " +
                                   "frameBorder=\"true\" " +
                                   "marginHeight=\"1\" " +
                                   "marginWidth=\"2\" " +
                                   "scrolling=\"2\">" +
                          "</segment>";

        checkRenderResults(instance, new SegmentRenderer(), expected);
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
