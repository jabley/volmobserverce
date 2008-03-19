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

package com.volantis.mcs.dom;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface Document {
    
    /**
     * Set the object.
     *
     * @param object The object.
     */
    void setObject(Object object);

    /**
     * Get the object.
     *
     * @return The object.
     */
    Object getObject();

    void addNode(Node node);

    Node getContents();

    /**
     * Get the fake element that encapsulates all the content for the
     * document.
     *
     * @return The fake element that encapsulates all the content for the
     * document.
     * @deprecated Use one of the other methods.
     */
    Element getContentRoot();

    /**
     * Get the root element.
     *
     * <p>The root element is the element that is the root
     *
     * @return The root element.
     */
    Element getRootElement();

    /**
     * Visit the nodes which constitute the content of this document in order.
     * <p>
     * This method saves a reference to the next child before calling the
     * current child's visit method. This allows that method to modify the
     * list with consistent behaviour.
     * </p>
     * <p>
     * If the child's visit method modifies the list then any new nodes added
     * before the next node will not be visited but any new nodes added after
     * the next node will be visited.
     * </p>
     *
     * @param iteratee The object which is visiting this document.
     */
    void forEachChild(NodeIteratee iteratee);

    /**
     * An "accept" method for objects which may visit the node.
     * (see GOF book).
     */
    void accept(DOMVisitor visitor);

    /**
     * Set the doc type for this document.
     *
     * @param docType The DocType.
     */
    void setDocType(DocType docType);

    /**
     * Get the document type.
     *
     * @return The document type.
     */
    DocType getDocType();

    void setDeclaration(XMLDeclaration declaration);

    XMLDeclaration getDeclaration();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/1	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 30-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 ===========================================================================
*/
