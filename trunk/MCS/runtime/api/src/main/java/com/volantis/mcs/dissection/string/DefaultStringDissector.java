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

package com.volantis.mcs.dissection.string;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * A default implementation of {@link StringDissector}.
 * <p>
 * Currently this is only able to dissect inline strings. Dissecting string
 * references would require the implementation of breakpoint handling
 * throughout which we don't currently need. A more complex implementation
 * of optimisation may require dissection of string references in future.
 * <p>
 * Note: this was constructed by cut and pasting from the old "non-accurate"
 * dissector, and removing the manual whitespace support. As such, it
 * hopefully works the way we "expect" (i.e. how it used to work), but I'm
 * not quite sure it is "correct". In particular, some of the fiddly internal
 * indexing seemed a bit weird to me when I modified it to work in this new
 * class, so I wouldn't be surprised if a few funnies turn up later.
 * Unfortunately I didn't have time to validate it completely...
 *
 * @todo implement Unicode surrogate handling rather than bodgy char casts.
 */
public class DefaultStringDissector
    implements StringDissector {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DefaultStringDissector.class);

    private static final int MINIMUM_SEGMENT_LENGTH = 16;

    private static final int MAXIMUM_SEARCH_DISTANCE = 32;

    // Inherit Javadoc.
    public int dissect(DissectableString string,
            SharedContentUsages sharedContent, StringSegment segment,
            int availableSpace, boolean mustDissect)
            throws DissectionException {

        // If the amount of space is too small then this node cannot fit.
        if (availableSpace < MINIMUM_SEGMENT_LENGTH) {
            if (logger.isDebugEnabled()) {
                logger.debug("Available space " + availableSpace + " for " +
                        this + " is less than minimum segment length of " +
                        MINIMUM_SEGMENT_LENGTH);
            }

            // If we must dissect then we should ignore
            // the minimum segment length.
            if (mustDissect) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ignoring minimum segment length of " +
                            MINIMUM_SEGMENT_LENGTH +
                            " as it is less than the limit of " +
                            availableSpace);
                }
            } else {
                return FAILED_TO_DISSECT;
            }
        }

        // Calculate the start and end positions of the segment for the
        // current shard.

        int start = segment.getStart();
        // If this is the not first segment then we strip off any leading space
        // we left from the last segment, otherwise we start at the left most
        // significant character (i.e. include any leading space?).
        if (start > 0) {
            if (Character.isWhitespace((char) string.charAt(start))) {
                start = string.getCharacterIndex(start, 1);
            }
        }

        // If this is not the first segment then we need to leave space
        // for an ellipsis followed by a space.
        if (segment.getStart() > 0) {
            segment.setPrefix("... ");
            // todo: implement string costing for elipsis.
            // end -= ....getStringCost("... ");
            availableSpace -= 4;
        }

        // NOTE: end index is exclusive.
        int end = string.getCharacterIndex(start, availableSpace);

        // If the maximum end point is before the end of the contents then
        // we need to split the contents.
        if (end < segment.getEnd()) {

            // We need to try and split the contents up evenly across the shards,
            // otherwise we could end up with the last shard having only a few
            // characters in it which would look strange. It is not possible to
            // do this perfectly as we do not know how many shards there are going
            // to be.
            //
            // We can make a reasonable attempt if we assume that the next shard
            // will have at least as much available space as this shard has. This
            // means that we can adjust how much we should consume based on how
            // much will be left for the next shard.
            //
            // The only way that the assumption can be false is if this is the
            // only leaf node in the dissectable sub tree and this is the first
            // shard. In this case the second shard could have less space than
            // this shard as its previous navigation link could be larger than
            // this nodes next navigation link.
            //
            // We can cope with this by biasing the split towards the current
            // shard.

            // We must leave space for a space followed by an ellipsis.
            segment.setSuffix(" ...");
            // todo: implement string costing for elipsis.
            // end -= ....getStringCost(" ...");
            end -= 4;

            // Make sure that no more than 2/3 of the contents are allocated
            // to this shard.
            int remaining = segment.getEnd() - start;
            int maxLength = remaining * 2 / 3;
            int length = end - start;
            if (length > maxLength) {
                length = maxLength;
                end = start + maxLength;
            }

            // Calculate the point at which we should stop searching for a
            // good place to stop. It should be over half way through the
            // contents allocated to this shard but no more than
            // MAXIMUM_SEARCH_DISTANCE.
            int searchDistance = MAXIMUM_SEARCH_DISTANCE;
            if (searchDistance > (length / 2)) {
                searchDistance = length / 2;
            }

            int searchEnd = end - searchDistance;

            // Look back through the buffer from the end for a good place to
            // split the contents. Currently we look for the first white space
            // character in the first sequence of white space before the
            // maximum end point. If we cannot find a white space character
            // within the maximum search length then we simply break it at the
            // maximum point.
            boolean foundWhiteSpace = false;
            for (int i = end - 1; i > searchEnd; i -= 1) {
                char c = (char) string.charAt(i);
                if (Character.isWhitespace(c)) {
                    end = i;
                    foundWhiteSpace = true;
                } else if (foundWhiteSpace) {
                    // We have already found a white space character so stop.
                    break;
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Dissected " + this +
                        " into partial segment from " + start + " to " + end);
            }

            segment.setStart(start);
            segment.setEnd(end);

            return DISSECTED_HAS_NEXT;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Dissected " + this +
                        " into complete segment " + segment);
            }

            return DISSECTED_END;
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

 24-Jun-03	521/3	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
