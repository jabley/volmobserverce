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
package com.volantis.synergetics.domvisitor;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Text;

import java.util.Iterator;

/**
 * A visitor-based DOM traversal or "walk" can be executed using an instance of
 * this class and an implementation of the {@link Visitor} interface.
 *
 * <p>{@link org.jdom.Element} nodes in a given DOM tree (or sub-tree) are always
 * visited. However, other types of node can also be visited by utilizing the
 * supplied mask values (such as {@link #ATTRIBUTES}) in the {@link
 * #nextHeader(Element,Visitor,int) nextHeader} method's <code>inclusionMask</code>
 * parameter. The mask values must be ORed together into the
 * <code>inclusionMask</code>.</p>
 *
 * <p>The walker implements the entire DOM traversal independent of the
 * visitor.</p>
 *
 * <p>The traversal can be controlled by the visitor's nextHeader method return
 * values. Normal traversals are performed by returning the {@link
 * Visitor.Action#CONTINUE CONTINUE} literal. Traversal of the "current"
 * element can be terminated early by returning {@link Visitor.Action#SKIP
 * SKIP}, where the current element is the parent of a current non-Element
 * node. The whole traversal can be terminated by returning {@link
 * Visitor.Action#STOP STOP}.</p>
 */
public class Walker {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(Walker.class);

    /**
     * This mask value should be ORed into the inclusions mask to request
     * visitation of attribute nodes.
     */
    public final static int ATTRIBUTES = 1 << 0;

    /**
     * This mask value should be ORed into the inclusions mask to request
     * visitation of text nodes.
     */
    public final static int TEXT = 1 << 1;

    /**
     * Requests a visitation traversal of {@link org.jdom.Element}s only, starting at
     * the given element.
     *
     * @param element the start-point for the traversal
     * @param visitor the visitor to be invoked
     */
    public void visit(Element element, Visitor visitor) {
        visit(element, visitor, 0);
    }

    /**
     * Requests a visitation traversal of {@link org.jdom.Element} and other, selected,
     * node types, starting at the given element.
     *
     * @param element       the start-point for the traversal
     * @param visitor       the visitor to be invoked
     * @param inclusionMask created by ORing the required mask values together
     *                      to define the additional node types that should be
     *                      visited
     */
    public void visit(Element element, Visitor visitor, int inclusionMask) {
        // start the traversal
        if (logger.isDebugEnabled()) {
            logger.debug("Walk started on element " + element + //$NON-NLS-1$
                         " with inclusion mask " + inclusionMask); //$NON-NLS-1$
        }

        visitElement(element, visitor, inclusionMask);
    }

    /**
     * Visits the given element, then if selected visits the element's
     * attributes and finally visits the element's content.
     *
     * <p>The visitation traversal can be curtailed at any point by the visitor
     * returning an {@link Visitor.Action Action} other than {@link
     * Visitor.Action#CONTINUE CONTINUE}. If an attribute or content visitation
     * returns {@link Visitor.Action#SKIP SKIP} then this method will return
     * {@link Visitor.Action#CONTINUE CONTINUE} since this element's container
     * should simply continue its processing.</p>
     *
     * @param element       the element to be visited
     * @param visitor       the visitor to be invoked
     * @param inclusionMask indicates the additional node types to be visited
     * @return the over-all action relating to the current element's traversal
     */
    private Visitor.Action visitElement(Element element,
                                        Visitor visitor,
                                        int inclusionMask) {
        if (logger.isDebugEnabled()) {
            logger.debug("Visiting element " + element); //$NON-NLS-1$
        }

        Visitor.Action action = visitor.visit(element);

        if (action == null) {
            throw new IllegalArgumentException(
                "Visitors must return a non-null Action"); //$NON-NLS-1$
        } else if (action == Visitor.Action.CONTINUE) {
            if ((inclusionMask & ATTRIBUTES) != 0) {
                action = visitAttributes(element, visitor);
            }
        }

        if (action == Visitor.Action.CONTINUE) {
            action = visitContent(element, visitor, inclusionMask);
        }

        if (action == Visitor.Action.SKIP) {
            action = Visitor.Action.CONTINUE;
        }

        return action;
    }

    /**
     * When selected, this method is called to nextHeader an element's attributes.
     *
     * <p>The visitation traversal of the attributes and their containing
     * element can be curtailed by the visitor returning a non-{@link
     * Visitor.Action#CONTINUE CONTINUE} action.</p>
     *
     * @param element the element who's attributes are to be visited
     * @param visitor the visitor to be invoked
     * @return the over-all action relating to the traversal of the attributes
     */
    private Visitor.Action visitAttributes(Element element,
                                           Visitor visitor) {
        Visitor.Action action = Visitor.Action.CONTINUE;
        Iterator i = element.getAttributes().iterator();
        Attribute attribute;

        while ((action == Visitor.Action.CONTINUE) && i.hasNext()) {
            attribute = (Attribute)i.next();

            if (attribute != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Visiting element " + element + "'s " + //$NON-NLS-1$ //$NON-NLS-2$
                                 "attribute " + attribute); //$NON-NLS-1$
                }

                action = visitor.visit(attribute);

                if (action == null) {
                    throw new IllegalArgumentException(
                        "Visitors must return a non-null Action"); //$NON-NLS-1$
                }
            }
        }

        return action;
    }

    /**
     * This method is called to nextHeader an element's content. The content is a
     * mix of node types including but not limited to Elements. Element type
     * content is handled by invoking {@link #visitElement}. Other types of
     * content will only be processed if they are included in the specified
     * <code>inclusionMask</code>.
     *
     * <p>The visitation traversal of the content and their containing element
     * can be curtailed at any point by the visitor returning a non-{@link
     * Visitor.Action#CONTINUE CONTINUE} action.</p>
     *
     * @param element       the element who's content is to be visited
     * @param visitor       the visitor to be invoked
     * @param inclusionMask indicates the additional node types to be visited
     * @return the over-all action relating to the traversal of the content
     */
    private Visitor.Action visitContent(Element element,
                                        Visitor visitor,
                                        int inclusionMask) {
        Visitor.Action action = Visitor.Action.CONTINUE;
        Iterator i = element.getContent().iterator();
        Object content;

        while ((action == Visitor.Action.CONTINUE) && i.hasNext()) {
            content = i.next();

            if (content != null) {
                if (content instanceof Element) {
                    action = visitElement((Element)content,
                                          visitor,
                                          inclusionMask);
                } else if (content instanceof Text) {
                    if ((inclusionMask & TEXT) != 0) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Visiting element " + element + "'s " + //$NON-NLS-1$ //$NON-NLS-2$
                                         "text " + content); //$NON-NLS-1$
                        }

                        action = visitor.visit((Text)content);
                    }
                }

                if (action == null) {
                    throw new IllegalArgumentException(
                        "Visitors must return a non-null Action"); //$NON-NLS-1$
                }
            }
        }

        return action;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Apr-05	7572/2	geoff	VBM:2005040612 Device repository merge report: create event model and XML report listeners

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 17-Dec-03	2226/1	philws	VBM:2003121115 Provide JDOM traversal walker/visitor

 ===========================================================================
*/
