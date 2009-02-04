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
package com.volantis.mcs.wbdom.dissection.io;

import com.volantis.mcs.dissection.dom.Accumulator;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.MultiByteInteger;

/**
 * An abstract class with common features of the sizer classes.
 * <p>
 * Currently this is mainly convenience methods to calculate the size of the
 * various components of a WBSAX stream.
 * <p>
 * NOTE: it would probably be a better design to use these methods via 
 * composition rather than inheritance, but no time for that at the moment...
 */ 
public abstract class AbtractSizer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The dissection accumulator to use for collecting the sizing information.
     */ 
    protected Accumulator accumulator;

    /**
     * Construct an instance of this class, with the accumulator provided.
     * 
     * @param accumulator will be used to collect the sizing information.
     */ 
    public AbtractSizer(Accumulator accumulator) {
        this.accumulator = accumulator;
    }

    /**
     * Calculate the cost of a 1 byte token and an inline string. 
     * <p>
     * Useful for sizing inline strings (STR_I) and extension strings (EXT_I*). 
     * 
     * @param string the string to cost.
     * @throws WBSAXException
     */ 
    final void costTokenAndString(WBSAXString string) throws WBSAXException {
        try {
            accumulator.add(1 + string.getBytes().length);
        } catch (DissectionException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Calculate the cost of a 1 byte token and a string reference.
     * <p>
     * Useful for sizing string references (STR_T) and extension references 
     * (EXT_T*). 
     * 
     * @param reference the string reference to cost.
     * @throws com.volantis.mcs.wbsax.WBSAXException
     */ 
    final void costTokenAndReference(StringReference reference) 
            throws WBSAXException {
        try {
            // This calculates the *worst case* cost of the reference for any 
            // output document from the input string table.
            int referenceCost = 1 + 
                    reference.resolvePhysicalIndex().getBytes().length;
            int contentCost = reference.resolveString().getBytes().length;
            accumulator.addShared(reference.resolveLogicalIndex(), 
                    referenceCost, contentCost);
        } catch (DissectionException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Calculate the cost of a 1 byte token and a multibyte integer. 
     * <p>
     * Useful for sizing entities (ENTITY).
     *  
     * @param integer
     * @throws WBSAXException
     */ 
    protected void costTokenAndMultibyteInteger(MultiByteInteger integer) 
            throws WBSAXException {
        try {
            accumulator.add(1 + integer.getBytes().length);
        } catch (DissectionException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Calculate the cost of a 1 byte token (pretty easy).
     * 
     * @throws WBSAXException
     */ 
    protected void costToken() throws WBSAXException {
        try {
            accumulator.add(1);
        } catch (DissectionException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Calculate the cost of an opaque value.
     * 
     * @param opaque
     * @throws WBSAXException
     */ 
    protected void costOpaque(OpaqueValue opaque) throws WBSAXException {
        try {
            accumulator.add(opaque.getBytes().length);
        } catch (DissectionException e) {
            throw new WBSAXException(e);
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

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/7	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
