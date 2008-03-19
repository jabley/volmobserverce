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
 * $Header: /src/voyager/com/volantis/mcs/dom/Text.java,v 1.3 2002/03/28 19:14:49 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * 30-May-03    Mat             VBM:2003042906 - Added
 *                              append(OpaqueValue part)
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.WBSAXValueVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * An instance of {@link WBDOMNode} for storing text content.
 */ 
public class WBDOMText extends WBDOMNode {

    /**
     * The value buffer which holds the content.
     */ 
    private WBSAXValueBuffer buffer;

    /**
     * Construct an instance of this class.
     */ 
    public WBDOMText() {
        buffer = createBuffer();
    }

    /**
     * Create the value buffer for this text node.
     * <p>
     * This is a protected factory method to allow subclasses to override
     * the type of value buffer that they use. 
     * 
     * @return the created value buffer.
     */ 
    protected WBSAXValueBuffer createBuffer() {
        return new WBSAXValueBuffer();
    }

    /**
     * Get the buffer containing the WBSAX values which contain the text.
     * 
     * @return the buffer.
     */ 
    public WBSAXValueBuffer getBuffer() {
        return buffer;
    }
    
    //
    // Visitor methods.
    //
    
    // Inherit javadoc.
    public void accept(WBDOMVisitor visitor) throws WBDOMException {
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

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/4	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
