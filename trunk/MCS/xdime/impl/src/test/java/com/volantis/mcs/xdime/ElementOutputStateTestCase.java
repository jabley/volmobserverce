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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime;

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DeviceLayoutContextMock;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.OutputBufferMock;
import com.volantis.mcs.protocols.RegionContent;
import com.volantis.mcs.protocols.layouts.ContainerInstanceMock;
import com.volantis.mcs.protocols.layouts.RegionInstanceMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Verifies that {@link ElementOutputStateImpl} behaves as expected.
 */
public class ElementOutputStateTestCase extends TestCaseAbstract {

    private XDIMEContextInternalMock context;
    private MarinerRequestContextMock requestContext;
    private MarinerPageContextMock pageContext;
    private ContainerInstanceMock containerInstance;
    private DeviceLayoutContextMock layoutContext;
    private RegionInstanceMock regionInstance;

    private static final String ID = "ID";
    private OutputBuffer FRAG_LINK_LABEL;
    private OutputBuffer ENCLOSING_FRAG_LINK_LABEL;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        context = new XDIMEContextInternalMock("context", expectations);
        requestContext =
                new MarinerRequestContextMock("requestContext", expectations);
        pageContext = new MarinerPageContextMock("pageContext", expectations);
        containerInstance = new ContainerInstanceMock("containerInstance",
                expectations);
        layoutContext = new DeviceLayoutContextMock("layoutContext",
                expectations);
        regionInstance = new RegionInstanceMock("regionInstance", expectations,
                NDimensionalIndex.ZERO_DIMENSIONS);

        FRAG_LINK_LABEL = new OutputBufferMock("FRAG_LINK_LABEL", expectations);
        ENCLOSING_FRAG_LINK_LABEL = new OutputBufferMock(
                "ENCLOSING_FRAG_LINK_LABEL", expectations);
    }

    /**
     * Verify that when calling apply on a state which specifies both layout and
     * container, they are both pushed on the stacks in the page context.
     *
     * @throws XDIMEException  if there was a problem running the test
     */
    public void testApplyWithSpecifiedContainerAndLayout()
            throws XDIMEException, LayoutException {

        // Set expectations.
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.pushContainerInstance(containerInstance);
        pageContext.expects.pushDeviceLayoutContext(layoutContext);
        pageContext.expects.initialiseDeviceLayoutContext(regionInstance);
        pageContext.expects.initialiseCurrentFragment();

        // Run test.
        ElementOutputStateImpl state = new ElementOutputStateImpl(
                containerInstance, layoutContext, regionInstance,
                FormattingResult.PROCESS, false, ID, context, null, false);
        state.apply();
    }

    /**
     * Verify that calling apply on a state which doesn't specify layout,
     * container or containing instance doesn't modify the page context.
     *
     * @throws XDIMEException  if there was a problem running the test
     */
    public void testApplyWithNoSpecifiedContainerLayoutOrRegion()
            throws XDIMEException {

        // Set expectations.
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);

        // Run test.
        ElementOutputStateImpl state = new ElementOutputStateImpl(null, null,
                null, FormattingResult.PROCESS, false, ID, context, null, false);
        state.apply();
    }

