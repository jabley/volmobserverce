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
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.mcs.servlet.CookieJar;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Used to store information regarding the particular users session
 * with the target service.
 *
 * NOTE: This file has been copied from the DSB depot. Any changes required in
 * this file may also need to be made in DSB.
 */
public class TargetHTTPProxySession implements HTTPProxySessionInterface {

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(TargetHTTPProxySession.class);


    /**
     * The value of the path session Id
     */
    private String sessionId = null;

    /**
     * Factory to use when preparing info for sending.
     */
    private static final HTTPFactory factory =
            HTTPFactory.getDefaultInstance();

    /**
     * True if a session has been established. False otherwise.
     */
    private boolean isEstablised = false;

    /**
     * The value of the session entity. This is used to store cookie session
     * information.
     */
    private HTTPMessageEntity sessionCookie = null;

    /**
     * The way that the session id should be encoded within the request
     * e.g path, url or cookie
     */
    private HTTPProxySessionEncodingEnum encoding =
            HTTPProxySessionEncodingEnum.COOKIE;

    /**
     * The handler for notification of state change events.
     */
    private transient SessionStateChangeHandler callbackHandler;

    /**
     * Cookie Jar for returned cookies. May be used if JSESSIONID is found
     * on the URL (indicating that the client may not support cookies).
     */
    private CookieJar cookieJar;

    public void setSessionStateChangeHandler(
            SessionStateChangeHandler newHandler) {
        callbackHandler = newHandler;
    }

    private void fireStateChanged() {
        if (callbackHandler != null) {
            callbackHandler.sessionStateChanged(this);
        }
    }

