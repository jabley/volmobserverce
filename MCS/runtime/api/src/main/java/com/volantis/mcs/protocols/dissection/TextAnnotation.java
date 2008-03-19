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
 * $Header: /src/voyager/com/volantis/mcs/protocols/dissection/TextAnnotation.java,v 1.8 2003/01/21 17:50:31 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 03-May-02    Paul            VBM:2002042203 - Improved the dissection of
 *                              text nodes by ignoring unnecessary white space.
 * 23-May-02    Paul            VBM:2002042202 - Stopped writing out NUL
 *                              characters and removed the divide hint code as
 *                              that is all handled in NodeAnnotation now.
 * 05-Jun-02    Adrian          VBM:2002021103 - Moved member var refusedShard
 *                              to NodeAnnotation superclass.  Updated method
 *                              markShardNodesImpl to refuse to split the text
 *                              content on first attempt to fit in a shard if
 *                              the node is a child of a KEEPTOGETHER_ELEMENT.
 * 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
 *                              keepTogether.
 * 13-Nov-02    Phil W-S        VBM:2002110507 - Update whitespace handling
 *                              in calculateContentsSize.
 * 21-Jan-03    Adrian          VBM:2003011605 - undid changes made in 
 *                              2002021103 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.mcs.utilities.WhitespaceUtilities;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.StringWriter;

/**
 * This class is used to annotate text nodes in the dom tree which is being
 * dissected.
 */
