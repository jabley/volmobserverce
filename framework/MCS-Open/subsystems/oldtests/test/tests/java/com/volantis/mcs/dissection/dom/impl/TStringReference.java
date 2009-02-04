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
 * 28-May-03    Paul            VBM:2003052901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom.impl;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.impl.TSimpleString;
import com.volantis.mcs.dissection.dom.impl.TString;
import com.volantis.mcs.dissection.dom.impl.TStringContext;
import com.volantis.mcs.dissection.dom.Accumulator;

import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.io.PrintWriter;

public class TStringReference
    extends AbstractTString {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Entity references are of the form &c<index>; so the overhead assuming
     * a maximum of 100 references is 5.
     */
    public static final int OVERHEAD = 5;

    private int index;

    /**
     * The cost of the entity declaration within the internal DTD.
     */
    private int entityDeclarationCost = -1;

    /**
     * The cost of the entity reference.
     */
    private int entityReferenceCost = -1;

    public TStringReference() {
    }

    public TStringReference(int index) {
        this.index = index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void addCost(TStringContext context,
                        Accumulator accumulator)
        throws DissectionException {

        TStringTable table = context.getStringTable();
        TSimpleString string = table.getEntry(index);
        String contents = string.getContents();

        // Calculate the cost of the entity declaration.
        if (entityDeclarationCost == -1) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            TDissectedContentHandler.writeEntityDeclaration(printWriter,
                                                            string, context,
                                                            index);
            entityDeclarationCost = writer.getBuffer().length();

            String entityReference
                = "&c" + (context.getStringTable().size() - 1) + ";";
            entityReferenceCost = entityReference.length();
        }

        accumulator.addShared(index, entityReferenceCost,
                              entityDeclarationCost);
    }

    public void append(TStringContext context,
                       StringBuffer buffer) {
        buffer.append("&c").append(index).append(";");
    }

    public void debugAppend(TStringContext context,
                            StringBuffer buffer) {

        TStringTable table = context.getStringTable();
        TSimpleString string = table.getEntry(index);
        buffer.append("[");
        string.debugAppend(context, buffer);
        buffer.append("]");
    }

    public void debugWrite(TStringContext context, Writer writer)
        throws DissectionException {
        try {
            TStringTable table = context.getStringTable();
            TSimpleString string = table.getEntry(index);
            writer.write("[");
            writer.write(Integer.toString(index));
            writer.write(":");
            string.debugWrite(context, writer);
            writer.write("]");
        } catch (IOException ioe) {
            throw new DissectionException(ioe);
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

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
