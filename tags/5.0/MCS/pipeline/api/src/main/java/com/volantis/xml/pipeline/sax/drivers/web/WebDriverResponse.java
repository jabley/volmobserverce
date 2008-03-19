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
 * This interface provides access to information from the response returned to
 * the web driver by the http server that it is communicating with.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and source
 * levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface WebDriverResponse {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Get the cookies jar into which the web driver should add cookies returned
     * by the server.
     * @param cookies The CookieJar that the web driver should use.
     */
    public void setCookies(HTTPMessageEntities cookies);

    /**
     * Get the cookies jar into which the web driver should add cookies returned
     * by the server.
     * @return The CookieJar that the web driver should use.
     */
    public HTTPMessageEntities getCookies();

    /**
     * Set the headers into which the web driver should add headers returned by
     * the server.
     * @param headers The headers that the web driver should use.
     */
    public void setHeaders(HTTPMessageEntities headers);

    /**
     * Get the headers into which the web driver should add headers returned
     * by the server.
     * @return The headers that the web driver should use.
     */
    public HTTPMessageEntities getHeaders();

    /**
     * Set the status code that was returned by the server.
     * @param statusCode The status code that was returned by the server.
     */
    public void setStatusCode(int statusCode);

    /**
     * Get the status code returned by the server.
     * @return The status code returned by the server.
     */
    public int getStatusCode();

    /**
     * Set the HTTP version that was returned by the server.
     * @param version The version returned by the server.
     */
    public void setHTTPVersion(HTTPVersion version);

    /**
     * Get the HTTP version returned by the server.
     * @return The HTTP version returned by the server.
     */
    public HTTPVersion getHTTPVersion();

    /**
     * Set the content type of the content received by the web driver.
     * @param contentType The content type of the returned content.
     */
    public void setContentType(String contentType);

    /**
     * Get the content type of the content received by the web driver..
     * @return The content type of the returned content.
     */
    public String getContentType();

    /**
     * Set the content that was ignored by the web driver.
     * @param inputStream A stream that can be used to retrieve the content.
     */
    public void setIgnoredContent(InputStream inputStream);

    /**
     * Get the content that was ignored by the web driver.
     * @return The content that was ignored by the web driver.
     */
    public InputStream getIgnoredContent();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jul-04	789/1	matthew	VBM:2004071602 Add HTTPVersion to WebDriverResponse

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 28-Jul-03	217/3	allan	VBM:2003071702 Intermediate group level changes

 28-Jul-03	217/1	allan	VBM:2003071702 Renamed and repacked set classes to http classes

 23-Jul-03	230/1	allan	VBM:2003072101 Restructure cookies, headers and request parameters and their containers. Remove PossiblyImmutable and HeaderConversions. Rename HttpFactory to HTTPFactory.

 17-Jul-03	215/3	steve	VBM:2003070806 Removed throws ImmutableObjectException declarations

 17-Jul-03	215/1	steve	VBM:2003070806 Implement we driver

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