//    /**
//     * Verify that calling apply on a state which doesn't specify layout,
//     * container or containing instance, but does specify a container name
//     * attempts to update a fragment instance corresponding to that name.
//     *
//     * @throws XDIMEException  if there was a problem running the test
//     */
//    public void testApplyWithOnlySpecifiedContainerName()
//            throws XDIMEException {
//
//        // Create test objects.
//        MapMock metaDataMap = new MapMock("mapMock", expectations);
//        MapMock propertyToMetaData =
//                new MapMock("propertyToMetatData", expectations);
//        final String containerName = "containerName";
//        ListMock fragments = new ListMock("listMock", expectations);
//        IteratorMock fragmentIterator =
//                new IteratorMock("fragmentIterator", expectations);
//        final CanvasLayoutMock layout = new CanvasLayoutMock(
//                "layout", expectations);
//        final FragmentMock fragment = LayoutTestHelper.createFragmentMock(
//                "fragmentMock", expectations, layout);
//        LayoutContentActivator.ContainerPosition pos =
//                new LayoutContentActivator.ContainerPosition(fragment, 0);
//        FragmentInstanceMock fragmentInstance = new FragmentInstanceMock(
//                "fragmentInstance", expectations,
//                NDimensionalIndex.ZERO_DIMENSIONS);
//        RuntimeDeviceLayoutMock deviceLayout =
//                new RuntimeDeviceLayoutMock("layout", expectations);
//
//        // Set expectations.
//        context.expects.getInitialRequestContext().returns(requestContext);
//        requestContext.expects.getMarinerPageContext().returns(pageContext);
//        pageContext.expects.getCurrentFragment().returns(fragment);
//        pageContext.expects.getMetaDataMap().returns(metaDataMap);
//        metaDataMap.expects.get(ID).returns(propertyToMetaData);
//        propertyToMetaData.expects.get(
//                MetaPropertyHandlerFactory.FRAGMENT_LINK_LABEL).
//                returns(FRAG_LINK_LABEL);
//        propertyToMetaData.expects.get(
//                MetaPropertyHandlerFactory.ENCLOSING_FRAGMENT_LINK_LABEL).
//                returns(ENCLOSING_FRAG_LINK_LABEL);
//        pageContext.expects.getDeviceLayoutContext().returns(layoutContext);
//        layoutContext.expects.getDeviceLayout().returns(deviceLayout);
//        deviceLayout.expects.getEnclosingFragments(containerName).returns(fragments);
//        fragments.expects.iterator().returns(fragmentIterator);
//        fragmentIterator.expects.next().returns(pos);
//        fragmentIterator.expects.hasNext().returns(false);
//        fragment.expects.getDimensions().returns(0);
//        pageContext.expects.getFormatInstance(fragment,
//                NDimensionalIndex.ZERO_DIMENSIONS).returns(fragmentInstance);
//        fragmentInstance.expects.setLinkToBuffer(FRAG_LINK_LABEL,0)
//                .returns(true);
//        fragmentInstance.expects.setLinkFromBuffer(ENCLOSING_FRAG_LINK_LABEL,0)
//                .returns(true);
//
//        // Run test.
//        ElementOutputStateImpl state = new ElementOutputStateImpl(null, null,
//                null, FormattingResult.PROCESS, false, ID, context, containerName);
//        state.apply();
//    }

    /**
     * Verify that when calling apply on a state which specifies only the
     * layout context, it is pushed on the stack in the page context.
     *
     * @throws XDIMEException  if there was a problem running the test
     */
    public void testApplyWithOnlyLayoutSpecified()
            throws XDIMEException, LayoutException {

        // Set expectations.
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.pushDeviceLayoutContext(layoutContext);
        pageContext.expects.initialiseDeviceLayoutContext(regionInstance);
        pageContext.expects.initialiseCurrentFragment();

        // Run test.
        ElementOutputStateImpl state = new ElementOutputStateImpl(
                null, layoutContext, regionInstance,
                FormattingResult.PROCESS, false, ID, context, null, false);
        state.apply();
    }

    /**
     * Verify that when calling apply on a state which specifies only the
     * container, it is pushed on the stack in the page context.
     *
     * @throws XDIMEException  if there was a problem running the test
     */
    public void testApplyWithOnlyContainerSpecified()
            throws XDIMEException, LayoutException {

        // Set expectations.
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.pushContainerInstance(containerInstance);

        // Run test.
        ElementOutputStateImpl state = new ElementOutputStateImpl(
                containerInstance, null, null,
                FormattingResult.PROCESS, false, ID, context, null, false);
        state.apply();
    }

    /**
     * Verify that calling revert on a state which doesn't specify layout,
     * container or containing instance doesn't modify the page context.
     */
    public void testRevertWhenNoSpecifiedContainerLayoutOrRegion() {

        // Set expectations.
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);

        // Run test.
        ElementOutputStateImpl state = new ElementOutputStateImpl(null, null,
                null, FormattingResult.PROCESS, false, ID, context, null, false);
        state.revert();
    }

    /**
     * Verify that when calling revert on a state which specifies both layout
     * and container, they are both popped off the stacks in the page context.
     */
    public void testRevertWithSpecifiedContainerAndLayout() {

        // Set expectations.
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.popContainerInstance(containerInstance);
        pageContext.expects.popDeviceLayoutContext().returns(layoutContext);
        regionInstance.fuzzy.addRegionContent(
                mockFactory.expectsInstanceOf(RegionContent.class));

        // Run test.
        ElementOutputStateImpl state = new ElementOutputStateImpl(
                containerInstance, layoutContext, regionInstance,
                FormattingResult.PROCESS, false, ID, context, null, false);
        state.revert();
    }

    /**
     * Verify that calling revert on a state which doesn't specify layout,
     * container or containing instance, but does specify a container name,
     * doesn't modify the page context.
     */
    public void testRevertWithOnlySpecifiedContainerName() {

        // Set expectations.
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);

        // Create test objects.
        final String containerName = "containerName";

        // Run test.
        ElementOutputStateImpl state = new ElementOutputStateImpl(null, null,
                null, FormattingResult.PROCESS, false, ID, context,
                containerName, false);
        state.revert();
    }

    /**
     * Verify that when calling revert on a state which specifies only the
     * container, it is popped off the stack in the page context.
     *
     * @throws XDIMEException  if there was a problem running the test
     */
    public void testRevertWithOnlyContainerSpecified()
            throws XDIMEException, LayoutException {

        // Set expectations.
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.popContainerInstance(containerInstance);

        // Run test.
        ElementOutputStateImpl state = new ElementOutputStateImpl(
                containerInstance, null, null,
                FormattingResult.PROCESS, false, ID, context, null, false);
        state.revert();
    }

    /**
     * Verify that when calling revert on a state which specifies only the
     * layout context, it is popped off the stack in the page context.
     */
    public void testRevertWithOnlyLayoutSpecified() {

        // Set expectations.
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.popDeviceLayoutContext().returns(layoutContext);
        regionInstance.fuzzy.addRegionContent(
                mockFactory.expectsInstanceOf(RegionContent.class));

        // Run test.
        ElementOutputStateImpl state = new ElementOutputStateImpl(
                null, layoutContext, regionInstance,
                FormattingResult.PROCESS, false, ID, context, null, false);
        state.revert();
    }

}