public class TextAnnotation
        extends NodeAnnotation {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TextAnnotation.class);


    private static final int MINIMUM_SEGMENT_LENGTH = 16;
    private static final int MAXIMUM_SEARCH_DISTANCE = 32;
    private static final int EXTRA_SEGMENTS = 4;

    private Text text;

    private ReusableStringBuffer contents;

    /**
     * An array of the start points of the text segments for each shard.
     */
    private int[] segmentStarts;

    /**
     * An array of the end points of the text segments for each shard.
     */
    private int[] segmentEnds;

    /**
     * The index of the left most significant character.
     */
    private int left;

    /**
     * The index of the right most significant character.
     */
    private int right;

    /**
     * A flag to indicate whether this node has previously refused to fit in
     * a shard.
     */
    private boolean refusedShard;


    public TextAnnotation() {
    }

    public void initialise() {

        contents = new ReusableStringBuffer();
        if (!text.isEncoded()) {
            // If the text is not pre-encoded, then we should encode it
            // before dissection to ensure we know the actual size of it.
            // Hopefully the dissector will not try and break in the middle
            // of any character entities...
            StringWriter sw = new StringWriter();
            try {
                protocol.getCharacterEncoder().encode(text.getContents(), 0,
                        text.getLength(), sw);

            } catch (IOException e) {
                // Should never happen.
                throw new RuntimeException(e);
            }
            contents.append(sw.toString());
        } else {
            // Pre-encoded, so just stick it straight in.
            contents.append(text.getContents(), 0, text.getLength());
        }

    }

    /**
     * Implement this method to return the Text node.
     */
    protected Node getNode() {
        return text;
    }

    /**
     * Set the value of the text property.
     *
     * @param text The new value of the text property.
     */
    public void setText(Text text) {
        this.text = text;
    }

    /**
     * Get the value of the text property.
     *
     * @return The value of the text property.
     */
    public Text getText() {
        return text;
    }

    protected int calculateContentsSize() {
        int length = contents.length();

        // Find the first non whitespace character. If the contents are completely
        // whitespace then this node can be collapsed to a single whitespace
        // character. If the content is empty, the node can be ignored.
        left = WhitespaceUtilities.getFirstNonWhitespaceIndex(contents);

        if (left == length) {
            right = left;

            if (left > 0) {
                // There is some whitespace content so preserve a single whitespace
                left--;
                return 1;
            } else {
                // No content at all
                return 0;
            }
        }

        // Preserve a single leading whitespace if necessary
        if (left > 0) {
            left--;

            // Make sure the whitespace is an appropriate one
            contents.setCharAt(left, ' ');
        }

        right = WhitespaceUtilities.getLastNonWhitespaceIndex(contents);

        // Make sure the non-whitespace character is accounted for in the indices
        if (right != length) {
            right++;
        }

        // Preserve a single trailing whitespace if necessary
        if (right < length) {
            // Make sure the whitespace is an appropriate one
            contents.setCharAt(right, ' ');

            right++;
        }

        return right - left;
    }

    protected int calculateFixedContentsSize() {
        return getContentsSize();
    }

    protected int calculateOverheadSize() {
        // The maximum overhead is 8, 4 each for an ellipsis and space at the
        // beginning and end of the segment.
        return 8;
    }

    /**
     * Return true if the specified shard is the first shard that contains (or
     * may contain) this node.
     */
    private boolean isFirstShard(int shardNumber) {
        return (firstShard == -1 || firstShard == shardNumber);
    }

    /**
     * Return true if the specified shard is the last shard that contains
     * this node.
     */
    private boolean isLastShard(int shardNumber) {
        return (completed && shardNumber == lastShard);
    }

    protected int markShardNodesImpl(int shardNumber, int limit)
            throws ProtocolException {

        if (logger.isDebugEnabled()) {
            logger.debug("Available space is " + limit + " content length is "
                    + getContentsSize());
        }

        // If the amount of space is too small then this node cannot fit.
        if (limit < MINIMUM_SEGMENT_LENGTH) {
            if (logger.isDebugEnabled()) {
                logger.debug("Available space " + limit + " for " + this
                        + " is less than minimum segment length of "
                        + MINIMUM_SEGMENT_LENGTH);
            }

            // If we have previously refused the shard then we should ignore the
            // minimum segment length.
            if (refusedShard) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ignoring minimum segment length of "
                            + MINIMUM_SEGMENT_LENGTH
                            + " as it is less than the limit of " + limit);

                }

            } else {
                // Remember that we have refused this shard.
                refusedShard = true;

                return NODE_CANNOT_FIT;
            }
        }

        // Make sure that the array of segment ends and starts are large enough,
        // we always need one more than the requested shard as we do not yet know
        // whether this is the last segment.
        if (segmentEnds == null) {
            // We could try and approximate the number of segments we need by
            // dividing the length by the available space. The problem with this
            // is that if we are right at the end of the shard then the available
            // space for this segment will be very small but the space for the
            // next segment may be much larger. This would lead to us allocating
            // far too large an array.
            segmentEnds = new int[shardNumber + EXTRA_SEGMENTS];
            segmentStarts = new int[shardNumber + EXTRA_SEGMENTS];
        } else if (segmentEnds.length <= shardNumber + 1) {
            int[] newSegmentEnds = new int[shardNumber + EXTRA_SEGMENTS];
            System.arraycopy(segmentEnds, 0, newSegmentEnds, 0,
                    segmentEnds.length);
            segmentEnds = newSegmentEnds;

            int[] newSegmentStarts = new int[shardNumber + EXTRA_SEGMENTS];
            System.arraycopy(segmentStarts, 0, newSegmentStarts, 0,
                    segmentStarts.length);
            segmentStarts = newSegmentStarts;
        }

        // Calculate the start and end positions of the segment for the
        // current shard.
        int start;
        int end;
        // The start of this segment was already calculated during the
        // processing of the previous segment, if this is the first segment
        // then we start at the left most significant character.
        if (isFirstShard(shardNumber)) {
            start = left;
        } else {
            start = segmentStarts[shardNumber];
        }

        // end = DissectableString.getCharacterIndex(start, limit)
        end = start + limit;
        // length = end - start
        int length = limit;

        // If the maximum end point is before the end of the contents then
        // we need to split the contents.
        char[] chars = contents.getChars();

        if (end < right) {

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
            //

            // If this is not the first shard then we need to leave space for an
            // ellipsis followed by a space.
            if (!isFirstShard(shardNumber)) {
                // end -= ....getStringCost("... ");
                // segment.setPrefix("... ");
                end -= 4;
            }

            // We must leave space for a space followed by an ellipsis.
            // end -= ....getStringCost(" ...");
            // segment.setPrefix(" ...");
            end -= 4;

            // Make sure that no more than 2/3 of the contents are allocated
            // to this shard.
            int remaining = right - start;
            int maxLength = remaining * 2 / 3;
            length = end - start;
            if (length > maxLength) {
                length = maxLength;
                end = start + maxLength;
            }

            // Calculate the point at which we should stop searching for a good
            // place to stop. It should be over half way through the contents
            // allocated to this shard but no more than MAXIMUM_SEARCH_DISTANCE.
            int searchDistance = MAXIMUM_SEARCH_DISTANCE;
            if (searchDistance > (length / 2)) {
                searchDistance = length / 2;
            }

            int searchEnd = end - searchDistance;

            // Look back through the buffer from the end for a good place to
            // split the contents. Currently we look for the first white space
            // character in the firts sequence of white space before the maximum end
            // point. If we cannot find a white space character within the maximum
            // search length then we simply break it at the maximum point.
            boolean foundWhiteSpace = false;
            for (int i = end - 1; i > searchEnd; i -= 1) {
                char c = chars[i];
                if (Character.isWhitespace(c)) {
                    end = i;
                    foundWhiteSpace = true;
                } else if (foundWhiteSpace) {
                    // We have already found a white space character so stop.
                    break;
                }
            }
        } else {
            end = right;
        }

        segmentEnds[shardNumber] = end;

        // Calculate the actual length we need for this node.
        length = end - start;

        // The beginning of the next segment is the first non white space
        // character after the end of the previous segment.
        int startNextSegment = right;
        for (int i = end; i < right; i += 1) {
            char c = chars[i];

            if (!Character.isWhitespace(c)) {
                startNextSegment = i;
                segmentStarts[shardNumber + 1] = startNextSegment;
                break;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Contents from " + start + " to " + end + " of "
                    + this + " have been added to shard "
                    + shardNumber);
            logger.debug("Next segment starts at " + startNextSegment
                    + " and end of content is " + right);
        }

        // If there is only whitespace after the current segment then this is the
        // last segment.
        if (startNextSegment == right) {
            // ADDED_NODE
            return length;
        } else {
            return SHARD_COMPLETE;
        }
    }

    public void generateContents(ReusableStringBuffer buffer) {
        buffer.append(contents.getChars(), left, right - left);
    }

    public void generateFixedContents(ReusableStringBuffer buffer) {
        buffer.append(contents.getChars(), left, right - left);
    }

    public void generateDissectedContents(ReusableStringBuffer buffer) {
        buffer.append(contents.getChars(), left, right - left);
    }

    public boolean generateShardContentsImpl(
            ReusableStringBuffer buffer,
            int shardNumber,
            boolean all) {

        boolean done = false;

        if (segmentEnds == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding contents of " + this
                        + " to shard " + shardNumber);
            }

            buffer.append(contents.getChars(), left, right - left);
        } else {
            int start = segmentStarts[shardNumber];
            int end = segmentEnds[shardNumber];

            // If this is not the first shard then append an ellipsis.
            if (!isFirstShard(shardNumber)) {
                buffer.append("... ");
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Adding contents from " + start + " to " + end
                        + " of " + this + " to shard " + shardNumber);
            }
            buffer.append(contents.getChars(), start, end - start);

            // If this is not the last shard then append an ellipsis and remember
            // that this shard must have finished.
            if (!isLastShard(shardNumber)) {
                buffer.append(" ...");
                done = true;
            }
        }

        return done;
    }

    /**
     * Implement the abstract generateDebugOutput method.
     */
    protected void generateDebugOutput(
            ReusableStringBuffer buffer,
            String indent) {
        buffer.append(indent).append(contents).append("\n");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Jun-03	363/1	pduffin	VBM:2003060302 Added some comments to aid in implementation of string dissector

 ===========================================================================
*/
