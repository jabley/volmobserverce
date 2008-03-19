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
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.layouts.TemporalFormatIteratorMock;
import com.volantis.mcs.layouts.iterators.IteratorSizeConstraintMock;
import com.volantis.mcs.protocols.AttributesTestHelper;
import com.volantis.mcs.protocols.SlideAttributesMock;
import com.volantis.mcs.protocols.layouts.TemporalFormatIteratorInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.shared.layouts.TemporalFormatIteratorRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.expectations.UnorderedExpectations;

/**
 * Tests for temporal format iterator renderers.
 */
public class TemporalFormatIteratorRendererTestCase
        extends AbstractFormatIteratorRendererTestAbstract {

    private static final String SINGLE_CLOCK_VALUE = "1";
    private static final String MULTIPLE_CLOCK_VALUES = "1,2";
    private static final String CLOCK_VALUE1 = "1";
    private static final String CLOCK_VALUE2 = "2";
    private TemporalFormatIteratorInstanceMock temporalInstanceMock;
    private TemporalFormatIteratorMock temporalMock;
    private SlideAttributesMock slideAttributesMock;
    protected IteratorSizeConstraintMock constraintMock;
    private static final int INSTANCE_COUNT = 50;

    // Javadoc inherited from superclass
    public void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        temporalInstanceMock = new TemporalFormatIteratorInstanceMock(
                        "temporalInstanceMock", expectations, indexMock);

        temporalMock = LayoutTestHelper.createTemporalFormatIteratorMock(
                        "temporalMock", expectations, canvasLayoutMock);

        LayoutTestHelper.addChildren(temporalMock.expects,
                                     new FormatMock.Expects[] {
            childMock.expects
        });

        slideAttributesMock = (SlideAttributesMock)
                AttributesTestHelper.createMockAttributes(
                        SlideAttributesMock.class, "par",
                        "slideAttributesMock", expectations);

        constraintMock = new IteratorSizeConstraintMock(
                "constraintMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the instance.
        temporalInstanceMock.expects.getFormat().returns(temporalMock).any();
        temporalInstanceMock.expects.getIndex().returns(indexMock).any();

        // Initialise the mock.
        temporalMock.expects.getMaxCellConstraint()
                .returns(constraintMock).any();

        // Initialise the index.
        indexMock.expects.addDimension().returns(childMockIndeces[0]);

        // Initialise the factory.
        layoutAttributesFactoryMock.expects.createSlideAttributes()
                .returns(slideAttributesMock);
    }

    /**
     *  Tests the render method for a temporal format iterator which contains
     *  a pane where the number of cells is fixe
     */
    public void testRenderTemporalFormatIteratorFixed() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the instance.
        temporalInstanceMock.expects.isEmpty().returns(false).any();

        final String[] clockValues = new String[] {
            CLOCK_VALUE1,
            CLOCK_VALUE2,
        };

        // Initialise the format.
        temporalMock.expects.getAttribute(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES)
                .returns(MULTIPLE_CLOCK_VALUES)
                .atLeast(1);

        // Initialise the constraint.
        final int instanceCount = 1;
        initialiseFixedConstraint(clockValues.length, instanceCount);

        // Initialise the instance counter.
        instanceCounterMock.expects.getMaxInstances(temporalMock, indexMock)
                .returns(instanceCount).any();

        addWriteExpectations(clockValues);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatRenderer renderer = new TemporalFormatIteratorRenderer(
                layoutAttributesFactoryMock);
        renderer.render(formatRendererContextMock, temporalInstanceMock);
    }

    /**
     * Tests the render method for time values.
     *
     * <p>If there are more cells than timeValues then the durations for the
     * extra cells should be the last time value existing.</p>
     */
    public void testRenderTemporalFormatIteratorFixedTimeValues()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the instance.
        temporalInstanceMock.expects.isEmpty().returns(false).any();

        final String[] clockValues = new String[] {
            CLOCK_VALUE1,
            CLOCK_VALUE2,
            CLOCK_VALUE2,
        };

        // Initialise the format.
        temporalMock.expects.getAttribute(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES)
                .returns(MULTIPLE_CLOCK_VALUES)
                .atLeast(1);

        // Initialise the constraint.
        initialiseFixedConstraint(clockValues.length, INSTANCE_COUNT);

        // Initialise the instance counter.
        instanceCounterMock.expects.getMaxInstances(temporalMock, indexMock)
                .returns(INSTANCE_COUNT).any();

        addWriteExpectations(clockValues);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatRenderer renderer = new TemporalFormatIteratorRenderer(
                layoutAttributesFactoryMock);
        renderer.render(formatRendererContextMock, temporalInstanceMock);
    }

    private void initialiseFixedConstraint(
            final int limit, final int instanceCount) {
        constraintMock.expects.isFixed()
                .returns(true).any();
        constraintMock.expects.getMaximumValue()
                .returns(limit).any();
        constraintMock.expects.getConstrained(instanceCount)
                .returns(Math.min(limit, instanceCount)).any();
    }

    private void addWriteExpectations(final String[] clockValues) {
        // Make sure that the layout module is invoked properly.
        expectations.add(new OrderedExpectations() {
            public void add() {

                for (int i = 0; i < clockValues.length; i += 1) {
                    final int index = i;

                    add(new UnorderedExpectations() {
                        public void add() {

                            formatRendererContextMock.expects
                                    .setCurrentFormatIndex(childMockIndeces[index]);

                            // Make sure that the attributes are set up correctly.
                            slideAttributesMock.expects.setDuration(clockValues[index]);
                        }
                    });

                    layoutModuleMock.expects
                            .writeOpenSlide(slideAttributesMock);

                    formatRendererContextMock.expects
                            .renderFormat(childInstanceMock);

                    layoutModuleMock.expects
                            .writeCloseSlide(slideAttributesMock);
                }

                formatRendererContextMock.expects
                        .setCurrentFormatIndex(indexMock);
            }
        });
    }

    /**
     * Tests the render method for a temporal format iterator.
     *
     * <p>Having specified a maximum of 4 cells we should have panes at
     * positions 0, 1 and 2.</p>
     */
    public void testRenderTemporalFormatIteratorVariable1() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        addVariableTemporalExpectations(4, 3);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatRenderer renderer = new TemporalFormatIteratorRenderer(
                layoutAttributesFactoryMock);
        renderer.render(formatRendererContextMock, temporalInstanceMock);
    }

    private void addVariableTemporalExpectations(
            int maxCellCount, int actualCellCount) {

        if (maxCellCount != 0 && actualCellCount > maxCellCount) {
            actualCellCount = maxCellCount;
        }

        // Initialise the instance.
        temporalInstanceMock.expects.isEmpty().returns(false).any();

        // Initialise the instance counter.
        instanceCounterMock.expects.getMaxInstances(temporalMock, indexMock)
                .returns(actualCellCount).any();

        final String[] clockValues = new String[actualCellCount];
        for (int i = 0; i < clockValues.length; i++) {
            clockValues[i] = CLOCK_VALUE1;
        }

        // Initialise the format.
        temporalMock.expects.getAttribute(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES)
                .returns(SINGLE_CLOCK_VALUE)
                .atLeast(1);

        // Initialise the constraint.
        constraintMock.expects.isFixed()
                .returns(false).any();
        constraintMock.expects.getMaximumValue()
                .returns(maxCellCount).any();
        if (maxCellCount != 0 && actualCellCount > maxCellCount) {
            constraintMock.expects.getConstrained(actualCellCount)
                    .returns(maxCellCount).any();
        } else {
            constraintMock.expects.getConstrained(actualCellCount)
                    .returns(actualCellCount).any();
        }

        addWriteExpectations(clockValues);
    }


    /**
     * Tests the render method for a temporal format iterator.
     *
     * <p>Having specified a maximum of 2 cells we should have panes at
     * positions 0 and 1.</p>
     */
    public void testRenderTemporalFormatIteratorVariable2() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        addVariableTemporalExpectations(2, 3);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatRenderer renderer = new TemporalFormatIteratorRenderer(
                layoutAttributesFactoryMock);
        renderer.render(formatRendererContextMock, temporalInstanceMock);
    }

    /**
     *  Tests the render method for a temporal format iterator
     *
     * <p>Having specified no maximum we should have panes at
     * positions 0 and 1.</p>
     */
    public void testRenderTemporalFormatIteratorVariable3() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        addVariableTemporalExpectations(0, 2);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatRenderer renderer = new TemporalFormatIteratorRenderer(
                layoutAttributesFactoryMock);
        renderer.render(formatRendererContextMock, temporalInstanceMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8862/3	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/7	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
