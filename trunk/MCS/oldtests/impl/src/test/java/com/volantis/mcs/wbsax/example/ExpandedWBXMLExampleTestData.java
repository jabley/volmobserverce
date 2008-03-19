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
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax.example;

import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.AttributeValueFactory;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.PublicIdFactory;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;

/**
 * WBSAX test data taken from Section 8.2, "An Expanded Example" in 
 * WAP-192-WBXML-20010725-a, "Binary XML Content Format Specification, 
 * Version 1.3, 25 July 2001".
 * <p> 
 * This concentrates on attributes and string references.
 */ 
public class ExpandedWBXMLExampleTestData extends SimpleWBXMLExampleTestData {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    protected void initialise() {
        CharsetCode charset = new CharsetCode(0x6A, "UTF-8");
        codec.setCharset(charset);
        elements = new ElementNameFactory();
        elements.registerElement(0x5, "CARD");
        elements.registerElement(0x6, "INPUT");
        elements.registerElement(0x7, "XYZ");
        elements.registerElement(0x8, "DO");
        
        attrStarts = new AttributeStartFactory();
        attrStarts.registerAttributeStart(0x5, "STYLE", "LIST");
        attrStarts.registerAttributeStart(0x6, "TYPE");
        attrStarts.registerAttributeStart(0x7, "TYPE", "TEXT");
        attrStarts.registerAttributeStart(0x8, "URL", "http://");
        attrStarts.registerAttributeStart(0x9, "NAME");
        attrStarts.registerAttributeStart(0xA, "KEY");
        
        attrValues = new AttributeValueFactory();
        attrValues.registerAttributeValue(0x85, ".org");
        attrValues.registerAttributeValue(0x86, "ACCEPT");
    }
    
    public boolean requiresStringTable() {
        return true;
    }

    /**
     * Note: this uses the char[] variants of the string factory methods to
     * vaguely ensure they work.
     * 
     * @param handler
     * @param bufferBefore
     * @throws com.volantis.mcs.wbsax.WBSAXException
     */ 
    public void fireEvents(WBSAXContentHandler handler,
            boolean bufferBefore) throws WBSAXException {
        char[] abcValue = "abc".toCharArray();
        char[] enterNameValue = " Enter name: ".toCharArray();
        
        StringReference abcRef = null;
        StringReference enterNameRef = null;
        
        if (bufferBefore) {
            abcRef = references.createReference(abcValue);
            enterNameRef = references.createReference(enterNameValue);
            stringTable.markComplete();
        }
        
        handler.startDocument(VersionCode.V1_3, PublicIdFactory.UNKNOWN, 
                codec, stringTable, strings);
        handler.startElement(elements.create("XYZ"), false, true);
        handler.startContent(); // XYZ
        handler.startElement(elements.create("CARD"), true, true);
        handler.startAttributes(); // CARD
        handler.addAttribute(attrStarts.create("NAME", null));
        if (!bufferBefore) {
            abcRef = references.createReference(abcValue);
        }
        handler.addAttributeValue(abcRef);
        handler.addAttribute(attrStarts.create("STYLE", "LIST"));
        handler.endAttributes(); // CARD
        handler.startContent(); // CARD
        handler.startElement(elements.create("DO"), true, false);
        handler.startAttributes(); // DO
        handler.addAttribute(attrStarts.create("TYPE", null));
        handler.addAttributeValue(attrValues.create("ACCEPT"));
        handler.addAttribute(attrStarts.create("URL", "http://"));
        handler.addAttributeValue(strings.create("xyz".toCharArray()));
        handler.addAttributeValue(attrValues.create(".org"));
        handler.addAttributeValue(strings.create("/s".toCharArray()));
        handler.endAttributes(); // DO
        handler.endElement(); // DO
        if (!bufferBefore) {
            enterNameRef = references.createReference(enterNameValue);
        }
        handler.addContentValue(enterNameRef);
        handler.startElement(elements.create("INPUT"), true, false);
        handler.startAttributes(); // INPUT
        handler.addAttribute(attrStarts.create("TYPE", "TEXT"));
        handler.addAttribute(attrStarts.create("KEY", null));
        handler.addAttributeValue(strings.create("N".toCharArray()));
        handler.endAttributes(); // INPUT
        handler.endElement(); // INPUT
        handler.endContent(); // CARD
        handler.endElement(); // CARD
        handler.endContent(); // XYZ
        handler.endElement(); // XYZ
        if (!bufferBefore) {
            stringTable.markComplete();
        }
        handler.endDocument();
    }

    public int[] getBytes() {
        return new int[] {
            0x03,           // Version 1.3 
            0x01,           // Unknown public id 
            0x6A,           // UTF8
            0x12,           // string table length = 18
            0x61, 0x62, 0x63, 0x00,
                            // 'a', 'b', 'c', null
            0x20, 0x45, 0x6E, 0x74, 0x65, 0x72, 0x20, 
                0x6E, 0x61, 0x6D, 0x65, 0x3A, 0x20, 0x00,
                            // ' ', 'E', 'n', 't', 'e', 'r', ' ', 
                            //      'n', 'a', 'm', 'e', ':', ' ', null
            0x47,           // XYZ tag, with content
            0xC5,           // CARD tag, with attributes and content
            0x09,           // NAME=
            0x83,           // STR_T
            0x00,           // string table offset 0
            0x05,           // STYLE="LIST"
            0x01,           // END (CARD attributes)
            0x88,           // DO, with attributes
            0x06,           // TYPE=
            0x86,           // ACCEPT
            0x08,           // URL="http://
            0x03,           // STR_I
            0x78, 0x79, 0x7A, 0x00,
                            // 'x', 'y', 'z', null
            0x85,           // .org
            0x03,           // STR_I
            0x2F, 0x73, 0x00,     
                            // '/', 's', null
            0x01,           // END (DO attributes)
            0x83,           // STR_T
            0x04,           // string table offset 4
            0x86,           // INPUT tag, with attributes
            0x07,           // TYPE="TEXT"
            0x0A,           // KEY=
            0x03,           // STR_I
            0x4E, 0x00,     // 'N', null
            0x01,           // END (INPUT content)
            0x01,           // END (CARD content)
            0x01            // END (XYZ content)
        };
    }

    public String getXML() {
        return 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +    
            "<XYZ>" +
                "<CARD NAME=\"abc\" STYLE=\"LIST\">" +
                    "<DO TYPE=\"ACCEPT\" URL=\"http://xyz.org/s\"/>" +
                    " Enter name: <INPUT TYPE=\"TEXT\" KEY=\"N\"/>" +
                "</CARD>" +
            "</XYZ>";
    }
    
}

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

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	733/1	geoff	VBM:2003070403 port from mimas and fix renames manually

 04-Jul-03	724/1	geoff	VBM:2003070403 first take at cleanup

 03-Jul-03	709/1	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/1	geoff	VBM:2003070209 clean up WBSAX test cases

 ===========================================================================
*/
