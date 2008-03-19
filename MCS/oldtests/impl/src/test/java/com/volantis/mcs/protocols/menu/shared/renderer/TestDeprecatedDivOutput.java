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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.dom.Element;

/**
 * A test implementation of {@link DeprecatedDivOutput} which writes out
 * the div attributes as simply as possible.
 */ 
public class TestDeprecatedDivOutput extends AbstractTestMarkupOutput 
        implements DeprecatedDivOutput {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    // Javadoc inherited.
    public void openDiv(DOMOutputBuffer dom, DivAttributes attributes) {
        Element element = dom.openStyledElement("test-div", attributes);
        addCoreAttributes(attributes, element);
    }

    // Javadoc inherited.
    public void closeDiv(DOMOutputBuffer dom, DivAttributes attributes) {
        dom.closeElement("test-div");
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 ===========================================================================
*/
