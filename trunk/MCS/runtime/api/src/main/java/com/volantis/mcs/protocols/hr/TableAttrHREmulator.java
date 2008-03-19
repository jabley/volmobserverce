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
package com.volantis.mcs.protocols.hr;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ProtocolException;


/**
 * Horizontal Rule Emulator using the tables.
 */
public class TableAttrHREmulator
        extends DefaultHorizontalRuleEmulator {

    //javadoc inherited
    public Element doEmulation(
            DOMOutputBuffer domOutputBuffer,
            HorizontalRuleAttributes hrAttrs)
            throws ProtocolException {

        Element table = domOutputBuffer.openElement("table");
        emulateTableStyles(table);
        domOutputBuffer.openElement("tr");
        Element td = domOutputBuffer.openElement("td");
        emulateTdStyles(td);
        domOutputBuffer.closeElement("td");
        domOutputBuffer.closeElement("tr");
        domOutputBuffer.closeElement("table");

        return table;
    }

    /**
     * Add the style details to the td
     *
     * @param td element
     */
    private void emulateTdStyles(Element td) {

        if (color != null) {

            String colourStr = color.getStandardCSS();

            td.setAttribute("bgcolor", colourStr);
            td.setAttribute("bordercolor", colourStr);
        }
    }

    /**
     * Add the style details to the table
     *
     * @param table   ellement
     */
    private void emulateTableStyles(Element table) {

        table.setAttribute("align", align.getStandardCSS());

        String heightStr = getStyleValueAsString(height);
        if (heightStr != null) {
            table.setAttribute("height", heightStr);
        }

        String widthStr = getStyleValueAsString(width);
        if (widthStr != null) {
            table.setAttribute("width", widthStr);
        }

        table.setAttribute("border", "0");
        table.setAttribute("cellpadding", "0");
        table.setAttribute("cellspacing", "0");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Oct-05	9565/6	ibush	VBM:2005081219 Horizontal Rule Emulation

 27-Oct-05	9565/3	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
