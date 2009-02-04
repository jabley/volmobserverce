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

package com.volantis.mcs.protocols.wml.css.emulator.renderer;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertyRenderer;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSImageSavingKeywords;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class WMLImageAltEmulationPropertyRendererTestCase
    extends TestCaseAbstract {

    private final DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Test that if the value says to save that nothing is done to the img
     * element.
     */
    public void testSave() {

        Element img = domFactory.createElement("img");

        StyleValue value = MCSImageSavingKeywords.DEFAULT;

        StyleEmulationPropertyRenderer renderer =
                new WMLImageAltEmulationPropertyRenderer();
        renderer.apply(img, value);

        assertEquals("Markup should match", "<img/>",
                     DOMUtilities.toString(img));
    }

    /**
     * Test that if the value says not to save and there is no alt text that
     * the alt text attribute is simply added to the img.
     */
    public void testDontSaveNoAlt() {

        Element img = domFactory.createElement("img");

        StyleValue value = MCSImageSavingKeywords.DISALLOW;

        StyleEmulationPropertyRenderer renderer =
                new WMLImageAltEmulationPropertyRenderer();
        renderer.apply(img, value);

        assertEquals("Markup should match", "<img alt=\"NO_SAVE\"/>",
                     DOMUtilities.toString(img));
    }

    /**
     * Test that if the value says not to save and there is an alt text that
     * the alt text attribute is copied to the title and set to the NO_SAVE
     * value.
     */
    public void testDontSaveWithAlt() {

        Element img = domFactory.createElement("img");
        img.setAttribute("alt", "Alternate Text");

        StyleValue value = MCSImageSavingKeywords.DISALLOW;

        StyleEmulationPropertyRenderer renderer =
                new WMLImageAltEmulationPropertyRenderer();
        renderer.apply(img, value);

        assertEquals("Markup should match",
                     "<img alt=\"NO_SAVE\"/>",
                     DOMUtilities.toString(img));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10039/6	ibush	VBM:2005103106 Remove title attribute from image renderer

 12-Dec-05	10039/4	ibush	VBM:2005103106 Remove title attribute from image renderer

 31-Oct-05	10039/1	ibush	VBM:2005103106 Remove title attribute from image renderer

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 ===========================================================================
*/
