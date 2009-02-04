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
import com.volantis.mcs.dissection.annotation.ShardLink;
import com.volantis.mcs.dissection.annotation.ShardLinkGroup;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.dissection.links.rules.ShardLinkContentRuleContext;

public class ShardLinkContentRuleContextImpl
    implements ShardLinkContentRuleContext {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private final ShardLinkDetails[] detailsArray;

    private int position;

    private int count;

    private boolean unusedSeparator;

    public ShardLinkContentRuleContextImpl(int max) {
        detailsArray = new ShardLinkDetails[max];
    }

    public void initialise(ShardLinkGroup group,
                           SelectedShards selectedShards) {

        this.count = group.getShardLinkCount();

        DissectableArea area = group.getDissectableArea();
        Shard shard = selectedShards.getShard(area);
        // Get the shard link details from the group.
        int i;
        for (i = 0; i < count; i += 1) {
            ShardLink link = group.getShardLink(i);
            ShardLinkDetails details = link.getDetails(shard);
            detailsArray[i] = details;
        }

        // Make sure that the rest of the array is empty.
        for (; i < detailsArray.length; i += 1) {
            detailsArray[i] = null;
        }

        position = BEFORE_FIRST_LINK;
    }

    public int getCount() {
        return count;
    }

    public ShardLinkDetails getDetails(int index) {
        // Check that the index is within range.
        if (index > count) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return detailsArray[index];
    }

    public int getPosition() {
        return position;
    }

    public void nextPosition() {
        position += 1;
    }

    public boolean isSeparatorUnused() {
        return unusedSeparator;
    }

    public void addedSeparator() {
        if (unusedSeparator) {
            throw new IllegalStateException("Unused separator already");
        }
        unusedSeparator = true;
    }

    public void usedSeparator() {
        unusedSeparator = false;
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
