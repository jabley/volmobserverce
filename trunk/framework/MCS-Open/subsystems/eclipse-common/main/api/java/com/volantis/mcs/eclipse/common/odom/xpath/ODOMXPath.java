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
package com.volantis.mcs.eclipse.common.odom.xpath;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Map;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;

/**
 * An XPath with ODOM extensions.
 */
public class ODOMXPath extends XPath {
    /**
     * The XPath function start identifier.
     */
    private static final String FUNCTION_START = "(";
    /**
     * XPath unsupported values when attempting to create an XPath.
     */
    private static final String[] UNSUPPORTED_VALUES = new String[]{
        "*", "//", "$", "<", ">", "=", "|", "::"
    };

    /**
     * Create an absolute XPath to the specified observable node.
     *
     * @param observable the observable that will be used to create the XPath.
     * @throws java.lang.IllegalArgumentException if observable is not a recognized type.
     */
    public ODOMXPath(ODOMObservable observable)
            throws IllegalArgumentException {
        this(null, observable, null);
    }


    /**
     * Create an absolute XPath to the specified observable node.
     *
     * @param observable           the observable that will be used to create
     *                             the XPath.
     * @param additionalNamespaces an array of additionalNamespaces that may be
     *                             used to create elements with the appropriate
     *                             namespace.
     * @throws IllegalArgumentException if observable is not a recognized type.
     */
    public ODOMXPath(ODOMObservable observable, Namespace[] additionalNamespaces)
            throws IllegalArgumentException {
        this(null, observable, additionalNamespaces);
    }

    /**
     * Create a relative or absolute XPath to the specified observable node. A
     * relative XPath will be created if start is not null.
     *
     * @param start      the start element.
     * @param observable the observable element node used to create the XPath.
     * @throws IllegalArgumentException if observable is not a recognized type.
     */
    public ODOMXPath(Element start, ODOMObservable observable)
            throws IllegalArgumentException {
        this(start, observable, null);
    }

    /**
     * Create a relative or absolute XPath to the specified observable node. A
     * relative XPath will be created if start is not null.
     *
     * @param start                the start element.
     * @param observable           the observable element node used to create
     *                             the XPath.
     * @param additionalNamespaces an array of additionalNamespaces that4 may be
     *                             used to create elements with the appropriate
     *                             namespace.
     * @throws java.lang.IllegalArgumentException if observable is not a recognized type.
     */

    public ODOMXPath(Element start,
                 ODOMObservable observable,
                 Namespace[] additionalNamespaces)
            throws IllegalArgumentException {
        
        if (observable instanceof Element) {
           xpath = createXPath(start, (Element) observable,
           additionalNamespaces);
        } else if (observable instanceof Attribute) {
           xpath = createXPath(start, (Attribute) observable,
           additionalNamespaces);
        } else if (observable instanceof Text) {
           xpath = createXPath(start, (Text) observable, additionalNamespaces);
        } else {
            throw new IllegalArgumentException("Unknown observable type:" +
                    observable);
        }
    }
    /**
     * Create an instance of this class with an XPath string.
     *
     * @param path the XPath, for example, '/catalog/cd'
     * @throws java.lang.IllegalArgumentException if the path is null or empty.
     */
    public ODOMXPath(String path)
            throws IllegalArgumentException {
        super(path);
    }

