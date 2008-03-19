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
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.string.StringSegment;
import com.volantis.mcs.dissection.dom.impl.PlainTStringWriter;
import com.volantis.mcs.dissection.dom.impl.TDissectableDocument;
import com.volantis.mcs.dissection.dom.impl.TElement;
import com.volantis.mcs.dissection.dom.*;
import com.volantis.mcs.dissection.impl.UnsupportedVisitor;
import com.volantis.mcs.dissection.links.ShardLinkDetails;

import java.io.PrintWriter;

public class TPlainOutputter
    implements DissectedContentHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private TDissectableDocument document;

    private TStringWriter stringWriter;

    private PrintWriter writer;

    private DocumentVisitor shardLinkOutputter;

    public TPlainOutputter(PrintWriter writer) {
        this.writer = writer;
        stringWriter = PlainTStringWriter.INSTANCE;
    }

    public void startDocument(DissectableDocument document,
                              SharedContentUsages usages)
        throws DissectionException {
        this.document = (TDissectableDocument) document;
        shardLinkOutputter = new ShardLinkOutputter(document);
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    }

    public void endDocument()
        throws DissectionException {
        writer.flush();
    }

    public void emptyElement(DissectableElement element)
        throws DissectionException {

        document.writeEmptyTag((TElement) element, writer, stringWriter);
    }

    public void startElement(DissectableElement element)
        throws DissectionException {

        document.writeOpenTag((TElement) element, writer, stringWriter);
    }

    public void endElement(DissectableElement element)
        throws DissectionException {

        document.writeCloseTag((TElement) element, writer, stringWriter);
    }

    public void shardLink(DissectableElement element,
                          ShardLinkDetails details)
        throws DissectionException {

        document.setShardLinkDetails(details);
        document.visitChildren(element, shardLinkOutputter);
        document.setShardLinkDetails(null);
    }

    public void text(DissectableText text)
        throws DissectionException {

        TText tTest = (TText) text;
        TString string = tTest.getContents();
        writer.write(string.getString(document));
    }

    public void text(DissectableText text, StringSegment segment)
            throws DissectionException {
        TText tTest = (TText) text;
        TString string = tTest.getContents();
        if (segment.getPrefix() != null) {
            writer.write(segment.getPrefix());
        }
        writer.write(string.getString(document), segment.getStart(), 
                segment.getEnd() - segment.getStart());
        if (segment.getSuffix() != null) {
            writer.write(segment.getSuffix());
        }
    }

    private class ShardLinkOutputter
        extends UnsupportedVisitor {

        public ShardLinkOutputter(DissectableDocument document) {
            super (document);
        }

        public void visitElement(DissectableElement element)
            throws DissectionException {
            if (this.document.isElementEmpty(element)) {
                emptyElement(element);
            } else {
                startElement(element);
                this.document.visitChildren(element, this);
                endElement(element);
            }
        }

        public void visitText(DissectableText text)
            throws DissectionException {
            text(text);
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

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 24-Jul-03	807/1	geoff	VBM:2003071405 now with fixed architecture

 24-Jun-03	521/3	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
