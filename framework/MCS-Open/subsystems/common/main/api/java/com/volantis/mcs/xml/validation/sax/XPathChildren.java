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
package com.volantis.mcs.xml.validation.sax;

import java.util.List;
import java.util.ArrayList;

/**
 * Used to calculate the predicate that should be assigned to a child element
 * when determining its XPath.
 */
public class XPathChildren {
   /**
     * @supplierRole children
     * @supplierCardinality 0..*
     * @associates XPathChild
     */
    protected List children;

    /**
     * Used to perform lookup in the children list for a named child of a given
     * namespace
     */
    private XPathChild lookupChild;

    /**
     * The given named child is added to this set of children. If the child
     * already exists, that existing entry's predicate is updated
     * otherwise a new entry is added. The entry is queried for the predicate
     * which is then returned.
     * @param name the name of the child element
     * @param namespaceURI the URI of the namespace that the child belongs to.
     * @return the predicate associated with the child element.
     */
    public int newChildPredicate(String name, String namespaceURI) {
        int predicate;

        if (children == null) {
            children = new ArrayList();
            // this is the first child.
            children.add(new XPathChild(name, namespaceURI));
            predicate = 1;
        } else {
            if (lookupChild == null) {
                lookupChild = new XPathChild(name, namespaceURI);
            } else {
                lookupChild.setName(name);
                lookupChild.setNamespaceURI(namespaceURI);
            }
            // find out how many children in the same namespace with the same
            // name have been encountered so far.
            int index = children.indexOf(lookupChild);
            if (index == -1) {
                // this is the first so create a child for this name/namespacURI
                children.add(new XPathChild(name, namespaceURI));
                predicate = 1;
            } else {
                // retrieve the child so that we can query its predicate
                XPathChild child = (XPathChild)children.get(index);
                // increment the child count for this name & namespace
                child.incrementCount();
                predicate = child.getCount();
            }
        }
        // return the suggested predicate
        return predicate;
    }

    /**
     * Cleans up an allocated resources.
     */
    public void dispose() {
        if (children != null) {
            children.clear();

            children = null;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Dec-03	2057/3	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 ===========================================================================
*/
