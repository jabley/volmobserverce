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
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom.impl;

import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.impl.DebugTStringWriter;
import com.volantis.mcs.dissection.dom.impl.TAttribute;
import com.volantis.mcs.dissection.dom.impl.TCalculatorFactory;
import com.volantis.mcs.dissection.dom.DissectableTestConstants;
import com.volantis.mcs.dissection.dom.*;
import com.volantis.mcs.dissection.impl.UnsupportedVisitor;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.mcs.dom.DocumentAnnotation;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.List;

public class TDissectableDocument
    implements DissectableDocument, DissectableTestConstants, TStringContext {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private DocumentAnnotation annotation;

    private TElement root;

    private final TStringTable stringTable;

    /**
     * This is only set while processing a shard link element.
     */
    private ShardLinkDetails shardLinkDetails;

    private CostCalculator calculator;

    private ShardLinkCalculator shardLinkCalculator;

    public TDissectableDocument(TCalculatorFactory factory) {
        // Create the string table before the calculator as the calculator takes
        // a copy.
        stringTable = new TStringTable();
        calculator = factory.createCalculator(this);
        shardLinkCalculator = new ShardLinkCalculator(this);
    }

    public void setCalculator(CostCalculator calculator) {
        this.calculator = calculator;
    }

    public CostCalculator getCalculator() {
        return calculator;
    }

    public TStringTable getStringTable() {
        return stringTable;
    }

    public void setShardLinkDetails(ShardLinkDetails shardLinkDetails) {
        this.shardLinkDetails = shardLinkDetails;
    }

    public ShardLinkDetails getShardLinkDetails() {
        return shardLinkDetails;
    }

    // =========================================================================
    //   Document Methods
    // =========================================================================

    public void setRootElement(TElement root) {
        this.root = root;
    }

    public DissectableElement getRootElement() {
        return root;
    }

    // =========================================================================
    //   Visit Methods
    // =========================================================================

    public void visitDocument(DocumentVisitor visitor)
        throws DissectionException {

        visitor.visitDocument(this);
    }

    public void visitNode(DissectableNode node,
                          DocumentVisitor visitor)
        throws DissectionException {

        TNode tNode = (TNode) node;
        tNode.visit(this, visitor);
    }

    public
        void visitChildren(DissectableElement element,
                           DocumentVisitor visitor)
        throws DissectionException {

        TElement tElement = (TElement) element;

        List list = tElement.children();
        int size = list.size();
        for (int i = 0; i < size; i += 1) {
            TNode tNode = (TNode) list.get(i);
            tNode.visit(this, visitor);
        }
    }

    // =========================================================================
    //   Annotation Methods
    // =========================================================================

    /**
     * Set the annotation associated with the specified node.
     */
    public void setAnnotation(DissectableNode node, NodeAnnotation annotation) {
        TNode tNode = (TNode) node;
        tNode.setAnnotation(annotation);
    }

    /**
     * Get the annotation associated with the specified node.
     */
    public NodeAnnotation getAnnotation(DissectableNode node) {
        TNode tNode = (TNode) node;
        return tNode.getAnnotation();
    }

    /**
     * Set the annotation associated with this document.
     */
    public void setAnnotation(DocumentAnnotation annotation) {
        this.annotation = annotation;
    }

    /**
     * Get the annotation associated with this document.
     */
    public DocumentAnnotation getAnnotation() {
        return annotation;
    }

    // =========================================================================
    //   Element Methods
    // =========================================================================

    public boolean isElementAtomic(DissectableElement element) {
        return false;
    }

    public boolean isElementEmpty(DissectableElement element) {
        TElement tElement = (TElement) element;
        return tElement.children().isEmpty();
    }

    public ElementType getElementType(DissectableElement element) {
        TElement tElement = (TElement) element;
        return tElement.getType();
    }

    // =========================================================================
    //   Text Methods
    // =========================================================================

    public DissectableString getDissectableString(DissectableText text)
        throws DissectionException {
        return new TDissectableString(text);
    }

    // =========================================================================
    //   Iterator Methods
    // =========================================================================

    public DissectableIterator childrenIterator(DissectableElement element,
                                                DissectableIterator iterator) {
        return childrenIterator(element, iterator, 0);
    }

    public DissectableIterator childrenIterator(DissectableElement element,
                                                DissectableIterator iterator,
                                                int start) {

        TIterator tIterator = (TIterator) iterator;
        if (tIterator == null) {
            tIterator = new TIterator();
        }

        TElement tElement = (TElement) element;

        tIterator.setList(tElement.children());
        tIterator.setIndex(start);

        return tIterator;
    }

    /*
    public boolean iteratorHasNext(DissectableIterator iterator) {
        TIterator tIterator = (TIterator) iterator;
        return tIterator.hasNext();
    }

    public DissectableNode iteratorNext(DissectableIterator iterator) {
        TIterator tIterator = (TIterator) iterator;
        return (DissectableNode) tIterator.next();
    }
    */

    // =========================================================================
    //   Walker Methods
    // =========================================================================

    public DissectableWalker getWalker() {
        return null;
    }

    // =========================================================================
    //   Size Methods
    // =========================================================================

    public boolean isAlwaysEmpty(TElement element) {
        // todo: Do this (better).
        // Previously this returned false, I hacked BR in here since the 
        // existing test documents only have BR as a potential always empty.
        return element.getName().getString(this).equals("br");
    }

    public void addDocumentOverhead(Accumulator accumulator)
        throws DissectionException {

        if (stringTable.size() > 0) {
            // Add the cost of the doctype declaration.
            StringBuffer buffer = new StringBuffer();

            TElement root = (TElement) getRootElement();
            String rootElementName = root.getName().getString(this);
            buffer.append("<!DOCTYPE ")
                .append(rootElementName)
                .append(" [\n]>\n");

            accumulator.add(buffer.length());
        }
        // Add the cost of the xml text declaration
        accumulator.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".length());
    }

    public void addElementOverhead(DissectableElement element,
                                   Accumulator accumulator)
        throws DissectionException {

        calculator.addElementOverhead(element, accumulator);
    }

    public void addShardLinkCost(DissectableElement element,
                                 Accumulator accumulator,
                                 ShardLinkDetails details)
        throws DissectionException {

        shardLinkDetails = details;

        shardLinkCalculator.addCost(element, accumulator);

        shardLinkDetails = null;
    }

    public void addTextCost(DissectableText text, Accumulator accumulator)
        throws DissectionException {

        calculator.addTextCost(text, accumulator);
    }

    // =========================================================================
    //   Shared Content Methods
    // =========================================================================

    public int getSharedContentCount() {
        return stringTable.size();
    }

    // =========================================================================
    //   Debug Methods
    // =========================================================================

    /**
     * Get the element description.
     * @param element The element whose description should be returned.
     * @return A string describing the element.
     */
    public String getElementDescription(DissectableElement element) {
        StringBuffer buffer = new StringBuffer();
        TElement tElement = (TElement) element;
        TString name = tElement.getName();
        name.debugAppend(this, buffer);
        return buffer.toString();
    }

    public String getEmptyElementDescription(DissectableElement element)
        throws DissectionException {
        return null;
    }

    public String getOpenElementDescription(DissectableElement element)
        throws DissectionException {
        return getOpenTag((TElement) element);
    }

    public String getCloseElementDescription(DissectableElement element)
        throws DissectionException {
        return getCloseTag((TElement) element);
    }

    public String getSharedContentDescription(int index)
        throws DissectionException {

        TSimpleString string = stringTable.getEntry(index);
        return string.getContents();
    }

    public String getTextDescription(DissectableText text)
        throws DissectionException {

        String description = getTextContents((TText) text);
        if (description.trim().length() == 0) {
            description = "<whitespace>";
        }

        return description;
    }

    public void writeOpenTag(TElement element, PrintWriter writer,
                             TStringWriter stringWriter)
        throws DissectionException {

        TString name = element.getName();
        writer.write("<");
        stringWriter.write(this, name, writer);

        List attributes = element.attributes();
        int count = attributes.size();
        for (int i = 0; i < count; i += 1) {
            TAttribute attribute = (TAttribute) attributes.get(i);

            // Add the costs of the attribute name and value.
            writer.write(" ");
            stringWriter.write(this, attribute.getName(), writer);
            writer.write("='");
            stringWriter.write(this, attribute.getValue(), writer);
            writer.write("'");
        }
        writer.write(">");
    }

    public void writeCloseTag(TElement element, PrintWriter writer,
                              TStringWriter stringWriter)
        throws DissectionException {

        TString name = element.getName();
        writer.write("</");
        stringWriter.write(this, name, writer);
        writer.write(">");
    }

    public void writeEmptyTag(TElement element, PrintWriter writer,
                              TStringWriter stringWriter)
        throws DissectionException {

        TString name = element.getName();
        writer.write("<");
        stringWriter.write(this, name, writer);

        List attributes = element.attributes();
        int count = attributes.size();
        for (int i = 0; i < count; i += 1) {
            TAttribute attribute = (TAttribute) attributes.get(i);

            // Add the costs of the attribute name and value.
            writer.write(" ");
            stringWriter.write(this, attribute.getName(), writer);
            writer.write("='");
            stringWriter.write(this, attribute.getValue(), writer);
            writer.write("'");
        }
        writer.write("/>");
    }

    public void output(PrintWriter writer)
        throws DissectionException {
        DocumentVisitor visitor = new OutputVisitor(writer);
        visitDocument(visitor);
    }

    public String getEmptyTag(TElement element)
        throws DissectionException {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        writeEmptyTag(element, printWriter, DebugTStringWriter.INSTANCE);
        return writer.getBuffer().toString();
    }

    public String getOpenTag(TElement element)
        throws DissectionException {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        writeOpenTag(element, printWriter, DebugTStringWriter.INSTANCE);
        return writer.getBuffer().toString();
    }

    public String getCloseTag(TElement element)
        throws DissectionException {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        writeCloseTag(element, printWriter, DebugTStringWriter.INSTANCE);
        return writer.getBuffer().toString();
    }

    public String getTextContents(TText text)
        throws DissectionException {
        TString contents = text.getContents();
        StringWriter writer = new StringWriter();
        contents.debugWrite(this, writer);
        return writer.getBuffer().toString();
    }

    private class OutputVisitor
        extends UnsupportedVisitor {

        private PrintWriter writer;

        public OutputVisitor(PrintWriter writer) {
            this.writer = writer;
        }

        public void visitDocumentImpl(DissectableDocument document)
            throws DissectionException {

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

            visitRootElement(document);

            writer.flush();
        }

        public void visitElement(DissectableElement element)
            throws DissectionException {

            writeOpenTag((TElement) element, writer,
                         DebugTStringWriter.INSTANCE);
            visitChildren(element, this);
            writeCloseTag((TElement) element, writer,
                          DebugTStringWriter.INSTANCE);
        }

        public void visitText(DissectableText text)
            throws DissectionException {

            TString contents = ((TText) text).getContents();
            contents.debugWrite(TDissectableDocument.this, writer);
        }
    }

    private class ShardLinkCalculator
        extends UnsupportedVisitor {

        private Accumulator accumulator;

        public ShardLinkCalculator(DissectableDocument document) {
            super(document);
        }

        public void addCost(DissectableElement element,
                            Accumulator accumulator)
            throws DissectionException {

            this.accumulator = accumulator;

            this.document.visitChildren(element, this);
        }

        public void visitElement(DissectableElement element)
            throws DissectionException {

            addElementOverhead(element, accumulator);

            this.document.visitChildren(element, this);
        }

        public void visitText(DissectableText text)
            throws DissectionException {

            addTextCost(text, accumulator);
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 24-Jul-03	807/1	geoff	VBM:2003071405 now with fixed architecture

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	521/3	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
