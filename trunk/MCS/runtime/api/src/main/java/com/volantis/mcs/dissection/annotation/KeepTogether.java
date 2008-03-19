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

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.KeepTogetherAttributes;
import com.volantis.mcs.dissection.impl.Shard;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This represents a keep together element.
 * <p>
 * The first time that this node is visited it will prevent itself being split
 * if there is not enough available space for the whole element. On subsequent
 * occasions it behaves as normal. In order to do this properly it requires that
 * the cost of itself be checked before it is processed.
 * <p>
 * In future it may be possible to force a break before or after. If a break
 * is forced before this element then it is not necessary to
 */
public class KeepTogether
    extends ElementAnnotation {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(KeepTogether.class);

    private KeepTogetherAttributes attributes;

    /**
     * If true then a break should be forced before the keep together element.
     */
    private boolean forceBreakBefore;

    /**
     * If true then a break should be forced after the keep together element.
     */
    private boolean forceBreakAfter;

    public KeepTogether() {
        // Keep together must always check the cost as it has to backtrack.
        setMustCheckCost(true);
    }

    public void setForceBreakBefore(boolean forceBreakBefore) {
        this.forceBreakBefore = forceBreakBefore;
    }

    public boolean getForceBreakBefore() {
        return forceBreakBefore;
    }

    public void setForceBreakAfter(boolean forceBreakAfter) {
        this.forceBreakAfter = forceBreakAfter;
    }

    public boolean getForceBreakAfter() {
        return forceBreakAfter;
    }

    public int selectShardContents(Shard shard)
        throws DissectionException {

        // If we have been asked to force a break before this node and the
        // current shard is not empty then stop this node from being added.
        if (forceBreakBefore && !shard.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Forcing break before " + this);
            }
            return NODE_CANNOT_FIT;
        }

        // Attempt to populate as normal, if this node should force a break
        // after and this node is complete then return SHARD_COMPLETE.
        int result = super.selectShardContents(shard);
        if (forceBreakAfter && result == ADDED_NODE) {
            if (logger.isDebugEnabled()) {
                logger.debug("Forcing break after " + this);
            }
            // Make sure that no more nodes can be added to this shard.
            shard.setAvailableSpace(-1);
        }

        return result;
    }

    /**
     * This method will refuse to dissect unless the force flag is set as that
     * is the whole point of this element.
     */
    protected int dissectNode(Shard shard, boolean mustDissect)
        throws DissectionException {

        // If we are allowed to then refuse to dissect.
        if (!mustDissect) {
            return NODE_CANNOT_FIT;
        }

        // Dissect as for a normal element.
        return super.dissectNode(shard, mustDissect);
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

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
