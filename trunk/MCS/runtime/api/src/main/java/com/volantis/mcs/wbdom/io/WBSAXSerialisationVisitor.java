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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection 
 *                              WBSAX serialiser.
 * 01-Jun-03    Geoff           VBM:2003042906 - Add to do.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.io;

import com.volantis.mcs.wbdom.WBDOMElement;
import com.volantis.mcs.wbdom.WBDOMText;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.WBDOMVisitor;
import com.volantis.mcs.wbdom.EmptyElementType;
import com.volantis.mcs.wbsax.ReferenceResolver;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.dom.output.SerialisationURLListener;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * An abstract implementation of {@link WBDOMVisitor} which provides most
 * of the infrastructure necessary to serialise a WBDOM out to WBSAX events.
 */ 
public abstract class WBSAXSerialisationVisitor implements WBDOMVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            WBSAXSerialisationVisitor.class);

    //
    // Objects which are used as input to or output from the process.
    //
    
    /**
     * The WBSAX content handler that the WBSAX events will be sent to.
     */ 
    protected WBSAXContentHandler handler;
    
    /**
     * The serialisation configuration which controls details of the 
     * serialisation process.
     */ 
    protected SerialisationConfiguration configuration;
    
    /**
     * The listener which is informed if an attribute which contains a URL is 
     * found in the output, as defined by the configuration.
     * <p>
     * May be null if not required.     
     */ 
    private SerialisationURLListener urlListener; 
    
    //
    // Child objects which we delegate to in order to serialise the various 
    // different components of a WBDOM. These are often visitors themselves.
    //

    /**
     * Iterating serialiser for attributes.
     */ 
    private WBSAXAttributesSerialiser attributesSerialiser;
    
    /**
     * Iterating serialiser for element children.
     */ 
    private WBSAXChildrenSerialiser childrenSerialiser;
    
    /**
     * Visiting serialiser for element names.
     */ 
    private WBSAXElementNameSerialiser elementNameSerialiser;
    
    /**
     * Visiting serialiser for attribute names.
     */ 
    private WBSAXAttributeNameSerialiser attributeNameSerialiser;
    
    /**
     * Visiting serialiser for element values.
     */ 
    protected WBSAXElementValueSerialiser elementValueSerialiser;
    
    /**
     * Visiting serialiser for attribute values.
     */ 
    protected WBSAXAttributeValueSerialiser attributeValueSerialiser;

    /**
     * Construct an instance of this class.
     * <p>
     * Note that {@link #initialiseSerialisers} must be called to complete the
     * construction process.
     * 
     * @param handler the WBSAX content handler we output WBSAX events to.
     * @param configuration the configuration to use.
     * @param urlListener destination for url attribute events, if provided.
     */ 
    protected WBSAXSerialisationVisitor(WBSAXContentHandler handler, 
            SerialisationConfiguration configuration, 
            SerialisationURLListener urlListener) {
        this.handler = handler;
        this.configuration = configuration;
        this.urlListener = urlListener;
        childrenSerialiser = new WBSAXChildrenSerialiser(handler, this);
    }

    /**
     * Initialise the element and attribute serialisers.
     * <p>
     * This should be called before using the class to complete the 
     * construction process.
     * 
     * @param resolver the reference resolver to use during serialisation.
     *      This may be used to translate string references from the input 
     *      string table to the output string table, if an output string table
     *      is required.
     */ 
    protected void initialiseSerialisers(ReferenceResolver resolver) {
        elementNameSerialiser = new WBSAXElementNameSerialiser(handler, 
                resolver);
        attributeNameSerialiser = new WBSAXAttributeNameSerialiser(handler, 
                resolver); 
        createValueSerialisers(resolver);
        attributesSerialiser = new WBSAXAttributesSerialiser(handler, 
                attributeNameSerialiser, attributeValueSerialiser, 
                configuration, urlListener);
    }

    /**
     * Create the element and attribute value serialisers. 
     * <p>
     * This is a Template Method for subclasses to override to allow them
     * to provide their own serialisers. It is normally called from 
     * {@link #initialiseSerialisers}.
     * 
     * @param resolver the reference resolver to use during serialisation.
     */ 
    protected void createValueSerialisers(ReferenceResolver resolver) {
        attributeValueSerialiser = new WBSAXAttributeValueSerialiser(handler, 
                resolver);
        elementValueSerialiser = new WBSAXElementValueSerialiser(handler, 
                resolver);
    }

    // Inherit Javadoc.
    public void visitElement(WBDOMElement element) throws WBDOMException {
        boolean hasAttributes = element.hasAttributes();
        boolean hasContent = element.hasChildren() || 
                element.getEmptyType() == EmptyElementType.StartAndEndTag;
        try {
            // Serialise the start of the element.
            elementNameSerialiser.use(hasAttributes, hasContent);
            element.accept(elementNameSerialiser);
            // Serialise the attributes.
            if (hasAttributes) {
                attributesSerialiser.use(element);
                element.forEachAttribute(attributesSerialiser);
            }
            // Serialise the child content.
            if (hasContent) {
                // Note: it is legal to have 0 content elements, so we still 
                // call start content and end content.
                element.forEachChild(childrenSerialiser);
            }
            // Serialise the end of the element.
            if (hasAttributes || hasContent) {
                handler.endElement();
            }
        } catch (WBSAXException e) {
            throw new WBDOMException(
                        exceptionLocalizer.format(
                                    "wbdom-element-end-write-error"),
                        e);
        }
    }

    // Inherit Javadoc.
    public void visitText(WBDOMText text) throws WBDOMException {
        try {
            // Size the values inside the text.
            text.getBuffer().accept(elementValueSerialiser);
        } catch (WBSAXException e) {            
            throw new WBDOMException(
                        exceptionLocalizer.format(
                                    "wbdom-element-value-write-error"),
                        e);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	781/2	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	751/2	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 27-Jun-03	559/4	geoff	VBM:2003060607 make WML use protocol configuration again

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/2	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
