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
package com.volantis.mcs.dom2wbsax;

import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.dom2wbsax.WBSAXAttributeValueSerialiser;
import com.volantis.mcs.dom2wbsax.WBSAXContentValueSerialiser;
import com.volantis.mcs.dom2wbsax.WBSAXDefaultValueProcessor;

/**
 * WBSAX Element Processors are used by the WBSAXDocumentOutputter to control and 
 * allow customisation of the serialisation of elements.
 * <p>
 * Element Processors in turn use Value Processors which control the way that
 * both attribute and content values are serialised.
 * <p>
 * These abstractions allows the client of WBSAXDocumentOutputter to "plug in" 
 * special case processing for the serialisation of "special" elements such as
 * the dissection and accesskey annotation elements.
 */ 
public class WBSAXElementProcessor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(WBSAXElementProcessor.class);

    /**
     * The default attribute value processor to use if no special content
     * processor could be found for this attribute. 
     */
    protected WBSAXValueProcessor defaultAttributeValueProcessor;

    /**
     * The default content value processor to use if no special content
     * processor could be found for this attribute. 
     */ 
    private WBSAXValueProcessor defaultContentValueProcessor;
    
    /**
     * The context for this element processor.
     */ 
    protected WBSAXProcessorContext context;

    /**
     * Construct an instance of this class.
     * 
     * @param context the shared context for all element processors.
     */ 
    public WBSAXElementProcessor(WBSAXProcessorContext context) {

        // Save the context
        this.context = context;
        
        // Set up the default element and value processors.
        WBSAXValueSerialiser attributeValueSerialiser =
                new WBSAXAttributeValueSerialiser(context);
        defaultAttributeValueProcessor = new WBSAXDefaultValueProcessor(
                new WBSAXStringSerialiser(context.getEncoding(),
                        attributeValueSerialiser));
        WBSAXValueSerialiser contentValueSerialiser =
                new WBSAXContentValueSerialiser(context);
        defaultContentValueProcessor = new WBSAXDefaultValueProcessor(
                new WBSAXStringSerialiser(context.getEncoding(),
                        contentValueSerialiser));
        
    }

    /**
     * Process an element start event.
     * <p>
     * This will be called to serialise the start of an element in the DOM 
     * (including it's attributes).
     * 
     * @param element the element being processed
     * @param content a flag to indicate if this element has content.
     * @throws com.volantis.mcs.wbsax.WBSAXException
     */ 
    public void elementStart(Element element,
            boolean content) throws WBSAXException {
        
        String name = element.getName();
        boolean attributes = element.getAttributes() != null;
        // If we have a registered code for this element
        ElementNameCode nameCode = context.getElementNames().create(name);
        WBSAXContentHandler contentHandler = context.getContentHandler();
        if (nameCode != null) {
            // Start the element with the name code
            contentHandler.startElement(nameCode, attributes, content);
        } else {
            // There was no code for the element in the token table.
            // So, we fall back to starting the element with a literal 
            // name, and hope the device understands it.
            StringReference nameRef =
                    context.getReferences().createReference(name);
            contentHandler.startElement(nameRef, attributes, content);
        }
        if (attributes) {
            contentHandler.startAttributes();
    
            for (Attribute attribute = element.getAttributes();
                 attribute != null;
                 attribute = attribute.getNext()) {

                String attrName = attribute.getName();
                String attrValue = attribute.getValue();

                // NOTE: code below is a kind of generic attribute value
                // processor - it searches all attribute names rather than
                // the more specific kind we have already implemented. Then 
                // we could register it externally if finalOutput was true.
                    
                // If we are generating the final output (no WBDOM) and
                //   the attribute had a value and 
                //   we are listening for urls and
                //   this attribute is registered as containing a URL... 
                if (context.isFinalOutput() && attrValue != null && 
                        context.getUrlListener() != null &&
                        context.getConfiguration().isURLAttribute(name, 
                                attrName)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Generating URL Attribute Event " + 
                                "for: " + name + " " + attrName + "='" + 
                                attrValue + "'");
                    }
                    // Then generate the URL event for the listener.
                    context.getUrlListener().foundURL(attrValue);
                }
                    
                // Add the attribute start code
                AttributeStartCode attrStart =
                        context.getAttributeStarts().create(attrName, attrValue);
                contentHandler.addAttribute(attrStart);
                    
                // And figure out how to add the attribute value...
                // If the start code included a prefix 
                String prefix = attrStart.getValuePrefix();
                if (prefix != null) {
                    // Remove the prefix from the value we are about to add
                    // as a string.
                    attrValue = attrValue.substring(prefix.length());
                }
                    
                // Process the attribute value.
                // Here we attempt to look up a registered attribute
                // processor and if that fails we fall back to the default.
                WBSAXValueProcessor attrValueProcessor =
                        context.getAttributeValueProcessor(attrName); 
                if (attrValueProcessor == null) {
                    attrValueProcessor = defaultAttributeValueProcessor;
                }
                attrValueProcessor.value(attrValue.toCharArray(),
                        attrValue.length());
            }
            contentHandler.endAttributes();
        }
    }
        
    /**
     * Process an element end event.
     * <p>
     * This will be called to serialise the end of an element in the DOM 
     * (after it's content has been written, if any).
     * 
     * @param element the element we are finishing with.
     * @param content a flag to indicate if this element had content.
     * @throws com.volantis.mcs.wbsax.WBSAXException
     */ 
    public void elementEnd(Element element, boolean content)
            throws WBSAXException {
        if (element.getAttributes() != null || content) {
            context.getContentHandler().endElement();
        }
        
    }

    /**
     * Process a content start event.
     * <p>
     * This will be called before we serialise the content of an element.
     * 
     * @throws com.volantis.mcs.wbsax.WBSAXException
     */ 
    public void contentStart() throws WBSAXException {
        context.getContentHandler().startContent();
    }
    
    /**
     * Process a content end event.
     * <p>
     * This will be called after we serialise the content of an element.
     * 
     * @throws com.volantis.mcs.wbsax.WBSAXException
     */ 
    public void contentEnd() throws WBSAXException {
        context.getContentHandler().endContent();
    }

    /**
     * Process a text node event.
     * <p>
     * This will be called to serialise a text node in the DOM. 
     *  
     * @param text
     * @param length
     * @throws com.volantis.mcs.wbsax.WBSAXException
     * 
     * @todo perhaps this should be in WBSAXDocumentOutputter instead?
     */ 
    public void text(char [] text, int length) throws WBSAXException {
        
        WBSAXValueProcessor contentValueProcessor =
                context.getContentValueProcessor();
        if (contentValueProcessor == null) {
            contentValueProcessor = defaultContentValueProcessor;
        }
        contentValueProcessor.value(text, length);
        
    }
        
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 02-Oct-03	1469/5	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
