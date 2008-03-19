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

import com.volantis.mcs.wbsax.WBSAXValueVisitor;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.OpaqueValue;

import java.io.PrintWriter;

/**
 * A implementation of {@link WBSAXValueVisitor} for printing attribute names 
 * to a print writer. 
 * <p>
 * Used to implement the debugging methods of 
 * {@link com.volantis.mcs.dissection.dom.DissectableDocument}. 
 */ 
class DebugValuePrinter implements WBSAXValueVisitor {
    
    public DebugValuePrinter(PrintWriter writer) {
        this.writer = writer;
    }

    /**
     * The writer we are writing our debug stuff to.
     */ 
    private PrintWriter writer;

    protected void print(String string) {
        writer.print("[");
        writer.print(string);
        writer.print("]");
    }
        
    // Inherit Javadoc.
    public void visitString(WBSAXString string) 
            throws WBSAXException {
        print(string.getString());
    }

    // Inherit Javadoc.
    public void visitReference(StringReference reference) 
            throws WBSAXException {
        print(reference.resolveString().getString());
    }

    // Inherit Javadoc.
    public void visitEntity(EntityCode entity) 
            throws WBSAXException {
        print("&#" + entity.getInteger() + ";");
    }

    // Inherit Javadoc.
    public void visitExtension(Extension extension) 
            throws WBSAXException {
        print("EXT_" + extension.intValue());
    }

    // Inherit Javadoc.
    public void visitExtensionString(Extension extension, 
            WBSAXString string)
            throws WBSAXException {
        print("EXT_I_" + extension.intValue());
    }

    // Inherit Javadoc.
    public void visitExtensionReference(Extension extension, 
            StringReference string)
            throws WBSAXException {
        print("EXT_T_" + extension.intValue());
    }

    // Inherit Javadoc.
    public void visitOpaque(OpaqueValue opaque) throws WBSAXException {
        print("OPAQUE{" + opaque.toString() + "}");
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

 ===========================================================================
*/
