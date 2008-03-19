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
package com.volantis.shared.net.url.http;

/**
 * HTTP header interface.
 */
public interface Header {

    /**
     * Returns the name of the header, never returns null.
     *
     * @return the name of the header
     */
    public String getName();

    /**
     * Returns the value of the header. May return null, if the header doesn't
     * have a value.
     *
     * <p>May return comma separated value list.</p>
     *
     * @return the value of the header or null
     */
    public String getValue();
}
