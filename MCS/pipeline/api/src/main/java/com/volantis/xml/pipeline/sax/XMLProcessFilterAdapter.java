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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-03-03     Doug            VBM:2003030405 - Created. Adapter class that
 *                              "Adapts" an XMLProcess to a XMLFilter.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

import java.io.IOException;

/**
 * This Adapter class allows clients to treat an XMLProcess as an XMLFilter.
 * In order to make this adapter useful it also implements the ContetnHandler
 * ErrorHandler, DTDHandler and EntityResolver interfaces. This allows these
 * Filters to be chained together.
 *
 * Note: the XMLProcess being adapted should not be chained to another
 * XMLProcess, that is the getNextProcess() method must return null. This class can
 * be altered to lift this restriction if needed.
 */
public class XMLProcessFilterAdapter
        implements XMLFilter, ContentHandler, ErrorHandler,
        DTDHandler, EntityResolver {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     *  The XMLProcess that is being adapted to an XMLFilter
     */
    private XMLProcess adaptee;

    /**
     * An XMLHandlerAdapter so that clients can register various sax
     * event handlers to handle the output generated.
     */
    private XMLHandlerAdapter splitter;

    /**
     * The XMLReader that is the parent of this XMLFilter
     */
    private XMLReader parent;

    /**
     * The DTDHandler that SAX DTD events will be forwarded to.
     */
    private DTDHandler dtdHandler;

    /**
     * The entity resolver that will be used to resolve entities.
     */
    private EntityResolver entityResolver;

    /**
     * Constructs an new XMLProcessFilterAdapter instance
     * @param adaptee the XMLProcess that is being adapted to a XMLFilter.
     * This XMLProcess should not be chained to another XMLProcess (ie the
     * getNextProcess() method must return null).
     */
    public XMLProcessFilterAdapter(XMLProcess adaptee) {

        this.adaptee = adaptee;
        if (null != adaptee.getNextProcess()) {
            throw new IllegalArgumentException("The XMLProcess being "
                                               + "adapted cannot be chained to annother XMLProcess");
        }
        // Construct the XMLHandlerAdapter that allows the
        // events from the process to be delivered to individual event
        // handlers as opposed to the next XMLProcess
        splitter = new XMLHandlerAdapter();
        // set the Splitter as the next XMLProcess for the adaptee.
        this.adaptee.setNextProcess(splitter);
    }

    /**
     * Method that returns the XMLProcess that is being adapted.
     * @return an XMLProcess
     */
    protected XMLProcess getXMLProcess() {
        return adaptee;
    }

    //=========================================================================
    // ContentHandler implementation
    //=========================================================================

    // javadoc inherited from the ContentHandler interface
    public void characters(char ch[], int start, int length)
            throws SAXException {
        adaptee.characters(ch, start, length);
    }

    // javadoc inherited from the ContentHandler interface
    public void endDocument() throws SAXException {
        adaptee.endDocument();
    }

    // javadoc inherited from the ContentHandler interface
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        adaptee.endElement(namespaceURI, localName, qName);
    }

    // javadoc inherited from the ContentHandler interface
    public void endPrefixMapping(String prefix) throws SAXException {
        adaptee.endPrefixMapping(prefix);
    }

    // javadoc inherited from the ContentHandler interface
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        adaptee.ignorableWhitespace(ch, start, length);
    }

    // javadoc inherited from the ContentHandler interface
    public void processingInstruction(String target, String data)
            throws SAXException {
        adaptee.processingInstruction(target, data);
    }

    // javadoc inherited from the ContentHandler interface
    public void setDocumentLocator(Locator locator) {
        adaptee.setDocumentLocator(locator);
    }

    // javadoc inherited from the ContentHandler interface
    public void skippedEntity(String name) throws SAXException {
        adaptee.skippedEntity(name);
    }

    // javadoc inherited from the ContentHandler interface
    public void startDocument() throws SAXException {
        adaptee.startDocument();
    }

    // javadoc inherited from the ContentHandler interface
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        adaptee.startElement(namespaceURI, localName, qName, atts);
    }

    // javadoc inherited from the ContentHandler interface
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        adaptee.startPrefixMapping(prefix, uri);
    }

    //=========================================================================
    // ErrorHandler implementation
    //=========================================================================

    // javadoc inherited from the ErrorHandler interface
    public void error(SAXParseException exception) throws SAXException {
        adaptee.error(exception);
    }

    // javadoc inherited from the ErrorHandler interface
    public void fatalError(SAXParseException exception) throws SAXException {
        adaptee.fatalError(exception);
    }

    // javadoc inherited from the ErrorHandler interface
    public void warning(SAXParseException exception) throws SAXException {
        adaptee.warning(exception);
    }

    //=========================================================================
    // DTDHandler implementation
    //=========================================================================

    // javadoc inherited from the DTDHandler interface
    public void notationDecl(String name,
                             String publicId,
                             String systemId) throws SAXException {
        if (null != dtdHandler) {
            dtdHandler.notationDecl(name, publicId, systemId);
        }
    }

    // javadoc inherited from the DTDHandler interface
    public void unparsedEntityDecl(String name,
                                   String publicId,
                                   String systemId,
                                   String notationName) throws SAXException {
        if (null != dtdHandler) {
            dtdHandler.unparsedEntityDecl(name, publicId,
                                          systemId, notationName);
        }
    }

    //=========================================================================
    // EntityResolver implementation
    //=========================================================================

    // javadoc inherited from the EntityResolver interface
    public InputSource resolveEntity(String publicId,
                                     String systemId)
            throws SAXException, IOException {
        return (null == entityResolver) ? null :
                entityResolver.resolveEntity(publicId, systemId);
    }

    //=========================================================================
    // XMLReader implementation
    //=========================================================================

    // javadoc inherited from the XMLReader interface
    public ContentHandler getContentHandler() {
        // The adaptees content events are forwarded to the XMLHandlerAdapter
        // so get the ContentHandler from the splitter.
        return splitter.getContentHandler();
    }

    // javadoc inherited from the XMLReader interface
    public void setContentHandler(ContentHandler handler) {
        // The adaptees content events are forwarded to the XMLHandlerAdapter
        // so set the ContentHandler on the splitter.
        splitter.setContentHandler(handler);
    }

    // javadoc inherited from the XMLReader interface
    public DTDHandler getDTDHandler() {
        return dtdHandler;
    }

    // javadoc inherited from the XMLReader interface
    public void setDTDHandler(DTDHandler handler) {
        dtdHandler = handler;
    }

    // javadoc inherited from the XMLReader interface
    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    // javadoc inherited from the XMLReader interface
    public void setEntityResolver(EntityResolver resolver) {
        entityResolver = resolver;
    }

    // javadoc inherited from the XMLReader interface
    public ErrorHandler getErrorHandler() {
        // The adaptees Error events are forwarded to the XMLHandlerAdapter
        // so get the ErrorHandler from the splitter.
        return splitter.getErrorHandler();
    }

    // javadoc inherited from the XMLReader interface
    public void setErrorHandler(ErrorHandler handler) {
        // The adaptees error events are forwarded to the XMLHandlerAdapter
        // so set the ErrorHandler on the splitter.
        splitter.setErrorHandler(handler);
    }

    // javadoc inherited from the XMLReader interface
    public boolean getFeature(String name)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        if (null != parent) {
            return parent.getFeature(name);
        } else {
            throw new SAXNotRecognizedException("Feature : " + name);
        }
    }

    // javadoc inherited from the XMLReader interface
    public void setFeature(String name, boolean value)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        if (null != parent) {
            parent.setFeature(name, value);
        }
    }

    // javadoc inherited from the XMLReader interface
    public Object getProperty(String name)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        if (null != parent) {
            return parent.getProperty(name);
        } else {
            throw new IllegalArgumentException("Property ; " + name);
        }
    }

    // javadoc inherited from the XMLReader interface
    public void setProperty(String name, Object value)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        if (null != parent) {
            parent.setProperty(name, value);
        }
    }

    // javadoc inherited from the XMLReader interface
    public void parse(InputSource input)
            throws IOException,
            SAXException {

        if (null == parent) {
            throw new IllegalStateException("Cannot perform parse as not " +
                                            "parent has been set");
        }
        // ensure that this filter has registered for events with its parent
        // before the parse is performed
        parent.setEntityResolver(this);
        parent.setDTDHandler(this);
        parent.setContentHandler(this);
        parent.setErrorHandler(this);
        parent.parse(input);
    }

    // javadoc inherited from the XMLReader interface
    public void parse(String systemId)
            throws IOException,
            SAXException {
        parse(new InputSource(systemId));
    }

    //=========================================================================
    // XMLFilter implementation
    //=========================================================================

    // javadoc inherited from the XMLFilter interface
    public void setParent(XMLReader parent) {
        this.parent = parent;
    }

    // javadoc inherited from the XMLFilter interface
    public XMLReader getParent() {
        return parent;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 ===========================================================================
*/
