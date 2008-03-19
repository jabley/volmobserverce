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
package com.volantis.mcs.layouts.spatial;

/**
 * Test cases for {@link VerticalCoordinateConverterRightToLeft}.
 */

public class VariableVerticalConverterChooserRTLTestCase
            extends VariableConverterChooserTestAbstract {

        /**
         * Test that object is created correctly.
         */
        public void testCreation() throws Exception {

            // =====================================================================
            //   Set Expectations
            // =====================================================================

            int instanceCount = 10;
            int height = 2;
            int maxWidth = calculateSecondDimensionSize(instanceCount, height);
            int width = 4;

            rowsConstraintMock.expects.getConstrained(instanceCount)
                    .returns(height).any();
            columnsConstraintMock.expects.getConstrained(maxWidth)
                    .returns(width).any();

            // =====================================================================
            //   Test Expectations
            // =====================================================================
            CoordinateConverterChooser chooser =
                    new VariableVerticalConverterChooser(
                            columnsConstraintMock, rowsConstraintMock);

            // Get the coordinate converter.
            CoordinateConverter coordinateConverter =
                chooser.chooseCoordinateConverter(instanceCount, true);
            assertTrue(coordinateConverter instanceof VerticalCoordinateConverterRightToLeft);
        }
    }


