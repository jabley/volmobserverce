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
 * $Header: /src/voyager/com/volantis/mcs/dom/Document.java,v 1.5 2002/05/23 09:49:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 02-Apr-02    Paul            VBM:2002021802 - Added release method to
 *                              release the contents of the document.
 * 26-Apr-02    Paul            VBM:2002042205 - Added visitContents method.
 * 23-May-02    Paul            VBM:2002042202 - Added getContentRoot method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.impl;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.DOMVisitor;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.XMLDeclaration;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.Comment;

/**
 * The root of the dom.
 */
public class DocumentImpl implements Document {

    /**
     * Some user data associated with the document.
     */
    Object object;

    /**
     * This element contains all the contents of the document.
     */
    ElementImpl contents;

    private DocType docType;

    private XMLDeclaration declaration;

    public DocumentImpl(DOMFactory factory) {
        contents = (ElementImpl)factory.createElement();
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void addNode(Node node) {
        contents.addTail(node);
    }

    public Node getContents() {
        return contents.getHead();
    }

    public Element getContentRoot() {
        return contents;
    }

    public Element getRootElement() {

        Element root = null;
        for (Node node = contents.getHead(); node != null;
             node = node.getNext()) {
            if (node instanceof Element) {
                Element element = (Element) node;
                if (root == null) {
                    root = element;
                } else {
                    throw new IllegalStateException(
                            "Document cannot have multiple elements at the " +
                                    "root, found " + root.getName() + " and " +
                                    element.getName());
                }
            } else if (node instanceof Text) {
                Text text = (Text) node;
                if (!text.isWhitespace()) {
                    throw new IllegalStateException(
                            "Document cannot have non whitespace text node " +
                                    "at the root, found {" +
                                    new String(text.getContents(), 0,
                                            text.getLength()) +
                                    "}");
                }
            } else if (node instanceof Comment) {
                // Ignore comments at the top level.
            } else {
                throw new IllegalStateException("Unknown node type: " + node);
            }
        }
        
        return root;
    }


    public void forEachChild(NodeIteratee iteratee) {
        contents.forEachChild(iteratee);
    }

    void reset() {
        object = null;
        contents = null;
    }

    // Javadoc inherited.
    public void accept(DOMVisitor visitor) {
        visitor.visit(this);
    }

    // Javadoc inherited.
    public void setDocType(DocType docType) {
        this.docType = docType;
    }

    // Javadoc inherited.
    public DocType getDocType() {
        return docType;
    }

    // Javadoc inherited.
    public void setDeclaration(XMLDeclaration declaration) {
        this.declaration = declaration;
    }

    // Javadoc inherited.
    public XMLDeclaration getDeclaration() {
        return declaration;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/1	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
