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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.AbstractTransformingVisitor;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMVisitorBasedTransformer;
import com.volantis.mcs.protocols.TransformingVisitor;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;

/**
 * Transformer for iMode devices that do not support tables and so instead
 * have to emulate them. This transformer will remove tables from the
 * markup and replace them with the iMode table emulation markup.
 */
public final class HTML_iModeTableEmulationTransformer
        extends DOMVisitorBasedTransformer {

    /**
     * This transformer is dedicated to removing all nested tables
     * (irrespective of what has been indicated in the layout).
     */
    protected static final class NestedTableRemover
            extends HTML_iModeUnabridgedTransformer {

        public NestedTableRemover(TransformationConfiguration configuration) {
            super(new HTML_iModeUnabridgedTransformer.HTML_iModeTransFactory(
                    configuration));
        }
    }

    /**
     * This transformer is dedicated to removing the top level tables that have
     * been left behind by the nested table remover.
     */
    final static class TransformVisitor
            extends AbstractTransformingVisitor {

        private final DOMFactory factory;

        private Element containingElement;

        /**
         * Initialise.
         * 
         * @param factory The factory to use to create nodes.
         */
        public TransformVisitor(DOMFactory factory) {
            this.factory = factory;
        }

        /**
         * This is where all the action happens and it is pretty
         * straight forward. If the element is a table and the
         * containingElement is null, then the table element is not
         * nested so turn the element into a "p" and make it the
         * containingElement. If element is a table and containingElement
         * is not null then the table element is nested so remove it by setting
         * its name to null.
         *
         * <p>If the element is a tr then remove it and insert a br after
         * it.</p>
         *
         * <p>If the element is a td then remove it and insert a space after
         * it.</p>
         *
         * @param element The current Element being visited.
         * @return true to continue visiting; false otherwise.
         */
        public void visit(Element element) {

            Element containingElement = this.containingElement;

            // Determine if the element has content because if it does not
            // then we can remove it instead of visiting all its children.
            boolean hasContent = true;
            if ("table".equals(element.getName())) {
                hasContent = tableElementHasContent(element);
                if (hasContent) {
                    if (containingElement != null) {
                        element.setName(null);
                    } else {
                        element.setName("p");
                        containingElement = element;
                        preserveOnlyAlignAttribute(element);
                    }
                }
            } else if ("tr".equals(element.getName())) {
                hasContent = tableElementHasContent(element);
                if (hasContent) {
                    if (element.getAttributeValue("align") != null) {
                        element.setName("div");
                        preserveOnlyAlignAttribute(element);
                    } else {
                        element.setName(null);
                        Element br = factory.createElement();
                        br.setName("br");
                        br.insertAfter(element);
                    }
                }
            } else if ("td".equals(element.getName()) ||
                    "th".equals(element.getName())) {
                hasContent = tableElementHasContent(element);
                if (hasContent) {
                    if (element.getAttributeValue("align") != null) {

                        // This may result in strange behaviour when a table
                        // row only has align set on some of its cells.
                        // Because div elements are rendered vertically
                        // (using br elements as line breaks), the cells will
                        // not be vertically aligned if different alignments
                        // have been specified or not all cells have alignments.
                        // It is better to specify alignments on table rows for
                        // iMode rather than on cells.
                        element.setName("div");
                        preserveOnlyAlignAttribute(element);
                    } else {
                        element.setName(null);
                        Text space = factory.createText();
                        space.append(" ");
                        space.insertAfter(element);
                    }
                }
            }

            boolean visitChildren = hasContent;
            if (!hasContent) {
                element.remove();
            }

            if (visitChildren) {
                Element savedContainingElement = this.containingElement;
                this.containingElement = containingElement;
                element.forEachChild(this);
                this.containingElement = savedContainingElement;
            }
        }

        /**
         * Helper method which removes all attributes from an element except
         * for any "align" attribute.
         *
         * @param element the element of interest
         */
        private void preserveOnlyAlignAttribute(Element element) {
            if (element.hasAttributes()) {
                Attribute attribute = element.getAttributes();
                while (null != attribute) {
                    final String attrName = attribute.getName();
                    if (!"align".equals(attrName)) {
                        element.removeAttribute(attrName);
                    }
                    attribute = attribute.getNext();
                }
            }
        }

        /**
         * Determine whether or not a table element (table, tr, th, td) has
         * content. This will be true if there are any td or th elements that
         * have content.
         * @param tableElement the table element
         * @return true if the table element has content; false otherwise.
         */
        private boolean tableElementHasContent(Element tableElement) {
            boolean hasContent = false;
            if ("table".equals(tableElement.getName())) {
                // Iterate through the rows to look for content.
                Node child = tableElement.getHead();
                while (child != null && !hasContent) {
                    if (child instanceof Element) {
                        hasContent = tableElementHasContent((Element) child);
                    }
                    if (!hasContent) {
                        child = child.getNext();
                    }
                }

            } else if ("tr".equals(tableElement.getName())) {
                // Iterate through the cells to look for content.
                Node child = tableElement.getHead();
                while (child != null && !hasContent) {
                    if (child instanceof Element) {
                        hasContent = tableElementHasContent((Element) child);
                    }
                    if (!hasContent) {
                        child = child.getNext();
                    }
                }
            } else if ("td".equals(tableElement.getName()) ||
                    "th".equals(tableElement.getName())) {

                // Iterate through the table children to look for content.
                Node child = tableElement.getHead();
                while (child != null && !hasContent) {
                    if (child instanceof Element) {
                        hasContent = true;
                    } else {
                        hasContent = ((Text) child).getLength() > 0;
                    }
                    child = child.getNext();
                }
            } else {
                // Non table, tr, td and th elements are considered to have
                // content.
                hasContent = true;
            }

            return hasContent;
        }
    }

    /**
     * Remove nested tables and then remove tables.
     */
    public Document transform(DOMProtocol protocol,
                              Document document) {

        NestedTableRemover tableRemover = new NestedTableRemover(
                protocol.getProtocolConfiguration());
        tableRemover.transform(protocol, document);

        return super.transform(protocol, document);
    }


    /**
     * Get a DOMVisitor that will do the table tranformation.
     * @param protocol The DOMProtocol - used for allocating new
     * Element and Text nodes where necessary.
     * @return The DOMVisitor that will emulate tables for iMode.
     */
    protected TransformingVisitor getDOMVisitor(DOMProtocol protocol) {
        return new TransformVisitor(protocol.getDOMFactory());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9184/2	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 22-Jun-05	8483/4	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 01-Jun-05	8606/1	pcameron	VBM:2005052409 Fixed align attribute for table rendering in iMode

 01-Jun-05	8604/7	pcameron	VBM:2005052409 Fixed align attribute for table rendering in iMode

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Apr-04	3867/1	allan	VBM:2004040615 Patched empty table, tr, td handling from GA line.

 14-Apr-04	3820/4	allan	VBM:2004040615 Remove obselete table, tr, and td elements.

 12-Oct-03	1540/4	allan	VBM:2003101101 Fixes for implementation review

 12-Oct-03	1540/2	allan	VBM:2003101101 Add emulated and native table support on HTML_iMode

 ===========================================================================
*/
