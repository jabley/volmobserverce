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
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection 
 *                              WBSAX serialiser.
 * 30-May-03    Mat             VBM:2003042906 - Implemented Geoffs hack
 *                              in ShardLink
 * 31-May-03    Geoff           VBM:2003042906 - Fix typo in the fix above.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection.io;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.dissection.dom.DissectedContentHandler;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.dissection.string.StringSegment;
import com.volantis.mcs.wbdom.WBDOMDocument;
import com.volantis.mcs.wbdom.WBDOMElement;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.VisitorChildrenIterator;
import com.volantis.mcs.wbdom.dissection.WBDOMDissectableDocument;
import com.volantis.mcs.wbdom.dissection.DissectableSpecialElement;
import com.volantis.mcs.wbdom.dissection.DissectableWBDOMText;
import com.volantis.mcs.dom.output.SerialisationURLListener;
import com.volantis.mcs.wbdom.io.WBSAXAttributeNameSerialiser;
import com.volantis.mcs.wbdom.io.WBSAXAttributeValueSerialiser;
import com.volantis.mcs.wbdom.io.WBSAXAttributesSerialiser;
import com.volantis.mcs.wbdom.io.WBSAXElementNameSerialiser;
import com.volantis.mcs.wbdom.io.WBSAXElementValueSerialiser;
import com.volantis.mcs.wbdom.io.WBSAXSerialisationVisitor;
import com.volantis.mcs.wbdom.io.SerialisationConfiguration;
import com.volantis.mcs.wbsax.CopyReferenceResolver;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;

/**
 * An implementation of {@link DissectedContentHandler} for WBDOM.
 * <p>
 * This allows the dissector to serialise a {@link WBDOMDissectableDocument}
 * out via WBSAX using it's "dissectable" interfaces.
 * <p>
 * This performs much of the same stuff that 
 * {@link DissectionWBSAXSerialisationVisitor} performs apart from the fact 
 * that in this case the dissector does the top level visiting. So, much
 * of the code is similar. This is poor but the current design of the dissector
 * requires this.
 */ 
