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
package com.volantis.mcs.servlet.http.proxy;

import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestPreprocessor;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPResponsePreprocessor;

import javax.servlet.http.HttpServletRequest;


/**
 * Default implementation of both the HTTPRequestPreprocessor and
 * HTTPResponsePreprocessor interfaces.
 *
 * NOTE: This file has been copied from the DSB depot. Any changes required in
 * this file may also need to be made in DSB.
 */
public class HTTPRequestResponseHandler
        implements HTTPRequestPreprocessor, HTTPResponsePreprocessor {

    /**
     * The request to obtain session information from.
     */
    private HttpServletRequest servletRequest = null;

    /**
     * The name of the remote project that MCS is providing a proxy for.
     */ 
    private String mcsRemoteProjectName = null;

    /**
     * This should be set to true by the response processor if the response
     * is a redirect to a service target that is not a target of this
     * service.
     */
    private boolean isRedirectServiceRequest = false;

    /**
     * Construct this Handler.
     * @param request
     */
    public HTTPRequestResponseHandler(HttpServletRequest servletRequest,
                                      String mcsRemoteProjectName) {
        
        if ((servletRequest == null)) {
            throw new IllegalArgumentException("Arguments must not be null");
        }
        this.servletRequest = servletRequest;
        this.mcsRemoteProjectName = mcsRemoteProjectName;


    }

    /**
     * Preprocess the request information.
     * @param headers
     * @param cookies
     * @param parameters
     * @param url
     * @return
     */
    public String preprocessRequest(HTTPMessageEntities headers,
                                    HTTPMessageEntities cookies,
                                    HTTPMessageEntities parameters,
                                    String url) {
        final HTTPProxySessionInterface targetSession =
                JSessionIDHandler.getProxySession(
                        servletRequest, mcsRemoteProjectName);
        HTTPProxySessionInterface clientSession =
                JSessionIDHandler.getClientSideSession(
                        servletRequest);

        // delegate to the JSessionIDHandler
        String resultUrl = JSessionIDHandler.
                handleRequest(targetSession,
                              clientSession,
                              url,
                              cookies,
                              parameters,
                              isRedirectServiceRequest);

        this.isRedirectServiceRequest = false;
        return resultUrl;
    }


    /**
     * Preprocess the response informaiton.
     * @param headers
     * @param cookies
     * @param statusCode
     */
    public void preprocessResponse(HTTPMessageEntities headers,
                                   HTTPMessageEntities cookies,
                                   int statusCode) {
        boolean wasDSBRequest = false;

        HTTPProxySessionInterface clientSession =
                JSessionIDHandler.getClientSideSession(
                        servletRequest);

        HTTPProxySessionInterface targetSession =
                JSessionIDHandler.getProxySession(
                        servletRequest, mcsRemoteProjectName);

        JSessionIDHandler.handleResponse(clientSession,
                                         targetSession,
                                         headers,
                                         cookies,
                                         wasDSBRequest);
    }
}
