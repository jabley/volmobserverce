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
package com.volantis.shared.net.http;

import com.volantis.shared.time.DateFormats;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Used to parse HTTP date representations.
 *
 * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html">Http Dates</a>
 */
public class HttpDateParser {

    /**
     * Array of accepted date formats.
     */
    private static final DateFormat[] FORMATS =
        new DateFormat[] {
            DateFormats.RFC_1123_GMT.create(),
            DateFormats.RFC_1036_GMT.create(),
            DateFormats.ASC_GMT.create()};

    /**
     * Parses a string and if it is one of the 3 formats that
     * can be used to represent a Http date
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html">Http Dates</a>
     * If the string is of the correct format the corresponding java
     * {@link Date} is returned. If the dateStr is mal formed then null is
     * returned.
     * @param dateStr the string that represents the http date
     * @return a {@link Date} instace or null if the dateStr param is not valid
     */
    public static Date parse(String dateStr) {
        Date result = null;
        for (int i = 0; i < FORMATS.length && result == null; i++) {
            final DateFormat format = FORMATS[i];
            try {
                synchronized(format) {
                    result = format.parse(dateStr);
                }
            } catch (ParseException e) {
                // try the next format
            }
        }
        return result;
    }
}
