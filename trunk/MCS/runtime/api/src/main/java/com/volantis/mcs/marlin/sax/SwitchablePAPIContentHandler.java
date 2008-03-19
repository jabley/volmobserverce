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
/*
 * $Header: /src/voyager/com/volantis/mcs/marlin/sax/SwitchablePAPIContentHandler.java,v 1.1 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-2003  Chris W         VBM:2003030404 - Switches between using the
 *                              PAPIContentHandler and PickleContentHandler
 *                              depending on whether we are in native markup.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A SwitchablePAPIContentHandler is an implementation of MarlinContentHandler
 * that uses either the PAPIContentHandler or the PickleContentHandler
 * depending on whether we are dealing with PAPI elements or elements within a
 * nativemarkup tag. So, in effect, this class decorates MarlinContentHandler.
 * As a result of this we must implement all methods defined in
 * MarlinContentHandler and delegate the calls to the stored PAPIContentHandler
 * and PickleContentHandler
 */
public class SwitchablePAPIContentHandler extends AbstractPAPIContentHandler {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(SwitchablePAPIContentHandler.class);

    /**
     * Reference to a PAPIContentHandler
     */
    private AbstractPAPIContentHandler papiContentHandler;
    
    /**
     * Reference to a PickleContentHandler
     */
    private AbstractPAPIContentHandler pickleContentHandler;
    
    /**
     * Refers to the current MarlinContentHandler that is being used.
     */
    private AbstractPAPIContentHandler currentContentHandler;

    /**
     * The NativeMarkupElementHandler that will be used by this class. The same
     * NativeMarkupElementHandler is used to reduce garbage.
     */
    private NativeMarkupElementHandler nativeMarkupElementHandler;

    /**
     * Creates a <code>SwitchablePAPIContentHandler</code> instance
     */
    public SwitchablePAPIContentHandler() {
        this(null);
    }

    /**
     * Creates a <code>SwitchablePAPIContentHandler</code> instance.
     *
     * @param flowControlManager    used to suppress SAX events at the root of
     *                              an <code>XMLPipeline</code>. If the source 
     *                              of the SAX events is not an
     *                              <code>XMLPipeline</code> null should be
     *                              passed in.
     */
    public SwitchablePAPIContentHandler(FlowControlManager flowControlManager) {
        super(flowControlManager);
        papiContentHandler = new PAPIContentHandler(marlinContext, context,
                                                    elementStackEntry);
        currentContentHandler = papiContentHandler;
    }

                 
    /* JavaDoc inherited from superclass
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, 
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes saxAttributes)
        throws SAXException {
        
        if ("nativemarkup".equals(localName)) {            
            if (context.getNativeMarkupDepth() == 0) {

                if (logger.isDebugEnabled()) {                
                    logger.debug("Switching to nativemarkup mode");                
                }
                
                // As this is the outermost nativemarkup element we need create
                // a papi element.
                // NB This class is the only MarlinContentHandler that knows
                // how to deal with nativemarkup elements.                
                super.startElement(namespaceURI, localName, 
                                   qName, saxAttributes);
                
                // Now that the papi element has been created we can
                // switch to the PickleContentHandler
                if (pickleContentHandler == null) {
                    pickleContentHandler = 
                        new PickleContentHandler(marlinContext, context, 
                                                 elementStackEntry);
                }
                currentContentHandler = pickleContentHandler;
            
            } else {
                // We've dealing with a nested nativemarkup element. Delegate
                // to the MarlinContentHandler that can deal with it.
                currentContentHandler.startElement(namespaceURI, localName,
                                                   qName, saxAttributes);                
            }
                                   
            // Increase the native markup toggle by one.
            context.incrementNativeMarkupDepth();
        } else {
            // Delegate to the MarlinContentHandler that can deal with
            // this element.
            currentContentHandler.startElement(namespaceURI, localName,
                                               qName, saxAttributes);
        }
        
    }

    /* JavaDoc inherited from superclass
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public void endElement(String namespaceURI, String localName,
                           String qName)
        throws SAXException {
        
        // If necessary switch off the nativemarkup toggle
        if ("nativemarkup".equals(localName)) {
            // Decrease the native markup toggle by one
            context.decrementNativeMarkupDepth();
            
            if (context.getNativeMarkupDepth() == 0) {

                if (logger.isDebugEnabled()) {
                    logger.debug( "Switching out of nativemarkup mode");                
                }
                
                // As this is the outermost closing nativemarkup element switch
                // back to the PAPIContentHandler                
                currentContentHandler = papiContentHandler; 
                
                // End the nativemarkup papi element
                // NB This class is the only MarlinContentHandler that knows
                // how to deal with nativemarkup elements.
                super.endElement(namespaceURI, localName, qName);               
            
            } else {
                // We've dealing with a nested nativemarkup element. Delegate
                // to the MarlinContentHandler that can deal with it.
                currentContentHandler.endElement(namespaceURI, localName, 
                                                 qName);                            
            }
        } else {
            // Delegate to the MarlinContentHandler that can deal with 
            // this element
            currentContentHandler.endElement(namespaceURI, localName, qName);            
        }
        
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.marlin.sax.AbstractPAPIContentHandler#getMarlinElementHandler(java.lang.String)
     */
    protected MarlinElementHandler getMarlinElementHandler(String localName) {
        if (nativeMarkupElementHandler == null) {
            nativeMarkupElementHandler = new NativeMarkupElementHandler();        
        }
        return nativeMarkupElementHandler;
    }

