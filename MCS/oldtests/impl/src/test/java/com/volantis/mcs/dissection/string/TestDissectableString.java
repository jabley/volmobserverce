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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dissection.string;

import com.volantis.mcs.dissection.DissectionException;

import java.util.Arrays;

/**
 * A testing implementation of {@link DissectableString} which includes 
 * customisable costs for each character. 
 */ 
public class TestDissectableString implements DissectableString {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private int totalCost;
    private char chars[];
    private int costs[];

    /**
     * Construct a dissectable string where each character has a cost of 1.
     * @param chars
     */ 
    public TestDissectableString(String chars) {
        int [] costs = new int[chars.length()];
        Arrays.fill(costs, 1);
        initialise(chars, costs);
    }

    /**
     * Construct a dissectable string where each character has the cost 
     * supplied in the related array entry.
     * 
     * @param chars
     * @param costs
     */ 
    public TestDissectableString(String chars, int[] costs) {
        initialise(chars, costs);
    }
    
    private void initialise(String chars, int[] costs) {
        if (chars.length() != costs.length) {
            throw new IllegalArgumentException();
        }
        this.chars = chars.toCharArray();
        this.costs = costs;
        for (int i=0; i < costs.length; i++) {
            totalCost += costs[i];
        }
    }

    public int getLength()
            throws DissectionException {
        return costs.length;
    }

    public int charAt(int index)
            throws DissectionException {
        return chars[index];
    }

    public int getNextBreakPoint(int breakPoint)
            throws DissectionException {
        throw new UnsupportedOperationException();
    }

    public int getPreviousBreakPoint(int breakPoint)
            throws DissectionException {
        throw new UnsupportedOperationException();
    }

    public int getCost()
            throws DissectionException {
        return totalCost;
    }

    public boolean isCostContextDependent()
            throws DissectionException {
        return false;
    }

    public int getRangeCost(int startIndex, int endIndex)
            throws DissectionException {
        int rangeCost = 0;
        for (int i=startIndex; i < endIndex; i++) {
            rangeCost += costs[i];
        }
        return rangeCost;
    }

    public int getCharacterIndex(int startIndex, int cost)
            throws DissectionException {
        if (startIndex < 0 || startIndex >= costs.length) {
            throw new IllegalArgumentException("Invalid start index " + 
                    startIndex);
        }
        int index = startIndex;
        int costSoFar = 0;
        boolean finished = false;
        while (!finished) {
            costSoFar += costs[index];
            if (costSoFar > cost) {
                finished = true;
            } else {
                index ++;
                if (index == costs.length) {
                   finished = true;
                }
            }
        }
        return index;
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

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
