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

package com.volantis.synergetics.osgi.impl.framework.watcher;

import java.text.DateFormat;
import java.util.Date;

public class Reporter {
    
    public static void report(String message) {
        DateFormat format = DateFormat.getDateTimeInstance(
            DateFormat.LONG, DateFormat.LONG);
        Date date = new Date(System.currentTimeMillis());
        String d = format.format(date);
        System.out.println(d + " " + message);
    }
}
