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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementInternal;

import java.util.ListIterator;
import java.util.Stack;

/**
 * Set of utility methods used by XHTML2 elements.
 */
public class ElementUtils {

    /**
     * Find the first containing element which is an XHTML2Element.
     *
     * We iterate from the head of the stack backwards until we find one or
     * come to the end. We assume that the head of the stack contains the
     * current element so skip it.
     *
     * @param context Context used to access the stack
     * @return the element found on the stack or null
     */
    public static XHTML2Element getContainingXHTML2Element(
        XDIMEContextInternal context) {

        Stack elementStack = ((XDIMEContextImpl)context).getStack();
        ListIterator elementIterator = elementStack.listIterator(
            elementStack.size());

        Object foundElement = null;

        // Skip the first element
        elementIterator.previous();

        while (foundElement == null && elementIterator.hasPrevious()) {
            Object currentElement = elementIterator.previous();

            if (currentElement instanceof XHTML2Element) {
                foundElement = currentElement;
            }
        }

        return (XHTML2Element)foundElement;
    }

    /**
     * Find the first containing element which is matched by the specified
     * filter.
     *
     * @param element The element to start with, may be null.
     * @param filter The filter to select element in the stack.
     * @return First element on the stack which is an instance of one of
     *         the supplied classes or null if none found.
     */
    public static XDIMEElementInternal getContainingElement(
            XDIMEElementInternal element, Filter filter) {

        XDIMEElementInternal foundElement = null;
        while (foundElement == null && element != null) {

            if (filter.matches(element)) {
                foundElement = element;
            } else {
                element = element.getParent();
            }
        }

        return foundElement;
    }

    public static String getAttributeValue(final XDIMEContextInternal context,
                                     final XDIMEAttributes attributes,
                                     final String namespace,
                                     final String attrName) {

        // get the value with the specified namespace
        String value = attributes.getValue(namespace, attrName);
        if (value == null) {
            // check default namespace
            final String defaultNamespace =
                context.getExpressionContext().getNamespacePrefixTracker().
                getNamespaceURI("");
            if (namespace.equals(defaultNamespace)) {
                value = attributes.getValue(null, attrName);
            }
        }
        return value;
    }

    /**
     * A filter that can be used to select an element that matches some
     * criteria.
     */
    public static interface Filter {

        /**
         * Check the element to see whether it matches the criteria and
         * therefore should be selected.
         *
         * @param element The element to check.
         * @return True if it matches, false otherwise.
         */
        public boolean matches(XDIMEElementInternal element);

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/2	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 30-Sep-05	9562/2	pabbott	VBM:2005092011 Add XHTML2 Object element

 22-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
