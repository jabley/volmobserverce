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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;

/**
 * {@link AbstractDivRemover} implementation which removes all redundant (as
 * defined for HTML_iMode) div elements from a {@link Document}.
 */
public class HTML_iModeDivRemover extends AbstractDivRemover {

    // Javadoc inherited.
    protected void removeInnerDivTag(Element outerDiv) {

        // If the outer div tag does not have an id and its child is an
        //  element, we possibly may be able to remove the inner div tag
        if ((outerDiv.getAttributeValue("id") == null)
                && (outerDiv.getHead() instanceof Element)) {

            // Find the inner div tag that is contained by the outerdiv
            Element innerDiv = findOnlyChild(outerDiv, "div");

            if (innerDiv != MULTIPLE_CHILDREN_FOUND) {
                // This div contains just one nested inner div tag

                // Can only remove the inner div if the align attribute is
                // the same on both inner and outer, or null on one (or
                // both) of them.
                String outerAlign = outerDiv.getAttributeValue("align");
                String innerAlign = innerDiv.getAttributeValue("align");
                if (outerAlign == null) {
                    outerAlign = innerAlign;
                    innerAlign = null;
                } else if (outerAlign.equals(innerAlign)) {
                    innerAlign = null;
                }

                if (innerAlign == null) {
                    if (outerAlign != null) {
                        outerDiv.setAttribute("align",outerAlign);
                    }

                    // Preserve the id attribute
                    if (innerDiv.getAttributeValue("id") != null) {
                        outerDiv.setAttribute("id",
                                innerDiv.getAttributeValue("id"));
                    }
                    // Remove the div tag
                    innerDiv.setName(null);
                }
            }
        }
    }

    // Javadoc inherited.
    protected boolean isDivEligibleForRemoval(Element element) {

        // Can only remove non null elements with no id attribute.
        boolean canRemove = element != null &&
                element.getAttributeValue("id") == null;

        if (canRemove) {
            String align = element.getAttributeValue("align");
            if (align != null) {
                // Can potentially remove an element with an align attribute,
                // as long as its parent is a div element with the same value
                // as for its align attribute.
                if (!parentTagMatches(element, "div")) {
                    // if parent tag isn't div, can't combine so it doesn't
                    // matter if their attribute values match.
                    canRemove = false;
                } else {
                    Element parent = getNonNullNamedParent(element);
                    canRemove = (parent != null) &&
                            align.equals(parent.getAttributeValue("align"));
                }
            }
        }

        return canRemove;
    }

    // javadoc inherited
    protected boolean isBlockyElement(final Node node) {
        
        // The original issue was raised against the XHTMLBasic protocol, so
        // the implementation here is not changed.
        return node instanceof Element;
    }
}
