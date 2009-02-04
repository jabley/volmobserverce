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

package com.volantis.mcs.dissection.links.rules;

import com.volantis.mcs.dissection.links.ShardLinkDetails;

/**
 * This contains the contextual information that is available to
 * {@link com.volantis.mcs.dissection.links.rules.ShardLinkContentRule}.
 * <p>
 * This contains the following information.
 * <ul>
 *   <li>The details of all the shard links within the containing shard link
 *       group that are being used in the order that the shard links elements
 *       where defined within the group.
 * </ul>
 */
public interface ShardLinkContentRuleContext {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Constant that indicates that the rule is being evaluated before the
     * first link.
     */
    public static final int BEFORE_FIRST_LINK = -1;

    /**
     * Constant that indicates that the rule is being evaluated after the
     * last link.
     */
    public static final int AFTER_LAST_LINK = Integer.MAX_VALUE;

    /**
     * Get the number of shard links that are in the group being processed.
     * @return The number of shard links that in the group being processed.
     */
    public int getCount();

    /**
     * Get the shard link details at the specified index.
     * <p>
     * If the shard link at the specified index is not needed then this will
     * return null.
     *
     * @param index The index of the shard link within the list in the group.
     * @return The {@link com.volantis.mcs.dissection.links.ShardLinkDetails} for the link, or null if the link
     * is not needed.
     */
    public ShardLinkDetails getDetails(int index);

    /**
     * Get the position of the rule relative to the shard links.
     * <p>
     * This methods has the following behaviour:
     * <ul>
     *   <li>If the rule is before all of the shard links then it returns
     *       {@link #BEFORE_FIRST_LINK}.
     *   <li>If the rule is after all of the shard links then it returns
     *       {@link #AFTER_LAST_LINK}.
     *   <li>Otherwise it returns the position of the previous shard link.
     * </ul>
     * @return The position of the rule.
     */
    public int getPosition();

    /**
     * Check to see whether the rule is preceded by a separator that is
     * unused.
     * <p>
     * This returns true from the moment that {@link #addedSeparator} is called
     * until the moment that the {@link #usedSeparator} method is called.
     */
    public boolean isSeparatorUnused();

    /**
     * Called by the {@link com.volantis.mcs.dissection.links.rules.SeparatorRule} to indicate that it has returned
     * true and thus added a separator.
     */
    public void addedSeparator();

    /**
     * Called when a link is added and thus uses any preceding unused separator.
     */
    public void usedSeparator();
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
