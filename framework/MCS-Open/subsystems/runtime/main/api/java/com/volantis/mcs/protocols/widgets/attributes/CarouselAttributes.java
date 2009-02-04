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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.attributes;

import com.volantis.mcs.protocols.ticker.attributes.FeedAttributes;

/**
 * Holds properties specific to CarouselElement.
 */
public class CarouselAttributes extends WidgetAttributes {
    
    /**
     * Holds refresh attributes.
     */
    private RefreshAttributes refreshAttributes;
    
    /**
     * Holds Feed attributes.
     */
    private FeedAttributes feedAttributes;

    /**
     * @return Returns the refreshAttributes.
     */
    public RefreshAttributes getRefreshAttributes() {
        return refreshAttributes;
    }

    /**
     * @param refreshAttributes The refreshAttributes to set.
     */
    public void setRefreshAttributes(RefreshAttributes refreshAttributes) {
        this.refreshAttributes = refreshAttributes;
    }

    /**
     * @return Returns the feedAttributes.
     */
    public FeedAttributes getFeedAttributes() {
        return feedAttributes;
    }

    /**
     * @param feedAttributes The feedAttributes to set.
     */
    public void setFeedAttributes(FeedAttributes feedAttributes) {
        this.feedAttributes = feedAttributes;
    }
}
