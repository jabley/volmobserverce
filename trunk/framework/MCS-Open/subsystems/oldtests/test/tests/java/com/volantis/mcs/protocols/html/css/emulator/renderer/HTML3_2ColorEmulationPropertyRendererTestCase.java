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
import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the color rule.
 */
public class HTML3_2ColorEmulationPropertyRendererTestCase 
        extends TestCaseAbstract {
    
    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Test the apply method.
     */
    public void testApplyWithFont() throws Exception {
        HTML3_2ColorEmulationPropertyRenderer renderer = 
                new HTML3_2ColorEmulationPropertyRenderer();
        Element node = domFactory.createElement();
        node.setName("p");

        StyleColor color = StyleColorNames.BLUE;
        renderer.apply(node, color);
        assertEquals("Buffer should match", "<p><font color=\"blue\"/></p>",
                DOMUtilities.toString(node));
    }

    /**
     * Test the apply method.
     */
    public void testApplyWithFontAlreadyOpen() throws Exception {
        HTML3_2ColorEmulationPropertyRenderer renderer = 
                new HTML3_2ColorEmulationPropertyRenderer();
        Element node = domFactory.createElement();
        node.setName("font");
        node.setAttribute("family", "serif");

        StyleColor color = StyleColorNames.BLUE;
        renderer.apply(node, color);
        assertEquals("Buffer should match", "<font color=\"blue\" family=\"serif\"/>",
                DOMUtilities.toString(node));
    }

    /**
     * Test the apply method.
     */
    public void testApplyWithBody() throws Exception {
        HTML3_2ColorEmulationPropertyRenderer renderer = 
                new HTML3_2ColorEmulationPropertyRenderer();
        Element node = domFactory.createElement();
        node.setName("body");

        StyleColor color = StyleColorNames.BLUE;
        renderer.apply(node, color);
        assertEquals("Buffer should match",
                "<body text=\"blue\"/>",
                DOMUtilities.toString(node));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 29-Jun-04	4720/4	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 28-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities - rework issues

 ===========================================================================
*/
