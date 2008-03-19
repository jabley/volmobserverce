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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.net.http;

import java.util.ArrayList;
import java.util.List;

/**
 * Type safe enumeration of the HTTP Status codes.
 */
public class HttpStatusCode {

    /**
     * A list of status codes.
     *
     * <p>The object at position <code>i</code> has the status code of <i>.</p>
     */
    private static final List statusCodes = new ArrayList();

    /**
     * A 200 status code.
     */
    public static final HttpStatusCode OK = getStatusCode(200);

    /**
     * A 404 status code.
     */
    public static final HttpStatusCode NOT_FOUND = getStatusCode(404);

    /**
     * A non standard status code that is not returned from the server but
     * only from the client when the server does not respond within the
     * specified time.
     */
    public static final HttpStatusCode RESPONSE_TIMED_OUT = getStatusCode(900);

    private final int statusCode;

    private final String description;

    private HttpStatusCode(int statusCode) {
        this.statusCode = statusCode;
        this.description = "HTTP " + statusCode;
    }

    public int getAsInt() {
        return statusCode;
    }

    public static HttpStatusCode getStatusCode(int statusCode) {
        HttpStatusCode code;
        synchronized (statusCodes) {
            int size = statusCodes.size();
            if (size <= statusCode) {
                // Expand the list so it can contain the code.
                for (int i = size; i <= statusCode; i += 1) {
                    statusCodes.add(null);
                }
                code = null;
            } else {
                code = (HttpStatusCode) statusCodes.get(statusCode);
            }

            if (code == null) {
                code = new HttpStatusCode(statusCode);
                statusCodes.set(statusCode, code);
            }
        }

        return code;
    }

    public String toString() {
        return description;
    }
}
