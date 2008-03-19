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
import com.volantis.mcs.layouts.RegionMock;
import com.volantis.mcs.protocols.RegionContentMock;
import com.volantis.mcs.protocols.layouts.RegionInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.shared.layouts.RegionRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.testtools.mock.expectations.OrderedExpectations;


/**
 * Tests rendering functionality of default region renderer.
 */
public class RegionRendererTestCase
        extends AbstractFormatRendererTestAbstract {

    /**
     * Tests that attributes are passed through correctly.
     *
     * @throws Exception if an error occurs
     */
    public void testAttributesPassed()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        RegionInstanceMock regionInstanceMock = new RegionInstanceMock(
                "regionInstanceMock", expectations,
                NDimensionalIndex.ZERO_DIMENSIONS);
        final RegionMock regionMock = LayoutTestHelper.createRegionMock(
                "regionMock", expectations, canvasLayoutMock);

        final RegionContentMock regionContent1 = new RegionContentMock(
                "regionContent1", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        regionInstanceMock.expects.isEmpty().returns(false).any();
        regionInstanceMock.expects.getFormat().returns(regionMock).any();

        regionInstanceMock.expects.getRegionContent()
                .returns(regionContent1).any();

        expectations.add(new OrderedExpectations() {
            public void add() {

                regionContent1.expects.render(formatRendererContextMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        FormatRenderer renderer = new RegionRenderer();
        renderer.render(formatRendererContextMock, regionInstanceMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/2	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 ===========================================================================
*/
