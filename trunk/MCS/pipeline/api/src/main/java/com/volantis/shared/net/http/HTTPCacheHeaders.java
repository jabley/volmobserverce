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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.http;

/**
 * Centralised list of all HTTP1.0 and HTTP1.1 headers related to caching.
 */
public class HTTPCacheHeaders {

    /**
     * Don't let anybody create an instance of this.
     */
    private HTTPCacheHeaders() {
    }

    /*********************************
     * HTTP 1.0 cache headers
     *********************************/

    /**
     * HTTP "Last-Modified" header
     */
    public static final HTTPMessageEntity LAST_MODIFIED =
        HTTPFactory.getDefaultInstance().createHeader("Last-Modified");

    /**
     * HTTP "Expires" header
     */
    public static final HTTPMessageEntity EXPIRES =
        HTTPFactory.getDefaultInstance().createHeader("Expires");

    /**
     * HTTP "Age" header
     */
    public static final HTTPMessageEntity AGE =
        HTTPFactory.getDefaultInstance().createHeader("Age");

    /**
     * HTTP "Date" header
     */
    public static final HTTPMessageEntity DATE =
        HTTPFactory.getDefaultInstance().createHeader("Date");

    /**
     * HTTP "Pragma" header
     */
    public static final HTTPMessageEntity PRAGMA =
        HTTPFactory.getDefaultInstance().createHeader("Pragma");

    /**
     * common directive used with the Pragma and Cache-Control headers.
     */
    public static final String NO_CACHE = "no-cache";

    /*****************************************************
     *  HTTP 1.1 cache headers
     *****************************************************/

    /**
     * Cache-control header.
     */
    public static final HTTPMessageEntity CACHE_CONTROL =
            HTTPFactory.getDefaultInstance().
            createHeader("Cache-Control");

    /**
     * HTTP "Vary" header.
     */
    public static final HTTPMessageEntity VARY_HEADER =
        SimpleHTTPFactory.getDefaultInstance().createHeader("Vary");

    /**
     * HTTP "ETag" header.
     */
    public static final HTTPMessageEntity ETAG_HEADER =
        SimpleHTTPFactory.getDefaultInstance().createHeader("ETag");

    public static final String MAX_AGE = "max-age";

    public static final String PRIVATE = "private";

    public static final String NO_STORE = "no-store";

    public static final String PUBLIC = "public";

    public static final String MUST_REVALIDATE = "must-revalidate";

    public static final String PROXY_REVALIDATE = "proxy-revalidate";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 27-Jul-04	800/1	matthew	VBM:2004071603 add new conditionals to Jigsaw header tokeniser

 ===========================================================================
*/
