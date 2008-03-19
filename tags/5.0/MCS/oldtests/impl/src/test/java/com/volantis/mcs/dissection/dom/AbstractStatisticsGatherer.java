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

package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.DissectionException;

import java.io.PrintStream;

public abstract class AbstractStatisticsGatherer
    extends AbstractDocumentVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private NodeCounts nodeCounts;

    private DocumentStats statistics;

    public AbstractStatisticsGatherer() {
        statistics = new DocumentStats();
        nodeCounts = statistics.getNodeCounts();
    }

    public DocumentStats getStatistics() {
        return statistics;
    }

    public void visitDocument(DissectableDocument document)
        throws DissectionException {

        this.document = document;
        try {
            gatherDocumentStatistics();
            visitDocumentImpl(document);
        } finally {
            this.document = null;
        }
    }

    protected void gatherDocumentStatistics() {
        nodeCounts.sharedContentCount = document.getSharedContentCount();
    }

    public void visitElement(DissectableElement element)
        throws DissectionException {

        nodeCounts.elementCount += 1;

        visitElementImpl(element);
    }

    public void visitText(DissectableText text)
        throws DissectionException {

        nodeCounts.textCount += 1;

        visitTextImpl(text);
    }

    public void visitShardLink(DissectableElement element)
        throws DissectionException {

        nodeCounts.shardLinkCount += 1;

        visitShardLinkImpl(element);
    }

    public void visitShardLinkGroup(DissectableElement element)
        throws DissectionException {

        nodeCounts.shardLinkGroupCount += 1;

        visitShardLinkGroupImpl(element);
    }

    public void visitShardLinkConditional(DissectableElement element)
        throws DissectionException {

        nodeCounts.shardLinkConditionalCount += 1;

        visitShardLinkConditionalImpl(element);
    }

    public void visitDissectableArea(DissectableElement element)
        throws DissectionException {

        nodeCounts.dissectableAreaCount += 1;

        visitDissectableAreaImpl(element);
    }

    public void visitKeepTogether(DissectableElement element)
        throws DissectionException {

        nodeCounts.keepTogetherCount += 1;

        visitKeepTogetherImpl(element);
    }

    public void print(PrintStream out) {
        nodeCounts.print(out);
    }

    protected abstract void visitElementImpl(DissectableElement element)
        throws DissectionException;

    protected abstract void visitTextImpl(DissectableText text)
        throws DissectionException;

    protected final void visitShardLinkImpl(DissectableElement element)
        throws DissectionException {

        visitElementImpl(element);
    }

    protected final void visitShardLinkGroupImpl(DissectableElement element)
        throws DissectionException {

        visitElementImpl(element);
    }

    protected final void visitShardLinkConditionalImpl(DissectableElement element)
        throws DissectionException {

        visitElementImpl(element);
    }

    protected final void visitDissectableAreaImpl(DissectableElement element)
        throws DissectionException {

        visitElementImpl(element);
    }

    protected final void visitKeepTogetherImpl(DissectableElement element)
        throws DissectionException {

        visitElementImpl(element);
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
