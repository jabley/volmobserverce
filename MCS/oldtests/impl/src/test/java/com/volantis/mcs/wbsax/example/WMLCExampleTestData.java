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

import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.PublicIdFactory;
import com.volantis.mcs.wbsax.wml.WMLVersion1_3TokenTable;

/**
 * WBSAX test data taken from Section 14.4 "WML Encoding Examples" in 
 * WAP-191-WML-20000219-a, "Wireless Application Protocol Wireless Markup 
 * Language Specification, Version 1.3".
 * <p>
 * This concentrates on attributes, token handling and WML variables. 
 */ 
public class WMLCExampleTestData extends ExpandedWBXMLExampleTestData {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";
    
    // Required since we don't support 1.2 officially.
    public static final PublicIdCode PUBLIC_ID_WML_1_2 = 
            new PublicIdCode(0x09, "-//WAPFORUM//DTD WML 1.2//EN", 
                    "http://www.wapforum.org/DTD/wml12.dtd");

    protected void initialise() {
        CharsetCode charset = new CharsetCode(0x6A, "UTF-8");
        codec.setCharset(charset);
        // Note this is not actually the correct version, but we do not
        // support 1.2... seems to be close enough.
        WMLVersion1_3TokenTable wml13Tokens = new WMLVersion1_3TokenTable();
        wml13Tokens.registerTags(elements);
        wml13Tokens.registerAttrStarts(attrStarts);
        wml13Tokens.registerAttrValues(attrValues);
    }

    public void fireEvents(WBSAXContentHandler handler,
            boolean bufferBefore) throws WBSAXException {
        String xValue = "X";
        String yValue = "Y";
        
        StringReference xRef = null;
        StringReference yRef = null;
        
        if (bufferBefore) {
            xRef = references.createReference(xValue);
            yRef = references.createReference(yValue);
            stringTable.markComplete();
        }
        
        handler.startDocument(new VersionCode(1,2), 
                PublicIdFactory.UNKNOWN, //PUBLIC_ID_WML_1_2,
                codec, stringTable, strings);
        handler.startElement(elements.create("wml"), false, true);
        handler.startContent();     // wml
        
        handler.startElement(elements.create("card"), true, true);
        handler.startAttributes();  // card
        handler.addAttribute(attrStarts.create("id", null));
        handler.addAttributeValue(strings.create("abc"));
        handler.addAttribute(attrStarts.create("ordered", "true"));
        handler.endAttributes();    // card
        handler.startContent();     // card
        
        handler.startElement(elements.create("p"), false, true);
        handler.startContent();     // p
        
        handler.startElement(elements.create("do"), true, true);
        handler.startAttributes();  // do
        handler.addAttribute(attrStarts.create("type", "accept"));
        handler.endAttributes();    // do
        handler.startContent();     // do
        
        handler.startElement(elements.create("go"), true, false);
        handler.startAttributes();  // go
        handler.addAttribute(attrStarts.create("href", "http://"));
        handler.addAttributeValue(strings.create("xyz"));
        handler.addAttributeValue(attrValues.create(".org/"));
        handler.addAttributeValue(strings.create("s"));
        handler.endAttributes();    // go
        handler.endElement();       // go
        
        handler.endContent();       // do
        handler.endElement();       // do

        handler.addContentValue(strings.create(" X : "));
        if (!bufferBefore) {
            xRef = references.createReference(xValue);
        }
        handler.addContentValueExtension(Extension.TWO, xRef);
        
        handler.startElement(elements.create("br"), false, false);
        
        handler.addContentValue(strings.create(" Y : "));
        if (!bufferBefore) {
            yRef = references.createReference(yValue);
        }
        handler.addContentValueExtension(Extension.TWO, yRef);

        handler.startElement(elements.create("br"), false, false);
        
        handler.addContentValue(strings.create(" Enter name: "));
        
        handler.startElement(elements.create("input"), true, false);
        handler.startAttributes();  // input
        handler.addAttribute(attrStarts.create("type", "text"));
        handler.addAttribute(attrStarts.create("name", null));
        handler.addAttributeValue(strings.create("N"));
        handler.endAttributes();    // input
        handler.endElement();       // input
        
        handler.endContent();       // p
        handler.endElement();       // p
        
        handler.endContent();       // card
        handler.endElement();       // card
        
        handler.endContent();       // wml
        handler.endElement();       // wml
        if (!bufferBefore) {
            stringTable.markComplete();
        }
        handler.endDocument();
    }

