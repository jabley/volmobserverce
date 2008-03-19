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

import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.shared.net.http.HTTPMessageEntities;

import java.io.Serializable;

/**
 * NOTE: This file has been copied from the DSB depot. Any changes required in
 * this file may also need to be made in DSB.
 */
public interface HTTPProxySessionInterface extends Serializable {

    /**
     * @return Returns the encoding.
     */
    HTTPProxySessionEncodingEnum getEncoding();

    /**
     * Return a string representing the session id.
     * @return the session id as a string. or null if one does not exist.
     */
    String getSessionID();

    /**
     * @return the param session id
     */
    String getSessionIDParam();

    /**
     * @return Returns the path sessionId.
     */
    String getSessionIDPath();

    /**
     * call with the information that is recieved from the target. This updates
     * the internal state of the session to reflect invalidation or
     * establishment of the session. It also removes path and param style session
     * information.
     * @param url the url that has been received
     */
    void receivedUrl(StringBuffer url);

    /**
     * Call this with the session cookie that is recieved from the target.
     * This method updates the sate of this proxy session to reflect the
     * current state of the session.
     * @param entity
     */
     void receivedSessionIDCookie(HTTPMessageEntity entity);

    /**
     * Encode the URL parameter with path style session encoding. This will
     * add the session to the url if the session is cookie based but not
     * established or if the session is established and path based.
     * The url should not have any session information in it.
     *
     * @param url the URL to encode. This is modified by the method)
     */
    void encodeUrl(StringBuffer url);

    /**
     * Call this mehtod to prepare to send information to the target.
     *
     * @param url
     * @param cookies
     * @param params
     */
    void prepareToSend(StringBuffer url,
                       HTTPMessageEntities cookies,
                       HTTPMessageEntities params);

    /**
     * Get the session cookie.
     * @return a copy of the session cookie or null if one has not been set.
     */
    HTTPMessageEntity getSessionIDCookie();

    /**
     * Returns true if the session is established. False otherwise.
     */
    boolean isValid();

    /**
     * invalidate this session proxy.
     */
    void invalidate();

    /**
     * Return true if cookie sessions are enabled.
     */
    boolean isCookie();

    /**
     * Return true if path encoding is in use. This can occur if cookie
     * encoding is actually set but the session is not yet established.
     */
    boolean isPath();

    /**
     * Returns true if param encoding may be in use. This can occur if
     * param encoding is set or if (isPath() && !isValid()). This allows
     * param encoding to be used if path or cookie encoding is set but the
     * session is not established.
     */
    boolean isParam();

}
