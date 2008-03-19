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
import com.volantis.synergetics.ArrayUtils;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * A abstract implementation of {@link DissectableString} which implements
 * the majority of a generic Composite participant of the Composite pattern
 * for dissectable strings.
 * <p>
 * The only part not implemented is the child iteration. This allows the
 * children to be stored in a way suitable for the DOM implemenation,
 * <p>
 * Using this class allows a DOM implementation of string dissection to be
 * considerably simpler than it might otherwise be. However, a DOM
 * implementation may choose whether to use this class or not.
 *
 * @todo the logic of getRangeCost() was used in SegmentElementValueSerialiser
 * It would be nice to generalise the states and algorithms into a separate
 * re-usable class but there was not time to do this during the development.
 */
public abstract class CompositeDissectableString implements DissectableString {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(CompositeDissectableString.class);

    private static final int IS_BEFORE = -1;
    private static final int IS_AFTER = -2;
    private static final int IS_INSIDE = -3;

    // These must all be initialised via initialise() before we kick off.
    private int totalChars;
    private int totalCost; // including overheads.
    private int childCount;

    private DissectableString[] children;
    private int[] chars;

    protected void initialise(int totalChars, int totalCost, int childCount) {
        this.totalChars = totalChars;
        this.totalCost = totalCost;
        this.childCount = childCount;
    }

    public abstract void forEachChild(InternalIterator iterator)
            throws DissectionException;

    public int getLength()
            throws DissectionException {
        return totalChars;
    }

    public int charAt(int index)
            throws DissectionException {
        collectChildrenAndCharacters();
        return chars[index];
    }

    private void collectChildrenAndCharacters() throws DissectionException {
        if (children == null) {
            children = new DissectableString[childCount];
            chars = new int[0];
            forEachChild(new InternalIterator() {
                int index;

                public void next(DissectableString value)
                        throws DissectionException {
                    // Copy the individual chars into our cache
                    int oldLength = chars.length;
                    int length = value.getLength();
                    chars = ArrayUtils.grow(chars, length);
                    for (int i = 0; i < length; i++) {
                        chars[oldLength + i] = value.charAt(i);
                    }
                    // Save this value for later.
                    children[index] = value;
                    index++;
                }
            });
        }
    }

    public int getNextBreakPoint(int breakPoint)
            throws DissectionException {
        // DefaultStringDissector doesn't use break points yet...
        throw new IllegalStateException("this should not be called yet");
    }

    public int getPreviousBreakPoint(int breakPoint)
            throws DissectionException {
        // DefaultStringDissector doesn't use break points yet...
        throw new IllegalStateException("this should not be called yet");
    }

    public int getCost()
            throws DissectionException {
        return totalCost;
    }

    public boolean isCostContextDependent()
            throws DissectionException {
        return false;
    }

    public int getRangeCost(int rangeStart, int rangeEnd)
            throws DissectionException {
        if (rangeStart < 0 || rangeStart > rangeEnd ||
                rangeEnd < rangeStart || rangeEnd > totalChars) {
            throw new IllegalArgumentException("Invalid indexes " +
                    rangeStart + "," + rangeEnd);
        }
        collectChildrenAndCharacters();

        if (logger.isDebugEnabled()) {
            logger.debug("Finding cost of range " + rangeStart + ":" + rangeEnd);
        }

        int childrenCount = children.length;
        int cost = 0;
        int childStart = 0;
        int childIndex = 0;
        int state = IS_BEFORE;
        while (state != IS_AFTER && childIndex < childrenCount) {
            DissectableString child = children[childIndex];
            int childLength = child.getLength();
            int childEnd = childStart + childLength;
            int lastState = state;
            switch (state) {
                case IS_BEFORE:
                    // If the child ends after the range starts...
                    if (childEnd > rangeStart) {
                        // Then the child overlaps the start of the range.
                        int start = rangeStart - childStart;
                        // If the child also overlaps the end of the range...
                        if (childEnd > rangeEnd) {
                            // Then the end point is in this child.
                            // So find the endpoint and that's all folks...
                            cost = child.getRangeCost(start, rangeEnd -
                                    childStart);
                            state = IS_AFTER;
                        } else {
                            // The end point is in a future child.
                            // So collect the partial cost and keep going.
                            cost = child.getRangeCost(start, childLength);
                            state = IS_INSIDE;
                        }
                    }
                    // else, we are still in IN_BEFORE state.
                    break;
                case IS_INSIDE:
                    // If the child ends after the range ends...
                    if (childEnd > rangeEnd) {
                        // The end point is in this child.
                        // So collect the partial cost and we are finished.
                        cost += child.getRangeCost(0, rangeEnd - childStart);
                        state = IS_AFTER;
                    } else {
                        // The endpoint is in a future child.
                        // So add the cost of this child and continue.
                        cost += child.getCost();
                    }
                    break;
                default:
                    throw new IllegalStateException("Illegal state " + state);

            }
            if (logger.isDebugEnabled()) {
                logger.debug(getStateName(lastState) + "->" +
                        getStateName(state) + " child=" + childIndex +
                        " index=" + childStart + " cost=" + cost );
            }
            childStart += childLength;
            childIndex++;
        }

        // We can arrive here if we run out of children or if we find the
        // end point. In either case, the cost we have calculated will be
        // correct.
        return cost;
    }

