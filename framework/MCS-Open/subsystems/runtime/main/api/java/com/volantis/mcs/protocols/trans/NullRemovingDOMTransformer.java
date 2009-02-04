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
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;

/**
 * Transforms a Document by removing all null named elements, and moving their
 * children to their parent element.
 */
public class NullRemovingDOMTransformer implements DOMTransformer {

    // Javadoc inherited.
    public Document transform(DOMProtocol protocol, Document document) {
        NullRemover nullRemover = new NullRemover();
        nullRemover.removeNullElements(document);
        return document;
    }

    /**
     * {@link NodeIteratee} implementation which removes all null named
     * elements from a {@link Document}.
     */
    public static class NullRemover extends RecursingDOMVisitor {

        /**
         * This method removes any null named elements and adds their children to
         * the parent's list of children, at the point in the list that the null
         * named element was.
         *
         * @param document from which to remove null named elements
         */
        public void removeNullElements(Document document) {
            document.accept(this);
        }

        // Javadoc inherited.
        public void visit(Document document) {
            document.forEachChild(this);
        }

        public void visit(Element element) {
            
            // if so iterate through its children
            element.forEachChild(this);
            
            // and if its name is null
            if (element.getName() == null) {
                // then remove it and add its children to its parent
                Element parent = element.getParent();
                final Node previous = element.getPrevious();
                if (previous != null) {
                    // it wasn't the first child, so add its children after
                    // the previous element
                    element.insertChildrenAfter(previous);
                } else {
                    // it was the first child, so add the children to the
                    // front of the parent's list of children
                    element.addChildrenToHead(parent);
                }
                element.remove();
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 ===========================================================================
*/
