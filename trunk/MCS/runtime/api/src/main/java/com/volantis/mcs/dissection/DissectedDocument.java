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

package com.volantis.mcs.dissection;

/**
 * Defines those methods that must be supported by dissected documents.
 */
public interface DissectedDocument {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Get the total cost of the document.
     * <p>
     * The total cost of the document as returned by this must be greater than
     * or equal to the actual cost of the outputted document. It will not be
     * equal if some of the costs cannot be calculated until it is written out.
     * In this case a worst case cost is used that must be greater than or equal
     * to the actual cost.
     * @return The total cost of the document.
     */
    public int getTotalCost ();

    /**
     * Return the number of dissectable areas that there are in this dissected
     * document.
     * @return The number of dissectable areas that there are in this dissected
     * document.
     */
    public int getDissectableAreaCount();

    /**
     * Get the identity of the dissectable area identified by its index within
     * this dissected document.
     * @param index The index of the dissectable area within this document.
     * @return The identity of the dissectable area.
     */
    public DissectableAreaIdentity getDissectableAreaIdentity(int index);

    /**
     * Create an instance of {@link RequestedShards} that can be used to select
     * a shard for each dissectable area in this document.
     * @return A new {@link RequestedShards} instance.
     */
    public RequestedShards createRequestedShards();

    //public AvailableShards createAvailableShards();

    public ShardIterator getShardIterator(DissectionContext dissectionContext,
                                          int dissectableAreaIndex);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
