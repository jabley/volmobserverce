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
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.WBSAXValueVisitor;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.WBSAXValueBuffer;
import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.DissectionException;

/**
 * A extension of {@link WBSAXValueBuffer} which wraps each WBSAX value with a 
 * WBDOM class which implements {@link DissectableString} so that they may 
 * later be composed into a 
 * {@link com.volantis.mcs.dissection.string.CompositeDissectableString} for
 * dissection.
 * <p>
 * In future we could consider avoid the GC overhead caused by wrapping. This
 * might involve having WBDOM versions of the WBSAX factory classes which 
 * create the value objects, for example. 
 */ 
public class DissectableWBSAXValueBuffer extends WBSAXValueBuffer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    //
    // Create the correct WBSAX value wrappers for dissection. 
    //
    
    // Inherit Javadoc.
    public void append(EntityCode entity) {
        add(new DissectableWBSAXEntity(entity));
    }

    // Inherit Javadoc.
    public void append(Extension extension, WBSAXString string) 
            throws WBDOMException {
        add(new DissectableWBSAXExtensionString(extension, string));
    }

    // Inherit Javadoc.
    public void append(WBSAXString string) throws WBDOMException {
        add(new DissectableWBSAXString(string));
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

 15-Jul-03	798/3	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
