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

package com.volantis.mcs.themes.values.converters;

import com.volantis.mcs.themes.values.AngleUnit;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for {@link AngleUnitConverter}.
 */
public class AngleUnitConverterTestCase extends TestCaseAbstract {
    /**
     * Precision for checking equality on double values.
     */
    private final static double PRECISION = 0.001;
        
    /**
     * Tests conversion between all units. 
     */
    public void testConversion() throws Exception {
        // 1 radian = 180/PI degrees
        assertEquals(1.0, AngleUnit.RAD, 180.0 / Math.PI, AngleUnit.DEG);

        // 1 grade = 0.9 degrees
        assertEquals(1.0, AngleUnit.GRAD, 0.9, AngleUnit.DEG);
    }

    /**
     * Tests conversion between source and destination values&units.
     *  
     * @param sourceValue The source value.
     * @param sourceUnit The source unit.
     * @param destValue Expected destination value.
     * @param destUnit The destination unit.
     */
    private void assertEquals(double sourceValue, AngleUnit sourceUnit, double destValue,
            AngleUnit destUnit) {
        AngleUnitConverter converter = AngleUnitConverter.getInstance();

        assertEquals("Test " + sourceValue + sourceUnit.toString() + "=" + destValue
                + destUnit.toString(), destValue, converter.convert(sourceValue, sourceUnit,
                destUnit), PRECISION);
        
        assertEquals("Test " + destValue + destUnit.toString() + "=1/" + sourceValue
                + sourceUnit.toString(), 1/sourceValue, converter.convert(destValue, destUnit,
                sourceUnit), PRECISION);
    }    
}
