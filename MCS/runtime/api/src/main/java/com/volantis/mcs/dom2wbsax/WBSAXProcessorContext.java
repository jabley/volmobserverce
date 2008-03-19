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

import com.volantis.charset.Encoding;
import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.dom2wbsax.WBSAXElementProcessor;
import com.volantis.mcs.dom2wbsax.WBSAXProcessorConfiguration;
import com.volantis.mcs.dom.output.SerialisationURLListener;

import java.util.HashMap;
import java.util.Stack;

/**
 * This context class stores the shared context required by all the objects
 * participating in serialising the MCSDOM into WBSAX events. Currently we
 * have the WBSAX Outputter, the various element processors and the various 
 * attribute and a value processors all participating and they all share this
 * object.
 *
 * @todo move runtime specific code in here out into a proper runtime package.
 * See VBM :2003092902 (old build). The dom (and wbsax/wbdom) package should
 * not be in runtime subsystem - they should be in cornerstone or their own
 * subsystems.
 */
public class WBSAXProcessorContext {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The element name factory
     */
    private ElementNameFactory elementNames;

    /**
     * The attributeStartFactory
     */
    private AttributeStartFactory attributeStarts;

    /**
     * The string factory.
     */
    private StringFactory strings;
    
    /**
     * String reference factory, used for literal element names only at
     * the moment.
     */ 
    private StringReferenceFactory references;

    /**
     * The encoding of the document we are serialising
     */ 
    private Encoding encoding;

    /**
     * The WBSAX string table of the document we are serialising.
     */ 
    private StringTable stringTable;

    /**
     * The processor configuration
     */
    private WBSAXProcessorConfiguration configuration;

    /**
     * The WBSAX content handler for our generated WBSAX events.
     */
    private WBSAXContentHandler contentHandler;

    /** 
     * true if we are generating the final output; i.e. there will be
     * no dissection and no WBDOM and no WBDOM serialisation. This is 
     * kinda hacky but is only required because we haven't implemented 
     * accurate dissection for MCS DOM yet - in that case there is only
     * one output phase rather than two...
     * 
     * @todo this should be defined in protocols package - see to do above. 
     */ 
    private boolean finalOutput;

    /** 
     * Optional listener for urls in the output; implemented for the packager. 
     * 
     * @todo this should be defined in protocols package - see to do above. 
     */
    private SerialisationURLListener urlListener;

    /**
     * The stack of element processors. 
     */ 
    private Stack elementProcessorStack = new Stack();

    /**
     * Trivial storage for attribute value processors; this maps an attribute 
     * name to a attribute value processor and thus stores a single "global" 
     * attribute value processor for each attribute name. 
     * <p>
     * Attribute value processors should logically be stored per element 
     * processor and be retrieved using a fallback up the element processor 
     * stack to find all the attribute value processors for an attribute. 
     * <p>
     * However, we know that there are currently only a few uses of attribute 
     * value processor which do not conflict on attribute name. Thus we can 
     * get away with a simplified structure which stores just those 
     * efficiently.
     */ 
    private HashMap attributeValueProcessors = new HashMap();

    /**
     * Trivial storage for content value processors; this stores a single 
     * "global" content value processor for all element processors. 
     * <p>
     * See {@link #attributeValueProcessors} for an explanation of how 
     * this may need to change if we have more complex use of special elements. 
     */ 
    private WBSAXValueProcessor contentValueProcessor;


    /**
     * Construct an instance of this class.
     *
     * @param elementNames
     *                   the WBSAX element name factory to use.
     * @param attributeStarts
     *                   the WBSAX attribute start factory to use.
     * @param strings    the WBSAX string factory to use.
     * @param references the WBSAX string reference factory to use.
     * @param encoding   the encoding of the document we are serialising.
     * @param stringTable
     *                   the WBSAX string table of the document we are
     *                   serialising.
     * @param configuration
     *                   the processor configuration we are using to serialise
     *                   this document.
     */
    public WBSAXProcessorContext(ElementNameFactory elementNames,
                                 AttributeStartFactory attributeStarts,
                                 StringFactory strings,
                                 StringReferenceFactory references,
                                 Encoding encoding,
                                 StringTable stringTable,
                                 WBSAXProcessorConfiguration configuration) {
        this.elementNames = elementNames;
        this.attributeStarts = attributeStarts;
        this.strings = strings;
        this.references = references;
        this.encoding = encoding;
        this.stringTable = stringTable;
        this.configuration = configuration;
    }

