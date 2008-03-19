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

import com.volantis.mcs.layouts.CanvasLayoutMock;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.layouts.MontageLayoutMock;
import com.volantis.mcs.protocols.FormatAttributesMock;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactoryMock;
import com.volantis.mcs.protocols.layouts.LayoutModuleMock;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContextMock;
import com.volantis.mcs.protocols.renderer.layouts.FragmentLinkWriterMock;
import com.volantis.mcs.protocols.renderer.shared.layouts.AbstractFormatRenderer;
import com.volantis.mcs.protocols.renderer.shared.layouts.IteratedFormatInstanceCounterMock;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutMock;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngineMock;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.ExpectationBuilder;

/**
 * Abstract base for extension by tests of
 * {@link AbstractFormatRenderer} specializations.
 */
public abstract class AbstractFormatRendererTestAbstract
        extends TestCaseAbstract {

    protected static final String STYLE_CLASS = "xyz";
    protected static final String BACKGROUND_COLOR = "red";
    protected static final String BORDER_WIDTH_STRING = "10";
    protected static final int BORDER_WIDTH_INT = 10;
    protected static final String CELL_PADDING = "0";
    protected static final String CELL_SPACING = "1";
    protected static final String HEIGHT = "11";
    protected static final String WIDTH = "12";
    protected static final String WIDTH_UNITS =
            FormatConstants.WIDTH_UNITS_VALUE_PERCENT;

    protected CanvasLayoutMock canvasLayoutMock;
    protected MontageLayoutMock montageLayoutMock;
    protected FormatRendererContextMock formatRendererContextMock;
    protected LayoutModuleMock layoutModuleMock;
    protected LayoutAttributesFactoryMock layoutAttributesFactoryMock;
    protected FragmentLinkWriterMock fragmentLinkWriterMock;
    protected IteratedFormatInstanceCounterMock instanceCounterMock;
    protected RuntimeDeviceLayoutMock runtimeDeviceLayoutMock;
    protected FormatStylingEngineMock formatStylingEngineMock;

    // Javadoc inherited from superclass
    public void setUp() throws Exception {
        super.setUp();

        canvasLayoutMock = new CanvasLayoutMock("canvasLayoutMock", expectations);

        montageLayoutMock = new MontageLayoutMock("montageLayoutMock", expectations);

        runtimeDeviceLayoutMock = new RuntimeDeviceLayoutMock(
                "runtimeDeviceLayoutMock", expectations);

        layoutModuleMock = new LayoutModuleMock("layoutModule", expectations);

        formatRendererContextMock = new FormatRendererContextMock(
                "formatRendererContextMock", expectations);

        fragmentLinkWriterMock =
                new FragmentLinkWriterMock("fragmentLinkWriterMock",
                                           expectations);

        layoutAttributesFactoryMock =
                new LayoutAttributesFactoryMock("layoutAttributesFactoryMock",
                                                expectations);

        instanceCounterMock =
                new IteratedFormatInstanceCounterMock("instanceCounterMock",
                                                      expectations);

        formatStylingEngineMock = new FormatStylingEngineMock(
                "formatStylingEngineMock", expectations);

        // Connect the mock objects together.
        formatRendererContextMock.expects.getLayoutModule()
                .returns(layoutModuleMock).any();
        formatRendererContextMock.expects.getFragmentLinkWriter()
                .returns(fragmentLinkWriterMock).any();
        formatRendererContextMock.expects.getDeviceLayout()
                .returns(runtimeDeviceLayoutMock).any();
        formatRendererContextMock.expects.getInstanceCounter()
                .returns(instanceCounterMock).any();
        formatRendererContextMock.expects.getFormatStylingEngine()
                .returns(formatStylingEngineMock).any();

    }

    /**
     * Verifies that rendering the specified format instance with the
     * specified format renderer produces the expected output.
     * @param toRender The format instance to render
     * @param renderer The format renderer to use
     * @param expectedOutput The output expected from the rendering process
     * @throws RendererException if an exception occurs while rendering.
     */
    protected void checkRenderResults(FormatInstance toRender,
                                      FormatRenderer renderer,
                                      String expectedOutput)
            throws Exception {
    }

    /**
     * Compare a string containing an XML fragment to the contents of a
     * DOMOutputBuffer.
     * @param expected The expected content of the output buffer
     * @param actual The output buffer to compare against
     * @throws Exception if an error occurs
     */
    protected void compareStringToBuffer(String expected,
                                         OutputBuffer actual)
            throws Exception {
    }

    /**
     * Initialise the mock with default pane and grid attributes.
     *
     * @param expects The mock's expects instance.
     */
    protected void expectGetDefaultPaneGridAttributes(FormatMock.Expects expects) {
//        expects.getBackgroundComponent().returns(null).any();
//        expects.getBackgroundComponentType().returns(null).any();
//        expects.getBackgroundColour().returns(BACKGROUND_COLOR).any();
//        expects.getBorderWidth().returns(BORDER_WIDTH_STRING).any();
//        expects.getCellPadding().returns(CELL_PADDING).any();
//        expects.getCellSpacing().returns(CELL_SPACING).any();
//        expects.getHeight().returns(HEIGHT).any();
//        expects.getWidth().returns(WIDTH).any();
//        expects.getWidthUnits().returns(WIDTH_UNITS);
    }

    /**
     * Prepare the mock for having default pane and grid attributes set on it.
     *
     * @param expects The mock's expects instance.
     */
    protected void expectSetPaneGridAttributes(
            FormatAttributesMock.Expects expects,
            Format formatMock) {

//        expects.setBackgroundColour(BACKGROUND_COLOR).atLeast(1);
//        expects.setBackgroundImage(null).atLeast(1);
//        expects.setBorderWidth(BORDER_WIDTH_STRING).atLeast(1);
//        expects.setCellPadding(CELL_PADDING).atLeast(1);
//        expects.setCellSpacing(CELL_SPACING).atLeast(1);
//        expects.setHeight(HEIGHT).atLeast(1);
//        expects.setWidth(WIDTH).atLeast(1);
//        expects.setWidthUnits(WIDTH_UNITS).atLeast(1);
        expects.setFormat(formatMock).atLeast(1);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 06-Jan-05	6391/2	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 ===========================================================================
*/
