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
 * $Header: /src/voyager/com/volantis/mcs/marlin/sax/AbstractPAPIContentHandler.java,v 1.1 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Paul            VBM:2002112214 - Created to convert SAX 2
 *                              events into PAPI calls.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 24-Apr-03    Chris W         VBM:2003030404 - Old MarlinContentHandler
 *                              renamed as this abstract class. New
 *                              MarlinContentHandler is an interface
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.context.MarinerRequestContext;

import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIConstants;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.sax.ExtendedSAXParseException;
import com.volantis.xml.utilities.sax.SAXUtils;

import java.io.IOException;
import java.io.Writer;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This is the abstract superclass of all concrete implementations of
 * MarlinContentHandler that process PAPI elements. Methods common to all
 * implementations appear here.
 */
public abstract class AbstractPAPIContentHandler
        extends AbstractMarlinContentHandler {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AbstractPAPIContentHandler.class);

    /**
     * This object contains some of the state of the content handler.
     * <p>
     * It is separated out from the MarlinContentHandler so that it can be made
     * available to other internal classes without exposing internal details of
     * this class.
     * <p>
     * The state could be stored in this class and made available through package
     * private methods but that requires that all users of this class belong to
     * the same package which may not be desirable or possible.
     * <p>
     * It only contains information which is of use to other classes, it will not
     * contain information internal to this object.
     */
    protected PAPIContentHandlerContext context;

    /**
     * This object is used for communicating with the PAPIContentHandlerContext.
     */
    protected ElementStackEntry elementStackEntry;

    /**
     * Create a new <code>MarlinContentHandler</code>.
     */
    protected AbstractPAPIContentHandler() {
        this(null);
    }

    /**
     * Create a new <code>MarlinContentHandler</code>.
     * <p>
     * Before the object can be used its initial MarinerRequestContext must have
     * been set.
     * @param flowControlManager        a FlowControlManager that can be used
     *                                  to suppress SAX events at the root of
     *                                  an <code>XMLPipeline</code>. If the
     *                                  source of the SAX events is not an
     *                                  <code>XMLPipeline</code> null should be
     *                                  passed in.
     */
    protected AbstractPAPIContentHandler(FlowControlManager flowControlManager) {
        super(flowControlManager);
        context = new PAPIContentHandlerContext();
        elementStackEntry = new ElementStackEntry();
    }

    // Javadoc inherited from super class.
    public void startDocument() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug(this + ": startDocument");
        }

        // Push the initial request context onto the stack.
        context.pushRequestContext(marlinContext.getInitialRequestContext());
    }

    // Javadoc inherited from super class.
    public void endDocument() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug(this + ": endDocument");
        }

        // Pop the initial request context off the stack.
        context.popRequestContext();
    }

    // Javadoc inherited from super class.
    public void characters(char[] characters, int start, int length)
            throws SAXException {

        // Check abort status.
        if (marlinContext.isAbort()) {
            if (logger.isDebugEnabled()) {
                logger.debug(this + ": characters: aborting");
            }

            return;
        }

        // Check the ignore depth.
        if (marlinContext.getIgnoreDepth() > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug(this + ": characters: ignoring: depth = " +
                    marlinContext.getIgnoreDepth());
            }

            return;
        }

        // Get the information about the current element.
        context.getElementEntry(elementStackEntry);

        MarlinElementHandler handler = elementStackEntry.handler;

        // If the element does not have a mixed content model then we can ignore
        // the characters.
        // If we only have white space discard them else throw an exception.
        if (handler == null || !handler.canContainCharacterData()) {

            if (SAXUtils.isWhitespaceCharacters(characters, start, length)) {
                return;
            }

            throw new ExtendedSAXParseException("Unexpected characters '" +
                    new String(characters, start, length) + "'",
                    marlinContext.getLocator());

        }

        // Get the current request context
        MarinerRequestContext requestContext = context.getRequestContext();

        PAPIElement element = elementStackEntry.element;
        PAPIAttributes attributes = elementStackEntry.attributes;

        try {
            // Get the content writer for the element.
            Writer writer = element.getContentWriter(requestContext, attributes);

            // Write the content out to the body of the element.
            writer.write(characters, start, length);
        } catch (PAPIException e) {
            throw new ExtendedSAXParseException(null, marlinContext.getLocator(), e);
        } catch (IOException e) {
            throw new ExtendedSAXParseException(null, marlinContext.getLocator(), e);
        }
    }

    /* JavaDoc inherited from superclass
    * This is a template method getMarlinElementHandler() is defined in this
    * class's subclasses.
    * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
    * java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes saxAttributes)
            throws SAXException {

        if (processElement()) {
            // TODO: Make sure that the namespaceURI matches the marlin cdm
            // namespace URI.

            // Get the MarlinElementHandler for the element identified by the
            // localName, if no suitable one exists then it is an error.
            MarlinElementHandler handler = getMarlinElementHandler(localName);
            if (handler == null) {
                throw new ExtendedSAXParseException("Unknown element '" + localName + "'",
                    marlinContext.getLocator());
            }

            pushElementOnStack(handler, saxAttributes);
            callPAPIElementStart();
        }
    }

    /**
     * Determine whether or not the current element should be processed.
     * @return boolean is true if element should be processed, false otherwise.
     */
    protected boolean processElement() {
        // Check abort status.
        if (marlinContext.isAbort()) {
            if (logger.isDebugEnabled()) {
                logger.debug(this + ": startElement: aborting");
            }

            return false;
        }

        // Check the ignore depth. Doing this here means that we will not do any
        // checking of ignored elements but is more efficient.
        if (marlinContext.getIgnoreDepth() > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                    this + ": startElement: ignoring: depth = " +
                    marlinContext.getIgnoreDepth());
            }

            marlinContext.increaseIgnoreDepth();
            return false;
        }

        // We've got here so this element must be processed.
        return true;
    }

    /**
     * Returns the appropriate MarlinElementHandler
     * @param localName A String containing the name of the current tag
     * @return MarlinElementHandler
     */
    protected abstract MarlinElementHandler
            getMarlinElementHandler(String localName);

    /**
     * Use the MarlinElementHandler to push the appropriate PAPIElement onto
     * the stack.
     */
    protected void pushElementOnStack(MarlinElementHandler handler,
                                      Attributes saxAttributes) throws SAXException {

        // Create an instance of the PAPIAttributes derived class specific to
        // the current element.
        PAPIAttributes attributes = handler.createPAPIAttributes(context);

        // Initialize the PAPIAttributes from the SAX2 Attributes.
        handler.initializePAPIAttributes(context, saxAttributes, attributes);

        // Make sure that the context is correctly prepared for the elements,
        // this may change the context so do not rely on any previously
        // retrieved values being correct.
        handler.beforePAPIElement(context, attributes);

        // Create an instance of the PAPIElement derived class specific to the
        // current element.
        PAPIElement element = handler.createPAPIElement(context);

        // Initialise the entry to push on the stack.
        //elementStackEntry.localName = localName;
        elementStackEntry.handler = handler;
        elementStackEntry.element = element;
        elementStackEntry.attributes = attributes;

        // Push all the information about the element onto the stack.
        context.pushElementEntry(elementStackEntry);
    }

    /**
     * Calls the elementStart() method on the appropriate PAPIElement.
     * @throws SAXParseException
     */
    protected void callPAPIElementStart() throws SAXParseException {
        MarinerRequestContext requestContext = context.getRequestContext();

        PAPIElement element = elementStackEntry.element;
        PAPIAttributes attributes = elementStackEntry.attributes;

        try {
            // Invoke the PAPIElement's elementStart method.
            int result = element.elementStart(requestContext, attributes);

            switch (result) {
                case PAPIConstants.PROCESS_ELEMENT_BODY:
                    // Do nothing.
                    break;

                case PAPIConstants.SKIP_ELEMENT_BODY:
                    // call skipElementBody() and return immediately.
                    skipElementBody();
                    return;

                default :
                    // An unknown return code was received from the PAPIElement.
                    throw new ExtendedSAXParseException("Unknown return code of " +
                            result + " from " + element + " startElement",
                            marlinContext.getLocator());
            }
        } catch (PAPIException e) {
            // Wrap the exception in a SAXException.
            throw new ExtendedSAXParseException(null, marlinContext.getLocator(), e);
        }
    }

    /* JavaDoc inherited from superclass
    * This is a template method getMarlinElementHandler() is defined in this
    * class's subclasses.
    * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
    * java.lang.String, java.lang.String)
    */
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException {

        if (processedElement()) {
            // Sanity check: TODO Make sure that the namespaceURI matches the
            // marlin cdm namespace URI.

            // end FlowControl mode
            endFlowControl();
            
            // Pop the information about the current element.
            context.popElementEntry(elementStackEntry);

            MarlinElementHandler handler = elementStackEntry.handler;

            // Sanity check: Make sure that the element matches the one that we
            // popped off the stack.
            MarlinElementHandler check = getMarlinElementHandler(localName);
            if (check != handler) {
                throw new ExtendedSAXParseException("Element " + localName +
                        " does not match popped " + handler,
                        marlinContext.getLocator());
            }

            callPAPIElementEnd(handler);
        }
    }

    /**
     * Determine whether or not the current element was processed by
     * startElement(). This method is analogous to processElement().
     * @return boolean is true if element should be processed, false otherwise.
     */
    protected boolean processedElement() {
        // Check abort status.
        if (marlinContext.isAbort()) {
            if (logger.isDebugEnabled()) {
                logger.debug(this + ": endElement: aborting");
            }

            return false;
        }

        // Check the ignore depth. Doing this here means that we will not do any
        // checking of ignored elements but is more efficient.
        if (marlinContext.getIgnoreDepth() > 0) {
            marlinContext.decreaseIgnoreDepth();
            if (marlinContext.getIgnoreDepth() > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            this + ": endElement: ignoring: depth = " +
                            marlinContext.getIgnoreDepth());
                }

                return false;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug(this + ": endElement: skipped");
                }
            }
        }

        // If we get here then we did process the element.
        return true;
    }

    /**
     * Calls the elementEnd() method on the appropriate PAPIElement.
     * @param handler
     * @throws SAXException
     */
    protected void callPAPIElementEnd(MarlinElementHandler handler)
            throws SAXException {

        // Get the current request context
        MarinerRequestContext requestContext = context.getRequestContext();

        PAPIElement element = elementStackEntry.element;
        PAPIAttributes attributes = elementStackEntry.attributes;

        try {
            // Invoke the PAPIElement's elementEnd method.
            int result = element.elementEnd(requestContext, attributes);

            // Reset the element, make sure that it uses the same context as was
            // passed to the elementStart and elementEnd methods.
            element.elementReset(requestContext);

            // Make sure that the context is correctly cleaned up for the
            // elements, this may change the context so do not rely on any
            // previously retrieved values being correct.
            handler.afterPAPIElement(context, attributes);

            // Reset the attributes.
            attributes.reset();

            switch (result) {
                case PAPIConstants.CONTINUE_PROCESSING:
                    // Do nothing.
                    break;

                case PAPIConstants.ABORT_PROCESSING:
                    // Set the abort flag.
                    marlinContext.setAbort(true);
                    if (logger.isDebugEnabled()) {
                        logger.debug(this + ": endElemement: abort");
                    }
                    return;

                default :
                    // An unknown return code was received from the PAPIElement.
                    throw new ExtendedSAXParseException("Unknown return code of "
                            + result + " from " + element + " endElement",
                            marlinContext.getLocator());
            }
        } catch (PAPIException e) {
            // Wrap the exception in a SAXException.
            throw new ExtendedSAXParseException(null, marlinContext.getLocator(), e);
        }
    }

    // Javadoc inherited from AbstractMarlinContentHandler interface.
    public MarinerRequestContext getCurrentRequestContext() {
        MarinerRequestContext result = context.getRequestContext();
        if (result == null) {
            result = marlinContext.getInitialRequestContext();
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Nov-05	10090/1	pabbott	VBM:2005103105 White space collapse problem

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 11-Apr-05	7376/1	allan	VBM:2005031101 SmartClient bundler - commit for testing

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 27-Aug-03	1253/1	doug	VBM:2003082202 Restructured MarlinContentHandler class hierarchy

 15-Aug-03	1111/3	chrisw	VBM:2003081306 done rework

 15-Aug-03	1111/1	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 13-Aug-03	1048/3	doug	VBM:2003070904 Modified MarlinContentHandlers so that they can control the flow of pipeline SAX events

 23-Jul-03	833/7	adrian	VBM:2003071902 refactored content handlers to extract common methods and fields

 22-Jul-03	833/5	adrian	VBM:2003071902 added marlin support for invocation elements

 ===========================================================================
*/
