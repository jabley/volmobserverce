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
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DocumentVisitor;
import com.volantis.mcs.dissection.dom.ElementType;
import com.volantis.mcs.wbdom.WBDOMElement;
import com.volantis.mcs.wbdom.NameVisitor;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.dom.NodeAnnotation;

/**
 * A {@link WBDOMElement} for the special dissector elements, which also 
 * implements {@link DissectableElement}.
 */ 
public class DissectableSpecialElement extends WBDOMElement 
        implements DissectableElement, DissectableWBDOMNode {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private ElementType type;

    public DissectableSpecialElement(ElementType type,
            NodeAnnotation annotation) {
        this.type = type;
        setAnnotation(annotation);
    }

    // Inherit Javadoc.
    public String getName() throws WBDOMException {
        return type.getDescription();
    }

    /**
     * Returns the dissector's element type for this special element.
     *
     * @return the dissector's element type.
     */ 
    public ElementType getType() {
        return type;
    }

    // Inherit Javadoc.
    public void accept(NameVisitor visitor) throws WBDOMException {
        // NameVisitor currently does not support obtaining a normal string
        // name. Normally this is not a problem because only the dissector
        // needs to know the names of it's elements - we never touch them.
        // If we ever needed to visit the entire, undissected DOM for some 
        // reason we would need to add a 
        //  - void visitStringProvider(StringNameProvider string)
        // method to NameVisitor (and create the StringNameProvider class).
        throw new UnsupportedOperationException("Name visiting for special " + 
                "dissector elements is not supported");
    }

    // Inherit Javadoc.
    public void accept(DocumentVisitor visitor) throws DissectionException {
        type.invoke(visitor, this);
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

 15-Jul-03	798/6	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
