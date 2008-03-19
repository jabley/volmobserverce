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
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.protocols.DOMHelper;
import com.volantis.mcs.protocols.css.emulator.CSSConstants;
import com.volantis.mcs.protocols.css.emulator.StyleEmulationRenderer;

import java.util.ArrayList;

/**
 * Visitor used to render styling emulation on elements. The visitor also
 * manages the deletion of elements.
 *
 */
public class StyleEmulationRenderingVisitor extends WalkingDOMVisitorStub {

    private StyleEmulationRenderer styleEmulationRenderer = null;
    private ArrayList elementsToBeCollapsed = null;

    public StyleEmulationRenderingVisitor(StyleEmulationRenderer renderer) {
        styleEmulationRenderer = renderer;
        elementsToBeCollapsed = new ArrayList();
    }

    // Javadoc inherited.
    public void visit(Element element) {
        styleEmulationRenderer.applyStyleToElement(element);

        // If the element was only a placeholder for the styles
        // delete it.
        if (element.getName().equals(CSSConstants.STYLE_ELEMENT)) {
            elementsToBeCollapsed.add(element);
        }

    }

    /**
     * After we have visited all elements see if any of them have been marked
     * for "collapse" - (Paul D doesn't like the word delete).
     *
     * @param element
     */
    public void afterChildren(Document element) {
        DOMHelper.collapseElements(elementsToBeCollapsed);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/5	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 ===========================================================================
*/