    public int[] getBytes() {
        return new int[] {
            0x02,           // Version 1.2
            0x01,           // Unknown Public ID
            //0x09,           // WML 1.2 Public id 
            0x6A,           // UTF8
            0x04,           // string table length = 4
            0x58, 0x00,     // 'X', null
            0x59, 0x00,     // 'Y', null
            
            0x7F,           // wml tag, with content
            
            0xE7,           // card tag, with attributes and content
            0x55,           // id=
            0x03,           // STR_I
            0x61, 0x62, 0x63, 0x00,
                            // 'a', 'b', 'c', null
            0x33,           // ordered=true
            0x01,           // END (card attributes)
            
            0x60,           // p tag
            
            0xE8,           // do tag, with attributes and content
            0x38,           // type=accept
            0x01,           // END (do attributes)
            
            0xAB,           // go, with attributes
            0x4B,           // href="http://
            0x03,           // STR_I
            0x78, 0x79, 0x7A, 0x00,
                            // 'x', 'y', 'z', null
            0x88,           // .org/
            0x03,           // STR_I
            0x73, 0x00,     // 's', null
            
            0x01,           // END (go attributes)
            0x01,           // END (do content)

            0x03,           // STR_I
            0x20, 0x58, 0x20, 0x3A, 0x20, 0x00,     
                            // ' ', 'X', ' ', ':', ' ', null
            0x82,           // EXT_T_2 (direct variable reference)
            0x00,           // string table reference 0

            0x26,           // br tag 

            0x03,           // STR_I
            0x20, 0x59, 0x20, 0x3A, 0x20, 0x00,     
                            // ' ', 'Y', ' ', ':', ' ', null
            0x82,           // EXT_T_2 (direct variable reference)
            0x02,           // string table reference 0

            0x26,           // br tag 

            0x03,
            0x20, 0x45, 0x6E, 0x74, 0x65, 0x72, 0x20, 
                0x6E, 0x61, 0x6D, 0x65, 0x3A, 0x20, 0x00,
                            // ' ', 'E', 'n', 't', 'e', 'r', ' ', 
                            //      'n', 'a', 'm', 'e', ':', ' ', null
            
            0xAF,           // input tag, with attributes
            0x48,           // type="text"
            0x21,           // name=
            0x03,           // STR_I
            0x4E, 0x00,     // 'N', null
            0x01,           // END (input attributes)
            
            0x01,           // END (p content)
            0x01,           // END (card content)
            0x01            // END (wml content)
        };
    }

    public String getXML() {
        return 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +    
            "<wml>" +
                "<card id=\"abc\" ordered=\"true\">" +
                    "<p>" +
                        "<do type=\"accept\">" + 
                            "<go href=\"http://xyz.org/s\"/>" +
                        "</do>" +
                        " X : $(X)<br/>" + 
                        " Y : $(Y)<br/>" + 
                        " Enter name: <input type=\"text\" name=\"N\"/>" +
                    "</p>" +
                "</card>" +
            "</wml>";
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

 04-Jul-03	733/1	geoff	VBM:2003070403 port from mimas and fix renames manually

 04-Jul-03	724/1	geoff	VBM:2003070403 first take at cleanup

 03-Jul-03	709/1	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/1	geoff	VBM:2003070209 clean up WBSAX test cases

 ===========================================================================
*/
