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

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.AbstractTransformingVisitor;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMVisitorBasedTransformer;
import com.volantis.mcs.protocols.TransformingVisitor;

/**
 * This class translates td align=center into centre elements.
 * <p/>
 * This is required as some devices (e.g BlackBerry 7290 V3.8) do not handle
 * align attribute of td properly.
 * <p/>
 * Note that the center element is a shorthand for div align=center. A slightly
 * more general solution might use this instead.
 *
 * TODO: only align="center" is handled now, other alignments may be added if
 * needed
 * TODO: this should be moved from trans to a more appropriate package.
 */
public final class TableCellAligningTransformer
        extends DOMVisitorBasedTransformer {

    // Javadoc inherited.
    protected TransformingVisitor getDOMVisitor(DOMProtocol protocol) {
        return new TableCellAligningVisitor(protocol.getDOMFactory());
    }

    public static final class TableCellAligningVisitor
            extends AbstractTransformingVisitor {

        private final DOMFactory factory;

        public TableCellAligningVisitor(DOMFactory factory) {
            this.factory = factory;
        }

        public void visit(Element element) {
            if ("td".equalsIgnoreCase(element.getName()) ||
                    "th".equalsIgnoreCase(element.getName())) {

                if ("center".equalsIgnoreCase(element.getAttributeValue("align"))) {

                    // Remove the align attribute as it is not supported and we
                    // are about to emulate it if necessary.
                    element.removeAttribute("align");

                    // If the centre aligned element has any content
                    if (element.getHead() != null) {
                        // Insert the center element after the td.
                        Element center = factory.createElement("center");
                        // We need to do this in two stages because the MCS DOM
                        // API is not very user friendly. Here goes.
                        // Move the td's children onto the center
                        element.addChildrenToHead(center);
                        // Add the center back as the first child of the td.
                        element.addHead(center);
                    }

                }
            }

            // Recursively process children
            element.forEachChild(this);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Dec-05	10910/1	pszul	VBM:2005101302 Emulation of align=center with center element necessary for Blackberry 7290 3.8

*/
