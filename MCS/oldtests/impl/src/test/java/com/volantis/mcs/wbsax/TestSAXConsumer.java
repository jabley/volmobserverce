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
 * 31-May-03    Geoff           VBM:2003042906 - Add opaque value support. 
 * 01-Jun-03    Geoff           VBM:2003042906 - Add whitespace stripping.
 * 02-Jun-03    Geoff           VBM:2003042906 - Trim leading and trailing
 *                              whitespace too and add to do.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import com.volantis.charset.CharacterRepresentable;
import com.volantis.charset.Encoding;
import com.volantis.charset.EncodingManager;
import com.volantis.mcs.utilities.WhitespaceUtilities;
import com.volantis.xml.sax.ExtendedSAXException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;

/**
 * A class which consumes SAX events and generates the corresponding WBSAX
 * events, in a dirty, testing kinda way. This is useful for generating WBSAX 
 * events from arbitary plain old XML.
 * <p>
 * It's "features" include:
 * <ul>
 *   <li>leading and trailing whitespace trimming (this makes counting 
 *      characters easier).
 *   <li>inability to parse variables in the input.
 *   <li>inability to parse entities in attribute values.
 *   <li>inability to parse XML entities (for example) to create string 
 *      references rather than inline strings (which paul's dissector test 
 *      cases can do).
 * </ul>
 * The lack of variable parsing means we cannot fully use the "XML input" test 
 * data combinations in {@link com.volantis.mcs.wbsax.WBSAXTestCaseAbstract}.
 * There is a to do there to fix this problem.
 * <p>
 * The lack of string reference support means we cannot fully use the 
 * "XML input WBXML output" test data combination as above, and there is a 
 * similar to do there for this.
 */ 
