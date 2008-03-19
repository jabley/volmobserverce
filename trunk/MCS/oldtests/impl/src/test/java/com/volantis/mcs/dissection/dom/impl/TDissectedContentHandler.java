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
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom.impl;

import com.volantis.mcs.dissection.dom.DissectedContentHandler;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.dissection.dom.TextOutputDocument;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.string.StringSegment;
import com.volantis.mcs.dissection.links.ShardLinkDetails;

import java.io.PrintWriter;

public class TDissectedContentHandler
    implements DissectedContentHandler {

    private TDissectableDocument document;

    private TStringWriter stringWriter = new PlainTStringWriter();

    private PrintWriter writer;

    public TDissectedContentHandler(TextOutputDocument output) {
        writer = new PrintWriter(output.getWriter());
    }

    public TDissectedContentHandler(PrintWriter writer) {
        this.writer = writer;
    }

    public void startDocument(DissectableDocument doc,
                              SharedContentUsages usages)
        throws DissectionException {

        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        this.document = (TDissectableDocument) doc;
        TElement root = (TElement) document.getRootElement();
        String rootElementName = root.getName().getString(document);
        if (usages != null) {
            int count = usages.getCount();
            writer.println("<!DOCTYPE " + rootElementName + " [");
            TStringTable table = document.getStringTable();
            for (int i = 0; i < count; i += 1) {
                if (usages.isSharedContentUsed(i)) {
                    TSimpleString entry = table.getEntry(i);
                    writeEntityDeclaration(writer, entry, document, i);
                }
            }
            writer.println("]>");
        }
    }

    public void endDocument()
        throws DissectionException {
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
    }

    public void text(DissectableText text)
        throws DissectionException {

        TString contents = ((TText) text).getContents();
        writer.write(contents.getString(document));
    }

    public void text(DissectableText text, StringSegment segment)
            throws DissectionException {
        TString contents = ((TText) text).getContents();
        if (segment.getPrefix() != null) {
            writer.write(segment.getPrefix());
        }
        writer.write(contents.getString(document), segment.getStart(), 
                segment.getEnd() - segment.getStart());
        if (segment.getSuffix() != null) {
            writer.write(segment.getSuffix());
        }
    }

    public static void writeEntityDeclaration(PrintWriter writer,
                                              TSimpleString string,
                                              TStringContext context,
                                              int index) {

        writer.println("<!ENTITY c" + index + " \""
                       + string.getString(context)
                       + "\">");
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

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	521/3	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
