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

import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.StringFactory;

/**
 * A factory for creating objects which populate a WBDOM.
 */ 
public interface WBDOMFactory {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Create a WBDOM Document with the parameters provided.
     * <p>
     * The version and public id are meta data associated with the document.
     * <p>
     * The codec, string table and string factory are utility objects which
     * are created by the caller and shared by all the subsequent objects
     * participating in a WBSAX pipeline.
     * 
     * @param version the coded version of the document.
     * @param publicId the coded public id of the document.
     * @param codec the codec being used for this document.
     * @param stringTable the string table being used for this document, or 
     *      null if there is none.
     * @param strings the string factory being used for this document.
     * @return the created document.
     */ 
    WBDOMDocument createDocument(VersionCode version, PublicIdCode publicId, 
            Codec codec, StringTable stringTable, StringFactory strings);

    /**
     * Create an element node which has a coded name.
     * 
     * @param code the name of the element as a code.
     * @return the element created.
     */ 
    CodedNameElement createCodeElement(ElementNameCode code);

    /**
     * Create an element node which has a literal name.
     * 
     * @param reference the name of the element as a literal.
     * @return the element created.
     */ 
    LiteralNameElement createLiteralElement(StringReference reference);
    
    /**
     * Create an attribute which has a coded "start".
     * 
     * @param code the start of the attribute as a code.
     * @return the attribute created.
     */ 
    CodedStartAttribute createCodeAttribute(AttributeStartCode code);
    
    /**
     * Create an attribute which has a literal name.
     * 
     * @param reference the name of the attribute as a literal.
     * @return the attribute created.
     */ 
    LiteralNameAttribute createLiteralAttribute(StringReference reference);

    /**
     * Create a text node.
     * 
     * @return the text node created.
     */ 
    WBDOMText createText();
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

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 12-Jun-03	385/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
