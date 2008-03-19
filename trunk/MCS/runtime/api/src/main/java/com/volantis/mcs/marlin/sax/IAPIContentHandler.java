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
import com.volantis.mcs.integration.iapi.InvokeAttributes;
import com.volantis.mcs.integration.iapi.InvokeElement;
import com.volantis.mcs.integration.iapi.ArgumentElement;
import com.volantis.mcs.integration.iapi.ArgumentsElement;
import com.volantis.mcs.integration.iapi.ArgumentAttributes;
import com.volantis.mcs.integration.iapi.IAPIAttributes;
import com.volantis.mcs.integration.iapi.IAPIElement;
import com.volantis.mcs.integration.iapi.IAPIException;
import com.volantis.mcs.integration.iapi.IAPIConstants;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.sax.ExtendedSAXParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.Stack;

/**
 * This is an implementation of MarlinContentHandler that deals with normal
 * IAPI elements.
 */
public class IAPIContentHandler extends AbstractMarlinContentHandler {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(IAPIContentHandler.class);
           
    /**
     * This object is used for communicating with the ContentHandlerContext.
     */
    protected IAPIElementStackEntry elementStackEntry;
    
    /**
     * A Stack of IAPIElementStackEntry Objects.
     */ 
    protected Stack elementStack = new Stack();

    /**
     * Creates a new <code>IAPIContentHandler</code> instance.
     */
    public IAPIContentHandler() {
        this(null);
    }

    /**
     * Creates a new <code>IAPIContentHandler</code> instance. 
     * @param flowControlManager a FlowControlManager that can be
     * used to suppress SAX events at the root of an <code>XMLPipeline</code>.
     * If the source of the SAX events is not an <code>XMLPipeline</code>
     * null should be passed in
     */ 
    public IAPIContentHandler(FlowControlManager flowControlManager) {
        super(flowControlManager);
    }
    
    // Javadoc inherited from AbstractMarlinContentHandler interface.
    public MarinerRequestContext getCurrentRequestContext() {
        return marlinContext.getInitialRequestContext();
    }

