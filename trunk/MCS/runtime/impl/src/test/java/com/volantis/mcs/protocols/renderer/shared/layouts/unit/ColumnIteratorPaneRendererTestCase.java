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

import com.volantis.mcs.layouts.IteratorPaneMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.AttributesTestHelper;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributesMock;
import com.volantis.mcs.protocols.OutputBufferMock;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneAttributesMock;
import com.volantis.mcs.protocols.layouts.ColumnIteratorPaneInstanceMock;
import com.volantis.mcs.protocols.layouts.IteratorPaneInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.shared.layouts.ColumnIteratorPaneRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.testtools.mock.method.CallUpdaterReturnsVoid;

/**
 * Tests rendering functionality of default column iterator renderer.
 */
public class ColumnIteratorPaneRendererTestCase
        extends IteratorPaneRendererTestAbstract {

    // Javadoc inherited.
    protected IteratorPaneMock.Expects createIteratorPaneMockExpects() {
        return LayoutTestHelper.createColumnIteratorPaneMock(
                "columnIteratorPane", expectations, canvasLayoutMock).expects;
    }

    // Javadoc inherited.
    protected IteratorPaneInstanceMock.Expects createIteratorPaneInstanceExpects(
            NDimensionalIndex index) {

        ColumnIteratorPaneInstanceMock paneInstanceMock =
                new ColumnIteratorPaneInstanceMock(
                        "paneInstanceMock", expectations,
                        NDimensionalIndex.ZERO_DIMENSIONS);

        return paneInstanceMock.expects;
    }

    // Javadoc inherited.
    protected PaneAttributesMock.Expects createPaneAttributeExpects()
            throws Exception {

        final ColumnIteratorPaneAttributesMock paneAttributesMock
                = (ColumnIteratorPaneAttributesMock)
                AttributesTestHelper.createMockAttributes(
                        ColumnIteratorPaneAttributesMock.class,
                        "pane", "paneAttributesMock", expectations);

        return paneAttributesMock.expects;
    }

    protected FormatRenderer createFormatRenderer() {
        return new ColumnIteratorPaneRenderer();
    }

    // Javadoc inherited.
    protected void expectsWriteOpenPane(
            PaneAttributes paneAttributesMock) {

        layoutModuleMock.fuzzy.writeOpenColumnIteratorPane(
                mockFactory.expectsInstanceOf(
                        ColumnIteratorPaneAttributes.class));
    }

    // Javadoc inherited.
    protected void expectsWriteClosePane(
            PaneAttributes paneAttributesMock) {

        layoutModuleMock.expects.writeCloseColumnIteratorPane(
                (ColumnIteratorPaneAttributes) paneAttributesMock);
    }

    // Javadoc inherited.
    protected void expectsWriteOpenPaneElement(
            PaneAttributes paneAttributesMock) {

        layoutModuleMock.fuzzy.writeOpenColumnIteratorPaneElement(
                mockFactory.expectsInstanceOf(
                        ColumnIteratorPaneAttributes.class));
    }

    // Javadoc inherited.
    protected void expectsWritePaneElementContents(
            OutputBufferMock buffer1) {

        layoutModuleMock.expects.writeColumnIteratorPaneElementContents(
                buffer1);
    }

    // Javadoc inherited.
    protected void expectsWriteClosePaneElement(
            PaneAttributes paneAttributesMock) {

        layoutModuleMock.expects.writeCloseColumnIteratorPaneElement(
                (ColumnIteratorPaneAttributes) paneAttributesMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10282/1	emma	VBM:2005110902 Forward port: fixing two layout rendering bugs

 11-Nov-05	10253/1	emma	VBM:2005110902 Fixing two layout rendering bugs

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/1	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 ===========================================================================
*/
