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

import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.dom.DissectableNode;
import com.volantis.mcs.dissection.annotation.DissectableArea;
import com.volantis.mcs.dissection.annotation.Cost;
import com.volantis.mcs.dissection.annotation.DissectableNodeAnnotation;
import com.volantis.mcs.dissection.links.ShardLinkDetails;


/**
 * This class contains all the shard related information needed by the
 * dissector.
 * <p>
 * There is one instance of this class for each shard in each dissectable
 * area.
 * <p>
 * When the shard contents are being selected this object is in the pending
 * state. Once this has been completed it is moved to the ready state.
 */
public class Shard {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Each shard needs to keep track of the shared entries that it contains.
     */
    private SharedContentUsages sharedContentUsages;

    /**
     * The index of the shard within the array of shards in the dissectable
     * area.
     */
    private int index = DissectableArea.UNINITIALISED_INDEX;

    /**
     * The details associated with the next link.
     */
    private ShardLinkDetails nextLink;

    /**
     * The details associated with the previous link.
     */
    private ShardLinkDetails previousLink;

    /**
     * A count of the number of nodes that have been added to this shard.
     */
    private int nodeCount;

    /**
     * This is used to perform temporary calculations while selecting.
     */
    private Cost temporaryShardCost;

    private int availableSpace;

    public void setAvailableSpace(int availableSpace) {
        this.availableSpace = availableSpace;
    }

    public int decrementAvailableSpace(int delta) {
        if (delta > availableSpace) {
            throw new IllegalArgumentException
                ("Delta (" + delta + ") greater than available space ("
                 + availableSpace + ")");
        }

        availableSpace -= delta;

        return availableSpace;
    }

    public int getAvailableSpace() {
        return availableSpace;
    }

    public void setIndex(int index) {
        if (this.index != DissectableArea.UNINITIALISED_INDEX) {
            throw new IllegalStateException("Index already set");
        }
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setSharedContentUsages(SharedContentUsages sharedContentUsages) {
        if (this.sharedContentUsages != null) {
            throw new IllegalStateException("SharedContentUsages already set");
        }
        this.sharedContentUsages = sharedContentUsages;
    }

    public SharedContentUsages getSharedContentUsages() {
        return sharedContentUsages;
    }

    public void setNextLink(ShardLinkDetails nextLink) {
        this.nextLink = nextLink;
    }

    public ShardLinkDetails getNextLink() {
        return nextLink;
    }

    public void setPreviousLink(ShardLinkDetails previousLink) {
        this.previousLink = previousLink;
    }

    public ShardLinkDetails getPreviousLink() {
        return previousLink;
    }

    /**
     * This returns a cost that will if necessary update the
     * {@link SharedContentUsages} of this shard.
     * <p>
     * Before it is returned it's total is set to 0.
     * @return A {@link Cost} object that can be used to calculate shard
     * related costs.
     */
    public Cost getTemporaryShardCost() {
        if (temporaryShardCost == null) {
            temporaryShardCost = new Cost("Shard", sharedContentUsages, true);
        } else {
            temporaryShardCost.resetTotal();
        }
        return temporaryShardCost;
    }


    /**
     * Add a node to this shard.
     * <p>
     * Currently this simply keeps count of the number of nodes added to the
     * shard.
     * @param node The node to add.
     */
    public void addNode (DissectableNodeAnnotation node) {
        nodeCount += 1;
    }

    /**
     * Check to see whether any nodes have been added to the shard.
     * @return True if no nodes have been added and false otherwise.
     */
    public boolean isEmpty () {
        return nodeCount == 0;
    }

    /**
     * Get the number of nodes that have been added to this shard.
     * @return The number of nodes that have been added to this shard.
     */
    public int getNodeCount () {
        return nodeCount;
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

 12-Jun-03	363/3	pduffin	VBM:2003060302 Added some comments to aid in implementation of string dissector

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/3	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
