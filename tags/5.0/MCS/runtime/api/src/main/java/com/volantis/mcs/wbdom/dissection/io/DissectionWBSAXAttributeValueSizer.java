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
 * 01-Jun-03    Geoff           VBM:2003042906 - Implement shard link costing.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection.io;

import com.volantis.mcs.runtime.dissection.SessionIdentifierSearcher;
import com.volantis.mcs.runtime.dissection.SessionIdentifierSearcherFactory;
import com.volantis.mcs.runtime.dissection.SessionIdentifierURL;
import com.volantis.mcs.wbdom.dissection.ShardLinkOpaqueValue;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.dissection.dom.Accumulator;

/**
 * Hacked up version of {@link WBSAXAttributeValueSizer} which
 * implements part of the optimiser.
 * <p>
 * This finds opaque values containing shard links and chops up the 
 * JSESSIONID's within.
 * <p>
 * Most of the code is "shared" using the Cut and Paste Anti-Pattern with 
 * {@link DissectionWBSAXAttributeValueSerialiser}. Note the string table
 * operation is different though.
 * 
 * @todo clean up optimisation (and remove dependencies between it and wbdom)
 */ 
public class DissectionWBSAXAttributeValueSizer 
    extends WBSAXAttributeValueSizer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private ShardLinkDetails shardLinkDetails;

    /**
     * A StringReferenceFactory to allow us to create StringReferences to 
     * existing string table entries when we extract the jsessionid from the 
     * shard link url.
     */
    private StringReferenceFactory inputReferences;

    public DissectionWBSAXAttributeValueSizer(
            Accumulator accumulator, 
            ShardLinkDetails shardLinkDetails,
            StringReferenceFactory inputReferences) {
        super(accumulator);
        this.shardLinkDetails = shardLinkDetails;
        this.inputReferences = inputReferences;
    }

    // Inherit Javadoc.
    public void visitOpaque(OpaqueValue opaque) throws WBSAXException {
        // Just do the normal thing if this isn't interesting for dissection.
        if (!(opaque instanceof ShardLinkOpaqueValue)) {
            super.visitOpaque(opaque);
            return;
        }
        
        // Make sure that the opaque value has the data it needs to render 
        // itself.
        ShardLinkOpaqueValue dopaque = (ShardLinkOpaqueValue) opaque;
        dopaque.setShardLinkDetails(shardLinkDetails);
        
        /* ShardLinkOpaque values need to be handled differently. We get the
         * url, find the jsessionid and its prefix and suffix. Then the costs of 
         * the prefix and suffix can be added to the normal size but the
         * jsessionid cost must be added to shared size
         */
        SessionIdentifierSearcher searcher
             = SessionIdentifierSearcherFactory.create();            
        SessionIdentifierURL splitURL
             = searcher.getJSessionId(dopaque.getString());
            
        StringFactory strings = inputReferences.getStringFactory();
        // Add the part of the url before the jsessionid as an inline string
        WBSAXString prefix = strings.create(splitURL.getPrefix());
        visitString(prefix);
            
        // If present, add jsessionid to the string table   
        if (splitURL.hasJsessionid()) {
            StringReference jsessionid
                 = inputReferences.getReference(splitURL.getJsessionid());
            visitReference(jsessionid);
        }
            
        // If present, add the part of the url after the jsessionid as an
        // inline string
        if (splitURL.hasSuffix())  {
            WBSAXString suffix = strings.create(splitURL.getSuffix());
            visitString(suffix);                   
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

 06-Oct-03	1469/3	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix rework stuff from phil)

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/3	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	781/2	geoff	VBM:2003070404 clean up WBSAX

 13-Jun-03	372/2	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/