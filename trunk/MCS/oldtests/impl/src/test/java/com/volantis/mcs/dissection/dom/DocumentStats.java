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

import com.volantis.mcs.dissection.impl.DissectionHelper;

import java.io.PrintStream;

public class DocumentStats {

    private DissectableAreaStats[] dissectableAreaStats;

    public NodeCounts nodeCounts;

    private ExpectedSharedContentUsages totalContentUsages;

    private ExpectedSharedContentUsages fixedContentUsages;

    public int totalCost;

    public int totalCostMimasAdjust;
    
    public int fixedCost;
    
    public int fixedCostMimasAdjust;
    
    /**
     * If the output size may legally be greater than the total cost (e.g if
     * generating WML from a WBDOM), then specify it here, otherwise you will
     * get a failure indicating that the output size may not be greater than 
     * the total cost.
     */ 
    public int outputSize;
    
    // HACK to force dissection without relying on hints in the input
    public int pageSize;

    public DissectableAreaStats getDissectableAreaStats(int index) {
        DissectableAreaStats stats = null;
        if (dissectableAreaStats == null) {
            dissectableAreaStats
                = new DissectableAreaStats[nodeCounts.dissectableAreaCount];
        } else {
            stats = dissectableAreaStats[index];
        }
        if (stats == null) {
            stats = new DissectableAreaStats();
            dissectableAreaStats[index] = stats;
        }

        return stats;
    }

    public NodeCounts getNodeCounts () {
        if (nodeCounts == null) {
            nodeCounts = new NodeCounts();
        }
        return nodeCounts;
    }

    public ExpectedSharedContentUsages getFixedContentUsages() {
        if (fixedContentUsages == null) {
            fixedContentUsages = new ExpectedSharedContentUsages();
        }
        return fixedContentUsages;
    }

    public ExpectedSharedContentUsages getTotalContentUsages() {
        if (totalContentUsages == null) {
            totalContentUsages = new ExpectedSharedContentUsages();
        }
        return totalContentUsages;
    }

    public void print(PrintStream out) {
        out.println("Node Counts");
        nodeCounts.print(out);
        out.println("Total Cost: " + totalCost);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/3	geoff	VBM:2003070703 merge from metis, and rename files manually, and fix up sizes for whitespace differences

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	521/3	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
