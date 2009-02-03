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

import com.volantis.mcs.wbdom.NameVisitor;
import com.volantis.mcs.wbdom.CodedNameProvider;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.LiteralNameProvider;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.AttributeStartCode;

import java.io.PrintWriter;

/**
 * A implementation of {@link NameVisitor} for printing attribute names to
 * a print writer. 
 * <p>
 * Used to implement the debugging methods of 
 * {@link com.volantis.mcs.dissection.dom.DissectableDocument}. 
 */ 
class DebugAttributeNamePrinter implements NameVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The writer we are writing our debug stuff to.
     */ 
    private PrintWriter writer;

    public DebugAttributeNamePrinter(PrintWriter writer) {
        this.writer = writer;
    }

    protected void print(String string) {
        writer.print(string);
    }
        
    // Inherit Javadoc.
    public void visitCodeProvider(CodedNameProvider code) throws WBDOMException {
        AttributeStartCode startCode = (AttributeStartCode)code.getCodedName();
        print(startCode.getName() + "=" + startCode.getValuePrefix());
    }

    // Inherit Javadoc.
    public void visitLiteralProvider(LiteralNameProvider literal) throws WBDOMException {
        try {
            print(literal.getLiteralName().resolveString().getString());
        } catch (WBSAXException e) {
            throw new WBDOMException(e);
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

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/