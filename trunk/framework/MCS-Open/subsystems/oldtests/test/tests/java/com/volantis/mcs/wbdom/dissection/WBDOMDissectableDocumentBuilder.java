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
 */

package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.dom.AbstractDissectableDocumentBuilder;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectableDocumentBuilder;
import com.volantis.mcs.dissection.dom.ElementType;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.mcs.wbdom.io.WBSAXParser;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.xml.sax.AnnotatedAttributes;
import com.volantis.xml.sax.ExtendedSAXException;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * An implementation of {@link DissectableDocumentBuilder} for WBDOM.
 * <p>
 * This class plugs in to the infrastructure provided by the dissector tests
 * to allow a WBDOM based {@link DissectableDocument} to be created from
 * an incoming SAX event stream. 
 * <p>
 * Note that the infrastructure emulates string references in element content
 * (but not attribute values) by some tricky SAX hacking to pretend that XML 
 * entities references are really string references.
 */ 
public class WBDOMDissectableDocumentBuilder
    extends AbstractDissectableDocumentBuilder {

    private final WBSAXParser realParser;

    private final DissectionTestSAXConsumer consumer;

    private final WBSAXContentHandler output;

    private StringReferenceFactory references;
    
    public WBDOMDissectableDocumentBuilder(
            WBSAXParser realParser,
            DissectionTestSAXConsumer consumer,
            StringReferenceFactory references) {
        this.realParser = realParser;
        this.consumer = consumer;
        this.references = references;
        this.output = consumer.getHandler();
    }

    public DissectableDocument getDissectableDocument()
        throws SAXException {

        WBDOMDissectableDocument dissectionDocument
                = new WBDOMDissectableDocument(realParser.getDocument());
        return dissectionDocument;
    }

    public void startElement(String namespaceURI, String localName,
            String qName, AnnotatedAttributes attributes) throws SAXException {
        consumer.startElement(namespaceURI, localName, qName, attributes);
    }

    public void startCommonElement(int index, 
            AnnotatedAttributes attributes)
            throws SAXException {
        consumer.startCommonElement(index, attributes);
    }

    public void startSpecialElement(ElementType type,
                                    NodeAnnotation annotation)
        throws SAXException {

        consumer.foundContent();
        SpecialOpaqueElementStart opaqueAdaptor =
            new SpecialOpaqueElementStart(type, annotation);
        try {
            // Note: ASSUMES that special elements always have content.
            // We may live to regret this...
            output.startElement(opaqueAdaptor, true);
            output.startContent();
        } catch (WBSAXException e) {
            throw new ExtendedSAXException(e);
        }
    }

    public void endSpecialElement() throws SAXException {
        try {
            output.endContent();
            output.endElement();
        } catch (WBSAXException e) {
            throw new ExtendedSAXException(e);
        }
    }

    public Object getShardLinkURLParameter() {
        // This needs to return a magic attribute value which when serialised
        // via the dissectable content handler (and for getShardLinkCost)
        // returns the shard link url obtained from the shard link details.
        return new ShardLinkOpaqueValue();
    }

    public int addStringTableEntry(String string) throws SAXException {
        StringReference reference = references.createReference(string);
        return reference.resolveLogicalIndex();
    }

    public void addSharedStringReference(int index) throws SAXException {
        // Note: paul's test case currently only supports string references
        // in content, so presumably this is supposed to add a string 
        // reference to the content?
        try {
            // 
            consumer.foundContent();
            // End any existing text node we have
            consumer.flushCharacters();
            // And create a new reference
            output.addContentValue(references.createReference(index));
        } catch (WBSAXException e) {
            throw new ExtendedSAXException(e);
        }
    }

    // =========================================================================
    //   Delegated methods
    //     The following methods are simply delegated to the consumer.
    // =========================================================================

    public void characters(char[] chars, int i, int i1) throws SAXException {
        consumer.characters(chars, i, i1);
    }

    public void endDocument() throws SAXException {
        consumer.endDocument();
    }

    public void endElement(String s, String s1, String s2) throws SAXException {
        consumer.endElement(s, s1, s2);
    }

    public void endPrefixMapping(String s) throws SAXException {
        consumer.endPrefixMapping(s);
    }

    public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException {
        consumer.ignorableWhitespace(chars, i, i1);
    }

    public void processingInstruction(String s, String s1) throws SAXException {
        consumer.processingInstruction(s, s1);
    }

    public void setDocumentLocator(Locator locator) {
        consumer.setDocumentLocator(locator);
    }

    public void skippedEntity(String s) throws SAXException {
        consumer.skippedEntity(s);
    }

    public void startDocument() throws SAXException {
        consumer.startDocument();
    }

    public void startPrefixMapping(String s, String s1) throws SAXException {
        consumer.startPrefixMapping(s, s1);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/2	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	774/2	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/2	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/3	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	722/2	geoff	VBM:2003070403 merge from mimas; fix rename failures manually

 04-Jul-03	724/6	geoff	VBM:2003070403 first take at cleanup

 04-Jul-03	717/1	geoff	VBM:2003070209 hack backport from proteus to mimas

 03-Jul-03	709/1	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/3	geoff	VBM:2003070209 clean up WBSAX test cases

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 12-Jun-03	385/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 12-Jun-03	368/2	geoff	VBM:2003061006 Enhance WBDOM to support string references

 10-Jun-03	309/5	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/3	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
