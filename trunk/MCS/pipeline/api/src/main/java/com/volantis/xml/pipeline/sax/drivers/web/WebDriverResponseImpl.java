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

import com.volantis.shared.net.http.HTTPMessageEntities;

import java.io.InputStream;

/**
 * Default implementation of the WebDriverResponse interface.
 */
public class WebDriverResponseImpl implements WebDriverResponse {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The CookieJar associated with this response.
     */
    private HTTPMessageEntities cookies;

    /**
     * The Headers associated with this response
     */
    private HTTPMessageEntities headers;

    /**
     * The status code associated with this repsonse
     */
    private int statusCode;

    /**
     * The content type associated with this response
     */
    private String contentType;

    /**
     * The ignoredContent associated with this response
     */
    private InputStream ignoredContent;

    /**
     * The http version associated with this response.
     */
    private HTTPVersion httpVersion = null;

    // javadoc inherited
    public void setCookies(HTTPMessageEntities cookies) {
        this.cookies = cookies;
    }

    // javadoc inherited
    public HTTPMessageEntities getCookies() {
        return cookies;
    }

    // javadoc inherited
    public void setHeaders(HTTPMessageEntities headers) {
        this.headers = headers;
    }

    // javadoc inherited
    public HTTPMessageEntities getHeaders() {
        return headers;
    }

    // javadoc inherited
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    // javadoc inherited
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Set the HTTP version that was returned by the server.
     * @param version The version returned by the server.
     */
    public void setHTTPVersion(HTTPVersion version) {
        this.httpVersion = version;
    }

    /**
     * Get the HTTP version returned by the server.
     * @return The HTTP version returned by the server.
     */
    public HTTPVersion getHTTPVersion() {
        return this.httpVersion;
    }

    // javadoc inherited
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    // javadoc inherited
    public String getContentType() {
        return contentType;
    }

    // javadoc inherited
    public void setIgnoredContent(InputStream inputStream) {
        this.ignoredContent = inputStream;
    }

    // javadoc inherited
    public InputStream getIgnoredContent() {
        return ignoredContent;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jul-04	789/1	matthew	VBM:2004071602 Add HTTPVersion to WebDriverResponse

 28-Jul-03	217/1	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 18-Jul-03	220/3	steve	VBM:2003071705 More PossiblyImmutable refactoring

 18-Jul-03	220/1	steve	VBM:2003071705 Renamed mutableCopy to createMutableCopy

 17-Jul-03	215/5	steve	VBM:2003070806 Removed throws ImmutableObjectException declarations

 17-Jul-03	215/3	steve	VBM:2003070806 Implement web driver

 17-Jul-03	215/1	steve	VBM:2003070806 Implement we driver

 17-Jul-03	200/1	doug	VBM:2003070806 Introduced the Web Driver support classes

 ===========================================================================
*/
