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
package com.volantis.devrep.repository.accessors;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import java.util.Stack;

/**
 * This {@link org.xml.sax.XMLFilter} ensures that an XML document does not
 * have any "default" XML namespace declarations. Any default namespace
 * declaration that is encountered will be replaced with an explicit
 * prefix to namespaceURI binding. The prefix to bind is specified
 * at construction.
 */
public class DefaultNamespaceAdapterFilter extends XMLFilterImpl {

    /**
     * A stack that will store the prefixes that we are binding to the
     * default namesapce
     */
    private Stack prefixes = new Stack();

    /**
     * The prefix that the client wishes to bind to the default namespace.
     */
    private String prefix;

    /**
     * A stack that will store the default namespace URIs that have been
     * encountered.
     */
    private Stack defaultNamespaceURIs = new Stack();

    /**
     * Intitializes the <code>DefaultNamespaceAdapterFilter</code> instance
     * with the given arguments.
     * @param prefix the prefix that will be bound to any default namepsace.
     * If nested default namespace declarations are encountered. This prefix
     * will be suffixed with an integer, starting at 1 and increment on
     * subsequent nested default namespace declarations
     */
    public DefaultNamespaceAdapterFilter(String prefix) {

        if (prefix == null || prefix.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "prefix argument cannot be null or whitespace : " //$NON-NLS-1$
                     + prefix);
        }
        this.prefix = prefix;
    }

    /**
     * Pushes a new prefix onto the internal prefixes stack.
     */
    private void pushPrefix() {
        int prefixCount = prefixes.size();
        String prefixToUse = (prefixes.size() == 0)
                ? prefix : prefix + (prefixCount);
        prefixes.push(prefixToUse);
    }

    /**
     * Pops a prefix off the internal prefixes stack.
     * @return the prefix that was popped.
     * @throws java.lang.IllegalStateException if the stack is empty
     */
    private String popPrefix() {
        if (prefixes.isEmpty()) {
            throw new IllegalStateException("Cannot pop an empty stack"); //$NON-NLS-1$
        }
        return (String) prefixes.pop();
    }

    /**
     * Returns the prefix that is at the top of the internal prefixes stack.
     * @return the prefix that is currently in use.
     * @throws java.lang.IllegalStateException if the stack is empty
     */
    private String getCurrentPrefix() {
        if (prefixes.isEmpty()) {
            throw new IllegalStateException("Cannot peek an empty stack"); //$NON-NLS-1$
        }
        return (String)prefixes.peek();
    }

    /**
     * Pushes the specified namespace URI onto the internal default namespaces
     * stack.
     * @param namespaceURI the namespace to push
     */
    private void pushNamespace(String namespaceURI) {
        defaultNamespaceURIs.push(namespaceURI);
    }

    /**
     * Pops a namesapce URI off the internal namespace stack.
     * @return the namespace URI that was popped.
     * @throws java.lang.IllegalStateException if the stack is empty
     */
    private String popNamespace() {
        if (defaultNamespaceURIs.isEmpty()) {
            throw new IllegalStateException("Cannot pop an empty stack"); //$NON-NLS-1$
        }
        return (String) defaultNamespaceURIs.pop();
    }

    /**
     * Returns the namespace that is at the top of the internal namespace stack
     * or null if it is empty
     * @return the current default namespace or null if one has not been
     * declared
     */
    private String getCurrentNamespace() {
        return (defaultNamespaceURIs.isEmpty())
                ? null : (String) defaultNamespaceURIs.peek();

    }

    /**
     * Fixes up a localName so that it is prefix with the current default
     * prefix.
     * @param localName the name that is to be qualified.
     * @return the qualified localName.
     */
    private String createQName(String localName) {
        String prefixToUse = getCurrentPrefix();
        StringBuffer buffer = new StringBuffer(localName.length() +
                                               prefixToUse.length() + 1);
        buffer.append(prefixToUse)
              .append(':')
              .append(localName);
        return buffer.toString();
    }

    // javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        if ("".equals(prefix)) { //$NON-NLS-1$
            // this is the end of a default namespace declaration
            // pop the prefix and namespaceURI stacks
            prefix = popPrefix();
            popNamespace();
        }
        // forward the event
        super.endPrefixMapping(prefix);
    }

    // javadoc inherited
    public void startPrefixMapping(String prefix,
                                   String namespaceURI) throws SAXException {
        if ("".equals(prefix)) { //$NON-NLS-1$
            // This is a default namespace declaration. Push the namespace
            // URI and a prefix onto the associated stacks
            pushPrefix();
            pushNamespace(namespaceURI);
            // set the prefix to the one that we will be using.
            prefix = getCurrentPrefix();
        }
        super.startPrefixMapping(prefix, namespaceURI);
    }

    // javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        if (namespaceURI.equals(getCurrentNamespace())) {
            // This event is for the default namespace.
            // Update the qName so that it references the prefix that we
            // are assigning to the current default namespace
            qName = createQName(localName);
        }
        // forward the event
        super.endElement(namespaceURI, localName, qName);
    }

    // javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attributes) throws SAXException {
        if (namespaceURI.equals(getCurrentNamespace())) {
            // This event is for the default namespace.
            // Update the qName so that it references the prefix that we
            // are assigning to the current default namespace
            qName = createQName(localName);
        }
        // forward the event
        super.startElement(namespaceURI, localName, qName, attributes);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4413/2	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 17-Dec-03	2219/1	doug	VBM:2003121502 Added dom validation to the eclipse editors

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 ===========================================================================
*/
