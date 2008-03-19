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
package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.StringFactory;

/**
 * A default implementation of {@link WBDOMFactory} which creates a WBDOM
 * as simply as possible.
 */ 
public class DefaultWBDOMFactory implements WBDOMFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    // Javadoc inherited.
    public WBDOMDocument createDocument(VersionCode version, PublicIdCode publicId,
            Codec codec, StringTable stringTable, StringFactory strings) {
        return new WBDOMDocument(version, publicId, codec, stringTable, 
                strings);
    }

    // Javadoc inherited.
    public CodedNameElement createCodeElement(ElementNameCode code) {
        return new CodedNameElement(code);
    }

    // Javadoc inherited.
    public LiteralNameElement createLiteralElement(StringReference reference) {
        return new LiteralNameElement(reference);
    }

    // Javadoc inherited.
    public CodedStartAttribute createCodeAttribute(AttributeStartCode code) {
        return new CodedStartAttribute(code);
    }

    // Javadoc inherited.
    public LiteralNameAttribute createLiteralAttribute(StringReference reference) {
        return new LiteralNameAttribute(reference);
    }

    // Javadoc inherited.
    public WBDOMText createText() {
        return new WBDOMText();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
