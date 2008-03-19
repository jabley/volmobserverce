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
 
package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * This class provides a common interface for all internal implementations
 * of MCSInternalContentHandler for use with NamespaceSwitchContentHandler.
 */
public abstract class AbstractMarlinContentHandler
        implements MCSInternalContentHandler{

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AbstractMarlinContentHandler.class);
    /**
     * Class that encapsulates the fields used by AbstractMarlinContentHandlers.
     */
    protected MarlinContentHandlerContext marlinContext;

    /**
     * Creates a new <code>AbstractMarlinContentHandler</code> instance
     */
    protected AbstractMarlinContentHandler() {
        this(null);
    }

    /**
     * Creates a new <code>AbstractMarlinContentHandler</code> instance.
     *
     * @param flowControlManager    a FlowControlManager that can be used to
     *                              suppress SAX events at the root of an
     *                              <code>XMLPipeline</code>. If the source
     *                              of the SAX events is not an
     *                              <code>XMLPipeline</code> null should be
     *                              passed in.
     */ 
    protected AbstractMarlinContentHandler(
            FlowControlManager flowControlManager) {
        marlinContext = new MarlinContentHandlerContext();
        marlinContext.setFlowControlManager(flowControlManager);
    }

    /**
     * Method that sets the ignoreDepth variable to one so that the current
     * element's body is not processed. In addition, if the source of the sax
     * events is an XMLPipeline the events are suppressed at the root of the
     * pipeline.
     */
    protected void skipElementBody() {
        // set the ignore depth to 1. This indicates that all SAX events
        // should be ignored
        marlinContext.setIgnoreDepth(1);
        if (marlinContext.getFlowControlManager() != null) {
            // events are being generated via a pipeline so tell
            // the pipelines flow control manager to suppress
            // all SAX events for the current elements body
            marlinContext.getFlowControlManager().exitCurrentElement();
            if (logger.isDebugEnabled()) {
                logger.debug("Instructing pipeline flow control " +
                        "to skip body of current element");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(this + ": startElemement: skip");
        }
    }

    /**
     * Checks if process is in FlowControl mode and ends it if so
     */
    protected void endFlowControl() {
        final FlowControlManager flowControlManager =
                marlinContext.getFlowControlManager();
        if (flowControlManager != null &&
                flowControlManager.inFlowControlMode()) {
            flowControlManager.handleEndElementEvent();
        }
    }
    
    // Javadoc inherited from ContentHandler interface
    public void ignorableWhitespace(char[] characters, int start, int length)
            throws SAXException {
        // do nothing
    }

    // Javadoc inherited from ContentHandler interface
    public void processingInstruction(String target, String data)
            throws SAXException {
        // do nothing
    }

    // Javadoc inherited from ContentHandler interface
    public void skippedEntity(String name) throws SAXException {
        // do nothing
    }

    // Javadoc inherited from ContentHandler interface
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        // do nothing
    }

    // Javadoc inherited from ContentHandler interface
    public void endPrefixMapping(String prefix) throws SAXException {
        // do nothing
    }

    // Javadoc inherited from ContentHandler interface
    public void startDocument() throws SAXException {
        // do nothing
    }

    // Javadoc inherited from ContentHandler interface
    public void endDocument() throws SAXException {
        // do nothing
    }

    // Javadoc inherited from ContentHandler interface
    public void characters(char[] characters, int start, int length)
            throws SAXException {
        // do nothing
    }

    // Javadoc inherited from MarlinContentHandler interface.
    public void setInitialRequestContext(MarinerRequestContext requestContext) {
        marlinContext.setInitialRequestContext(requestContext);
    }

    // Javadoc inherited.
    public void setDocumentLocator(Locator locator) {
        marlinContext.setLocator(locator);
    }

    // Javadoc inherited.
    public Locator getDocumentLocator() {
        return marlinContext.getLocator();
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

 27-Aug-03	1253/1	doug	VBM:2003082202 Restructured MarlinContentHandler class hierarchy

 15-Aug-03	1111/1	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 13-Aug-03	1048/3	doug	VBM:2003070904 Modified MarlinContentHandlers so that they can control the flow of pipeline SAX events

 23-Jul-03	833/5	adrian	VBM:2003071902 updated javadoc

 23-Jul-03	833/3	adrian	VBM:2003071902 refactored content handlers to extract common methods and fields

 22-Jul-03	833/1	adrian	VBM:2003071902 added marlin support for invocation elements

 ===========================================================================
*/