    /* As this class decorates MarlinContentHandler, we must implement all
     * methods defined in MarlinContentHandler and delegate the calls to the
     * stored PAPIContentHandler and PickleContentHandler. Hence the need for
     * the methods below.
     */

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] characters, int start, int length)
        throws SAXException {        
        currentContentHandler.characters(characters, start, length);        
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        currentContentHandler.endDocument();       
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(Locator locator) {
        currentContentHandler.setDocumentLocator(locator);        
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.marlin.sax.MarlinContentHandler#setInitialRequestContext(com.volantis.mcs.context.MarinerRequestContext)
     */
    public void setInitialRequestContext(MarinerRequestContext requestContext) {
        currentContentHandler.setInitialRequestContext(requestContext);        
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException {
        currentContentHandler.startDocument();        
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String prefix) throws SAXException {        
        currentContentHandler.endPrefixMapping(prefix);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] characters, int start, int length)
        throws SAXException {        
        currentContentHandler.ignorableWhitespace(characters, start, length);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, 
     * java.lang.String)
     */
    public void processingInstruction(String target, String data)
        throws SAXException {        
        currentContentHandler.processingInstruction(target, data);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(String name) throws SAXException {        
        currentContentHandler.skippedEntity(name);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
     * java.lang.String)
     */
    public void startPrefixMapping(String prefix, String uri)
        throws SAXException {        
        currentContentHandler.startPrefixMapping(prefix, uri);
    }

    // Javadoc inherited from AbstractMarlinContentHandler interface.
    public MarinerRequestContext getCurrentRequestContext() {
        return currentContentHandler.getCurrentRequestContext();
    }

    // Javadoc inherited from MarlinContentHandler interface.
    public Locator getDocumentLocator() {
        return currentContentHandler.getDocumentLocator();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Aug-03	1111/4	chrisw	VBM:2003081306 done rework

 15-Aug-03	1111/2	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 13-Aug-03	1048/2	doug	VBM:2003070904 Modified MarlinContentHandlers so that they can control the flow of pipeline SAX events

 22-Jul-03	833/1	adrian	VBM:2003071902 added marlin support for invocation elements

 23-Jun-03	459/3	mat	VBM:2003061910 Changed marlin-canvas-schema to 2003061910

 23-Jun-03	459/1	mat	VBM:2003061910 Change getContentWriter() to return correct nativeWriter for Native markup elements

 ===========================================================================
*/
