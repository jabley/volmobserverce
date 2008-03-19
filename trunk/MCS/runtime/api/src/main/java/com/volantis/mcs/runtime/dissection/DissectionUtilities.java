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

package com.volantis.mcs.runtime.dissection;

import com.volantis.mcs.dissection.DissectableAreaIdentity;
import com.volantis.mcs.dissection.DissectedDocument;
import com.volantis.mcs.dissection.RequestedShards;
import com.volantis.mcs.runtime.FragmentationState;

public class DissectionUtilities {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public static RequestedShards getRequestedShards(FragmentationState fragmentationState,
                                                     DissectedDocument document) {

        // Create a SelectedShards object to hold the shards that have been
        // selected for each dissectable area.
        RequestedShards requestedShards = document.createRequestedShards();

        // Get the shard that has been selected for each dissectable area. This
        // is done by iterating over the dissectable areas looking to see if a
        // shard has been specified for them in the fragmentation state. If no
        // shard has been explicitly set then the first shard is used.
        int count = requestedShards.getCount();
        for (int i = 0; i < count; i += 1) {
            DissectableAreaIdentity identity
                = document.getDissectableAreaIdentity(i);

            // Get the shard index from the fragmentation state. Really we
            // should push the identity into the fragmentation state but for
            // now we will expand it here.
            String inclusionPath = identity.getInclusionPath();
            String name = identity.getName();
            int shardIndex;
            if (fragmentationState == null) {
                shardIndex = 0;
            } else {
                shardIndex = fragmentationState.getShardIndex(inclusionPath,
                                                              name);
            }

            // Store the index in the requested shards.
            requestedShards.setShard(i, shardIndex);
        }

        return requestedShards;
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
