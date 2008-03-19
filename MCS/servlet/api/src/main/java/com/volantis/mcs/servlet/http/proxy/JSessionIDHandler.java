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
import com.volantis.shared.net.http.cookies.Cookie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * NOTE: This file has been copied from the DSB depot. Any changes required in
 * this file may also need to be made in DSB.
 */
public class JSessionIDHandler {

    /**
     * Logger.
     */
    private static final Log LOG = LogFactory.getLog(JSessionIDHandler.class);

    /**
     * The HTTPFactory to use
     */
    public static HTTPFactory factory = HTTPFactory.getDefaultInstance();

    /**
     * Session id for cookie name
     */
    public static final String JSESSIONID_STRING = "JSESSIONID";


    /**
     * Prefix for key used to access information about the session between MCS
     * and the target.
     */
    public static final String TARGET_SESSION = "V-MCS-JSESSIONID-";

    /**
     * Used to find path style jsession id's in StringBuffer urls
     */
    public static final String PATH_JSESSIONID_STRING =
            ";" + JSESSIONID_STRING.toLowerCase() + "=";

    /**
     * Used to find parameter style jsessions in StringBuffer urls
     */
    public static final String PARAM_JSESSIONID_STRING =
            JSESSIONID_STRING.toLowerCase() + "=";

    /**
     * Used to find session Parameters
     */
    public static final HTTPMessageEntity JSESSIONID_PARAM =
            factory.createRequestParameter(JSESSIONID_STRING);

    /**
     * Used to find location headers
     */
    public static final HTTPMessageEntity LOCATION_HEADER =
            factory.createHeader("Location");


    /**
     * Add the specified session string to the url. If url is null or
     * session is null then no changes are made.
     *
     * @param session the session string to add. Can be null.
     * @param url the url to which the session should be added. Can be null.
     */
    public static void addPathSessionID(String session, StringBuffer url) {
        if (session != null && url != null) {
            int start = StringBufferUtils.indexOf(url, '?', 0);
            if (start >= 0) {
                String insert = PATH_JSESSIONID_STRING + session;
                url.replace(start, start, insert);
            } else { // no params found so append to end
                url.append(PATH_JSESSIONID_STRING + session);
            }
        }
    }

    /**
     * Add to the url, as a parameter, the specified session information.
     * @param session the session to add
     * @param url the url to add the session info to.
     */
    public static void addParamSessionID(String session, StringBuffer url) {
        if (session != null && url != null) {
            int start = StringBufferUtils.indexOf(url, '?', 0);
            if (start >= 0) {
                url.append("&" + PARAM_JSESSIONID_STRING + session);
            } else { // no params found so append to end
                url.append("?" + PATH_JSESSIONID_STRING + session);
            }
        }
    }


    /**
     * Utility method for handling the response of a webd request.
     * @param clientProxy The proxy to use for client->mcs session
     * @param targetProxy The proxy to use for mcs->target session
     * @param responseHeaders the headers to process.
     * @param responseCookies the cookies to process.
     * @param wasMCSRedirect true if the response is a redirect to an mcs
     * service.
     */
    public static void handleResponse(
            HTTPProxySessionInterface clientProxy,
            HTTPProxySessionInterface targetProxy,
            HTTPMessageEntities responseHeaders,
            HTTPMessageEntities responseCookies,
            boolean wasMCSRedirect) {

        HTTPProxySessionInterface tmpProxy = null;
        if (!wasMCSRedirect) { // this response is not from a known mcs site.
            tmpProxy = targetProxy;
        } else { // this response is from a known mcs site
            tmpProxy = clientProxy;
        }


        // search for Location headers.
        HTTPMessageEntity[] locations =
                responseHeaders.retrieve(LOCATION_HEADER.getIdentity());
        // loop backwards to ensure the first header is the one we keep
        // if found this is a redirect.
        if (locations != null && locations.length > 1) {
            String url = locations[0].getValue();
            if (url != null) {
                StringBuffer urlBuffer = new StringBuffer(url);
                tmpProxy.receivedUrl(urlBuffer);
            }
        }

        // remove any response cookies
        List cookies = removeJSessionIDCookies(responseCookies);
        // update the target proxy session with the first of the
        // session cookies
        if (cookies.size() > 0)
            tmpProxy.receivedSessionIDCookie(
                    (HTTPMessageEntity) cookies.get(0));

        // Store remaining cookies in the CookieJar for this target.
        Iterator cookiesIterator = responseCookies.iterator();

        while (cookiesIterator.hasNext()) {
                ((TargetHTTPProxySession)targetProxy).addCookieToCookieJar(
                        (HTTPMessageEntity)cookiesIterator.next());
        }



    }


