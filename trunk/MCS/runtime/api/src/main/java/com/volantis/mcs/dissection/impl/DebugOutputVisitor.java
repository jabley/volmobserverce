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

package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.annotation.DissectedDocumentImpl;
import com.volantis.mcs.dissection.annotation.ElementAnnotation;
import com.volantis.mcs.dissection.annotation.DissectableNodeAnnotation;
import com.volantis.mcs.dissection.annotation.TextAnnotation;
import com.volantis.mcs.dissection.dom.*;

import java.io.PrintWriter;
import java.util.StringTokenizer;

public class DebugOutputVisitor
    extends AbstractDocumentVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private static final char[] INDENT = new char[1024];

    static {
        for (int i = 0; i < INDENT.length; i += 1) {
            INDENT[i] = ' ';
        }

    }

    private PrintWriter writer;

    private int indent = 0;

    public DebugOutputVisitor() {
    }

    public void indent() {
        writer.write(INDENT, INDENT.length - indent, indent);
    }

    public void more() {
        indent += 2;
    }

    public void less() {
        indent -= 2;
    }

    public void debug(DissectedDocumentImpl annotation, PrintWriter writer)
        throws DissectionException {
        document = annotation.getDocument();
        this.writer = writer;

        debugOutputUsages(document, writer, "Fixed Content",
                          annotation.getFixedContentUsages());

        document.visitDocument(this);

        writer.flush();
    }

    public static void debugOutputUsages(DissectableDocument document,
                                         PrintWriter writer,
                                         String description,
                                         SharedContentUsages usages)
        throws DissectionException {

        writer.print("Begin Shared Usages (");
        writer.print(description);
        writer.println(")");

        if (usages != null) {
            int count = usages.getCount();
            for (int i = 0; i < count; i += 1) {
                if (usages.isSharedContentUsed(i)) {
                    writer.print("  ");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(document.getSharedContentDescription(i));
                }
            }
        }
        writer.print("End Shared Content (");
        writer.print(description);
        writer.println(")");
    }

    private String getNodeState(DissectableNodeAnnotation annotation) {
        if (annotation != null) {
            StringBuffer buffer = new StringBuffer();
            annotation.addState(buffer);
            return buffer.toString();
        }
        return "";
    }

    protected void visitDocumentImpl(DissectableDocument document)
        throws DissectionException {
        visitRootElement(document);
    }

    public void visitElement(DissectableElement element)
        throws DissectionException {

        writeElement(document, element,
                     document.getOpenElementDescription(element),
                     document.getCloseElementDescription(element));
    }

    private void writeElement(DissectableDocument document,
                              DissectableElement element,
                              String open, String close)
        throws DissectionException {

        ElementAnnotation annotation
            = (ElementAnnotation) document.getAnnotation(element);

        String state = getNodeState(annotation);
        indent();
        writer.print(state);
        writer.println(open);
        more();
        document.visitChildren(element, this);
        less();
        indent();
        writer.print(state);
        writer.println(close);
    }

    public void visitText(DissectableText text)
        throws DissectionException {

        TextAnnotation annotation
            = (TextAnnotation) document.getAnnotation(text);

        String state = getNodeState(annotation);

        String description = document.getTextDescription(text);
        StringTokenizer tokenizer = new StringTokenizer(description, "\n");
        for (; tokenizer.hasMoreTokens();) {
            indent();
            writer.print(state);
            writer.print("text: ");
            writer.println(tokenizer.nextToken());
        }
    }

    public void visitShardLink(DissectableElement element)
        throws DissectionException {

        indent();
        writer.println("SHARD-LINK");

        more();
        document.visitChildren(element, this);
        less();
    }

    public void visitShardLinkGroup(DissectableElement element)
        throws DissectionException {

        indent();
        writer.println("SHARD-LINK-GROUP");

        more();
        document.visitChildren(element, this);
        less();
    }

    public void visitShardLinkConditional(DissectableElement element)
        throws DissectionException {

        indent();
        writer.println("SHARD-LINK-CONDITIONAL");

        more();
        document.visitChildren(element, this);
        less();
    }

    public void visitDissectableArea(DissectableElement element)
        throws DissectionException {

        // todo: Write out the contents of the shared content table for the
        // todo: dissectable area overhead and the shard links.

        writeElement(document, element,
                     "BEGIN DISSECTABLE-AREA",
                     "END DISSECTABLE-AREA");
    }

    public void visitKeepTogether(DissectableElement element)
        throws DissectionException {

        writeElement(document, element,
                     "BEGIN KEEP-TOGETHER",
                     "END KEEP-TOGETHER");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
