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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertyRenderer;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests the vertical alignment renderer.
 */
public class HTML3_2VerticalAlignEmulationPropertyRendererTestCase
        extends TestCaseAbstract {
    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();


    /**
     * Tests that vertical alignment is rendered correctly.
     */
    public void testAddingVerticalAlignmentAttribute() throws Exception {
        final HTML3_2VerticalAlignEmulationPropertyRenderer renderer =
                new HTML3_2VerticalAlignEmulationPropertyRenderer();

        // Create the "top" vertical alignment style value.
        final StyleKeyword kvalue = VerticalAlignKeywords.TOP;

        // Test that an align attribute is added to the img element.
        assertTrue("align attribute should be added to the img element",
                doTest(renderer, kvalue, "img",
                        "<img align=\"top\"/>"));

        // Test that an align attribute is added to the input element.
        assertTrue("align attribute should be added to the input element",
                doTest(renderer, kvalue, "input",
                        "<input align=\"top\"/>"));

        // Test that a valign attribute is added to the tr element.
        assertTrue("valign attribute should be added to the tr element",
                doTest(renderer, kvalue, "tr",
                        "<tr valign=\"top\"/>"));

        // Test that a valign attribute is added to the td element.
        assertTrue("valign attribute should be added to the td element",
                doTest(renderer, kvalue, "td",
                        "<td valign=\"top\"/>"));

        // Test that a valign attribute is added to the th element.
        assertTrue("valign attribute should be added to the th element",
                   doTest(renderer, kvalue, "th", "<th valign=\"top\"/>"));
    }

    /**
     * Helper method which tests that the StyleValue added as an attribute to
     * the named element is as expected.
     * @param renderer the StyleEmulationPropertyRenderer to test
     * @param kvalue the style value to add as an attribute
     * @param elementName the name of the element to which the attribute is
     * added
     * @param expectedResult the expected result of the attribute addition
     * @return true if the rendering was as expected; false otherwise
     */
    private boolean doTest(StyleEmulationPropertyRenderer renderer,
                           StyleKeyword kvalue, String elementName,
                           String expectedResult) {

        // Create the named element in the buffer,
        Element elem = domFactory.createElement();
        elem.setName(elementName);

        // Do the rendering.
        renderer.apply(elem, kvalue);

        // Returns whether or not the rendering was as expected.
        return DOMUtilities.toString(elem).equals(expectedResult);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 15-Jun-05	8788/1	rgreenall	VBM:2005050501 Merge from 331

 15-Jun-05	8792/1	rgreenall	VBM:2005050501 Style emulation support for <td> element.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Aug-04	5136/1	pcameron	VBM:2004080502 Fixed NullPointerExceptions in renderers and added unit tests

 ===========================================================================
*/
