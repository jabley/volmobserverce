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
 * 01-Jun-03    Geoff           VBM:2003042906 - Implement shard link costing.
 * 02-Jun-03    Geoff           VBM:2003042906 - Fix size calculation bug.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.Accumulator;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DissectableIterator;
import com.volantis.mcs.dissection.dom.DissectableNode;
import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.dissection.dom.DocumentVisitor;
import com.volantis.mcs.dissection.dom.ElementType;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.wbdom.AttributesInternalIterator;
import com.volantis.mcs.wbdom.WBDOMDocument;
import com.volantis.mcs.wbdom.WBDOMElement;
import com.volantis.mcs.wbdom.WBDOMNode;
import com.volantis.mcs.wbdom.WBDOMText;
import com.volantis.mcs.wbdom.VisitorAttributesIterator;
import com.volantis.mcs.wbdom.VisitorChildrenIterator;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.EmptyElementType;
import com.volantis.mcs.wbdom.dissection.io.WBSAXSizingVisitor;
import com.volantis.mcs.wbdom.dissection.io.WBSAXNameSizer;
import com.volantis.mcs.wbdom.dissection.io.WBSAXValueSizeVisitor;
import com.volantis.mcs.wbdom.dissection.io.WBSAXAttributeValueSizer;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.MultiByteInteger;
import com.volantis.mcs.dom.DocumentAnnotation;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * An implementation of {@link DissectableDocument} for WBDOM.
 * <p>
 * This implements a Wrapper/Facade over a {@link WBDOMDocument} that allows 
 * the dissector to access the WBDOM via it's "dissectable" interfaces. 
 * 
 * @see DissectableDocument
 * @see DissectableElement
 * @see DissectableIterator
 * @see DissectableText
 */ 
