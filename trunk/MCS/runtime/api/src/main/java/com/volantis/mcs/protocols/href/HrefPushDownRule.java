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
package com.volantis.mcs.protocols.href;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;

/**
 * Take the href field and move it down to the child elements.
 */
public class HrefPushDownRule extends HrefRule {

    /**
     * Loop round all child elements adding the href to them.
     * These will be processed when the visitor reaches them. Which assumes
     * that child elemets are processed after parents.
     *
     * @param element current element
     */
    public void transform(Element element) {

        String href = element.getAttributeValue(HREF);

        Node child = element.getHead();

        while (child != null) {

            if (child instanceof Element) {
                Element childElement = (Element)child;

                childElement.setAttribute(HREF, href);
            }

            child = child.getNext();

        }

        element.removeAttribute(HREF);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
