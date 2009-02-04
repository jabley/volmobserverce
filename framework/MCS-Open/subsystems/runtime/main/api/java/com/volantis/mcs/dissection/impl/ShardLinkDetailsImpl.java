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

import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.utilities.MarinerURL;

// todo: Shard link details needs to have its own SharedContentUsages object
// todo: to track usages of shared content within its body. For now it uses
// todo: the dissectable area's.

public class ShardLinkDetailsImpl
    implements ShardLinkDetails {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private int destinationShardIndex;

    private MarinerURL url;

    private Object userData;

    public void setDestinationShardIndex(int destinationShardIndex) {
        this.destinationShardIndex = destinationShardIndex;
    }

    public int getDestinationShardIndex() {
        return destinationShardIndex;
    }

    public void setURL(MarinerURL url) {
        this.url = url;
    }

    public MarinerURL getURL() {
        return url;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public Object getUserData() {
        return userData;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
