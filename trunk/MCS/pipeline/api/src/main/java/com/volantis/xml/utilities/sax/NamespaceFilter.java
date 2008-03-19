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
package com.volantis.xml.utilities.sax;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An XMLFilter that associates a default namespace with elements that do not
 * have an associated namespace. It is therefore guaranteed that every
 * <code>startElement</code> and every <code>endElement</code>
 * generated/forwarded by this filter will have an associated namespace.
 */
public class NamespaceFilter extends XMLFilterImpl {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The namespace URI to use for those elements that do not belong to a
     * namespace
     */
    private String defaultNamespaceURI;

    /**
     * The namespace prefix to use for those elements that do not belong to a
     * namespace
     */
    private String defaultNamespacePrefix;

    /**
     * The prefixed namespace prefix (:prefix) to use for those elements
     * that do not belong to a namespace
     */
    private String prefixedNamespacePrefix;

    /**
     * The element depth count at which the
     * defaultNamespaceURI/defaultNamespacePrefix mapping was declared
     */
    private int elementCountNamespaceDeclared = 0;

    /**
     * Used to perform element depth counting
     */
    private int elementCount = 0;

    /**
     * Creates a new NamespaceFilter instance
     * @param defaultNamespaceURI the namcespaceURI to use for elements that
     * do not belong to a namespace.  Must not be null or an empty String.
     * @param defaultNamespacePrefix the prefix to use associate with the
     * default namespace. Must not be null or an empty String.
     * @throws IllegalArgumentException if either of the parameters are
     * null or an empty string.
     */
    public NamespaceFilter(String defaultNamespacePrefix,
                           String defaultNamespaceURI) {

        if (null == defaultNamespacePrefix ||
                defaultNamespacePrefix.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "defaultNamespacePrefix cannot be null or an empty string");
        }
        if (null == defaultNamespaceURI ||
                defaultNamespaceURI.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "defaultNamespaceURI cannot be null or an empty string");
        }

        this.defaultNamespacePrefix = defaultNamespacePrefix;
        this.defaultNamespaceURI = defaultNamespaceURI;

        // when we use this prefix we will be appending it to an element
        // name with a : in between. To reduce garbage might as well append
        // the : only once.
        this.prefixedNamespacePrefix = defaultNamespacePrefix + ":";
    }

    // javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String prefixedName,
                             Attributes attributes) throws SAXException {
        elementCount++;
        String fixedUpNamespaceURI = namespaceURI;
        String fixedUpPrefixedName = prefixedName;
        if (0 == namespaceURI.length()) {
            // element has no namespace provided;
            if (0 == elementCountNamespaceDeclared) {
                // We need to declare the namespace that we are about
                // to use
                elementCountNamespaceDeclared = elementCount;
                startPrefixMapping(defaultNamespacePrefix,
                                   defaultNamespaceURI);
            }
            fixedUpNamespaceURI = defaultNamespaceURI;
            fixedUpPrefixedName = prefixedNamespacePrefix + localName;
        }
        // forward the "fixed up event"
        super.startElement(fixedUpNamespaceURI,
                           localName,
                           fixedUpPrefixedName,
                           attributes);
    }

    // javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String prefixedName) throws SAXException {
        String fixedUpNamespaceURI = namespaceURI;
        String fixedUpPrefixedName = prefixedName;
        if (0 == namespaceURI.length()) {
            fixedUpNamespaceURI = defaultNamespaceURI;
            fixedUpPrefixedName = prefixedNamespacePrefix + localName;
        }
        super.endElement(fixedUpNamespaceURI, localName, fixedUpPrefixedName);
        if (elementCount == elementCountNamespaceDeclared) {
            elementCountNamespaceDeclared = 0;
            endPrefixMapping(defaultNamespacePrefix);
        }
        elementCount--;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
