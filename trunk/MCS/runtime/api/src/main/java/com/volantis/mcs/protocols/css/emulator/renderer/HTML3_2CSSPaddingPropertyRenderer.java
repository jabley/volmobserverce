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
 * properties using inline CSS.
 *
 */
public class HTML3_2CSSPaddingPropertyRenderer
    extends HTML3_2CSSPropertyRenderer {

    /**
     * Initialise a new instance with the supplied parameters.
     *
     * @param propertyName the border width property name.
     */
    public HTML3_2CSSPaddingPropertyRenderer(String propertyName) {
        super(propertyName, propertyName);
    }

    /**
     * Apply the given style to the table element, supclasses need to provide
     * and implementation of this method.
     *
     * @param element to apply style to
     * @param value to apply to table element
     */
    protected void applyStyleToTableElement(Element element, String value) {
        Element td = getTd(element);

        addStyleToTd(value, td);

        removePaddingFromTable(element);

    }

    /**
     * If the cellpadding has been set on the enclosing table remove it.
     *
     * @param element to check
     */
    private void removePaddingFromTable(Element element) {
        if (element.getName().equals("table")) {
            String cellpadding = element.getAttributeValue("cellpadding");
            if (cellpadding != null) {
                element.setAttribute("cellpadding", "0");
            }
        }
    }

    /**
     * Given an element, which may be <table> or <td>, return a <td>
     * which is a single child of the given element. If we need to add new
     * elements we will create the following structre.
     * <pre>
     *        <table>  - given
     *           |
     *         <tr>
     *           |
     *         <td>    - return this
     * </pre>
     * if the above structure already exists, return the existing <td>.
     *
     * @param element which will contain the sought td, assumed not to be null
     * @return a <td> element inside the given table
     */
    private Element getTd(Element element) {

        Element tr = null;
        Element td = null;

        if (element.getName().equals("table") && hasOneChild(element)) {
            Node trNode = element.getHead();
            if (trNode instanceof Element) {
                tr = (Element)trNode;

                if (hasOneChild(tr)) {
                    Node tdNode = tr.getHead();
                    if (tdNode instanceof Element) {
                        td = (Element)tdNode;
                    }
                }
            }

        } else if (element.getName().equals("td")) {
            td = element;
        }

        if (td == null) {
            tr = DOMHelper.insertChildElement(element, "tr");
            td = DOMHelper.insertChildElement(tr, "td");
            DOMHelper.insertChildElement(td, "table");
        }
        return td;
    }

}

