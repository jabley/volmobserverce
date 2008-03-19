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
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;

import java.util.Iterator;
import java.util.List;

/**
 * Helper class used to add and delete elements of a DOM tree. Designed to
 * be used by visitor.
 */
public class DOMHelper {

    /**
     * Add a new element as a child of this element. Copy element's
     * children into the new element.
     *
     * @param element element under which to create the new element.
     * @param elementName Name to given to new element
     * @return new element
     */
    public static Element insertChildElement(Element element, String elementName) {


        Element newElement = element.getDOMFactory().createElement();
        newElement.setName(elementName);

        element.addChildrenToHead(newElement);
        element.addHead(newElement);

        return newElement;
    }
    /**
     * Add a new element as the parent of this element. Make this element
     * the only child of the new element.
     *
     * @param element element under which to create the new element.
     * @param elementName Name to given to new element
     * @return new element
     */
    public static Element insertParentElement(Element element, String elementName) {


        Element newElement = element.getDOMFactory().createElement(elementName);

        newElement.replace(element);
        newElement.addHead(element);

        return newElement;
    }


    /**
     * Remove all flagged elements.
     * All children of a deleted element will be made
     * children of element's parent.
     */
    public static void collapseElements(List elementsToBeDeleted) {
        Iterator i = elementsToBeDeleted.iterator();

        while (i.hasNext()) {
            Element element = (Element)i.next();
            element.insertChildrenAfter(element);
            element.remove();
        }

    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Sep-05	9128/7	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05	9128/5	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/3	pabbott	VBM:2005071114 Add XHTML 2 elements

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 ===========================================================================
*/
