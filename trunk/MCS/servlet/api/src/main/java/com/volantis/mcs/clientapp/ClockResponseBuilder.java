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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.clientapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;

class ClockResponseBuilder extends SampleAppResponseBuilder {
    
    /** 
     * Source of data to be used by this builder 
     */
    private ClockDataFeed feed;

    public ClockResponseBuilder(ClockDataFeed feed) {
        this.feed = feed;
    }

    protected void writeBodyContents(Map params, PrintWriter out) throws IOException {
        out.print("<response:clock>");
        Calendar c = feed.getCalendar();
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)-1);
        StringBuffer sb = new StringBuffer();
        sb.append("[")
        .append(c.get(Calendar.YEAR)).append(",")
        .append(c.get(Calendar.MONTH)).append(",")
        .append(c.get(Calendar.DAY_OF_MONTH)).append(",")
        .append(c.get(Calendar.HOUR_OF_DAY)).append(",")
        .append(c.get(Calendar.MINUTE)).append(",")
        .append(c.get(Calendar.SECOND)).append(",");
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)+1);        
        sb.append(c.getTimeInMillis())
        .append("]");
        out.print(sb.toString());
        out.println("</response:clock>");
    }
}
