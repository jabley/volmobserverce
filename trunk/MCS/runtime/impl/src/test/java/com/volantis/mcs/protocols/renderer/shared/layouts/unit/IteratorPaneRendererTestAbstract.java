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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts.unit;

import com.volantis.mcs.layouts.IteratorPane;
import com.volantis.mcs.layouts.IteratorPaneMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.OutputBufferMock;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneAttributesMock;
import com.volantis.mcs.protocols.layouts.IteratorPaneInstance;
import com.volantis.mcs.protocols.layouts.IteratorPaneInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.styling.StylesMock;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import mock.java.util.CollectionsTestHelper;
import mock.java.util.IteratorMock;

/**
 * Base class for all automatically iterated panes.
 */
public abstract class IteratorPaneRendererTestAbstract
        extends PaneRendererTestAbstract {

    /**
     * The object used to associate expectations with the mock pane object.
     */
    protected IteratorPaneMock.Expects iteratorPaneMockExpects;

    /**
     * The mock pane object.
     */
    protected IteratorPane iteratorPaneMock;

    // Javadoc inherited.
    public void setUp() throws Exception {
        super.setUp();

        iteratorPaneMockExpects = createIteratorPaneMockExpects();
        iteratorPaneMock = (IteratorPane)
                iteratorPaneMockExpects._getMock();
    }

    /**
     * Create an iterator pane mock and return its expects instance.
     *
     * <p>This returns an Expects instance because the iterator pane mock
     * objects do not share a common base mock class even though the iterator
     * panes do. This is due to the fact that Java does not support multiple
     * inheritance of classes.</p>
     *
     * @return The expects instance of a newly instantiated iterator pane mock.
     */
    protected abstract IteratorPaneMock.Expects createIteratorPaneMockExpects();

    /**
     * Create an iterator pane instance mock and return its expects instance.
     *
     * @param index The index of the instance.
     *
     * @return The expects instance of a newly instantiated iterator pane
     * instance mock.
     */
    protected abstract IteratorPaneInstanceMock.Expects createIteratorPaneInstanceExpects(NDimensionalIndex index);

    /**
     * Create a pane attributes mock and return its expects instance.
     *
     * @return The expects instance of a newly instantiated pane attributes
     * mock.
     */
    protected abstract PaneAttributesMock.Expects createPaneAttributeExpects()
            throws Exception;

    /**
     * Create the renderer to test.
     *
     * @return The renderer to test.
     */
    protected abstract FormatRenderer createFormatRenderer();

    /**
     * Add expectations when closing a pane.
     *
     * @param paneAttributesMock The mock pane attributes.
     */
    protected abstract void expectsWriteClosePane(
            PaneAttributes paneAttributesMock);

    /**
     * Add expectations when closing a pane element.
     *
     * @param paneAttributesMock The mock pane attributes.
     */
    protected abstract void expectsWriteClosePaneElement(
            PaneAttributes paneAttributesMock);

    /**
     * Add expectations when writing a pane element contents.
     *
     * @param buffer1 The buffer containing the pane's contents.
     */
    protected abstract void expectsWritePaneElementContents(
            OutputBufferMock buffer1);

    /**
     * Add expectations when opening a pane element.
     *
     * @param paneAttributesMock The mock pane attributes.
     */
    protected abstract void expectsWriteOpenPaneElement(
            PaneAttributes paneAttributesMock);

    /**
     * Add expectations when opening a pane.
     *
     * @param paneAttributesMock The mock pane attributes.
     */
    protected abstract void expectsWriteOpenPane(
            PaneAttributes paneAttributesMock);

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

        IteratorPaneInstanceMock.Expects paneInstanceMockExpects =
                createIteratorPaneInstanceExpects(
                        NDimensionalIndex.ZERO_DIMENSIONS);

        IteratorPaneInstance paneInstanceMock = (IteratorPaneInstance)
                paneInstanceMockExpects._getMock();

        PaneAttributesMock.Expects paneAttributesMockExpects =
                createPaneAttributeExpects();
        final PaneAttributes paneAttributesMock = (PaneAttributes)
                paneAttributesMockExpects._getMock();

        final OutputBufferMock buffer1 = new OutputBufferMock(
                "buffer1", expectations);
        final OutputBufferMock buffer2 = new OutputBufferMock(
                "buffer2", expectations);

        final StylesMock iteratorPaneStylesMock =
                new StylesMock("iteratorPaneStylesMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Pane instance is not empty.
        paneInstanceMockExpects.isEmpty()
                .returns(false)
                .any();

        // Associate a format with the instance.
        paneInstanceMockExpects.getFormat()
                .returns(iteratorPaneMock)
                .any();

        // Pane is root of the layout.
        iteratorPaneMockExpects.getParent().returns(null).any();

        // Set the attributes on the pane.
//        iteratorPaneMockExpects.getBackgroundComponent().returns(null).any();
//        iteratorPaneMockExpects.getBackgroundComponentType().returns(null).any();
//        iteratorPaneMockExpects.getBackgroundColour().returns(BACKGROUND_COLOR).any();
//        iteratorPaneMockExpects.getBorderWidth().returns(BORDER_WIDTH_STRING).any();
//        iteratorPaneMockExpects.getCellPadding().returns(CELL_PADDING).any();
//        iteratorPaneMockExpects.getCellSpacing().returns(CELL_SPACING).any();
//        iteratorPaneMockExpects.getHeight().returns(HEIGHT).any();
//        iteratorPaneMockExpects.getWidth().returns(WIDTH).any();
//        iteratorPaneMockExpects.getWidthUnits().returns(WIDTH_UNITS).any();

        // Get the attributes from the pane instance.
        paneInstanceMockExpects.getAttributes()
                .returns(paneAttributesMock)
                .any();

        // Style class is used for styling.
        paneInstanceMockExpects.getStyleClass().returns(STYLE_CLASS).any();
//        paneAttributesMockExpects.setStyleClass(STYLE_CLASS).any();

        // Make sure that the attributes are copied.
//        paneAttributesMockExpects.setBackgroundColour(BACKGROUND_COLOR).atLeast(1);
//        paneAttributesMockExpects.setBackgroundImage(null).atLeast(1);
//        paneAttributesMockExpects.setBorderWidth(BORDER_WIDTH_STRING).atLeast(1);
//        paneAttributesMockExpects.setCellPadding(CELL_PADDING).atLeast(1);
//        paneAttributesMockExpects.setCellSpacing(CELL_SPACING).atLeast(1);
        paneAttributesMockExpects.setPane(iteratorPaneMock).atLeast(1);
//        paneAttributesMockExpects.setHeight(HEIGHT).atLeast(1);
//        paneAttributesMockExpects.setWidth(WIDTH).atLeast(1);
//        paneAttributesMockExpects.setWidthUnits(WIDTH_UNITS).atLeast(1);
        paneAttributesMockExpects.setStyles(iteratorPaneStylesMock).atLeast(1);

        // Initialise the format context.

        // Neither of the buffers are empty.
        buffer1.expects.isEmpty().returns(false).any();
        buffer2.expects.isEmpty().returns(false).any();

        final IteratorMock iteratorMock = new IteratorMock(
                "iteratorMock", expectations);

        paneInstanceMockExpects.getBufferIterator().returns(iteratorMock);

        expectations.add(new OrderedExpectations() {
            public void add() {

                // Style the pane.
                formatStylingEngineMock.expects
                        .startStyleable(iteratorPaneMock, STYLE_CLASS)
                        .returns(iteratorPaneStylesMock);

                expectsWriteOpenPane(paneAttributesMock);

                // Buffer 1
                CollectionsTestHelper.addNextIteration(iteratorMock, buffer1);

                expectsWriteOpenPaneElement(paneAttributesMock);

                expectsWritePaneElementContents(buffer1);

                expectsWriteClosePaneElement(paneAttributesMock);

                // Buffer 2
                CollectionsTestHelper.addNextIteration(iteratorMock, buffer2);

                expectsWriteOpenPaneElement(paneAttributesMock);

                expectsWritePaneElementContents(buffer2);

                expectsWriteClosePaneElement(paneAttributesMock);

                // End
                CollectionsTestHelper.endIteration(iteratorMock);

                expectsWriteClosePane(paneAttributesMock);

                // Finish styling the pane.
                formatStylingEngineMock.expects.endStyleable(iteratorPaneMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        FormatRenderer renderer = createFormatRenderer();
        renderer.render(formatRendererContextMock,
                        paneInstanceMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/
