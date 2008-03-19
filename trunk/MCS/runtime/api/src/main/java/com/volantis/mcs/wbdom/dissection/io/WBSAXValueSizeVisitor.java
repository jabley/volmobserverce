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
 * 02-Jun-03    Geoff           VBM:2003042906 - Fix size calculation bug.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection.io;

import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.WBSAXValueVisitor;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.MultiByteInteger;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.Accumulator;
import com.volantis.mcs.wbdom.dissection.io.AbtractSizer;

/**
 * An implementation of {@link WBSAXValueVisitor} which visits WBSAX values
 * in order to calculate their size when rendered into a WBXML output stream.
 */ 
public class WBSAXValueSizeVisitor extends AbtractSizer 
        implements WBSAXValueVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public WBSAXValueSizeVisitor(Accumulator accumulator) {
        super(accumulator);
    }

    // Inherit Javadoc.
    public void visitString(WBSAXString string) throws WBSAXException {
        // Cost the STR_I token and the inline string data.
        costTokenAndString(string);
    }

    // Inherit Javadoc.
    public void visitReference(StringReference reference) throws WBSAXException {
        // Cost the STR_T token and the string reference.
        costTokenAndReference(reference);
    }

    // Inherit Javadoc.
    public void visitEntity(EntityCode entity) throws WBSAXException {
        // Cost the ENTITY token and the entity value.
        costTokenAndMultibyteInteger(entity);
    }

    // Inherit Javadoc.
    public void visitExtension(Extension extension) throws WBSAXException {
        // Cost the EXT* token.
        costToken();
    }

    // Inherit Javadoc.
    public void visitExtensionString(Extension extension, WBSAXString string)
            throws WBSAXException {
        // Cost the EXT_I* token and the inline string data.
        costTokenAndString(string);
    }

    // Inherit Javadoc.
    public void visitExtensionReference(Extension extension, 
            StringReference reference) throws WBSAXException {
        // Cost the EXT_T* token and the string reference.
        costTokenAndReference(reference);
    }

    // Inherit Javadoc.
    public void visitOpaque(OpaqueValue opaque) throws WBSAXException {
        // Cost the opaque value.
        costOpaque(opaque);
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

 26-Aug-03	1238/3	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/5	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 12-Jun-03	368/2	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
