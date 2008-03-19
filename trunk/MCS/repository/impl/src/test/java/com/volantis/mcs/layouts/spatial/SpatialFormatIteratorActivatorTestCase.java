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

package com.volantis.mcs.layouts.spatial;

import com.volantis.mcs.layouts.FormatIteratorActivatorTestAbstract;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.SpatialFormatIteratorMock;
import com.volantis.mcs.layouts.iterators.IteratorSizeConstraintMock;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Tests for {@link SpatialFormatIteratorActivator).
 */
public class SpatialFormatIteratorActivatorTestCase
        extends FormatIteratorActivatorTestAbstract {

    private static final int MAX_COLUMN_COUNT = 4;
    private static final int MAX_ROW_COUNT = 5;
    private static final boolean ACROSS_DOWN = true;
    private static final boolean DOWN_ACROSS = false;
    private static final ExpectedValue ENDLESS_STRING_ARRAY_INSTANCE =
            mockFactory.expectsInstanceOf(EndlessStringArray.class);
    
    protected SpatialFormatIteratorMock spatialMock;
    protected CoordinateChooserFactoryMock coordinateChooserFactoryMock;
    protected CoordinateConverterChooserMock coordinateConverterChooserMock;
    protected IteratorSizeConstraintMock rowsConstraintMock;
    protected IteratorSizeConstraintMock columnsConstraintMock;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        spatialMock = LayoutTestHelper.createSpatialFormatIteratorMock(
                        "spatialMock", expectations,
                        canvasLayoutMock);

        coordinateChooserFactoryMock = new CoordinateChooserFactoryMock(
                "coordinateChooserFactoryMock", expectations);

        coordinateConverterChooserMock = new CoordinateConverterChooserMock(
                "coordinateConverterChooserMock", expectations);

        rowsConstraintMock = new IteratorSizeConstraintMock(
                "rowsConstraintMock", expectations);

        columnsConstraintMock = new IteratorSizeConstraintMock(
                "columnsConstraintMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================
    }

    /**
     * Test that parameters are created correctly when going across then down
     * with a fixed number of rows and columns.
     */
    public void testSetCoordinateConverterChooserAcrossDownFixedFixed()
            throws Exception {

        doTestActivation(FIXED, FIXED, ACROSS_DOWN);
    }

    /**
     * Test that parameters are created correctly when going down then across
     * with a fixed number of rows and columns.
     */
    public void testSetCoordinateConverterChooserDownAcrossFixedFixed()
            throws Exception {

        doTestActivation(FIXED, FIXED, DOWN_ACROSS);
    }

    /**
     * Test that parameters are created correctly when going across then down
     * with a variable number of rows and a fixed number of columns.
     */
    public void testSetCoordinateConverterChooserAcrossDownFixedVariable()
            throws Exception {

        doTestActivation(FIXED, VARIABLE, ACROSS_DOWN);
    }

    /**
     * Test that parameters are created correctly when going down then across
     * with a variable number of rows and a fixed number of columns.
     */
    public void testSetCoordinateConverterChooserDownAcrossFixedVariable()
            throws Exception {

        doTestActivation(FIXED, VARIABLE, DOWN_ACROSS);
    }

    /**
     * Test that parameters are created correctly when going across then down
     * with a fixed number of rows and a variable number of columns.
     */
    public void testSetCoordinateConverterChooserAcrossDownVariableFixed()
            throws Exception {

        doTestActivation(VARIABLE, FIXED, ACROSS_DOWN);
    }

    /**
     * Test that parameters are created correctly when going down then across
     * with a fixed number of rows and a variable number of columns.
     */
    public void testSetCoordinateConverterChooserDownAcrossVariableFixed()
            throws Exception {

        doTestActivation(VARIABLE, FIXED, DOWN_ACROSS);
    }

    /**
     * Test that parameters are created correctly when going across then down
     * with a variable number of rows and columns.
     */
    public void testSetCoordinateConverterChooserAcrossDownVariableVariable()
            throws Exception {

        doTestActivation(VARIABLE, VARIABLE, ACROSS_DOWN);
    }

    /**
     * Test that parameters are created correctly when going down then across
     * with a variable number of rows and columns.
     */
    public void testSetCoordinateConverterChooserDownAcrossVariableVariable()
            throws Exception {

        doTestActivation(VARIABLE, VARIABLE, DOWN_ACROSS);
    }

    /**
     * Parameterised test to check that activation works correctly on a number
     * of different parts.
     *
     * @param columnCountIsFixed True if the column count is fixed, false
     * otherwise.
     * @param rowCountIsFixed True if the row count is fixed, false otherwise.
     * @param acrossDown True if the orientation is across then down, false if
     * it is down then across.
     */
    private void doTestActivation(
            final boolean columnCountIsFixed, final boolean rowCountIsFixed,
            final boolean acrossDown)
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ListTokenizerMock listTokenizerMock =
                new ListTokenizerMock("listTokenizerMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the column constraint attributes.
        spatialMock.expects
                .getAttributeAsInt(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT,
                                   "render-spatial-iterator-missing-columns")
                .returns(MAX_COLUMN_COUNT);
        spatialMock.expects
                .attributeHasValue(SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS,
                                   "fixed")
                .returns(columnCountIsFixed);

        // Initialise the row constraint attributes.
        spatialMock.expects
                .getAttributeAsInt(SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT,
                                   "render-spatial-iterator-missing-rows")
                .returns(MAX_ROW_COUNT);
        spatialMock.expects
                .attributeHasValue(SpatialFormatIterator.SPATIAL_ITERATOR_ROWS,
                                   "fixed")
                .returns(rowCountIsFixed);

        // Initialise the iteration direction.
        spatialMock.expects.attributeHasValue(
                SpatialFormatIterator.SPATIAL_ITERATOR_2D_INDEXING_DIRECTION,
                "AcrossDown").returns(acrossDown).any();

        // Prepare the factory to create a row constraint.
        iteratorConstraintFactoryMock.expects
                .createConstraint(MAX_ROW_COUNT, rowCountIsFixed)
                .returns(rowsConstraintMock);

        // Prepare the factory to create a column constraint.
        iteratorConstraintFactoryMock.expects
                .createConstraint(MAX_COLUMN_COUNT, columnCountIsFixed)
                .returns(columnsConstraintMock);

        // Initialise the row and column style class lists.
        spatialMock.expects
                .getAttribute(SpatialFormatIterator.ROW_STYLE_CLASSES)
                .returns("r1 r2").any();
        spatialMock.expects
                .getAttribute(SpatialFormatIterator.COLUMN_STYLE_CLASSES)
                .returns("c1 c2").any();

        expectations.add(new OrderedExpectations() {
            public void add() {

                listTokenizerMock.expects.extractTokens("r1 r2")
                        .returns(new String[0]).any();

                spatialMock.fuzzy
                        .setRowStyleClasses(ENDLESS_STRING_ARRAY_INSTANCE);
            }
        });

        expectations.add(new OrderedExpectations() {
            public void add() {

                listTokenizerMock.expects.extractTokens("c1 c2")
                        .returns(new String[0]).any();

                spatialMock.fuzzy
                        .setColumnStyleClasses(ENDLESS_STRING_ARRAY_INSTANCE);
            }
        });

        expectations.add(new OrderedExpectations() {
            public void add() {
                if (acrossDown) {
                    coordinateChooserFactoryMock.expects
                            .createHorizontalConverterChooser(
                                    columnsConstraintMock, rowsConstraintMock)
                            .returns(coordinateConverterChooserMock);
                } else {
                    coordinateChooserFactoryMock.expects
                            .createVerticalConverterChooser(
                                    columnsConstraintMock, rowsConstraintMock)
                            .returns(coordinateConverterChooserMock);
                }

                spatialMock.expects.setCoordinateConverterChooser(
                        coordinateConverterChooserMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        SpatialFormatIteratorActivator activator
                = new SpatialFormatIteratorActivator(
                        coordinateChooserFactoryMock,
                        iteratorConstraintFactoryMock,
                        listTokenizerMock);
        activator.activate(spatialMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
