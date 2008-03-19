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

import com.volantis.mcs.utilities.MarinerURL;

/**
 * This contains the information necessary to output a shard specific instance
 * of a shard link.
 */
public interface ShardLinkDetails {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Get the zero based index of the shard that this shard link will navigate
     * to.
     * <p>
     * This could be used to modify the text associated with the shard link
     * in order to give the reader some indication of their position within the
     * shards.
     * @return The zero based index of the shard that this shard link will
     * navigate to.
     */
    public int getDestinationShardIndex();

    /**
     * Get the URL for this shard link.
     * @return The MarinerURL for this shard link.
     */
    public MarinerURL getURL();

    /**
     * Get the user data associated with this shard link.
     * <p>
     * This is the value of the user data attribute that was specified on the
     * shard link attributes for the shard link element for which these details
     * are being provided.
     * @see com.volantis.mcs.dissection.links.rules.ShardLinkContentRule
     */
    public Object getUserData();
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