    //
    // Manage the stack of element processors.
    //

    /**
     * Push the element processor provided onto the element processor stack.
     * <p>
     * The element processor stack contains an element processor for each 
     * nesting level of elements found in the DOM.
     * <p>
     * This must be called just before each element is found in 
     * the DOM, by the {@link com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter}.
     *  
     * @param processor the element processor to push onto the stack.
     * @see #getElementProcessor
     */ 
    public void pushElementProcessor(WBSAXElementProcessor processor) {
        
        elementProcessorStack.push(processor);
        
    }
    
    /**
     * Pop the last element processor from the element processor stack.
     * <p>
     * This must be called just after each element is found in the DOM, by the 
     * {@link com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter}.
     *  
     * @return the last element processor to pushed onto the stack.
     * @see #getElementProcessor
     */ 
    public WBSAXElementProcessor popElementProcessor() {
        
        return (WBSAXElementProcessor) elementProcessorStack.pop();
        
    }
    
    /**
     * Return the last element processor from the element processor stack.
     * <p>
     * This may be called at any time to retrieve the element processor being
     * used to process the current element in the DOM, by the 
     * {@link com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter}.
     *  
     * @return the last element processor to pushed onto the stack.
     */ 
    public WBSAXElementProcessor getElementProcessor() {
        
        return (WBSAXElementProcessor) elementProcessorStack.peek();
        
    }
    
    // 
    // Manage the emulated stack of attribute value processors.
    //

    /**
     * Push the attribute value processor provided onto the stack of attribute 
     * value processors. 
     * <p>
     * The attribute value processor stack is "sparse". If an element does not 
     * push an attribute value processor onto a stack for each of it's 
     * attributes, then those that do not have a processor registered will use 
     * the ones registered by their parent element processor, if any, or else 
     * they will use a default attribute value processor. 
     * <p>
     * This may be called for any attribute found when processing an element
     * in the DOM, by the {@link com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter}.
     * 
     * @param attrName the name of the attribute.
     * @param processor the processor to use for this attribute.
     * @see #getAttributeValueProcessor
     */ 
    public void pushAttributeValueProcessor(String attrName,
            WBSAXValueProcessor processor) {
        
        // Currently we only store one value processor per attribute name, so
        // we need to reject attempts to register the same attr twice.
        Object oldProcessor = attributeValueProcessors.get(attrName);
        if (oldProcessor != null)
            throw new IllegalStateException(
                    "Can't register attribute value processor (" + 
                    processor + ") for " + attrName + " as (" + oldProcessor + 
                    ") already registered");
        attributeValueProcessors.put(attrName, processor);
        
    }
    
    /**
     * Pop the last attribute value processor from the stack of attribute 
     * value processors. 
     * <p>
     * This must be called for each attribute pushed when processing an element
     * in the DOM, by the {@link com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter}.
     * 
     * @param attrName the name of the attribute.
     * @return the last attribute value processor pushed onto the stack.
     * @see #pushAttributeValueProcessor
     * @see #getAttributeValueProcessor
     */ 
    public WBSAXValueProcessor popAttributeValueProcessor(String attrName) {
        
        return (WBSAXValueProcessor) attributeValueProcessors.remove(attrName);
        
    }
    
    /**
     * Return the last attribute value processor from the stack of attribute 
     * value processors. 
     * <p>
     * This may be called at any time to retrieve the attribute value 
     * processor being used to process attributes within the current element 
     * in the DOM, by the {@link com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter}.
     * <p>
     * NOTE: attribute value processors are used to process both the attributes
     * of the element where they were pushed, and any below that until they
     * are overridden by a child element processor.
     * <p>
     * NOTE: the current implementation does not fully implement the contract
     * stated above. Currently we only store one attribute per name as we know
     * that there are no valid duplicate uses of an attribute. In future this
     * will probably change and at that point we will need to implement a 
     * proper stack of attribute value processors. 
     * 
     * @param attrName the name of the attribute.
     * @return the last attribute value processor pushed onto the stack.
     * @see #getAttributeValueProcessor
     */ 
    public WBSAXValueProcessor getAttributeValueProcessor(String attrName) {
        
        return (WBSAXValueProcessor) attributeValueProcessors.get(attrName);
        
    }

