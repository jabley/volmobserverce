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

import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * The string segmenter dissects a string into segments iteratively, storing
 * the results into an array of segments.
 * <p>
 * This class manages the segment state handling for the string dissector, 
 * which makes the dissector simpler and easier to understand.  
 */ 
public class StringSegmenter {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";


    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(StringSegmenter.class);

    /**
     * The number of cells in the segment array when it is initially created.
     */
    private static final int INITIAL_SEGMENT_CELLS = 4;

    /**
     * The number of cells to add to the segment array when it is expanded.
     */
    private static final int EXTRA_SEGMENT_CELLS = 4;

    /**
     * The contents to dissect.
     */
    private DissectableString contents;

    /**
     * The thing to dissect the contents with.
     */ 
    private StringDissector stringDissector;

    /**
     * The array of segments, that have been dissected from the contents.
     * <p> 
     * This is grown dynamically as needed because it is not known at the 
     * start how many are required.
     * <p>
     * We could try and approximate the number of segments we need by dividing
     * the length by the available space. The problem with this is that if we 
     * are right at the end of the shard then the available space for this 
     * segment will be very small but the space for the next segment may be 
     * much larger. This would lead to us allocating far too large an array.
     */
    private StringSegment[] segments;
    
    public StringSegmenter(StringDissector stringDissector, DissectableString contents) {
        this.stringDissector = stringDissector;
        this.contents = contents;
    }

    // this will be called with index >= the last index passed in
    // index == last index if mustDissect = true :-).
    public int segment(SharedContentUsages sharedContent, int segmentIndex,
                       int availableSpace, boolean mustDissect) 
            throws DissectionException {

        if (segments == null || segmentIndex >= segments.length) {
            expandSegmentArray();
        }
        
        StringSegment segment = new StringSegment();
        // segments after the first one continue where that one left off
        if (segmentIndex > 0) {
            segment.setStart(segments[segmentIndex - 1].getEnd());
        }
        // else it's zero...
        
        // segments always try and consume the entire text available???
        segment.setEnd(contents.getLength());
        
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to dissect string into segment " + segment);
        }
        int result = stringDissector.dissect(contents, sharedContent, segment, 
                availableSpace, mustDissect);
        String qualifier = "";
        switch (result) {
            case StringDissector.DISSECTED_END:
                qualifier = "last ";
                // Fall through.
            case StringDissector.DISSECTED_HAS_NEXT:
                if (logger.isDebugEnabled()) {
                    logger.debug("Dissected string into " + qualifier + 
                            " segment " + segment);
                }
                // Save the segment that we dissected.
                segments[segmentIndex] = segment;
                break;
            case StringDissector.FAILED_TO_DISSECT:
                // We couldn't dissect any more from the string.
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to dissect string into " + segment);
                }
                break;
            default:
                throw new IllegalStateException(
                        "Invalid string dissector return " + result);
        }
        return result;
    }
    
    public StringSegment getSegment(int index) {
        return segments[index];
    }
    
    /**
     * Expand the segment array.
     */
    private void expandSegmentArray() {
        if (segments == null) {
            segments = new StringSegment[INITIAL_SEGMENT_CELLS];
        } else {
            int newSize = segments.length + EXTRA_SEGMENT_CELLS;
            StringSegment[] newSegments = new StringSegment[newSize];
            System.arraycopy(segments, 0, newSegments, 0,
                             segments.length);
            segments = newSegments;
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

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 24-Jun-03	365/4	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
