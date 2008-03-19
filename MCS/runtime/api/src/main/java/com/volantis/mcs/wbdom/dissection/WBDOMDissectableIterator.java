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
 * 30-May-03    Mat             VBM:2003042906 - Check for currentChild being
 *                              null in hasNext()
 * 02-Jun-03    Geoff           VBM:2003042906 - Commit fixes put in by Paul
 *                              since I am too brain dead to fix it myself.
 *                              Thanks Paul!
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.dom.DissectableIterator;
import com.volantis.mcs.dissection.dom.DissectableNode;
import com.volantis.mcs.wbdom.WBDOMNode;

/**
 * An implementation of {@link DissectableIterator} for use by 
 * {@link WBDOMDissectableDocument}.
 */ 
class WBDOMDissectableIterator implements DissectableIterator {
    
    private WBDOMNode current;
    
    /**
     * Create an instance of this class, with the child node supplied.
     * 
     * @param first the first node that should be returned by this iterator.
     */ 
    WBDOMDissectableIterator(WBDOMNode first) {
        this.current = first;
    }

    // Inherit javadoc.
    public DissectableNode next() {
        WBDOMNode next = current;
        current = next.getNext();
        return (DissectableNode) next;
    }
        
    // Inherit javadoc.
    public boolean hasNext() {
        return current != null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
