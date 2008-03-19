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

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.string.CompositeDissectableString;
import com.volantis.mcs.wbdom.WBSAXValueBuffer;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbsax.WBSAXValueVisitor;

/**
 * A implementation of {@link CompositeDissectableString} for WBDOM.
 */ 
public class DissectableWBDOMCompositeString 
        extends CompositeDissectableString {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private DissectableWBSAXValueBuffer buffer;

    public DissectableWBDOMCompositeString(DissectableWBSAXValueBuffer text) 
            throws DissectionException {
        this.buffer = text;
        
        // Need to collect
        // - sum of all text lengths = length
        // - sum of all binary lengths = cost
        // - notice if any shared references there = context depenent
        // - optionally 
        //   - (collect breakpoints)
        //   - collect characters
        //   - collect bytes per char = getRangeCost, getCharacterIndex
        InitialisationIterator itr = new InitialisationIterator();
        forEachChild(itr);
        // this could be cleaner. maybe a end() or last() method?
        initialise(itr.totalChars, itr.totalCost, itr.childCount);
    }

    public void forEachChild(final InternalIterator iterator) 
            throws DissectionException {
        try {
            buffer.forEachWBSAXValueAcceptor(
                    new WBSAXValueBuffer.InternalIterator() {
                public void next(WBSAXValueVisitor.Acceptor value) 
                        throws WBDOMException {
                    try {
                        iterator.next((DissectableString) value);
                    } catch (DissectionException e) {
                        throw new WBDOMException(e);
                    }
                }
            });
        } catch (WBDOMException e) {
            // Hmm. Could we extract the original exception and rethrow that?
            throw new DissectionException(e);
        }
    }

    private static class InitialisationIterator implements InternalIterator {
        int totalChars = 0;
        int totalCost = 0;
        int childCount = 0;
        public void next(DissectableString value) 
                throws DissectionException {
            totalChars += value.getLength();
            totalCost += value.getCost();
            childCount ++;
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

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/6	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
