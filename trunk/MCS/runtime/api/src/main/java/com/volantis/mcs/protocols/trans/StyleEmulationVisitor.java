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
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.debug.DebugDocument;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AbstractTransformingVisitor;
import com.volantis.shared.stack.ArrayListStack;
import com.volantis.shared.stack.Stack;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Iterator;

/**
 * The style emulation visitor used to visit the DOM tree nodes and transform
 * them if necessary.
 *
 * The algorithm transforms a DOM tree by:<p>
 *
 * <ul>
 *
 * <li>removing redundant stylistic elements and/or anti-elements</li>
 *
 * <li>if stylistic elements break any containment rules they should be
 * recursively pushed into their children until the containment rules aren't
 * broken or the stylistic element is illegal or not required.</li>
 *
 * <li>pulling anti-elements up to the same level as their open counterpart
 * element and then removing the anti-element.</li>
 *
 * <li>removing stylistic elements from indivisible elements that don't permit
 * them as direct children (once again the stylistic elements are pushed down
 * into the indivisible element's children).</li>
 *
 * <li>merging mergeable elements such as font when one contains another (care
 * should be taken not to push a mergeable element through another mergeable
 * element since the lower one takes precendence over the higher one in the
 * tree).</li>
 *
 * <li>ensuring that stylistic elements may be pushed through anti-elements if
 * and only if the anti-element is not a its counterpart (e.g. b cannot be
 * pushed through anti-b, but can be pushed through anti-u)</li>
 *
 * <li>mulitply nested style elements are all pushed down of an element that
 * should not contain it (e.g. table contain b which contains i which contains
 * font should result in b, i, font all pushed down).</li>
 *
 * </ul>
 *
 *
 * <p>The algorithm always favours pushing stylistic elements down into the
 * tree as deep as possible. In certain circumstances anti-elements may also be
 * promoted up recursively (whilst ensuring the promotion itself doesn't split
 * indivisible elements along the way). Stylistic elements are always tracked
 * (open and close) using a tracker to ensure that redundant styles may be
 * discarded (bold within a bold, etc.). If a stylistic element contains an
 * element it shouldn't it is pushed down to all its children.</p>
 *
 * <p>General algorithm:</p>
 *
 * <ol>
 *
 * <li> If the element being visited is an indivisible element: </li>
 *
 * <ul><li>process the indivisible element (this should catch most
 * elements)</li></ul>
 *
 * <li> If the element being visited is a stylistic element: </li>
 *
 * <ul>
 *
 * <li>process the stylistic element (b, u, i, etc.)</li> </ul>
 *
 * <li> If the element being visited is an anti-element: </li> <ul>
 *
 * <li>process the anti-element (anti-b, anti-u, anti-i, etc.)</li> </ul>
 *
 * <li>else do nothing (all elements should already be processed)</li> </ol>
 *
 *
 * <p>Example of containment rule enforcement</p>
 *
 * <p>We need to ensure that the containment rules are not broken whilst
 * processing elements. This is done by testing to see if the current element
 * is contained by a style element that shouldn't contain this other
 * element.</p>
 *
 * <p>If no such element exists we should continue processing otherwise we need
 * to push any elements that shouldn't contain me, down into me.</p>
 *
 * For example.
 *
 * <pre>
 *         p                           p
 *         |                         /  \
 *         b                       b    (p)
 *       /  \                      |    /  \
 *     t1   (p)         ->         t1  b    b
 *          / \                        |    |
 *        t2    u                      t2   u
 *              |                           |
 *              t3                          t3
 *
 * where (p) is the current element being processed and b shouldn't contain p.
 * b would be pushed down into p.
 *
 * </pre>
 *
 * @see StyleEmulationTransformer
 */
