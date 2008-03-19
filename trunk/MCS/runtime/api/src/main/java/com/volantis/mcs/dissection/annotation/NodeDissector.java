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

package com.volantis.mcs.dissection.annotation;

import com.volantis.mcs.dissection.impl.Shard;
import com.volantis.mcs.dissection.DissectionException;

/**
 * This interface encapsulates the dissection behaviour of the
 */
public interface NodeDissector {

    // =========================================================================
    //   Selection Constants
    //     The following constants are used by the selection process.
    // =========================================================================

    /**
     * Indicates that neither the node or any of its parts can fit in the shard.
     * @see #selectShardContents
     */
    public static final int NODE_CANNOT_FIT = -1;

    /**
     * Indicates that the shard is complete.
     * @see #selectShardContents
     */
    public static final int SHARD_COMPLETE = -2;

    /**
     * Indicates that the node was added to the shard.
     */
    public static final int ADDED_NODE = -3;

    /**
     * This method selects those nodes which belong to the specified shard.
     * <p>
     * A node can belong to either one shard or a consecutive range of shards
     * so each node must store the range of shards that it belongs to.
     *
     * @param shard The shard whose contents are being selected.
     * @return If the current node and all its children which are not part
     * of a previous shard can fit into the space remaining then this method
     * must return {@link #ADDED_NODE}.
     * <p>
     * If neither the current node or any of its children which are not part
     * of a previous shard can fit into the space remaining then this method
     * must return {@link #NODE_CANNOT_FIT}.
     * <p>
     * If some of the children of the current node which are not part of a
     * previous shard could fit in the space remaining but some of them could
     * not then this method must return {@link #SHARD_COMPLETE}.
     * <p>
     * This method should never be called on a completed node, i.e. one
     * whose parts have all been added to a shard. If it is then an exception
     * is thrown.
     * @param shard The shard whose contents are being selected.
     */
    public int selectShardContents(Shard shard)
        throws DissectionException;
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