    // javadoc inherited
    public synchronized String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Id : ");
        sb.append(this.sessionId);
        sb.append(" Enc : ");
        sb.append(this.encoding);
        return sb.toString();
    }

    // javadoc inherited
    public synchronized HTTPProxySessionEncodingEnum getEncoding() {
        return encoding;
    }

    // javadoc inherited
    public synchronized String getSessionID() {
        String result = null;
        if (this.getEncoding().equals(HTTPProxySessionEncodingEnum.COOKIE)
                && getSessionIDCookie() != null) {
            result = getSessionIDCookie().getValue();
        } else if (this.getEncoding().equals(
                HTTPProxySessionEncodingEnum.PATH)) {
            result = getSessionIDPath();
        } else if (this.getEncoding().equals(
                HTTPProxySessionEncodingEnum.PARAM)) {
            result = getSessionIDPath();
        }
        return result;
    }

    /**
     * @param encoding The encoding to set.
     */
    protected synchronized void setEncoding(HTTPProxySessionEncodingEnum encoding) {
        this.encoding = encoding;
        fireStateChanged();
    }

    // javadoc inherited
    public synchronized String getSessionIDParam() {
        return this.sessionId;
    }

    /**
     * Set the param session id.
     * @param param the id to set.
     */
    protected synchronized void setSessionIDParam(String param) {
        this.sessionId = param;
        fireStateChanged();
    }

    public synchronized String getSessionIDPath() {
        return sessionId;
    }

    /**
     * Call to notify the Proxy session that you have sent a sessionID to
     * the target.
     * @param sessionId The sessionId to that has been sent to the target. This
     * can be null.
     */
    protected synchronized void sentSessionIDPath(String sessionId) {
        if (sessionId != null) {
            if (!getEncoding().equals(HTTPProxySessionEncodingEnum.COOKIE)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("path session sent " + sessionId);
                }
            }

        }
    }

    /**
     * Call to notify the Proxy session that you have sent a sessionID to
     * the target.
     * @param sessionId The sessionId to that has been sent to the target. This
     * can be null.
     */
    protected synchronized void sentSessionIDParam(String sessionId) {
        if (sessionId != null) {
            if (!getEncoding().equals(HTTPProxySessionEncodingEnum.COOKIE)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("param session sent " + sessionId);
                }
            }

        }
    }


    // javadoc inherited
    public synchronized void receivedUrl(StringBuffer url) {
        if (url != null) {
            String sPath = JSessionIDHandler.removeSessionInfoFromPath(url);
            if (sPath != null) {
                receivedSessionIDPath(sPath);
            }
            String sParam = JSessionIDHandler.removeSessionInfoFromParams(url);
            if (sPath != null) {
                receivedSessionIDParam(sParam);
            }
        }
    }

    /**
     * Called if a path style session has been encountered.
     * @param session
     */
    protected synchronized void receivedSessionIDPath(String session) {
        if (session != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("path session recieved " + session);
            }
            // if this is really a path based session
            if (isPath() && !isValid()) {
                // if the session is equal to the sent path
                if (!session.equals(getSessionID())) {
                    invalidate(false);
                    this.sessionId = session;
                    fireStateChanged();
                }
            } else {
                // someone is trying to craete a new session
                invalidate(false);
                this.sessionId = session;
                fireStateChanged();
            }
        }
    }

    /**
     * Called if a param style session has been encountered.
     * @param session
     */
    protected synchronized void receivedSessionIDParam(String session) {
        if (session != null) {
            // if this is really a path based session
            if (isParam() && !isValid()) {
                // if the session is equal to the sent path
                if (!session.equals(getSessionID())) {
                    invalidate(false);
                    this.sessionId = session;
                    fireStateChanged();
                }
            } else {
                // someone is trying to craete a new session
                invalidate(false);
                this.sessionId = session;
                fireStateChanged();
            }
        }
    }

    // javadoc inherited
    public synchronized void encodeUrl(StringBuffer url) {
        if (url != null) {
            if (isPath()) {

                if(getSessionIDPath()!=null) {
                    // an established path based session is in use or
                    // cookies are in use but session is not established
                    JSessionIDHandler.
                            addPathSessionID(this.getSessionIDPath(),
                                             url);
                    this.isEstablised = true;
                    // setEncoding will fire the state changed notification
                    this.setEncoding(HTTPProxySessionEncodingEnum.PATH);
                }
            }
            if (isParam()) {

                if(getSessionIDParam()!=null) {
                    JSessionIDHandler.addPathSessionID(this.getSessionIDParam(),
                                                       url);
                    this.isEstablised = true;
                    // setEncoding will fire the state changed notification
                    this.setEncoding(HTTPProxySessionEncodingEnum.PARAM);
                }
            }
        }
    }

    // javadoc inherited
    public synchronized void prepareToSend(StringBuffer url,
                                           HTTPMessageEntities cookies,
                                           HTTPMessageEntities params) {

        if (cookies != null) {

            // Add cookies from the cookie jar if we have no request
            // cookies.  This is the best indicator we have that the
            // client does not support cookies.
            if (cookies.size() == 0) {
                copyCookiesFromCookieJar(cookies);
            }
        }


        if (isCookie() && sessionCookie != null) {
            sentSessionIDCookie(this.sessionCookie);
            cookies.add(this.sessionCookie);
        }

        if(isPath() || isParam() && sessionId !=null && url!=null){
            encodeUrl(url);
        }

        if (isParam() && sessionId != null && params != null) {
            HTTPMessageEntity e = factory.createRequestParameter(
                    JSessionIDHandler.JSESSIONID_STRING);
            e.setValue(getSessionID());
            sentSessionIDParam(getSessionID());
        }
    }

    /**
     * Adds all of the cookies stored in the cookie jar,
     * {@link #cookieJar} into the given <code>cookies</code> that will
     * be sent with the request to the target URL.
     *
     * @param cookies the collection of cookies to add to.
     */
    private void copyCookiesFromCookieJar(HTTPMessageEntities cookies) {
        Iterator cookiesIterator = getCookieJar().iterator();
        while (cookiesIterator.hasNext()) {
            cookies.add((HTTPMessageEntity)cookiesIterator.next());
        }
    }

    // javadoc inherited
    public synchronized HTTPMessageEntity getSessionIDCookie() {
        return copyCookie(this.sessionCookie);
    }

    /**
     * Set the session that has been set to the target cookie.
     * This automatically sets the session encoding
     * to COOKIE and unsets any other session information
     * @param entity the session cookie to set. if null no state change occurs.
     */
    private synchronized void sentSessionIDCookie(HTTPMessageEntity entity) {
        // if a cookie is sent then we have established a session
        if (entity != null) {
            this.sessionCookie = copyCookie(entity);
            this.encoding = HTTPProxySessionEncodingEnum.COOKIE;
            this.isEstablised = true;
            fireStateChanged();
        }

    }

    /**
     * Call this with the session cookie that is recieved from the target.
     * This method updates the sate of this proxy session to reflect the
     * current state of the session.
     * @param entity
     */
    public synchronized void receivedSessionIDCookie(HTTPMessageEntity entity) {
        if (entity != null) {
            if (sessionCookie != null
                    && sessionCookie.getValue() != null) {

                if (!sessionCookie.getValue().equals(entity.getValue())) {
                    sessionCookie = copyCookie(entity);
                    isEstablised = false;
                    // setEncoding will fire state change notification
                    setEncoding(HTTPProxySessionEncodingEnum.COOKIE);
                }
            } else {
                sessionCookie = copyCookie(entity);
                // setEncoding will fire state change notification
                setEncoding(HTTPProxySessionEncodingEnum.COOKIE);
            }
        }
    }

    /**
     * Utility method for copying cookies.
     * @param entity the cookie to copy.
     * @return A copy of the Cookie or null if a problem occurs.
     */
    private static Cookie copyCookie(HTTPMessageEntity entity) {
        Cookie result = null;
        if (entity instanceof Cookie) {
            // create a copy of the Cookie
            Cookie cookie = (Cookie) entity;
            result = HTTPFactory.getDefaultInstance().
                    createCookie(cookie.getName(), cookie.getDomain(),
                                 cookie.getPath());
            result.setComment(cookie.getComment());
            result.setMaxAge(cookie.getMaxAge());
            result.setValue(cookie.getValue());
            result.setSecure(cookie.isSecure());
        }
        return result;
    }

    /**
     * @return the hashcode associated with this object.
     */
    public synchronized int hashCode() {
        int result = 145235;
        String s = getSessionID();
        if (s != null) {
            result = s.hashCode();
        }
        return result;
    }

    /**
     * @param o the object to compare with this
     * @return true if <code>o</code> is an instance of TargetHTTPProxySession
     * and has the same sessionID as this object. The type of the session
     * (cookie, path or param is not taken into account)
     */
    public synchronized boolean equals(Object o) {
        boolean result = false;
        if (o instanceof TargetHTTPProxySession) {
            TargetHTTPProxySession other = (TargetHTTPProxySession) o;
            if (other.getSessionID().equals(getSessionID())) {
                result = true;
            }
        }
        return result;
    }

    // javadoc inherited
    public synchronized boolean isValid() {
        return this.isEstablised;
    }

    // javadoc inherited
    public synchronized void invalidate() {
        invalidate(true);
    }

    /**
     * Mark the session as invalid, optionally notifying any listener of a
     * state change.
     *
     * @param fireStateChanged
     */
    public synchronized void invalidate(boolean fireStateChanged) {
        this.isEstablised = false;
        this.sessionCookie = null;
        this.sessionId = null;
        this.encoding = HTTPProxySessionEncodingEnum.COOKIE;
        if (fireStateChanged) {
            fireStateChanged();
        }
    }

    // javadoc inherited
    public synchronized boolean isCookie() {
        return getEncoding().equals(HTTPProxySessionEncodingEnum.COOKIE);
    }

    // javadoc inherited
    public synchronized boolean isPath() {
        return getEncoding().equals(HTTPProxySessionEncodingEnum.PATH)
                || (isCookie() && !isValid());
    }

    // javadoc inherited
    public synchronized boolean isParam() {
        return getEncoding().equals(HTTPProxySessionEncodingEnum.PARAM)
                || (isPath() && !isValid());
    }

    public synchronized void addCookieToCookieJar(
            HTTPMessageEntity httpMessageEntity) {
        if (cookieJar == null) {
            cookieJar = new CookieJar();
        }
        cookieJar.addCookie(copyCookie(httpMessageEntity));
        fireStateChanged();
    }

    public synchronized CookieJar getCookieJar() {
        if (cookieJar == null) {
            cookieJar = new CookieJar();
        }
        return cookieJar;
    }

}

