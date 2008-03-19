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
 * $Header: /cvs/architecture/architecture/api/src/java/com/volantis/xml/namespace/NamespacePrefixTracker.java,v 1.6 2003/07/25 15:00:56 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.namespace;

/**
 * This provides support for tracking changes to mapping from prefixes to
 * namespace URIs and vice versa.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * <p>This class is intended to be used within a SAX
 * {@link org.xml.sax.ContentHandler} and relies on the order that the
 * prefix mapping and element events are received in SAX. i.e.
 * startPrefixMapping events are received before the associated <code>startElement</code>
 * event and the <code>endPrefixMapping</code> events are received after the associated
 * <code>endElement</code> event.</p>
 * <p>Most elements will not have any prefix mappings at all so implementations
 * of this should be able to handle that efficiently both in terms of time and
 * performance.</p>
 * <p>The methods are intended to be used as follows.</p>
 * <dl>
 *   <dt><code>startElement</code></dt>
 *   <dd>This should be called by the SAX ContentHandler <code>startElement</code> method.
 * </dd>
 *   <dt><code>endElement</code></dt>
 *   <dd>This should be called by the SAX ContentHandler <code>endElement</code> method.</dd>
 *   <dt><code>startPrefixMapping</code></dt>
 *   <dd>This should be called by the SAX ContentHandler <code>startPrefixMapping</code>
 * method.</dd>
 *   <dt><code>endPrefixMapping</code></dt>
 *   <dd>This should be called by the SAX ContentHandler <code>endPrefixMapping</code>
 * method.</dd>
 * </dl>
 *
 * <p>If a {@link QName} has an empty prefix then the mapping of the prefix to
 * namespace URI is dependent on what the {@link QName} is used for. If it is
 * an element then it maps to the default namespace, if it is an attribute then
 * it defaults to the empty namespace and if it is something else such as an
 * XPath function name then it maps to the default function namespace.</p>
 *
 * <h2>Caveat</h2>
 *
 * <p>SAX specifically states that <code>startPrefixMapping</code> and <code>endPrefixMapping</code>
 * events cannot be relied upon to be properly nested with respect to each
 * other. The only thing that can be relied upon is that all the
 * <code>startPrefixMapping</code> events are received before the corresponding <code>startElement</code>
 * event and the <code>endPrefixMapping</code> events are received after the corresponding
 * <code>endElement</code> event.</p>
 *
 * <p>The events are only properly nested with respect to each other if the
 * <code>endPrefixMapping</code> events are received in the reverse order to the order that
 * the <code>startPrefixMapping</code>} events were received in.</p>
 * <pre>
 * &lt;E xmlns:A="ALPHA" xmlns:B="BETA" xmlns:C="GAMMA"&gt;
 *   ...
 * &lt;/E&gt;
 * </pre>
 * <p>Given the previous markup the first list shows one possible sequence of
 * properly nested events and the second list shows one possible sequence of
 * badly nested events.</p>
 * <ol>
 * <li><code>startPrefixMapping(A, ALPHA)</code></li>
 * <li><code>startPrefixMapping(B, BETA)</code></li>
 * <li><code>startPrefixMapping(C, GAMMA)</code></li>
 * <li><code>startElement(E)</code></li>
 * <li>...</li>
 * <li><code>endElement(E)</code></li>
 * <li><code>endPrefixMapping(C)</code></li>
 * <li><code>endPrefixMapping(B)</code></li>
 * <li><code>endPrefixMapping(A)</code></li>
 * </ol>
 * <ol>
 * <li><code>startPrefixMapping(A, ALPHA)</code></li>
 * <li><code>startPrefixMapping(B, BETA)</code></li>
 * <li><code>startPrefixMapping(C, GAMMA)</code></li>
 * <li><code>startElement(E)</code></li>
 * <li>...</li>
 * <li><code>endElement(E)</code></li>
 * <li><code>endPrefixMapping(A)</code></li>
 * <li><code>endPrefixMapping(B)</code></li>
 * <li><code>endPrefixMapping(C)</code></li>
 * </ol>
 * <p>If the events are not received in properly nested order then the mappings
 * held by this object may be incorrect during the processing of
 * <code>endPrefixMapping</code> events. The mappings will be correct once all the
 * <code>endPrefixMapping</code> events for an element have been received.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface NamespacePrefixTracker {

    /**
     * Indicate to the tracker that another element has started.
     * <p>The tracker will use this to update its internal state.</p>
     */
    public void startElement();

    /**
     * Add a mapping between the prefix and the namespace.
     * @param prefix The prefix.
     * @param namespaceURI The namespace.
     */
    public void startPrefixMapping(String prefix, String namespaceURI);

    /**
     * Remove mapping between the prefix and the namespace.
     * @param prefix The prefix.
     */
    public void endPrefixMapping(String prefix);

    /**
     * Indicate to the tracker that an element has ended.
     * <p>The tracker will use this to update its internal state.</p>
     */
    public void endElement();

    /**
     * Get the prefix associated with the specified namespace URI.
     * <p>There can be multiple prefixes associated with a particular namespace
     * at any one time. The specific prefix returned by this method is not
     * defined.</p>
     * @param namespaceURI The namespace URI.
     * @return The prefix associated with the specified namespace URI, or null
     * if no such mapping exists.
     */
    public String getNamespacePrefix(String namespaceURI);

    /**
     * Get the namespace URI associated with the specified namespace prefix.
     * @param prefix The namespace prefix.
     * @return The namespace URI associated with the prefix, or null if no such
     * mapping exists.
     */
    public String getNamespaceURI(String prefix);



    /**
     * Resolve an element's QName into an ExpandedName.
     * <p>This is equivalent to invoking {@link #resolveQName} with the
     * default namespace URI (the one that is bound to the empty prefix).</p>
     * @param qName The QName of the element.
     * @return The ExpandedName object that the QName resolved to, or null if
     * the QName contained an unknown prefix.
     */
    public ExpandedName resolveElementQName(QName qName);

    /**
     * Resolve an attribute's QName into an ExpandedName.
     * <p>This is equivalent to invoking {@link #resolveQName} with the null
     * namespace (an empty string).</p>
     * @param qName The QName of the attribute.
     * @return The ExpandedName object that the QName resolved to, or null if
     * the QName contained an unknown prefix.
     */
    public ExpandedName resolveAttributeQName(QName qName);

    /**
     * Resolve a QName into an ExpandedName.
     * <p>If the QName has an empty prefix then the resulting ExpandedName uses
     * the specified namespace URI.</p>
     * @param qName The QName to resolve.
     * @param defaultNamespaceURI The namespace URI to use if the prefix is
     * empty.
     * @return The ExpandedName object that the QName resolved to, or null if
     * the QName contained an unknown prefix.
     */
    public ExpandedName resolveQName(QName qName, String defaultNamespaceURI);

    /**
     * Return the prefixes that are currently defined in the tracker. This
     * includes the default namespace if it is set. The prefix for the default
     * namespace is an empty string.
     *
     * @return the prefixes that are currently defined in the tracker. This
     * includes the default namespace.
     */
    public String[] getPrefixes();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 25-Jul-03	242/3	steve	VBM:2003072310 Replaced pauls NamespacePrefixTracker and added PublicAPI tags

 ===========================================================================
*/