    // Javadoc inherited from ContentHandler interface
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes saxAttributes)
            throws SAXException {
        if (processElement()) {            
            if ("invoke".equals(localName)) {
                pushInvokeEntryOnStack(saxAttributes);
            } else if ("arguments".equals(localName)) {
                pushArgumentsEntryOnStack(saxAttributes);
            } else if ("argument".equals(localName)) {
                pushArgumentEntryOnStack(saxAttributes);
            } else {
                throw new ExtendedSAXParseException("Unknown IAPIElement: " +
                        localName, marlinContext.getLocator());
            }
            callIAPIElementStart();
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
     * Push an InvokeElement onto the top of our stack of elements.
     * @param saxAttributes The SAX Attributes with the infomation required
     * to create our IAPIElement     
     */ 
    protected void pushInvokeEntryOnStack(Attributes saxAttributes) {
        InvokeAttributes attrs = new InvokeAttributes();
        attrs.setName(saxAttributes.getValue("pluginName"));
        attrs.setMethodName(saxAttributes.getValue("methodName"));
        
        InvokeElement element = new InvokeElement();
        
        elementStackEntry = new IAPIElementStackEntry(element, attrs);
        elementStack.push(elementStackEntry);
    }
    
    /**
     * Push an ArgumentsElement onto the top of our stack of elements.
     * @param saxAttributes The SAX Attributes with the infomation required
     * to create our IAPIElement    
     */
    protected void pushArgumentsEntryOnStack(Attributes saxAttributes) {
        ArgumentsElement element = new ArgumentsElement();

        elementStackEntry = new IAPIElementStackEntry(element, null);
        elementStack.push(elementStackEntry);
    }
    
    /**
     * Push an ArgumentElement onto the top of our stack of elements.
     * @param saxAttributes The SAX Attributes with the infomation required
     * to create our IAPIElement    
     */
    protected void pushArgumentEntryOnStack(Attributes saxAttributes) {
        ArgumentElement element = new ArgumentElement();

        ArgumentAttributes attrs = new ArgumentAttributes();
        attrs.setName(saxAttributes.getValue("name"));
        attrs.setValue(saxAttributes.getValue("value"));
        
        elementStackEntry = new IAPIElementStackEntry(element, attrs);
        elementStack.push(elementStackEntry);
    }

    /**
     * Calls the elementStart() method on the appropriate IAPIElement.
     * @throws SAXParseException
     */
    protected void callIAPIElementStart() throws SAXParseException {
        MarinerRequestContext requestContext = 
            marlinContext.getInitialRequestContext();

        IAPIElement element = elementStackEntry.getElement();
        IAPIAttributes attributes = elementStackEntry.getAttributes();

        try {
            // Invoke the IAPIElement's elementStart method.
            int result = element.elementStart(requestContext, attributes);

            switch (result) {
                case IAPIConstants.PROCESS_ELEMENT_BODY:
                    // Do nothing.
                    break;

                case IAPIConstants.SKIP_ELEMENT_BODY:               
                    // call skipElementBody() and return immediately.
                    skipElementBody();
                    return;

                default :
                    // An unknown return code was received from the PAPIElement.
                    throw new ExtendedSAXParseException("Unknown return code of " +
                            result + " from " + element + " startElement",
                            marlinContext.getLocator());
            }
        } catch (IAPIException e) {
            // Wrap the exception in a SAXException.
            throw new ExtendedSAXParseException(null, marlinContext.getLocator(), e);
        }
    }

    // Javadoc inherited from ContentHandler interface
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException {
        if (processedElement()) {
            
            // end FlowControl mode
            endFlowControl();
            
            // Pop the information about the current element.
            elementStackEntry = (IAPIElementStackEntry) elementStack.pop();

            callIAPIElementEnd();
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
                    logger.debug(this + ": endElement: ignoring: depth = " + 
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
     * Calls the elementEnd() method on the appropriate IAPIElement.          
     * @throws SAXException
     */
    protected void callIAPIElementEnd() throws SAXException {

        // Get the current request context
        MarinerRequestContext requestContext =
            marlinContext.getInitialRequestContext();

        IAPIElement element = elementStackEntry.element;
        IAPIAttributes attributes = elementStackEntry.attributes;

        try {
            // Invoke the PAPIElement's elementEnd method.
            int result = element.elementEnd(requestContext, attributes);
        
            // Reset the element, make sure that it uses the same context as 
            //was passed to the elementStart and elementEnd methods.
            element.elementReset(requestContext);
        
            // Reset the attributes.
            if (attributes != null) {
                attributes.reset();    
            }
            

            switch (result) {
                case IAPIConstants.CONTINUE_PROCESSING:
                    // Do nothing.
                    break;

                case IAPIConstants.ABORT_PROCESSING:
                    // Set the abort flag.
                    marlinContext.setAbort(true);
                    if (logger.isDebugEnabled()) {
                        logger.debug(this + ": endElemement: abort");
                    }
                    return;

                default :
                    // An unknown return code was received from the IAPIElement.
                    throw new ExtendedSAXParseException("Unknown return code of "
                            + result + " from " + element + " endElement",
                            marlinContext.getLocator());
            }
        } catch (IAPIException e) {
            // Wrap the exception in a SAXException.
            throw new ExtendedSAXParseException(null, marlinContext.getLocator(), e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Aug-03	1111/1	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 13-Aug-03	1048/3	doug	VBM:2003070904 Modified MarlinContentHandlers so that they can control the flow of pipeline SAX events

 23-Jul-03	833/3	adrian	VBM:2003071902 refactored content handlers to extract common methods and fields

 22-Jul-03	833/1	adrian	VBM:2003071902 added marlin support for invocation elements

 ===========================================================================
*/
