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
package com.volantis.shared.net.impl.url.http;

import com.volantis.shared.net.url.http.Header;

/**
 * Simple Header implementation to create unmodifiable headers.
 */
public class HeaderImpl implements Header {
    /**
     * The name of the header.
     */
    private final String name;

    /**
     * The value of the header
     */
    private final String value;

    /**
     * Creates a new Header implementation with the specified header name and
     * value.
     *
     * @param name the name of the header
     * @param value the value of the header
     */
    public HeaderImpl(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    // javadoc inherited
    public String getName() {
        return name;
    }

    // javadoc inherited
    public String getValue() {
        return value;
    }
}
