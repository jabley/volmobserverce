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
package com.volantis.mcs.wbsax.example;

import com.volantis.mcs.wbsax.WBSAXTestData;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.PublicIdFactory;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.AttributeValueFactory;
import com.volantis.mcs.wbsax.StringReference;

/**
 * WBSAX test data which has been designed to test those parts of WBSAX not
 * covered by any of the other examples.
 * <p>
 * Currently this only tests literal element names, but should be extended 
 * to complete the coverage.
 */ 
public class SyntheticExampleTestData extends WBSAXTestData {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    // Inherit Javadoc.
    protected void initialise() {
        CharsetCode charset = new CharsetCode(0x6A, "UTF-8");
        codec.setCharset(charset);
        elements = new ElementNameFactory();
        
        attrStarts = new AttributeStartFactory();
        
        attrValues = new AttributeValueFactory();
    }
    
    // Inherit Javadoc.
    public boolean requiresStringTable() {
        return true;
    }

    // Inherit Javadoc.
    public void fireEvents(WBSAXContentHandler handler,
            boolean bufferBefore) throws WBSAXException {
        char[] abcValue = "abc".toCharArray();
        
        StringReference abcRef = null;
        
        if (bufferBefore) {
            abcRef = references.createReference(abcValue);
            stringTable.markComplete();
        }
        
        handler.startDocument(VersionCode.V1_3, PublicIdFactory.UNKNOWN, 
                codec, stringTable, strings);
        
        if (!bufferBefore) {
            abcRef = references.createReference(abcValue);
        }
        handler.startElement(abcRef, false, false);
        
        if (!bufferBefore) {
            stringTable.markComplete();
        }
        handler.endDocument();
    }

    // Inherit Javadoc.
    public int[] getBytes() {
        return new int[] {
            0x03,           // Version 1.3 
            0x01,           // Unknown public id 
            0x6A,           // UTF8
            0x04,           // string table length = 4
            0x61, 0x62, 0x63, 0x00,
                            // 'a', 'b', 'c', null
            0x04,           // LITERAL
            0x00,           // string table offset 0
        };
    }

    // Inherit Javadoc.
    public String getXML() {
        return 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +    
            "<abc/>";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Sep-03	1336/4	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC (more documentation)

 09-Sep-03	1336/2	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 ===========================================================================
*/
