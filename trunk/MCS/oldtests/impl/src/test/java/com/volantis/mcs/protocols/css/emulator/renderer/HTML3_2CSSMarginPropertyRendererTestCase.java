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
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the HTML3_2CSSMarginPropertyRenderer
 */
public class HTML3_2CSSMarginPropertyRendererTestCase
    extends TestCaseAbstract {

    public void testMarginTopEmulation() {
        Element table = createTableStructure();

        HTML3_2CSSMarginPropertyRenderer marginPropertyRenderer =
            new HTML3_2CSSMarginPropertyRenderer("margin-top");

        StyleValue style = StyleValueFactory.getDefaultInstance()
            .getLength(null, 15, LengthUnit.PX);

        marginPropertyRenderer.apply(table, style);

        String expectedRendering =
            "<table><tr><td style=\"padding-top:15px; \"/></tr><tr><td/></tr></table>";
        String actualRendering = DOMUtilities.toString(table);

        assertEquals("Margin not as expected", expectedRendering,
            actualRendering);
    }

    public void testMarginBottomEmulation() {
        Element table = createTableStructure();

        HTML3_2CSSMarginPropertyRenderer marginPropertyRenderer =
            new HTML3_2CSSMarginPropertyRenderer("margin-bottom");

        StyleValue style = StyleValueFactory.getDefaultInstance()
            .getLength(null, 15, LengthUnit.PX);

        marginPropertyRenderer.apply(table, style);

        String expectedRendering =
            "<table><tr><td/></tr><tr><td style=\"padding-top:15px; \"/></tr></table>";
        String actualRendering = DOMUtilities.toString(table);

        assertEquals("Margin not as expected", expectedRendering,
            actualRendering);
    }

    public void testMarginTopAndBottomEmulation() {
        Element table = createTableStructure();

        HTML3_2CSSMarginPropertyRenderer marginTopPropertyRenderer =
            new HTML3_2CSSMarginPropertyRenderer("margin-top");

        HTML3_2CSSMarginPropertyRenderer marginBottomPropertyRenderer =
            new HTML3_2CSSMarginPropertyRenderer("margin-bottom");

        StyleValue style = StyleValueFactory.getDefaultInstance()
            .getLength(null, 15, LengthUnit.PX);

        marginTopPropertyRenderer.apply(table, style);
        marginBottomPropertyRenderer.apply(table, style);

        String expectedRendering =
            "<table><tr><td style=\"padding-top:15px; \"/></tr><tr><td/></tr><tr><td style=\"padding-top:15px; \"/></tr></table>";
        String actualRendering = DOMUtilities.toString(table);

        assertEquals("Margin not as expected", expectedRendering,
            actualRendering);
    }

    /**
     * Utility method to create a table structure.
     *
     * @return a table element with one <tr> and one <td>
     */
    private Element createTableStructure() {
        Element table = createElement("table");
        Element tr = createElement("tr");
        Element td = createElement("td");

        table.addHead(tr);
        tr.addHead(td);
        return table;
    }


    /**
     * Factory method to create a new element with the specified name.
     *
     * @param name the name of the new element.
     * @return an element initialised with the supplied name.
     */
    private Element createElement(String name) {
        return DOMFactory.getDefaultInstance().createElement(name);
    }

}
