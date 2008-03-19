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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

import java.util.Map;
import java.util.HashMap;

/**
 * A type safe enumerator for HTTP Request types. There are only two: GET and
 * POST.
 */
public class HTTPRequestType {

    /**
     * A container for all the available HTTPRequestTypes.
     */
    private static Map REQUEST_TYPES;

    static {
        REQUEST_TYPES = new HashMap();
    }

    /**
     * The GET HttpRequestType.
     */
    public static HTTPRequestType GET = new HTTPRequestType("GET");

    /**
     * The POST HttpRequestType.
     */
    public static HTTPRequestType POST = new HTTPRequestType("POST");

    /**
     * The request type name as would be specified in the request header.
     */
    private String name;

    /**
     * The private and only constructor.
     * @param name The HTTP request type name.
     */
    private HTTPRequestType(final String name) {
        this.name = name;
        REQUEST_TYPES.put(name.toLowerCase(), this);
    }

    /**
     * Get the request type name as would be specified in the request header.
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Provide the HTTPRequestType for a specified request name.
     * @param name The name of the required HTTPRequestType. This is not
     * case sensitive.
     * @return The HTTPRequestType with the same name as that provided or null
     * if none exist.
     */
    public static HTTPRequestType httpRequestType(final String name) {
        return name == null ? null:
            ((HTTPRequestType) REQUEST_TYPES.get(name.toLowerCase()));
    }

    // javadoc inherited
    public String toString() {
        return name;
    }
}
