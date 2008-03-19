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

import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for {@link LengthUnitConverter}.
 */
public class LengthUnitConverterTestCase extends TestCaseAbstract {
    /**
     * Precision for checking equality on double values.
     */
    private final static double PRECISION = 0.001;
    
    /**
     * Tests conversion between all units. 
     */
    public void testConversion() throws Exception {
        // 1 cm = 10 mm 
        assertEquals(1.0, LengthUnit.CM, 10.0, LengthUnit.MM);
        
        // 1 in = 25.4 mm
        assertEquals(1.0, LengthUnit.IN, 25.4, LengthUnit.MM);
        
        // 1 pt = 1/72 in
        assertEquals(1.0, LengthUnit.PT, 1.0/72.0, LengthUnit.IN);
        
        // 1 pc = 12 pt
        assertEquals(1.0, LengthUnit.PC, 12.0, LengthUnit.PT);
    }

    /**
     * Tests conversion between source and destination values&units.
     *  
     * @param sourceValue The source value.
     * @param sourceUnit The source unit.
     * @param destValue Expected destination value.
     * @param destUnit The destination unit.
     */
    private void assertEquals(double sourceValue, LengthUnit sourceUnit, double destValue,
            LengthUnit destUnit) {
        LengthUnitConverter converter = LengthUnitConverter.getInstance();

        assertEquals("Test " + sourceValue + sourceUnit.toString() + "=" + destValue
                + destUnit.toString(), destValue, converter.convert(sourceValue, sourceUnit,
                destUnit), PRECISION);

        assertEquals("Test " + destValue + destUnit.toString() + "=1/" + sourceValue
                + sourceUnit.toString(), 1/sourceValue, converter.convert(destValue, destUnit,
                sourceUnit), PRECISION);
    }    
}
