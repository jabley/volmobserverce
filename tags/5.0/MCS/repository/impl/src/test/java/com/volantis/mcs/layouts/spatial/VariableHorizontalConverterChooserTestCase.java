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

/**
 * Test cases for {@link VariableHorizontalConverterChooser}.
 */
public class VariableHorizontalConverterChooserTestCase
        extends VariableConverterChooserTestAbstract {

    /**
     * Test that object is created correctly.
     */
    public void testCreation()
        throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        int instanceCount = 10;
        int width = 4;
        int maxHeight = calculateSecondDimensionSize(instanceCount, width);
        int height = 2;

        columnsConstraintMock.expects.getConstrained(instanceCount)
                .returns(width).any();
        rowsConstraintMock.expects.getConstrained(maxHeight)
                .returns(height).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        CoordinateConverterChooser chooser =
                new VariableHorizontalConverterChooser(
                        columnsConstraintMock, rowsConstraintMock);

        // Get the coordinate converter.
        CoordinateConverter coordinateConverter =
            chooser.chooseCoordinateConverter(instanceCount, false);
        assertTrue(coordinateConverter instanceof HorizontalCoordinateConverter);
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
