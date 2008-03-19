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

package com.volantis.mcs.dissection.annotation;

import com.volantis.mcs.dissection.*;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.impl.*;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.dom.DocumentAnnotation;

import java.io.PrintWriter;

/**
 * This is the base class for the classes which are used to annotate a document
 * tree for the purposes of dissection.
 */
public class DissectedDocumentImpl
    implements DissectedDocument, DocumentAnnotation {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Contains the usages of shared content with the fixed content.
     */
    private SharedContentUsages fixedContentUsages;

    /**
     * Array of dissection areas.
     */
    private DissectableArea[] dissectableAreas;

    private DissectableDocument document;

    private MarinerURL documentLocation;

    /**
     * The maximum number of links in a group in the page.
     */
    private int maxLinksPerGroup;

    /**
     * The total cost of the document.
     */
    private Cost totalCost;

    /**
     * The fixed cost of the document.
     */
    private Cost fixedCost;

    private DissectionURLManager urlManager;

    public DissectedDocumentImpl() {
    }

    public void setDocument(DissectableDocument document) {
        if (this.document != null) {
            throw new IllegalStateException("Document already set");
        }

        this.document = document;
    }

    public DissectableDocument getDocument() {
        return document;
    }

    public void setMaxLinksPerGroup(int maxLinksPerGroup) {
        this.maxLinksPerGroup = maxLinksPerGroup;
    }

    public int getMaxLinksPerGroup() {
        return maxLinksPerGroup;
    }

    /**
     * Set the dissectable areas.
     * <p>
     * This method must only be called once per instance of this class. The
     * array is not copied, rather the reference is stored directly so it must
     * not be modified once this method has been called.
     * @param dissectableAreas The array of dissectable areas.
     */
    public void setDissectableAreas(DissectableArea[] dissectableAreas) {
        if (this.dissectableAreas != null) {
            throw new IllegalStateException("Method already called");
        }

        this.dissectableAreas = dissectableAreas;
    }

    /**
     * Get the DissectableArea at the specified index.
     * @param dissectableAreaIndex The index of the dissectable area within
     * this document.
     * @return The DissectableArea at the specified index.
     */
    public DissectableArea getDissectableArea(int dissectableAreaIndex) {
        return dissectableAreas[dissectableAreaIndex];
    }

    public int getDissectableAreaCount() {
        return dissectableAreas.length;
    }

    public DissectableAreaIdentity getDissectableAreaIdentity(int index) {
        return getDissectableArea(index).getIdentity();
    }

    public RequestedShards createRequestedShards() {
        return new RequestedShardsImpl(dissectableAreas.length);
    }

    public AvailableShards createAvailableShards() {
        return new AvailableShardsImpl(dissectableAreas.length);
    }

    public ShardIterator getShardIterator(DissectionContext dissectionContext,
                                          int dissectableAreaIndex) {
        DissectableArea area = getDissectableArea(dissectableAreaIndex);
        return area.getShardIterator(dissectionContext);
    }

    public void setUrlManager(DissectionURLManager urlManager) {
        this.urlManager = urlManager;
    }

    public DissectionURLManager getUrlManager() {
        return urlManager;
    }

    public Cost getTotalContentCost () {
        if (totalCost == null) {
            SharedContentUsages usages
                = DissectionHelper.createSharedContentUsages(document);
            totalCost = new Cost ("Total Cost", usages, true);
        }

        return totalCost;
    }

    public int getTotalCost() {
        return totalCost.getTotal();
    }

    public SharedContentUsages getTotalContentUsages() {
        return totalCost.getSharedContentUsages();
    }

    public Cost getFixedContentCost () {
        if (fixedCost == null) {
            SharedContentUsages usages
                = DissectionHelper.createSharedContentUsages(document);
            fixedCost = new Cost ("Fixed Cost", usages, true);
        }

        return fixedCost;
    }

    public int getFixedCost() {
        return fixedCost.getTotal();
    }

    public SharedContentUsages getFixedContentUsages() {
        return fixedCost.getSharedContentUsages();
    }

    public void setDocumentLocation(MarinerURL documentLocation) {
        this.documentLocation = documentLocation;
        documentLocation.makeReadOnly();
    }

    public MarinerURL getDocumentURL() {
        return documentLocation;
    }

    /**
     * Create a SelectedShards instance that can hold the selected shards for
     * all the dissectable areas in this document.
     * @return A new SelectedShards instance.
     */
    public SelectedShards createSelectedShards() {
        return new SelectedShards(dissectableAreas.length);
    }

    public void debugOutput(PrintWriter writer)
        throws DissectionException {

        DebugOutputVisitor visitor = new DebugOutputVisitor();
        visitor.debug(this, writer);
    }

}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */


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
