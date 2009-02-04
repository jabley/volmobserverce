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
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.string.StringSegment;
import com.volantis.mcs.dissection.impl.DebugOutputVisitor;
import com.volantis.mcs.dissection.links.ShardLinkDetails;

import java.io.PrintWriter;

public class DebugContentHandler
    extends AbstractDissectedContentHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private PrintWriter writer;

    public DebugContentHandler(PrintWriter writer) {
        this.writer = writer;
    }

    protected void startDocumentImpl(SharedContentUsages usages)
        throws DissectionException {

        DebugOutputVisitor.debugOutputUsages(document, writer, "Shared Content",
                                             usages);
    }

    protected void endDocumentImpl()
        throws DissectionException {

        writer.flush();
    }

    public void emptyElement(DissectableElement element)
        throws DissectionException {

        writer.print("EMPTY - ");
        writer.println(document.getEmptyElementDescription(element));
    }

    public void startElement(DissectableElement element)
        throws DissectionException {

        writer.print("START - ");
        writer.println(document.getOpenElementDescription(element));
    }

    public void endElement(DissectableElement element)
        throws DissectionException {

        writer.print("END - ");
        writer.println(document.getCloseElementDescription(element));
    }

    public void shardLink(DissectableElement element,
                          ShardLinkDetails details) {

        writer.println("SHARD LINK: " + details.getURL()
                       + ", " + details.getDestinationShardIndex());
        //writer.println(document.getShardLinkDescription(element));
    }

    public void text(DissectableText text)
        throws DissectionException {

        writer.print("TEXT - ");
        writer.println(document.getTextDescription(text));
    }


    public void text(DissectableText text, StringSegment segment) 
            throws DissectionException {
        writer.print("TEXT - ");
        writer.print("[");
        writer.print(segment.getPrefix());
        writer.print(" ");
        writer.print(segment.getStart());
        writer.print(" ");
        writer.print(segment.getEnd());
        writer.print(" ");
        writer.print(segment.getSuffix());
        writer.print("]: ");
        writer.println(document.getTextDescription(text));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-03	521/3	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
