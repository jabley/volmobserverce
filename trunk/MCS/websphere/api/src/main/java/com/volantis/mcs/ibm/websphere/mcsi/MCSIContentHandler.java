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
 
package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.marlin.sax.AbstractMarlinContentHandler;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.sax.ExtendedSAXParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.volantis.mcs.papi.PAPIException;
import java.util.Stack;

/**
 * This is an implementation of MarlinContentHandler that deals with normal
 * MCSI elements.
 */
public class MCSIContentHandler extends AbstractMarlinContentHandler {
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(MCSIContentHandler.class);
           
    /**
     * This object is used for communicating with the ContentHandlerContext.
     */
    protected MCSIElementStackEntry elementStackEntry;
    
    /**
     * A Stack of MCSIElementStackEntry Objects.
     */ 
    protected Stack elementStack = new Stack();

    /**
     * Creates a new <code>MCSIContentHandler</code> instance.
     */
    public MCSIContentHandler() {
        this(null);
    }

    /**
     * Creates a new <code>MCSIContentHandler</code> instance. 
     * @param flowControlManager a FlowControlManager that can be
     * used to suppress SAX events at the root of an <code>XMLPipeline</code>. 
     * If the source of the SAX events is not an <code>XMLPipeline</code>
     * null should be passed in
     */ 
    public MCSIContentHandler(FlowControlManager flowControlManager) {
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
            if ("portlet-context".equals(localName)) {
                pushPortletContextEntryOnStack(saxAttributes);
            } else if ("jdbc-policies".equals(localName)) {
                pushJDBCPoliciesEntryOnStack(saxAttributes);
            } else if ("xml-policies".equals(localName)) {
                pushXMLPoliciesEntryOnStack(saxAttributes);
            } else if ("assets".equals(localName)) {
                pushAssetsEntryOnStack(saxAttributes);
            } else if ("portlet-content".equals(localName)) {
                pushPortletContentEntryOnStack(saxAttributes);      
            } else if ("generated-resources".equals(localName)) {
                pushGeneratedResourcesEntryOnStack(saxAttributes);
            } else {
                throw new ExtendedSAXParseException("Unknown MCSIElement: " +
                        localName, marlinContext.getLocator());
            }
            callMCSIElementStart();
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
     * Push a PortletContextElement onto the top of our stack of elements.
     * @param saxAttributes The SAX Attributes with the infomation required
     * to create our MCSIElement     
     */ 
    protected void pushPortletContextEntryOnStack(Attributes saxAttributes) {
        
        PortletContextElement element = new PortletContextElement();
        
        elementStackEntry = new MCSIElementStackEntry(element, null);
        elementStack.push(elementStackEntry);
    }
    
    /**
     * Push an ArgumentsElement onto the top of our stack of elements.
     * @param saxAttributes The SAX Attributes with the infomation required
     * to create our MCSIElement    
     */
    protected void pushJDBCPoliciesEntryOnStack(Attributes saxAttributes) {
        JdbcPoliciesElement element = new JdbcPoliciesElement();
        
        JdbcPoliciesAttributes attrs = new JdbcPoliciesAttributes();
        attrs.setName(saxAttributes.getValue("name"));        

        elementStackEntry = new MCSIElementStackEntry(element, attrs);
        elementStack.push(elementStackEntry);
    }
    
    /**
     * Push an ArgumentsElement onto the top of our stack of elements.
     * @param saxAttributes The SAX Attributes with the infomation required
     * to create our MCSIElement    
     */
    protected void pushXMLPoliciesEntryOnStack(Attributes saxAttributes) {
        XmlPoliciesElement element = new XmlPoliciesElement();

        XmlPoliciesAttributes attrs = new XmlPoliciesAttributes();
        attrs.setDirectory(saxAttributes.getValue("directory"));        

        elementStackEntry = new MCSIElementStackEntry(element, attrs);
        elementStack.push(elementStackEntry);
    }

    /**
     * Push an ArgumentsElement onto the top of our stack of elements.
     * @param saxAttributes The SAX Attributes with the infomation required
     * to create our MCSIElement    
     */
    protected void pushAssetsEntryOnStack(Attributes saxAttributes) {
        AssetsElement element = new AssetsElement();

        AssetsAttributes attrs = new AssetsAttributes();
        attrs.setBaseUrl(saxAttributes.getValue("base-url"));

        elementStackEntry = new MCSIElementStackEntry(element, attrs);
        elementStack.push(elementStackEntry);
    }

    /**
     * Push a GeneratedResourceElement onto the top of our stack of elements.
     *
     * @param saxAttributes The SAX Attributes with the infomation required
     *      to create our MCSIElement
     */
    protected void pushGeneratedResourcesEntryOnStack(Attributes saxAttributes) {
        GeneratedResourcesElement element = new GeneratedResourcesElement();

        GeneratedResourcesAttributes attrs = new GeneratedResourcesAttributes();
        attrs.setBaseDir(saxAttributes.getValue("base"));

        elementStackEntry = new MCSIElementStackEntry(element, attrs);
        elementStack.push(elementStackEntry);
    }

    /**
     * Push an ArgumentsElement onto the top of our stack of elements.
     * @param saxAttributes The SAX Attributes with the infomation required
     * to create our MCSIElement    
     */
    protected void pushPortletContentEntryOnStack(Attributes saxAttributes) {
        PortletContentElement element = new PortletContentElement();

        elementStackEntry = new MCSIElementStackEntry(element, null);
        elementStack.push(elementStackEntry);
    }


    /**
     * Calls the elementStart() method on the appropriate MCSIElement.
     * @throws SAXParseException
     */
    protected void callMCSIElementStart() throws SAXParseException {
        MarinerRequestContext requestContext = 
            marlinContext.getInitialRequestContext();

        MCSIElement element = elementStackEntry.getElement();
        MCSIAttributes attributes = elementStackEntry.getAttributes();

        try {
            // Invoke the MCSIElement's elementStart method.
            int result = element.elementStart(requestContext, attributes);

            switch (result) {
                case MCSIConstants.PROCESS_ELEMENT_BODY:
                    // Do nothing.
                    break;

                case MCSIConstants.SKIP_ELEMENT_BODY:               
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

    // Javadoc inherited from ContentHandler interface
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException {
        if (processedElement()) {
            
            // end FlowControl mode
            endFlowControl();
            
            // Pop the information about the current element.
            elementStackEntry = (MCSIElementStackEntry) elementStack.pop();

            callMCSIElementEnd();
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
     * Calls the elementEnd() method on the appropriate MCSIElement.          
     * @throws SAXException
     */
    protected void callMCSIElementEnd() throws SAXException {

        // Get the current request context
        MarinerRequestContext requestContext =
            marlinContext.getInitialRequestContext();

        MCSIElement element = elementStackEntry.element;
        MCSIAttributes attributes = elementStackEntry.attributes;

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
                case MCSIConstants.CONTINUE_PROCESSING:
                    // Do nothing.
                    break;

                case MCSIConstants.ABORT_PROCESSING:
                    // Set the abort flag.
                    marlinContext.setAbort(true);
                    if (logger.isDebugEnabled()) {
                        logger.debug(this + ": endElemement: abort");
                    }
                    return;

                default :
                    // An unknown return code was received from the MCSIElement.
                    throw new ExtendedSAXParseException("Unknown return code of "
                            + result + " from " + element + " endElement",
                            marlinContext.getLocator());
            }
        } catch (PAPIException e) {
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

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/3	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
