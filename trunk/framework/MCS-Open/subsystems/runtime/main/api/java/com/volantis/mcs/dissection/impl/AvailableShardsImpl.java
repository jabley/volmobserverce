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

import com.volantis.mcs.dissection.AvailableShards;

public class AvailableShardsImpl
    implements AvailableShards {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Each cell of this array contains the count of the number of shards
     * currently available in the corresponding dissectable area.
     */
    private final int[] shardCount;

    /**
     * Each cell of this array contains a flag to indicate whether the
     * corresponding dissectable area has any more shards.
     */
    private final boolean[] anyMoreShards;

    public AvailableShardsImpl(int count) {
        shardCount = new int[count];
        anyMoreShards = new boolean[count];
    }

    public int getCount() {
        return shardCount.length;
    }

    public int getShardCount(int dissectableAreaIndex) {
        return shardCount[dissectableAreaIndex];
    }

    public boolean anyMoreShards(int dissectableAreaIndex) {
        return anyMoreShards[dissectableAreaIndex];
    }

    public void setShardCount(int dissectableAreaIndex,
                              int count,
                              boolean anyMore) {
        shardCount[dissectableAreaIndex] = count;
        anyMoreShards[dissectableAreaIndex] = anyMore;
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
