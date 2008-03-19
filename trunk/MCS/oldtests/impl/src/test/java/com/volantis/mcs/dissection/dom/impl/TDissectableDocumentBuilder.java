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
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom.impl;

import com.volantis.mcs.dissection.dom.AbstractDissectableDocumentBuilder;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.ElementType;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.xml.sax.AnnotatedAttributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.ArrayList;

public class TDissectableDocumentBuilder
    extends AbstractDissectableDocumentBuilder {

    private static TString openBrace = new TSimpleString("{");
    private static TString closeBrace = new TSimpleString("}");

    private static TShardLinkURLParameter SHARD_LINK_URL_PARAMETER
        = new TShardLinkURLParameter();

    private TDissectableDocument document;

    private ArrayList elements;

    private TStringTable stringTable;

    public TDissectableDocumentBuilder(TCalculatorFactory factory) {
        document = new TDissectableDocument(factory);
        elements = new ArrayList();

        stringTable = document.getStringTable();
    }

    public TDissectableDocument getDocument() {
        return document;
    }

    private void pushElement(TElement element) {
        elements.add(element);
    }

    private TElement popElement() {
        int last = elements.size() - 1;
        return (TElement) elements.remove(last);
    }

    private TElement peekElement() {
        int last = elements.size() - 1;
        if (last == -1) {
            return null;
        } else {
            return (TElement) elements.get(last);
        }
    }

    private void addElement(TElement element) {
        TElement parent = peekElement();
        if (parent == null) {
            document.setRootElement(element);
        } else {
            parent.addChild(element);
        }
    }

    private void append(TString string) {
        TElement element = peekElement();
        TNode last = element.getLastChild();
        if (last instanceof TText) {
            TText text = (TText) last;
            text.append(string);
        } else {
            element.addChild(new TText(string));
        }
    }

    private void append(char[] chars, int offset, int length) {
        TElement element = peekElement();
        TNode last = element.getLastChild();
        TString string = new TSimpleString(new String(chars, offset, length));
        if (last instanceof TText) {
            TText text = (TText) last;
            text.append(string);
        } else {
            element.addChild(new TText(string));
        }
    }

    protected TString getName(String namespaceURI, String localName,
                              String qName) {

        TString name;
        if (qName != null) {
            name = new TSimpleString(qName);
        } else if (localName != null) {
            name = new TSimpleString(localName);
            if (namespaceURI != null) {
                TCompoundString fq = new TCompoundString();
                fq.addString(openBrace);
                fq.addString(new TSimpleString(namespaceURI));
                fq.addString(closeBrace);
                fq.addString(name);
                name = fq;
            }
        } else {
            throw new IllegalArgumentException("No valid name specified");
        }

        return name;
    }

    public DissectableDocument getDissectableDocument()
        throws SAXException {
        return document;
    }

    public void startSpecialElement(ElementType type,
                                    NodeAnnotation annotation)
        throws SAXException {

        String description = type.getDescription();
        TElement element = new TElement(new TSimpleString(description));
        element.setAnnotation(annotation);
        element.setType(type);

        addElement(element);

        pushElement(element);
    }

    public void endSpecialElement()
        throws SAXException {

        popElement();
    }

    public int addStringTableEntry(String string) {
        return stringTable.addEntry(string);
    }

    public void addSharedStringReference(int index) {
        append(new TStringReference(index));
    }

    public Object getShardLinkURLParameter() {
        return SHARD_LINK_URL_PARAMETER;
    }

    public void characters(char[] chars, int offset, int length)
        throws SAXException {

        append(chars, offset, length);
    }

    public void endDocument()
        throws SAXException {
    }

    public void endElement(String namespaceURI, String localName,
                           String qName)
        throws SAXException {

        popElement();
    }

    public void endPrefixMapping(String s)
        throws SAXException {
    }

    public void ignorableWhitespace(char[] chars, int i, int i1)
        throws SAXException {
    }

    public void processingInstruction(String s, String s1)
        throws SAXException {
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void skippedEntity(String s)
        throws SAXException {
    }

    public void startDocument()
        throws SAXException {
    }

    public void startElement(String namespaceURI, String localName,
                             String qName,
                             AnnotatedAttributes attributes)
        throws SAXException {

        TString name = getName(namespaceURI, localName, qName);
        TElement element = new TElement(name);

        processElement(element, attributes);
    }

    public void startCommonElement(int index, AnnotatedAttributes attributes)
            throws SAXException {
        
        TString name = new TStringReference(index);
        TElement element = new TElement(name);

        processElement(element, attributes);
        
    }

    private void processElement(TElement element,
            AnnotatedAttributes attributes) {
        
        String namespaceURI;
        String localName;
        String qName;
        int count = attributes.getLength();
        for (int i = 0; i < count; i += 1) {
            namespaceURI = attributes.getURI(i);
            localName = attributes.getLocalName(i);
            qName = attributes.getQName(i);

            TString attributeName;

            // Check to see whether this a special attribute and if it is
            // then use the returned object instead of its value.
            TString specialValue = (TString) attributes.getAnnotation(i);
            TString attributeValue;
            if (specialValue == null) {
                attributeName = getName(namespaceURI, localName, qName);
                attributeValue = new TSimpleString(attributes.getValue(i));
            } else {
                attributeName = getName(null, localName, null);
                attributeValue = specialValue;
            }

            TAttribute attribute = new TAttribute(attributeName,
                                                  attributeValue);
            element.addAttribute(attribute);
        }

        addElement(element);

        // Add to the stack of elements.
        pushElement(element);
    }

    public void startPrefixMapping(String s, String s1)
        throws SAXException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
