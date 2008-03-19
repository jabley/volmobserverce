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

/**
 * Defines the methods and behaviour of objects that can dissect a
 * DissectableString into multiple parts.
 * <p>
 * Objects that implement this must not maintain any string specific state
 * within this object. They must also be thread-safe as multiple threads may be
 * calling the same methods on the same objects at the same time.
 * <p>
 * @see com.volantis.mcs.dissection.string.StringSegment
 * @todo might be worth abstracting SharedContentUsages and availableSpace into 
 * an AvailableSpace interface ala Accumulator (decrements rather than adds).
 */
public interface StringDissector {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Dissected, and there are more segments in the string.
     */ 
    public static int DISSECTED_HAS_NEXT = 1;
    
    /**
     * Dissected, and that was the last segment in the string.
     */ 
    public static int DISSECTED_END = 2;
    
    /**
     * The string could not be dissected into the available space. 
     */ 
    public static int FAILED_TO_DISSECT = 3;
    
    /**
     * Dissect the specified string.
     * <p>
     * If the force parameter is set to false then this method has the option
     * to not consume any characters. If the force parameter is set to true
     * then this method must consume characters at all costs. If it cannot
     * such as if the next part of the string cannot be split and exceeds the
     * available space then it should throw an exception.
     * <p>
     * The <code>segment</code> parameter is used as both an input and an
     * output parameter.
     * <p>
     * On input the start position is set to the end position from the previous
     * call, or 0 if it is the first call. The end position is set to just after
     * the last character that could be included.
     * <p>
     * On output the start position should be set to the start of the dissected
     * string  and the end position should be set to the end of the dissected
     * string.
     * <p>
     * The segments produced by this method from a single string must not
     * overlap but they do not have to completely cover the input string. e.g.
     * if the string is split at white spaces then those white spaces do not
     * have to be part of the text segment. Instead they could be seen as
     * segment separators.
     * 
     * @param string The string to dissect.
     * @param sharedContent This holds information about shared content that has
     * already been either referenced from the fixed content, or the current
     * shard. This will be null if the document does not contain any shared
     * content.
     * @param segment The text segment that this dissector must produce.
     * @param availableSpace The amount of space available for the dissected
     * string.
     * @param mustDissect If this is set to true then it means that this method
     * must dissect the string, if it does not then dissection will fail.
     * @return If the string was successfully dissected, and there is more 
     * content left in the string to be consumed, then this method must return 
     * {@link #DISSECTED_HAS_NEXT}.
     * <p>
     * If the string was successfully dissected, and the entire contents 
     * have now been consumed, then this method must return 
     * {@link #DISSECTED_END}.
     * <p>
     * If the string could not be dissected into the available space, then 
     * this method must return {@link #FAILED_TO_DISSECT}.
     * @throws com.volantis.mcs.dissection.DissectionException If the string
     * could not be dissected for whatever reason.
     */
    public int dissect(DissectableString string,
                       SharedContentUsages sharedContent,
                       StringSegment segment,
                       int availableSpace,
                       boolean mustDissect)
        throws DissectionException;

}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 24-Jun-03	521/3	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/2	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 12-Jun-03	363/2	pduffin	VBM:2003060302 Added some comments to aid in implementation of string dissector

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