    /**
     * Utility method to handle preprocesing of request information. This
     * method removes any param or path style session information from
     * the requestUrl. It then appends a path style session information to
     * the url (if the
     * @param proxy The  target HTTPSession proxy from which session informaion will
     * be obtained.
     * @param clientProxy the client side proxy session.
     * @param requestUrl the url that will be used for the request.
     * @param requestCookies the cookies for the request.
     * @param requestParams the parameters for the request.
     * @param isMCSRedirect true if the request is being made as the result of
     * and internal redirect (blind/intelligent) to a dsb service target
     *
     * @return the url to use in the request.
     */
    public static String handleRequest(
            HTTPProxySessionInterface proxy,
            HTTPProxySessionInterface clientProxy,
            String requestUrl,
            HTTPMessageEntities requestCookies,
            HTTPMessageEntities requestParams,
            boolean isMCSRedirect) {

        HTTPProxySessionInterface tmpProxy = null;
        // redirect to an mcs service is occuring so don't remove JSession
        // information from path or params. The correct info will have been
        // added by the handleResponse method.
        if (!isMCSRedirect) {
            tmpProxy = clientProxy;
        } else {
            tmpProxy = proxy;
        }

        StringBuffer bufferUrl = new StringBuffer(requestUrl);
        // remove path style session from requestUrl (if any)
        // redirects might caused some to be appended
        removeSessionInfoFromPath(bufferUrl);
        // for safety we remove any param session information.
        // although there should not be any.
        removeSessionInfoFromParams(bufferUrl);

        removeJSessionIDCookies(requestCookies);
        removeJSessionIDParams(requestParams);
        proxy.prepareToSend(bufferUrl, requestCookies, requestParams);
        // strip out the params we added to the url as the
        // PluggableHTTPManager does not like them there. It only expects
        // them in the requestParams entities object
        JSessionIDHandler.removeSessionInfoFromParams(bufferUrl);
        requestUrl = bufferUrl.toString();

        return requestUrl;
    }


