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

package com.volantis.mcs.protocols.gallery.attributes;

import com.volantis.mcs.protocols.widgets.attributes.DetailAttributes;
import com.volantis.mcs.protocols.widgets.attributes.SummaryAttributes;

/**
 * ItemsCount element attributes.
 */
public class ItemAttributes extends BaseGalleryAttributes {
    private SummaryAttributes summaryAttributes;
    private DetailAttributes detailAttributes;
    /**
     * @return Returns the detailAttributes.
     */
    public DetailAttributes getDetailAttributes() {
        return detailAttributes;
    }
    /**
     * @param detailAttributes The detailAttributes to set.
     */
    public void setDetailAttributes(DetailAttributes detailAttributes) {
        this.detailAttributes = detailAttributes;
    }
    /**
     * @return Returns the summaryAttributes.
     */
    public SummaryAttributes getSummaryAttributes() {
        return summaryAttributes;
    }
    /**
     * @param summaryAttributes The summaryAttributes to set.
     */
    public void setSummaryAttributes(SummaryAttributes summaryAttributes) {
        this.summaryAttributes = summaryAttributes;
    }
}
