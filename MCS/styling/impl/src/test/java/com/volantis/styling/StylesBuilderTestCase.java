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

package com.volantis.styling;

import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class StylesBuilderTestCase
        extends TestCaseAbstract {

    public void test() {

        Styles styles = StylesBuilder.getStyles("{color: blue}" +
                                                "::before {color: green}" +
                                                "::after {color: red}");
        StyleColor value;

        value = getColor(styles);
        assertEquals("Blue", StyleColorNames.BLUE, value);

        Styles beforeStyles = styles.getNestedStyles(PseudoElements.BEFORE);
        assertNotNull("Before styles", beforeStyles);
        value = getColor(beforeStyles);
        assertEquals("Green", StyleColorNames.GREEN, value);

        Styles afterStyles = styles.getNestedStyles(PseudoElements.AFTER);
        assertNotNull("After styles", afterStyles);
        value = getColor(afterStyles);
        assertEquals("Red", StyleColorNames.RED, value);
    }

    private StyleColor getColor(Styles styles) {
        return (StyleColor) styles.getPropertyValues().getComputedValue(
                        StylePropertyDetails.COLOR);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 02-Sep-05	9407/1	pduffin	VBM:2005083007 Committing resolved conflicts

 ===========================================================================
*/