public class WBDOMDissectableDocument implements DissectableDocument {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            WBDOMDissectableDocument.class);

    /**
     * The WBDOM document that we are wrapping. 
     */ 
    private WBDOMDocument document;

    /**
     * A string reference factory for the document's input string table.
     * <p>
     * NOTE: This is only useful for resolving existing references rather than 
     * creating new ones since the input string table is already complete.
     */ 
    private StringReferenceFactory inputReferences; 
    
    /**
     * Construct an instance of this class, with the configuration and 
     * document supplied.
     * 
     * @param document the document to wrap.
     */ 
    public WBDOMDissectableDocument(WBDOMDocument document) {
        this.document = document;
        this.inputReferences = new StringReferenceFactory(
                document.getStringTable(), document.getStringFactory());
    }

    /**
     * Get the WBDOM document that this class wraps.
     * 
     * @return the WBDOM document.
     */ 
    public WBDOMDocument getDocument() {
        return document;
    }

    
    // =========================================================================
    //   Document Methods
    // =========================================================================
    
    // Inherit Javadoc.
    public DissectableElement getRootElement() {
        return (DissectableCodeElement) document.getRootElement();
    }

    
    // =========================================================================
    //   Visit Methods
    // =========================================================================

    // Inherit Javadoc.
    public void visitDocument(DocumentVisitor visitor)
        throws DissectionException {
        visitor.visitDocument(this);
    }

    // Inherit Javadoc.
    public void visitNode(DissectableNode node, DocumentVisitor visitor)
            throws DissectionException {
        DissectableWBDOMNode dnode = (DissectableWBDOMNode) node;
        dnode.accept(visitor);
    }

    // Inherit Javadoc.
    public void visitChildren(DissectableElement element, 
            DocumentVisitor visitor) throws DissectionException {
        WBDOMElement delement = (WBDOMElement) element;
        WBDOMNode child = delement.getChildren();
        while (child != null) {
            ((DissectableWBDOMNode)child).accept(visitor);
            child = child.getNext();
        }
    }

    
    // =========================================================================
    //   Annotation Methods
    // =========================================================================

    // Inherit Javadoc.
    public void setAnnotation(DissectableNode node,
            NodeAnnotation annotation) {
        WBDOMNode dnode = (WBDOMNode) node;
        dnode.setAnnotation(annotation);
    }

    // Inherit Javadoc.
    public NodeAnnotation getAnnotation(
            DissectableNode node) {
        WBDOMNode dnode = (WBDOMNode) node;
        return dnode.getAnnotation();
    }

    // Inherit Javadoc.
    public void setAnnotation(DocumentAnnotation annotation) {
        document.setAnnotation(annotation);
    }

    // Inherit Javadoc.
    public DocumentAnnotation getAnnotation() {
        return document.getAnnotation();
    }

    
    // =========================================================================
    //   Element Methods
    // =========================================================================

    // Inherit Javadoc.
    public boolean isElementAtomic(DissectableElement element) {
        DissectableWBDOMElement delement = (DissectableWBDOMElement) element;
        return delement.isAtomic();
    }

    // Inherit Javadoc.
    public boolean isElementEmpty(DissectableElement element) 
            throws DissectionException {
        WBDOMElement delement = (WBDOMElement) element;
        return (!delement.hasChildren() && 
                delement.getEmptyType() == EmptyElementType.EmptyTag);
    }

    // Inherit Javadoc.
    public ElementType getElementType(DissectableElement element)
        throws DissectionException {
        // Note: currently we assume that the dissector will only call this
        // for it's own special elements. If this turns out not to be the case,
        // we need to return the "plain" ElementType here if the class is
        // one of our elements.
        DissectableSpecialElement delement = 
                (DissectableSpecialElement) element;
        return delement.getType();
    }

    
    // =========================================================================
    //   Text Methods
    // =========================================================================

    // Inherit Javadoc.
    public DissectableString getDissectableString(DissectableText text)
            throws DissectionException {
        DissectableWBDOMText dtext = (DissectableWBDOMText)text;
        return dtext.getDissectableString();
    }

    
    // =========================================================================
    //   Iterator Methods
    // =========================================================================

    // Inherit Javadoc.
    public DissectableIterator childrenIterator(
            DissectableElement element, 
            DissectableIterator iterator,
            int start) {
        WBDOMElement delement = (WBDOMElement) element;
        WBDOMNode child = delement.getChildren();
        for (int i=0; i < start; i++) {
            child = child.getNext();
        }
        return new WBDOMDissectableIterator(child);
    }

    // Inherit Javadoc.
    public DissectableIterator childrenIterator(
            DissectableElement element, 
            DissectableIterator iterator) {
        return childrenIterator(element, iterator,  0);
    }

    
    // =========================================================================
    //   Shared Content Methods
    // =========================================================================

    // Inherit Javadoc.
    public int getSharedContentCount() {
        return document.getStringTable().size();
    }

    
    // =========================================================================
    //   Cost Calculator methods
    // =========================================================================
    
    // Inherit Javadoc.
    public void addDocumentOverhead(Accumulator accumulator)
            throws DissectionException {
        // The overhead for a document is:
        //   cost(version code) + 
        //   cost(public id code) + 
        //   cost(charset code) + 
        //   worst case cost(string table length)
        // Note: this calculates the *worst case* cost of the string table 
        // length for any output document from the input string table.
        // Note: dissection does not currently support the case where the 
        // public id is passed as a literal rather than a code.
        try {
            accumulator.add(
                    1 + // public id is a single byte integer
                    cost(document.getPublicId()) + 
                    cost(document.getCodec().getCharset()) + 
                    cost(document.getStringTable().length()));
        } catch (WBSAXException e) {
            throw new DissectionException(e);
        }
    }

    private final int cost(MultiByteInteger mb_int32) {
        return mb_int32.getBytes().length;
    }
    
    // Inherit Javadoc.
    public void addElementOverhead(DissectableElement element,
            Accumulator accumulator)
            throws DissectionException {
        WBDOMElement delement = (WBDOMElement) element;
        try {
            WBSAXNameSizer nameSummer = new WBSAXNameSizer(accumulator);
            WBSAXAttributeValueSizer attributeValueSummer = 
                    new WBSAXAttributeValueSizer(accumulator);
            AttributesInternalIterator attributesSummer = 
                    new VisitorAttributesIterator(nameSummer, 
                            attributeValueSummer);
            // Calculate the size of the element name.
            delement.accept(nameSummer);
            // Calculate the size of the attributes
            if (delement.hasAttributes()) {
                delement.forEachAttribute(attributesSummer);
            }
            // Add one for the content end marker if will be one.
            if (delement.hasChildren() || delement.getEmptyType() == 
                    EmptyElementType.StartAndEndTag) {
                accumulator.add(1);
            }
        } catch (WBDOMException e) {
            throw new DissectionException(
                        exceptionLocalizer.format("cannot-calculate-size"), e);
        }
    }

    // Inherit Javadoc.
    public void addShardLinkCost(DissectableElement element,
            Accumulator accumulator, ShardLinkDetails details)
            throws DissectionException {
        WBSAXSizingVisitor sizeVisitor = new WBSAXSizingVisitor(
                accumulator, details, inputReferences);
        WBDOMElement delement = (WBDOMElement) element;
        try {
            // Iterate over the children, ignoring their parent.
            VisitorChildrenIterator childrenIterator = 
                    new VisitorChildrenIterator(sizeVisitor);
            if (delement.hasChildren()) {
                delement.forEachChild(childrenIterator);
            }
        } catch (WBDOMException e) {
            // MCSDI0019X="Unable to calculate shard link cost"
            throw new DissectionException(
                        exceptionLocalizer.format(
                                    "cannot-calculate-shard-link-cost"),
                        e);
        }
    }

    // Inherit Javadoc.
    public void addTextCost(DissectableText text, Accumulator accumulator)
            throws DissectionException {
        WBDOMText dtext = (WBDOMText) text; 
        WBSAXValueSizeVisitor valueSummer = 
                new WBSAXValueSizeVisitor(accumulator);
        try {
            // Calculate the size of the text
            dtext.getBuffer().accept(valueSummer);
        } catch (WBSAXException e) {
            throw new DissectionException(
                        exceptionLocalizer.format("cannot-calculate-size"), e);
        }
    }


    // =========================================================================
    //   Debug Methods
    // =========================================================================
    
    // Inherit Javadoc.
    public String getElementDescription(DissectableElement element) 
            throws DissectionException {
        WBDOMElement delement = (WBDOMElement) element;
        try {
            return delement.getName();
        } catch (WBDOMException e) {
            throw new DissectionException(
                        exceptionLocalizer.format("element-description-error"),
                        e);
        }
    }

    // Inherit Javadoc.
    public String getEmptyElementDescription(DissectableElement element)
            throws DissectionException {
        // todo: implement this ala the Open and Close element descriptions?
        // Need to ask Paul what is going on here.
        // NOTE: it will also have to respect the EmptyElementType.
        // NOTE: the TDOM version just returns null here, but then again it
        // didn't support empty elements properly.
        return null;
    }

    // Inherit Javadoc.
    public String getOpenElementDescription(DissectableElement element)
            throws DissectionException {
        WBDOMElement delement = (WBDOMElement) element;
        try {
            StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            pw.print("<");
            // Print the element name
            pw.print(delement.getName());
            // Print the attributes.
            DebugAttributeNamePrinter attributeNamePrinter = 
                    new DebugAttributeNamePrinter(pw);
            DebugAttributeValuePrinter attributeValuePrinter = 
                    new DebugAttributeValuePrinter(pw);
            AttributesInternalIterator attributesPrinter = 
                    new VisitorAttributesIterator(attributeNamePrinter, 
                            attributeValuePrinter) {
                        public void before() throws WBDOMException {
                            pw.println(" ");
                        }
                    };
            if (delement.hasAttributes()) {
                delement.forEachAttribute(attributesPrinter);
            }
            pw.print(">");
            return sw.toString();
        } catch (WBDOMException e) {
            throw new DissectionException(
                        exceptionLocalizer.format(
                                    "error-getting-open-element-description"),
                        e);
        }
    }

    // Inherit Javadoc.
    public String getCloseElementDescription(DissectableElement element)
            throws DissectionException {
        WBDOMElement delement = (WBDOMElement) element;
        try {
            return "</" + delement.getName() + ">";
        } catch (WBDOMException e) {            
            throw new DissectionException(
                        exceptionLocalizer.format(
                                    "close-element-description-error"),
                        e);
        }
    }

    // Inherit Javadoc.
    public String getSharedContentDescription(int index)
            throws DissectionException {
        StringReference reference = inputReferences.createReference(index);
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            reference.accept(new DebugValuePrinter(pw));
            return sw.toString();
        } catch (WBSAXException e) {
            throw new DissectionException(e);
        }
    }

    // Inherit Javadoc.
    public String getTextDescription(DissectableText text)
            throws DissectionException {
        WBDOMText dtext = (WBDOMText) text;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            dtext.getBuffer().accept(new DebugValuePrinter(pw));
            return sw.toString();
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

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/4	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	751/3	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 27-Jun-03	559/4	geoff	VBM:2003060607 make WML use protocol configuration again

 24-Jun-03	365/2	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 13-Jun-03	372/2	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/4	geoff	VBM:2003061006 Enhance WBDOM to support string references

 09-Jun-03	309/2	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
