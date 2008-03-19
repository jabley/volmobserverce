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
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.WalkingDOMVisitor;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.WalkingDOMVisitorBasedTransformer;
import com.volantis.mcs.protocols.css.emulator.StyleEmulationRenderer;

/**
 * This transformer will render style emulation.
 */
public class StyleEmulationRenderingTransformer
        extends WalkingDOMVisitorBasedTransformer {

    /**
     * Renderer used to add styling to elements of the DOM tree.
     */
    private final StyleEmulationRenderer styleEmulationRenderer;

    public StyleEmulationRenderingTransformer(
            StyleEmulationRenderer renderer) {
        styleEmulationRenderer = renderer;
    }

    // Javadoc inherited
    protected WalkingDOMVisitor getWalkingDOMVisitor(DOMProtocol protocol) {
        return new StyleEmulationRenderingVisitor(styleEmulationRenderer);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 ===========================================================================
*/