    /**
     * Convert a HTTPMessageEntity cookie into a javax.servlet.http.Cookie
     * @return a servlet cookie. or null if there was a problem.
     */
    public static javax.servlet.http.Cookie convertCookie(
            HTTPMessageEntity entity) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Converting Volantis cookie to Servlet cookie");
        }
        javax.servlet.http.Cookie result = null;

        if (entity instanceof Cookie) {
            Cookie c = (Cookie) entity;
            result = new javax.servlet.http.Cookie(c.getName(), c.getValue());
            result.setDomain(c.getDomain());
            result.setPath(c.getPath());
            result.setMaxAge(c.getMaxAge());
            result.setComment(c.getComment());
            result.setSecure(c.isSecure());
        }
        if (LOG.isDebugEnabled()) {
            if (result == null) {
                LOG.debug("Converting Volantis cookie to Servlet " +
                          "cookie failed");
            }
        }
        return result;
    }

    /**
     * Convert a javax.servlet.http.Cookie cookie into a HTTPMessageEntity
     * @return a HTTPMessageEntityCookie. or null if there was a problem.
     */
    public static HTTPMessageEntity convertCookie(
            javax.servlet.http.Cookie cookie) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Converting Volantis cookie to Servlet cookie");
        }
        HTTPMessageEntity result = null;

        if (cookie != null) {
            Cookie c = factory.createCookie(
                    cookie.getName(), cookie.getDomain(), cookie.getPath());
            c.setComment(cookie.getComment());
            c.setSecure(cookie.getSecure());
            c.setMaxAge(cookie.getMaxAge());
            c.setValue(cookie.getValue());

        }
        if (LOG.isDebugEnabled()) {
            if (result == null) {
                LOG.debug("Converting Servlet cookie to Volantis " +
                          "cookie failed");
            }
        }
        return result;
    }

    /**
      * Get the proxy session associated with this session. If a session does
      * not exit then create one, add a proxy session to it and return the
      * result.
      *
      * <p>The proxy sessions are stored as separate atttributes in the session,
      * using a key which contains the service ID. This is to provide fine
      * granularity for session persistence.</p>
      *
      * @param request the request from which to obtain the session
      * @return a proxy session object containing information about the
      * mcs->target session.
      */
    public synchronized static HTTPProxySessionInterface getProxySession(
            HttpServletRequest request, String serviceId) {

        HTTPProxySessionInterface result = null;
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        final HttpSession session = request.getSession(true);

        String sessionKey;


        sessionKey = TARGET_SESSION + serviceId;


        // these should be changed to get/setAttribute when support for
        // servlet 2.2 is dropped
        Object o = session.getValue(sessionKey);
        if (o != null) {
            result = (HTTPProxySessionInterface) o;
        } else {
            result = new TargetHTTPProxySession();
            session.putValue(sessionKey, result);
        }

        // Register a session state change handler that sets the session
        // attribute when the state changes.
        if (result instanceof TargetHTTPProxySession) {
            final String key = sessionKey;
            SessionStateChangeHandler handler =
                    new SessionStateChangeHandler() {
                        // Javadoc inherited
                        public void sessionStateChanged(Object changedObject) {
                            session.putValue(key, changedObject);
                        }
                    };
            ((TargetHTTPProxySession) result).
                    setSessionStateChangeHandler(handler);
        }

        return result;
    }

    /**
     * Return a proxy session that represents the client->mcs session information.
     * This attempts to determine the type of the client->mcs session.
     * It is currently implemented dynamically but should probably be stored in
     * the client->mcs session for better performance. Howver doing that would
     * require consideration as to how and when it is updated.
     *
     * @param request the servlet request from which the session should be
     * obtained.
     * @return a proxy session that reflects the servlet
     * session (client to mcs)
     */
    public synchronized static HTTPProxySessionInterface getClientSideSession(
            HttpServletRequest request) {
        HTTPProxySessionInterface proxy = new ClientHTTPProxySession(request);


        return proxy;

    }

    /**
     * Get the domain from the provided fully qualified url
     * @param url the URL fro which to obtain the domain.
     * @return the domain.
     * @throws IllegalArgumentException on error.
     */
    protected static final String getDomain(StringBuffer url) {


        String result = null;
        // find the start of the hostname
        int first = StringBufferUtils.indexOf(url, "://", 0);
        if (first >= 0) {
            // find the '/' at the end of the host:port bit.
            int second = StringBufferUtils.indexOf(url, "/", first + 3);
            if (second >= 3) {
                result = url.substring(first + 3, first + 3 + second);
                // now check to see if there is a port number and remove it
                // if there is
                first = result.indexOf(':');
                if (first > 0) {
                    result = result.substring(0, first);
                }
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Cound not parse domain from" +
                                               "url");
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Got domain: " + result + " from url " + url);
            }
        }
        return result;
    }

    /**
     * Remove JSessionID cookies from the entities object.
     * @param cookies the collection of cookies from which jsessionid's should
     * be removed.
     * @return A list of the HTTPMessageEntities that have been removed. An
     * empty list if no cookies were removed.
     */
    public static List removeJSessionIDCookies(HTTPMessageEntities cookies) {

        List results = new ArrayList();
        Iterator entities = cookies.iterator();
        while (entities.hasNext()) {
            HTTPMessageEntity entity = (HTTPMessageEntity) entities.next();
            if (entity.getName().equalsIgnoreCase(JSESSIONID_STRING)) {
                results.add(entity);
            }
        }

        // iterate over the entities identified for removal and remove them
        // from the cookie set.
        Iterator removed = results.iterator();
        while(removed.hasNext()) {
            cookies.remove(((HTTPMessageEntity)removed.next()).getIdentity());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Removed " + results.size() + " JSESSIONID cookies");
        }
        return results;
    }


    /**
     * Remove the JSessionID parameters from the Entities object.
     * @param entities the parameters to from which to remove the JSessionID's
     * @return the removed parameters.
     */
    public static HTTPMessageEntity[] removeJSessionIDParams(
            HTTPMessageEntities entities) {
        return entities.remove(JSESSIONID_PARAM.getIdentity());
    }

    /**
     * Remove session information from url paramaters.
     * @param url the url from which the params should be removed.
     * @return the last session id that was removed.
     */
    public static String removeSessionInfoFromParams(StringBuffer url) {
        String result = null;

        boolean finished = false;
        // search for start of params.
        int start = StringBufferUtils.indexOf(url, '?', 0);
        // loop over params.
        if (start >= 0) {
            while (!finished) {
                int offsetFromStart = StringBufferUtils.
                        indexOfIgnoreCase(url, PARAM_JSESSIONID_STRING, start);
                if (offsetFromStart < 1) {
                    // cant find any more "Jsessionid=" tags
                    finished = true;
                } else {
                    // find the end of the JSessionID
                    int end = StringBufferUtils.
                            indexOf(url, '&', start + offsetFromStart);

                    // no subsequent param so truncate
                    if (end < 0) {
                        result = url.substring(start + offsetFromStart
                                               + PARAM_JSESSIONID_STRING.length(),
                                               url.length());
                        url.setLength(start + offsetFromStart - 1);
                    } else { //remove by replacement
                        result = url.substring(start + offsetFromStart
                                               + PARAM_JSESSIONID_STRING.length(),
                                               start + offsetFromStart + end);
                        url.replace(start + offsetFromStart,
                                    start + offsetFromStart + end + 1, "");
                    }
                }

            }
        }
        return result;
    }

    /**
     * Remove path style JSessionid information from the supplied url.
     *
     * @param url the url to process.
     * @return the session information, null if no session
     * information was found or any other problem was encountered.
     */
    public static String removeSessionInfoFromPath(StringBuffer url) {

        String result = null;

        // find start of session info (if any exists)
        int start = StringBufferUtils.indexOf(url, ';', 0);

        if (start >= 0) {// found a potential match
            // check match is real
            if (StringBufferUtils.indexOfIgnoreCase(
                    url, PATH_JSESSIONID_STRING, start) == 0) {

                int end = StringBufferUtils.indexOf(url, '?', start);
                // no parameters exist
                if (end < 0) {
                    // remove the session info by truncation
                    // remove the session information
                    result = url.substring(start +
                                           PATH_JSESSIONID_STRING.length(),
                                           url.length());
                    url.setLength(start);
                } else {
                    // remove the session information
                    result = url.substring(start +
                                           PATH_JSESSIONID_STRING.length(),
                                           start + end);
                    url.replace(start, start + end, "");
                }
            }
        }

        return result;
    }
}


