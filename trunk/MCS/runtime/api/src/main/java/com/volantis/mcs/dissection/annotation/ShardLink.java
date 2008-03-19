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

import com.volantis.mcs.dissection.impl.Shard;
import com.volantis.mcs.dissection.impl.ShardLinkActionImpl;
import com.volantis.mcs.dissection.links.ShardLinkDetails;

public class ShardLink
    extends AbstractChildShardLinkElementAnnotation {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private ShardLinkActionImpl action;

    public void setAction(ShardLinkActionImpl action) {
        if (this.action != null) {
            throw new IllegalStateException("Action already set");
        }
        this.action = action;
    }

    public ShardLinkActionImpl getAction() {
        return action;
    }

    /**
     * Get the details for this shard link out of the shard.
     * <p>
     * @param shard The shard that this link refers to.
     * @return The appropriate ShardLinkDetails for this links action, or
     * null if the link should not be output.
     */
    public ShardLinkDetails getDetails(Shard shard) {
        return action.getShardLinkDetails(shard);
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
