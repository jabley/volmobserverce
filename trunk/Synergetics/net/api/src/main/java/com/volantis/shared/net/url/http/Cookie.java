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
package com.volantis.shared.net.url.http;

/**
 * HTTP Cookie interface.
 */
public interface Cookie {

    /**
     * Returns true if the client is sending cookies only over a secure
     * protocol, or false if the client can send cookies using any protocol.
     *
     * @return false if the client can use any standard protocol; otherwise,
     * true
     */
    public boolean isSecure();

    /**
     * Returns the comment describing the purpose of this cookie, or null if the
     * cookie has no comment.
     * @return a String containing the comment, or null if none
     */
    public String getComment();

    /**
     * Returns the path on the server to which the client returns this cookie.
     * The cookie is visible to all subpaths on the server.
     * @return a String specifying a path that contains a servlet name
     */
    public String getPath();

    /**
     * Returns the domain name set for this cookie.
     * The form of the domain name is set by RFC 2109.
     * @return a String containing the domain name
     */
    public String getDomain();

    /**
     * Returns the name of the cookie. The name cannot be changed after creation.
     * @return a String specifying the cookie's name
     */
    public String getName();

    /**
     * Returns the value of the cookie.
     * @return a String containing the cookie's present value
     */
    public String getValue();

    /**
     * Returns the specification version of this cookie. All implementations
     * default to 0 (corresponding to the netscape preliminary specification).
     *
     * @return the version of this cookie
     */
    public int getVersion();


    /**
     * Returns the maximum age of the cookie, specified in seconds,
     * By default, -1 indicating the cookie will persist until client shutdown.
     * @return an integer specifying the maximum age of the cookie in seconds;
     * if negative, means the cookie persists until client shutdown
     */
    public int getMaxAge();
}
