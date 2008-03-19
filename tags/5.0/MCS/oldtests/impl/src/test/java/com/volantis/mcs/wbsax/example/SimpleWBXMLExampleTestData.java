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

import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.PublicIdFactory;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXTestData;

/**
 * WBSAX test data taken from Section 8.1, "A Simple XML Document" in 
 * WAP-192-WBXML-20010725-a, "Binary XML Content Format Specification, 
 * Version 1.3, 25 July 2001".
 * <p>
 * This concentrates specifically on simple element content: entities and 
 * inline strings.
 */ 
public class SimpleWBXMLExampleTestData extends WBSAXTestData {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    protected void initialise() {
        CharsetCode charset = new CharsetCode(3, "US-ASCII");
        codec.setCharset(charset);
        elements = new ElementNameFactory();
        elements.registerElement(0x5, "BR");
        elements.registerElement(0x6, "CARD");
        elements.registerElement(0x7, "XYZ");
    }

    public boolean requiresStringTable() {
        return false;
    }

    public void fireEvents(WBSAXContentHandler handler, 
            boolean bufferBefore) 
            throws WBSAXException {
        if (bufferBefore && stringTable != null) {
            stringTable.markComplete();
        }
        handler.startDocument(VersionCode.V1_3, PublicIdFactory.UNKNOWN, 
                codec, null, strings);
        handler.startElement(elements.create("XYZ"), false, true);
        handler.startContent(); // XYZ
        handler.startElement(elements.create("CARD"), false, true);
        handler.startContent(); // CARD
        handler.addContentValue(strings.create(" X & Y"));
        handler.startElement(elements.create("BR"), false, false);
        handler.addContentValue(strings.create(" X"));
        int nbsp = 0x00A0;
        handler.addContentValueEntity(new EntityCode(nbsp));
        handler.addContentValue(strings.create("="));
        handler.addContentValueEntity(new EntityCode(nbsp));
        handler.addContentValue(strings.create("1 "));
        handler.endContent(); // CARD
        handler.endElement(); // CARD
        handler.endContent(); // XYZ
        handler.endElement(); // XYZ
        if (!bufferBefore && stringTable != null) {
            stringTable.markComplete();
        }
        handler.endDocument();
    }

    public int[] getBytes() {
        return new int[] {
            0x03,           // Version 1.3 
            0x01,           // Unknown public id 
            0x03,           // US-ASCII
            0x00,           // string table length = 0
            0x47,           // XYZ tag, with content
            0x46,           // CARD tag, with content
            0x03,           // STR_I
            0x20, 0x58, 0x20, 0x26, 0x20, 0x59, 0x00, 
                            // ' ', 'X', ' ', '&', ' ', 'Y', null
            0x05,           // BR tag
            0x03,           // STR_I
            0x20, 0x58, 0x00,
                            // ' ', 'X', null
            0x02,           // ENTITY
            0x81, 0x20,     // entity value (160)
            0x03,           // STR_I
            0x3D, 0x00,     // '=', null
            0x02,           // ENTITY
            0x81, 0x20,     // entity value (160)
            0x03,           // STR_I
            0x31, 0x20, 0x00,
                            // '1', ' ', null
            0x01,           // END (CARD content)
            0x01            // END (XYZ content)
        };
    }

    public String getXML() {
        return 
            "<?xml version=\"1.0\" encoding=\"US-ASCII\"?>" +    
            "<XYZ>" +
                "<CARD>" +
                    " X &amp; Y<BR/>" +
                    " X&#160;=&#160;1 " +
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

 03-Jul-03	696/2	geoff	VBM:2003070209 clean up WBSAX test cases

 ===========================================================================
*/
