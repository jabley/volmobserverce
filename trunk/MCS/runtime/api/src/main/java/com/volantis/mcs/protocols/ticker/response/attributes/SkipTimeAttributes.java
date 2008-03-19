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

package com.volantis.mcs.protocols.ticker.response.attributes;

import com.volantis.mcs.protocols.MCSAttributes;

/**
 * SkipTime response element attributes
 */
public class SkipTimeAttributes extends MCSAttributes {
    private String from;
    private String to;
    /**
     * @return Returns the from.
     */
    public String getFrom() {
        return from;
    }
    /**
     * @param from The from to set.
     */
    public void setFrom(String from) {
        this.from = from;
    }
    /**
     * @return Returns the to.
     */
    public String getTo() {
        return to;
    }
    /**
     * @param to The to to set.
     */
    public void setTo(String to) {
        this.to = to;
    }
}
