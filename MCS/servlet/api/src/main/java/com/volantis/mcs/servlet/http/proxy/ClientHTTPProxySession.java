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
package com.volantis.mcs.servlet.http.proxy;

import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.shared.net.http.HTTPMessageEntities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provide HTTPProxySession like behaviour from a real HttpSession.
 * The state of this object is entirely determined by the HttpServlet
 * session.
 *
 * NOTE: This file has been copied from the DSB depot. Any changes required in
 * this file may also need to be made in DSB.
 */
public class ClientHTTPProxySession implements HTTPProxySessionInterface {

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(
            ClientHTTPProxySession.class);


    private boolean isValid = false;

    private boolean isFromUrl = false;

    private boolean isFromCookie = false;

    private String id;

    /**
     * The way that the session id should be encoded within the request
     * e.g path, url or cookie
     */
    private HTTPProxySessionEncodingEnum encoding =
            HTTPProxySessionEncodingEnum.COOKIE;

    /**
     * Factory to use when preparing info for sending.
     */
    private static final HTTPFactory factory =
            HTTPFactory.getDefaultInstance();


    public ClientHTTPProxySession(HttpServletRequest request) {
        // get the session (create if one does not exist)
        HttpSession session = request.getSession(true);
        StringBuffer url = request.getRequestURL();

        this.isValid = request.isRequestedSessionIdValid();
        this.isFromUrl = request.isRequestedSessionIdFromUrl();
        this.isFromCookie = request.isRequestedSessionIdFromCookie();
        this.id = session.getId();
    }

    /**
     * Does nothing.
     * rest of javadoc inherited
     */
    public void receivedSessionIDCookie(HTTPMessageEntity entity) {

    }

    // javadoc inherited
    public synchronized String getSessionIDPath() {
        return id;
    }

    // javadoc inherited
    public void encodeUrl(StringBuffer url) {
        if (url != null) {
            if (isPath()) {
                JSessionIDHandler.addPathSessionID(getSessionID(), url);
            }
            if (isParam()) {
                JSessionIDHandler.addParamSessionID(getSessionID(), url);
            }
        }
    }

    /**
     * creates a mock cookie to be used.
     * @return
     */
    public synchronized HTTPMessageEntity getSessionIDCookie() {
        HTTPMessageEntity cookie = null;
        if (getSessionID() != null) {
            //@todo add domain, path and security to this.
            cookie = factory
                    .createCookie(JSessionIDHandler.JSESSIONID_STRING, "", "");
            cookie.setValue(getSessionID());
        }
        return cookie;
    }

    // javadoc inherited
    public HTTPProxySessionEncodingEnum getEncoding() {
        return null;
    }

    // javadoc inherited
    public String getSessionID() {
        return id;
    }

    // javadoc inherited
    public synchronized String getSessionIDParam() {
        return this.id;
    }

    // do nothing.
    public void invalidate() {
    }

    // javadoc inherited
    public boolean isCookie() {
        return (isValid() && isFromCookie) || !isValid();
    }

    // javadoc inherited
    public boolean isPath() {
        StringBuffer bufferUrl = new StringBuffer(id);

        return (isValid() && isFromUrl)
                || !isValid();
    }

    // params are not supported.
    public boolean isParam() {
        return false;
    }


    // javadoc inherited
    public boolean isValid() {
        return isValid;
    }

    /**
     * write out the session data.
     * @param url
     * @param cookies
     * @param params
     */
    public void prepareToSend(StringBuffer url,
                              HTTPMessageEntities cookies,
                              HTTPMessageEntities params) {
        if (isCookie() && cookies != null) {
            HTTPMessageEntity cookie = getSessionIDCookie();
            if (cookie != null) {
                cookies.add(cookie);
            }
        }
        if (isPath() || isParam() && id != null && url != null) {
            encodeUrl(url);
        }
        if (isParam() && id != null && params != null) {
            HTTPMessageEntity e = factory.createRequestParameter(
                    JSessionIDHandler.JSESSIONID_STRING);
            e.setValue(getSessionID());
        }

    }

    // do nothing
    public void receivedUrl(StringBuffer url) {
    }

}
