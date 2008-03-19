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
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection 
 *                              WBSAX serialiser.
 * 30-May-03    Mat             VBM:2003042906 - Geofss hack removed as it 
 *                              was in the wrong place!
 * 01-Jun-03    Geoff           VBM:2003042906 - Add to do.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection.io;

import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.wbdom.io.WBSAXSerialisationVisitor;
import com.volantis.mcs.wbdom.io.WBSAXElementValueSerialiser;
import com.volantis.mcs.wbdom.io.SerialisationConfiguration;
import com.volantis.mcs.dom.output.SerialisationURLListener;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.ReferenceResolver;
import com.volantis.mcs.wbsax.CopyReferenceResolver;

/**
 * An implementation of {@link WBSAXSerialisationVisitor} which visits a 
 * section of a dissectable WBDOM in order to serialise it out as WBSAX events.
 * <p>
 * NOTE: This is is only use for serialising the contents of shard links as
 * the dissector is responsible for visiting the rest of the DOM it manages.
 * <p>
 * Yes, this is inconsistent.
 */ 
public class DissectionWBSAXSerialisationVisitor 
        extends WBSAXSerialisationVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The details of the shard link we are visiting.
     */ 
    private ShardLinkDetails shardLinkDetails;

    /**
     * Construct an instance of this class.
     * 
     * @param handler the wsax content handler to output WBSAX events to.
     * @param configuration the serialisation configuration which controls 
     *      details of how the serialisation takes place. 
     * @param shardLinkDetails information about the shard link being 
     *      serialised.
     * @param urlListener listener which is informed if an attribute which 
     *      contains a URL is found in the output, as defined by the 
     *      configuration. May be null if not required.
     * @param resolver the copy reference resolver that is being used to 
     *      create the output string table. 
     */ 
    public DissectionWBSAXSerialisationVisitor(WBSAXContentHandler handler, 
            SerialisationConfiguration configuration, 
            ShardLinkDetails shardLinkDetails,
            SerialisationURLListener urlListener,
            CopyReferenceResolver resolver) {
        super(handler, configuration, urlListener);
        this.shardLinkDetails = shardLinkDetails;
        initialiseSerialisers(resolver);
    }

    // Inherit Javadoc.
    protected void createValueSerialisers(ReferenceResolver resolver) {
        // Create a hackky attribute value serialiser that does helps do the
        // bodgy JSESSIONID optimisation that we do. This will be fixed later.
        attributeValueSerialiser = new DissectionWBSAXAttributeValueSerialiser(
                handler, (CopyReferenceResolver) resolver, shardLinkDetails);
        // Create a normal element value serialiser.
        elementValueSerialiser = new WBSAXElementValueSerialiser(handler, 
                resolver);
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

 26-Aug-03	1238/3	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	781/2	geoff	VBM:2003070404 clean up WBSAX

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
