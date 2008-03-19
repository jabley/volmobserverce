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
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DocumentVisitor;
import com.volantis.mcs.wbdom.CodedNameElement;
import com.volantis.mcs.wbsax.ElementNameCode;

/**
 * A dissectable {@link CodedNameElement}.
 * <p>
 * This implements both our dissectable element interface and also the 
 * dissector's dissectable element interface so it may be used with the 
 * dissector.
 */ 
public class DissectableCodeElement extends CodedNameElement 
        implements DissectableWBDOMElement, DissectableElement {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private boolean atomic;
    
    public DissectableCodeElement(ElementNameCode nameCode, boolean atomic) {
        super(nameCode);
        this.atomic = atomic;
    }

    // Inherit Javadoc.
    public boolean isAtomic() {
        return atomic;
    }

    // Inherit Javadoc.
    public void accept(DocumentVisitor visitor) throws DissectionException {
        visitor.visitElement(this);
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

 ===========================================================================
*/
