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
package com.volantis.mcs.protocols.css.emulator.renderer;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.html.css.emulator.renderer.HTML3_2BorderSpacingEmulationAttributeValueRenderer;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test ...
 */
public class StyleEmulationElementSetAttributeRendererTestCase 
        extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * The object to use to create style values.
     */
    private StyleValueFactory styleValueFactory;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        styleValueFactory = StyleValueFactory.getDefaultInstance();
    }

    /**
     * Test the apply method.
     */
    public void testApply() throws Exception {

        StyleEmulationPropertyRenderer renderer = 
                new StyleEmulationElementSetAttributeRenderer(
                        new String[] {"table"}, "cellspacing", 
                        new HTML3_2BorderSpacingEmulationAttributeValueRenderer());

        Element node = domFactory.createElement();
        node.setName("table");

        StyleValue first = styleValueFactory.getLength(null, 15, LengthUnit.PX);
        
        StylePair pair = styleValueFactory.getPair(first, null);

        renderer.apply(node, pair);
        assertEquals("Buffer should match",
                "<table cellspacing=\"15\"/>",
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

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 30-Jun-04	4781/3	adrianj	VBM:2002111405 Created SMS test case and added check for null/empty mime types in protocols

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 29-Jun-04	4720/3	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 28-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities - rework issues

 ===========================================================================
*/
