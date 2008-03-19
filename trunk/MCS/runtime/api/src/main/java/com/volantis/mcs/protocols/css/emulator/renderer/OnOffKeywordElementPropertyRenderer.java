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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMHelper;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValue;

/**
 * Renders a style property that has at least two keywords that turn emulation
 * on and off and that can be represented as elements
 */
public class OnOffKeywordElementPropertyRenderer
        implements StyleEmulationPropertyRenderer {

    private final StyleKeyword offKeyword;
    private final StyleKeyword onKeyword;
    private final String offElementName;
    private final String onElementName;

    /**
     * Initialise.
     *
     * @param onKeyword      The keyword that turns the style on.
     * @param onElementName  The name of the element that turns the style on.
     * @param offKeyword     The keyword that turns the style off.
     * @param offElementName The name of the element that turns the style off.
     */
    public OnOffKeywordElementPropertyRenderer(
            final StyleKeyword onKeyword, final String onElementName,
            final StyleKeyword offKeyword,
            final String offElementName) {

        this.offKeyword = offKeyword;
        this.onKeyword = onKeyword;
        this.offElementName = offElementName;
        this.onElementName = onElementName;
    }

    // Javadoc inherited.
    public void apply(Element element, StyleValue value) {
        if (value == offKeyword) {
            element = DOMHelper.insertChildElement(element, offElementName);
        } else if (value == onKeyword) {
            element = DOMHelper.insertChildElement(element, onElementName);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 14-Sep-05	9496/3	pduffin	VBM:2005091211 Addressing review comments

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 06-May-05	8090/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 06-May-05	8048/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
