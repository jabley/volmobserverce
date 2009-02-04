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

import com.volantis.mcs.dissection.annotation.DissectableArea;

/**
 * This class contains the shards that have been selected for each dissectable
 * area.
 * <p>
 * A dissectable area is identified by its index within the list of areas in
 * the document. The shard is identified by its index with the list of shards
 * in the shard area.
 */
public class SelectedShards {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The index in this array is dissectable areas and the values are the
     * shard indexes within the associated area.
     */
    private Shard[] shards;

    /**
     * Create a new <code>SelectedShard</code> that supports the specified
     * number of dissectable areas.
     * @param count The number of dissectable areas.
     */
    public SelectedShards(int count) {
        shards = new Shard[count];
    }

    public int getCount() {
        return shards.length;
    }

    /**
     * Set the specified shard for the specified dissectable area.
     * @param dissectableArea
     * @param shard
     */
    public void setShard(DissectableArea dissectableArea,
                         Shard shard) {
        shards[dissectableArea.getIndex()] = shard;
    }

    public Shard getShard(DissectableArea dissectableArea) {
        return shards[dissectableArea.getIndex()];
    }

    public Shard getShard(int dissectableAreaIndex) {
        return shards[dissectableAreaIndex];
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