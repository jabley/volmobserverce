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

package com.volantis.mcs.dissection.dom;

import java.io.PrintStream;

public class NodeCounts {

    public int elementCount;
    public int textCount;
    public int shardLinkCount;
    public int shardLinkGroupCount;
    public int shardLinkConditionalCount;
    public int dissectableAreaCount;
    public int keepTogetherCount;
    public int sharedContentCount;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeCounts)) return false;

        final NodeCounts nodeCounts = (NodeCounts) o;

        if (dissectableAreaCount != nodeCounts.dissectableAreaCount) return false;
        if (elementCount != nodeCounts.elementCount) return false;
        if (keepTogetherCount != nodeCounts.keepTogetherCount) return false;
        if (shardLinkConditionalCount != nodeCounts.shardLinkConditionalCount) return false;
        if (shardLinkCount != nodeCounts.shardLinkCount) return false;
        if (shardLinkGroupCount != nodeCounts.shardLinkGroupCount) return false;
        if (textCount != nodeCounts.textCount) return false;
        if (sharedContentCount != nodeCounts.sharedContentCount) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = elementCount;
        result = 29 * result + textCount;
        result = 29 * result + shardLinkCount;
        result = 29 * result + shardLinkGroupCount;
        result = 29 * result + shardLinkConditionalCount;
        result = 29 * result + dissectableAreaCount;
        result = 29 * result + keepTogetherCount;
        result = 29 * result + sharedContentCount;
        return result;
    }

    public void print(PrintStream out) {
        out.println("Element Count: " + elementCount);
        out.println("Text Count: " + textCount);
        out.println("Dissectable Area Count: " + dissectableAreaCount);
        out.println("Shard Link Group Count: " + shardLinkGroupCount);
        out.println("Shard Link Count: " + shardLinkCount);
        out.println("Shard Link Conditional Count: "
                    + shardLinkConditionalCount);
        out.println("Keep Together Count: " + keepTogetherCount);
        out.println("Shared Content Count: " + sharedContentCount);
    }
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
