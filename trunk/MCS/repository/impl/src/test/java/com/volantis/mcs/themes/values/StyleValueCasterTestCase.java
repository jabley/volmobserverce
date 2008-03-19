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

package com.volantis.mcs.themes.values;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;

/**
 * Test cases for {@link StyleValueCaster}.
 */
public class StyleValueCasterTestCase
        extends TestCaseAbstract {
    private StyleValueCaster caster;

    protected void setUp() throws Exception {
        super.setUp();

        caster = new StyleValueCaster();
    }

    /**
     * Test StyleInteger to StyleInteger
     */
    public void testInteger2Integer() throws Exception {

        StyleValue value = caster.cast(
            StyleValueFactory.getDefaultInstance().getInteger(null, 2),
                StyleValueType.INTEGER);
        assertEquals(
            StyleValueFactory.getDefaultInstance().getInteger(null, 2), value);
    }

    /**
     * Test StyleNumber to StyleInteger
     */
    public void testNumber2IntegerGood() throws Exception {

        StyleValue value = caster.cast(
            StyleValueFactory.getDefaultInstance().getNumber(null, 2.0),
                StyleValueType.INTEGER);
        assertEquals(
            StyleValueFactory.getDefaultInstance().getInteger(null, 2), value);
    }

    /**
     * Test StyleNumber to StyleInteger
     */
    public void testNumber2IntegerBad() throws Exception {

        StyleValue value = caster.cast(
            StyleValueFactory.getDefaultInstance().getNumber(null, 2.1),
                StyleValueType.INTEGER);
        assertNull(value);
    }
}
