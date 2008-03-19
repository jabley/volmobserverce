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
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-2002  Sumit           VBM:2002111105 - Tests render methods for
 *                              spatial and temporal iterators and panes
 * 25-Nov-02    Payal           VBM:2002111804 - Modified
 *                              testRenderSpatialFormatIteratorAcrossDown(),
 *                              testRenderSpatialFormatIteratorDownAcross()
 *                              hardcoded the SpatialFormatIterator choices.
 * 13-Jan-03    Chris W         VBM:2003011311 - Added test methods to check
 *                              whether FormatIterators with variable numbers
 *                              of rows / columns / cells render correctly.
 * 28-Jan-03    Geoff           VBM:2003012802 - Add testcase specifically for
 *                              the bug in this VBM, change tabs to spaces.
 * 04-Deb-03    Mat             VBM:2003012807 - Added
 *                              testRenderTemporalFormatIteratorFixedTimeValues()
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses the
 *                              new shared Test... versions of classes rather
 *                              their own "cut & paste" inner classes.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Update VolantisProtocol
 *                              specialization.
 * 05-Mar-03    Sumit           VBM:2003022605 - Updated to test spatials using
 *                              new methods in VolantisProtocol.
 * 14-Mar-03    Doug            VBM:2003030409 - Added the testFragLinkOrdering
 *                              fixture.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException
 *                              declarations where necessary.
 * 30-May-03    Mat             VBM:2003042911 - Change writeCanvasContent()
 *                              & writeMontageContent() to accept a
 *                              PackageBodyOutput instead of a Writer
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.layouts.FragmentMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.layouts.FragmentInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.SegmentLinkWriterMock;
import com.volantis.mcs.protocols.renderer.shared.layouts.unit.AbstractFormatRendererTestAbstract;
import com.volantis.mcs.protocols.renderer.shared.layouts.unit.FormatRendererTestHelper;
import com.volantis.mcs.runtime.styling.CompiledStyleSheetCollection;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.styling.sheet.CompiledStyleSheetMock;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

import java.io.IOException;

/**
 * Test teh device layout renderer.
 */
