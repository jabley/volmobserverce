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

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;

import java.util.Iterator;
import java.util.List;

import com.volantis.mcs.xml.xpath.XPath;


/**
 * Used to store information about the current position in a document during
 * an event-based traversal (at the element level).
 */
public final class XPathNode {

    /**
     * The element found at the current position in the document.
     */
    private Element element;

    /**
     * The XPath of the element relative to the start point of the traversal.
     * This should be null for the root node.
     */
    private XPath xpath;

    /**
     * Element children
     */
    private Iterator elementChildren;

    /**
     * Object used to manage the predicate allocation for child nodes.
     * @supplierRole children
     * @supplierCardinality 0..1
     */
    private XPathChildren children;

    /**
     * Initializes a <code>XPathNode</code> with the given parameter.
     * @param root the root {@link Element}. Cannot be null
     * @throws IllegalArgumentException if the element argument is null.
     */
    public XPathNode(Element root) {
        this(root, null);
    }

    /**
     * Initializes a <code>XPathNode</code> with the given parameters.
     * @param element the {@link Element}. Cannot be null
     * @param xpath The XPath of the element relative to the start point
     * of the traversal.
     * @throws IllegalArgumentException if the element argument is null.
     */
    public XPathNode(Element element,
                     XPath xpath) {
        if (element == null) {
            throw new IllegalArgumentException(
                    "element argument cannot be null"); //$NON-NLS-1$
        }
        this.element = element;
        this.xpath = xpath;
    }


    /**
     * Get the next element child while iterating over the
     * element children.
     * @return the next element child in document order in an
     * iteration of all the element children
     */
    public Element getNextChild() {
        if(elementChildren==null) {
            elementChildren = element.getChildren().iterator(); // AMB
        }
        return (Element) elementChildren.next();
    }

    /**
     * The path step required to identify the specified child is returned by
     * this method. This handles predication.
     * @param namespaceURI the namespace associated with the child element
     * @param localName the name of the child element
     * @param qName the qualified name (prefixed) of the element
     */
    public String newChildPath(String namespaceURI,
                               String localName,
                               String qName) {
        String path;
        boolean needsPredicate = true;

        // lazily create the XPathChildren instance
        if (children == null) {
            children = new XPathChildren();
        }

        // ask the XPathChildren instance to allocate a predicate value
        // for this node.
        int predicate = children.newChildPredicate(localName, namespaceURI);

        if (predicate == 1) {
            // check to see if the predicate is actually needed.
            // retrieve the namespace associated with the child
            Namespace ns = Namespace.getNamespace(namespaceURI);
            // retrieve the children with the given name and namespace.
            // Unfortunately this generates unavoidable garbage.
            List children = element.getContent(new ElementFilter(localName,
                                                                 ns));
            // only need the predicate if there is more than one child
            needsPredicate = children.size() > 1;

        }

        // calculate the path string. Note we use the qName (namespace prefixed
        // name).
        if (needsPredicate) {
            // Initial size allows for a 3 digit predicate
            StringBuffer buffer = new StringBuffer(qName.length() + 5);
            // append predicate
            buffer.append(qName).append('[').append(predicate).append(']');
            path = buffer.toString();
        } else {
            // no predicate needed
            path = qName;
        }
        return path;
    }

    /**
     * Return the {@link Element} for this node
     * @return the Element for this node
     */
    public Element getElement() {
        return element;
    }

    /**
     * Return the {@link com.volantis.mcs.xml.xpath.XPath} for this node
     * @return the XPath for this node or null if this node is the root node
     */
    public XPath getXPath() {
        return xpath;
    }

    /**
     * Deallocates any resources.
     */
    public void dispose() {
        element = null;
        xpath = null;
        // ask the XPathChildren instance to dispose.
        if (children != null) {
            children.dispose();
        }
        children = null;
        elementChildren = null;
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

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 09-Dec-03	2057/5	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 ===========================================================================
*/
