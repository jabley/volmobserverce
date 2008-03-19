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

import java.util.Stack;

import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.xml.xpath.XPath;

/**
 * This class is used to track the XPath during sax event handling
 */
public final class XPathTracker {

    /**
     * @supplierRole path
     * @supplierCardinality 0..*
     * @associates XPathNode
     */
    protected Stack path;

    /**
     * The root node for the tracking operation.
     *
     * @supplierRole root
     * @supplierCardinality 1
     */

    protected Element root;

    /**
     * Initializes a <code>XPathTracker</code> with the given arguments
     *
     * @param root the node for the tracking opertaion
     */
    public XPathTracker(Element root) {
        if (root == null) {
            throw new IllegalArgumentException(
                    "Must have a root node to track"); //$NON-NLS-1$
        }
        this.root = root;
        this.path = new Stack();
    }

    /**
     * Clients should call this method to update the trackers state whenever
     * the start of an element is encountered
     *
     * @param namespaceURI The URI of the namespace associated with the
     *                     element. Should be an empty string if the element belongs to the default
     *                     namespace.
     * @param localName    the name of the element
     * @param qName        the qualified name of the element
     * @throws com.volantis.mcs.xml.xpath.XPathException
     *          if an error occurs
     */
    public void startElement(String namespaceURI,
                             String localName,
                             String qName) throws XPathException {
        if (path.isEmpty()) {
            // this is the first element event. We already now that it is for]
            // the root node.
            path.push(new XPathNode(root));
        } else {
            // retrieve the parent node.
            XPathNode parentNode = (XPathNode) path.peek();

            // work out the child step
            String childStep =
                    parentNode.newChildPath(namespaceURI, localName, qName);

            String prefix = null;
            Namespace ns = null;
            if (namespaceURI != "" && qName != null) { //$NON-NLS-1$
                int i = qName.indexOf(':');
                if (i != -1) {
                    // todo - can we avoid this garbage
                    prefix = qName.substring(0, i);
                    ns = Namespace.getNamespace(prefix, namespaceURI);
                }
            }
            // work out the path from the root node to this child element
            XPath childXPath = new XPath(parentNode.getXPath(),
                    childStep,
                    new Namespace[]{ns});

            // Obtain the element by assuming that the document is
            // being parsed in document order and getting the next
            // child from the parent. This significantly improves
            // performance over using XPath selectSingleElement.
            Element child = parentNode.getNextChild();

            if (!child.getName().equals(localName)) {
                throw new IllegalStateException("Unexpected child retrieved" +
                        " from XPathNode. Expected \"" +
                        localName + "\" but got \"" + child.getName() + "\"");
            }

            // store away an XPathNode for this element
            path.push(new XPathNode(child, childXPath));
        }
    }

    /**
     * Clients should call this method to update the trackers state whenever
     * the end of an element is encountered
     *
     * @param namespaceURI The URI of the namespace associated with the
     *                     element. Should be an empty string if the element belongs to the default
     *                     namespace.
     * @param localName    the name of the element
     * @param qName        the qualified name of the element
     */
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) {
        // pop the current XPathNode from the stack
        XPathNode node = (XPathNode) path.pop();

        // dispose of the nodes resources.
        node.dispose();
    }

    /**
     * Returns the relative XPath relevant at the point during event processing
     * when the method is called. This will return null when processing the
     * root element.
     *
     * @return the relative XPath relevant at the point during event processing
     *         when the method is called. This will return null when processing the
     *         root element.
     */
    public XPath currentXPath() {
        XPathNode node = (XPathNode) path.peek();

        return node.getXPath();
    }

    /**
     * Returns the relative Element relevant at the point during event processing
     * when the method is called.
     *
     * @return the relative Element relevant at the point during event processing
     *         when the method is called. 
     */
    public Element currentElement() {
        XPathNode node = (XPathNode)path.peek();

        return node.getElement();
    }

    /**
     * Returns true if the tracker has no internal tracking state.
     *
     * @return true iff the tracker has no internal tracking state.
     */
    public boolean isEmpty() {
        return path.isEmpty();
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