public class TestSAXConsumer extends DefaultHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    // Encoding manager is slow to initialise to do it once only.
    static EncodingManager encodingManager = new EncodingManager();
    
    private WBSAXContentHandler handler;
    
    protected ElementNameFactory elementNamefactory;
    private AttributeStartFactory attributeStartFactory;
    private StringTable stringTable;
    private Codec codec;

    // Optional, with default values.
    private VersionCode version;
    private PublicIdCode publicId;

    // Derived
    private Encoding encoding;
    private StringFactory strings;
    protected StringReferenceFactory references;
    
    protected boolean startElementQueued;
    protected ElementNameCode elementNameCode;
    protected StringReference elementNameReference;
    protected List attrNames;
    protected List attrValues;
    
    private StringBuffer buffer = new StringBuffer();
    
    public TestSAXConsumer(WBSAXContentHandler handler,
            ElementNameFactory elementNameFactory, 
            AttributeStartFactory attrStartFactory, StringTable stringTable, 
            Codec codec, StringFactory strings) throws Exception {
        this.handler = handler;
        this.elementNamefactory = elementNameFactory;
        this.attributeStartFactory = attrStartFactory;
        this.stringTable = stringTable;
        this.codec = codec;
        
        this.version = VersionCode.V1_3;
        this.publicId = PublicIdFactory.UNKNOWN;

        this.strings = strings;
        // Try and set an encoding to use if we were provided a charset.
        CharsetCode charset = codec.getCharset();
        if (charset != null) {
            encoding = encodingManager.getEncoding(charset.getCharsetName());
        }
        this.references = new StringReferenceFactory(stringTable, strings);
    }

    public WBSAXContentHandler getHandler() {
        return handler;
    }

    public void setVersion(VersionCode version) {
        this.version = version;
    }

    public void setPublicId(PublicIdCode publicId) {
        this.publicId = publicId;
    }

    public void startDocument() throws SAXException {
        try {
            handler.startDocument(version, publicId, codec, stringTable, strings);
        } catch (WBSAXException e) {
            throw new ExtendedSAXException(e);
        }
    }
    
    public void endDocument() throws SAXException {
        flushCharacters();
        try {
            if (stringTable != null) {
                stringTable.markComplete();
            }
            handler.endDocument();
        } catch (WBSAXException e) {
            throw new ExtendedSAXException(e);
        }
    }

    public void startElement(String uri, String localName, String qName, 
            Attributes attributes) 
            throws SAXException {
        flushCharacters();
        foundContent();
        startElementQueued = true;

        // Parse the element name.
        // Since the straight WBSAX test cases do not yet use namespaces, 
        // we use a default mechanism to create element name references the
        // same way the real code works. This is a bit lax for a testing
        // environment really - there is a to do for namespace support...
        elementNameCode = elementNamefactory.create(localName);
        if (elementNameCode == null) {
            elementNameReference = references.getReference(localName);
            if (elementNameReference == null) {
                elementNameReference = references.createReference(localName);
            }
        }

        parseElementAttributes(attributes);
    }

    protected void parseElementAttributes(Attributes attributes) {
        // Copy the attributes since it cannot be accessed safely after this
        // method finishes.
        attrNames = new ArrayList();
        attrValues = new ArrayList();
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            String name = attributes.getLocalName(i);
            String value = attributes.getValue(i);
            attrNames.add(name);
            attrValues.add(value);
        }
    }

    private boolean startElement(boolean hasContent) throws SAXException {
        boolean hasAttributes;
        try {
            int length = attrNames.size();
            hasAttributes = length > 0;
            if (elementNameReference != null) {
                handler.startElement(elementNameReference, 
                        hasAttributes, hasContent);
            } else {
                handler.startElement(elementNameCode, 
                        hasAttributes, hasContent);
            }
            if (hasAttributes) {
                handler.startAttributes();
                for (int i = 0; i < length; i++) {
                    String name = (String) attrNames.get(i);
                    Object value = attrValues.get(i);
                    if (value instanceof OpaqueValue) {
                        OpaqueValue ovalue = (OpaqueValue) value;
                        AttributeStartCode start =
                                attributeStartFactory.create(name, null);
                        handler.addAttribute(start);
                        handler.addAttributeValueOpaque(ovalue);
                    } else {
                        String svalue = (String) value;
                        AttributeStartCode start =
                                attributeStartFactory.create(name, svalue);
                        handler.addAttribute(start);
                        WBSAXString string;
                        if (start.getValuePrefix() != null) {
                            int prefixLength = start.getValuePrefix().length();
                            string = strings.create(svalue, prefixLength, 
                                    svalue.length() - prefixLength);
                        } else {
                            string = strings.create(svalue);
                        }
                        // todo: attribute values need entity parsing
                        handler.addAttributeValue(string);
                    }
                }
                handler.endAttributes();
            }
        } catch (WBSAXException e) {
            throw new ExtendedSAXException(e);
        }
        startElementQueued = false;
        elementNameCode = null;
        elementNameReference = null;
        attrNames = null;
        attrValues = null;
        return hasAttributes;
    }

    public void endElement(String s, String s1, String s2) throws SAXException {
        flushCharacters();
        try {
            if (startElementQueued) {
                boolean hasAttributes = startElement(false);
                if (hasAttributes) {
                    handler.endElement();
                }
            } else {
                handler.endContent();
                handler.endElement();
            }
        } catch (WBSAXException e) {
            throw new ExtendedSAXException(e);
        }
    }

    public void characters(char[] chars, int start, int length) 
            throws SAXException {
        foundContent();
        buffer.append(chars, start, length);
    }
    
    public void flushCharacters() 
            throws SAXException {
        int start = 0;
        int length = buffer.length();
        char[] chars = new char[length]; 
        buffer.getChars(0, length, chars, 0);
        buffer.setLength(0);

        // quick hack to eliminiate entirely whitespace block
        if (WhitespaceUtilities.isWhitespace(chars, start, length)) {
            return;
        }
        
        // Slowest possible way to trim leading and trailing whitespace.
        String tmp = new String(chars, start, length);
        tmp.trim();
        chars = tmp.toCharArray();
        
        try {
             // If they provided
             if (encoding != null) {
                 int upto=start;
                 for (int i = start; i < length; i++) {
                     char aChar = chars[i];
                     CharacterRepresentable rep = 
                                            encoding.checkCharacter(aChar);
                     if (rep.notRepresentable()) {
                         if (upto < i) {
                             handler.addContentValue(strings.create(
                                     chars, upto, i - upto));
                         }
                         handler.addContentValueEntity(
                                 new EntityCode(aChar));
                         upto = i + 1;
                     }
                 }
                 if (upto < start + length ) {
                     handler.addContentValue(
                             strings.create(chars, upto, 
                                     start + length - upto));
                 }
             } else {
                 // Assume all the characters are representable. Doesn't
                 // really sound like a good idea.
                 handler.addContentValue(
                         strings.create(chars, start, length));
             }
         } catch (WBSAXException e) {
             throw new ExtendedSAXException(e);
         }
    }

    public void foundContent() throws SAXException {
        if (startElementQueued) {
            startElement(true);
            try {
                handler.startContent();
            } catch (WBSAXException e) {
                throw new ExtendedSAXException(e);
            }
        }
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

 19-Jan-04	2653/6	steve	VBM:2004011304 Merge from proteus

 16-Jan-04	2576/3	steve	VBM:2004011304 Support multibyte character sets

 09-Sep-03	1336/2	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 16-Jan-04	2576/1	steve	VBM:2004011304 Support multibyte character sets

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/2	geoff	VBM:2003071405 works and tested but no design review yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 03-Jul-03	709/2	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/7	geoff	VBM:2003070209 clean up WBSAX test cases

 09-Jun-03	309/2	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
