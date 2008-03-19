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

import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemsCount element attributes.
 */
public class ItemsAttributes extends BaseGalleryAttributes {
    private String count;
    private LoadAttributes loadAttributes;
    private final List itemWidgetIds = new ArrayList();

    public LoadAttributes getLoadAttributes() {
        return loadAttributes;
    }

    public void setLoadAttributes(LoadAttributes loadAttributes) {
        this.loadAttributes = loadAttributes;
    }

    /**
     * @return Returns the count.
     */
    public String getCount() {
        return count;
    }

    /**
     * @param count The count to set.
     */
    public void setCount(String count) {
        this.count = count;
    }

    /**
     * @return Returns the itemWidgetIds.
     */
    public List getItemWidgetIds() {
        return itemWidgetIds;
    }
}
