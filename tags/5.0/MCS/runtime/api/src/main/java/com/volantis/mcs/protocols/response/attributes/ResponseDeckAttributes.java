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
 * Attributes for deck element from widgets-response namespace 
 */

public class ResponseDeckAttributes extends WidgetAttributes {
    private int pagesCount = 0;

    /**
     * @return Returns the pagesCount.
     */
    public int getPagesCount() {
        return pagesCount;
    }

    /**
     * @param pagesCount The pagesCount to set.
     */
    public void setTotalPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }
}