public class DeviceLayoutRendererTestCase
        extends AbstractFormatRendererTestAbstract {

    private DeviceLayoutContextMock deviceLayoutContextMock;
    private LayoutAttributesMock layoutAttributesMock;
    protected SegmentLinkWriterMock segmentLinkWriterMock;

    private DeviceLayoutRenderer renderer;

    // Javadoc inherited from superclass
    public void setUp() throws Exception {
        super.setUp();

        expectations = mockFactory.createUnorderedBuilder();

        deviceLayoutContextMock = new DeviceLayoutContextMock(
                "deviceLayoutContext", expectations);

        layoutAttributesMock = (LayoutAttributesMock)
                AttributesTestHelper.createMockAttributes(
                        LayoutAttributesMock.class, "layout",
                        "layoutAttributesMock", expectations);

        segmentLinkWriterMock =
                new SegmentLinkWriterMock("segmentLinkWriterMock",
                                          expectations);

        // Connect the mocks up.
//        deviceLayoutContextMock.expects.getFormatRendererContext()
//                .returns(formatRendererContextMock).any();

        layoutAttributesFactoryMock.expects.createLayoutAttributes()
                .returns(layoutAttributesMock);

        formatRendererContextMock.expects.getSegmentLinkWriter()
                .returns(segmentLinkWriterMock).any();

        // Make sure that the attributes are initialised properly.
        layoutAttributesMock.expects
                .setDeviceLayoutContext(deviceLayoutContextMock)
                .atLeast(1);

        // Initialise the styling mocks.
        setupStyling();

        renderer = new DeviceLayoutRenderer(layoutAttributesFactoryMock);
    }

    private void setupStyling() {

        final CompiledStyleSheetMock layoutCompiledStyleSheetMock =
                new CompiledStyleSheetMock("layoutCompiledStyleSheetMock",
                                           expectations);

        final CompiledStyleSheetMock themeCompiledStyleSheetMock =
                new CompiledStyleSheetMock("themeCompiledStyleSheetMock",
                                           expectations);

        final CompiledStyleSheetCollection compiledStyleSheets =
                new CompiledStyleSheetCollection();

        compiledStyleSheets.addStyleSheet(themeCompiledStyleSheetMock);

        deviceLayoutContextMock.expects.getDeviceLayout()
                .returns(runtimeDeviceLayoutMock).any();
        runtimeDeviceLayoutMock.expects.getCompiledStyleSheet()
                .returns(layoutCompiledStyleSheetMock).any();

        deviceLayoutContextMock.expects.getThemeStyleSheets()
                .returns(compiledStyleSheets).any();

        // Commented this out as there seems to be some problem with the mock
        // framework.
//        expectations.add(new OrderedExpectations() {
//            public void add() {
                formatStylingEngineMock.expects.pushStyleSheet(
                        layoutCompiledStyleSheetMock);

                formatStylingEngineMock.expects
                        .pushStyleSheet(themeCompiledStyleSheetMock);

                formatStylingEngineMock.expects
                        .popStyleSheet(themeCompiledStyleSheetMock);

                formatStylingEngineMock.expects
                        .popStyleSheet(layoutCompiledStyleSheetMock);
//            }
//        });
    }

    /**
     * Tests the peer/parent link ordering of the
     * renderLayout(DeviceLayoutContext) method.
     * @exception IOException if an error occurs
     */
    public void testFragLinkOrderingParentFirst()
            throws Exception {

        doFragLinkOrderingTest(true);
    }

    private void doFragLinkOrderingTest(final boolean parentLinkFirst)
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final FragmentInstanceMock fragmentInstanceMock =
                new FragmentInstanceMock("fragmentInstanceMock", expectations,
                                         NDimensionalIndex.ZERO_DIMENSIONS);

        final FragmentMock enclosingMock = LayoutTestHelper.createFragmentMock(
                "enclosingMock", expectations, canvasLayoutMock);

        final FragmentMock fragmentMock = LayoutTestHelper.createFragmentMock(
                "fragmentMock", expectations, canvasLayoutMock);

        final FragmentMock peerMock = LayoutTestHelper.createFragmentMock(
                "peerMock", expectations, canvasLayoutMock);

        // Connect the fragments together.
        FormatMock.Expects[] children = new FormatMock.Expects[] {
            fragmentMock.expects,
            peerMock.expects
        };
        LayoutTestHelper.addChildren(enclosingMock.expects, children);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the format renderer context.
        formatRendererContextMock.expects.getCurrentFragment()
                .returns(fragmentMock)
                .any();

        // Initialise the layout module.
        layoutModuleMock.expects.getSupportsFragmentLinkListTargetting()
                .returns(true).any();

        // Select renderer for fragments.
//        formatRendererSelectorMock.expects.selectFormatRenderer(fragmentMock)
//                .returns(formatRendererMock)
//                .any();

        // Fragment wants peer links to be generated.
        fragmentMock.expects.getPeerLinks().returns(true).any();
        fragmentMock.expects.isParentLinkFirst().returns(parentLinkFirst).any();

        // Associate the fragment instance with the fragment.

        FormatRendererTestHelper.connectFormatInstanceToFormat(
                formatRendererContextMock,
                fragmentInstanceMock.expects, fragmentMock, NDimensionalIndex.ZERO_DIMENSIONS);

        expectations.add(new OrderedExpectations() {
            public void add() {

                formatRendererContextMock.expects
                        .pushDeviceLayoutContext(deviceLayoutContextMock)
                        .atLeast(1);

                deviceLayoutContextMock.expects
                        .getIncludingDeviceLayoutContext()
                        .returns(null).atLeast(1);

                layoutModuleMock.expects.writeOpenLayout(layoutAttributesMock);

                if (parentLinkFirst) {
                    fragmentLinkWriterMock.expects.writeFragmentLink(
                            formatRendererContextMock, fragmentMock,
                            enclosingMock, true, true);

                    fragmentLinkWriterMock.expects.writeFragmentLink(
                            formatRendererContextMock, fragmentMock, peerMock,
                            true, false);
                } else {
                    fragmentLinkWriterMock.expects.writeFragmentLink(
                            formatRendererContextMock, fragmentMock, peerMock,
                            true, false);

                    fragmentLinkWriterMock.expects.writeFragmentLink(
                            formatRendererContextMock, fragmentMock,
                            enclosingMock, true, true);
                }

                formatRendererContextMock.expects
                        .renderFormat(fragmentInstanceMock);

                segmentLinkWriterMock.expects.writeDefaultSegmentLink();

                layoutModuleMock.expects.writeCloseLayout(layoutAttributesMock);

                formatRendererContextMock.expects
                        .popDeviceLayoutContext()
                        .atLeast(1);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        renderer.renderLayout(deviceLayoutContextMock, formatRendererContextMock);
    }

    /**
     * Tests the peer/parent link ordering of the
     * renderLayout(DeviceLayoutContext) method.
     * @exception IOException if an error occurs
     */
    public void testFragLinkOrderingPeerFirst()
            throws Exception {

        doFragLinkOrderingTest(false);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 15-Nov-05	10278/3	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 15-Nov-05	10326/7	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 15-Nov-05	10278/3	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 11-Jul-05	8862/7	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 17-Feb-05	6957/3	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 06-Jan-05	6391/10	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/1	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 10-Dec-04	6391/4	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 10-Dec-04	6391/2	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 26-Oct-04	5977/1	tom	VBM:2004022303 Added linkStyleClass to fragments

 14-Oct-04	5808/3	byron	VBM:2004101317 Support style classes: Runtime DOMProtocol/DeviceLayoutRenderer

 02-Jul-04	4713/9	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 30-Jun-04	4781/3	adrianj	VBM:2002111405 Created SMS test case and added check for null/empty mime types in protocols

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 16-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 17-Nov-03	1888/1	mat	VBM:2003110512 Only check value of rows for an empty iterator

 17-Nov-03	1891/1	mat	VBM:2003110512 Still write the FIR to the context, even if iterator was skipped

 23-Sep-03	1412/6	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (rework to get link style from source fragment)

 23-Sep-03	1412/4	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (rework to get link style from source fragment)

 17-Sep-03	1412/2	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 14-Aug-03	1083/1	geoff	VBM:2003081305 Port 2003060909 forward

 14-Aug-03	1063/1	geoff	VBM:2003081305 Port 2003060909 forward

 01-Jul-03	677/1	doug	VBM:2003032706 Fixed problem if fragments

 05-Jun-03	285/3	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
