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
import com.volantis.mcs.layouts.CanvasLayoutMock;
import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DeviceLayoutContextMock;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.layouts.ContainerInstanceImplMock;
import com.volantis.mcs.runtime.layouts.StyleFormatReference;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.MCSContainerKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Verifies that {@link ElementOutputStateBuilderImpl} behaves as expected.
 * @todo later add more extensive test cases!
 */
public class ElementOutputStateBuilderTestCase extends TestCaseAbstract {

//    private ElementOutputStateBuilderImpl builder;
    private XDIMEContextInternalMock context;
    private MarinerRequestContextMock requestContext;
    private MarinerPageContextMock pageContext;
    ElementOutputStateMock parentOutputState;
    XDIMEElementInternalMock parent;
    private ContainerInstanceImplMock containerInstance;
    private DeviceLayoutContextMock layoutContext;
    private FormatMock format;

    private static final FormatReference FORMAT_REF =
            new FormatReference("stem", NDimensionalIndex.ZERO_DIMENSIONS);
    private static final StyleFormatReference STYLE_FORMAT_REF =
            new StyleFormatReference(FORMAT_REF);
    private static final StyleValueFactory STYLE_VALUE_FACTORY = StyleValueFactory.getDefaultInstance();

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        context = new XDIMEContextInternalMock("context", expectations);
        requestContext =
                new MarinerRequestContextMock("requestContext", expectations);
        pageContext = new MarinerPageContextMock("pageContext", expectations);
        parentOutputState = new ElementOutputStateMock("parent", expectations);
        parent = new XDIMEElementInternalMock("parent", expectations);
        containerInstance = new ContainerInstanceImplMock("containerInstance",
                expectations, NDimensionalIndex.ZERO_DIMENSIONS);
        layoutContext = new DeviceLayoutContextMock("layoutContext",
                expectations);
        final CanvasLayoutMock layout = new CanvasLayoutMock(
                "layout", expectations);
        format = LayoutTestHelper.createFormatMock(
                "format", expectations, layout);
    }

    /**
     * Verify that if the current container is specified and the parent is
     * null, the FormattingResult is PROCESS if the layout context is not null,
     * and process if it is.
     */
    public void testDetermineFormattingIfCurrentSpecifiedAndNullParent() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, null, null, null);

        // Test with null container and layout.
        doTest(true, false, null, null, FormattingResult.PROCESS, builder);

        // Test with null container and valid layout.
        doTest(true, false, null, containerInstance, FormattingResult.PROCESS, builder);

        // Test with valid container and layout.
        doTest(true, false, layoutContext, containerInstance, FormattingResult.SUPPRESS, builder);

        // Test with valid container and null layout.
        doTest(true, false, layoutContext, null, FormattingResult.SUPPRESS, builder);
    }

    /**
     * Verify that if the current container is specified and the parent is
     * suppressing, the FormattingResult is always SUPPRESS.
     */
    public void testDetermineFormattingIfCurrentSpecifiedAndSuppressingParent() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, parentOutputState, null, null);

        parentOutputState.expects.isSuppressing().returns(true).fixed(2);

        // Test with null container and layout.
        doTest(true, false, null, null, FormattingResult.SUPPRESS, builder);

        // Test with null container and valid layout.
        doTest(true, false, null, containerInstance, FormattingResult.SUPPRESS, builder);

        // Test with valid container and layout.
        doTest(true, false, layoutContext, containerInstance, FormattingResult.SUPPRESS, builder);

        // Test with valid container and null layout.
        doTest(true, false, layoutContext, null, FormattingResult.SUPPRESS, builder);
    }

    /**
     * Verify that if the current container is specified and the parent is
     * not suppressing, the FormattingResult is PROCESS if the layout context
     * is null and SUPPRESS if it isn't.
     */
    public void testDetermineFormattingIfCurrentSpecifiedAndNotSuppressingParent() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, parentOutputState, null, null);

        parentOutputState.expects.isSuppressing().returns(false).fixed(2);

        // Test with null container and layout.
        doTest(true, false, null, null, FormattingResult.PROCESS, builder);

        // Test with null container and valid layout.
        doTest(true, false, null, containerInstance, FormattingResult.PROCESS, builder);

        // Test with valid container and layout.
        doTest(true, false, layoutContext, containerInstance, FormattingResult.SUPPRESS, builder);

        // Test with valid container and null layout.
        doTest(true, false, layoutContext, null, FormattingResult.SUPPRESS, builder);
    }

    /**
     * Verify that if the current container is not targetted, and the container
     * instance is null, then the FormattingResult is:
     * <ul>
     * <li>SKIP if the layout context is null</li>
     * <li>SUPPRESS if it's non null</li>
     * </ul>
     */
    public void testDetermineFormattingIfNotCurrentAndNullContainerInstance() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, parentOutputState, null, null);

        // Test with null container and layout.
        doTest(false, false, null, null, FormattingResult.SKIP, builder);

        // Test with valid container and null layout.
        doTest(false, false, layoutContext, null, FormattingResult.SKIP, builder);
    }

    private void doTest(
            boolean current,
            boolean isInactiveGroup,
            DeviceLayoutContextMock layoutContext,
            ContainerInstanceImplMock containerInstance,
            FormattingResult expectedResult,
            ElementOutputStateBuilderImpl builder) {

        builder.specifiedCurrentContainer = current;
        builder.isInactiveGroup = isInactiveGroup;
        FormattingResult result = builder.determineFormattingResult(
                layoutContext, containerInstance, false);
        assertEquals(expectedResult, result);
    }
    /**
     * Verify that if the current container is not targetted, and the container
     * instance is not null or ignorable, and it is not in a suppressed layout,
     * then the FormattingResult is:
     * <ul>
     * <li>SUPPRESS if it's in an inactive group</li>
     * <li>and PROCESS otherwise.</li>
     * </ul>
     */
    public void testDetermineFormattingIfNotCurrentAndNotIgnorableContainer() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, parentOutputState, null, null);

        containerInstance.expects.ignore().returns(false).fixed(4);
        parentOutputState.expects.isInSuppressedLayout().returns(false).fixed(2);

        assertFalse(builder.specifiedCurrentContainer);
        assertFalse(builder.isInactiveGroup);

        doTest(false, false, null, containerInstance, FormattingResult.PROCESS, builder);

        doTest(false, false, layoutContext, containerInstance,
                FormattingResult.PROCESS, builder);

        doTest(false, true, null, containerInstance, FormattingResult.SUPPRESS, builder);

        doTest(false, true, layoutContext, containerInstance,
                FormattingResult.SUPPRESS, builder);
    }

    /**
     * Verify that if the current container is not targetted, and the container
     * instance should be ignored then the FormattingResult is SKIP.
     */
    public void testDetermineFormattingIfNotCurrentAndIgnorableContainer() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(context, null, null, null);

        builder.specifiedCurrentContainer = false;

        containerInstance.expects.ignore().returns(true);
        doTest(false, false, null, containerInstance, FormattingResult.SKIP, builder);

        containerInstance.expects.ignore().returns(true);
        doTest(false, false, layoutContext, containerInstance, FormattingResult.SKIP, builder);
    }

    /**
     * Verify that you can create an {@link ElementOutputState} when only the
     * context is initialised, and that it is not suppressing or in an inactive
     * group.
     */
    public void testCreateElementOutputStateWithContextOnly() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, parentOutputState, null, null);

        // Set expectations.
        // From ElementOutputStateImpl#createElementOutputState
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        parentOutputState.expects.isInactiveGroup().returns(false);
        parentOutputState.expects.isSuppressing().returns(false).fixed(2);
        parentOutputState.expects.isSuppressingDescendants().returns(false);
        parentOutputState.expects.isInSuppressedLayout().returns(false);

        ElementOutputState state = builder.createElementOutputState();

        assertNotNull(state);
        assertFalse(state.isInactiveGroup());
        assertFalse(state.isSuppressing());
    }

    /**
     * Make sure that if output state is calculated for non-atomic element, and
     * container is null, the element is going to be skipped.
     */
    public void testCreateElementOutputStateWithNonAtomicElement() {

        final boolean isElementAtomic = false;

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, parentOutputState, null, null, false, isElementAtomic);

        parentOutputState.expects.isSuppressing().returns(false);

        // Test with null container and layout.
        doTest(true, false, null, null, FormattingResult.PROCESS, builder);
    }

    /**
     * Make sure that if output state is calculated for atomic element, and
     * container is null, the element is going to be suppressed (along with its children), not skipped.
     */
    public void testCreateElementOutputStateWithAtomicElement() {

        final boolean isElementAtomic = true;

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, parentOutputState, null, null, false, isElementAtomic);

        parentOutputState.expects.isSuppressing().returns(true);

        // Test with null container and layout.
        doTest(true, false, null, null, FormattingResult.SUPPRESS, builder);
    }

    /**
     * Verify that you can create an {@link ElementOutputState} when only the
     * context is initialised, and that it is suppressing if the parent is.
     */
    public void testCreateElementOutputStateWithContextOnlyAndSuppressingParent() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, parentOutputState, null, null);

        // Set expectations.
        // From ElementOutputStateImpl#createElementOutputState
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        parentOutputState.expects.isInactiveGroup().returns(false);
        parentOutputState.expects.isSuppressing().returns(true).fixed(2);
        parentOutputState.expects.isSuppressingDescendants().returns(false);
        parentOutputState.expects.isInSuppressedLayout().returns(false);

        ElementOutputState state = builder.createElementOutputState();

        assertNotNull(state);
        assertFalse(state.isInactiveGroup());
        assertTrue(state.isSuppressing());
    }

    /**
     * Verify that you can create an {@link ElementOutputState} when only the
     * context is initialised, and that it is suppressing if the parent is in
     * an inactive group.
     */
    public void testCreateElementOutputStateWithContextOnlyAndParentInInactiveGroup() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, parentOutputState, null, null);

        // Set expectations.
        // From ElementOutputStateImpl#createElementOutputState
        context.expects.getInitialRequestContext().returns(requestContext);
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        parentOutputState.expects.isInactiveGroup().returns(true);
        parentOutputState.expects.isSuppressing().returns(false).fixed(2);
        parentOutputState.expects.isSuppressingDescendants().returns(false);
        parentOutputState.expects.isInSuppressedLayout().returns(false);

        ElementOutputState state = builder.createElementOutputState();

        assertNotNull(state);
        assertTrue(state.isInactiveGroup());
        assertTrue(state.isSuppressing());
    }

    public void testGetSpecifiedContainerWithFormatRef() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, null, null, null);

        pageContext.expects.getFormat(FORMAT_REF.getStem(),
                FormatNamespace.CONTAINER).returns(format);
        pageContext.expects.getFormatInstance(format, FORMAT_REF.getIndex()).
                returns(containerInstance);

        assertEquals(containerInstance,
                builder.getSpecifiedContainer(STYLE_FORMAT_REF, pageContext, layoutContext));
    }

