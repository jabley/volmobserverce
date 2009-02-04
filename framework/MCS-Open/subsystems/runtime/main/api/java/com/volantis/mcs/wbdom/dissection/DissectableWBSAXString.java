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
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.WBSAXCharacter;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXValueVisitor;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.synergetics.ArrayUtils;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.ArrayUtils;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * An Adaptor which wraps {@link WBSAXString} in order to make it implement 
 * {@link DissectableString}.
 */ 
public class DissectableWBSAXString implements DissectableString, 
        WBSAXValueVisitor.Acceptor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DissectableWBSAXString.class);

    /**
     * The overhead of a WBSAX string. This is the code it start with (STR_I)
     * and the null terminator, each take a byte.
     */ 
    private static int OVERHEAD = 1 + 1; 
    
    protected WBSAXString string;

    // calculated up front
    private char[] chars;
    private int length;
    private int totalCost;
    
    // calculated lazily.
    private int[] costs;
    
    public DissectableWBSAXString(WBSAXString string) throws WBDOMException {
        this.string = string;
        try {
            // Calculate all but the individual character costs now.
            // We calculate them lazily cost it's a bit slow.
            chars = string.getChars();
            length = chars.length;
            // Total cost includes overheads, but already incudes a null term.
            totalCost = string.getBytes().length + OVERHEAD - 1;
        } catch (WBSAXException e) {
            throw new WBDOMException(e);
        }
    }

    // Inherit Javadoc.
    public int charAt(int index) {
        return chars[index];
    }

    // Inherit Javadoc.
    public int getNextBreakPoint(int breakPoint)
            throws DissectionException {
        // DefaultStringDissector doesn't use break points yet...
        throw new IllegalStateException("this should not be called yet");
    }

    // Inherit Javadoc.
    public int getPreviousBreakPoint(int breakPoint)
            throws DissectionException {
        // DefaultStringDissector doesn't use break points yet...
        throw new IllegalStateException("this should not be called yet");
    }

    // Inherit Javadoc.
    public int getLength() {
        return length;
    }

    // Inherit Javadoc.
    public int getCost() {
        return totalCost;
    }

    // Inherit Javadoc.
    public boolean isCostContextDependent()
            throws DissectionException {
        return false;
    }

    // Inherit Javadoc.
    public int getRangeCost(int startIndex, int endIndex)
            throws DissectionException {
        if (startIndex < 0 || startIndex > endIndex || endIndex > length) {
            throw new IllegalArgumentException("Invalid indexes " + 
                    startIndex + "," + endIndex);
        }
        calculateCosts();
        int rangeCost = 0;
        for (int i = startIndex; i < endIndex; i++) {
            rangeCost += costs[i];
        }
        // Add the overhead if we got >0 cost.
        if (rangeCost > 0) {
            rangeCost += OVERHEAD;
        }
        return rangeCost; 
    }

    // Inherit Javadoc.
    public int getCharacterIndex(int startIndex, int cost)
            throws DissectionException {
        if (startIndex < 0 || startIndex >= length) {
            throw new IllegalArgumentException("Invalid start index " + 
                    startIndex);
        }
        calculateCosts();
        int index = startIndex;
        int costSoFar = OVERHEAD;
        boolean finished = false;
        while (!finished) {
            costSoFar += costs[index];
            if (costSoFar > cost) {
                finished = true;
            } else {
                index ++;
                if (index == length) {
                   finished = true;
                }
            }
        }
        return index;
    }

    /**
     * Calculates the costs of each character.
     * <p>
     * This is designed to be called lazily so that we don't calculate these
     * costs till we really need them, since this is s.l.o.w.
     * 
     * @throws DissectionException
     */ 
    private void calculateCosts() throws DissectionException {
        if (costs == null) {
            costs = new int[length];
            try {
                string.forEachCharacter(new WBSAXString.InternalIterator() {
                    int index = 0;
                    public void next(WBSAXCharacter character) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("character[" + index + "] is " + 
                                    character);
                        }
                        costs[index] = character.getBytes().length;
                        index++;
                    }
                });
                if (logger.isDebugEnabled()) {
                    logger.debug("chars for '" + string + "' are " +
                            ArrayUtils.toString(chars));
                    logger.debug("costs for '" + string + "' are " +
                            ArrayUtils.toString(costs));
                }
            } catch (WBSAXException e) {
                throw new DissectionException(e);
            }
        }
    }
    
    // Inherit Javadoc.
    public void accept(WBSAXValueVisitor visitor) throws WBSAXException {
        visitor.visitString(string);
    }
    
    protected String debugName() {
        return "DissectableWBSAXString";
    }
    
    // Inherit Javadoc.
    public String toString() {
        return "[" + debugName() + ":l=" + length + ",tc=" + totalCost + "c=" + 
                ArrayUtils.toString(costs) + "]"; 
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-05	8925/1	allan	VBM:2005062308 Move ArrayUtils to Synergetics

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/2	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 24-Jun-03	365/2	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
