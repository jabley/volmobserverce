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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AbstractTransformingVisitor;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * {@link RecursingDOMVisitor} implementation which removes all redundant div
 * elements from a {@link Document}.
 */
public abstract class AbstractDivRemover
        extends AbstractTransformingVisitor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(AbstractDivRemover.class);

    /**
     * Factory to use to create DOM objects.
     */
    private static final DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * A constant to indicate that multiple children have been found.
     */
    protected static final Element MULTIPLE_CHILDREN_FOUND =
            domFactory.createElement();


    public void visit(Element element) {
        String name = element.getName();

        if ("div".equals(name)) {
            processDivElement(element);
        }
        element.forEachChild(this);
    }

    /**
     * Process the div element by removing the tag or its child if necessary.
     *
     * @param element the element being examined
     */
    private void processDivElement(Element element) {

        if (parentTagMatches(element, "form") ||
                parentTagMatches(element, "body")) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("<div> tag found with <form> or <body> tag as " +
                        "a parent. This <div> tag will not be removed.");
            }
            // Remove any inner div tag if necessary.
            removeInnerDivTag(element);
        } else if (canRemoveDivElement(element, false)) {
            // Remove this div tag if it eligible for removal.
            element.setName(null);
        } else {
            removeInnerDivTag(element);
            /*
            This bit of code is commented out and left here in order to show how
            empty divs that have no children whatsoever (which have been created by
            client's for reasons specific to their layout) may be removed.

              if (countChildren(element) <= 0)  {
                  // This element has no children at all, so remove it
                  element.setName(null);
              } else {
                  // Examine the inner div tag and remove it if possible
                  removeInnerDivTag(element);
              }
            */
        }
    }

    /**
     * If the outer tag does not contain an id attribute and contains one
     * element which is a div tag, then this inner element may be removed,
     * assuming that the styling information can be preserved. Care should be
     * taken to preserve the inner tag's attribute(s) by promoting these
     * attributes to the parent tag.
     * <p/>
     * For example, if we have:
     * <pre>
     * &lt;div class="VE-pane VP-nobreak"&gt; &lt;!-- outer div --&gt;
     *   &lt;div class="VE-pane VE-test" id="111"&gt &lt;!-- inner div
     * --&gt ...
     *   &lt;/div&gt;
     * &lt;/div&gt
     * </pre>
     * then the inner div tag may be removed and any class and id
     * attributes not contained in the outer div tag are moved to the
     * parent div tag. In this example, 'VE-test' and id="111" will be
     * moved to the parent tag.
     * <p/>
     *
     * @param outerDiv the outer div tag as an <code>Element</code>
     *                 object.
     */
    protected abstract void removeInnerDivTag(Element outerDiv);

    /**
     * Return true if the parent of element has a name specified as
     * tagName
     *
     * @param element the element that is used to find it's parent
     * @param tagName the name of the markup to find, e.g. 'form'
     * @return true if the parent of element has a name specified as
     *         tagName
     */
    protected boolean parentTagMatches(Element element, String tagName) {
        Element parent = getNonNullNamedParent(element);
        return parent != null && tagName.equals(parent.getName());
    }

    /**
     * Find the first direct ancestor (i.e. the non null parent) of the given
     * element that has a non null name.
     *
     * @param element whose first non null ancestor to find
     * @return the first direct ancestor that has a non null name. May be null.
     */
    protected Element getNonNullNamedParent(Element element) {

        // @todo unify the duplicated code c.f. StylePropertyAnalyser.getParent(Element)

        // jabley - don't know if this is ever likely to happen, or just left
        // over from the conversion to iterative version from the original
        // recursive implementation.
        if (element == null) {
            return null;
        }

        Element parent = element.getParent();
        while (parent != null && parent.getName() == null) {
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * A div element is eligible for removal if it is non null, it doesn't have
     * an id attribute, and either it is unstyled, or its parent is a div
     * element with the same styling information. Return true if the element is
     * eligible for removal, false otherwise.
     *
     * @param element the element to be processed
     * @return true if the element can be removed, false otherwise
     */
    protected abstract boolean isDivEligibleForRemoval(Element element);


    /**
     * Determine whether the div element passed in as a parameter is eligible
     * for removal.
     * <p/>
     * A div element can be removed if:
     * <ul>
     * <li>It doesn't contain any information which must be preserved
     * (determined by calling {@link #isDivEligibleForRemoval}).
     * </li>
     * AND EITHER:
     * <li>its sibling is null or its sibling is an <code>Element</code>. A
     * sibling may be following or preceding sibling, depending on the value
     * of the following flag (following == <code>true</code> implies
     * look for a following sibling, following == <code>false</code> implies
     * look for a preceding sibling).
     * <p/>
     * If the node does not have a direct sibling, then attempt to find its
     * parent's sibling, etc.</li>
     * OR
     * <li>
     * The 'sibling' which is found is a Text node, but it has a div element
     * between it and the element being examined i.e.<p/>
     * &lt;p&gt;Text<p/>
     * &lt;div&gt;<p/>
     * &lt;div&gt;Some more text&lt;/div&gt;<p/>
     * &lt;/div&gt;<p/>
     * &lt;/p&gt;<p/>
     * (the inner div is the element being examined, and it's nearest previous
     * sibling is that of its parent)
     * </li>
     * </ul>
     *
     * @param element the element used to start the sibling search.
     * @param following the direction to look for siblings. <code>true</code>
     *                  implies look for a following sibling;
     *                  <code>false</code> implies look for a preceding
     *                  sibling.
     * @return true if this node is eligible for removal, false otherwise.
     */
    protected boolean canRemoveDivElement(Element element, boolean following) {

        // guard condition
        if (!isElementDisposable(element)) {
            return false;
        }

        // Check to see if the div could be removed i.e. doesn't contain any
        // information which must be preserved.
        if (isDivEligibleForRemoval(element)) {

            // @todo Everything inside this if branch seems at the wrong level
            // of abstraction and doesn't seem particularly clear as to why it
            // does what it does. Potential candidate for re-write? jabley

            // Predicate Node that is being tested to see if it has a sibling.
            // We start off the the specified element itself; i.e. self.
            Node ancestorOrSelfWithSibling = element;

            // Set the ancestorOrSelfWithSibling to an element on that axis
            // with a sibling in the required direction (following or
            // preceding)
            while (!hasSibling(ancestorOrSelfWithSibling, following)) {

                // Get the parent instead
                ancestorOrSelfWithSibling = ancestorOrSelfWithSibling
                        .getParent();

                // Don't need to worry about whether the nearest sibling is
                // a text node if there is a div node between it and the
                // element to be removed.
                //
                // @todo jabley This seems to be some sort of duplicatation with removeInnerDiv?
                if (isDivElement(ancestorOrSelfWithSibling)) {
                    return true;
                }
            }

            Node sibling = getSiblingForRemovalTest(
                    ancestorOrSelfWithSibling,
                    following);
            return ((sibling == null) || isBlockyElement(sibling));
        }
        return false;
    }

    /**
     * Return a sibling which can be used to check if the context div element
     * can be removed. May be null.
     *
     * If the <code>ancestorOrSelfWithSibling</code> is null, then this method
     * returns null.
     *
     * @param ancestorOrSelfWithSibling the <code>Node</code> which may have a
     *                                  sibling. May be null.
     * @param following                 if true, search the following-sibling
     *                                  axis for a sibling, otherwise search
     *                                  the preceding-sibling axis.
     * @return a sibling <code>Node</code>, or null.
     */
    private Node getSiblingForRemovalTest(final Node ancestorOrSelfWithSibling,
                                          final boolean following) {
        Node sibling = null;
        if (ancestorOrSelfWithSibling != null) {
            sibling = following ? ancestorOrSelfWithSibling.getNext() :
                    ancestorOrSelfWithSibling.getPrevious();
            sibling = findSibling(sibling, following);
        }
        return sibling;
    }

    /**
     * Return true if the specified <code>Node</code> is an
     * <code>Element</code> that has the blockiness property, as defined by
     * XHTML, otherwise false. In this context, 'blocky' is taken to mean that
     * the element has whitespace effects; e.g. a div will typically cause a
     * line break to be rendered by a browser before and after the div
     * contents. The implementation is left to subclasses; the intention is
     * that they can take advantage of protocol parameterisation to decide how
     * to act.
     *
     * @param node the node being tested for 'blockiness'
     * @return true if the element is 'blocky'.
     */
    protected abstract boolean isBlockyElement(final Node node);

    private boolean isDivElement(final Node node) {
        return node instanceof Element
                && ("div".equals(((Element) node).getName()));
    }

    /**
     * Determine if this element has a non null sibling immediately before (if
     * forward is true) or after.
     *
     * @param node    to examine
     * @param forward if true, the specified <code>node</code> will be
     *                checked for a next sibling, otherwise it will be
     *                checked for a previous sibling.
     * @return true if the element has a non null sibling either immediately
     *         before or after it
     */
    private boolean hasSibling(Node node, boolean forward) {
        Node sibling = (forward ? node.getNext() : node.getPrevious());
        return sibling != null;
    }

    /**
     * If the protocol supports preservation of tags and the current node is
     * flagged to be preserved then this node is not eligible for removal.
     *
     * @param element the element to examine as to whether or not it is
     *                disposable (may be removed).
     * @return true if this node is disposable, false otherwise.
     */
    protected boolean isElementDisposable(Element element) {
        return true;
    }

    /**
     * <p/>
     * Find an inner element that is an only child of the parameter
     * outerElement and matches the specifies tagName. If the outerElement
     * has any elements that have null names then drill down those nodes
     * and see if their children have elements with the tagName name, or
     * null names, and so forth. </p>
     * <p/>
     * Only return the inner element if it is the only child of
     * outerElement, or it's siblings have null names and none of their
     * children (or their children if it's name is null) contain elements
     * with the specified tag name. </p>
     *
     * @param outerElement the div element represent the outer most div
     *                     element
     * @param tagName      the name of the tag to find
     * @return the sole inner div tag element if found, null if none was
     *         found, or {@link #MULTIPLE_CHILDREN_FOUND} if more than one
     *         non-null child was found regardless of its name.
     */
    protected Element findOnlyChild(Element outerElement, String tagName) {
        Element matchedElement = null;
        int nonNullInnerElementCount = 0;
        boolean terminateLoop = false;

        // Loop round the children of the outer element. The loop terminates
        // if more than one non-null child has been found, or if more than
        // one non-null inner element has been found.
        for (Node node = outerElement.getHead();
             ((node != null) && !terminateLoop &&
                     nonNullInnerElementCount < 2);
             node = node.getNext()) {

            if (node instanceof Element) {
                Element innerElement;
                Element currentElement = (Element) node;
                String elementName = currentElement.getName();
                if (elementName == null) {
                    // Do a depth first traversal of the tree and return an
                    // element if it is the only element that matches the tag
                    // name amongst null siblings, etc.
                    innerElement = findOnlyChild(currentElement, tagName);

                    // If multiple non-null inner elements were found, flag
                    // this so that the loop can terminate.
                    if (innerElement == MULTIPLE_CHILDREN_FOUND) {
                        terminateLoop = true;
                    }
                } else {
                    innerElement = currentElement;
                }
                if (innerElement != null) {
                    // Count all non-null inner elements.
                    ++nonNullInnerElementCount;
                    if ((tagName != null) &&
                            tagName.equals(innerElement.getName())) {
                        // Only assign the matched element if the names
                        // match.
                        matchedElement = innerElement;
                    } else {
                        // A non-null child has been found that doesn't match.
                        // Therefore, there is no point in searching for a
                        // matching child since multiple children would then
                        // be found. Therefore the loop can be terminated.
                        terminateLoop = true;
                    }
                }
            }
        }

        if (nonNullInnerElementCount == 1) {
            // Only one non-null inner element has been found. If this
            // element matched the name we're looking for then return the
            // matched element. Otherwise, the presence of this non-null
            // element means that we can only ever achieve a multiple match.
            return matchedElement == null ?
                    MULTIPLE_CHILDREN_FOUND :
                    matchedElement;
        } else if (nonNullInnerElementCount > 1) {
            // More than one non-null inner element was found.
            return MULTIPLE_CHILDREN_FOUND;
        }

        // There was no match found.
        return null;
    }

    /**
     * <p>Return a valid sibling by traversing the sibling axis (following or
     * preceding).</p>
     *
     * <p>A sibling is considered valid if it is a <code>Text</code> node, or
     * an <code>Element</code> node that does not have a null name. If an
     * element with a null name is found, then search its preceding (or
     * following) sibling axis, depending on the value of the
     * <code>following</code> flag.</p>
     *
     * @param startNode the node that we will use to start the traversal.
     * @param following if <code>true</code> traverses the following-sibling
     *                  axis, otherwise traverse the preceding-sibling axis.
     * @return the valid sibling, or null if none is found.
     */
    protected Node findSibling(Node startNode, boolean following) {
        Node sibling = startNode;
        while (sibling != null) {
            Node node = sibling;
            if (node instanceof Element) {
                Element element = (Element) node;

                // If name is null drill down and try to find adjacent sibling
                // as a child of the current sibling.
                if (element.getName() == null) {
                    node = following ?
                            element.getHead() :
                            element.getTail();
                    node = findSibling(node, following);
                }
            }
            // We have found a non-empty sibling, return it.
            if (node != null) {
                return node;
            }
            // Node must be null, so permit while loop to find next sibling.
            sibling = following ? sibling.getNext() : sibling.getPrevious();
        }
        return null;
    }

    /**
     * Return the number of children that this element has. Note that children
     * that have empty names are not counted, but their non-null-named
     * descendants are, if there are any.
     *
     * @param parent the parent element used to count its children
     * @return the number of descendants that this element has (0 to n)
     */
    protected int countChildren(Element parent) {
        int elementCount = 0;
        for (Node node = parent.getHead();
             node != null;
             node = node.getNext()) {

            if (node instanceof Element) {
                String elementName = ((Element) node).getName();
                if (elementName == null) {
                    elementCount += countChildren((Element) node);
                } else {
                    ++elementCount;
                }
            } else if (node instanceof Text) {
                ++elementCount;
            }
        }
        return elementCount;
    }
}