    // endcost is a length rather than an index...
    public int getCharacterIndex(int rangeStart, int rangeCost)
            throws DissectionException {
        collectChildrenAndCharacters();
        // todo: ought we to limit rangeCost to be < totalCost here?
        if (rangeStart < 0 || rangeStart > totalChars) {
            throw new IllegalArgumentException("Invalid start index " +
                    rangeStart);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Finding index from " + rangeStart + " for cost " + rangeCost);
        }

        int endPoint = 0;
        int childrenCount = children.length;
        int cost = 0;
        int childStart = 0;
        int childIndex = 0;
        int state = IS_BEFORE;
        while (state != IS_AFTER && childIndex < childrenCount) {
            DissectableString child = children[childIndex];
            int childLength = child.getLength();
            int childCost = child.getCost();
            int lastState = state;
            switch (state) {
                case IS_BEFORE:
                    // If the child ends after the range starts...
                    if (childStart + childLength > rangeStart) {
                        // The the start point is in this child.
                        int start = rangeStart - childStart;
                        // If the child cost is greater than the range cost
                        if (childCost > rangeCost) {
                            // Then the end point is also in this child.
                            endPoint = childStart + child.getCharacterIndex(
                                    start, rangeCost);
                            state = IS_AFTER;
                        } else {
                            // The end point is in a future child.
                            // So collect the partial cost and keep going.
                            cost += child.getRangeCost(start, childLength);
                            state = IS_INSIDE;
                        }
                    }
                    // else, we are still in IN_BEFORE state.
                    break;
                case IS_INSIDE:
                    int remainingCost = rangeCost - cost;
                    // If the child cost is greater than the remaining cost
                    if (childCost > remainingCost) {
                        // The end point is in this child.
                        // So calculate the end point and we are finished.
                        endPoint = childStart + child.getCharacterIndex(
                                0, remainingCost);
                        state = IS_AFTER;
                    } else {
                        // The end point is in a future child.
                        // So add the cost of this child and continue.
                        cost += childCost;
                    }
                    break;
                default:
                    throw new IllegalStateException("Illegal state " + state);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(getStateName(lastState) + "->" +
                        getStateName(state) + " child=" + childIndex +
                        " index=" + childStart + " cost=" + cost );
            }
            childStart += childLength;
            childIndex++;
        }
        // We can arrive here if we run out of children or if we find the
        // end point. If it is the former, we still need to calculate the end
        // point - luckily this is very easy :-).
        if (state != IS_AFTER) {
            endPoint = childStart;
        }
        return endPoint;
    }

    String getStateName(int state) {
        switch (state) {
            case IS_BEFORE:
                return "Before";
            case IS_INSIDE:
                return "Inside";
            case IS_AFTER:
                return "After";
            default:
                throw new IllegalStateException();
        }
    }

    public interface InternalIterator {
        void next(DissectableString string) throws DissectionException;
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

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
