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
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests the padding renderer.
 */
public class HTML3_2PaddingEmulationPropertyRendererTestCase
        extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Tests that padding is rendered correctly.
     */
    public void testPaddingEmulation() throws Exception {
        HTML3_2PaddingEmulationPropertyRenderer renderer =
                new HTML3_2PaddingEmulationPropertyRenderer();

        Element table = domFactory.createElement();
        table.setName("table");

        Element td = domFactory.createElement();
        td.setName("td");
        table.addTail(td);

        StyleLength length = StyleValueFactory.getDefaultInstance().getLength(
            null, 5.0, LengthUnit.PX);

        renderer.apply(td, length);

        assertEquals("Cell padding should have been added to the table",
                "<table cellpadding=\"5\"><td/></table>",
                DOMUtilities.toString(table));
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

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Aug-04	5136/3	pcameron	VBM:2004080502 Fixed NullPointerExceptions in renderers and added unit tests

 ===========================================================================
*/
