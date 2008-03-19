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
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.protocols.DOMHelper;

/**
 * This class is responsible for providing a renderer for padding
 * properties using inline CSS. Currently this only renders top
 * and bottom as it has been written specifically for a Blackberry 7290.
 */
public class HTML3_2CSSMarginPropertyRenderer
    extends HTML3_2CSSPropertyRenderer {

    private boolean renderTop = false;
    private boolean renderBottom = false;

    /**
     * Initialise a margin property renderer, we set the CSS property name to
     * padding-top which will be added to a new td in the apply method.
     *
     * @param propertyName to apply to element
     */
    public HTML3_2CSSMarginPropertyRenderer(String propertyName) {
        super("padding-top", propertyName);

        if (propertyName.equals("margin-top")) {
            renderTop = true;
        } else if (propertyName.equals("margin-bottom")) {
            renderBottom = true;
        } else {
            throw new IllegalStateException("Renderer unable to handle "+propertyName);
        }
    }

    /**
     * Apply the given style by creating a new <tr>, containing a new
     * <td>. The emulated style will be applied to this new <td>. If
     * we are rendering a margin-top the new <tr> will be added as the first
     * child element, if we are rendering a margin-bottom then we will add it
     * as the last child element.
     *
     * @param element to apply style to
     * @param value to apply to table element
     */
    protected void applyStyleToTableElement(Element element,
        String value) {

        Element table = getTable(element);

        Element tr = table.getDOMFactory().createElement();
        tr.setName("tr");

        if (renderTop) {
            table.addHead(tr);
        } else if (renderBottom) {
            table.addTail(tr);
        } else {
            throw new IllegalStateException(
                "renderTop and renderBottom are both false, " +
                    "this should not be possible as the constructor should have caught this");
        }

        Element td = DOMHelper.insertChildElement(tr, "td");

        addStyleToTd(value, td);


    }

    /**
      * Given an element, which may be a <table> or a <td>, get the <table>
      * element which is either the given element or its child.
      * This method will insert new elements below the current element.
      *
      * @param element to search from
      * @return the required table element
      */
    private Element getTable(Element element) {

        Element table = null;

        if (element.getName().equals("table")) {
            pushBackgroundColourDown(element);

            table = element;
        } else if (element.getName().equals("td")) {
            if (containsATable(element)) {
                table = (Element)element.getHead();

                pushBackgroundColourDown(element);
                pushBackgroundColourDown(table);

            } else {
                table = DOMHelper.insertChildElement(element, "table");
                Element tr = DOMHelper.insertChildElement(table, "tr");
                DOMHelper.insertChildElement(tr, "td");

                pushBackgroundColourDown(element);
                pushBackgroundColourDown(table);
            }
        }


        return table;
    }

    /**
     * Does the given element contain a single table element?
     *
     * @param element to start checking from
     * @return true if the required structure is found
     */
    private boolean containsATable(Element element) {

        boolean result = false;

        if (hasOneChild(element)) {
            Node nodeChild1 = element.getHead();
            if (nodeChild1 instanceof Element) {
                Element elementChild1 = (Element)nodeChild1;
                if (elementChild1.getName().equals("table")) {
                        result = true;
                    }
            }
        }
        return result;
    }

    /**
     * Move the background colour attribute from the given table, if found,
     * and add it to the inner table.
     *
     * @param outerElement containing the background colour
     */
    private void pushBackgroundColourDown(Element outerElement) {

        String backgroundColour = outerElement.getAttributeValue("bgcolor");

        if (backgroundColour != null) {

            outerElement.removeAttribute("bgcolor");

            Node child = outerElement.getHead();
            while (child != null) {
                if (child instanceof Element) {
                    Element elementChild = (Element)child;
                    if (elementChild.getAttributeValue("bgcolor") == null) {
                        elementChild.setAttribute("bgcolor", backgroundColour);
                    }
                }

                child = child.getNext();
            }

        }


    }

}

