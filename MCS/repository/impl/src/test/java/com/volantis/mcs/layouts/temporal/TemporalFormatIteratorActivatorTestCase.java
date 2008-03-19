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

package com.volantis.mcs.layouts.temporal;

import com.volantis.mcs.layouts.FormatIteratorActivatorTestAbstract;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.layouts.TemporalFormatIteratorMock;
import com.volantis.mcs.layouts.activator.FormatActivator;
import com.volantis.mcs.layouts.iterators.IteratorSizeConstraintMock;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;

/**
 * Tests for {@link TemporalFormatIteratorActivator}.
 */
public class TemporalFormatIteratorActivatorTestCase
    extends FormatIteratorActivatorTestAbstract {

    private TemporalFormatIteratorMock temporalMock;

    /**
     * Test that the max cell constraint is set properly.
     */ 
    public void testActivation()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        temporalMock = LayoutTestHelper.createTemporalFormatIteratorMock(
                        "temporalMock", expectations,
                        canvasLayoutMock);

        final IteratorSizeConstraintMock maxCellsConstraintMock =
                new IteratorSizeConstraintMock("maxCellsConstraintMock",
                                               expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the temporal format.
        temporalMock.expects.getAttributeAsInt(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT,
                "render-temporal-iterator-missing-cells")
                .returns(5)
                .any();

        temporalMock.expects.attributeHasValue(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS, "fixed")
                .returns(FIXED)
                .any();

        iteratorConstraintFactoryMock.expects.createConstraint(5, FIXED)
                .returns(maxCellsConstraintMock);

        temporalMock.expects.setMaxCellConstraint(maxCellsConstraintMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatActivator activator = new TemporalFormatIteratorActivator(
                iteratorConstraintFactoryMock);
        activator.activate(temporalMock);
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
