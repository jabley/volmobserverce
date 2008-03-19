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
package com.volantis.mcs.wbdom.dissection.io;

import com.volantis.mcs.dissection.string.StringSegment;
import com.volantis.mcs.wbdom.io.WBSAXElementValueSerialiser;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.WBSAXValueVisitor;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * An implementation of {@link WBSAXValueVisitor} which wraps a 
 * {@link WBSAXElementValueSerialiser} in order to restrict the serialisation 
 * to only serialise the content which is inside the range of a dissected 
 * {@link StringSegment}.
 * <p>
 * This is used for serialising text blocks which have been dissected.
 * 
 * @todo logic here was derived from CompositeDissectableString.getRangeCost(). 
 * It would be nice to generalise the states and algorithms into a separate 
 * re-usable class but there was not time to do this during the development. 
 */ 
public class SegmentElementValueSerialiser implements WBSAXValueVisitor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(SegmentElementValueSerialiser.class);

    private static final int IS_BEFORE = -1;
    private static final int IS_INSIDE = -2;
    private static final int IS_AFTER = -3;

    private StringFactory strings;
    private WBSAXElementValueSerialiser elementValueSerialiser;
    private int state;
    private int rangeStart;
    private int rangeEnd;
    private int childStart; 

    public SegmentElementValueSerialiser(
            StringFactory strings,
            WBSAXElementValueSerialiser elementValueSerialiser, 
            StringSegment segment) {
        this.strings = strings;
        this.elementValueSerialiser = elementValueSerialiser;
        this.state = IS_BEFORE;
        rangeStart = segment.getStart();
        rangeEnd = segment.getEnd();
    }

    // Inherit Javadoc.
    public void visitString(final WBSAXString string) throws WBSAXException {
        final int childLength = string.getChars().length;
        serialise(childLength, new SegmentValueSerialiser() {
            public void serialise(int startIndex, int endIndex) 
                    throws WBSAXException {
                if (startIndex > 0 || endIndex < childLength) {
                    // serialise substring
                    String substring = string.getString().substring(
                            startIndex, endIndex);
                    WBSAXString saxSubString = strings.create(substring);
                    elementValueSerialiser.visitString(saxSubString);
                } else {
                    // serialise entire string
                    elementValueSerialiser.visitString(string);
                }
            }
        });
    }

    // Inherit Javadoc.
    public void visitReference(StringReference reference) throws WBSAXException {
        throw new UnsupportedOperationException("not implemented");
    }

    // Inherit Javadoc.
    public void visitEntity(final EntityCode entity) throws WBSAXException {
        // Slow but it should work. Optimise when we have more tests...
        serialise(1, new SegmentValueSerialiser() {
            public void serialise(int startIndex, int endIndex) 
                    throws WBSAXException {
                if (startIndex != 0 || endIndex != 1) {
                    throw new IllegalArgumentException("Invalid entity range "
                            + startIndex + "," + endIndex);
                } else {
                    // serialise entire entity
                    elementValueSerialiser.visitEntity(entity);
                }
            }
        });
    }

    // Inherit Javadoc.
    public void visitExtension(Extension extension) throws WBSAXException {
        throw new IllegalStateException();
    }

    // Inherit Javadoc.
    public void visitExtensionString(Extension extension, final WBSAXString string)
            throws WBSAXException {
        // Slow but it should work. Optimise when we have more tests...
        final int childLength = string.getChars().length;
        serialise(childLength, new SegmentValueSerialiser() {
            public void serialise(int startIndex, int endIndex) 
                    throws WBSAXException {
                if (startIndex != 0 || endIndex != childLength) {
                    throw new IllegalArgumentException("Invalid extension string range "
                            + startIndex + "," + endIndex);
                } else {
                    // serialise entire string
                    elementValueSerialiser.visitString(string);
                }
            }
        });
    }

    // Inherit Javadoc.
    public void visitExtensionReference(Extension extension, StringReference string)
            throws WBSAXException {
        throw new UnsupportedOperationException("not implemented"); // yet
    }

    // Inherit Javadoc.
    public void visitOpaque(final OpaqueValue opaque) throws WBSAXException {
        // The only place that it is valid to have opaque values currently is 
        // in shard links which are never dissected.
        throw new IllegalStateException(
                "Dissection of opaque values not supported.");
    }

    /**
     * Private helper interface to allow us to simulate a closure.
     */ 
    private interface SegmentValueSerialiser {
        void serialise(int startIndex, int endIndex) throws WBSAXException;
    }
    
    private void serialise(int childLength, SegmentValueSerialiser serialiser)
            throws WBSAXException {
        int childEnd = childStart + childLength;
        int lastState = state;
        int start = -1;
        int end = -1;
        switch (state) {
            case IS_BEFORE:
                // If the child ends after the range starts...
                if (childEnd > rangeStart) {
                    // Then the child overlaps the start of the range.
                    start = rangeStart - childStart;
                    // If the child also overlaps the end of the range...
                    if (childEnd > rangeEnd) {
                        // Then the end point is in this child.
                        end = rangeEnd - childStart;
                        state = IS_AFTER;
                    } else {
                        // The end point is in a future child.
                        end = childLength;
                        state = IS_INSIDE;
                    }
                    serialiser.serialise(start, end);
                }
                // else, we are still in IN_BEFORE state.
                break;
            case IS_INSIDE:
                // If the child ends after the range ends...
                start = 0;
                if (childEnd > rangeEnd) {
                    // The end point is in this child.
                    // So collect the partial cost and we are finished.
                    end = rangeEnd - childStart;
                    state = IS_AFTER;
                } else {
                    // The endpoint is in a future child.
                    // So add the cost of this child and continue.
                    end = childLength;
                }
                serialiser.serialise(0, end);
                break;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(getStateName(lastState) + "->" + 
                    getStateName(state) + // " child=" + childIndex + 
                    " index=" + childStart /* + " cost=" + cost */);
        }
        childStart += childLength;
    }
    
    private String getStateName(int state) {
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
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/3	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/3	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
