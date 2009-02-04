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

package com.volantis.mcs.protocols.response.attributes;

import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;

/**
 * Attributes for table element from widgets-response namespace 
 */
public class ResponseTableBodyAttributes extends WidgetAttributes {
    private int totalRowsCount = -1;

    /**
     * Returns the number of total rows, or -1 if it's not known.
     * 
     * @return Returns the totalRowsCount.
     */
    public int getTotalRowsCount() {
        return totalRowsCount;
    }

    /**
     * @param totalRowsCount The totalRowsCount to set.
     */
    public void setTotalRowsCount(int totalRowsCount) {
        this.totalRowsCount = totalRowsCount;
    }
}
