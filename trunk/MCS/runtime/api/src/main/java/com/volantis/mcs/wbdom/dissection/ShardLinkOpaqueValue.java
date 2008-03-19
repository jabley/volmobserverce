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
 * 31-May-03    Geoff           VBM:2003042906 - Fix getString() to render
 *                              URLs properly.
 * 02-Jun-03    Geoff           VBM:2003042906 - Use the Paul coding monkey to
 *                              fix a bug with getBytes() where it wasn't 
 *                              adding a terminating nul.
 * 02-Jun-03    Geoff           VBM:2003042906 - Use my last remaining brain
 *                              cells to add STR_I to the start of the binary 
 *                              attribute value we generate. Hope this is OK!
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.WBSAXValueVisitor;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.GlobalToken;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.UnsupportedEncodingException;

/**
 * An implementation of {@link OpaqueValue} which represents a shard link 
 * within an WBSAX attribute's value.
 * <p>
 * Unfortunately, this class is now completely misused and abused by 
 * {@link com.volantis.mcs.wbdom.dissection.io.DissectionWBSAXAttributeValueSerialiser}
 * and
 * {@link com.volantis.mcs.wbdom.dissection.io.DissectionWBSAXAttributeValueSizer}
 * as part of the hacks put in place to implement bodgy optimisation.
 * 
 * @todo sort this out when we fix optimisation.
 */ 
public class ShardLinkOpaqueValue extends OpaqueValue {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            ShardLinkOpaqueValue.class);

    private ShardLinkDetails shardLinkDetails;

    /**
     * 
     * This has to be called by the DOM dissection content handler before
     * it attempts to get it's value. 
     * 
     * @param shardLinkDetails
     */ 
    public void setShardLinkDetails(ShardLinkDetails shardLinkDetails) {
        this.shardLinkDetails = shardLinkDetails;
    }

    public byte[] getBytes() throws WBSAXException {
        if (shardLinkDetails != null) {
            try {
                // Extract the bytes for the URL
                byte [] string = getUrl().getBytes("US-ASCII");
                // Wrap that in a STR_I at the start and NUL at the end
                // to create a valid attribute value.
                byte [] attribute = new byte [string.length + 2];
                attribute[0] = GlobalToken.STR_I;
                System.arraycopy(string, 0, attribute, 1, string.length);               
                return attribute;
            } catch (UnsupportedEncodingException e) {                
                throw new WBSAXException(
                            exceptionLocalizer.format(
                                        "url-ascii-encoding-error"),
                            e);
            }
        } else {
            throw new IllegalStateException(
                    "Attempt to render shard link opaque before details set");
        }
    }

    // For debugging?
    public String getString() {
        if (shardLinkDetails != null) {
            return getUrl();
        } else {
            throw new IllegalStateException(
                    "Attempt to render shard link opaque before details set");
        }
    }

    private String getUrl() {
        return shardLinkDetails.getURL().getExternalForm();
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
