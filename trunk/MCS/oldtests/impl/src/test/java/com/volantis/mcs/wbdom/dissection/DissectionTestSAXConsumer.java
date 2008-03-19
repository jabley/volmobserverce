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
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.TestSAXConsumer;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.xml.sax.AnnotatedAttributes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * A {@link TestSAXConsumer} which has been customised explictly for the
 * dissection test cases. This means special support for handling elements
 * which have literal (string reference) names, and for handling "extended"
 * attributes. 
 */ 
public class DissectionTestSAXConsumer extends TestSAXConsumer {

    public DissectionTestSAXConsumer(WBSAXContentHandler handler, 
            ElementNameFactory elementNameFactory, 
            AttributeStartFactory attrStartFactory, StringTable stringTable, 
            Codec codec, StringFactory strings) throws Exception {
        super(handler, elementNameFactory, attrStartFactory, stringTable, 
                codec, strings);
    }

    /**
     * Special start element method for elements with literal (string 
     * reference) names. Note this is not part of SAX so must be called 
     * directly from a reference to this object.
     * 
     * @param index the string reference logical index of the element name
     * @param attributes the element's attributes
     * @throws SAXException if there was a problem
     */ 
    public void startCommonElement(int index, AnnotatedAttributes attributes)
            throws SAXException {
        flushCharacters();
        foundContent();
        startElementQueued = true;
        
        elementNameReference = references.createReference(index);
        
        parseElementAttributes(attributes);
    }

    /**
     * Parse an element's extended attributes, and handle any annotations in 
     * the extended attributes. These annotations are used to pass the special
     * markup attributes from the dissection test case's input format through.  
     * 
     * @param attributes attributes of an element, with optional annotations.
     */ 
    protected void parseElementAttributes(Attributes attributes) {
        AnnotatedAttributes attrs = (AnnotatedAttributes) attributes;
        // Copy the attributes since it cannot be accessed safely after this
        // method finishes.
        attrNames = new ArrayList();
        attrValues = new ArrayList();
        int length = attrs.getLength();
        for (int i = 0; i < length; i++) {
            String name = attrs.getLocalName(i);
            String value = attrs.getValue(i);
            // Check to see whether this a special attribute and if it is
            // then use the returned object instead of its value.
            Object opaque = attrs.getAnnotation(i);
            attrNames.add(name);
            if (opaque != null) {
                attrValues.add(opaque);
            } else {
                attrValues.add(value);
            }
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

 09-Sep-03	1336/3	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC (more documentation)

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 ===========================================================================
*/