//    public void testGetSpecifiedContainerWithStyleString() {
//        layoutContext.expects.getFormat(STYLE_STRING.getString(),
//                FormatNamespace.CONTAINER).returns(format);
//        format.expects.getDimensions().returns(0);
//        layoutContext.expects.getFormatInstance(format,
//                NDimensionalIndex.ZERO_DIMENSIONS).returns(containerInstance);
//
//        assertEquals(containerInstance,
//                builder.getSpecifiedContainer(STYLE_STRING, pageContext, layoutContext));
//    }

    public void testGetSpecifiedContainerWithCurrent() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, null, null, null);

        assertFalse(builder.specifiedCurrentContainer);
        assertNull(builder.getSpecifiedContainer(MCSContainerKeywords.CURRENT,
                pageContext, layoutContext));
        assertTrue(builder.specifiedCurrentContainer);
    }

    public void testGetSpecifiedContainerWithNotCurrentStyleKeyword() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, null, null, null);

        assertFalse(builder.specifiedCurrentContainer);
        try {
            builder.getSpecifiedContainer(StyleKeywords.RANDOM,
                    pageContext, layoutContext);
            fail("Should not be able to retrieve a container with " +
                    "invalid style keyword");
        } catch(IllegalStateException e) {
            // do nothing correct behaviour.
        }
    }

    public void testGetSpecifiedContainerWithOtherStyleValue() {

        ElementOutputStateBuilderImpl builder =
                new ElementOutputStateBuilderImpl(
                        context, null, null, null);

        assertFalse(builder.specifiedCurrentContainer);
        try {
            builder.getSpecifiedContainer(
                STYLE_VALUE_FACTORY.getLength(null, 10, LengthUnit.CM),
                    pageContext, layoutContext);
            fail("Should not be able to retrieve a container with " +
                    "invalid type of StyleValue");
        } catch(IllegalStateException e) {
            // do nothing correct behaviour.
        }
    }

    /**
     * Verify the failure case:
     * <ul>
     * <li>element specifies a new layout</li>
     * <li>a subsequent element specifies another new layout and valid
     * container in that layout</li>
     * </ul>
     * This should fail because no container was specified in the outer layout,
     * so there is therefore nowhere to put the valid inner container.
     */
//    public void testDetermineFormattingSuppresses() {
//        context.expects.getEnclosingElement().returns(parent);
//        parent.expects.getOutputState().returns(parentOutputState);
//        parentOutputState.expects.temporarilySuppressing().returns(true);
//
//        builder.specifiedCurrentContainer = false;
//        builder.isInactiveGroup = false;
//        FormattingResult result = builder.determineFormattingResult(
//                layoutContext, containerInstance);
//        assertEquals(FormattingResult.SUPPRESS, result);
//    }
}
