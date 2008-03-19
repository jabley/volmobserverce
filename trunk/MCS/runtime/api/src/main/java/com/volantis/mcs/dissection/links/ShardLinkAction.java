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

package com.volantis.mcs.dissection.links;

import com.volantis.mcs.dissection.annotation.DissectableArea;
import com.volantis.mcs.dissection.impl.Shard;
import com.volantis.mcs.dissection.impl.ShardLinkActionImpl;

/**
 * An enumeration of the possible actions that a shard link supports.
 */
public abstract class ShardLinkAction {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * This action goes to the next shard.
     */
    public static final ShardLinkAction NEXT;

    /**
     * This action goes to the previous shard.
     */
    public static final ShardLinkAction PREVIOUS;

    static {
        NEXT = new ShardLinkActionImpl("NEXT") {

            /**
             * Get the details for the next link from the shard.
             */
            public ShardLinkDetails getShardLinkDetails(Shard shard) {
                return shard.getNextLink();
            }

            public void incrementLinkCount(DissectableArea dissectableArea) {
                dissectableArea.incrementNextLinkCount();
            }
        };

        PREVIOUS = new ShardLinkActionImpl("PREVIOUS") {

            /**
             * Get the details for the previous link from the shard.
             */
            public ShardLinkDetails getShardLinkDetails(Shard shard) {
                return shard.getPreviousLink();
            }

            public void incrementLinkCount(DissectableArea dissectableArea) {
                dissectableArea.incrementPreviousLinkCount();
            }
        };

    }

    private final String description; // for debug only

    protected ShardLinkAction(String name) {
        description = name;
    }

    public String toString() {
        return description;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