    /**
     * Create an instance of this class with an XPath string.
     *
     * @param path       the XPath, for example, '/catalog/cd'
     * @param namespaces an array of namespaces that may be used to create
     *                   elements with the appropriate namespace.
     * @throws java.lang.IllegalArgumentException if the path is null or empty.
     */
    public ODOMXPath(String path, Namespace[] namespaces) {
        super(path, namespaces);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param path              the XPath as a string, for example
     *                          '/catalog/cd'
     * @param encodedNamespaces a string containing encoded namespace
     *                          prefix/uri pairs as would be generated by
     *                          {@link #getNamespacesString}
     */
    public ODOMXPath(String path, String encodedNamespaces) {
        super(path, encodedNamespaces);
    }

    /**
     * Create an instance of this class with an XPath and relative string.
     *
     * @param xpath        an instance of an existing XPath object.
     * @param relativePath the XPath, for example, 'catalog/cd' which must not
     *                     start with a '/' character.
     * @throws java.lang.IllegalArgumentException if the relativePath is empty or starts
     *                                  with '/'.
     */
    public ODOMXPath(XPath xpath, String relativePath)
            throws IllegalArgumentException {
        super(xpath, relativePath);
    }

    /**
     * Create an instance of this class with an XPath and relative string and
     * additional namespaces.
     *
     * @param xpath                an instance of an existing XPath object, may
     *                             be null.
     * @param relativePath         the XPath, for example, 'catalog/cd' which
     *                             must not start with a '/' character.
     * @param additionalNamespaces an array of additionalNamespaces that may be
     *                             used to create elements with the appropriate
     *                             namespace.
     * @throws java.lang.IllegalArgumentException if the relativePath is empty or starts
     *                                  with '/'.
     */
    public ODOMXPath(XPath xpath, String relativePath, Namespace[] additionalNamespaces)
            throws IllegalArgumentException {
        super(xpath, relativePath, additionalNamespaces);
    }

    /**
     * Create an absolute XPath to this element.
     *
     * @param element the element that will be used to create the XPath.
     */
    public ODOMXPath(Element element) {
        super(element);
    }

    /**
     * Create an absolute XPath to this element.
     *
     * @param element              the element that will be used to create the
     *                             XPath.
     * @param additionalNamespaces an array of additionalNamespaces that may be
     *                             used to create elements with the appropriate
     *                             namespace.
     */
    public ODOMXPath(Element element, Namespace[] additionalNamespaces) {
        this(null, element, additionalNamespaces);
    }

    /**
     * Create an absolute XPath to the specified attribute.
     *
     * @param attribute the attribute that will be used to create the XPath.
     */
    public ODOMXPath(Attribute attribute) {
        super(attribute);
    }

    /**
     * Create an absolute XPath to the specified attribute.
     *
     * @param attribute            the attribute that will be used to create the
     *                             XPath.
     * @param additionalNamespaces an array of additionalNamespaces that may be
     *                             used to create elements with the appropriate
     *                             namespace.
     */
    public ODOMXPath(Attribute attribute, Namespace[] additionalNamespaces) {
        super(attribute, additionalNamespaces);
    }

    /**
     * Create an absolute XPath to the specified text node.
     *
     * @param text the text that will be used to create the XPath.
     */
    public ODOMXPath(Text text) {
        super(text);
    }

    /**
     * Create an absolute XPath to the specified text node.
     *
     * @param text                 the text that will be used to create the
     *                             XPath.
     * @param additionalNamespaces an array of additionalNamespaces that may be
     *                             used to create elements with the appropriate
     *                             namespace.
     */
    public ODOMXPath(Text text, Namespace[] additionalNamespaces) {
        super(text, additionalNamespaces);
    }

    /**
     * Create a relative or absolute XPath to the specified element. A
     * relative XPath will be created if start is not null.
     *
     * @param start   the start element.
     * @param element the end element that will be used to create the XPath.
     */
    public ODOMXPath(Element start, Element element) {
        super(start, element);
    }

    /**
     * Create a relative or absolute XPath to the specified element. A relative
     * XPath will be created if start is not null.
     *
     * @param start                the start element.
     * @param element              the end element that will be used to create
     *                             the XPath.
     * @param additionalNamespaces an array of additionalNamespaces that may be
     *                             used to create elements with the appropriate
     *                             namespace.
     */
    public ODOMXPath(Element start, Element element,
                     Namespace[] additionalNamespaces) {
        super(start, element, additionalNamespaces);
    }

    /**
     * Create a relative or absolute XPath to the specified attribute. A
     * relative XPath will be created if start is not null.
     *
     * @param start     the start element.
     * @param attribute the end element that will be used to create the XPath.
     */
    public ODOMXPath(Element start, Attribute attribute) {
        super(start, attribute);
    }

    /**
     * Create a relative or absolute XPath to the specified attribute. A
     * relative XPath will be created if start is not null.
     *
     * @param start                the start element.
     * @param attribute            the end element that will be used to create
     *                             the XPath.
     * @param additionalNamespaces an array of additionalNamespaces that may be
     *                             used to create elements with the appropriate
     *                             namespace.
     */
    public ODOMXPath(Element start, Attribute attribute,
                     Namespace[] additionalNamespaces) {
        super(start, attribute, additionalNamespaces);
    }

    /**
     * Create a relative XPath to the specified text node from the starting
     * element.
     *
     * @param start the start element.
     * @param text  the end text node that will be used to create the XPath.
     */
    public ODOMXPath(Element start, Text text) {
        super(start, text);
    }

    /**
     * Create a relative XPath to the specified text node from the starting
     * element.
     *
     * @param start                the start element.
     * @param text                 the end text node that will be used to create
     *                             the XPath.
     * @param additionalNamespaces an array of additionalNamespaces that may be
     *                             used to create elements with the appropriate
     *                             namespace.
     */
    public ODOMXPath(Element start, Text text, Namespace[] additionalNamespaces) {
        super(start, text, additionalNamespaces);
    }

    /**
     * Create an instance of this class with an XPath and an attribute, where
     * the attribute is treated as a relative path containing only the
     * attribute's name. Effectively this just appends the attribute name to the
     * given XPath, using the appropriate syntax for an XPath component of type
     * attribute.
     *
     * @param xpath     an instance of an existing XPath object.
     * @param attribute the attribute to augment to the xpath in the way
     *                  described above. This may not be null.
     */
    public ODOMXPath(XPath xpath, Attribute attribute) {
        super(xpath, attribute);
    }

    /**
     * Create an instance of this class with an XPath and an attribute, where
     * the attribute is treated as a relative path containing only the
     * attribute's name. Effectively this just appends the attribute name to the
     * given XPath, using the appropriate syntax for an XPath component of type
     * attribute.
     *
     * @param xpath      an instance of an existing XPath object.
     * @param attribute  the attribute to augment to the xpath in the way
     *                   described above. This may not be null.
     * @param namespaces an array of namespaces that may be used to create
     *                   elements with the appropriate namespace.
     */
    public ODOMXPath(XPath xpath, Attribute attribute, Namespace[] namespaces) {
        super(xpath, attribute, namespaces);
    }

    /**
     * Create an instance of this class with an XPath and a relative XPath.
     * Effectively this just appends the relative XPath to the given XPath.
     *
     * @param xpath         an instance of an existing XPath object.
     * @param relativeXPath the xpath to augment to the first xpath in the way
     *                      described above. This may not be null and must be
     *                      relative.
     * @throws java.lang.IllegalArgumentException if the specified relative xpath is not
     *                                  of correct syntax for a relative
     *                                  xpath.
     */
    public ODOMXPath(XPath xpath, XPath relativeXPath)
            throws IllegalArgumentException {
        super(xpath, relativeXPath, null);
    }

    /**
     * Create an instance of this class with an XPath and a relative XPath.
     * Effectively this just appends the relative XPath to the given XPath.
     *
     * @param xpath         an instance of an existing XPath object.
     * @param relativeXPath the xpath to augment to the first xpath in the way
     *                      described above. This may not be null and must be
     *                      relative.
     * @param namespaces    an array of namespaces that may be used to create
     *                      elements with the appropriate namespace.
     * @throws java.lang.IllegalArgumentException if the specified relative xpath is not
     *                                  of correct syntax for a relative xpath.
     */
    public ODOMXPath(XPath xpath, XPath relativeXPath, Namespace[] namespaces)
            throws IllegalArgumentException {
        super(xpath, relativeXPath, namespaces);
    }


    /**
     * This method is used, iteratively, to cause the creation of a given XPath
     * set of ODOM nodes where nodes in the XPath are missing.
     *
     * <p>Each step in processing the XPath simply takes the first component of
     * the XPath (i.e. up to a '/' or the end of the string) and creates a
     * sub-XPath from it. This is used to access the required node.</p>
     *
     * <p>If not found, that sub-node is created. This could be an optionally
     * predicated element, an attribute or an optionally predicated text node.
     * The predicate can simply be removed before processing the remainder of
     * the path (the required number of new instances must be created to ensure
     * that the required predicated instance will exist).</p>
     *
     * <p>Note: The path is for an attribute if it starts with the '@'
     * character, a text node if it is the value "text()" and otherwise is for
     * a named element.</p>
     *
     * @param context the ODOMElement that serves as the context ('parent'
     *                node).
     * @param factory the ODOMFactory that will be used to create a Text,
     *                Attribute or Element node.
     * @return the lowest node created in the path.
     * @throws IllegalArgumentException if context is null or factory is null.
     * @throws IllegalStateException    if an attempt was made to create a node
     *                                  of an unsupported type.
     * @throws com.volantis.mcs.xml.xpath.XPathException           if the XPath cannot be applied using
     *                                  the given context (the XPath is
     *                                  absolute and the context element
     *                                  doesn't match the root step in the
     *                                  path) or if the XPath is erroneous
     *                                  (e.g. contains path steps after an
     *                                  attribute or text node step).
     */
    public ODOMObservable create(ODOMElement context, ODOMFactory factory)
            throws IllegalArgumentException,
            IllegalStateException,
            XPathException {
        if (context == null) {
            throw new IllegalArgumentException(
                    "The context parameter may not be null.");
        }

        if (factory == null) {
            throw new IllegalArgumentException(
                    "The factory parameter may not be null.");
        }

        String xpathStr = getExternalForm();
        for (int i = 0; i < UNSUPPORTED_VALUES.length; i++) {
            String unsupportedValue = UNSUPPORTED_VALUES[i];
            if (xpathStr.indexOf(unsupportedValue) != -1) {
                throw new IllegalStateException(
                        "Creation of nodes using '" + unsupportedValue +
                        "' is not supported.");
            }
        }

        // Make sure that an absolute XPath is applied on the actual root
        // node
        if (xpathStr.startsWith(XPATH_DELIM)) {
            if (!xpathStr.substring(1, xpathStr.indexOf(XPATH_DELIM, 1)).
                    equals(context.getName())) {
                throw new XPathException("Cannot apply an absolute XPath " +
                        "(" + xpathStr + ") to a context " +
                        "that doesn't match names with " +
                        "the defined root node");
            } else {
                // Trim off the root node name and leading delimiter
                xpathStr =
                        xpathStr.substring(xpathStr.indexOf(XPATH_DELIM, 1) + 1);
            }
        }

        // We now know that we are dealing with 'simple' XPaths. However,
        // we don't cater for functions besides the text() function, so if we
        // find a '(' we need to determine whether the function is text or not.
        ODOMObservable odomObservable = null;
        StringTokenizer tokenizer = new StringTokenizer(xpathStr, XPATH_DELIM);

        // Create an XPath relative to the parent (tracked during path
        // traversal). For example, with a context of 'a' and an XPath of
        // 'b/c/d' the parent and relative XPath will change as follows for
        // each token found:
        //
        // Parent XPath  Token
        // ------ -----  -----
        // a      b      b
        // a/b    c      c
        // a/b/c  d      d
        Element parent = context;
        XPath xpath;
        String xPathToken;

        while (tokenizer.hasMoreTokens()) {
            xPathToken = tokenizer.nextToken();
            // @todo pass the namespaces on in a nicer way (e.g. have a protected constructor that takes the map and copies it)
            xpath = new XPath(xPathToken, this.getNamespacesString());

            // Check to see if the node or nodes already exist in the document.
            List nodes = xpath.selectNodes(parent);
            ODOMObservable node = null;
            if ((nodes == null) || (nodes.size() == 0)) {
                // Node wasn't found, so create one.
                ODOMObservable result = null;
                int predicateStart = xPathToken.indexOf(PREDICATE_START);

                // Handle a predicate on the current path step if needed
                if (predicateStart != -1) {
                    String predicate =
                            xPathToken.substring(predicateStart + 1,
                                    xPathToken.
                            indexOf(PREDICATE_END));
                    if (!isPredicateValid(predicate)) {
                        throw new IllegalStateException(
                                "Unsupported predicate format: " + predicate);
                    }

                    String elementName = xPathToken.substring(0,
                            predicateStart);
                    while (node == null) {
                        // Create the correct number of nodes according to the
                        // number in the predicate field. This involves
                        // creating the node and checking to see if the
                        // original value can be found, if not create another
                        // and so on...
                        result = createNode((ODOMElement) parent,
                                factory,
                                elementName);
                        node = (ODOMObservable) xpath.selectSingleNode(parent);
                    }
                } else {
                    // No predicate required on this step
                    result = createNode((ODOMElement) parent,
                            factory,
                            xPathToken);
                }

                // Since a new node has been created, we need to track this
                // latest node creation (for the return value) and track
                // the parent for the next path step (if the new node is an
                // element and there are further steps)
                if (result != null) {
                    odomObservable = result;

                    if (result instanceof Element) {
                        parent = (Element) result;
                    } else if (tokenizer.hasMoreElements()) {
                        throw new XPathException(
                                "XPath creation for \"" + xpath +
                                "\" terminated early because the new node \"" +
                                new ODOMXPath(result).getExternalForm() + "\" is " +
                                "not an element but should have sub-nodes " +
                                "created");
                    }
                } else {
                    throw new XPathException("Unexpected null result while " +
                            "creating path step");
                }
            } else if (nodes.size() == 1) {
                // Node was found so store it as the new parent but only if it
                // is an Element).
                node = (ODOMObservable) nodes.get(0);

                if (node instanceof Element) {
                    parent = (Element) node;
                } else if (tokenizer.hasMoreTokens()) {
                    throw new XPathException(
                            "XPath creation for \"" + xpath +
                            "\" terminated early because the existing node \"" +
                            new ODOMXPath(node).getExternalForm() + "\" is not " +
                            "an element");
                }
            } else {
                throw new IllegalStateException(
                        "Creation of more than one node for this xpath is not " +
                        "supported: " + xpath.getExternalForm());
            }
        }

        return odomObservable;
    }

    /**
     * Create an ODOMObservable node (either a ODOMText, ODOMAttribute or
     * ODOMElement) node.
     *
     * @param context    the ODOMElement that serves as the context ('parent'
     *                   node).
     * @param factory    the ODOMFactory that will be used to create a Text,
     *                   Attribute or Element node.
     * @param xPathToken the token used to create a node.
     * @return an ODOMObservable node (either a ODOMText, ODOMAttribute or
     *         ODOMElement) node.
     */
    private ODOMObservable createNode(final ODOMElement context,
                                      final ODOMFactory factory,
                                      String xPathToken) {
        ODOMObservable result = null;
        if (xPathToken.startsWith(ATTRIBUTE)) {
            // The token starts with '@' so create a new attribute and add
            // this attribute to the context.
            Attribute attribute = factory.attribute(xPathToken.substring(1),
                    "");
            context.setAttribute(attribute);
            result = (ODOMObservable) attribute;

        } else if (xPathToken.startsWith(XPATH_TEXT_FUNCTION)) {
            // The token is a text item, to create a new text item and add
            // it to the context (if it is not found).
            Text text = factory.text("");
            context.addContent(text);
            result = (ODOMObservable) text;

        } else {
            Element element = createODOMElement(factory, xPathToken);
            context.addContent(element);
            result = (ODOMObservable) element;
        }
        return result;
    }

    /**
     * Create an ODOMElement from the xPath Token.
     *
     * @param factory    the ODOMFactory that will be used to create a Text,
     *                   Attribute or Element node.
     * @param xPathToken the XPath token used to create the ODOMElement.
     * @return a newly created ODOMElement from the xPath Token.
     * @throws IllegalStateException if the creation of an element cannot be
     *                               completed due to an invalid state (e.g.
     *                               found an unsupported identifier).
     */
    private ODOMElement createODOMElement(final ODOMFactory factory,
                                          String xPathToken)
            throws IllegalStateException {

        if (xPathToken.indexOf(FUNCTION_START) != -1) {
            throw new IllegalStateException("Creation of XPath with an " +
                    "unknown function: " + xPathToken);
        }
        String elementName = xPathToken;
        int index = xPathToken.indexOf(':');
        String namespace = null;
        if (index != -1) {
            // We have found a namespace so create an appropriate element
            // with this namespace.
            namespace = xPathToken.substring(0, index);
            elementName = xPathToken.substring(index + 1, xPathToken.length());
        }
        ODOMElement element = null;
        if (namespace != null) {
            String uri = null;
            if ((namespaceURIMap != null) &&
                    (uri = (String) namespaceURIMap.get(namespace)) != null) {
                element = (ODOMElement) factory.element(elementName,
                        Namespace.getNamespace(namespace, uri));
            } else {
                throw new IllegalStateException(
                        "Unable to resolve namespace: " + namespace);
            }

        } else {
            element = (ODOMElement) factory.element(elementName);
        }
        return element;
    }

    /**
     * Remove the items in specified in the XPath relative to the context.
     * <p/>
     * Note that if the removed item is an attribute and there are no other
     * attributes on the element AND the element has no children or content,
     * then remove this element. Continue until we reach the root node or the
     * context itself. In this manner the root node and context will never be
     * removed.
     * <p/>
     * If the node to be removed is an element this is removed regardless of
     * whether it has any content or children. Once it has been removed the
     * recursing up the hierarchy following the "now empty" rule for the parent
     * element removal is adhered to (as described above).
     * <p/>
     * Similarly, if the ODOMObervable object is a Text node or Element we
     * examine its parent (and its parents, etc.) using similar logic described
     * above.
     * <p/>
     * For example for the input xml jdom:
     * <pre>
     * &lt;root&gt;
     *   &lt;catalog&gt;
     *      &lt;cd name="Michael Jaxen's Bleet It"/&gt;
     *   &lt;catalog&gt;
     * &lt;/root&gt;
     *  </pre>
     * deleting the 'name' attribute will remove the 'cd' element AND the
     * 'catalog' element if the context was the 'root' element.
     *
     * @param context the ODOMObservable object to be removed (cannot be null).
     * @return the topmost ODOMObservable object that was removed, or null if
     *         nothing was removed.
     * @throws IllegalArgumentException if the context is null.
     * @throws XPathException           if an unknown observable node was
     *                                  encountered.
     */
    public ODOMObservable remove(ODOMObservable context)
            throws IllegalArgumentException, XPathException {

        if (context == null) {
            throw new IllegalArgumentException("Context may not be null.");
        }
        Object result = null;
        ODOMObservable node = selectSingleNode(context);
        if (node != null) {
            Element parent = null;
            if (node instanceof Element) {
                parent = node.getParent();
                result = ((Element) node).detach();
            } else if (node instanceof Attribute) {
                parent = node.getParent();
                result = ((Attribute) node).detach();
            } else if (node instanceof Text) {
                parent = node.getParent();
                result = ((Text) node).detach();
            } else {
                throw new XPathException("Unknown ODOMObservable node: " + node);
            }
            if ((result != null) && (parent != null)) {
                ODOMObservable removedParent = removeParents(context, parent);
                if (removedParent != null) {
                    result = removedParent;
                }
            }
        }
        return (ODOMObservable) result;
    }

    /**
     * Remove any parents and their parents, etc. for the context and element
     * element.
     *
     * @param context the ODOMObservable context.
     * @param element  the actual element.
     * @return the top most removed node, or null if no element was removed.
     */
    private ODOMObservable removeParents(ODOMObservable context, Element element) {
        ODOMObservable result = null;
        if (element != null) {
            List parentAttributeList = element.getAttributes();

            // Consider deleting me if I don't have any attributes, children,
            // or content and I am not the context or root node.
            boolean haveAttributes = (parentAttributeList != null) &&
                    parentAttributeList.size() > 0;

            boolean haveContent = (element.getContent().size() > 0);

            if (!haveAttributes && !haveContent && (element != context) &&
                    !element.isRootElement()) {

                // OK to remove me - first get my parent.
                Element parent = element.getParent();
                result = (ODOMObservable) element.detach();

                // Now remove my parent's parents (if they may be removed).
                ODOMObservable deadAncestor = removeParents(context, parent);
                if (deadAncestor != null) {
                    result = deadAncestor;
                }
            }
        }
        return result;
    }

    /**
     * Evaluates the wrapped XPath expression and returns the first entry in
     * the list of selected nodes.
     *
     * @param context the node to use as context for evaluating the XPath
     *                expression.
     * @return the first selected nodes, which is an instance of one of the
     *         following ODOM/JDOM classes: ODOM/Element, ODOM/Attribute,
     *         ODOM/Text, ODOM/CDATA, Comment or ProcessingInstruction or null
     *         if no node was selected.
     * @exception XPathException if a Jaxen exception is raised.
     */
    public ODOMObservable selectSingleNode(ODOMObservable context)
            throws XPathException {
        return (ODOMObservable) selectSingleNode((Object) context);
    }

    /**
     * Returns true if the given predicate (a value extracted from within the
     * predicate syntax "[]" markers) is valid in the context of explicit
     * interpretation.
     *
     * @param predicate the predicate string to be checked
     * @return true if the predicate string is valid in the context of explicit
     *         interpretation
     */
    private boolean isPredicateValid(String predicate) {
        boolean valid = true;
        try {
            Integer.valueOf(predicate);
        } catch (NumberFormatException e) {
            valid = false;
        }
        return valid;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 ===========================================================================
*/
