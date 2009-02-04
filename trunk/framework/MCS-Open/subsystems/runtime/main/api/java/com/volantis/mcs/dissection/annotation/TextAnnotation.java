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

package com.volantis.mcs.dissection.annotation;

import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.dissection.impl.Shard;
import com.volantis.mcs.dissection.string.StringSegment;
import com.volantis.mcs.dissection.string.StringDissector;
import com.volantis.mcs.dissection.string.StringSegmenter;
import com.volantis.mcs.dissection.string.DefaultStringDissector;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This class is responsible for splitting a text node into multiple segments
 * if necessary.
 * <p>
 * This class essentially provides a bridge between the dissector and the
 * {@link StringSegmenter} class. The {@link StringSegmenter} is responsible
 * for dissecting the text into multiple segments with the help of a
 * {@link StringDissector}.
 * <p>
 * Just like other nodes each node within the dissectable content must belong
 * to a contiguous range of shards (possibly only one). Therefore the mapping
 * from a shard number to text segment index is extremely easy as it simply
 * involves subtracting the shard number from the first shard that this node
 * belongs to.
 * <p>
 * If this has to be split across multiple shards then there is one segment
 * per shard. These are managed by the {@link StringSegmenter}.
 */
public class TextAnnotation
    extends DissectableNodeAnnotation {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";


    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(TextAnnotation.class);

    /**
     * The string dissector we are using, this is currently hardcoded but
     * may not be in future.
     */
    private static StringDissector stringDissector =
            new DefaultStringDissector();
    //        new BodgyStringDissector(3, 6);

    // =========================================================================
    //   Constant Fields
    //     The fields in this section are all set during the annotation process
    //     and once set are not changed. This means that they can be accessed
    //     without worrying about synchronization.
    // =========================================================================

    private DissectableText text;

    // =========================================================================
    //   Selection Specific Fields
    //     The fields in this section are used solely by the selection process
    //     and must not be accessed during generation.
    // =========================================================================

    // =========================================================================
    //   Selection and Output Specific Fields
    //     The fields in this section are used by both the selection process
    //     and the output process and so could be accessed concurrently by
    //     selection and generation threads.
    // =========================================================================

    /**
     * The array of segments, this is grown dynamically as needed because it is
     * not known at the start how many are required.
     */
    private StringSegmenter segmenter;

    // =========================================================================
    //   End Of Fields
    // =========================================================================


    public TextAnnotation() {
        // todo: Do this for now in order to prevent the select variable being
        // todo: called but someday we will implement that and remove this.
        setMustCheckCost(true);

    }

    public void setText(DissectableText text) {
        if (this.text != null) {
            throw new IllegalStateException("Text already set");
        }
        this.text = text;
    }


    protected void addCost(Cost cost)
        throws DissectionException {
        cost.addTextCost(document, text);
    }

    /**
     * Nothing to do as a text node is a leaf node.
     */
    protected void addPartsToShard(Shard shard) {
        // Nothing to do.
    }

    /**
     * This method delegates the majority of the work to the
     * {@link StringSegmenter#segment} method.
     * <p>
     * This method has to extract the appropriate information from the Shard
     * and pass it to the {@link StringSegmenter#segment} method in the
     * appropriate form.
     */
    protected int dissectNode(Shard shard, boolean mustDissect)
            throws DissectionException {

        // Lazily initialise the segmenter from the text / string when the
        // client code tries to dissect the text into the first shard.
        if (segmenter == null) {
            DissectableString string = document.getDissectableString(text);
            segmenter = new StringSegmenter(stringDissector, string);
        }

        // Handle the fact that when we first get called we haven't been
        // placed in a shard at all. Seems ugly that we have to do this...
        int segmentIndex = 0;
        if (inAnyShard()) {
            segmentIndex = getSegmentIndex(shard);
        }

        // Try to dissect the string to obtain a segment for this shard.
        int returnCode = segmenter.segment(shard.getSharedContentUsages(),
                segmentIndex, shard.getAvailableSpace(), mustDissect);
        // Translate the return code into something that makes sense as
        // defined by DissectableNodeAnnotation.selectShardContents().
        switch (returnCode) {
            case StringDissector.DISSECTED_END:
                // Everything remaining in the string fitted in, so return...
                return ADDED_NODE;
            case StringDissector.DISSECTED_HAS_NEXT:
                // Only part of the string fitted in, so return...
                return SHARD_COMPLETE;
            case StringDissector.FAILED_TO_DISSECT:
                // Nothing could fit in, so return...
                return NODE_CANNOT_FIT;
            default:
                throw new IllegalStateException("Invalid segmenter return " +
                        returnCode);
        }
    }

    /**
     * This method does not do anything at the moment.
     * @param shard
     * @return undefined
     * @throws DissectionException
     * @throws UnsupportedOperationException always
     */
    protected int selectVariableShardContentsImpl(Shard shard)
        throws DissectionException {

        throw new UnsupportedOperationException("Cannot do this yet");
    }

    /**
     * Get the text segment index for the specified shard.
     * <p>
     * This will fail ungraciously if this annotation has not been added to
     * any shard at this point.
     *
     * @param shard The shard to which the returned StringSegment belongs.
     * @return The StringSegment index selected for this shard.
     */
    private int getSegmentIndex(Shard shard) {
        if (!inAnyShard()) {
            throw new IllegalStateException(
                    "Attempt to calculate text segment index before " +
                    "text annotation added to first shard");
        }

        // The segment array is indexed by the offset of the shard's index
        // from the first shard that this text node is in.
        int shardIndex = shard.getIndex();
            return shardIndex - getFirstShard();
    }

    /**
     * Get the text segment for the specified shard.
     *
     * @param shard The shard to which the returned StringSegment belongs.
     * @return The StringSegment index selected for this shard.
     */
    public StringSegment getSegment(Shard shard) {
        return segmenter.getSegment(getSegmentIndex(shard));
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

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 24-Jun-03	521/2	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/2	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 12-Jun-03	363/3	pduffin	VBM:2003060302 Added some comments to aid in implementation of string dissector

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
