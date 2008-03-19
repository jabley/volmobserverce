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
package com.volantis.mcs.protocols.css.emulator.renderer;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class is responsible for testing the behaviour of
 * {@link StyleEmulationElementAppendAttributeRenderer}.
 */
public class StyleEmulationElementAppendAttributeRendererTestCase extends
        TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    private static DOMFactory DOM_FACTORY = DOMFactory.getDefaultInstance();

    /**
     * Constant for the hexidecimal value for the color orange.
     */
    private static final int COLOR_ORANGE = 0xffa500;

    /**
     * Tests that {@link StyleEmulationElementAppendAttributeRenderer#apply}
     * works correctly when the style attribute on an element has not yet
     * been set.
     */
    public void testOpenWhenStyleAttributeNotCurrentlySet() throws Exception {

        Element tdElement = createElement("td");
        testBorderLeftColorRendererOnTd(tdElement);
    }

    public void testOpenWhenStyleElementPreviouslySet() throws Exception {

        Element tdElement = createElement("td");

        // run the first renderer to create a style attribute on the td
        // element
        testBorderLeftColorRendererOnTd(tdElement);

        // Now try using another renderer to append an additional css property
        // to the existing style attribute.  Previous values set should not be
        // overriden.
        testBorderLeftWidthRendererOnTd(tdElement);
    }

    private void testBorderLeftColorRendererOnTd(Element tdElement) throws Exception {
        StyleEmulationPropertyRenderer borderLeftColorRenderer =
                  new StyleEmulationElementAppendAttributeRenderer(
                        new String[] {"td"}, "style",
                        new CSSColorPropertyRenderer("border-left-color"));



        StyleValue color = StyleValueFactory.getDefaultInstance()
                .getColorByRGB(null, COLOR_ORANGE);

        borderLeftColorRenderer.apply(tdElement, color);

        assertEquals("Buffer should match",
                "<td style=\"border-left-color:#ffa500;\"/>",
                DOMUtilities.toString(tdElement));
    }

    private void testBorderLeftWidthRendererOnTd(Element tdElement)
            throws Exception {

        StyleEmulationPropertyRenderer borderLeftWidthRenderer =
                  new StyleEmulationElementAppendAttributeRenderer(
                        new String[] {"td"}, "style",
                        new CSSBorderWidthPropertyRenderer("border-left-width"));

        StyleLength borderWidthStyle = StyleValueFactory.getDefaultInstance()
                .getLength(null, 15, LengthUnit.PX);


        borderLeftWidthRenderer.apply(tdElement, borderWidthStyle);

        assertEquals("Buffer should match",
                "<td style=\"border-left-color:#ffa500;border-left-width:15px;\"/>",
                DOMUtilities.toString(tdElement));
    }

    /**
     * Factory method to create a new element with the specified name.
     *
     * @param name the name of the new element.
     *
     * @return an element initialised with the supplied name.
     */
    private Element createElement(String name) {
        return DOM_FACTORY.createElement(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Nov-05	10381/2	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 17-Nov-05	10251/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 ===========================================================================
*/
