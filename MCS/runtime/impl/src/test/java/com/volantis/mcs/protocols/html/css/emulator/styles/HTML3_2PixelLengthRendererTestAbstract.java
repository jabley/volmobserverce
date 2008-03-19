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

package com.volantis.mcs.protocols.html.css.emulator.styles;

import com.volantis.mcs.protocols.styles.PropertyRenderer;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class HTML3_2PixelLengthRendererTestAbstract
        extends TestCaseAbstract {

    private Styles styles;
    private MutablePropertyValues properties;
    private final PropertyRenderer renderer;
    private final StyleProperty property;

    public HTML3_2PixelLengthRendererTestAbstract(
            StyleProperty property, PropertyRenderer renderer) {
        this.property = property;
        this.renderer = renderer;
    }

    protected void setUp() throws Exception {
        super.setUp();

        styles = StylingFactory.getDefaultInstance().createStyles(null);
        properties = styles.getPropertyValues();
    }

    /**
     * Test the getLineHeight method.
     */
    public void testGetValue() throws Exception {
        doTestGetValue(StyleValueFactory.getDefaultInstance().getLength(
            null, 1.3, LengthUnit.PX), "1");
        doTestGetValue(FontSizeKeywords.XX_SMALL, null);
        doTestGetValue(StyleValueFactory.getDefaultInstance().getLength(
            null, 1.44, LengthUnit.EM), null);
    }

    /**
     * Helper for testGetValue
     */
    private void doTestGetValue(StyleValue styleValue, String expected) {
        properties.setComputedValue(property, styleValue);
        assertEquals(expected, renderer.getAsString(styles));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
