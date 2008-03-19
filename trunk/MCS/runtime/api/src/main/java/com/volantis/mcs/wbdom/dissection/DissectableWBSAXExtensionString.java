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

import com.volantis.mcs.wbsax.WBSAXValueVisitor;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.dissection.DissectionException;

/**
 * An Adaptor which wraps {@link Extension} and {@link WBSAXString} in order 
 * to make it implement 
 * {@link com.volantis.mcs.dissection.string.DissectableString}.
 * <p>
 * At paul's suggestion, this was implemented so that each "extension string"
 * appears as a single, very costly character. Supposedly, this prevents it 
 * being dissected.
 */ 
public class DissectableWBSAXExtensionString 
        extends DissectableWBSAXCharacter {

    /**
     * A "special" character used to represent extensions. Must be non-
     * space.
     * 
     * @todo Perhaps the "special" character ought to be defined by dissector?
     *      I would imagine it would want to detect it and handle it specially 
     *      (e.g. for debugging?).
     */
    private static final char NON_SPACE_SPECIAL_CHAR = '$';
    
    private Extension extension;
    
    private WBSAXString string;
    
    // Add a single "special" character which has the cost of the string,
    // i.e. cost(STR_I + bytes) 

    public DissectableWBSAXExtensionString(Extension extension, 
            WBSAXString string) throws WBDOMException {
        this.extension = extension;
        this.string = string;
    }

    // Inherit javadoc.
    protected int getChar() {
        return NON_SPACE_SPECIAL_CHAR;
    }

    // Inherit javadoc.
    public int getCost() throws DissectionException {
        try {
            return 1 + string.getBytes().length;
        } catch (WBSAXException e) {
            throw new DissectionException(e);
        }
    }

    // Inherit javadoc.
    public boolean isCostContextDependent()
            throws DissectionException {
        return false;
    }
    
    // Inherit javadoc.
    public void accept(WBSAXValueVisitor visitor) throws WBSAXException {
        visitor.visitExtensionString(extension, string);
    }

    // Inherit javadoc.
    public String toString() {
        try {
            return "[DissectableWBSAXExtensionString:c=" + getCost() + "]";
        } catch (DissectionException e) {
            throw new RuntimeException(e);
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

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/3	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
