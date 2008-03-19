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

import com.volantis.mcs.dissection.DissectionException;

/**
 * This rule can be used to wrap content that forms a separator between two
 * links.
 * <p>
 * It returns true if the following criteria are satisfied:
 * <ol>
 *   <li>No unused separator.
 *   <li>Preceded by at least one link.
 *   <li>Followed by at least one link.
 * </ol>
 */
public class SeparatorRule
    implements ShardLinkContentRule {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public SeparatorRule() {
    }

    public boolean checkRule(ShardLinkContentRuleContext context)
        throws DissectionException {

        /**
         * Always fail if there is an unused separator.
         */
        if (context.isSeparatorUnused()) {
            return false;
        }

        // Get the position, if it is before the first or after the last link
        // then fail.
        int position = context.getPosition();
        if (position == ShardLinkContentRuleContext.BEFORE_FIRST_LINK
            || position == ShardLinkContentRuleContext.AFTER_LAST_LINK) {
            return false;
        }

        // Search back from the current position to see whether there is a
        // preceding link.
        boolean found = false;
        for (int i = position; i >= 0; i -= 1) {
            if (context.getDetails(i) != null) {
                found = true;
                break;
            }
        }

        // Fail if we could not find a previous link.
        if (!found) {
            return false;
        }

        // Search forwards from after the current position to see whether there
        // is a following link. Don't include the position as it is the previous
        // link.
        int count = context.getCount();
        found = false;
        for (int i = position + 1; i < count; i += 1) {
            if (context.getDetails(i) != null) {
                found = true;
                break;
            }
        }

        // If we found one then all the criteria have matched so we should
        // process our body, otherwise we need to skip it.
        return found;
    }
    
    // Javadoc inherited.
    public String toString() {
        return "[SeparatorRule]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