public class WBDOMDissectedContentHandler implements DissectedContentHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The WBSAX content handler that the WBSAX events will be sent to.
     */ 
    private WBSAXContentHandler handler;
    
    /**
     * The serialisation configuration which controls details of the 
     * serialisation process.
     */ 
    private SerialisationConfiguration configuration;
    
    /**
     * The listener which is informed if an attribute which contains a URL is 
     * found in the output, as defined by the configuration.
     * <p>
     * May be null if not required.     
     */ 
    private SerialisationURLListener urlListener; //optional

    //
    // Child objects which we delegate to in order to serialise the various 
    // different components of a WBDOM.
    //

    /**
     * Iterating serialiser for WBSAX attributes.
     */ 
    private WBSAXAttributesSerialiser attributesSerialiser;

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
    private WBSAXElementValueSerialiser elementValueSerialiser;

    /**
     * Visiting serialiser for attribute values.
     */ 
    private WBSAXAttributeValueSerialiser attributeValueSerialiser;
    
    /**
     * The factory for creating WBSAX strings.
     */ 
    private StringFactory strings;

    /**
     * The factory for creating WBSAX string references (in the output string 
     * table).
     */ 
    private StringReferenceFactory references;
    
    /**
     * The string table of the output document.
     */ 
    private StringTable stringTable;
    
    /**
     * Reference resolver which copies string references from the input string
     * table to the output string table.
     */ 
    private CopyReferenceResolver resolver;
    
    public WBDOMDissectedContentHandler(WBSAXContentHandler handler, 
            SerialisationConfiguration configuration, 
            SerialisationURLListener urlListener) {
        this.handler = handler;
        this.configuration = configuration;
        this.urlListener = urlListener;
    }

    // Inherit Javadoc.
    public void startDocument(DissectableDocument document,
            SharedContentUsages usages) throws DissectionException {
        WBDOMDissectableDocument ddocument = (WBDOMDissectableDocument) document;
        WBDOMDocument wbdomDocument = ddocument.getDocument();
        
        strings = wbdomDocument.getStringFactory();
        stringTable = new StringTable();
        references = new StringReferenceFactory(
                stringTable, strings); 
        resolver = new CopyReferenceResolver(references);
        elementNameSerialiser = new WBSAXElementNameSerialiser(handler, 
                resolver);
        attributeNameSerialiser = new WBSAXAttributeNameSerialiser(handler,
                resolver); 
        attributeValueSerialiser = new WBSAXAttributeValueSerialiser(handler, 
                resolver);
        attributesSerialiser = new WBSAXAttributesSerialiser(
                handler, attributeNameSerialiser, attributeValueSerialiser,
                configuration, urlListener);
        elementValueSerialiser = new WBSAXElementValueSerialiser(handler, 
                resolver);
        try {
            handler.startDocument(wbdomDocument.getVersion(), 
                    wbdomDocument.getPublicId(), 
                    wbdomDocument.getCodec(), stringTable, strings);
        } catch (WBSAXException e) {
            throw new DissectionException(e);
        }
    }

    // Inherit Javadoc.
    public void endDocument()
            throws DissectionException {
        try {
            // Mark the string table complete.
            // todo: we should be able to use the shared usages passed into
            // start document to create a string table up front. When we do,
            // then we should mark the string table complete at that point, 
            // so the WBXML producers do not need to buffer the header. 
            if (stringTable != null) {
                stringTable.markComplete();
            }
            handler.endDocument();
        } catch (WBSAXException e) {
            throw new DissectionException(e);
        }
    }

    
    // Inherit Javadoc.
    public void emptyElement(DissectableElement element)
            throws DissectionException {
        WBDOMElement delement = (WBDOMElement) element;
        boolean hasAttributes = delement.hasAttributes();
        // can't do this check because sometimes we force stuff to be 
        // empty that isn't really.
//        if (delement.hasChildren()) {
//            throw new IllegalStateException(
//                    "Attempt to write an invalid (non-empty) element");
//        }
        try {
            // Serialise the start of the element.
            elementNameSerialiser.use(hasAttributes, false);
            delement.accept(elementNameSerialiser);
            // Serialise the attributes.
            if (hasAttributes) {
                attributesSerialiser.use(delement);
                delement.forEachAttribute(attributesSerialiser);
                handler.endElement();
            }
        } catch (WBDOMException e) {
            throw new DissectionException(e);
        } catch (WBSAXException e) {
            throw new DissectionException(e);
        }
    }

    // Inherit Javadoc.
    public void startElement(DissectableElement element)
            throws DissectionException {
        final WBDOMElement delement = (WBDOMElement) element;
        boolean hasAttributes = delement.hasAttributes();
        // can't do this check as sometimes we force stuff to be non-empty
        // which is empty!
//        if (!delement.hasChildren()) {
//            throw new IllegalStateException(
//                    "Attempt to write start of an invalid (empty) element " + 
//                    delement);
//        }
        try {
            // Serialise the start of the element.
            elementNameSerialiser.use(hasAttributes, true);
            delement.accept(elementNameSerialiser);
            // Serialise the attributes.
            if (hasAttributes) {
                attributesSerialiser.use(delement);
                delement.forEachAttribute(attributesSerialiser);
            }
            handler.startContent();
        } catch (WBDOMException e) {
            throw new DissectionException(e);
        } catch (WBSAXException e) {
            throw new DissectionException(e);
        }
    }

    // Inherit Javadoc.
    public void endElement(DissectableElement element)
            throws DissectionException {
        WBDOMElement delement = (WBDOMElement) element;
        // can't do this check as sometimes we force stuff to be non-empty
        // which is empty!
//        if (!delement.hasChildren()) {
//            throw new IllegalStateException(
//                    "Attempt to write end of an invalid (empty) element " + 
//                    delement);
//        }
        try {
            // Serialise the end of the element.
            handler.endContent();
            handler.endElement();
        } catch (WBSAXException e) {
            throw new DissectionException(e);
        }
    }

    // Inherit Javadoc.
    public void shardLink(DissectableElement element,
            ShardLinkDetails details) throws DissectionException {
        DissectableSpecialElement delement = (DissectableSpecialElement) element;
        WBSAXSerialisationVisitor visitor = 
                new DissectionWBSAXSerialisationVisitor(handler, configuration,
                        details, urlListener, resolver);
        // Iterate over the children, ignoring their parent.
        VisitorChildrenIterator childrenSerialiser = 
            new VisitorChildrenIterator(visitor);
        try {
            if (delement.hasChildren()) {
                delement.forEachChild(childrenSerialiser);
            }
        } catch (WBDOMException e) {
            throw new DissectionException(e);
        }
    }

    // Inherit Javadoc.
    public void text(DissectableText text)
            throws DissectionException {
        DissectableWBDOMText dtext = (DissectableWBDOMText) text;
        try {
            // Serialise the value of the text.
            dtext.getBuffer().accept(elementValueSerialiser);
        } catch (WBSAXException e) {
            throw new DissectionException(e);
        }
    }

    // Inherit Javadoc.
    public void text(DissectableText text, StringSegment segment)
            throws DissectionException {
        DissectableWBDOMText dtext = (DissectableWBDOMText) text;
        try {
            String prefix = segment.getPrefix();
            if (prefix != null) {
                handler.addContentValue(strings.create(prefix));
            }
            SegmentElementValueSerialiser serialiser = 
                    new SegmentElementValueSerialiser(strings, 
                            elementValueSerialiser, segment);
            dtext.getBuffer().accept(serialiser);
            String suffix = segment.getSuffix();
            if (suffix != null) {
                handler.addContentValue(strings.create(suffix));
            }
        } catch (WBSAXException e) {
            throw new DissectionException(e);
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

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/3	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	781/3	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	751/2	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 27-Jun-03	559/3	geoff	VBM:2003060607 make WML use protocol configuration again

 24-Jun-03	365/3	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/3	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
