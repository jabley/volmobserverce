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
import com.volantis.mcs.wbsax.WBSAXValueVisitor;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.DissectionException;

/**
 * An Adaptor which wraps {@link EntityCode} in order to make it implement 
 * {@link com.volantis.mcs.dissection.string.DissectableString}.
 */ 
public class DissectableWBSAXEntity extends DissectableWBSAXCharacter {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private EntityCode entity;

    // Add a single character which has cost of the character encoded,
    // i.e. cost(ENTITY + mb_u_int32)
    
    public DissectableWBSAXEntity(EntityCode entityCode) {
        this.entity = entityCode;
    }

    // Inherit Javadoc.
    protected int getChar() {
        return entity.getInteger();
    }

    // Inherit Javadoc.
    public int getCost() {
        return 1 + entity.getBytes().length;
    }

    // Inherit Javadoc.
    public boolean isCostContextDependent()
            throws DissectionException {
        return false;
    }

    // Inherit Javadoc.
    public void accept(WBSAXValueVisitor visitor) throws WBSAXException {
        visitor.visitEntity(entity);
    }

    // Inherit Javadoc.
    public String toString() {
        return "[DissectableWBSAXEntity:c=" + getCost() + "]"; 
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

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/3	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
