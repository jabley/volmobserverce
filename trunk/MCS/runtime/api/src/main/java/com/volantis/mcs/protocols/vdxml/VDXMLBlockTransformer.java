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
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.wml.WMLConstants;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Shamelessly ripped off from WMLDOMTransformer to do exactly the same thing
 * for block elements as was done there for p elements - i.e. promote them
 * up to the "top" level (in this case the TEXTE level).
 * <p>
 * This was the quickest, dirtiest way to do this at the time. Sorry!
 */
public class VDXMLBlockTransformer
        implements WMLConstants {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(VDXMLBlockTransformer.class);


    private Element parent;

    /**
     * Transforms the element passed in.
     *
     * @param factory The DOMFactory used to create nodes in the Document.
     * @param element The element to be transformed.
     */
    public void transform(DOMFactory factory, Element element) {

        this.parent = element;
        transformChildren(factory, element);
    }

    private void transformElement(
            DOMFactory factory,
            Element element) {
        String name = element.getName();

        if (VDXMLConstants.PSEUDO_BLOCK_ELEMENT.equals(name)) {
            promoteBlock(factory, element);
        }

        transformChildren(factory, element);
    }

    private void transformChildren(
            DOMFactory factory,
            Element element) {

        Node next;
        Node child = element.getHead();
        for (; child != null; child = next) {
            next = child.getNext();
            transformNode(factory, child);
        }
    }


    private void transformNode(DOMFactory factory, Node node) {
        if (node instanceof Element) {
            transformElement(factory, (Element) node);
        } else if (node instanceof Text) {
            transformTextNode();
        } else {
            throw new IllegalStateException("Unknown node: " + node);
        }
    }

    private boolean canSplitElement() {
        return true;
    }

    /**
     * Transform the text node.
     */
    private void transformTextNode() {
    }

    /**
     * Promote a node.
     * <p>
     * This makes sure that it is valid to split the parent element before
     * promoting the node.
     * </p>
     *
     * @param node The node to promote.
     */
    private boolean promoteNode(Node node, boolean trimEmptyNodes) {

        Element parent = node.getParent();

        // Make sure that it makes sense to split our parent.
        if (!canSplitElement()) {
            throw new IllegalStateException("Cannot split " + parent);
        }

        return node.promote(trimEmptyNodes);
    }

    private boolean isValidParentForBlock(Element parent) {
        return (this.parent == parent);
    }

    /**
     * Move the specified block node up the tree until it is a child
     * of a card node, or a child of a dissectable node.
     *
     * @param element The block node.
     */
    private void promoteBlock(
            DOMFactory factory,
            Element element) {

        // If this element has no children then discard it.
        if (element.isEmpty()) {
            element.remove();

            return;
        }

        Element parent = element.getParent();
        while (true) {
            String parentName = parent.getName();

            // If the parent is a valid parent for a block then stop.
            if (isValidParentForBlock(parent)) {
                return;
            }

            // We need to promote this block. We do this by splitting our
            // parent node into two nodes, the first one contains all our preceding
            // siblings and the other one contains all our following siblings. We
            // then insert this block node between them.
            //
            // If the parent is a block node then we need to merge its
            // attributes into this block node and then discard it.
            // e.g.
            //            C
            //            |                        C
            //       ... P01 ...                _-~|~-_
            //         /  |  \       -->  ... P01 P03 P01 ...
            //       T02 P03 T05               |   |   |
            //            |                   T02 T04 T05
            //           T04
            //
            // If the parent is not a block node then it needs to swap position
            // with this block node.
            // e.g.
            //            C                        C
            //            |                     _-~|~-_
            //       ... B01 ...          ... B01 P03 B01 ...
            //         /  |  \       -->       |   |   |
            //       T02 P03 T05              T02 B01 T05
            //            |                        |
            //           T04                      T04
            //
            //

            if (logger.isDebugEnabled()) {
                logger.debug("Promoting element " + element);
            }

            // Move this node up, splitting out parent, remember whether the
            // parent element can be discarded.
            // NOTE: this also trims any Ps created which contain only whitespace.
            boolean discardParent = promoteNode(element, true);

            if (VDXMLConstants.PSEUDO_BLOCK_ELEMENT.equals(parentName)) {
                // Merge the attributes from the parent block node into this
                // one, existing values are not changed.
                element.mergeAttributes(parent, false);
            } else {
                // Reuse the parent node if we can but otherwise create a clone of
                // it and then insert it as the parent of this element's children.
                Element child;
                if (discardParent) {
                    child = parent;
                    parent = null;
                    discardParent = false;
                } else {
                    child = factory.createElement();
                    child.copy(parent);
                }

                element.addChildrenToTail(child);

                element.addTail(child);
            }

            // Move up the tree.
            parent = element.getParent();
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 08-Jun-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support (javadoc and make rendering clearer)

 28-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support (block element support)

 ===========================================================================
*/
