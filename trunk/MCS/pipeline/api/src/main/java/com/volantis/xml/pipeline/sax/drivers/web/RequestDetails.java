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

/**
 * Encapsulates a request to a {@link PluggableHTTPManager}
 */
public class RequestDetails {
    /**
     * The request url
     */
    private String url;

    /**
     * The context URL
     */
    private String contextURL;

    /**
     * The type of request (i.e POST or GET).
     */
    private HTTPRequestType requestType;

    /**
     * A WebDriverRequest instance that may contain additional
     * request headers, cookies or parameters
     */
    private WebDriverRequest request;

    /**
     * A WebDriverResponse that will be populated with the
     * response details.
     */
    private WebDriverResponse response;

    /**
     * true if an only if redirects should be followed.
     */
    private boolean followRedirects;

    /**
     * The version of HTTP that should be used when sending the
     * request.
     */
    private HTTPVersion version;

    /**
     * Used to preprocess an HTTP request before it is executed
     */
    private HTTPRequestPreprocessor requestPreprocessor;

    /**
     * Used to preprocess headers after an HTTP request is executed but
     * before the Web driver processes the response.
     */
    private HTTPResponsePreprocessor responsePreprocessor;

    /**
     * Used to process the body of the response.
     */
    private HTTPResponseProcessor responseProcessor;

    /**
     *
     * @param url the request URl. Cannot be null
     * @param contextURL the current context or base URL. Can be null.
     * @param requestType the type of request (i.e POST or GET). Cannot be null.
     * @param request a WebDriverRequest instance that may contain additional
     * request headers, cookies or parameters. Can be null.
     * @param response a WebDriverResponse that will be populated with the
     * response details. Can be null.
     * @param followRedirects true if an only if redirects should be followed.
     * @param version the version of HTTP that should be used when sending the
     * request. Cannot be null.
     * @param requestPreprocessor a {@link HTTPRequestPreprocessor} instance
     * that will be used to preprocess the HTTP request before it is executed.
     * Can be null.
     * @param responsePreprocessor a {@link HTTPResponsePreprocessor} instance that
     * will be used to preprocess headers after an HTTP request is executed but
     * before the Web driver processes the response. Can be null.
     * @param responseProcessor will be invoked in order to allow the client
     * to process the body of the response. Cannot be null.
     */
    public RequestDetails(String url,
                          String contextURL,
                          HTTPRequestType requestType,
                          WebDriverRequest request,
                          WebDriverResponse response,
                          boolean followRedirects,
                          HTTPVersion version,
                          HTTPRequestPreprocessor requestPreprocessor,
                          HTTPResponsePreprocessor responsePreprocessor,
                          HTTPResponseProcessor responseProcessor) {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        if (requestType == null) {
            throw new IllegalArgumentException("requestType cannot be null");
        }
        if (version == null) {
            throw new IllegalArgumentException("version cannot be null");
        }
        if (responseProcessor == null) {
            throw new IllegalArgumentException(
                    "responseProcessor cannot be null");
        }
        this.url = url;
        this.contextURL = contextURL;
        this.requestType = requestType;
        this.request = request;
        this.response = response;
        this.followRedirects = followRedirects;
        this.version = version;
        this.requestPreprocessor = requestPreprocessor;
        this.responsePreprocessor = responsePreprocessor;
        this.responseProcessor = responseProcessor;
    }

    // javadoc unnecessary
    public String getContextURL() {
        return contextURL;
    }

    // javadoc unnecessary
    public boolean isFollowRedirects() {
        return followRedirects;
    }

    // javadoc unnecessary
    public HTTPRequestPreprocessor getRequestPreprocessor() {
        return requestPreprocessor;
    }

    // javadoc unnecessary
    public HTTPResponsePreprocessor getResponsePreprocessor() {
        return responsePreprocessor;
    }

    // javadoc unnecessary
    public WebDriverRequest getRequest() {
        return request;
    }

    // javadoc unnecessary
    public HTTPRequestType getRequestType() {
        return requestType;
    }

    // javadoc unnecessary
    public WebDriverResponse getResponse() {
        return response;
    }

    // javadoc unnecessary
    public HTTPResponseProcessor getResponseProcessor() {
        return responseProcessor;
    }

    // javadoc unnecessary
    public String getUrl() {
        return url;
    }

    // javadoc unnecessary
    public HTTPVersion getVersion() {
        return version;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 08-Sep-04	869/1	doug	VBM:2004090707 Add web driver request preprocessing

 07-Sep-04	865/2	doug	VBM:2004090707 Add web driver request preprocessing

 07-Sep-04	858/1	doug	VBM:2004090610 Added preprocessing of response capability

 ===========================================================================
*/
