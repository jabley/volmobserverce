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
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.dissection.dom.DocumentVisitor;
import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.wbdom.WBDOMText;
import com.volantis.mcs.wbdom.WBSAXValueBuffer;

/**
 * An extension of {@link WBDOMText} which implements the 
 * {@link DissectableText} interface so it may be used with the dissector.
 */ 
public class DissectableWBDOMText extends WBDOMText 
        implements DissectableWBDOMNode, DissectableText {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private DissectableWBDOMCompositeString string;

    protected WBSAXValueBuffer createBuffer() {
        return new DissectableWBSAXValueBuffer();
    }

    /**
     * Get the composite dissectable string which encompasses the entire
     * content of this text node.
     * 
     * @return the dissectable string
     * @throws DissectionException
     */ 
    public DissectableString getDissectableString() throws DissectionException {
        if (string == null) {
            string = new DissectableWBDOMCompositeString(
                    (DissectableWBSAXValueBuffer)this.getBuffer()); 
        }
        return string;
    }
    
    //
    // Visitor methods.
    //
    
    // Inherit Javadoc.
    public void accept(DocumentVisitor visitor) throws DissectionException {
        visitor.visitText(this);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
