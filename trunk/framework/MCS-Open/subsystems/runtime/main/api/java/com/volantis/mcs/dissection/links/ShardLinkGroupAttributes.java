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

import com.volantis.mcs.dissection.DissectableAreaIdentity;
import com.volantis.mcs.dissection.SpecialElementAttributes;

/**
 * Groups a set of shard link attributes into a single unit.
 */
public class ShardLinkGroupAttributes
    extends SpecialElementAttributes {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The identify of the dissectable area to which this belongs. This must
     * not be null.
     */
    private DissectableAreaIdentity dissectableArea;

    // todo: Document that a reference to this is kept by the dissector.
    public void setDissectableArea(DissectableAreaIdentity dissectableArea) {
        this.dissectableArea = dissectableArea;
    }

    public DissectableAreaIdentity getDissectableArea() {
        return dissectableArea;
    }
    
    // Javadoc inherited.
    public String toString() {
        return "[ShardLinkGroupAttributes:dissectableArea=" + 
                dissectableArea + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
