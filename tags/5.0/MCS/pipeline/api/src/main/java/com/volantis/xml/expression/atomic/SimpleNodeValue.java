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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.sax.ExtendedSAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/** 
 * This is a simple, generic implementation of the NodeValue interface.
 *
 * @see com.volantis.xml.expression.ExpressionFactory
 */
public class SimpleNodeValue extends AtomicSequence implements NodeValue {

    /**
     * Lazy loaded {@link TransformerFactory} instance which is used
     * to create an {@link Transformer}s used in {@link #streamContents} method.
     */
    private static TransformerFactory transformerFactory = null;

    /**
     * The value wrappered by this class.
     */
    private Node value;

    /**
     * Initialize the new instance using the given parameters.
     *
     * @param factory the factory by which the value was created
     * @param value   the value to be wrappered
     */
    public SimpleNodeValue(ExpressionFactory factory, Node value) {
        super(factory);
        this.value = value;
    }

    // javadoc inherited
    public Node asW3CNode() {
        return value;
    }

    /**
     * The string equivalent of the node's content is returned as per the
     * <a href="http://www.w3.org/TR/xpath20/#dt-string-value">
     * dt-string-value</a> of the W3C XPath specification.
     *
     * @note rest of javadoc inherited
     */
    public StringValue stringValue() {
        final StringValue result;
        if (value != null) {
            final StringBuffer buffer = new StringBuffer();
            getValueOfTextNodes(value, buffer);
            result = factory.createStringValue(buffer.toString());
        } else {
            result = factory.createStringValue("");
        }
        return result; 
    }

    /**
     * Appending given {@link StringBuffer} parameter with values of all
     * text nodes of given {@link Node} children or with value of given
     * {@link Node} if given node's type is {@link Node#TEXT_NODE}.
     * 
     * @param node the node which values should be added to given buffer
     * @param buffer the buffer to be filles by given node values
     */
    private void getValueOfTextNodes(final Node node,
            final StringBuffer buffer) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            buffer.append(node.getNodeValue());
        } else {
            final NodeList childList = node.getChildNodes();
            final int childCount = childList != null ?
                    childList.getLength() : 0;
            for (int i = 0; i < childCount; i++) {
                getValueOfTextNodes(childList.item(i), buffer);
            }
        }
    }

    // javadoc inherited
    public void streamContents(ContentHandler contentHandler)
            throws SAXException {
        if (value == null) {
            return;
        }
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            Transformer transformer = getTransformer();
            Source source = new DOMSource(value);
            Result result = new StreamResult(stream);
            transformer.transform(source, result);
            if (stream.size() != 0) {
                final XMLReader parser = XMLReaderFactory.createXMLReader();
                parser.setContentHandler(
                        new IgnoringSetDocumentLocatorHandler(contentHandler));
                parser.parse(new InputSource(
                        new ByteArrayInputStream(stream.toByteArray())));
            }
        } catch (Exception e) {
            throw new ExtendedSAXException(e);
        }
    }

    /**
     * Gets and returns a {@link Transformer} instance using the lazily loaded
     * {@link #transformerFactory}.
     * 
     * <p>The new transformer instance is created every time when this method is
     * invoked because the {@link Transformer} and {@link TransformerFactory}
     * interface implementations are <strong>NOT</strong> guaranteed to be
     * thread safe.</p>
     * 
     * @return the transformer
     * @throws TransformerConfigurationException if there is a problem
     *                                           creating the new transformer
     * @throws TransformerFactoryConfigurationError if there is a problem
     *                                           creating the new transformer
     *                                           factory
     */
    private synchronized Transformer getTransformer() 
            throws TransformerConfigurationException,
            TransformerFactoryConfigurationError {
        if (transformerFactory == null) {
            transformerFactory = TransformerFactory.newInstance();
        }
        return transformerFactory.newTransformer();
    }
    
    private class IgnoringSetDocumentLocatorHandler implements ContentHandler {
        private ContentHandler contentHandler;
        
        public IgnoringSetDocumentLocatorHandler(ContentHandler contentHandler) {
            this.contentHandler = contentHandler;
        }

        // javadoc inherited
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            contentHandler.characters(ch, start, length);
        }

        // javadoc inherited
        public void endDocument() throws SAXException {
            
        }

        // javadoc inherited
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            contentHandler.endElement(uri, localName, name);
        }

     // javadoc inherited
        public void endPrefixMapping(String prefix) throws SAXException {
            contentHandler.endPrefixMapping(prefix);
        }

        // javadoc inherited
        public void ignorableWhitespace(char[] ch, int start, int length)
                throws SAXException {
            contentHandler.ignorableWhitespace(ch, start, length);
        }

        // javadoc inherited
        public void processingInstruction(String target, String data)
                throws SAXException {
            contentHandler.processingInstruction(target, data);
        }

        /**
         * Does nothing
         */
        public void setDocumentLocator(Locator locator) {
            
        }

        // javadoc inherited
        public void skippedEntity(String name) throws SAXException {
            contentHandler.skippedEntity(name);
        }

        // javadoc inherited
        public void startDocument() throws SAXException {
            
        }

        // javadoc inherited
        public void startElement(String uri, String localName, String name,
                Attributes atts) throws SAXException {
            contentHandler.startElement(uri, localName, name, atts);
        }

        // javadoc inherited
        public void startPrefixMapping(String prefix, String uri)
                throws SAXException {
            contentHandler.startPrefixMapping(prefix, uri);
        }

        
    }
    
}
