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
package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

import java.net.URL;
import java.util.Stack;
import java.util.Iterator;

/**
 * Standard SAX event consumers, such as XSLT Transformers and serializers as
 * well as custom written ContentHandlers will not work properly unless receive
 * the contextual information within the event stream. This is the
 * responsibility of the context annotating process which will eventually be an
 * exact mirror of the context updating process.
 *
 * Context annotating processes can be created using the method
 * {@link com.volantis.xml.pipeline.sax.XMLPipelineFactory#createContextAnnotatingProcess}
 *
 * The context annotating process adds the following information back into the
 * stream.
 *
 * <ul>
 * <li> Upon invocation of the startProcess method it will invoke
 *      setDocumentLocator with a Locator object that wraps the
 *      XMLPipelineContext and delegates to its current Locator, generate a
 *      startDocument event and finally forward the input event.
 * <li> Upon invocation of the stopProcess method it will generate a matching
 *      endDocument event.
 * <li> It will add an xml:base attribute with a value equal to the value of
 *      the current base URI to the root elements (if we are generating an XML
 *      fragment there may be many) and also to any element when it detects
 *      that the current base URI is different to the value of the closest
 *      enclosing xml:base attribute.
 * </ul>
 */
public class ContextAnnotatingProcess extends XMLProcessImpl {

    /**
     * The volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2004. ";

    /**
     * The depth of the element that are processing.  Should be incremented
     * on startProcess and decremented on endProcess.
     */
    private int elementDepth = 0;

    /**
     * A stack of base URI values maintained with reference to the element
     * depth at which they were pushed onto the stack. If a new base URI is
     * seen by the startElement method (and it is not the base URI on our
     * stack) then it is added onto the top of the stack and the attributes of
     * the event are copied with the new base URI. The endElement event will
     * pop the baseURI from the stack upon decrementing the current depth count
     * down to the value store for the URI at the top of the stack.
     */
    private Stack uriStack = new Stack();

    /**
     * If true then the root element will have the current base URI set as
     * the xml:base attribute.  Ordinarily this is not required as the
     * Document Locator provides the parser with the same information.
     * However, if the document comes from a transform it is not guaranteed
     * to have a DocumentLocator.
     */
    private boolean setBaseURIOnRoot = false;

    private boolean generatedStartDocumentEvent;

    /**
     * Buffers the startNamespacePrefix events so that no empty start and
     * end namespace prefix pairs are not ouput.
     *
     * The is necessary because in certain circumstances xalan will produce a
     * garbage namespace declaration in the element following a start and end
     * namespace prefix event which do not contain a start and end element.
     *
     * This is a normal set of events:
     *  startNamespacePrefix("xd", "http://....")
     *  startElement(...)
     *  ...
     *  endElement(...)
     *  endNamespacePrefix("xd")
     *
     *  The startMNamespacePrefix and endNamespacePrefix happen outside the
     *  element, so if a rule removes the element the namespace events will
     *  will still be sent. If this happens xalan will sometimes produce a
     *  namespace declaration of the form xmlns:%@$#^@#="%@$#^@#".
     *
     * The current fix is to buffer startNamespacePrefix events and only forward
     * them if they are followed by a startElement event.
     *
     */
    private Stack bufferedStartNamespacePrefixes = new Stack();

    /**
     * Create a new ContextAnnotatingProcess.
     */
    public ContextAnnotatingProcess() {
        this(false);
    }

    /**
     * Create a new ContextAnnotatingProcess.
     * @param setBaseURIOnRoot If true then the root element will have the
     * current base URI set as the xml:base attribute.
     */
    public ContextAnnotatingProcess(boolean setBaseURIOnRoot) {
        this.setBaseURIOnRoot = setBaseURIOnRoot;
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        generateStartDocument();
        super.startProcess();
    }

    // javadoc inherited
    public void stopProcess() throws SAXException {
        XMLProcess consumer = getConsumerProcess();
        if (consumer != null) {
            consumer.endDocument();
        }
        super.stopProcess();
    }

    /**
     * We must generate a start document event before any other event is seen.
     * @throws SAXException
     */
    protected void generateStartDocument() throws SAXException {
        if (!generatedStartDocumentEvent) {
            XMLProcess consumer = getConsumerProcess();
            if (consumer != null) {
                ContextWrapperLocator locator =
                        new ContextWrapperLocator(getPipelineContext());
                consumer.setDocumentLocator(locator);
                consumer.startDocument();
                generatedStartDocumentEvent = true;
            }
        }
    }

    // Javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        generateStartDocument();
        // we store the start event away and only pass it on if it is followed
        // by a startElement event. We do this as there is a bug in Xalan that
        // results in junk namespace being generated if a startPrefixMapping
        // event is followed by a endPrefixMapping event with no content in
        // between
        bufferedStartNamespacePrefixes.push(new XMLNamespace(prefix, uri));
    }

    // Javadoc inherited
    public void endPrefixMapping(String prefix)
            throws SAXException {
        if (bufferedStartNamespacePrefixes.isEmpty()) {
            // if the stack of prefixes is empty then we know that
            // we have had a startElement event after the starPrefixMappping
            // event(s). Therefore we can for the corresponding
            // endPrefixMapping event.
            super.endPrefixMapping(prefix);

        } else {
            // startPrefixMapping events are still on the stack so we have not
            // had a startElement event in between the startPrefixMapping
            // and endPrefixMapping events. We need to supress this event.
            // as the start event we not forwarded. We are using the stack as
            // a counter here to ensure that we continue to supress the
            // endPrefixMappig events for all declarations that were not
            // forwarded. 
            bufferedStartNamespacePrefixes.pop();
        }
    }

    // Javadoc inherited
    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {
        generateStartDocument();
        super.characters(ch, start, length);
    }

    // Javadoc inherited
    public void ignorableWhitespace(char ch[],
                                    int start,
                                    int length) throws SAXException {
        generateStartDocument();
        super.ignorableWhitespace(ch, start, length);
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        if (elementDepth == 0) {
            generateStartDocument();
        }
        // Ordinarily we want to suppress the baseURI from being added to the
        // root element so we push the current baseURI onto the stack ahead of
        // checking if we already have it.  Slightly hacky maybe but necessary.
        if (!setBaseURIOnRoot && elementDepth == 0) {
            URL baseURI = getPipelineContext().getCurrentBaseURI();
            startNewBaseURI(baseURI);
        }

        elementDepth++;
        Attributes attributes = atts;
        URL baseURI = getPipelineContext().getCurrentBaseURI();
        if (baseURI != null) {
            URIWrapper wrapper = null;
            if (!uriStack.isEmpty()) {
                wrapper = (URIWrapper)uriStack.peek();
            }
            if (wrapper != null) {
                URL top = wrapper.getURL();
                if (!baseURI.equals(top)) {
                    startNewBaseURI(baseURI);
                    attributes = createAttributesWithBaseURI(atts, baseURI);
                }
            } else {
                startNewBaseURI(baseURI);
                attributes = createAttributesWithBaseURI(atts, baseURI);
            }
        }
        while (!bufferedStartNamespacePrefixes.isEmpty()) {
            XMLNamespace namespace =
                    (XMLNamespace) bufferedStartNamespacePrefixes.pop();
            super.startPrefixMapping(namespace.getPrefix(),
                                     namespace.getURI());

        }
        super.startElement(namespaceURI, localName, qName, attributes);
    }

    /**
     * Copy the buffered start name prefixes from this instance to the instance
     * passed in.
     * @param process the non-null process which isn't this process. 
     */
    public void copyNamespacePrefixes(ContextAnnotatingProcess process) {
        if (this != process && process != null) {
            Iterator iterator = bufferedStartNamespacePrefixes.iterator();
            while (iterator.hasNext()) {
                XMLNamespace ns = (XMLNamespace) iterator.next();
                process.bufferedStartNamespacePrefixes.push(ns);
            }
        }
    }

    /**
     * Create a new URIWrapper
     * @param base The base URL to store.
     */
    private void startNewBaseURI(URL base) {
        URIWrapper wrapper = new URIWrapper(base, elementDepth);
        uriStack.push(wrapper);
    }

    /**
     * Create a new Attributes from the specified Attributes and with an
     * extra xml:base="http://baseurl" with the url determined from the
     * specified URL.
     * @param atts The Attributes from which to derive the new Attributes
     * @param base The url to add to the Attributes as the xml:base
     * @return The new Attributes with the xml:base set.
     */
    private Attributes createAttributesWithBaseURI(Attributes atts, URL base) {
        AttributesImpl result = new AttributesImpl(atts);
        result.addAttribute(NamespaceSupport.XMLNS,
                            "base", "xml:base", "String", base.toExternalForm());
        return result;
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        super.endElement(namespaceURI, localName, qName);
        if (!uriStack.isEmpty()) {
            URIWrapper wrapper = (URIWrapper)uriStack.peek();
            if (wrapper != null) {
                if (wrapper.getPushedDepth() == elementDepth) {
                    uriStack.pop();
                }
            }
        }
        elementDepth--;
    }

    /**
     * Wrapper class used to maintain stack of URL with respect to the element
     * depth that they were initially set in the XMLPipelineContext.
     */
    private class URIWrapper {

        /**
         * The URL to store
         */
        private URL url;

        /**
         * The element depth at which the URL was initially set.
         */
        private int pushedDepth;

        public URIWrapper(URL url, int pushDepth) {
            this.url = url;
            this.pushedDepth = pushDepth;
        }

        /**
         * Get the URL
         * @return the URL
         */
        public URL getURL() {
            return url;
        }

        /**
         * The element nesting depth at which this item was pushed onto the
         * stack.
         * @return the element depth at which this item was pushed onto the
         * stack.
         */
        public int getPushedDepth() {
            return pushedDepth;
        }
    }

    /**
     * Class that encapsulates an XMLNamespace. Note - could have used a
     * the {@link com.volantis.xml.pipeline.Namespace} class but as this
     * is a form of "type safe enum" creating instances of this class would
     * result in these instances being available to other clients at runtime.
     */
    private static final class XMLNamespace {
        /**
         * Namespace prefix
         */
        private String prefix;

        /**
         * Namespace uri
         */
        private String uri;

        /**
         * Creates a new XMLNamespace instance
         * @param prefix namespace prefix
         * @param uri namespace uri
         */
        XMLNamespace(String prefix, String uri) {
            this.prefix = prefix;
            this.uri = uri;
        }

        // javadoc unecesary
        public String getURI() {
            return uri;
        }

        // javadoc unecesary
        public String getPrefix() {
            return prefix;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 05-Feb-04	525/1	adrian	VBM:2004011902 fixed rework issues for baseuri support work

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 20-Jan-04	527/3	adrian	VBM:2004011903 Added Copyright statements to new classes

 20-Jan-04	527/1	adrian	VBM:2004011903 Added ContextAnnotationProcess and supporting classes

 ===========================================================================
*/