public final class StyleEmulationVisitor
        extends AbstractTransformingVisitor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(StyleEmulationVisitor.class);

    // TODO: move these into css emulator package

    /**
     * Constant declaration for the anti bold element.
     */
    public static final String ANTI_BOLD_ELEMENT = "ANTI-B";

    /**
     * Constant declaration for the anti italic element.
     */
    public static final String ANTI_ITALICS_ELEMENT = "ANTI-I";

    /**
     * Constant declaration for the anti underline element.
     */
    public static final String ANTI_UNDERLINE_ELEMENT = "ANTI-U";

    /**
     * Constant declaration for the anti strike element.
     */
    public static final String ANTI_STRIKE_ELEMENT = "ANTI-STRIKE";

    /**
     * Constant declaration for the anti strike element.
     */
    public static final String ANTI_SIZE_ELEMENT = "ANTI-SIZE";

    /**
     * Constant declaration for the anti blink element.
     */
    public static final String ANTI_BLINK_ELEMENT = "ANTI-BLINK";

    /**
     * A marker for marking a DOM node as visited.
     */
    private static final Object VISITED = new Object();

    /**
     * Store the DOM factory so that we can allocate elements.
     */
    private final DOMFactory domFactory;
    private StyleEmulationElementTracker tracker;

    /**
     * The style configuration associated with this visitor.
     */
    private final StyleEmulationElementConfiguration styleCfg;

    /**
     * The document that we are visiting, or null if not set.
     */
    private Document document;

    /**
     * Construct the visitor with the protocol.
     *
     * @param factory            the DOM factory used for element allocation.
     * @param styleConfiguration
     * @param tracker
     */
    public StyleEmulationVisitor(
            DOMFactory factory,
            StyleEmulationElementConfiguration styleConfiguration,
            StyleEmulationElementTracker tracker) {
        this.styleCfg = styleConfiguration;
        this.domFactory = factory;
        this.tracker = tracker;
    }

    // javadoc inherited
    public void visit(Text text) {
        if (text.getObject() == null) {
            markAsVisited(text);
        }
    }

    // javadoc inherited
    public void visit(Element element) {

        boolean terminateTraversal = false;

        // Debug that we are starting to process the current element.
        debugNode(element, "Started");

        // Only visit this element if it has not already been visited.
        if (element.getObject() != VISITED) {

            // Log the entire dom and current element every time through here.
            // This makes it *much* easier to find bugs in here.
            if (logger.isDebugEnabled()) {
                DebugDocument debug = new DebugDocument();
                if (document != null) {
                    logger.debug("ENTIRE DOM:\n" + debug.debug(document));
                }
                logger.debug("THIS ELEMENT:\n" + debug.debug(element));
            }

            // We mark this element as visited so that we know not to re-visit
            // it later if the DOM tree changes considerably and causes the
            // traversal to backtrack on itself.
            markAsVisited(element);

            final String elementName = element.getName();
            if (styleCfg.isIndivisibleElement(elementName)) {
                terminateTraversal = processIndivisibleElement(element);

            } else if (styleCfg.isStylisticElement(elementName)) {
                terminateTraversal = processStylisticElement(element, tracker);

            } else if (styleCfg.isAntiElement(elementName)) {
                processAntiElement(element, tracker);

            } else if (styleCfg.isDivisibleStyleElement(elementName)) {
                element.forEachChild(this);

            } else {
                throw new IllegalStateException(
                        "Unhandled element: '" + elementName + "'");
            }
        }

        // Debug that we are finished processing the current element.
        debugNode(element, "Finished");

        if (terminateTraversal) {
            skipSiblings();
        }
    }

    public void visit(Document document) {
        this.document = document;
        super.visit(document);
    }

    /**
     * <p>Process the indivisible element by checking to see if this
     * indivisible element permits the containment of stylistic elements. If
     * not, any stylistic elements are pushed down and then removed.</p>
     *
     * <p>Otherwise a check is made to to see if any [grand]*)parent of the
     * indivisible element is a stylistic element and whether any child of the
     * indivisible element is the counterpart (anti-xxx element) for that
     * element. If so, we may promote the indivisible element to the same level
     * as its open counterpart.</p>
     *
     * @param element the indivisible element that will be processed.
     * @return true if the current traversal should terminate, false
     *         otherwise.
     */
    private boolean processIndivisibleElement(Element element) {

        if (logger.isDebugEnabled()) {
            logger.debug("Processing the indivisible element: " +
                    element);
        }
        // Ensure that this indivisible element doesn't break the containment
        // rules.
        //
        // The rules may be broken if my parent is a stylistic element and
        // doesn't permit me, or if I don't permit the containment of stylistic
        // elements.
        //
        // For example in HTML v3.2. if this indivisible element is a table
        // element then its parent shouldn't be the b, u, etc. element.
        //
        // If this containment is violated then pull the stylistic element
        // into me. Continue until valid.
        Element parent = element.getParent();
        if (parent != null) {
            String parentName = parent.getName();
            while (styleCfg.isStylisticElement(parentName) &&
                    !styleCfg.isContainmentPermitted(parentName,
                            element.getName())) {
                pushElementDownToAllChildren(parent,
                        parent.getHead(),
                        true, null);
                removeElement(parent);
                tracker.closed(parentName);
                parent = element.getParent();  // re-evaluate my parent.
                parentName = parent.getName();
            }
        }

        if (!styleCfg.isStylePermittedInElement(element.getName())) {
            // Recursively push any style elements that occur as children of
            // the indivisible element into any children. If a child doesn't
            // exist, or doesn't permit style elements then the style
            // element(s) will be removed.
            if (logger.isDebugEnabled()) {
                logger.debug("The indivisible element (" + element.getName() +
                        ") doesn't allow style elements.");
            }
            // NOTE: This deliberately doesn't remove any [great]*grandchildren
            //       style elements since they may be permitted as children
            //       of this element's children.
            pushStyleElementsDown(element);
        } else {
            // Need to examine the children of the indivisible element to see
            // if it contains any anti-xxx elements that have an open
            // counterpart. If one is found we need to push the open
            // counterpart into the indivisible element and promote and then
            // remove the anti-element.
            Node x = element.getHead();
            while (x != null) {
                boolean removed = false;
                if (x instanceof Element) {
                    Element childElement = (Element) x;
                    String child = childElement.getName();

                    boolean pushedDown = false;

                    if (styleCfg.isAntiElement(child)) {
                        // Found an anti-element...push any open stylistic elements
                        // into this indivisible element.
                        Iterator stylisticElements = styleCfg.
                                getCounterpartElementNames(child);

                        while (stylisticElements.hasNext()) {
                            String stylisticElement =
                                    (String) stylisticElements.next();
                            if (tracker.isOpen(stylisticElement)) {
                                // This child is an anti-element and its
                                // counterpart is open. We need to push the
                                // counterpart into the  indivisible element.
                                pushCounterpartElementDown(element,
                                        stylisticElement);
                                pushedDown = true;
                                tracker.closed(stylisticElement);
                            }
                        }
                        if (pushedDown) {
                            // Promote and then remove anti-element.
                            childElement.promote(true);
                            removeElement(childElement);
                            removed = true;

                            // I have had an anti-element that has been
                            // promoted. The dom tree may have changed
                            // so we should terminate the traversal here.
                            // terminateTraversal = true;
                        } else {
                            // anti-element may be removed (but take care
                            // since we still want to continue this
                            // iteration.
                            removeElement(childElement);
                            removed = true;
                        }
                        // Start the traversal from the beginning again
                        // because we have promoted and/or removed a child
                        // element. Doing this is guarenteed to be fail-safe.
                        x = element.getHead();

                    } else {
                        // It is not an anti-element but its children may
                        // be or their children may be so we need to ensure
                        // that they are also visited.
                        visit(childElement);
                    }
                }
                if (!removed && (x != null)) {
                    x = x.getNext();
                }
            }
        }

        element.forEachChild(this);
        return false;
    }

    /**
     * Push stylistic elements that are not permitted to be children of an
     * element into the children, if possible.
     *
     *
     * <pre>
     *             :                           :
     *           table*                      table
     *           /  \                        /  \
     *         td    b         ->          td   td
     *         |     |                      |    |
     *        t1     td                    t1    b
     *               |                           |
     *               t2                          t2
     *
     *  where table is the current element being processed and table shouldn't
     *  contain b. b would be pushed down into the rightmost td.
     *
     * </pre>
     *
     * @param element the element that will be used to push any of its style
     *                elements down
     */
    protected void pushStyleElementsDown(Element element) {
        if (element != null) {
            Node x = element.getHead();
            boolean modified;
            while (x != null) {
                modified = false;
                if (x instanceof Element) {
                    final Element child = ((Element) x);
                    String name = child.getName();

                    if (styleCfg.isAntiElement(name)) {
                        modified = true;
                        pushStyleElementsDown(child);

                    } else if (styleCfg.isStylisticElement(name)) {
                        pushStyleElementsDown(child);
                        pushElementDownToAllChildren(child, child.getHead(),
                                false, null);
                        modified = true;
                    }
                    if (modified) {
                        x = x.getNext();
                        removeElement(child);
                    }
                }
                // We need to check this since x may have been removed and
                // x was the only child.
                if (!modified && (x != null)) {
                    x = x.getNext();
                }
            }
        }
    }

    /**
     * We have encountered one of the stylistic elements.
     * For example: &lt;b&gt; or &lt;u&gt;, &lt;font&gt;,  etc.<p>
     *
     * For this element we can do the following:<ul>
     *
     * <li>remove it if it has been opened already.</li>
     * <li>process the containment restrictions by pushing this
     * element down, if necessary.</li>
     *
     * <li>If this element has been pushed down or removed then
     * terminate the traversal and start it again at an appropriate
     * starting element.</li>
     *
     * <li>If this element wasn't pushed or removed, continue the
     * traversal.</li>
     * </ul>
     *
     * @param element the element being processed.
     * @param tracker the element tracker.
     * @return true if the current traversal should terminate, false
     *         otherwise.
     */
    private boolean processStylisticElement(
            Element element,
            StyleEmulationElementTracker tracker) {
        String elementName = element.getName();
        boolean terminateTraversal = false;
        if (logger.isDebugEnabled()) {
            logger.debug("Processing stylistic element: " + element);
        }

        if (tracker.isOpen(elementName)) {
            // This element is already open so process this element as
            // if it is going to be removed, unless it is a mergeable
            // element in which case we handle it as a stylistic case.
            if (styleCfg.isMergeableElement(elementName)) {

                // Pull the tracked counterpart element down the tree to
                // where this element is.
                // NOTE: I was going to remove this as part of the fix applied
                // below as I cannot see the point of moving style elements
                // around like this, but it turns out that if you do so, a lot
                // of duplication that it removes as part of the merging as the
                // element is pulled down is not cleaned up. This is
                // particularly obvious in v3.5 and later where we have fully
                // populated styles. In order to remove this we need to have an
                // overall story duplication elimination, see VBM:2004102003.
                pushCounterpartElementDown(element, elementName);

                // The children may well be font elements as well.
                // Really we should have a proper way to deal with duplicates
                // (see VBM:2004102003) but this visitor is so fragile I am
                // loath to change it. So, in this case we just check for the
                // trivial case where all the child elements are redundant and
                // remove them.
                boolean allChildrenRedundant = true;
                Node childNode = element.getHead();
                while (childNode != null && allChildrenRedundant) {
                    if (childNode instanceof Element) {
                        Element childElement = (Element) childNode;
                        if (elementName.equals(childElement.getName())) {
                            // Check to see if all the child attributes are
                            // present and the same in the parent.
                            Attribute attribute = childElement.getAttributes();
                            while (attribute != null && allChildrenRedundant) {
                                String value = element.getAttributeValue(
                                        attribute.getName());
                                if (!attribute.getValue().equals(value)) {
                                    // child's attributes are different
                                    allChildrenRedundant = false;
                                }
                                attribute = attribute.getNext();
                            }
                        } else {
                            // element names are different
                            allChildrenRedundant = false;
                        }
                    }
                    childNode = childNode.getNext();
                }

                if (allChildrenRedundant) {
                    // remove all the children *carefully*
                    childNode = element.getHead();
                    while (childNode != null) {
                        // Remember the next node in case we disturb it.
                        Node next = childNode.getNext();
                        // Remove the element.
                        if (childNode instanceof Element) {
                            removeElement((Element) childNode);
                        }
                        // Move to the next node.
                        childNode = next;
                    }
                }

                // close the tracker. 
                tracker.closed(elementName);
                element.forEachChild(this);

                // DO NOT continue current traversal.
                terminateTraversal = true;
            } else {
                Node sibling = element.getNext();

                processElementToBeRemoved(element);

                // Visit siblings to the right.
                while (sibling != null) {
                    sibling.accept(this);
                    sibling = sibling.getNext();
                }
                // continue current traversal.
            }
        } else {
            // Track this element (opened).
            tracker.opened(elementName);
            Element parent = element.getParent();

            boolean pushedDown = pushStylisticElementDown(
                    element, element.getHead());

            if (pushedDown) {
                // Track this element (closed).
                tracker.closed(elementName);

                removeElement(element);
                parent.forEachChild(this);

                terminateTraversal = true;
            } else {
                // Continue processing (element hasn't been removed or
                // pushed down).

                // HACK: the code below is a nasty hack.
                // Unfortunately I can't see an easy non-hack alternative as
                // the design of this code seems fatally flawed, it modifies
                // the dom both above and below the current processing point.
                // The commented out code shown failed previously when the
                // stylistic element gets pulled down into multiple children,
                // as it cached the next link during iteration and childrens
                // next links are modified. See VBM:2006092212.
                forEachChildAllowingModification(element);
                // element.visitChildren(this, tracker);

                // Track this element (closed).
                tracker.closed(elementName);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Finished processing stylistic element: " + element);
        }
        return terminateTraversal;
    }

    /**
     * <p>Ensure that the containment rules are not broken whilst processing other
     * elements. This is done by testing to see if the current element is
     * contained by a style element that shouldn't contain this other
     * element.</p>
     *
     * <p>If no such element exists we should continue processing otherwise we
     * need to push any elements that shouldn't contain me, down into me.</p>
     *
     * For example.
     *
     * <pre>
     *      n0                           n0
     *        \                         /  \
     *         b                       b    n3
     *       /  \                      |   /  \
     *     n1   n3         ->         n1  b    p
     *          / \                       |   /  \
     *        n2    p*                   n2  b    b
     *             / \                       |    |
     *            n4  n5                     n3   n5
     *
     * where p* is the current element being processed and b shouldn't
     * contain p. b would be pushed down into p.
     *
     * </pre>
     *
     * @param element the element being processed.
     * @param tracker the element tracker.
     * @return true if the current traversal should terminate, false
     *         otherwise.
     */


    /**
     * We have encountered one of the anti elements.
     * For example: &lt;anti-b&gt; or &lt;anti-u&gt;, etc.<p>
     *
     *
     * If this element's counterpart has been opened:
     * <ul>
     * <li>promote the anti-element to the same level as its open
     * counterpart and then remove the anti-element.</li>
     * </ul>
     * otherwise
     * <ul>
     * <li>remove the anti-element</li>
     * </ul>
     *
     * @param element the element being processed.
     * @param tracker the element tracker.
     */
    private void processAntiElement(
            Element element,
            StyleEmulationElementTracker tracker) {
        String antiElementName = element.getName();

        if (logger.isDebugEnabled()) {
            logger.debug("Processing anti-element: " + element);
        }

        Node sibling = element.getNext();

        Iterator stylisticElements = styleCfg.getCounterpartElementNames(
                antiElementName);
        boolean promoted = false;
        while (stylisticElements.hasNext()) {
            String stylisticElementName = (String) stylisticElements.next();
            if (tracker.isOpen(stylisticElementName)) {
                // Recursively promote the element until it reaches
                // the same level as its counterpart element.
                promoteAntiElementRecursively(
                        element, stylisticElementName, tracker);
                promoted = true;
            }
        }
        if (promoted) {
            // Get the (possibly) new sibling of the promoted element.
            sibling = element.getNext();
            tracker.closed(antiElementName);
        }

        // Visited my children before removing the anti-element.
        processElementToBeRemoved(element);

        // Visit siblings to the right.
        while (sibling != null) {
            sibling.accept(this);
            sibling = sibling.getNext();
        }
    }

    /**
     * Keep track of whether or not I have visited this node already.
     *
     * @param node the node being visited.
     */
    private void markAsVisited(Node node) {
        node.setObject(VISITED);
    }

    /**
     * Print out a debug message identifying the node being processed,
     * accompanied with the message supplied.
     * <p>
     * If the message is constructed dynamically then this method should be
     * wrapped in <code>logger.isDebugEnabled()</code>.
     *
     * @param node    the node to identify.
     * @param message a message to accompany the node identifier.
     * @todo put this pseudo xpath calculator into the MCS DOM itself.
     */
    private void debugNode(Node node, String message) {
        if (logger.isDebugEnabled()) {

            // Calculate the path to this element for debugging.
            Node parent = node;
            Stack stack = new ArrayListStack();
            do {
                stack.push(parent);
                parent = parent.getParent();
            } while (parent != null);

            // Remove the fake parent node if there is one.
            Node top = (Node) stack.peek();
            if (top instanceof Element && ((Element) top).getName() == null) {
                stack.pop();
            }

            StringBuffer path = new StringBuffer();
            while (!stack.isEmpty()) {
                Node pathNode = (Node) stack.pop();

                if (pathNode instanceof Element) {
                    final String name = ((Element) pathNode).getName();
                    path.append(name);
                } else {
                    path.append("(text)");
                }

                // Count the next and previous nodes
                int nextCount = 0;
                Node next = pathNode.getNext();
                while (next != null) {
                    nextCount++;
                    next = next.getNext();
                }
                int previousCount = 0;
                Node previous = pathNode.getPrevious();
                while (previous != null) {
                    previousCount++;
                    previous = previous.getPrevious();
                }
                int position = previousCount + 1;
                int total = position + nextCount;
                path.append("[").append(position).append(":").append(total)
                        .append("]");

                if (!stack.isEmpty()) {
                    path.append(" -> ");
                }
            }

            logger.debug(
                    message + " - Path: " + path + " - Object: " + node + "");
        }
    }

    /**
     * Find the (grand)parent element that matches the element
     * name and then proceed to push that element down the tree. For
     * example,
     *
     * <pre>
     *
     *          n1                 n1
     *          |                 /  \
     *    ... font 1 ...       font 1  n4
     *        / \               |      |
     *    n2      n4     ->     n2   font 2
     *     |      |             |      |
     *    n3     font 2         n3   font 1
     *
     * where 'font 2' is the current node being processed and 'font 1' is
     * pushed down into 'font 2' and a new 'font 1' child of n1 is created.
     *
     * </pre>
     *
     * @param startElement the element that may be pushed down.
     * @param elementName  the name of the [(grand)*]parent to find..
     */
    protected void pushCounterpartElementDown(
            Element startElement,
            String elementName) {

        if (logger.isDebugEnabled()) {
            logger.debug("Pushing counterpart element " + elementName +
                    " down to  " + startElement);
        }

        Element elementToPushDown = startElement;
        String name = null;
        do {
            elementToPushDown = elementToPushDown.getParent();
            name = elementToPushDown.getName();
        } while (name == null || !name.equals(elementName));

        Element parent = startElement;

        // Don't push anything into this element (a direct grandparent of the
        // start element). This reduces garbage and is unnecessary since the
        // element has already been pushed into the ignorable element lineage.
        Element ignorableElement = null;
        do {
            parent = parent.getParent();

            pushElementDownToAllChildren(elementToPushDown, parent.getHead(),
                    false, ignorableElement);

            ignorableElement = parent;
        } while ((parent != null) && (parent != elementToPushDown));

        // We can safely remove the element that was pushed down since it
        // has been copied wherever necessary.
        removeElement(elementToPushDown);
    }

    /**
     * Store the head and the tail nodes of the element to be removed and then
     * remove the element.<p>
     *
     * If the head is non-null (we have at least one child of the removed
     * element) then iterate over the child nodes whilst visiting each node
     * until we reach the stored tail node.
     *
     * @param element the element to be removed.
     */
    private void processElementToBeRemoved(Element element) {

        if (logger.isDebugEnabled()) {
            logger.debug("Removing element: " + element);
        }
        Node head = element.getHead();
        Node tail = element.getTail();

        // Remove the element after storing the elements head and tail.
        removeElement(element);

        if (head != null) {
            // We have at least one child node.
            Node next = head;
            boolean processedTail = false;
            while ((next != null) && !processedTail) {
                next.accept(this);
                processedTail = next == tail;
                next = next.getNext();
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Element removed: " + element);
        }
    }

    /**
     * If any child is an element that should not be contained
     * by the element being pushed down then return true, otherwise return
     * false.
     *
     * @param node    the head node to start the iteration on.
     * @param element the element that may contain the node.
     * @return return true if any of the sibling nodes is an element that
     *         may not be contained by the element being pushed down then,
     *         otherwise return false.
     */
    private boolean anyChildBreaksContainment(String element, Node node) {
        for (Node x = node; x != null; x = x.getNext()) {
            if (x instanceof Element) {
                String name = ((Element) x).getName();
                if (!styleCfg.isContainmentPermitted(element, name) &&
                        !styleCfg.isAntiElement(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Attempt to push the stylistic element down into the tree. Return true if
     * the element was pushed down, false otherwise.<p>
     *
     * For example, if the current element being processed is b then the elment
     * will be pushed down as follows (elements enclosed in '[' and ']' are
     * newly allocated elements):
     *
     * <pre>
     *           root                     root
     *            |                      /  |  \
     * current--> b                    [b] table [b]
     *         /  |   \                |    |     |
     *        A  table  B              A    tr    B
     *            |                       / | \
     *           tr                      td td td
     *         /  |  \       -->        /   |   \
     *       td  td   td              [b]  [b]  [b]
     *      /|\  /|\  /|\             /|\  /|\  /|\
     *
     * </pre>
     *
     * @param element    the element to push down.
     * @param firstChild the first child of element being processed.
     * @return true if the element was pushed down, false otherwise.
     */
    private boolean pushStylisticElementDown(
            Element element,
            Node firstChild) {

        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to push element: " +
                    element.getName() + " down: First child " +
                    firstChild);
        }
        boolean elementPushed = false;

        // If this element should be pushed down do so, whilst recursively
        // pushing down elements that still break the containment rules.
        if (anyChildBreaksContainment(element.getName(), firstChild)) {
            // For each child, see if it is allowed to be contained by its
            // immediate parent. If not, continue to push the element down.
            pushElementDownToAllChildren(element, firstChild,
                    true, null);
            elementPushed = true;
        }
        return elementPushed;
    }

    /**
     * Push the element down to all my children. This includes text nodes
     * and may be recursive (if the recursive flag is set).
     *
     * @param elementToPushDown the element to push down.
     * @param firstChild        the first child to examine (may be null).
     * @param recursive         true if we should push the element down until the
     *                          containment rules aren't broken by any immediate
     *                          children, false otherwise.
     * @param ignorableElement  the element that should be ignored (the pushable
     *                          element is not pushed into this element).
     * @todo better an optimization here would be to group the element to push down around similar nodes (eg. b can be the parent of the first three text children).
     * @todo better this may be turned into a simple visitor??
     */
    protected void pushElementDownToAllChildren(
            Element elementToPushDown,
            Node firstChild,
            boolean recursive,
            Element ignorableElement) {

        if (logger.isDebugEnabled()) {
            logger.debug("Pushing element " + elementToPushDown +
                    " to all children starting with " + firstChild);
        }

        // Iterate over the children to see if we need to wrap the child
        // in the element to push down, or push this element even further
        // down.
        for (Node x = firstChild; x != null; x = x.getNext()) {
            if (x instanceof Element) {
                if (x != ignorableElement) {
                    boolean parentSupportsStyles = true;
                    Element element = (Element) x;

                    final String name = element.getName();
                    // Check to see if this child may contain stylistic markup
                    // If not, attempt to push the element down to my children.
                    final String pushElementName = elementToPushDown.getName();

                    boolean bothMergeable = styleCfg.isMergeableElement(name) &&
                            styleCfg.isMergeableElement(pushElementName);

                    boolean pushThroughAntiElement =
                            styleCfg.isAntiElement(name) &&
                                    styleCfg.isCounterpart(name,
                                            pushElementName);

                    boolean pushable =
                            (!bothMergeable && !pushThroughAntiElement) ||
                                    styleCfg.isDivisibleStyleElement(name);

                    if (!styleCfg.isStylePermittedInElement(name)) {
                        if (pushable) {
                            pushElementDownToAllChildren(elementToPushDown,
                                    element.getHead(), true, null);
                        }
                        parentSupportsStyles = false;
                    }
                    boolean createOrMergeElement = false;
                    // Only push style elements into this element if it
                    // supports styles. If the element is indivisible the
                    // stylistic elements should be pushed down deeper into
                    // elements that may contain styles. If none are found
                    // then that style element is lost (which is OK).
                    if (parentSupportsStyles) {
                        if (recursive && pushable) {
                            createOrMergeElement = !pushStylisticElementDown(
                                    elementToPushDown,
                                    element.getHead());
                        } else {
                            // Always attmpt to create or merge an element for
                            // non-recursive traversal.
                            createOrMergeElement = true;
                        }

                        if (createOrMergeElement) {
                            createOrMergeElement(elementToPushDown, element);
                        }
                    }
                }
            } else if (x instanceof Text) {
                Text text = (Text) x;
                // We only push stylistic elements down so we don't need to
                // wrap the text in a stylistic element if the length is zero
                // or the parent of the text doesn't permit stylistic elements.
                String parentName = x.getParent().getName();
                boolean wrapNode = (text.getLength() > 0) && wrapNode(
                        parentName, elementToPushDown.getName());

                if ((wrapNode)) {
                    // Processing a text node so create a new clone of the
                    // stylistic element and add the text as a child of this
                    // new element. Update the tree so that the new element
                    // 'replaces' the text node.
                    Node leftSibling = x.getPrevious();
                    Element parent = x.getParent();

                    Element newElement = domFactory.createElement();
                    newElement.copy(elementToPushDown);

                    // Remove x from the list and
                    x.remove();
                    // Add the text to the newly created element.
                    newElement.addHead(x);

                    if (leftSibling != null) {
                        newElement.insertAfter(leftSibling);
                    } else {
                        newElement.addToHead(parent);
                    }
                    // Because we have replaced x with newElement we need
                    // to ensure for loop continues by assigning x to
                    // newElement.
                    x = newElement;
                }
                // else ignore empty text nodes as they are not interesting.
            } else {
                // Should never happen.
                throw new IllegalStateException("Unknown node type: " + x);
            }

        }
    }

    /**
     * Return true if the parent name may wrap the new node name without
     * breaking any containment rules, false otherwise.
     *
     * @param parentName  the parent name.
     * @param newNodeName the new node to be added as a child of the parent
     *                    name.
     * @return true if the parent name may wrap the new node name without
     *         breaking any containment rules, false otherwise.
     */
    private boolean wrapNode(String parentName, String newNodeName) {
        boolean wrapNode = styleCfg.isStylePermittedInElement(parentName);
        if (wrapNode && styleCfg.isStylisticElement(parentName) &&
                !styleCfg.isContainmentPermitted(parentName, newNodeName)) {
            wrapNode = false;
        }
        return wrapNode;
    }

    /**
     * Create or merge the element with the element being pushed down. Note
     * that if the element isn't permitted to be contained then DO NOT create
     * the element.
     *
     * @param elementToPushDown the element being pushed down.
     * @param element           the element that will become the new parent of the
     *                          created node, or will contain the merged attributes of the element
     *                          being pushed down.
     */
    private void createOrMergeElement(
            Element elementToPushDown,
            Element element) {
        final String pushElementName = elementToPushDown.getName();
        final String name = element.getName();

        // If this is a mergeable element then merge the attributes, otherwise
        // copy the element to push down and make it a child of the element.
        if (styleCfg.isMergeableElement(name) &&
                pushElementName.equals(name)) {
            element.mergeAttributes(elementToPushDown, false);
        } else {
            if (wrapNode(name, pushElementName)) {
                copyElementAsNewChild(elementToPushDown, element);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "Style not permitted in " + name + " element " +
                                    "(" + pushElementName + ") not " +
                                    "pushed down ");
                }
            }
        }
    }

    /**
     * Create a new element that is a copy of the elementToCopy and add it
     * as a child of the parent element.
     *
     * @param elementToCopy the element to copy.
     * @param parent        the parent element.
     */
    private void copyElementAsNewChild(Element elementToCopy, Element parent) {

        Element newElement = domFactory.createElement();
        newElement.copy(elementToCopy);
        parent.addChildrenToHead(newElement);
        parent.addHead(newElement);
    }

    /**
     * Recursively promote the element and update the element tracker as
     * necessary.<p>
     *
     * Promote 'anti-xxx' elements up to the level of their counterpart
     * element. If the any parent element of the anti-element happens to be an
     * indivisible element (such as an anchor) we cannot promote the anti
     * element so we are forced to discontinue the promoting. Note that we also
     * do not know whether or not the indivisible element actually permits
     * stylistic elements anyway. We rely on the fact that this (having to
     * terminate the promoting) should never be the case since all stylistic
     * elements are processing before anti-elements and are always pushed down
     * as deep as possible (thus being close to the anti-element)<p>
     *
     * For example, anti-b will be promoted up to same level as b (anti-b is
     * then removed):
     *
     * <pre>
     *           root                  root
     *            |                   __|___
     *       ...  b ...             /   |    \
     *         /  |   \    --> ... b [anti-b] b ...
     *       n1 anti-b n2          |    |     |
     *            |               n1   n3    n2
     *           n3
     * </pre>
     *
     * We sometimes need to copy the displaced parent element as follows (here
     * we copy u as child of the anti-b element in one step of the recursion:
     *
     * <pre>
     *           root                  root
     *            |                   __|___
     *       ...  b ...             /   |    \
     *            |            ... b [anti-b] b ...
     *            u                |    |     |
     *         /  |   \    -->     u    u     u
     *       n1 anti-b n2          |    |     |
     *            |               n1   n3    n2
     *           n3
     * </pre>
     *
     * @param antiElement the anti-element to promote.
     * @param tracker     the element tracker.
     */
    private void promoteAntiElementRecursively(
            Element antiElement,
            String antiElementCounterpartName,
            StyleEmulationElementTracker tracker) {

        if (logger.isDebugEnabled()) {
            logger.debug("Promoting anti element recursively: " + antiElement);
        }
        Element parent = antiElement.getParent();
        if (styleCfg.isIndivisibleElement(parent.getName())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not promote anti-element (" + antiElement +
                        ") since it is contained in an indivisible element");
            }
        } else {
            antiElement.promote(true);

            String parentName = parent.getName();

            // Decrement the parent name in the tracker since this parent is
            // now a child of the element that has just been pushed up.
            tracker.closed(parentName);

            if ((parent != null) && (antiElementCounterpartName != null) &&
                    (parentName == null ||
                            !parentName.equals(antiElementCounterpartName))) {
                // We need copy the parent element (which may be a displaced
                // italic element, for example) as a child of the newly promoted
                // anti-element.
                if (!antiElementCounterpartName.equals(parentName) &&
                        !styleCfg.isCounterpart(antiElement.getName(),
                                parentName)) {
                    copyElementAsNewChild(parent, antiElement);
                }
                promoteAntiElementRecursively(
                        antiElement, antiElementCounterpartName, tracker);
            }
        }
    }

    /**
     * Remove the element and preserve the element's children.<p/>
     *
     * For example, removing node C results in the following:
     *
     * <pre>
     *           root                  root
     *            |                     |
     *        ... A ...             ... A ...
     *        _-  |  -_    -->     _-  /  \  -_
     *       B    C    D          B   E    F   D
     *            /\
     *           E  F
     * </pre>
     *
     * @param element the element to remove.
     */
    private void removeElement(Element element) {
        Node leftSibling = element.getPrevious();
        if (leftSibling != null) {
            element.insertChildrenAfter(leftSibling);
        } else {
            element.addChildrenToHead(element.getParent());
        }
        element.remove();
    }

    /**
     * An alternative to {@link Element#forEachChild} which allows the
     * element being processed to modify the iteration by changing it's next
     * link.
     * <p>
     * Parts of this visitor's processing apparently rely on this behaviour
     * being allowed.
     * <p>
     * {@link Element#forEachChild} caches the next link (to allow the
     * currently processed node to be removed) and so cannot be used in all
     * cases.
     * <p>
     * NOTE: use this method with caution!
     *
     * @param element the element who's children are to be visited.
     */
    private void forEachChildAllowingModification(Element element) {

        Node node = element.getHead();
        while (node != null) {
            node.accept(this);
            node = node.getNext();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 10-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 01-Aug-05	8923/4	pabbott	VBM:2005063010 End to End CSS emulation test

 26-Jul-05	8923/2	pabbott	VBM:2005063010 Done

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 18-May-05	8273/1	tom	VBM:2004091703 Added Stylistic Blink Support to iMode

 11-Mar-05	7357/1	pcameron	VBM:2005030906 Fixed node annotation for dissection

 11-Jan-05	6433/13	matthew	VBM:2004120805 Resolve namespace conflicts

 04-Jan-05	6433/8	matthew	VBM:2004120805 recommit

 31-Dec-04	6433/4	matthew	VBM:2004120805 fix null pointer exception in pushCounterpartElementDown

 08-Dec-04	6433/1	matthew	VBM:2004120805 stop NPE from being thrown in rare circumstances

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 10-Nov-04	6172/1	claire	VBM:2004110808 supermerge: Handling null named elements in the style emulation code

 10-Nov-04	6164/1	claire	VBM:2004110808 Handling null named elements in the style emulation code

 29-Oct-04	5877/13	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - fix comments

 28-Oct-04	5877/11	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 27-Oct-04	5877/9	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 26-Oct-04	5877/6	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 19-Oct-04	5843/3	geoff	VBM:2004100710 Invalid WML is being generated since introduction of theme style options (R599)

 19-Oct-04	5854/1	claire	VBM:2004080905 Allowing multiple levels of anti- elements

 19-Oct-04	5782/1	claire	VBM:2004080905 Allowing multiple levels of anti- elements

 19-Aug-04	5272/1	byron	VBM:2004081902 MCS erroneously outputs anti-xxx elements

 19-Aug-04	5268/1	byron	VBM:2004081902 MCS erroneously outputs anti-xxx elements

 21-Jul-04	4752/9	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - fix anti-size in WML

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 14-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 14-Jul-04	4752/7	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 14-Jul-04	4752/5	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 13-Jul-04	4752/3	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 ===========================================================================
*/
