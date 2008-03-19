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

import com.volantis.mcs.dissection.string.StringDissector;
import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.string.StringSegment;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.DissectionException;

/**
 * A testing implementation of 
 * {@link com.volantis.mcs.dissection.string.StringDissector} that dissects 
 * strings into three equal segments (as far as is practicable).
 * <p>
 * This was originally used to manually integration test the basic mechanics 
 * of string dissection before any other implementations of 
 * {@link com.volantis.mcs.dissection.string.StringDissector} were available. 
 * Currently it is used for unit testing as it has very predictable behaviour.
 * <p>
 * If you wish to reuse it for integration testing, you will need to move it 
 * out of the testsuite jar and back into the main jars so that it is visible 
 * to the main dissector classes. In time, when the dissector is more "plug 
 * and play", this restriction will presumably be lifted. 
 */ 
public class BodgyStringDissector implements StringDissector {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private int segments;
    private int minimumLength;
    
    public BodgyStringDissector(int segments, int minimumLength) {
        this.segments = segments;
        this.minimumLength = minimumLength;
    }

    public int dissect(DissectableString string,
            SharedContentUsages sharedContent, StringSegment segment,
            int availableSpace, boolean mustDissect) 
            throws DissectionException {

        int length = string.getLength();
        
        // Return if this string is smaller than we like dissecting.
        if (length < minimumLength) {
            return FAILED_TO_DISSECT;
        }

        // Calculate the segment we'd like to use.
        int segmentLength = length / segments;
        int currentSegment = segment.getStart() / segmentLength;
        int maxSegments = segments - 1;
        int returnCode;
        if (currentSegment > maxSegments) {
            throw new IllegalStateException("Invalid segment " + 
                    currentSegment);
        } else if (currentSegment == maxSegments) {
            // Use up all remaining characters; the settings in segment
            // should already be set for this.
            returnCode = DISSECTED_END;
        } else {
            // Use up a third of the total length.
            segment.setEnd(segment.getStart() + segmentLength);
            returnCode = DISSECTED_HAS_NEXT;
        }

        // Reject the proposed segment if the shard don't have space for it.
        int consumed = string.getRangeCost(segment.getStart(), 
                segment.getEnd());
        if (availableSpace < consumed) {
            returnCode = FAILED_TO_DISSECT;
        }
        
        return returnCode;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	774/2	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/2	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/3	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	365/3	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
