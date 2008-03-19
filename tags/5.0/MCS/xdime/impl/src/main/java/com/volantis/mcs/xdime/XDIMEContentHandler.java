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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.marlin.sax.MCSInternalContentHandler;
import com.volantis.mcs.xdime.schema.XDIME2Elements;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.SchemaNamespaces;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.InternalXMLPipelineFactory;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.pipeline.sax.flow.FlowController;
import com.volantis.xml.sax.ExtendedSAXException;
import com.volantis.xml.sax.ExtendedSAXParseException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.io.IOException;

public class XDIMEContentHandler implements MCSInternalContentHandler, FlowController {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    XDIMEContentHandler.class);

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(XDIMEContentHandler.class);

    private static final SchemaNamespaces XDIME_2_ELEMENTS =
            XDIME2Elements.getDefaultInstance();

    /**
     * Class that encapsulates the fields used by XDIMEContentHandlers.
     */
    protected XDIMEContextInternal xdimeContext;

    /**
     * Factory for creating {@link XDIMEElement} and {@link XDIMEAttributes}.
     */
    protected XDIMEElementHandler handler;

    private boolean inFlowControlMode;

    /**
     * Initialize a new instance.
     */
    public XDIMEContentHandler() {
        this(null, (XDIMEContextInternal)
                XDIMEContextFactory.getDefaultInstance().createXDIMEContext(),
                XDIMEElementHandler.getDefaultInstance());
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param flowControlManager    a FlowControlManager that can be used to
     *                              suppress SAX events at the root of an
     *                              XMLPipeline. If the source of the SAX
     *                              events is not an XMLPipeline> null should
     *                              be supplied.
     * @param xdimeContext          the context which encapsulates the state of
     *                              the request being processed
     * @param handler               the {@link XDIMEElementHandler} that should
     *                              be used to create xdime elements and
     *                              attributes
     */
    public XDIMEContentHandler(FlowControlManager flowControlManager,
            XDIMEContextInternal xdimeContext, XDIMEElementHandler handler) {

        this.xdimeContext = xdimeContext;
        if (flowControlManager == null) {
            InternalXMLPipelineFactory factory =
                    InternalXMLPipelineFactory.getInternalInstance();
            flowControlManager = factory.createFlowControlManager();
        }
        this.xdimeContext.setFlowControlManager(flowControlManager);

        flowControlManager.addFlowController(this);

        this.handler = handler;
    }

    // Javadoc inherited.
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
            throws SAXException {

        if (inFlowControlMode) {
            FlowControlManager flowControlManager =
                    xdimeContext.getFlowControlManager();

            flowControlManager.handleStartElementEvent();
        } else {
            try {
                ElementType elementType = XDIME_2_ELEMENTS.getElementType(
                        namespaceURI, localName);

                // create the XDIME element
                XDIMEElement element =
                        handler.createXDIMEElement(elementType, xdimeContext);

                // create the XDIME attributes
                XDIMEAttributes attributes =
                        handler.createXDIMEAttributes(elementType, atts);

                // push this element onto the stack
                xdimeContext.pushElement(element);

                // start processing the element
                XDIMEResult result =
                        element.elementStart(xdimeContext, attributes);

                if (XDIMEResult.SKIP_ELEMENT_BODY.equals(result)) {
                    skipElementBody();
                } else if (!XDIMEResult.PROCESS_ELEMENT_BODY.equals(result)) {
                    // An invalid return code was received from the
                    // XDIMEElement as the only other acceptable option is
                    // to CONTINUE_PROCESSING
                    throw new ExtendedSAXParseException(
                            EXCEPTION_LOCALIZER.format("xdime-result-invalid",
                            new Object[]{result, "elementStart",
                                         element}),
                            xdimeContext.getDocumentLocator());
                }
            } catch (IllegalArgumentException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(e);
                }
                throw new ExtendedSAXException(e);
            } catch (XDIMEException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(e);
                }
                throw new ExtendedSAXException(e);
            }
        }
    }

    /**
     * Method that sets the ignoreDepth variable to one so that
     * the current elements body is not processed. In addition, if
     * the source of the sax events is an XMLPipeline the events
     * are suppressed at the root of the pipeline.
     */
    protected void skipElementBody() {
        if (xdimeContext.getFlowControlManager() != null) {
            // events are being generated via a pipeline so tell
            // the pipelines flow control manager to suppress
            // all SAX events for the current elements body
            xdimeContext.getFlowControlManager().exitCurrentElement();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Instructing pipeline flow control " +
                        "to skip body of current element");
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(this + ": startElemement: skip");
        }
    }

    // Javadoc inherited.
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {

        FlowControlManager flowControlManager =
                xdimeContext.getFlowControlManager();
        if (!inFlowControlMode ||
                flowControlManager.handleEndElementEvent()) {
            try {

                // pop this element off the stack
                XDIMEElement element = xdimeContext.popElement();

                XDIMEResult result = element.elementEnd(xdimeContext);

                if (XDIMEResult.ABORT_PROCESSING.equals(result)) {
                    // set the abort flag
                    xdimeContext.setAbort(true);
                } else if (!XDIMEResult.CONTINUE_PROCESSING.equals(result)) {
                    // An invalid return code was received from the
                    // XDIMEElement as the only other acceptible option is
                    // to CONTINUE_PROCESSING
                    throw new ExtendedSAXParseException(
                            EXCEPTION_LOCALIZER.format("xdime-result-invalid",
                            new Object[]{result, "elementEnd",
                                         element}),
                            xdimeContext.getDocumentLocator());
                }

            } catch (XDIMEException e) {
                throw new ExtendedSAXException(e);
            }
        }
    }

    // Javadoc inherited from ContentHandler interface
    public void setDocumentLocator(Locator locator) {
        xdimeContext.setDocumentLocator(locator);
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

        if (!inFlowControlMode) {

            try {
                // Get the content writer for the element.
                FastWriter writer = xdimeContext.getContentWriter();

                // Write the content out to the body of the element.
                writer.write(characters, start, length);
            } catch (IOException e) {
                throw new ExtendedSAXParseException(null,
                        xdimeContext.getDocumentLocator(), e);
            }
        }
    }

    // Javadoc inherited from MCSInternalContentHandler interface.
    public MarinerRequestContext getCurrentRequestContext() {
        return xdimeContext.getInitialRequestContext();
    }

    // Javadoc inherited from MCSInternalContentHandler interface.
    public void setInitialRequestContext(MarinerRequestContext requestContext) {
        xdimeContext.setInitialRequestContext(requestContext);
    }

    // Javadoc inherited from MCSInternalContentHandler interface.
    public Locator getDocumentLocator() {
        return xdimeContext.getDocumentLocator();
    }

    public void beginFlowControl() {
        inFlowControlMode = true;
    }

    public void endFlowControl() {
        inFlowControlMode = false;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Dec-05	10523/1	ianw	VBM:2005112406 Fix uo XDIMCP Title element

 01-Dec-05	10514/1	ianw	VBM:2005112406 Fixed XDIMECP Title elemement

 10-Oct-05	9673/2	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 ===========================================================================
*/
