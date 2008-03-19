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

import com.volantis.mcs.layouts.DissectingPaneMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.AttributesTestHelper;
import com.volantis.mcs.protocols.DissectingPaneAttributesMock;
import com.volantis.mcs.protocols.OutputBufferMock;
import com.volantis.mcs.protocols.PaneAttributesMock;
import com.volantis.mcs.protocols.layouts.DissectingPaneInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.shared.layouts.DissectingPaneRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.styling.StylesMock;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

/**
 * Tests rendering functionality of default dissecting pane renderer.
 */
public class DissectingPaneRendererTestCase
        extends PaneRendererTestAbstract {

    private static final String INCLUSION_PATH = "inclusion path";
    private static final String LINK_TO_TEXT = "Link To Text";
    private static final String LINK_FROM_TEXT = "Link From Text";
    private static final String TAG_NAME = "pane";
    private static final boolean NEXT_LINK_NOT_FIRST = false;

    /**
     * Tests that attributes are passed through correctly.
     * @throws Exception if an error occurs
     */
    public void testAttributesPassed() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final DissectingPaneInstanceMock paneInstanceMock =
                new DissectingPaneInstanceMock(
                        "paneInstanceMock", expectations,
                        NDimensionalIndex.ZERO_DIMENSIONS);

        final DissectingPaneMock paneMock =
                LayoutTestHelper.createDissectingPaneMock(
                        "paneMock", expectations, canvasLayoutMock);

        // A mock of the attributes stored within the dissecting pane instance.
        final PaneAttributesMock paneAttributesMock = (PaneAttributesMock)
                AttributesTestHelper.createMockAttributes(
                        PaneAttributesMock.class, TAG_NAME, "paneAttributesMock",
                        expectations);

        final DissectingPaneAttributesMock dissectingPaneAttributesMock =
                (DissectingPaneAttributesMock)
                AttributesTestHelper.createMockAttributes(
                        DissectingPaneAttributesMock.class,
                        TAG_NAME, "dissectingPaneAttributesMock", expectations);

        final StylesMock stylesMock =
                new StylesMock("stylesMock", expectations);

        final OutputBufferMock paneContentsMock =
                new OutputBufferMock("paneContentsMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // In this test the instance is never empty.
        paneInstanceMock.expects.isEmpty().returns(false).any();

        // Associate the format with the instance.
        paneInstanceMock.expects.getFormat()
                .returns(paneMock).any();

        // Associate the attributes with the instance.
        paneInstanceMock.expects.getAttributes()
                .returns(paneAttributesMock).any();

        // Initialise the context.
        formatRendererContextMock.expects.getInclusionPath()
                .returns(INCLUSION_PATH).any();

        layoutAttributesFactoryMock.expects.createDissectingPaneAttributes()
                .returns(dissectingPaneAttributesMock);

        // Initialise the pane attributes.
        paneAttributesMock.expects.setStyles(stylesMock).atLeast(1);
        paneAttributesMock.expects.getStyles()
                .returns(stylesMock).atLeast(1);

        // Initialise the pane.
        paneMock.expects.getParent().returns(null).any();
        paneMock.expects.isNextLinkFirst()
                .returns(NEXT_LINK_NOT_FIRST).atLeast(1);

        expectGetDefaultPaneAttributes(paneMock.expects);

        // Initialise the pane instance.
        paneInstanceMock.expects.getLinkToText()
                .returns(LINK_TO_TEXT).atLeast(1);
        paneInstanceMock.expects.getLinkFromText()
                .returns(LINK_FROM_TEXT).atLeast(1);
        paneInstanceMock.expects.getCurrentBuffer(false)
                .returns(paneContentsMock).any();
        paneInstanceMock.expects.getStyleClass()
                .returns(STYLE_CLASS).any();

        // Set up the properties.
        dissectingPaneAttributesMock.expects.setStyles(stylesMock);

        dissectingPaneAttributesMock.expects
                .setInclusionPath(INCLUSION_PATH).atLeast(1);
        dissectingPaneAttributesMock.expects
                .setDissectingPane(paneMock).atLeast(1);
        dissectingPaneAttributesMock.expects
                .setIsNextLinkFirst(NEXT_LINK_NOT_FIRST).atLeast(1);
        dissectingPaneAttributesMock.expects
                .setLinkText(LINK_TO_TEXT).atLeast(1);
        dissectingPaneAttributesMock.expects
                .setBackLinkText(LINK_FROM_TEXT).atLeast(1);

        expectSetPaneAttributes(paneAttributesMock, paneMock);

        expectations.add(new OrderedExpectations() {
            public void add() {

                formatStylingEngineMock.expects
                        .startStyleable(paneMock, STYLE_CLASS)
                        .returns(stylesMock);

                layoutModuleMock.expects.writeOpenDissectingPane(
                        dissectingPaneAttributesMock);

                layoutModuleMock.expects.writeOpenPane(paneAttributesMock);

                layoutModuleMock.expects.writePaneContents(paneContentsMock);

                layoutModuleMock.expects.writeClosePane(paneAttributesMock);

                layoutModuleMock.expects.writeCloseDissectingPane(
                        dissectingPaneAttributesMock);

                formatStylingEngineMock.expects.endStyleable(paneMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        FormatRenderer renderer = new DissectingPaneRenderer(
                layoutAttributesFactoryMock);
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
