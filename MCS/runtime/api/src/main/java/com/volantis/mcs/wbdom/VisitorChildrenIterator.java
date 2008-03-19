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
package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbdom.ChildrenInternalIterator;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.WBDOMNode;
import com.volantis.mcs.wbdom.WBDOMVisitor;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;

/**
 * An implementation of {@link ChildrenInternalIterator} which iterates over
 * the children, visiting each one with an instance of {@link WBDOMVisitor}.
 * <p>
 * This can be considered to be a kind of Adaptor which adapts the "internal 
 * iterator" interface to the "visitor" interface.
 */ 
public class VisitorChildrenIterator implements ChildrenInternalIterator {

    /**
     * The WBDOM visitor we will use to visit each child.
     */ 
    private WBDOMVisitor visitor;

    /**
     * Construct an instance of this class with the WBDOM visitor provided.
     * 
     * @param visitor the visitor to visit the children.
     */ 
    public VisitorChildrenIterator(WBDOMVisitor visitor) {
        this.visitor = visitor;
    }

    // Javadoc inherited.
    public void before() throws WBDOMException {
        // By default, do nothing before visiting the children.
        // The sizing/serialising subclasses will override this.
    }

    // Javadoc inherited.
    public void next(WBDOMNode child) throws WBDOMException {
        try {
            child.accept(visitor);
        } catch (WBDOMException e) {
            throw new WBDOMException(e);
        }
    }

    // Javadoc inherited.
    public void after() throws WBDOMException {
        // By default, do nothing after visiting the children.
        // The sizing/serialising subclasses will override this.
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

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