    // 
    // Manage the emulated stack of content value processors.
    //

    /**
     * Push the content value processor provided onto the stack of content 
     * value processors. 
     * <p>
     * The content value processor stack is "sparse". If an element does not 
     * push a content value processor onto a stack, then those that do not 
     * have a processor registered will use the one registered by their parent 
     * element processor, if any, or else they will use a default content 
     * value processor. 
     * <p>
     * This may be called when processing an element in the DOM, by the 
     * {@link com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter}.
     * 
     * @param processor the processor to use.
     * @see #getContentValueProcessor
     */ 
    public void pushContentValueProcessor(WBSAXValueProcessor processor) {
        
        // Currently we only store one value processor for content, so we need
        // to reject attempts to register more than one at a time.
        if (contentValueProcessor != null)
            throw new IllegalStateException(
                    "Can't register content value processor (" + 
                    processor + ") as (" + contentValueProcessor + 
                    ") already registered");
        contentValueProcessor = processor;
        
    }
    
    /**
     * Pop the last content value processor from the stack of content value 
     * processors. 
     * <p>
     * This must be called if a content value processor was pushed when 
     * processing an element in the DOM, by the {@link com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter}.
     * 
     * @return the last content value processor pushed onto the stack.
     * @see #pushContentValueProcessor
     * @see #getContentValueProcessor
     */ 
    public WBSAXValueProcessor popContentValueProcessor() {
        
        WBSAXValueProcessor lastProcessor = contentValueProcessor;
        contentValueProcessor = null;
        return lastProcessor;
        
    }

    /**
     * Return the last content value processor from the stack of content
     * value processors. 
     * <p>
     * This may be called at any time to retrieve the content value 
     * processor being used to process text within the current element 
     * in the DOM, by the {@link com.volantis.mcs.dom2wbsax.WBSAXDocumentOutputter}.
     * <p>
     * NOTE: content value processors are used to process both the text
     * of the element where they were pushed, and any below that until they
     * are overridden by a child element processor.
     * <p>
     * NOTE: the current implementation does not fully implement the contract
     * stated above. Currently we only store a single content value processor 
     * as we know that there are no valid duplicate uses of it. In future this
     * will probably change and at that point we will need to implement a 
     * proper stack of content value processors. 
     * 
     * @return the last content value processor pushed onto the stack.
     * @see #getContentValueProcessor
     */ 
    public WBSAXValueProcessor getContentValueProcessor() {
        
        return contentValueProcessor;
        
    }

    //
    // Simple getters and setters for shared context objects.
    // Most of these are WBSAX factories for creating new WBSAX data objects.
    //
    
    // Javadoc unnecessary.
    public ElementNameFactory getElementNames() {
        return elementNames;
    }

    // Javadoc unnecessary.
    public AttributeStartFactory getAttributeStarts() {
        return attributeStarts;
    }

    // Javadoc unnecessary.
    public StringFactory getStrings() {
        return strings;
    }

    // Javadoc unnecessary.
    public StringReferenceFactory getReferences() {
        return references;
    }

    // Javadoc unnecessary.
    public Encoding getEncoding() {
        return encoding;
    }

    // Javadoc unnecessary.
    public StringTable getStringTable() {
        return stringTable;
    }
    
    // Javadoc unnecessary.
    public WBSAXProcessorConfiguration getConfiguration() {
        return configuration;
    }

    // Javadoc unnecessary.
    public WBSAXContentHandler getContentHandler() {
        return contentHandler;
    }

    // Javadoc unnecessary.
    public boolean isFinalOutput() {
        return finalOutput;
    }

    // Javadoc unnecessary.
    public SerialisationURLListener getUrlListener() {
        return urlListener;
    }
    
    // Javadoc unnecessary.
    public void setContentHandler(WBSAXContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    // Javadoc unnecessary.
    public void setFinalOutput(boolean finalOutput) {
        this.finalOutput = finalOutput;
    }

    // Javadoc unnecessary.
    public void setUrlListener(SerialisationURLListener urlListener) {
        this.urlListener = urlListener;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 02-Mar-05	7243/9	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/3	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 06-Oct-03	1469/9	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix rework stuff from phil)

 02-Oct-03	1469/7	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
