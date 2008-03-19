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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultVerticalAlignEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationElementSetAttributeRenderer;
import com.volantis.mcs.themes.StyleValue;

/**
 * Renders the 'vertical-align' style property as stylistic markup for HTML
 * 3.2.
 *
 * @todo This is a rather nasty hack. Think of a better way.
 */
public class HTML3_2VerticalAlignEmulationPropertyRenderer
    extends StyleEmulationElementSetAttributeRenderer {

    public HTML3_2VerticalAlignEmulationPropertyRenderer() {
        super(new String[]{"tr", "td", "th", "img", "input"}, "valign",
            new DefaultVerticalAlignEmulationAttributeValueRenderer());
    }

    // javadoc inherited
    protected void processAttribute(Element element, String attributeName,
                                    StyleValue value) {

        String name = element.getName();

        String renderedValue = attributeValueRenderer.render(value);

        if (renderedValue != null) {
            if ("tr".equals(name) || "td".equals(name) || "th".equals(name)) {
                setAttributeValue(element, "valign", renderedValue);
            } else if ("img".equals(name) || "input".equals(name)) {
                setAttributeValue(element, "align", renderedValue);
            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 10-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 03-Aug-05	8923/1	pabbott	VBM:2005063010 End to End CSS emulation test

 15-Jun-05	8788/1	rgreenall	VBM:2005050501 Merge from 331

 15-Jun-05	8792/1	rgreenall	VBM:2005050501 Style emulation support for <td> element.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Aug-04	5141/1	pcameron	VBM:2004080502 Fixed NullPointerExceptions in renderers and added unit tests

 09-Aug-04	5136/1	pcameron	VBM:2004080502 Fixed NullPointerExceptions in renderers and added unit tests

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
