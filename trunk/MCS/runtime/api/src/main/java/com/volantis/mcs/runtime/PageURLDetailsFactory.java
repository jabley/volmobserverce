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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.mcs.integration.PageURLDetails;
import com.volantis.mcs.integration.PageURLType;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for providing PageURLDetails objects. A factory is used to avoid
 * re-create the same kinds of PageURLDetails over and over again. If/when
 * PageURLDetails ever become significantly more complicated then this
 * approach may no longer be feasible.
 */
public final class PageURLDetailsFactory {

    /**
     * Since there is a limited number of different kinds of PageURLDetails
     * objects and these do not change we maintain a List of them
     * to avoid having to recreate the objects over and over again.
     *
     * @todo Change the list into a Map and possibly also 
     */
    private static final List detailsList = new ArrayList();

    /**
     * The private default Constructor preventing instantiation of this
     * class.
     */
    private PageURLDetailsFactory() {
    }

    /**
     * Create a PageURLDetails configured with the given pageURLType.
     * This factory method will only create a new PageURLDetails is none
     * already exist with the specified configuration.
     * @param pageURLType the PageURLType property for the requested
     * PageURLDetails.
     * @return the requested PageURLDetails.
     */
    public static synchronized PageURLDetails
            createPageURLDetails(final PageURLType pageURLType) {
        PageURLDetails details = getDetails(pageURLType);
        if (details == null) {
            details = new PageURLDetails() {
                public PageURLType getPageURLType() {
                    return pageURLType;
                }
            };
            detailsList.add(details);
        }

        return details;
    }

    /**
     * Search the List of existing PageURLDetails. If a PageDetails exists
     * with the specified PageURLType then return it. Otherwise return null.
     * @param pageURLType the PageTypeURL with which to identify the
     * PageDetails.
     * @return the existing PageURLDetails or null if none was found.
     */
    private static PageURLDetails getDetails(PageURLType pageURLType) {
        PageURLDetails details = null;
        for (int i = 0; i < detailsList.size() && details == null; i++) {
            PageURLDetails existingDetails = (PageURLDetails) detailsList.get(i);
            if (existingDetails.getPageURLType() == pageURLType) {
                details = existingDetails;
            }
        }

        return details;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 ===========================================================================
*/
