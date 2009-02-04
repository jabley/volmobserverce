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

import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.ReplicaMock;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.protocols.layouts.FormatInstanceMock;
import com.volantis.mcs.protocols.layouts.ReplicaInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.shared.layouts.unit.AbstractFormatRendererTestAbstract;
import com.volantis.mcs.protocols.renderer.shared.layouts.ReplicaRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;

/**
 * Tests rendering functionality of default replica renderer.
 */
public class ReplicaRendererTestCase
        extends AbstractFormatRendererTestAbstract {

    /**
     * Tests that attributes are passed through correctly.
     * @throws Exception if an error occurs
     */
    public void testAttributesPassed() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final NDimensionalIndex INDEX = NDimensionalIndex.ZERO_DIMENSIONS;
        final ReplicaInstanceMock replicaInstanceMock =
                new ReplicaInstanceMock("replicaInstanceMock", expectations,
                                        INDEX);

        final ReplicaMock replicaMock = LayoutTestHelper.createReplicaMock(
                "replicaMock", expectations, canvasLayoutMock);

        final FormatInstanceMock formatInstanceMock =
                new FormatInstanceMock("formatInstanceMock", expectations,
                                       INDEX);

        final FormatMock formatMock = LayoutTestHelper.createFormatMock(
                "formatMock", expectations, canvasLayoutMock);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        replicaInstanceMock.expects.isEmpty().returns(false).any();
        replicaInstanceMock.expects.getFormat().returns(replicaMock).any();
        replicaInstanceMock.expects.getIndex().returns(INDEX).any();

        replicaMock.expects.getFormat().returns(formatMock).any();

        formatInstanceMock.expects.getFormat().returns(formatMock).any();

        // Create an association between the child instance and its format.

        FormatRendererTestHelper.connectFormatInstanceToFormat(
                formatRendererContextMock,
                formatInstanceMock.expects, formatMock, NDimensionalIndex.ZERO_DIMENSIONS);

        // The format referenced by the replica will be rendered.
        formatRendererContextMock.expects.renderFormat(formatInstanceMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        FormatRenderer renderer = new ReplicaRenderer();
        renderer.render(formatRendererContextMock, replicaInstanceMock);
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
