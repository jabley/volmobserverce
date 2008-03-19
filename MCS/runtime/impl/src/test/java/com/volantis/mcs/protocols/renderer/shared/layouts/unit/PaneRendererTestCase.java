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
import com.volantis.mcs.layouts.PaneMock;
import com.volantis.mcs.protocols.AttributesTestHelper;
import com.volantis.mcs.protocols.OutputBufferMock;
import com.volantis.mcs.protocols.PaneAttributesMock;
import com.volantis.mcs.protocols.layouts.PaneInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.shared.layouts.PaneRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.styling.StylesMock;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

/**
 * Tests rendering functionality of default pane renderer.
 */
public class PaneRendererTestCase extends PaneRendererTestAbstract {

    private PaneMock paneMock;
    private OutputBufferMock paneContentsMock;

    // Javadoc inherited from superclass
    public void setUp() throws Exception {
        super.setUp();

        paneMock = LayoutTestHelper.createPaneMock(
                "pane", expectations, canvasLayoutMock);
        paneContentsMock = new OutputBufferMock("paneContents", expectations);
    }

    /**
     * Tests that attributes are passed through correctly.
     * @throws Exception if an error occurs
     */
    public void testAttributesPassed()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        PaneInstanceMock paneInstanceMock = new PaneInstanceMock(
                "paneInstance", expectations,
                NDimensionalIndex.ZERO_DIMENSIONS);

        final PaneAttributesMock paneAttributesMock = (PaneAttributesMock)
                AttributesTestHelper.createMockAttributes(
                        PaneAttributesMock.class,
                        "pane", "paneAttributes", expectations);

        final StylesMock stylesMock =
                new StylesMock("stylesMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Pane instance is not empty.
        paneInstanceMock.expects.isEmpty()
                .returns(false)
                .any();

        // Associate a format with the instance.
        paneInstanceMock.expects.getFormat()
                .returns(paneMock)
                .any();

        // Pane is root of the layout.
        paneMock.expects.getParent().returns(null).any();

        // Set the attributes on the pane.
        expectGetDefaultPaneAttributes(paneMock.expects);

        // Get the attributes from the pane instance.
        paneInstanceMock.expects.getAttributes()
                .returns(paneAttributesMock).any();
        paneInstanceMock.expects.getStyleClass()
                .returns(STYLE_CLASS).any();

        // Set the current buffer.
        paneInstanceMock.expects.getCurrentBuffer(false)
                .returns(paneContentsMock)
                .any();

        // Make sure that the attributes are copied.
        paneAttributesMock.expects.setStyles(stylesMock).atLeast(1);
        expectSetPaneAttributes(paneAttributesMock, paneMock);

        // Initialise the context.

        expectations.add(new OrderedExpectations() {
            public void add() {

                formatStylingEngineMock.expects
                        .startStyleable(paneMock, STYLE_CLASS)
                        .returns(stylesMock);

                layoutModuleMock.expects.writeOpenPane(paneAttributesMock);

                layoutModuleMock.expects.writePaneContents(paneContentsMock);

                layoutModuleMock.expects.writeClosePane(paneAttributesMock);

                formatStylingEngineMock.expects.endStyleable(paneMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        FormatRenderer renderer = new PaneRenderer();
        renderer.render(formatRendererContextMock, paneInstanceMock);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/2	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 ===========================================================================
*/
