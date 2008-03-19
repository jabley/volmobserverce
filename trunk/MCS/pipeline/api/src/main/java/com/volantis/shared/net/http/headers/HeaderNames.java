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
package com.volantis.shared.net.http.headers;

/**
 * Container for http header name constants.
 */
public abstract class HeaderNames {
    /**
     * The accept-language header name.
     */
    public static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";

    /**
     * The content-type header.
      */
    public static final String CONTENT_TYPE_HEADER = "Content-Type";

    /**
     * The if-modified-since header name.
     */
    public static final String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";

    /**
     * The last-modified header.
     */
    public static final String LAST_MODIFIED_HEADER = "Last-Modified";

    /**
     * The user-agent header name.
     */
    public static final String USER_AGENT_HEADER = "User-Agent";

    /**
     * The if-none-match header name.
     */
    public static final String IF_NONE_MATCH_HEADER = "If-None-Match";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Apr-05	7679/1	allan	VBM:2005041320 SmartClient Packager - minimal testing

 ===========================================================================
*/
