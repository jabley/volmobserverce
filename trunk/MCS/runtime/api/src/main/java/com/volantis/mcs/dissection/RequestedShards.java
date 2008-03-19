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

package com.volantis.mcs.dissection;

/**
 * Encapsulates the set of shards that should be output by the dissector.
 * <p>
 * Each dissectable area within a dissectable document has its own shard.
 */
public interface RequestedShards {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Get the number of dissectable areas.
     * @return The number of dissectable areas.
     */
    public int getCount();

    /**
     * Set the shard to output for the specified dissectable area.
     * @param dissectableAreaIndex The index of the dissectable area that the
     * shard is being requested for.
     * @param shardIndex The shard index within that dissectable area.
     */
    public void setShard(int dissectableAreaIndex, int shardIndex);

    /**
     * Get the index of the shard requested for the specified dissectable area.
     * @param dissectableAreaIndex The index of the dissectable area that the
     * shard was requested.
     * @return The index of the shard requested for the specified dissectable
     * area.
     */
    public int getShard(int dissectableAreaIndex);
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
