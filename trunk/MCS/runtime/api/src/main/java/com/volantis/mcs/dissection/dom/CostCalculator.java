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

package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.links.ShardLinkDetails;

/**
 * Contains methods to calculate the costs and overheads associated with various
 * parts of the document.
 */
public interface CostCalculator {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Calculate the overhead of the document and add it to the specified
     * accumulator.
     * <p>
     * e.g. if the output format was textual XML then the document overhead
     * would be the DTD specification and the xml text declaration.
     * @param accumulator The object into which the calculated costs should be
     * added.
     */
    void addDocumentOverhead(Accumulator accumulator)
        throws DissectionException;

    /**
     * Calculate the overhead of the element and add it to the specified
     * accumulator.
     * <p>
     * e.g. if the output format was textual XML then the open tag and close
     * tag constitute the element overhead.
     * @param element The element.
     * @param accumulator The object into which the calculated costs should be
     * added.
     */
    void addElementOverhead(DissectableElement element,
                            Accumulator accumulator)
        throws DissectionException;

    /**
     * Calculate the cost of the shard link element and add it to the
     * specified accumulator.
     * <p>
     * The cost of the shared link element includes all of its contained
     * elements and text and also the result of any substitutions done on its
     * body.
     *
     * @param element The element.
     * @param accumulator The object into which the calculated costs should be
     * added.
     * @throws com.volantis.mcs.dissection.DissectionException
     */
    void addShardLinkCost(DissectableElement element,
                          Accumulator accumulator,
                          ShardLinkDetails details)
        throws DissectionException;

    /**
     * Calculate the cost of the text node and add it to the specified
     * accumulator.
     * @param text The text.
     * @param accumulator The object into which the calculated cost should be
     * added.
     */
    void addTextCost(DissectableText text, Accumulator accumulator)
        throws DissectionException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
